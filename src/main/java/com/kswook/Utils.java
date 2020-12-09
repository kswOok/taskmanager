package com.kswook;

import com.alibaba.fastjson.JSONArray;
import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.BitSet;
import java.util.Properties;

@Component
public class Utils {
    private static Logger logger = LoggerFactory.getLogger(Utils.class);


    @Scheduled(cron = "0 0 0/1 * * ?")
    public void scheduled1() {
        String data = JSONArray.toJSONString(HelloController.lastResult);
        Utils.sendEmail("任务数据暂存",data);
    }

    public static final String toEmail = "kangshouwei@deguijiaoyu.com";
    public static final String emailPassword = "Popkart2";
    public static final String fromEmail = "kangshouwei@deguijiaoyu.com";
    public static final String EmailHost = "smtp.exmail.qq.com";
    // 获取系统属性
    public static final Properties properties = System.getProperties();

    public static void sendEmail(String title, String content) {
        // 发件人电子邮箱
        // 指定发送邮件的主机为 smtp.qq.com
        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", EmailHost);
        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, emailPassword); //发件人邮件用户名、密码
            }
        });

        try {
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);
            // Set From: 头部头字段
            message.setFrom(new InternetAddress(fromEmail));
            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            // Set Subject: 头部头字段
            message.setSubject(title);
            // 设置消息体
            message.setText(content);
            // 发送消息
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }


}