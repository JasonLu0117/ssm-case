package com.lym.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {

    public static void sendMail(String receiver, String senderName, String receiverName, String subject, String content) throws Exception {
        Properties props = getProperties();

        Session session = Session.getDefaultInstance(props);
        
        session.setDebug(true);

        MimeMessage message = createMimeMessage(session, props.getProperty("sender.username"), receiver, senderName, receiverName, subject, content);

        Transport transport = session.getTransport();

        transport.connect(props.getProperty("sender.username"), props.getProperty("sender.password"));

        transport.sendMessage(message, message.getAllRecipients());

        transport.close();
    }
    
    // 创建一封只包含文本的简单邮件
    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail, String senderName, String receiverName, String subject, String content) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, senderName, "UTF-8"));
        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, receiverName, "UTF-8"));
        // 4. Subject: 邮件主题
        message.setSubject(subject, "UTF-8");
        // 5. Content: 邮件正文（可以使用html标签）
        message.setContent(content, "text/html;charset=UTF-8");
        // 6. 设置发件时间
        message.setSentDate(new Date());
        // 7. 保存设置
        message.saveChanges();

        return message;
    }
    
    private static Properties getProperties() {
        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = MailUtil.class.getClassLoader().getResourceAsStream("mail.properties");
        // 使用properties对象加载输入流
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}