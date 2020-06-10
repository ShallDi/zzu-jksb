package org.bxd.jksb.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    public Map<String, String> login(String usr, String pwd) {
        return getLoginCredentials(restTemplateBuilder.build().postForObject(LOGIN_URL, createLoginHttpEntity(usr, pwd), String.class));
    }

    public String autoSelectSbType(Map<String, String> map) {
        String result = restTemplateBuilder.build().postForObject(JKSB_URL, createSbTypeHttpEntity(map.get(PTOPID), map.get(SID)), String.class);
        return result;
    }

    public String autoSb(Map<String, String> map, String address) {
        String html = restTemplateBuilder.build().postForObject(JKSB_URL, createJksbHttpEntity(map.get(PTOPID), map.get(SID), address), String.class);
        log.info("Date:{}, AutoJksb Result Html: {}", new Date(), html);
        String result = null;
        try {
            result = getReturnInfo(html);
        } catch (Exception e) {
            log.warn("Exception: ", e.getMessage());
            result = html;
        }
        return result;
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

    private String getReturnInfo(String text) {
        Document d = Jsoup.parse(text);
        char[] a = d.body().getElementById("bak_0").child(1).child(1).child(1).child(1).toString().toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            if (isChinese(a[i])) {
                sb.append(a[i]);
            }
        }
        return sb.toString();
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

    private HttpEntity createSbTypeHttpEntity(String ptopid, String sid) {
        MultiValueMap<String, String> requestMap= new LinkedMultiValueMap<String, String>();
        requestMap.add("day6", "b");
        requestMap.add("did", "1");
        requestMap.add("door","");
        requestMap.add("men6","");
        requestMap.add("ptopid", ptopid);
        requestMap.add("sid", sid);
        return new HttpEntity(requestMap,null);
    }

    private HttpEntity createLoginHttpEntity(String usr, String pwd) {
        MultiValueMap<String, String> requestMap= new LinkedMultiValueMap<String, String>();
        requestMap.add("uid", usr);
        requestMap.add("upw", pwd);
        return new HttpEntity(requestMap,null);
    }

    private HttpEntity createJksbHttpEntity(String ptopid, String sid, String address) {
        MultiValueMap<String, String> requestMap= new LinkedMultiValueMap<String, String>();
        requestMap.add("myvs_1", "否");
        requestMap.add("myvs_2", "否");
        requestMap.add("myvs_3", "否");
        requestMap.add("myvs_4", "否");
        requestMap.add("myvs_5", "否");
        requestMap.add("myvs_6", "否");
        requestMap.add("myvs_7", "否");
        requestMap.add("myvs_8", "否");
        requestMap.add("myvs_9", "否");
        requestMap.add("myvs_10", "否");
        requestMap.add("myvs_11", "否");
        requestMap.add("myvs_12", "否");
        requestMap.add("myvs_13a", "41");
        requestMap.add("myvs_13b", "4101");
        requestMap.add("myvs_13c", address);
        requestMap.add("myvs_14", "否");
        requestMap.add("myvs_14b", "");
        requestMap.add("myvs_15", "否");
        requestMap.add("myvs_16", "其他(请说明)");
        requestMap.add("myvs_16b", "");
        requestMap.add("myvs_17", "D");
        requestMap.add("myvs_18", "A");
        requestMap.add("did", "2");
        requestMap.add("door", "");
        requestMap.add("day6", "b");
        requestMap.add("men6", "a");
        requestMap.add("sheng6", "");
        requestMap.add("shi6", "");
        requestMap.add("fun3", "");
        requestMap.add("ptopid", ptopid);
        requestMap.add("sid", sid);
        return new HttpEntity(requestMap,null);
    }
}
