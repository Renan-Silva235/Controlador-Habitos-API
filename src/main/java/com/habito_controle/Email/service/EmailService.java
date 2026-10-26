package com.habito_controle.Email.service;

import com.habito_controle.Exceptions.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.from}")
    private String from;

    public void sendVerificationCode(String to, String code){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Seu código de verificação");
        message.setText("Seu código de verificação: " + code);

        try{
            mailSender.send(message);
        }catch (MailException error){
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível enviar o email. Tente Novamente.");
        }
    }
}
