package com.onerivet.deskbook.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.onerivet.deskbook.models.entity.SeatConfiguration;
import com.onerivet.deskbook.models.entity.SeatNumber;
import com.onerivet.deskbook.models.entity.SeatRequest;
import com.onerivet.deskbook.models.payload.TakeActionDto;
import com.onerivet.deskbook.repository.SeatConfigurationRepo;
import com.onerivet.deskbook.repository.SeatRequestRepo;
import com.onerivet.deskbook.services.SeatRequestService;

@Service
public class AutoApproveSheduleServiceImple {

	@Autowired private SeatRequestRepo seatRequestRepo;
	
	@Autowired private SeatRequestService seatRequestService;
	
	@Autowired private SeatConfigurationRepo seatConfigurationRepo; 

	
	//@Scheduled(cron = "0 0 0 * * *") // Run at 12 AM (midnight) every day
	@Scheduled(cron = "0 * * * * *")// every minute
	public void acceptRequestwithin24Hours() {
		
		LocalDate bookingDate = LocalDate.now().plusDays(1);
		
		Sort sort = Sort.by(Sort.Direction.ASC, "CreatedDate");
		List<SeatRequest> pendingSeatRequest = seatRequestRepo.findByBookingDateAndRequestStatusAndDeletedDateNull(bookingDate, 1, sort);
		
		
		
		if(!pendingSeatRequest.isEmpty()) {
			
			SeatConfiguration seatConfiguration = seatConfigurationRepo.findBySeatNumberAndDeletedByNull(new SeatNumber(pendingSeatRequest.get(0).getSeatId().getSeatNumber()));
			
		System.out.println(bookingDate);
// Send all data to TakeAction class for accept request and reject other seat request 		
		TakeActionDto takeActionDto = new TakeActionDto();
		
		takeActionDto.setSeatId(pendingSeatRequest.get(0).getSeatId().getSeatNumber());
		takeActionDto.setEmployeeId(pendingSeatRequest.get(0).getEmployee().getId());
		takeActionDto.setBookingDate(pendingSeatRequest.get(0).getBookingDate());
		takeActionDto.setRequestStatus(2);
		
		seatRequestService.takeAction(seatConfiguration.getEmployee().getId(), takeActionDto);
		}
	}
}
