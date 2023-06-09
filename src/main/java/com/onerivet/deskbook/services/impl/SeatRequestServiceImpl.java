package com.onerivet.deskbook.services.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onerivet.deskbook.models.entity.Employee;
import com.onerivet.deskbook.models.entity.SeatConfiguration;
import com.onerivet.deskbook.models.entity.SeatNumber;
import com.onerivet.deskbook.models.entity.SeatRequest;
import com.onerivet.deskbook.models.payload.AcceptRejectDto;
import com.onerivet.deskbook.models.payload.SeatOwnerDetailsDto;
import com.onerivet.deskbook.models.payload.SeatRequestDto;
import com.onerivet.deskbook.models.payload.TemporarySeatOwnerDto;
import com.onerivet.deskbook.repository.EmployeeRepo;
import com.onerivet.deskbook.repository.FloorRepo;
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
	
	@Autowired private FloorRepo floorRepo;

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

		SeatRequest seatRequest = seatRequestRepo.findByRequestStatusAndBookingDateAndSeatIdAndDeletedDateNull(2,
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
// check condition who have that seatNumber already at that date with pending and approve
//Not in requirement: check the seat no is not request person's seat
// check condition total 3 request have that seat with data, seatnumber and employeeid single

		// Request sent by employee and his details
		Employee employee = employeeRepo.findById(employeeId).get();
		// seat object
		SeatNumber seatNumber = seatNumberRepo.findById(seatRequestDto.getSeatId()).get();


		// no of request present on One seat
		int numberOfRequestOnSeat = seatRequestRepo.countFindByRequestStatusAndSeatIdAndBookingDateAndDeletedDateNull(1,seatNumber,
				seatRequestDto.getBookingDate());

		// User can total 3 request on same day
		int countPerDayEmployeeRequest = seatRequestRepo.countFindByEmployeeIdAndBookingDateAndDeletedDateNull(
				employee.getId(), seatRequestDto.getBookingDate());
		
// Already request but  except reject: he can again request on that seat
		SeatRequest alreadyRequested = seatRequestRepo.findByRequestStatusAndEmployeeIdAndSeatIdAndBookingDateAndDeletedDateNull(1,
				employeeId, new SeatNumber(seatRequestDto.getSeatId()), seatRequestDto.getBookingDate());

		if (employee.getModeOfWork().getId() == 2) {// WFH

			if (numberOfRequestOnSeat == 3) {// not use
				return "This seat is already requested by Three employee, please choose a different available seat for your booking!";
			}
			else if (countPerDayEmployeeRequest == 3) {
				return "You request limit exceed!";
			} else if (alreadyRequested != null) { //except reject
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

			// send email on owner admin and employee condition check
			

			return "Your seat request has been submitted!";
		}
		return "Hybrid Employee Can not book Seat";
	}
// this method check at up side 
	@Override
	public Boolean seatApproveStatus(String employeeId, LocalDate bookingDate) {

		SeatRequest alreadyApproveSeat = seatRequestRepo
				.findByRequestStatusAndEmployeeIdAndBookingDateAndDeletedDateNull(2, employeeId, bookingDate);
		// check user have approve one seat at a day and he can not book another seat
		// again
//		int alreadyApproveSeat = seatRequestRepo.findRequestStatusByEmployeeIdAndBookingDateAndDeletedDateNull(
//				employeeId, bookingDate);
//		
//		if(alreadyApproveSeat == 2) {
//			
//			return alreadyApproveSeat;
//		}
//		return alreadyApproveSeat;
//	}
		if (alreadyApproveSeat == null) {
			return false;
		}
		return true;

	}

	
	
	@Override
	public String acceptReject(String employeeId, AcceptRejectDto takeAction) throws IOException {

		if (takeAction.getRequestStatus() == 2) {// Approve = 2
			
			// Update for approve
			SeatRequest requestedEmployee = seatRequestRepo.findByRequestStatusAndEmployeeIdAndSeatIdAndBookingDateAndDeletedDateNull(1,
					takeAction.getEmployeeId(), new SeatNumber(takeAction.getSeatId()), takeAction.getBookingDate());

			requestedEmployee.setRequestStatus(2);
			
			requestedEmployee.setModifiedBy(new Employee(employeeId));
			requestedEmployee.setModifiedDate(LocalDateTime.now());

			seatRequestRepo.save(requestedEmployee);
			
			// Call Email service for Approved seat
			//String city = floorRepo.findByFloorName(new Floor(takeAction.getFloor()));
//			
//			Body body = new Body();
//			
//			body.setEmployeeName(requestedEmployee.getEmployee().getFirstName());
//			body.setBookingDate(takeAction.getBookingDate());
//			//body.setCity(city);
//			//body.setFloorName(takeAction.getFloor());
//			body.setSeatNumber(takeAction.getSeatId());
//			body.setDuration("Full Day");
//			
//			String fileName = "/static/seatacceptedofemployee.jsp";
//			EmailDto emailDto = new EmailDto();
//			
//			System.out.println("1st");
//			emailDto.setTo("abhishekpandey81299@gmail.com");//takeAction.getEmailId());
//			emailDto.setSubject("Approval of Your Office Seat in the Deskbook Application System");
//			emailDto.setBody("Dear [Employee's Name], We are pleased to inform you that your office seat request in the Deskbook Application System has been approved. Congratulations! Here are the details regarding your approved seat:  ");
//			
//			ModelMap map = new ModelMap();
//			map.addAttribute("body",body);
//			
//			System.out.println("2st");
//			emailService.sendMailRequest(emailDto, fileName);
			
			
// temp seat owner request sent on different seat which will rejected automatic
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
