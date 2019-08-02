/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.zt.api.gateway.demo.util;

import com.alibaba.fastjson.JSON;
import com.zt.api.gateway.demo.Response;
import com.zt.api.gateway.demo.constant.*;
import com.zt.api.gateway.demo.constant.Constants;
import com.zt.api.gateway.demo.constant.HttpHeader;
import com.zt.api.gateway.demo.constant.SystemHeader;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Http工具类
 */
public class HttpUtil {






    /**
     * HTTP GET
     * @param host
     * @param path
     * @param connectTimeout
     * @param headers
     * @param querys
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static Response httpGet(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {
        headers = initialBasicHeader(HttpMethod.GET, path, headers, querys, null, signHeaderPrefixList, appKey, appSecret);

        HttpClient httpClient = wrapClient(host);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

        HttpGet get = new HttpGet(initUrl(host, path, querys));

        for (Map.Entry<String, String> e : headers.entrySet()) {
            get.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        return convert(httpClient.execute(get));
    }

    /**
     * HTTP POST表单
     * @param host
     * @param path
     * @param connectTimeout
     * @param headers
     * @param querys
     * @param bodys
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static Response httpPost(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, Map<String, String> bodys, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }

        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");

        headers = initialBasicHeader(HttpMethod.POST, path, headers, querys, bodys, signHeaderPrefixList, appKey, appSecret);

        HttpClient httpClient = wrapClient(host);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

        HttpPost post = new HttpPost(initUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        UrlEncodedFormEntity formEntity = buildFormEntity(bodys);
        if (formEntity != null) {
            post.setEntity(formEntity);
        }

        return convert(httpClient.execute(post));
    }

    /**
     * Http POST 字符串
     * @param host
     * @param path
     * @param connectTimeout
     * @param headers
     * @param querys
     * @param body
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static Response httpPost(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, String body, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {
    	headers = initialBasicHeader(HttpMethod.POST, path, headers, querys, null, signHeaderPrefixList, appKey, appSecret);

    	HttpClient httpClient = wrapClient(host);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

        HttpPost post = new HttpPost(initUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (StringUtils.isNotBlank(body)) {
            post.setEntity(new StringEntity(body, Constants.ENCODING));

        }

        return convert(httpClient.execute(post));
    }

    /**
     * HTTP POST 字节数组
     * @param host
     * @param path
     * @param connectTimeout
     * @param headers
     * @param querys
     * @param bodys
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static Response httpPost(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, byte[] bodys, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {
    	headers = initialBasicHeader(HttpMethod.POST, path, headers, querys, null, signHeaderPrefixList, appKey, appSecret);

    	HttpClient httpClient = wrapClient(host);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

        HttpPost post = new HttpPost(initUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (bodys != null) {
            post.setEntity(new ByteArrayEntity(bodys));
        }

        return convert(httpClient.execute(post));
    }

    /**
     * HTTP PUT 字符串
     * @param host
     * @param path
     * @param connectTimeout
     * @param headers
     * @param querys
     * @param body
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static Response httpPut(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, String body, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {
    	headers = initialBasicHeader(HttpMethod.PUT, path, headers, querys, null, signHeaderPrefixList, appKey, appSecret);

    	HttpClient httpClient = wrapClient(host);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

        HttpPut put = new HttpPut(initUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            put.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (StringUtils.isNotBlank(body)) {
            put.setEntity(new StringEntity(body, Constants.ENCODING));

        }

        return convert(httpClient.execute(put));
    }

    /**
     * HTTP PUT字节数组
     * @param host
     * @param path
     * @param connectTimeout
     * @param headers
     * @param querys
     * @param bodys
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static Response httpPut(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, byte[] bodys, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {
    	headers = initialBasicHeader(HttpMethod.PUT, path, headers, querys, null, signHeaderPrefixList, appKey, appSecret);

    	HttpClient httpClient = wrapClient(host);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

        HttpPut put = new HttpPut(initUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
        	put.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (bodys != null) {
        	put.setEntity(new ByteArrayEntity(bodys));
        }

        return convert(httpClient.execute(put));
    }

    /**
     * HTTP DELETE
     * @param host
     * @param path
     * @param connectTimeout
     * @param headers
     * @param querys
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static Response httpDelete(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {
        headers = initialBasicHeader(HttpMethod.DELETE, path, headers, querys, null, signHeaderPrefixList, appKey, appSecret);

        HttpClient httpClient = wrapClient(host);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

        HttpDelete delete = new HttpDelete(initUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            delete.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        return convert(httpClient.execute(delete));
    }

    /**
     * 构建FormEntity
     * 
     * @param formParam
     * @return
     * @throws UnsupportedEncodingException
     */
    private static UrlEncodedFormEntity buildFormEntity(Map<String, String> formParam)
            throws UnsupportedEncodingException {
        if (formParam != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

            for (String key : formParam.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, formParam.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, Constants.ENCODING);
            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            return formEntity;
        }

        return null;
    }
    
