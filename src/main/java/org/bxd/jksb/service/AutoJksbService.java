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
import java.util.Date;
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
        
    }

    @Scheduled(cron = "0 34 4 * * ?")
    public void autoSb() {
        userCache.getAll().forEach((k, v) -> {
            try {
                Map<String, String> loginCredentials =  jksbHttpUtils.login(v.getCount(), v.getPassword());
                jksbHttpUtils.autoSelectSbType(loginCredentials);
                sbResultCache.setResult(v.getName(), sdf.format(new Date()) + " -> " + jksbHttpUtils.autoSb(loginCredentials, v.getAddress()));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}
