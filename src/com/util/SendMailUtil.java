package com.util;

import com.sun.mail.util.MailSSLSocketFactory;

import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by guagua on 18/2/19.
 */
public class SendMailUtil {
    public static final String HOST = "SMTP.qq.com";
    public static final String PROTOCOL = "SMTP";
    public static final int PORT = 465;
    public static final String SENDER = "415459424@qq.com";//此处需要与host相同的网站
    public static final String SENDERPWD = "lvmbcoelxfnrbgii";

    /**
     * 获取用于发送邮件的Session
     * @return
     */
    public static Session getSession() {
        Properties props = new Properties();
        try {
            props.put("mail.smtp.host", HOST);//设置服务器地址
            props.put("mail.store.protocol" , PROTOCOL);//设置协议
            props.put("mail.smtp.port", PORT);//设置端口
            props.put("mail.smtp.auth" , true);
            props.put("mail.smtp.password",SENDERPWD);
            props.put("mail.smtp.starttls.enable",true);
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);
        }
        catch(GeneralSecurityException e){
            e.printStackTrace();
            System.out.println("GeneralSecurityException");
        }


        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER, SENDERPWD);
            }
        };
        Session session = Session.getDefaultInstance(props,authenticator);
        session.setDebug(true);
        return session;
    }

    /**
     * 发送邮件
     * @param receiver
     * @param content
     */
    public static void send(String receiver, String content) {
        Session session = getSession();
        try {
            System.out.println("-------开始发送-------");
            Message msg = new MimeMessage(session);
            //设置message属性
            msg.setFrom(new InternetAddress(SENDER));
            InternetAddress[] addrs = {new InternetAddress(receiver)};
            msg.setRecipients(Message.RecipientType.TO, addrs);
            msg.setSubject("帐号激活");
            msg.setSentDate(new Date());
            msg.setContent(content,"text/html;charset=utf-8");
            //开始发送
            Transport.send(msg);
            System.out.println("-------发送完成-------");
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
