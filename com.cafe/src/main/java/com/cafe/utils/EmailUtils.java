package com.cafe.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Component
public class EmailUtils {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendEmail(String to, String subject, String string, List<String> list) {
        boolean isSent = false;
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setFrom("shribinwade.100@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(string,true);
            if(list!=null && list.size()>0)
            helper.setCc(getCcArray(list));

            mailSender.send(mimeMessage);

            isSent=true;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return isSent;
    }
    private String[] getCcArray(List<String> ccList){
        String[] cc = new String[ccList.size()];
        for(int i=0 ; i < ccList.size(); i++){
            cc[i] = ccList.get(i);
        }
        return cc;
    }

    public void forgotMail(String to,String subject,String password) throws MessagingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("shribinwade.100@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);

        String htmlMsg =  "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";

       message.setContent(htmlMsg,"text/html");
       mailSender.send(message);

    }
}
