/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.service;

import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Logger;

@Service
public class EmailSendingService {

    private final Logger logger = Logger.getLogger(getClass().getName());

    public boolean sendPasswordReset(final String recipientEmail, final String tempPassword) {
        final String subject = "[TestTool / SimplerInvoicing] Password reset";
        final String message = new StringBuilder().append("Please use this temporary password to login and change your account's password: ").
                append(tempPassword).
                append("\r\n If you have not requested this action, please ignore or delete this email!").
                toString();

        return send(recipientEmail, message, subject);
    }

    public boolean sendUsernameReminder(final String recipientEmail, final String username) {
        final String subject = "[TestTool / SimplerInvoicing] Username reminder";
        final String message = new StringBuilder().append("This is your username: ").
                append(username).
                append("\r\n If you have not requested this action, please ignore or delete this email!").
                toString();

        return send(recipientEmail, message, subject);
    }

    private boolean send(String recipientEmail, final String text, final String subject) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "localhost");
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("noreply@tt.simplerinvoicing.org"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));

            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
            logger.info("Sent message successfully....");
        } catch (MessagingException mex) {
            logger.severe("Error encountered while trying to send email, " + mex.getMessage());
            mex.printStackTrace();
            return false;
        }

        return true;
    }
}
