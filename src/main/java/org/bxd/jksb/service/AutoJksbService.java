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
import java.util.concurrent.*;

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

    @Autowired
    private MailService mailService;

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(3);

    private static final ThreadLocal<SimpleDateFormat> sdf = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm"));

    @Scheduled(cron = "0 23 6 * * ?")
    public void autoSb() {
        try {
            userRepository.findAll().forEach(u -> EXECUTOR_SERVICE.submit(() -> execute(u)));
        }catch (Exception e) {
            log.error("Fetch user from repository fail, info: {}", e.getMessage());
            mailService.sendMail("Fetch user from repository fail", e.getMessage());
        }
    }

    @Scheduled(cron = "0 30 7 * * ?")
    public void autoSendResult() {
        log.info("Send AutoSb Result ...");
        List<String> result = sbResultCache.get("hrx");
        mailService.sendMail("今日打卡结果", result.get(result.size() - 1));
    }

    private long randomSleepTime() {
        // 20分钟以内
        return (new Random().nextInt(20)) * 1000 * 60;
    }


    private void execute(User v) {
        try {
            long sleepTime = randomSleepTime();
            log.info("User: {}， jksb delay execute at {}.", v.getName(), sdf.get().format(new Date(new Date().getTime() + sleepTime)));
            Thread.sleep(sleepTime);
            Map<String, String> loginCredentials =  jksbHttpUtils.login(v.getName(), v.getAccount(), v.getPassword());
            jksbHttpUtils.autoSelectSbType(v.getName(), loginCredentials);
            sbResultCache.setResult(v.getName(), sdf.get().format(new Date()) + " -> " + jksbHttpUtils.autoSb(v.getName(), loginCredentials, v.getAddress()));
        }catch (Exception e){
            log.error("User: {}, Thread execute exception, message: {}", v.getName(), e.getMessage());
            mailService.sendMail("Auto 打卡 fail", "用户：" + v.getName() + ", info: " + e.getMessage());
        }
    }
}
