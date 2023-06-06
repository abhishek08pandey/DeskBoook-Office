package com.onerivet.deskbook.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.onerivet.deskbook.models.payload.EmailDto;
import com.onerivet.deskbook.services.EmailService;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class EmailServiceImpl implements EmailService {

	@Autowired private JavaMailSender javaMailSender;
	
	@Override
	public String sendMailRequest(EmailDto emaiDto) {
	
		SimpleMailMessage mail = new SimpleMailMessage();
		
		mail.setTo(emaiDto.getTo());
		mail.setSubject(emaiDto.getSubject());
		mail.setText(emaiDto.getBody());
		
//		mail.setTo("abhishekpandey81299@gmail.com");
//		mail.setSubject("seat");
//		mail.setText("seat request");
		
		javaMailSender.send(mail);
		
		return "Email Sent successfully to:";
		
	}

	
}
