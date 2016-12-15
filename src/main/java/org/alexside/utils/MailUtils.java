package org.alexside.utils;

import java.util.Properties;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by abalyshev on 15.12.16.
 */
public class MailUtils {

    private static Logger log = Logger.getLogger(MailUtils.class.getName());

    private static Sender sender;

    static {
        sender = new Sender();
    }

    public static void sendMail(String subject, String content, String toMail) {
        try {
            sender.send(subject, content, toMail);
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }

    public static class Sender {
        private String username;
        private String password;
        private Properties props;

        public Sender() {
            this.username = "kibi02service@gmail.com";
            this.password = "4217330gav";

            props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
        }

        public void send(String subject, String text, String toEmail){
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                Message message = new MimeMessage(session);
                //от кого
                message.setFrom(new InternetAddress(username));
                //кому
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                //Заголовок письма
                message.setSubject(subject);
                //Содержимое
                message.setText(text);

                //Отправляем сообщение
                Transport.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
