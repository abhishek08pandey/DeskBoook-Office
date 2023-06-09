package com.onerivet.deskbook.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.onerivet.deskbook.models.entity.Employee;
import com.onerivet.deskbook.models.entity.SeatConfiguration;
import com.onerivet.deskbook.models.entity.SeatNumber;
import com.onerivet.deskbook.models.entity.SeatRequest;
import com.onerivet.deskbook.models.payload.AcceptRejectDto;
import com.onerivet.deskbook.repository.SeatConfigurationRepo;
import com.onerivet.deskbook.repository.SeatRequestRepo;
import com.onerivet.deskbook.services.SeatRequestService;

@Service
public class AutoApproveSheduleServiceImple {

	@Autowired
	private SeatRequestRepo seatRequestRepo;

//	@Autowired
//	private SeatRequestService seatRequestService;
//
//	@Autowired
//	private SeatConfigurationRepo seatConfigurationRepo;

	 @Scheduled(cron = "0 0 0 * * *") // Run at 12 AM (midnight) every day
	//@Scheduled(cron = "0 * * * * *") // every minute
	public void acceptRequestwithin24Hours() {

		LocalDate bookingDate = LocalDate.now().plusDays(1);

		Sort sort = Sort.by(Sort.Direction.ASC, "CreatedDate");
		List<SeatRequest> pendingSeatRequest = seatRequestRepo
				.findByBookingDateAndRequestStatusAndDeletedDateNull(bookingDate, 1, sort);

		if (!pendingSeatRequest.isEmpty()) {

//			SeatConfiguration seatConfiguration = seatConfigurationRepo.findBySeatNumberAndDeletedByNull(
//					new SeatNumber(pendingSeatRequest.get(0).getSeatId().getSeatNumber()));

			System.out.println(bookingDate);
// Send all data to TakeAction class for accept request and reject other seat request 		
			AcceptRejectDto takeAction = new AcceptRejectDto();

			takeAction.setSeatId(pendingSeatRequest.get(0).getSeatId().getSeatNumber());
			takeAction.setEmployeeId(pendingSeatRequest.get(0).getEmployee().getId());
			takeAction.setBookingDate(pendingSeatRequest.get(0).getBookingDate());
			takeAction.setRequestStatus(2);

			// seatRequestService.takeAction(seatConfiguration.getEmployee().getId(),
			// takeActionDto);
			if (takeAction.getRequestStatus() == 2) {// Approve = 2

				// Update for approve
				SeatRequest requestedEmployee = seatRequestRepo
						.findByRequestStatusAndEmployeeIdAndSeatIdAndBookingDateAndDeletedDateNull(1,
								takeAction.getEmployeeId(), new SeatNumber(takeAction.getSeatId()),
								takeAction.getBookingDate());

				requestedEmployee.setRequestStatus(2);
				//requestedEmployee.setModifiedBy(new Employee(employeeId));
				requestedEmployee.setModifiedDate(LocalDateTime.now());

				// Call Email service for Approved seat

				seatRequestRepo.save(requestedEmployee);

// temp seat owner request sent on different seat which will rejected automatic
				List<SeatRequest> rejectSeatList = seatRequestRepo
						.findByEmployeeIdAndRequestStatusAndBookingDateAndDeletedDateNull(takeAction.getEmployeeId(), 1,
								takeAction.getBookingDate());

				if (rejectSeatList != null) {

					for (int i = 0; i < rejectSeatList.size(); i++) {

						rejectSeatList.get(i).setRequestStatus(3);
						//rejectSeatList.get(i).setModifiedBy(new Employee(employeeId));
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
						//otherEmployeeRequest.get(i).setModifiedBy(new Employee(employeeId));
						otherEmployeeRequest.get(i).setModifiedDate(LocalDateTime.now());

						// Call Email service for rejected seat

						seatRequestRepo.save(otherEmployeeRequest.get(i));
					}
				}
				//return "Seat Approved";

			}
		}
	}
}
