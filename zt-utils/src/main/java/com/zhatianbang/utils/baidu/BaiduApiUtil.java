package com.zhatianbang.utils.baidu;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BaiduApiUtil {
    //    public static final String KEY_1 = "CaXMqxCO5vAgXaKh7XCPDqXeuALG9Gzy";
    public static final String KEY_1 = "fHGc7kQc2DauHysXDVjXXOb8G6sWTlHU";
    private static CloseableHttpClient hc;

    static {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        hc = httpClientBuilder.build();
    }

    public static String getTurnType(int type) {
        switch (type) {
            case 0:
                return "无效";
            case 1:
                return "直行";
            case 2:
                return "右前方转弯";
            case 3:
                return "右转";
            case 4:
                return "右后方转弯";
            case 5:
                return "掉头";
            case 6:
                return "左后方转弯";
            case 7:
                return "左转";
            case 8:
                return "左前方转弯";
            case 9:
                return "左侧";
            case 10:
                return "右侧";
            case 11:
                return "分歧左";
            case 12:
                return "分歧中央";
            case 13:
                return "分歧右";
            case 14:
                return "环岛";
            case 15:
                return "进渡口";
            case 16:
                return "出渡口";
            default:
                return "直行";
        }
    }

    public static Map<String, String> getBaiduGeocoderLatitude(String address) {
        BufferedReader in = null;
        try {
            // 将地址转换成utf-8的16进制
            address = URLEncoder.encode(address, "UTF-8");
            // 如果有代理，要设置代理，没代理可注释
            URL tirc = new URL("http://api.map.baidu.com/geocoding/v3/?address="
                    + address + "&output=json&ak=" + KEY_1);
            in = new BufferedReader(new InputStreamReader(tirc.openStream(),
                    "UTF-8"));
            String res;
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            String str = sb.toString();
            Map<String, String> map = null;
            if (!StringUtils.isBlank(str)) {
                int lngStart = str.indexOf("lng\":");
                int lngEnd = str.indexOf(",\"lat");
                int latEnd = str.indexOf("},\"precise");
                if (lngStart > 0 && lngEnd > 0 && latEnd > 0) {
                    String lng = str.substring(lngStart + 5, lngEnd);
                    String lat = str.substring(lngEnd + 7, latEnd);
                    map = new HashMap<String, String>();
                    map.put("lng", lng);
                    map.put("lat", lat);
                    return map;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Map<String, Object> getDriveDirection(String origin, String destination, String origin_region, String destination_region, int tactics) {
        Map<String, Object> map = new HashMap<String, Object>();
        String url =
                "http://api.map.baidu.com/direction/v1?mode=driving&ak=" + KEY_1 + "&origin="
                        + origin + "&destination=" + destination + "&origin_region=" + origin_region + "&destination_region=" + destination_region + "&tactics=" + tactics + "&output=json";
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse resp = hc.execute(get);
            String jsonStr = EntityUtils.toString(resp.getEntity(), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            JSONArray routesInfo = jsonObject.getJSONObject("result").getJSONArray(
                    "routes");
            int distance = routesInfo.getJSONObject(0).getInteger("distance");
            int duration = routesInfo.getJSONObject(0).getInteger("duration");
            JSONArray stepsJsonArray = routesInfo.getJSONObject(0).getJSONArray("steps");
            map.put("distance", distance);
            map.put("duration", duration);
            map.put("steps", stepsJsonArray);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO  合并优化，减少请求api的频率
    public static String getGeocoderAddress(double lng, double lat) {

        String url =
                "http://api.map.baidu.com/reverse_geocoding/v3/?ak=" + KEY_1 + "&coordtype=wgs84ll&location="
                        + lat + "," + lng + "&extensions_poi=1" + "&output=json";
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse resp = hc.execute(get);
            String jsonStr = EntityUtils.toString(resp.getEntity(), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            String fulAddress = jsonObject.getJSONObject("result").getString(
                    "formatted_address") + jsonObject.getJSONObject("result").getString(
                    "sematic_description");
            return fulAddress;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getGeocoderAddressFromBd09ll(double lng, double lat) {

        String url =
                "http://api.map.baidu.com/reverse_geocoding/v3/?ak=" + KEY_1 + "&coordtype=bd09ll&location="
                        + lat + "," + lng + "&extensions_poi=1" + "&output=json";
        HttpGet get = new HttpGet(url);
        System.out.println(url);
        try {
            HttpResponse resp = hc.execute(get);
            String jsonStr = EntityUtils.toString(resp.getEntity(), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            String fulAddress = jsonObject.getJSONObject("result").getString(
                    "formatted_address") + jsonObject.getJSONObject("result").getString(
                    "sematic_description");
            return fulAddress;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 批量获取实际地理位置（最多20条）
     *
     * @param locationList
     * @return
     */
    public static List<String> batchGetGeocoderAddressFromBd09ll(List<double[]> locationList) {
        // 定义返回结果
        List<String> result = new ArrayList<>();

        // 批量获取实际地理位置的接口url
        String url = "http://api.map.baidu.com/batch";
        // 创建httpPost对象
        HttpPost httpPost = new HttpPost(url);
        try {
            // 设置请求头
            httpPost.setHeader("Content-type", "application/json");
            // 获取批量请求获取实际地理位置的body
            String entityString = getBatchRequestBodyString(locationList);
            // 设置JSON格式的请求体
            httpPost.setEntity(new StringEntity(entityString, "UTF-8"));
            // 执行请求
            HttpResponse response = hc.execute(httpPost);
            // 处理请求结果为JSONObject
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            // 取出批量获取的结果array
            JSONArray batchResultArr = jsonObject.getJSONArray("batch_result");
            // 遍历
            for (int i = 0; i < batchResultArr.size(); i++) {
                JSONObject resultJSONObject = batchResultArr.getJSONObject(i).getJSONObject("result");
                // 获取地址
                String fulAddress = resultJSONObject.getString("formatted_address") + resultJSONObject.getString("sematic_description");
                // 添加到返回结果list
                result.add(fulAddress);
            }

            // 返回
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 组装批量请求获取实际地理位置的body
     *
     * @param locationList
     * @return
     */
    private static String getBatchRequestBodyString(List<double[]> locationList) {
        // 创建list对象
        List<Map<String, String>> reqs_list = new ArrayList<>();
        // body_url前缀
        String body_url_pre = "/reverse_geocoding/v3/?output=json&coordtype=bd09ll&extensions_poi=1&ak=" + KEY_1 + "&location=";
        // 遍历组装请求参数
        for (double[] location : locationList) {
            Map<String, String> map = new HashMap<>();
            map.put("method", "get");
            map.put("url", body_url_pre + location[0] + "," + location[1]);
            reqs_list.add(map);
        }
        // 创建paramJson对象
        JSONObject paramJson = new JSONObject();
        // 将reqs_list放入paramJson中
        paramJson.put("reqs", reqs_list);
        // 返回
        return paramJson.toJSONString();
    }

    /**
     * 获取省份
     *
     * @param lng
     * @param lat
     * @return
     */
    public static String getProvince(double lng, double lat, int type) {


        String url = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=" + KEY_1 + "&coordtype=wgs84ll&location="
                + lat + "," + lng + "&extensions_poi=1" + "&output=json";

        if (type == 0) {
            url = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=" + KEY_1 + "&coordtype=bd09ll&location="
                    + lat + "," + lng + "&extensions_poi=1" + "&output=json";
        }
//        else if (type == CommonConstant.WGS84){
//            url = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=" + KEY_1 + "&coordtype=wgs84ll&location="
//                    + lat + "," + lng + "&extensions_poi=1" + "&output=json";
//        }
        HttpGet get = new HttpGet(url);
        System.out.println(url);
        try {
            HttpResponse resp = hc.execute(get);
            String jsonStr = EntityUtils.toString(resp.getEntity(), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            String province = jsonObject.getJSONObject("result").getJSONObject(
                    "addressComponent").getString("province");
            return province;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Map<String, String> geocoderAddress(double lon, double lat) {
        Map<String, String> map = new HashMap<String, String>();
        String url =
                "http://api.map.baidu.com/geocoder?key=" + KEY_1 + "&location="
                        + lat + "," + lon + "&output=json";
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse resp = hc.execute(get);
            String jsonStr = EntityUtils.toString(resp.getEntity(), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            System.out.println(jsonObject.getJSONObject("result"));
            JSONObject jsonObject1 = JSONObject.parseObject(jsonObject.getJSONObject("result").getString("addressComponent"));
            map.put("address", jsonObject.getJSONObject("result").getString("formatted_address"));
            map.put("province", jsonObject1.getString("province"));
            map.put("city", jsonObject1.getString("city"));
            map.put("street", jsonObject1.getString("street"));
            map.put("district", jsonObject1.getString("district"));
            map.put("street_number", jsonObject1.getString("street_number"));
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONArray GetPOIDataByRect(Double minLon, Double maxLon, Double minLat, Double maxLat, int pTypeID, String pStrKey, int curPage, int pageSize) {
        JSONArray jsonArray = new JSONArray();
        String url =
                "http://api.map.baidu.com/place/v2/search?ak=" + KEY_1 + "&query=";
        String queryString = "%E5%8A%A0%E6%B2%B9%E7%AB%99";
        if (!StringUtils.isBlank(pStrKey)) {
            queryString = URLEncoder.encode(pStrKey);
        } else {
            if (pTypeID == 1) {//高速路出口
                queryString = URLEncoder.encode(queryString);
            } else if (pTypeID == 2) {//收费站
                queryString = "%E6%94%B6%E8%B4%B9%E7%AB%99";
            } else if (pTypeID == 3) {//加油站
                queryString = "%E5%8A%A0%E6%B2%B9%E7%AB%99";
            } else if (pTypeID == 4) {//服务区
                queryString = "%E6%9C%8D%E5%8A%A1%E5%8C%BA";
            } else if (pTypeID == 5) {//天气

            }
        }
        url += queryString + "&output=json";
        url += "&bounds=" + minLat + "," + minLon + "," + maxLat + "," + maxLon;
        url += "&page_num=" + (curPage - 1) + "&page_size=" + pageSize;
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse resp = hc.execute(get);
            String jsonStr = EntityUtils.toString(resp.getEntity(), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            if ("ok".equals(jsonObject.get("message"))) {
                jsonArray = JSONArray.parseArray((String) jsonObject.get("results"));
            }
            return jsonArray;
        } catch (Exception e) {
            e.printStackTrace();
            return jsonArray;
        }
    }

    public static Map<String, String> getWeatherByCity(String city) {
        Map<String, String> map = new HashMap<String, String>();
        String url =
                "http://api.map.baidu.com/telematics/v3/weather?ak=" + KEY_1 + "&location="
                        + city + "&output=json";
        HttpGet get = new HttpGet(url);
        System.out.println(url);
        try {
            HttpResponse resp = hc.execute(get);
            String jsonStr = EntityUtils.toString(resp.getEntity(), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            if ("success".equals(jsonObject.get("status"))) {
                String date = jsonObject.getString("date");
                map.put("date", date);
                JSONObject result = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("weather_data").getJSONObject(0);
                map.put("dayPictureUrl", result.getString("dayPictureUrl"));
                map.put("nightPictureUrl", result.getString("nightPictureUrl"));
                map.put("weather", result.getString("weather"));
                map.put("wind", result.getString("wind"));
                map.put("temperature", result.getString("temperature"));
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        double[] gcj2bd = GpsToBaidu.wgs2bd(29.970771111111112, 121.5647288888889);
        getGeocoderAddress(121.5647288888889, 29.970771111111112);
        System.out.println(getGeocoderAddress(121.5647288888889, 29.970771111111112));
//        getGeocoderAddressFromBd09ll(121.575410, 29.974501);
    }
}
