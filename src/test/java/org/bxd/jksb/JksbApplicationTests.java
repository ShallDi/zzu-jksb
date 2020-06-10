package org.bxd.jksb;

import org.bxd.jksb.service.AutoJksbService;
import org.bxd.jksb.utils.JksbHttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class JksbApplicationTests {

    @Autowired
    private AutoJksbService autoJksbService;

    @Autowired
    private JksbHttpUtils jksbHttpUtils;

    @Test
    void contextLoads() {
        autoJksbService.autoSb();
    }
}
