package com.onerivet.deskbook.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onerivet.deskbook.models.payload.EmailDto;
import com.onerivet.deskbook.models.response.GenericResponse;
import com.onerivet.deskbook.services.EmailService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@Validated
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/deskbook")
public class EmailController {

	@Autowired private EmailService emailService;
	
//	@PostMapping("/email")
//	public GenericResponse<String> sendMailRequest(@RequestBody EmailDto emaiDto) {
//		GenericResponse<String> genericResponse = new GenericResponse<>(emailService.sendMailRequest(emaiDto), null);
//		return genericResponse;
//	}

}
