package com.hezhan.mybatis.demo.util;

import com.alibaba.fastjson.JSON;
import com.hezhan.mybatis.demo.exception.RemoteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @Author hezhan
 * @Date 2019/10/14 16:47
 */
@Component
public class RestTemplateUtil {

    @Autowired
    private RestTemplate restTemplate;

    private static String KEY_HEADER = "KEY_HEADER";

    private static String KEY_BODY = "KEY_BODY";

    /**
     * 有header参数的get请求
     *
     * @param url          请求路径
     * @param headerParam  header参数
     * @param uriVariables 可变参数
     * @return
     */
    public <T> T getForHeader(String url, Map<String, String> headerParam, Map<String, Object> uriVariables, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (!CollectionUtils.isEmpty(headerParam)) {
            for (Map.Entry<String, String> entry : headerParam.entrySet()) {
                headers.add(entry.getKey(), getValueEncode(entry.getValue()));
            }
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        try {
            if (CollectionUtils.isEmpty(uriVariables)) {
                return restTemplate.exchange(url, HttpMethod.GET, httpEntity, clazz).getBody();
            } else {
                return restTemplate.exchange(url, HttpMethod.GET, httpEntity, clazz, uriVariables).getBody();
            }
        } catch (Exception e) {
            throw new RemoteException("访问" + url + "接口时报错，错误原因为：", e);
        }
    }

    /**
     * 不传header参数的get请求
     *
     * @param url          请求路径
     * @param clazz        指定返回类型
     * @param uriVariables 可变参数
     * @param <T>          返回类型
     * @return
     */
    public <T> T get(String url, Class<T> clazz, Map<String, Object> uriVariables) {
        try {
            if (CollectionUtils.isEmpty(uriVariables)) {
                return restTemplate.getForObject(url, clazz);
            } else {
                return restTemplate.getForObject(url, clazz, uriVariables);
            }
        } catch (Exception e) {
            throw new RemoteException("访问" + url + "接口时报错，错误原因为：", e);
        }
    }

    /**
     * 无返回值的put请求
     *
     * @param url         请求路径
     * @param headerParam 请求头
     * @param bodyParam   封装的body参数
     */
    public void put(String url, Map<String, String> headerParam, String bodyParam) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (!CollectionUtils.isEmpty(headerParam)) {
            for (Map.Entry<String, String> entry : headerParam.entrySet()) {
                headers.add(entry.getKey(), getValueEncode(entry.getValue()));
            }
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(bodyParam, headers);
        try {
            restTemplate.put(url, httpEntity);
        } catch (Exception e) {
            throw new RemoteException("访问" + url + "接口时报错，错误原因为：", e);
        }
    }

    /**
     * 无返回值的put请求
     *
     * @param url   请求路径
     * @param param 参数
     */
    public void put(String url, Map<String, Map<String, String>> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> headerParam = param.get(KEY_HEADER);
        Map<String, String> bodyParam = param.get(KEY_BODY);
        if (!CollectionUtils.isEmpty(headerParam)) {
            for (Map.Entry<String, String> entry : headerParam.entrySet()) {
                headers.add(entry.getKey(), getValueEncode(entry.getValue()));
            }
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(bodyParam), headers);
        try {
            restTemplate.put(url, httpEntity);
        } catch (Exception e) {
            throw new RemoteException("访问" + url + "接口时报错，错误原因为：", e);
        }
    }

    /**
     * post请求
     *
     * @param url   请求路径
     * @param param 参数
     * @param type  指定返回类型
     * @param <T>   返回类型
     * @return
     */
    public <T> T post(String url, Map<String, Map<String, String>> param, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> headerParam = param.get(KEY_HEADER);
        Map<String, String> bodyParam = param.get(KEY_BODY);
        if (!CollectionUtils.isEmpty(headerParam)) {
            for (Map.Entry<String, String> entry : headerParam.entrySet()) {
                headers.add(entry.getKey(), getValueEncode(entry.getValue()));
            }
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(bodyParam), headers);
        try {
            return restTemplate.postForObject(url, httpEntity, type);
        } catch (Exception e) {
            throw new RemoteException("访问" + url + "接口时报错，错误原因为：", e);
        }
    }

    /**
     * post请求
     *
     * @param url         请求地址
     * @param headerParam header参数
     * @param body        body参数
     * @param type        指定返回类型
     * @param <T>         返回数据
     * @return
     */
    public <T> T post(String url, Map<String, String> headerParam, String body, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (!CollectionUtils.isEmpty(headerParam)) {
            for (Map.Entry<String, String> entry : headerParam.entrySet()) {
                headers.add(entry.getKey(), getValueEncode(entry.getValue()));
            }
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
        try {
            return restTemplate.postForObject(url, httpEntity, type);
        } catch (Exception e) {
            throw new RemoteException("访问" + url + "接口时报错，错误原因为：", e);
        }
    }

    /**
     * delete请求
     *
     * @param url         请求路径
     * @param headerParam header参数
     */
    public void delete(String url, Map<String, String> headerParam) {
        HttpHeaders headers = new HttpHeaders();
        if (!CollectionUtils.isEmpty(headerParam)) {
            for (Map.Entry<String, String> entry : headerParam.entrySet()) {
                headers.add(entry.getKey(), getValueEncode(entry.getValue()));
            }
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        try {
            restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class);
        } catch (Exception e) {
            throw new RemoteException("访问" + url + "接口时报错，错误原因为：", e);
        }
    }

    /**
     * 带body参数的delete请求
     *
     * @param url         请求路径
     * @param headerParam 请求头
     * @param bodyParam   封装的body参数
     */
    public void delete(String url, Map<String, String> headerParam, String bodyParam) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (!CollectionUtils.isEmpty(headerParam)) {
            for (Map.Entry<String, String> entry : headerParam.entrySet()) {
                headers.add(entry.getKey(), getValueEncode(entry.getValue()));
            }
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(bodyParam, headers);
        try {
            restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class);
        } catch (Exception e) {
            throw new RemoteException("访问" + url + "接口时报错，错误原因为：", e);
        }
    }

    /**
     * 由于OkHttp会对header参数进行校验，不支持中文，所以封装了一个转义方法
     *
     * @param value 校验的header参数
     * @return
     */
    private String getValueEncode(String value) {
        if (value == null) {
            return "";
        }
        String newValue = value.replace("\n", "");
        for (int i = 0, length = newValue.length(); i < length; i++) {
            char c = newValue.charAt(i);
            if (c <= 31 && c != '\t' || c >= 127) {
                try {
                    return URLEncoder.encode(newValue, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RemoteException("URLEncoder encode header参数" + newValue + "失败");
                }
            }
        }
        return newValue;
    }
}
