package com.onerivet.deskbook.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onerivet.deskbook.models.entity.Employee;
import com.onerivet.deskbook.models.entity.SeatNumber;
import com.onerivet.deskbook.models.entity.SeatRequest;
import com.onerivet.deskbook.models.payload.TakeActionDto;
import com.onerivet.deskbook.repository.EmployeeRepo;
import com.onerivet.deskbook.repository.SeatConfigurationRepo;
import com.onerivet.deskbook.repository.SeatNumberRepo;
import com.onerivet.deskbook.repository.SeatRequestRepo;
import com.onerivet.deskbook.util.EmailUtil;

@Service
public class Dummy {

	@Autowired
	private SeatConfigurationRepo seatConfigurationRepo;

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private SeatRequestRepo seatRequestRepo;
	@Autowired
	private SeatNumberRepo seatNumberRepo;

	@Autowired
	private EmailUtil emailUtil;
	
	
	public String takeAction(String employeeId, TakeActionDto takeAction) {

		if (takeAction.getRequestStatus() == 2) {// Approve = 2
			
			// Update for approve
			SeatRequest requestedEmployee = seatRequestRepo.findByRequestStatusAndEmployeeIdAndSeatIdAndBookingDateAndDeletedDateNull(1,
					takeAction.getEmployeeId(), new SeatNumber(takeAction.getSeatId()), takeAction.getBookingDate());

			requestedEmployee.setRequestStatus(2);
			requestedEmployee.setModifiedBy(new Employee(employeeId));
			requestedEmployee.setModifiedDate(LocalDateTime.now());

			// Call Email service for Approved seat
			
			seatRequestRepo.save(requestedEmployee);
			
// request sent on different seat by approval employee which will rejected automatic
			List<SeatRequest> rejectSeatList = seatRequestRepo
					.findByEmployeeIdAndRequestStatusAndBookingDateAndDeletedDateNull(takeAction.getEmployeeId(), 1 ,
							takeAction.getBookingDate());

			if (rejectSeatList != null) {

				for (int i = 0; i < rejectSeatList.size(); i++) {

					rejectSeatList.get(i).setRequestStatus(3);
					rejectSeatList.get(i).setModifiedBy(new Employee(employeeId));
					rejectSeatList.get(i).setModifiedDate(LocalDateTime.now());

					// Call Email service for rejected seat
					
					seatRequestRepo.save(rejectSeatList.get(i));
				}
			}
// Other Employee on same seat request will automatic rejected		
			List<SeatRequest> otherEmployeeRequest = seatRequestRepo
					.getByRequestStatusAndBookingDateAndSeatIdAndDeletedDateNull(1, takeAction.getBookingDate(),
							new SeatNumber(takeAction.getSeatId()));

			if (otherEmployeeRequest != null) {

				for (int i = 0; i < otherEmployeeRequest.size(); i++) {

					otherEmployeeRequest.get(i).setRequestStatus(3);
					otherEmployeeRequest.get(i).setModifiedBy(new Employee(employeeId));
					otherEmployeeRequest.get(i).setModifiedDate(LocalDateTime.now());

					// Call Email service for rejected seat
					
					seatRequestRepo.save(otherEmployeeRequest.get(i));
				}
			}
			return "Seat Approved";
			
		} else {
			// If rejected 3
			SeatRequest requestedEmployee = seatRequestRepo.findByRequestStatusAndEmployeeIdAndSeatIdAndBookingDateAndDeletedDateNull(1,
					takeAction.getEmployeeId(), new SeatNumber(takeAction.getSeatId()), takeAction.getBookingDate());

			requestedEmployee.setRequestStatus(3);
			requestedEmployee.setModifiedBy(new Employee(employeeId));
			requestedEmployee.setModifiedDate(LocalDateTime.now());

			// Call Email service for rejected seat
			seatRequestRepo.save(requestedEmployee);
			
			return "Seat Reject";

		}
	}
}