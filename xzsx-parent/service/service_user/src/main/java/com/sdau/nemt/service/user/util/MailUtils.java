package com.sdau.nemt.service.user.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
public class MailUtils {


    //发送第二封验证码邮件

    public static void sendMail(String to, String code) throws Exception{

        //设置发送邮件的主机

        String host = "smtp.163.com";

        //1.创建连接对象，连接到邮箱服务器

        Properties props = System.getProperties();

        //Properties 用来设置服务器地址，主机名 。。 可以省略

        //设置邮件服务器

        props.setProperty("mail.smtp.host", host);

        props.put("mail.smtp.auth", "true");

        //SSL加密

        MailSSLSocketFactory sf = new MailSSLSocketFactory();

        sf.setTrustAllHosts(true);

        props.put("mail.smtp.ssl.enable","true");

        props.put("mail.smtp.ssl.socketFactory", sf);

        //props：用来设置服务器地址，主机名；Authenticator：认证信息

        Session session = Session.getDefaultInstance(props,new Authenticator() {

            @Override

            //通过密码认证信息

            protected PasswordAuthentication getPasswordAuthentication() {

                //new PasswordAuthentication(用户名, password);

                //这个用户名密码就可以登录到邮箱服务器了,用它给别人发送邮件

                return new PasswordAuthentication("buerjiguang@163.com","HIBJXZDHXCLZLFRQ");

            }

        });

        try {

            Message message = new MimeMessage(session);

            //2.1设置发件人：

            message.setFrom(new InternetAddress("buerjiguang@163.com"));

            //2.2设置收件人 这个TO就是收件人

            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));

            //2.3邮件的主题

            message.setSubject("来自高考志愿辅助决策系统的验证码邮件");

            //2.4设置邮件的正文 第一个参数是邮件的正文内容 第二个参数是：是文本还是html的连接

            message.setContent("<head>\n" +
                    "    <base target=\"_blank\" />\n" +
                    "    <style type=\"text/css\">::-webkit-scrollbar{ display: none; }</style>\n" +
                    "    <style id=\"cloudAttachStyle\" type=\"text/css\">#divNeteaseBigAttach, #divNeteaseBigAttach_bak{display:none;}</style>\n" +
                    "    <style id=\"blockquoteStyle\" type=\"text/css\">blockquote{display:none;}</style>\n" +
                    "    <style type=\"text/css\">\n" +
                    "        body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}\n" +
                    "        td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}\n" +
                    "        pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}\n" +
                    "        th,td{font-family:arial,verdana,sans-serif;line-height:1.666}\n" +
                    "        img{ border:0}\n" +
                    "        header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}\n" +
                    "        blockquote{margin-right:0px}\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body tabindex=\"0\" role=\"listitem\">\n" +
                    "<table width=\"700\" border=\"0\" align=\"center\" cellspacing=\"0\" style=\"width:700px;\">\n" +
                    "    <tbody>\n" +
                    "    <tr>\n" +
                    "        <td>\n" +
                    "            <div style=\"width:700px;margin:0 auto;border-bottom:1px solid #ccc;margin-bottom:30px;\">\n" +
                    "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"700\" height=\"39\" style=\"font:12px Tahoma, Arial, 宋体;\">\n" +
                    "                    <tbody><tr><td width=\"210\"></td></tr></tbody>\n" +
                    "                </table>\n" +
                    "            </div>\n" +
                    "            <div style=\"width:680px;padding:0 10px;margin:0 auto;\">\n" +
                    "                <div style=\"line-height:1.5;font-size:14px;margin-bottom:25px;color:#4d4d4d;\">\n" +
                    "                    <strong style=\"display:block;margin-bottom:15px;\">尊敬的用户：<span style=\"color:#f60;font-size: 16px;\"></span>您好！</strong>\n" +
                    "                    <strong style=\"display:block;margin-bottom:15px;\">\n" +
                    "                        您正在进行<span style=\"color: red\">邮箱验证</span>操作，请在验证码输入框中输入：<span style=\"color:#f60;font-size: 24px\">"+code+"</span>，以完成操作。\n" +
                    "                    </strong>\n" +
                    "                </div>\n" +
                    "                <div style=\"margin-bottom:30px;\">\n" +
                    "                    <small style=\"display:block;margin-bottom:20px;font-size:12px;\">\n" +
                    "                        <p style=\"color:#747474;\">\n" +
                    "                            注意：此操作可能会注册账号、修改您的密码、登录邮箱或绑定手机。如非本人操作，请及时登录并修改密码以保证帐户安全\n" +
                    "                            <br>（工作人员不会向你索取此验证码，请勿泄漏！)\n" +
                    "                        </p>\n" +
                    "                    </small>\n" +
                    "                </div>\n" +
                    "            </div>\n" +
                    "            <div style=\"width:700px;margin:0 auto;\">\n" +
                    "                <div style=\"padding:10px 10px 0;border-top:1px solid #ccc;color:#747474;margin-bottom:20px;line-height:1.3em;font-size:12px;\">\n" +
                    "                    <p>此为系统邮件，请勿回复<br>\n" +
                    "                        请保管好您的邮箱，避免账号被他人盗用\n" +
                    "                    </p>\n" +
                    "                    <p>心之所向终至所归网络科技小组</p>\n" +
                    "                </div>\n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "    </tr>\n" +
                    "    </tbody>\n" +
                    "</table>\n" +
                    "</body>", "text/html;charset=UTF-8");

            //3.发送一封激活邮件

            Transport.send(message);



        }catch(MessagingException mex){

            mex.printStackTrace();

        }

    }

}
