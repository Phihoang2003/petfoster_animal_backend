package com.hoangphi.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailUtils {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendEmail(String to,String object,String body){
        //send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(object);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendTemplateEmail(String to, String subjects, String nameTemplate, Map<String, String> map)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subjects);
        Context context = new Context();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        String body = templateEngine.process(nameTemplate, context);
        helper.setText(body, true);
        mailSender.send(message);
    }

}
