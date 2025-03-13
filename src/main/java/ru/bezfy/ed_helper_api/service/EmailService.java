package ru.bezfy.ed_helper_api.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import ru.bezfy.ed_helper_api.exception.EmailFailureException;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.model.VerificationToken;

import java.util.Properties;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${server.url}")
    private String url;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false"); // Включить логирование

        return mailSender;
    }

    public void sendVerificationEmail(VerificationToken verificationToken) throws EmailFailureException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(verificationToken.getUser().getEmail());
        message.setSubject("Verify your email to active your account.");
        message.setText("Your verification code is: " + verificationToken.getToken());
        try {
            getJavaMailSender().send(message);

        } catch (MailException ex) {
            System.out.println(ex.getMessage());
            throw new EmailFailureException();
        }
    }

    public void sendAccessInGroupEmail(LocalUser user) throws EmailFailureException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("Link for access in group");
        message.setSubject("Link for access in group");
        message.setText("Link for access in group: ________________________");
        try {
            getJavaMailSender().send(message);

        } catch (MailException ex) {
            System.out.println(ex.getMessage());
            throw new EmailFailureException();
        }
    }

    public void sendPasswordResetEmail(LocalUser user, String token) throws EmailFailureException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Password reset request");
        message.setText("Please follow the link below to reset your password.\n" +
                url + "/auth/reset/" + token);
        try {
            mailSender.send(message);
        } catch (MailException ex) {
            throw new EmailFailureException();
        }
    }
}
