package com.lym.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @Description: 邮件发送工具
 */
public class MailUtil {

    /**
     * 发送邮件
     * 
     * @param receiver 给谁发
     * @param text 发送内容
     */
    public static void sendMail(String receiver, String text) throws MessagingException {
        // 创建连接对象连接到邮件服务器
        Properties properties = new Properties();
        // 设置发送人邮件的基本参数
        // 发送人邮件服务器
        properties.put("mail.smtp.host", "smtp.huic188.com");
        // 发送端口
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.auth", "true");
        // 设置发送人邮箱的账号和密码
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 两个参数分别是发送人的邮件的账户和密码
                return new PasswordAuthentication("admin@huic188.com", "这里写你的账号的密码");
            }
        });

        // 创建邮件对象
        Message message = new MimeMessage(session);
        // 设置发件人
        message.setFrom(new InternetAddress("admin@huic188.com"));
        // 设置收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
        // 设置主题
        message.setSubject("这是一份测试邮件");
        // 设置邮件正文 第二个参数是邮件发送的类型
        message.setContent(text, "text/html;charset=UTF-8");
        // 发送一封邮件
        Transport.send(message);
    }
    
}
