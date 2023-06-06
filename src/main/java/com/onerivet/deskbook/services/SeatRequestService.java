package com.onerivet.deskbook.services;

import java.time.LocalDate;

import com.onerivet.deskbook.models.payload.SeatOwnerDetailsDto;
import com.onerivet.deskbook.models.payload.SeatRequestDto;

public interface SeatRequestService {

	public SeatOwnerDetailsDto seatDetails(int id, LocalDate bookingDate);
	
	 public String seatRequest(String employeeId, SeatRequestDto seatRequestDto);
	
}
