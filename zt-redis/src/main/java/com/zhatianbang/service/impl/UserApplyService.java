package com.zhatianbang.service.impl;

import com.zhatianbang.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by lenovo on 2019/4/18.
 */
@Service
@CacheConfig(cacheNames="userInfoCache") // 本类内方法指定使用缓存时，默认的名称就是userInfoCache
@Transactional(propagation= Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class UserApplyService implements com.zhatianbang.service.UserApplyService {

    @Autowired
    UserMapper userMapper;

    @Override
    @Nullable
    @Cacheable(value = "UserInfoList", keyGenerator = "simpleKeyGenerator") // @Cacheable 会先查询缓存，如果缓存中存在，则不执行方法
    public Map<String,Object> getApplyInfo(String apply_no){

        Map<String, Object> applyInfo = userMapper.getApplyInfo(apply_no);
        return applyInfo;
    };

    @Nullable
    @Cacheable(key="#p0") // @Cacheable 会先查询缓存，如果缓存中存在，则不执行方法
    public Map<String,Object> findById(String id){
        System.err.println("根据id=" + id +"获取用户对象，从数据库中获取");
        Assert.notNull(id,"id不用为空");
        return this.userMapper.getUserByUsername(id);
    }

    @Nullable
    @Cacheable(value = "UserInfoList", keyGenerator = "simpleKeyGenerator")
    public void getAllInfo(String id,String apply_no){
        System.err.println("根据id=" + id +"获取用户对象，从数据库中获取");
        Assert.notNull(id,"id不用为空");
        Map<String, Object> applyInfo = userMapper.getApplyInfo(apply_no);
        Map<String, Object> userByUsername = userMapper.getUserByUsername(id);
        System.out.println(userByUsername);
        System.out.println(applyInfo);
    }
}
