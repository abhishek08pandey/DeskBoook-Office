package com.onerivet.deskbook.services;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.RequestBody;

import com.onerivet.deskbook.models.payload.SeatOwnerDetailsDto;
import com.onerivet.deskbook.models.payload.SeatRequestDto;
import com.onerivet.deskbook.models.payload.TakeActionDto;

public interface SeatRequestService {

	public SeatOwnerDetailsDto seatDetails(int id, LocalDate bookingDate);
	
	 public String seatRequest(String employeeId, SeatRequestDto seatRequestDto);

	public Boolean seatApproveStatus(String employeeId, LocalDate seatRequestDto);
	
	  public String takeAction(String employeeId, @RequestBody TakeActionDto takeAction);
	}
