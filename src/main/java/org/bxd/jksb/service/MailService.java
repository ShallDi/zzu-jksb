package org.bxd.jksb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(AutoJksbService.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static final String JKSB_TITLE = "健康打卡信息";

    @Value("${receive.username}")
    private String receiverMail;

    @Value("${spring.mail.username}")
    private String senderMail;

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(sdf.format(new Date()) + " " + JKSB_TITLE);
            helper.setFrom(senderMail);
            helper.setTo(receiverMail);
            helper.setText(formatContent(title, content),true);
            mailSender.send(message);
            log.info("Send message success");
        } catch (MessagingException e) {
            log.error("Create or send mail fail, info: {}", e.getMessage());
        }
    }

    private String formatContent(String title, String info) {
        return "<p>" + title + "</p><p>" + info + "</p>";
    }
}