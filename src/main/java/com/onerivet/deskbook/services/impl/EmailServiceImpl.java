package com.onerivet.deskbook.services.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.onerivet.deskbook.models.payload.EmailDto;
import com.onerivet.deskbook.services.EmailService;


@Service
public class EmailServiceImpl implements EmailService {

	@Autowired private JavaMailSender javaMailSender;
	
	@Override
	public void sendMailRequest(EmailDto emaiDto, String fileName) throws IOException {
		
//        ClassPathResource resource = new ClassPathResource(fileName);
//        String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        
        
		SimpleMailMessage mail = new SimpleMailMessage();
		
		mail.setFrom("DeskBook.1Rivet@outlook.com");
			
		mail.setTo(emaiDto.getTo());
		mail.setSubject(emaiDto.getSubject());
		mail.setText(emaiDto.getBody());
		
		 try {
	            javaMailSender.send(mail);
	        } catch (MailException ex) {
	            System.out.println("Failed to send email. Error: " + ex.getMessage());
	            // Handle the exception or rethrow it
	        }
	    }
	}