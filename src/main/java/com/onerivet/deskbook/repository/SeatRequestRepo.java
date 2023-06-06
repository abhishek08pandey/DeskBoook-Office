package com.onerivet.deskbook.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onerivet.deskbook.models.entity.Employee;
import com.onerivet.deskbook.models.entity.SeatNumber;
import com.onerivet.deskbook.models.entity.SeatRequest;

public interface SeatRequestRepo extends JpaRepository<SeatRequest, Integer> {

	public SeatRequest findByEmployeeIdAndSeatIdAndBookingDateAndDeletedDateNull(String employee, SeatNumber seatId, LocalDate bookingDate);
	
	public int countFindBySeatIdAndBookingDateAndDeletedDateNull(SeatNumber seatId, LocalDate bookingDate);
	
	public SeatRequest findByRequestStatusAndBookingDateAndSeatIdAndDeletedDateNull(int requestStatus, LocalDate bookingDate,  SeatNumber seatId);

	
	public int countFindByEmployeeIdBookingDateAndDeletedDateNull(String employeeId, LocalDate bookingDate);
//	
//	public int findRequestStatusByEmployeeIdAndBookingDateAndDeletedDateNull(String employee, LocalDate bookingDate);
	
	
	
	//@Query(value = "SELECT SeatRequest s WHERE s.seatId =:seatId")
		//@Query(value = "SELECT * FROM Booking.SeatRequest WHERE SeatId = seatId", nativeQuery = true)
//		@Query(value = "SELECT * FROM Booking.SeatRequest WHERE SeatRequest.SeatId ? ", nativeQuery = true)
		public List<SeatRequest> findSeatRequestBySeatId(SeatNumber seatId );
}
