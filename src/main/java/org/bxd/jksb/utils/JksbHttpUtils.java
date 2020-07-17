package org.bxd.jksb.utils;

import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JksbHttpUtils {

    private static final Logger log = LoggerFactory.getLogger(JksbHttpUtils.class);

    private static final String PTOPID = "ptopid";

    private static final int PTOPID_LENGTH = 33;

    private static final int SID_LENGTH = 18;

    private static final String SID = "sid";

    private static final String LOGIN_URL = "https://jksb.v.zzu.edu.cn/vls6sss/zzujksb.dll/login";

    private static final String JKSB_URL = "https://jksb.v.zzu.edu.cn/vls6sss/zzujksb.dll/jksb";

    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            //添加TLSv1、TLSv1.1、TLSv1.2、TLSv1.3支持
            .connectionSpecs(Arrays.asList(ConnectionSpec.COMPATIBLE_TLS))
            .build();

    public Map<String, String> login(String name, String usr, String pwd) {
        final Map<String, String> resultMap = new HashMap<>();
        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(createLoginRequestBody(usr,pwd))
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            log.info("Date:{}, Method: login, Code:{}, Message:{}", new Date(), response.code(), response.message());
            String body = getReturnInfo(response.body().string());
            recordResponseInfoLog(name, body);
            resultMap.putAll(getLoginCredentials(body));
        } catch (IOException e) {
            recordResponseWarnLog(name, e.getMessage());
        }
        return resultMap;
    }

    public String autoSelectSbType(String name, Map<String, String> map) {
        final StringBuilder result = new StringBuilder();
        Request request = new Request.Builder()
                .url(JKSB_URL)
                .post(createSbTypRequestBody(map.get(PTOPID), map.get(SID)))
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            log.info("Date:{}, Method: autoSelectSbType, Code:{}, Message:{}", new Date(), response.code(), response.message());
            String body = getReturnInfo(response.body().string());
            recordResponseInfoLog(name, body);
            result.append(body);
        } catch (IOException e) {
            recordResponseWarnLog(name, e.getMessage());
        }
        return result.toString();
    }

    public String autoSb(String name, Map<String, String> map, String address) {
        final StringBuilder result = new StringBuilder();
        Request request = new Request.Builder()
                .url(JKSB_URL)
                .post(createJksbRequestBody(map.get(PTOPID), map.get(SID), address))
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            log.info("Date:{}, Method: autoSb, Code:{}, Message:{}", new Date(), response.code(), response.message());
            String body = getReturnInfo(response.body().string());
            recordResponseInfoLog(name, body);
            result.append(body);
        } catch (IOException e) {
            recordResponseWarnLog(name, e.getMessage());
        }
        return result.toString();
    }

    private void recordResponseInfoLog(String name, String info) {
        log.info("Date: {}, User: {}, onResponse: {}", new Date(), name, info);
    }

    private void recordResponseWarnLog(String name, String info) {
        log.info("Date: {}, User: {}, onFailure: {}", new Date(), name, info);
    }

    private RequestBody createSbTypRequestBody(String ptopid, String sid) {
        return new FormBody.Builder()
                .add("day6", "b")
                .add("did", "1")
                .add("door","")
                .add("men6","")
                .add("ptopid", ptopid)
                .add("sid", sid)
                .build();
    }

    private RequestBody createLoginRequestBody(String usr, String pwd) {
        return new FormBody.Builder()
                .add("uid", usr)
                .add("upw", pwd)
                .build();
    }

    private RequestBody createJksbRequestBody(String ptopid, String sid, String address) {
        return new FormBody.Builder()
                .add("myvs_1", "否")
                .add("myvs_2", "否")
                .add("myvs_3", "否")
                .add("myvs_4", "否")
                .add("myvs_5", "否")
                .add("myvs_6", "否")
                .add("myvs_7", "否")
                .add("myvs_8", "否")
                .add("myvs_9", "否")
                .add("myvs_10", "否")
                .add("myvs_11", "否")
                .add("myvs_12", "否")
                .add("myvs_13a", "41")
                .add("myvs_13b", "4101")
                .add("myvs_13c", address)
                .add("myvs_14", "否")
                .add("myvs_14b", "")
                .add("myvs_15", "否")
                .add("myvs_16", "其他(请说明)")
                .add("myvs_16b", "")
                .add("myvs_17", "D")
                .add("myvs_18", "A")
                .add("did", "2")
                .add("door", "")
                .add("day6", "b")
                .add("men6", "a")
                .add("sheng6", "")
                .add("shi6", "")
                .add("fun3", "")
                .add("ptopid", ptopid)
                .add("sid", sid)
                .build();
    }

    private Map<String, String> getLoginCredentials(String text) {
        Map<String, String> temp = new HashMap<>(2);
        Document document = Jsoup.parse(text);
        String scriptText = document.select("script").toString();
        int ptopidIndex = scriptText.indexOf("ptopid");
        int sidIndex = scriptText.indexOf("sid");
        temp.put(PTOPID, scriptText.substring(ptopidIndex + 7, ptopidIndex + 7 + PTOPID_LENGTH));
        temp.put(SID, scriptText.substring(sidIndex + 4, sidIndex + 4 + SID_LENGTH));
        return temp;
    }

    private boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    private String getReturnInfo(String text) {
        Document d = Jsoup.parse(text);
        char[] a = d.body().getElementById("bak_0").toString().toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            if (isChinese(a[i])) {
                sb.append(a[i]);
            }
        }
        return sb.toString();
    }
}
