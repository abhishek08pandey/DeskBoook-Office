package com.onerivet.deskbook.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onerivet.deskbook.models.entity.Employee;
import com.onerivet.deskbook.models.entity.SeatConfiguration;
import com.onerivet.deskbook.models.entity.SeatNumber;
import com.onerivet.deskbook.models.entity.SeatRequest;
import com.onerivet.deskbook.models.payload.EmailDto;
import com.onerivet.deskbook.models.payload.SeatOwnerDetailsDto;
import com.onerivet.deskbook.models.payload.SeatRequestDto;
import com.onerivet.deskbook.models.payload.TemporarySeatOwnerDto;
import com.onerivet.deskbook.repository.EmployeeRepo;
import com.onerivet.deskbook.repository.SeatConfigurationRepo;
import com.onerivet.deskbook.repository.SeatNumberRepo;
import com.onerivet.deskbook.repository.SeatRequestRepo;
import com.onerivet.deskbook.services.EmailService;
import com.onerivet.deskbook.services.SeatRequestService;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class SeatRequestServiceImpl implements SeatRequestService {

	@Autowired
	private SeatConfigurationRepo seatConfigurationRepo;

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private SeatRequestRepo seatRequestRepo;
	@Autowired
	private SeatNumberRepo seatNumberRepo;

	@Autowired
	private EmailService emailService;

	@Override
	public SeatOwnerDetailsDto seatDetails(int id, LocalDate bookingDate) {

		// This is the owner details
		SeatConfiguration seatConfiguration = seatConfigurationRepo
				.findBySeatNumberAndDeletedByNull(new SeatNumber(id));
		Employee employee = employeeRepo.findById(seatConfiguration.getEmployee().getId()).get();
		// get from front end
		// LocalDate bookingDate = LocalDate.now();
		SeatOwnerDetailsDto seatDetailsDto = new SeatOwnerDetailsDto();

// Note: send variable to front and if request is already present
//		// if user already requested
//		SeatRequest userSeatRequestPresent = seatRequestRepo
//				.findByEmployeeIdAndSeatIdAndBookingDateAndDeletedDateNull(employeeId, seatNumber, bookingDate);

		seatDetailsDto.setFirstName(employee.getFirstName());
		seatDetailsDto.setLastName(employee.getLastName());
		seatDetailsDto.setDesignation(employee.getDesignation().getDesignationName());// modelMapper.map(employee.getDesignation(),
																						// DesignationDto.class));
		seatDetailsDto.setEmailId(employee.getEmailId());

//		int numberOfRequest = seatRequestRepo
//				.countFindBySeatIdAndBookingDateAndDeletedDateNull(seatConfiguration.getSeatNumber(), bookingDate);

//		System.out.println(numberOfRequest);
//		seatDetailsDto.setCountOfRequest(numberOfRequest);

		SeatRequest seatRequest = seatRequestRepo.findByRequestStatusAndBookingDateAndSeatIdAndDeletedDateNull(1,
				bookingDate, new SeatNumber(id));
		if (seatRequest == null) {
			return seatDetailsDto;
		}
		TemporarySeatOwnerDto temp = new TemporarySeatOwnerDto();

		temp.setFirstName(seatRequest.getEmployee().getFirstName());
		temp.setLastName(seatRequest.getEmployee().getLastName());
		temp.setEmailId(seatRequest.getEmployee().getEmailId());
		temp.setDesignation(seatRequest.getEmployee().getDesignation().getDesignationName());

		seatDetailsDto.setTemporarySeatOwner(temp);
		// A Person who get the Seat

		// seatRequestRepository

		return seatDetailsDto;
	}

	@Override
	public String seatRequest(String employeeId, SeatRequestDto seatRequestDto) {

//Maximum limit for request seats is reached, up to 3 seats can be requested for a Day
// check condition who have that seatNumber already at that date
// check the seat no is not request person's seat
// check conditon total 3 request have that seat with data, seatnumber and employeeid single

		// Request sent by employee and his details
		Employee employee = employeeRepo.findById(employeeId).get();
		// seat object
		SeatNumber seatNumber = seatNumberRepo.findById(seatRequestDto.getSeatId()).get();
		// seat owner info
//		SeatConfiguration seatConfiguration = seatConfigurationRepo
//				.findBySeatNumberAndDeletedByNull(new SeatNumber(seatRequestDto.getSeatId()));

		// no of request present on One seat
		int numberOfRequestOnSeat = seatRequestRepo.countFindBySeatIdAndBookingDateAndDeletedDateNull(seatNumber,
				seatRequestDto.getBookingDate());

		// check user have approve one seat and he can not book another seat again
//		int alreadyApproveSeat = seatRequestRepo.findRequestStatusByEmployeeIdAndBookingDateAndDeletedDateNull(
//				employeeId, seatRequestDto.getBookingDate());

		// User can total 3 request on same day
		int countPerDayEmployeeRequest = seatRequestRepo.countFindByEmployeeIdBookingDateAndDeletedDateNull(employee.getId(),
				seatRequestDto.getBookingDate());

		SeatRequest alreadyRequested = seatRequestRepo.findByEmployeeIdAndSeatIdAndBookingDateAndDeletedDateNull(
				employeeId, new SeatNumber(seatRequestDto.getSeatId()), seatRequestDto.getBookingDate());
		
		if (employee.getModeOfWork().getId() == 2) {// WFH

		 if (numberOfRequestOnSeat == 3) {
			return "This seat is already requested by Three employee, please choose a different available seat for your booking!";
		}
//		else if (alreadyApproveSeat == 1) {
//			return "You have already Approve One seat on that day!";
//		}
		else if (countPerDayEmployeeRequest == 3) {
			return "You request limit exceed!";
		} 
		else if (alreadyRequested != null) {
			return "You already requested this Seat!";
		}
// Note:- RequestStatus:- 1 =Pending , 2 = Accepted, 3 =Rejected , 4 = Cancel
		SeatRequest seatRequest = new SeatRequest();

		seatRequest.setEmployee(employee);
		seatRequest.setSeatId(seatNumber);
		seatRequest.setCreatedDate(LocalDateTime.now());
		seatRequest.setBookingDate(seatRequestDto.getBookingDate());
		seatRequest.setReason(seatRequestDto.getReason());
		seatRequest.setRequestStatus(1);

		seatRequest = seatRequestRepo.save(seatRequest);

		return "Your seat request has been submitted!";
	}
		return "Hybrid Employee Can not book Seat";
		}
}