    private static String initUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
    	StringBuilder sbUrl = new StringBuilder();
    	sbUrl.append(host);
    	if (!StringUtils.isBlank(path)) {
    		sbUrl.append(path);
        }
    	if (null != querys) {
    		StringBuilder sbQuery = new StringBuilder();
        	for (Map.Entry<String, String> query : querys.entrySet()) {
        		if (0 < sbQuery.length()) {
        			sbQuery.append(Constants.SPE3);
        		}
        		if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
        			sbQuery.append(query.getValue());
                }
        		if (!StringUtils.isBlank(query.getKey())) {
        			sbQuery.append(query.getKey());
        			if (!StringUtils.isBlank(query.getValue())) {
        				sbQuery.append(Constants.SPE4);
        				sbQuery.append(URLEncoder.encode(query.getValue(), Constants.ENCODING));
        			}        			
                }
        	}
        	if (0 < sbQuery.length()) {
        		sbUrl.append(Constants.SPE5).append(sbQuery);
        	}
        }
    	
    	return sbUrl.toString();
    }
    	

    /**
     * 初始化基础Header
     * @param method
     * @param path
     * @param headers
     * @param querys
     * @param bodys
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws MalformedURLException
     */
    private static Map<String, String> initialBasicHeader(String method, String path,
                                                          Map<String, String> headers, 
                                                          Map<String, String> querys,
                                                          Map<String, String> bodys,
                                                          List<String> signHeaderPrefixList,
                                                          String appKey, String appSecret)
            throws MalformedURLException {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }

        headers.put(SystemHeader.X_CA_TIMESTAMP, String.valueOf(new Date().getTime()));
        //headers.put(SystemHeader.X_CA_NONCE, UUID.randomUUID().toString());
        headers.put(SystemHeader.X_CA_KEY, appKey);
        headers.put(SystemHeader.X_CA_SIGNATURE,
                SignUtil.sign(appSecret, method, path, headers, querys, bodys, signHeaderPrefixList));

        return headers;
    }

    /**
     * 读取超时时间
     * 
     * @param timeout
     * @return
     */
    private static int getTimeout(int timeout) {
        if (timeout == 0) {
            return Constants.DEFAULT_TIMEOUT;
        }

        return timeout;
    }
    
    private static Response convert(HttpResponse response) throws IOException {
    	Response res = new Response(); 
    	
    	if (null != response) {
    		res.setStatusCode(response.getStatusLine().getStatusCode());
    		for (Header header : response.getAllHeaders()) {
    			res.setHeader(header.getName(), MessageDigestUtil.iso88591ToUtf8(header.getValue()));
            }
    		
    		res.setContentType(res.getHeader("Content-Type"));
    		res.setRequestId(res.getHeader("X-Ca-Request-Id"));
    		res.setErrorMessage(res.getHeader("X-Ca-Error-Message"));
    		res.setBody(readStreamAsStr(response.getEntity().getContent()));
    		
    	} else {
    		//服务器无回应
    		res.setStatusCode(500);
    		res.setErrorMessage("No Response");
    	}
    	
    	return res;
    }


	/**
	 * 将流转换为字符串
	 *
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String readStreamAsStr(InputStream is) throws IOException {
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    WritableByteChannel dest = Channels.newChannel(bos);
	    ReadableByteChannel src = Channels.newChannel(is);
	    ByteBuffer bb = ByteBuffer.allocate(4096);
	
	    while (src.read(bb) != -1) {
	        bb.flip();
	        dest.write(bb);
	        bb.clear();
	    }
	    src.close();
	    dest.close();
	
	    return new String(bos.toByteArray(), Constants.ENCODING);
	}

	private static HttpClient wrapClient(String host) {
		HttpClient httpClient = new DefaultHttpClient();
		if (host.startsWith("https://")) {
			sslClient(httpClient);
		}
		
		return httpClient;
	}
	
	private static void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] xcs, String str) {
                	
                }
                public void checkServerTrusted(X509Certificate[] xcs, String str) {
                	
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
        	throw new RuntimeException(ex);
        }
    }







    /*---------------------------------------------------------------分割线------------------------------------------------------*/
    //get请求
    public static String doGet(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static String doGet(String url) {
        return doGet(url, null);
    }

    // post请求
    public static String doPost(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }

    public static String doPost(String url) {
        return doPost(url, null);
    }


    // post请求以json数据传输
    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }


    /**
     * 模拟客户端发送数据
     */
    @RequestMapping("/testPost")
    public void testDoPost(){
        Map<String,String> map = new HashMap<>();
        map.put("name","zhuangmeng");
        map.put("password","132341");


        map.put("json","sdda");
        String postParam = JSON.toJSONString(map);
        // post请求
        String jsonResponse = doPostJson("http://localhost:8001/hebbank/httpClientJsonResp",postParam);
        System.out.println("doPostJson返回的值是："+jsonResponse);

        // get请求
        String getData = doGet("http://localhost:8001/hebbank/getData",map);
        System.out.println("doGet返回的值是："+getData);

    }

    /**
     * 模拟服务端接收数据
     * @param request
     * @return
     */
    // 接收post请求数据处理
    @RequestMapping(value = "/httpClientJsonResp",method = RequestMethod.POST)
    @ResponseBody
    public String httpClientJsonResp(HttpServletRequest request){
        StringBuffer str = new StringBuffer();
        System.out.println("sdfa");
        try {
            InputStream inputStream = request.getInputStream();
            byte[] buf = new byte[1024];
            int len;
            str = new StringBuffer();
            while((len = inputStream.read(buf)) != -1){
                str.append(new String(buf,0,len));
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        String string = str.toString();
        System.out.println(string);
        return string;
    }

    // 接收get请求数据处理
    @RequestMapping(value = "/getData",method = RequestMethod.GET)
    @ResponseBody
    public  String testDoGetData(HttpServletRequest request){
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String json = request.getParameter("json");
        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        map.put("password",password);
        map.put("json",json);
        String str = JSON.toJSONString(map);
        return str;

    }
}