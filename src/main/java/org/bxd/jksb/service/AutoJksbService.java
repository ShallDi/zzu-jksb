package org.bxd.jksb.service;

import org.bxd.jksb.cache.SbResultCache;
import org.bxd.jksb.cache.UserCache;
import org.bxd.jksb.pojo.User;
import org.bxd.jksb.utils.JksbHttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jws.soap.SOAPBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class AutoJksbService {

    @Autowired
    private UserCache userCache;

    @Autowired
    private SbResultCache sbResultCache;

    @Autowired
    private JksbHttpUtils jksbHttpUtils;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @PostConstruct
    private void init() {
        userCache.setUser("hrx", new User("hrx", "201812272013584", "04190024", "河南省郑州市郑州大学新校区松园1号楼522宿舍"));
        userCache.setUser("bhb", new User("bhb", "201722362013943", "10120356", "科学大道100号"));
    }

    @Scheduled(cron = "0 34 4 * * ?")
    public List<String> autoSb() {
        final List<String> count = new ArrayList<>();
        userCache.getAll().forEach((k, v) -> {
            try {
                Map<String, String> loginCredentials =  jksbHttpUtils.login(v.getCount(), v.getPassword());
                jksbHttpUtils.autoSelectSbType(loginCredentials);
                sbResultCache.setResult(v.getName(), sdf.format(new Date()) + " -> " + jksbHttpUtils.autoSb(loginCredentials, v.getAddress()));
                count.add(v.getName());
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        return count;
    }
}
