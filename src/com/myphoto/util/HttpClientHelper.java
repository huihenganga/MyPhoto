/**
 * 
 */
package com.myphoto.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
 

/**
 * @author wcj
 * 2019-3-9上午12:15:00
 */
public class HttpClientHelper {
 
    private static Logger logger = Logger.getLogger(HttpClientHelper.class);
    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 7000;
 
    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
 
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        // 在提交请求之前 测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }
 
 
 
    /**
     * 发送GET请求（HTTP），K-V形式
     * @param url
     * @author Charlie.chen；
     * @return
     */
    public static String doGet(String url, Map<String, Object> params) throws Exception {
 
 
        URI uri = null;
        // 创建默认的HttpClient实例.
        try(CloseableHttpClient httpclient = HttpClients.createDefault();) {
            if(params != null){
                List<NameValuePair> nameParams = paramsToNameValuePair(params);
                String queryString = URLEncodedUtils.format(nameParams, "utf-8");
                uri = URIUtils.resolve(new URI(url),queryString);
            }else{
                uri = new URI(url);
            }
            // 定义一个get请求方法
            HttpGet httpget = new HttpGet(uri);
 
            // 执行get请求，返回response服务器响应对象, 其中包含了状态信息和服务器返回的数据
            CloseableHttpResponse httpResponse = httpclient.execute(httpget);
 
            // 使用响应对象, 获得状态码, 处理内容
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            logger.info("Send a http get request and the response code is :"+statusCode);
            if (statusCode == HttpStatus.SC_OK) {
                // 使用响应对象获取响应实体
                HttpEntity entity = httpResponse.getEntity();
                // 将响应实体转为字符串
                String response = EntityUtils.toString(entity, "utf-8");
                return response;
            }
        } catch (Exception e) {
            logger.info("Send a http get request occurred exception",e);
            throw e;
        }
        return null;
    }
 
    /**
     * 发送POST请求（HTTP），K-V形式
     * @param url
     * @param params
     * @author Charlie.chen
     * @return  响应结果
     */
    public static String doPost(String url, Map<String,Object> params) throws IOException {
 
        try(CloseableHttpClient httpclient = HttpClients.createDefault();){// 创建默认的HttpClient实例.
 
            // 定义一个get请求方法
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Content-type","application/json,charset=utf-8");
            httppost.setHeader("Accept", "application/json");
 
            if (params!=null) {
                List<NameValuePair> list = paramsToNameValuePair(params);
                httppost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
            }
 
            // 执行post请求，返回response服务器响应对象, 其中包含了状态信息和服务器返回的数据
            CloseableHttpResponse httpResponse = httpclient.execute(httppost);
 
            // 使用响应对象, 获得状态码, 处理内容
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                // 使用响应对象获取响应实体
                HttpEntity entity = httpResponse.getEntity();
                // 将响应实体转为字符串
                String response = EntityUtils.toString(entity, "utf-8");
                return response;
            }
 
        } catch (Exception e) {
            logger.info("Send a http get request occurred exception",e);
            throw e;
        }
        return null;
    }
 
 
    /**
     * 转换参数
     * @param params
     * @return
     */
    private static List<NameValuePair> paramsToNameValuePair(Map<String,Object> params){
        List<NameValuePair> pairList = new ArrayList<>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue()==null ? null : entry.getValue().toString());
            pairList.add(pair);
        }
        return pairList;
    }
 
