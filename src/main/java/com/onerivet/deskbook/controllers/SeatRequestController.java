package com.onerivet.deskbook.controllers;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onerivet.deskbook.models.payload.AcceptRejectDto;
import com.onerivet.deskbook.models.payload.SeatOwnerDetailsDto;
import com.onerivet.deskbook.models.payload.SeatRequestDto;
import com.onerivet.deskbook.models.response.GenericResponse;
import com.onerivet.deskbook.services.SeatRequestService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@Validated
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/deskbook")
public class SeatRequestController {
	
	@Autowired private SeatRequestService seatRequestService;
	
	
	@GetMapping("/seat-details/{id}/{bookingDate}")
	public GenericResponse<SeatOwnerDetailsDto> seatDetails(Principal principal, @PathVariable("id") int id, @PathVariable("bookingDate") LocalDate bookingDate) {
		return new GenericResponse<SeatOwnerDetailsDto>(seatRequestService.seatDetails(id, bookingDate), null);
	}
	
	//Book seat
	@PostMapping("/request-seat/")
    public GenericResponse<String> seatRequest(Principal principal, @RequestBody SeatRequestDto seatRequestDto) {
        return new GenericResponse<String>(seatRequestService.seatRequest(principal.getName(), seatRequestDto), null);
    }
	
	// check user have approve one seat at a day and he can not book another seat again
	@GetMapping("/request-status/{bookingDate}")
    public GenericResponse<Boolean> seatApproveStatus(Principal principal,@PathVariable ("bookingDate") LocalDate bookingDate ) {
        return new GenericResponse<Boolean>(seatRequestService.seatApproveStatus(principal.getName(), bookingDate), null);
    }
	

	 // Approve 2 and Reject 3
    @PutMapping("/accepted-reject")
    public GenericResponse<String> acceptReject(Principal principal,@RequestBody AcceptRejectDto takeAction) throws IOException{
        GenericResponse<String> genericResponse = new GenericResponse<String>(this.seatRequestService.acceptReject(principal.getName(),takeAction), null);
        return genericResponse;
    }
}
