package ru.bezfy.ed_helper_api.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
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
    private final Resend resend = new Resend("re_3rH8Bdpx_2GJMhUe94RHUFn47o83Tnq4i");

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }




    public void sendVerificationEmail(VerificationToken verificationToken) throws EmailFailureException {



        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Ed-helper verification <Verification@edhelper.ai>")
                .to(verificationToken.getUser().getEmail())
                .subject("Verify your email to active your account.")
                .html("<strong>Your verification code is: " + verificationToken.getToken() + "</strong>")
                .build();

        try {
            System.out.println("Email sent successfully");
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println(data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }
    public void sendAccessInGroupEmail(LocalUser user) throws EmailFailureException {
        Resend resend = new Resend("re_3rH8Bdpx_2GJMhUe94RHUFn47o83Tnq4i");


        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Ed-helper Group link <Request@edhelper.ai>")

                .to(user.getEmail())
                .subject("Access in your group.")
                .html("<strong>Access in your group: __url__</strong>")
                .build();

        try {
            System.out.println("Email sent successfully");
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println(data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }

    public void sendPasswordResetEmail(VerificationToken verificationToken) throws EmailFailureException {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Ed-helper Verification email <Verification@edhelper.ai>")
                .to(verificationToken.getUser().getEmail())
                .subject("Verify your email to reset your password.")
                .html("<strong>Your verification code is: " + verificationToken.getToken() + "</strong>")
                .build();

        try {
            System.out.println("Email sent successfully");
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println(data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }
}

//re_3rH8Bdpx_2GJMhUe94RHUFn47o83Tnq4i
