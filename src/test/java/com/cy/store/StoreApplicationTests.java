package com.cy.store;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.util.HMACSHA1;
import com.cy.store.util.HexUtil;
import com.cy.store.util.JsonResult;
import com.sun.crypto.provider.HmacSHA1;
import lombok.val;
import org.apache.tomcat.util.buf.HexUtils;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class StoreApplicationTests {

    @Resource
private DataSource dataSource;

@Autowired
RestTemplate restTemplate;

    public String contextLoads() throws SQLException, IOException{
        String url = "https://open.gd.189.cn/ms-gateway-web/ms-application/api/token/getAccessToken?" +
                "appId=xID8pqx9GZ&appSecret=g1fo9ZLML9XZaX8TzslMCsjgzTgjN5yt";
        //URL url = new URL("https://open.gd.189.cn/ms-gateway-web/ms-application/api/token/getAccessToken");

        String s = restTemplate.getForObject(url, String.class);

        JSONObject jsonObject = JSONObject.parseObject(s);
        System.out.println(jsonObject.getString("msg"));
        String accessToken = jsonObject.getJSONObject("data").getString("accesstoken");
        return accessToken;


    }

    public String timeStamp(){
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        return timeStamp;
    }

    public String getSignature(String accessToken,String timeStamp) throws Exception {

        String secretKey = "lPtejRp8x8PvnqfZDbRJ9ECQS5xA3HEp";
        String appSecret = "g1fo9ZLML9XZaX8TzslMCsjgzTgjN5yt";
        String sign = getSign(accessToken, appSecret, timeStamp, secretKey);
        //https://open.gd.189.cn/ms-gateway-web/ms-server/api/ser/xxxx(???:adsl)/xxxx(???:tmOrderCallback)
        // /xxxx(???:v1.0)?
        // accessToken=xxxxx&timeStamp=xxxxxx&signature=xxxxxx

        return sign;

    }

    @Test
    public void getCode() throws Exception {

//        areaCode  *string
//        ??????????????????020
        //String accessToken = contextLoads();
        String accessToken = "QUE2RUU0REMyRDJGOEYzOTFCNjg3RUFCREYyMzFBRDVDOEI3MDlGMDEzRDMxNjBGQ0Q2NDlBOEQ1QTkyRDdGNA==";

        System.out.println(accessToken);
        String timeStamp = timeStamp();
        System.out.println(timeStamp);

        String signature = getSignature(accessToken, timeStamp);

        System.out.println(signature);
//        String url = "https://open.gd.189.cn/ms-gateway-web/ms-server/api/ser/adsl" +
//                "/tmOrderCallback/v1.0?accessToken={accessToken}&timeStamp={timeStamp}&signature={signature}";
        String url = "https://open.gd.189.cn/ms-gateway-web/ms-server/api/ser/sms/sendTemplateMsgForAll" +
                "/v1.0?accessToken={accessToken}&timeStamp={timeStamp}&signature={signature}";

        Map<String,String> uriVar = new HashMap<>();
        uriVar.put("accessToken",accessToken);
        uriVar.put("timeStamp",timeStamp);
        uriVar.put("signature",signature);
//
        String areaCode = "020";
//        Phone  *string
//        ????????????
//
        String phone = "15113545963";

//        Template   *string
//        ?????????
//
        String template = "DQXQXT_YZM";
//        Value   string
//        ????????????????????????????????????

        String value = "123456";
//
//        {
//            "areaCode": "string",
//                "phone": "string",
//                "template": "string",
//                "value": "string"
//        }
        //url = urlAppend(url, accessToken, timeStamp, signature);
        Map<String,String> request = new HashMap<>();
        //accessToken???timeStamp???signature
//        areaCode": "string",
//        "phone": "string",
//                "template": "string",
//                "value": "string"

        request.put("areaCode",areaCode);
        request.put("phone",phone);
        request.put("template",template);
        request.put("value",value);

        String code = restTemplate.postForObject(url,request,String.class,uriVar);
        System.out.println(code);
    }

    public String urlAppend(String url,String... params){
        for (int i = 0;i<params.length;i++){
            url += "/" + params[i];
        }
        return url;
    }


    public static String getSign(String token, String appSecret, String timeStamp, String secretKey) throws Exception {
        // ???token???appSecret???timeStamp????????????????????????key+value???????????????????????????concatString
        String concatString = "token" + token + "appSecret" + appSecret + "timeStamp" + timeStamp;
        // ???????????? uppercase (hex ( uppercase (hmac_sha1 (concatString, secretKey))))
        String concatSha = HMACSHA1.hamcsha1(concatString.getBytes(), secretKey.getBytes());
        concatSha = concatSha.toUpperCase();
        String concatHex = HexUtil.conventBytesToHexString(concatSha.getBytes());
        String sign = concatHex.toUpperCase();
        return sign;
    }

}