// 
//    /**
//     * 发送 不安全的SSL POST请求（信任所有证书）
//     * （HTTPS），K-V形式
//     * @param url
//     * @param params
//     * @author Charlie.chen
//     */
//    public static String doPostSSL_unSafe(String url, Map<String, Object> params,boolean isJson) {
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setSSLSocketFactory(createUnsafeSSLConn())
//                .setConnectionManager(connMgr)
//                .setDefaultRequestConfig(requestConfig)
//                .build();
//        HttpPost httpPost = new HttpPost(url);
//        if(isJson){
//            httpPost.addHeader("Content-type","application/json;charset=UTF-8");
//            httpPost.addHeader("Accept","application/json");
//            httpPost.addHeader("Cache-Control: no-cache","Pragma: no-cache");
//        }
//        CloseableHttpResponse response = null;
//        String httpStr = null;
// 
//        try {
//            httpPost.setConfig(requestConfig);
//            if(isJson){
//                String paramsStr = JSON.toJSONString(params);
//                StringEntity entity = new StringEntity(paramsStr,ContentType.APPLICATION_JSON);
//                httpPost.setEntity(entity);
//            }
//            if(!isJson && params!=null&&params.size()>0){
//                List<NameValuePair> pairList = paramsToNameValuePair(params);
//                httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));
//            }
// 
//            response = httpClient.execute(httpPost);
//            int statusCode = response.getStatusLine().getStatusCode();
//            logger.info("Send a PostSSL request and the response code is :"+statusCode);
//            if (statusCode != HttpStatus.SC_OK) {
//                return null;
//            }
//            HttpEntity entity = response.getEntity();
//            if (entity == null) {
//                return null;
//            }
//            httpStr = EntityUtils.toString(entity, "utf-8");
//        } catch (Exception e) {
//            logger.error("Http post unexpected exception",e);
//        } finally {
//            if (response != null) {
//                try {
//                    EntityUtils.consume(response.getEntity());
//                } catch (IOException e) {
//                    logger.error("Http client close response exception",e);
//                }
//            }
//        }
//        return httpStr;
//    }
// 
// 
// 
//    public static File doPostToFileSSL_unSafe(String url, String paramJson,File file) throws IOException {
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setSSLSocketFactory(createUnsafeSSLConn())
//                .setConnectionManager(connMgr)
//                .setDefaultRequestConfig(requestConfig)
//                .build();
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.addHeader("Content-type","application/json;charset=UTF-8");
//        httpPost.addHeader("Accept","application/json");
//        httpPost.addHeader("Cache-Control: no-cache","Pragma: no-cache");
// 
//        CloseableHttpResponse response = null;
//        String httpStr = null;
// 
//        try {
//            httpPost.setConfig(requestConfig);
//            //TODO   这个干吗的，被我注释了httpPost.setEntity(entityParam);
// 
//            response = httpClient.execute(httpPost);
//            int statusCode = response.getStatusLine().getStatusCode();
//            logger.info("Send a PostSSL request and the response code is :"+statusCode);
//            if (statusCode == HttpStatus.SC_OK) {
//                // 使用响应对象获取响应实体
//                HttpEntity entity = response.getEntity();
//                entity.writeTo(new FileOutputStream(file));
//                return file;
//            }
//        } catch (Exception e) {
//            logger.error("Http post unexpected exception",e);
//            throw e;
//        } finally {
//            if (response != null) {
//                try {
//                    EntityUtils.consume(response.getEntity());
//                } catch (IOException e) {
//                    logger.error("Http client close response exception",e);
//                }
//            }
//        }
//        return null;
//    }
// 
// 
// 
//    /**
//     * 发送安全的SSL POST请求
//     *      * （HTTPS），K-V形式
//     * @param url
//     * @param params
//     * @param certificateAddress    证书地址
//     * @param certificatePassword   证书密码
//     * @return
//     */
//    public static String doPostSSL_Safe(String url, Map<String, Object> params,String certificateAddress,
//                                        String certificatePassword) throws Exception {
// 
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setSSLSocketFactory(createSafeSSLConn(certificateAddress,certificatePassword))
//                .setConnectionManager(connMgr)
//                .setDefaultRequestConfig(requestConfig)
//                .build();
//        HttpPost httpPost = new HttpPost(url);
//        CloseableHttpResponse response = null;
//        String httpStr = null;
// 
//        try {
//            httpPost.setConfig(requestConfig);
//            List<NameValuePair> pairList = paramsToNameValuePair(params);
//            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));
//            response = httpClient.execute(httpPost);
//            int statusCode = response.getStatusLine().getStatusCode();
//            logger.info("Send a PostSSL request and the response code is :"+statusCode);
//            if (statusCode != HttpStatus.SC_OK) {
//                return null;
//            }
//            HttpEntity entity = response.getEntity();
//            if (entity == null) {
//                return null;
//            }
//            httpStr = EntityUtils.toString(entity, "utf-8");
//        } catch (Exception e) {
//            logger.error("Http post unexpected exception",e);
//        } finally {
//            if (response != null) {
//                try {
//                    EntityUtils.consume(response.getEntity());
//                } catch (IOException e) {
//                    logger.error("Http client close response exception",e);
//                }
//            }
//        }
//		return httpStr;
//    }
// 
// 
// 
//    /**
//     * 创建不安全的SSL连接
//     * @author Charlie.chen
//     * @return
//     */
//    private static SSLConnectionSocketFactory createUnsafeSSLConn() {
//        SSLConnectionSocketFactory sslsf = null;
//        try {
//            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
//                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                    return true;
//                }
//            }).build();
// 
//            sslsf = new SSLConnectionSocketFactory(sslContext);
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        }
//        return sslsf;
//    }
// 
//    /**
//     * 建立给定证书的安全连接
//     * @param certificateAddress   证书地址
//     * @param password  证书密码
//     * @return
//     * @throws Exception 
//     */
//    private static SSLConnectionSocketFactory createSafeSSLConn(String certificateAddress,String password) throws Exception {
//        //读取证书
//        KeyStore keyStore = KeyStore.getInstance("PKCS12");//PKCS12，java中的公匙加密标准
//		try (FileInputStream instream = new FileInputStream(new File(certificateAddress));){
//            keyStore.load(instream, password.toCharArray());//加载给定的证书
////            keyStore.load(null,null); //若没有可用的证书的写法
//        }
// 
//        //建立证书连接工厂
//        SSLConnectionSocketFactory sslsf = null;
//        try {
//            SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore,password.toCharArray()).build();
//            sslsf = new SSLConnectionSocketFactory(sslContext);
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        }
//        return sslsf;
//    }
}
