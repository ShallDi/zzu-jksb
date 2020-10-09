package org.bxd.jksb;

import org.bxd.jksb.entity.User;
import org.bxd.jksb.repository.UserRepository;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JksbApplicationTests {
    public static void main(String[] args) {
        List a = new ArrayList<>();

        a.add(null);
        a.add(null);
        System.out.println(a.size());

    }

    void contextLoads() {
//        System.out.println(repository.save(new User("hrx", "201812272013584", "04190024", "河南省郑州市郑州大学新校区松园1号楼522宿舍")));
//        System.out.println(repository.save(new User("bhb", "201722362013943", "10120356", "科学大道100号")));
//        System.out.println(repository.save(new User("dxz", "201722362014037", "12435678", "郑州大学新校区")));
//        System.out.println(repository.save(new User("wxp", "201822362014456", "10123551", "郑州市科学大道郑州大学新校区")));
//        System.out.println(repository.save(new User("zy", "201722362014089", "02097213", "郑州大学新校区松园")));
//        System.out.println(repository.save(new User("yq", "201712362013792", "09266027", "郑州市科学大道100号郑州大学新校区")));
    }

}
