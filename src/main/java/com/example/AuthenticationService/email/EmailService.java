package com.example.AuthenticationService.email;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    @Autowired
    private final JavaMailSender javaMailSender;

    @Async
    public void sendVerificationEmail(String recipient, String token){

        //TODO: GET DOMAIN IP ADDRESS, OR SEND VIA REQUEST IN THE PARAMETER
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText("<a href=http://localhost:8080/user/verify?token=" + token + ">Verify</a>", true);
            helper.setTo(recipient);
            helper.setSubject("Verification link");
            javaMailSender.send(mimeMessage);
        }
        catch (MessagingException e){
            throw new IllegalStateException("Failed to send email");
        }
    }


}
