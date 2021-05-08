package com.example.login.service;

import com.example.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.example.login.model.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Random;
import java.util.logging.Logger;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    private final static Logger logger = Logger.getLogger(EmailSenderService.class.getName());


    public static String getRandomNumberString() {

        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public void sendSimpleEmail(String toEmail,
                                String body,
                                String subject) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("senthuran.2019537@iit.ac.lk");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        System.out.println("Mail Send...");
    }

    public String generateCode(String toEmail) {
        try {
            User user = userRepository.findByEmail(toEmail);
            String code = getRandomNumberString();
            logger.info("------Code-----" + code);
            user.setCode(code);
            userRepository.save(user);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("senthuran.2019537@iit.ac.lk");
            message.setTo(toEmail);
            message.setText("Please use the code for Authentication: " + code);
            message.setSubject("Adaptive Auth Application");

            mailSender.send(message);
            logger.info("Mail Send...");
            return "Success";
        } catch (Exception e){
            logger.info("Exception  "+e);
        }
        return "Failure";
    }

    public void sendEmailWithAttachment(String toEmail,
                                        String body,
                                        String subject,
                                        String attachment) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper
                = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom("senthuran.2019537@iit.ac.lk");
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setText(body);
        mimeMessageHelper.setSubject(subject);

        FileSystemResource fileSystem
                = new FileSystemResource(new File(attachment));

        mimeMessageHelper.addAttachment(fileSystem.getFilename(),
                fileSystem);

        mailSender.send(mimeMessage);
        System.out.println("Mail Send...");

    }
}