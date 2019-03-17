package com.myphoto.util;

import java.io.IOException;
import java.util.Random;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

import com.tencent.cloud.CosStsClient;

import sun.misc.BASE64Encoder;

public class Signature {
    private String secretId;
    private String secretKey;
    private long currentTime;
    private int random;
    private int signValidDuration;

    private static final String HMAC_ALGORITHM = "HmacSHA1";
    private static final String CONTENT_CHARSET = "UTF-8";

    public static byte[] byteMerger(byte[] byte1, byte[] byte2) {
        byte[] byte3 = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, byte3, 0, byte1.length);
        System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);
        return byte3;
    }

    public String getUploadSignature() throws Exception {
        String strSign = "";
        String contextStr = "";

        long endTime = (currentTime + signValidDuration);
        contextStr += "secretId=" + java.net.URLEncoder.encode(secretId, "utf8");
        contextStr += "&currentTimeStamp=" + currentTime;
        contextStr += "&expireTime=" + endTime;
        contextStr += "&random=" + random;

        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(this.secretKey.getBytes(CONTENT_CHARSET), mac.getAlgorithm());
            mac.init(secretKey);

            byte[] hash = mac.doFinal(contextStr.getBytes(CONTENT_CHARSET));
            byte[] sigBuf = byteMerger(hash, contextStr.getBytes("utf8"));
            strSign = new String(new BASE64Encoder().encode(sigBuf).getBytes());
            strSign = strSign.replace(" ", "").replace("\n", "").replace("\r", "");
        } catch (Exception e) {
            throw e;
        }
        return strSign;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public void setRandom(int random) {
        this.random = random;
    }

    public void setSignValidDuration(int signValidDuration) {
        this.signValidDuration = signValidDuration;
    }
    public static String getUploadSign() {
        Signature sign = new Signature();
        sign.setSecretId("AKID2xiI9wAyDk5PYVqrGj4DByZ0GLEzZxAB");
        sign.setSecretKey("74tW2NWTFpDQTK3bxc9XkpmY9YlmbEm9");
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        int rand=new Random().nextInt(java.lang.Integer.MAX_VALUE);
        sign.setRandom(rand);
        sign.setSignValidDuration(3600 * 24 * 2);

        try {
            String signature = sign.getUploadSignature();
            System.out.println("signature : " + signature);
            return signature;
        } catch (Exception e) {
            System.out.print("获取签名失败");
            e.printStackTrace();
        }
        return "获取签名失败";
    }
    
    
    public static JSONObject getUploadSign2() throws IOException {
    	
        TreeMap<String, Object> config = new TreeMap<String, Object>();

        // 固定密钥
        config.put("SecretId", "AKID2xiI9wAyDk5PYVqrGj4DByZ0GLEzZxAB");
        // 固定密钥
        config.put("SecretKey", "74tW2NWTFpDQTK3bxc9XkpmY9YlmbEm9");
        // 临时密钥有效时长，单位是秒
        config.put("durationSeconds", 1800);
        // 换成您的 bucket
        config.put("bucket", "huajia-1255442634");
        // 换成 bucket 所在地区
        config.put("region", "ap-guangzhou");
        // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的目录，例子：* 或者 a/* 或者 a.jpg
        config.put("allowPrefix", "*");
        // 密钥的权限列表。简单上传和分片需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
        String[] allowActions = new String[] {
                // 简单上传
                "name/cos:PutObject",
                // 分片上传
                "name/cos:InitiateMultipartUpload",
                "name/cos:ListMultipartUploads",
                "name/cos:ListParts",
                "name/cos:UploadPart",
                "name/cos:CompleteMultipartUpload"
        };
        config.put("allowActions", allowActions);
        JSONObject credential = CosStsClient.getCredential(config);
        System.out.println(credential);
        return credential;
    }
    
    public static void main(String[] args) {
        Signature sign = new Signature();
        sign.setSecretId("AKID2xiI9wAyDk5PYVqrGj4DByZ0GLEzZxAB");
        sign.setSecretKey("74tW2NWTFpDQTK3bxc9XkpmY9YlmbEm9");
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        System.out.println("setCurrentTime : " + System.currentTimeMillis() / 1000);
        int rand=new Random().nextInt(java.lang.Integer.MAX_VALUE);
        sign.setRandom(rand);
        System.out.println("setRandom : " + rand);
        System.out.println("setSignValidDuration : " + 3600 * 24 * 2);
        sign.setSignValidDuration(3600 * 24 * 2);
        try {
            String signature = sign.getUploadSignature();
            System.out.println("signature : " + signature);
        } catch (Exception e) {
            System.out.print("获取签名失败");
            e.printStackTrace();
        }
    }

}