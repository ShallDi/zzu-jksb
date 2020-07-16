package org.bxd.jksb.service;

import org.bxd.jksb.cache.SbResultCache;
import org.bxd.jksb.entity.User;
import org.bxd.jksb.repository.UserRepository;
import org.bxd.jksb.utils.JksbHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@EnableScheduling
public class AutoJksbService {

    private static final Logger log = LoggerFactory.getLogger(AutoJksbService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SbResultCache sbResultCache;

    @Autowired
    private JksbHttpUtils jksbHttpUtils;

    private static final ThreadLocal<SimpleDateFormat> sdf = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm"));

    @Scheduled(cron = "0 23 6 * * ?")
    public List<String> autoSb() {
        final List<String> count = new CopyOnWriteArrayList<>();
        userRepository.findAll().forEach(u -> {
            try {
                Thread.sleep(randomSleepTime());
            } catch (InterruptedException e) {
                log.warn("Thread sleep exception, message: {}", e.getMessage());
            }
            new Thread(() -> {
                execute(u, count);
            }).start();
        });
        return count;
    }

    private long randomSleepTime() {
        return (new Random().nextInt(30)) * 1000 * 60;
    }

    private void execute(User v, List<String> count) {
        try {
            Map<String, String> loginCredentials =  jksbHttpUtils.login(v.getAccount(), v.getPassword());
            jksbHttpUtils.autoSelectSbType(loginCredentials);
            sbResultCache.setResult(v.getName(), sdf.get().format(new Date()) + " -> " + jksbHttpUtils.autoSb(loginCredentials, v.getAddress()));
            count.add(v.getName());
        }catch (Exception e){
            log.warn("Thread execute exception, message: {}", e.getMessage());
        }
    }
}
