package com.onerivet.deskbook.services;

import java.io.IOException;

import com.onerivet.deskbook.models.payload.EmailDto;

public interface EmailService {

	public void sendMailRequest(EmailDto emaiDto, String htmlFileName) throws IOException;
}
