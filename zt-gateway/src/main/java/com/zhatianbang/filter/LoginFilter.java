package com.zhatianbang.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 功能描述：模拟登录过滤器,继承ZuulFilter 重写方法
 * Created by lenovo on 2019/3/13.
 */
@Component
public class LoginFilter extends ZuulFilter {

    /**
     * 过滤器类型 PRE_TYPE：前置过滤器
     * @return
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**
     * 过滤器顺序，越小越先执行
     * @return
     */
    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * 过滤器是否生效
     * @return
     */
    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        // 获取请求的路由
        String uri = request.getRequestURI();
        // 获取请求的绝对路径
        StringBuffer url = request.getRequestURL();
        //如果是下单路由请求 返回true 执行拦截
        if("saveOrder".equals(uri)){
            return true;
        }
        return false;
    }

    /**
     * 过滤器执行业务逻辑
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //JWT
        RequestContext requestContext =  RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();


        //获取请求头token对象，模拟token信息放在请求头中传递过来
        String token = request.getHeader("token");

        if(StringUtils.isBlank((token))){
            // 获取请求参数token对象，模拟token信息放在请求参数中传递到后台
            token  = request.getParameter("token");
        }


        //登录校验逻辑  根据公司情况自定义 JWT
        if (StringUtils.isBlank(token)) {
            requestContext.setSendZuulResponse(false);
            // 向前端返回状态401码,非法请求
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }

        return null;
    }
}
