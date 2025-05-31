package com.greenacademy.productstore.services;

import java.time.format.DateTimeFormatter;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.greenacademy.productstore.config.UrlConfig;
import com.greenacademy.productstore.models.User;

import jakarta.mail.internet.MimeMessage;


@Service
public class EmailServices {

    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;
    private UrlConfig urlConfig;

    public EmailServices(JavaMailSender javaMailSender, TemplateEngine templateEngine, UrlConfig urlConfig) {
        this.urlConfig = urlConfig;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmailVerification(User user) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);    

            Context context = new Context();
            context.setVariable("name", user.getUsername());
            context.setVariable("email", user.getEmail());
            context.setVariable("url", urlConfig.getBaseUrl() + "/verify/" + user.getVerificationToken());
            context.setVariable("expiredTokenAt", user.getExpiredToken_at().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")).toString());

            helper.setFrom("no-reply@mywebsite");
            helper.setTo(user.getEmail());
            helper.setSubject("Verification Email");

            helper.setText(templateEngine.process("emails/email-verification", context), true);

            javaMailSender.send(message);
            System.out.println("Verification email sent successfully");
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmailResetPassword(User user) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariable("name", user.getUsername());
            context.setVariable("email", user.getEmail());
            context.setVariable("url", urlConfig.getBaseUrl() + "/reset-password/" + user.getVerificationToken());
            context.setVariable("expiredTokenAt", user.getExpiredToken_at().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")).toString());

            helper.setFrom("no-reply@mywebsite");
            helper.setTo(user.getEmail());
            helper.setSubject("Forgot Password");

            helper.setText(templateEngine.process("emails/email-forgot-password", context), true);

            javaMailSender.send(message);
            System.out.println("Forgot password email sent successfully");
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
