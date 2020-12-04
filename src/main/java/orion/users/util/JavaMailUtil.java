package orion.users.util;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class JavaMailUtil {


    
    public static void sendMail(String recepient, String hash, String link) throws Exception {
        
        System.out.println("Preparing to sent");
        Properties props = new Properties();
        /** Parâmetros de conexão com servidor Hotmail */



        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp-mail.outlook.com");
        props.put("mail.smtp.ssl.trust", "smtp-mail.outlook.com");
        props.put("mail.smtp.port", "587");



        String myAccountEmail = "testorionservice@outlook.com";
        String password = "MICROservices2020";


        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

    Message message;

             message = prepareMessage(session, myAccountEmail, recepient, hash, link);


        Transport.send(message);
        System.out.println("Message sent");

    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recepient, String hashcode, String link) {
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("oi, eu sou o Orion");
           
           
    if(hashcode != null){
        link = link + hashcode;
        message.setContent(link, "text/html");
    }else{
        message.setContent(link, "text/html");
    }
             
            return message;
        } catch (Exception ex) {
            Logger.getLogger(JavaMailUtil.class.getName()).log(Level.SEVERE, null, ex);
      }
    return null;
    } 


}




