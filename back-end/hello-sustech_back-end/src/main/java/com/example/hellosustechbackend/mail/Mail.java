package com.example.hellosustechbackend.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class Mail {
    private Properties properties;
    private String send_account;
    private String receive_account;
    private String check_code;
    private Message message;
    private Session session;

    public Mail(String receive_account,String check_code){
        this.send_account = "1625303434@qq.com";
        this.receive_account = receive_account;
        this.check_code = check_code;
        initialMail();
    }
    private void initialMail(){
        properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");// 连接协议
        properties.put("mail.smtp.host", "smtp.qq.com");// 主机名
        properties.put("mail.smtp.port", 465);// 端口号
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
        properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
        // 得到回话对象
        session = Session.getInstance(properties);
        // 获取邮件对象
        message = new MimeMessage(session);
    }

    public boolean SendMessage(String Subject,String content){
        boolean result = true;
        try{
            // 设置发件人邮箱地址
            message.setFrom(new InternetAddress(send_account));
            // 设置收件人邮箱地址
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receive_account));
            // 设置邮件标题
            message.setSubject(Subject);
            // 设置邮件内容
            message.setText(content+check_code);
            //设置邮箱发送者名字
            message.setFrom(new InternetAddress(send_account, "Hello SUSTech"));
            // 得到邮差对象
            Transport transport = session.getTransport();
            // 连接自己的邮箱账户
            transport.connect(send_account, "bqsayctzegmndgbd");// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }catch (Exception e){
            result = false;
        }
        return result;
    }
}
