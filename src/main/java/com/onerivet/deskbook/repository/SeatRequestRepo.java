package com.onerivet.deskbook.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.onerivet.deskbook.models.entity.SeatNumber;
import com.onerivet.deskbook.models.entity.SeatRequest;

public interface SeatRequestRepo extends JpaRepository<SeatRequest, Integer> {

// Check total 3 request 
	public SeatRequest findByRequestStatusAndEmployeeIdAndSeatIdAndBookingDateAndDeletedDateNull(int pending, String employee, SeatNumber seatId, LocalDate bookingDate);

	// numberOfRequestOnSeat <= 3
	public int countFindByRequestStatusAndSeatIdAndBookingDateAndDeletedDateNull(int pending, SeatNumber seatId, LocalDate bookingDate);
	
	public SeatRequest findByRequestStatusAndBookingDateAndSeatIdAndDeletedDateNull(int requestStatus, LocalDate bookingDate,  SeatNumber seatId);
	
	public List<SeatRequest> getByRequestStatusAndBookingDateAndSeatIdAndDeletedDateNull(int requestStatus, LocalDate bookingDate,  SeatNumber seatId);

	
//	@Query(value = "SELECT COUNT(*) FROM Booking.SeatRequest s WHERE s.EmployeeId=? AND s.BookingDate=? AND s.DeletedDate=:NULL ", nativeQuery = true)//AND s.DeletedDate=:NULL")
//	
//	public int countFindByEmployeeIdBookingDate(String employeeId, LocalDate bookingDate);
	//@Query(value = "SELECT COUNT(s) FROM Booking.SeatRequest s WHERE s.EmployeeId=:employeeId")
	public int countFindByEmployeeIdAndBookingDateAndDeletedDateNull(String employeeId, LocalDate bookingDate);
	
	public int findRequestStatusByEmployeeIdAndBookingDateAndDeletedDateNull(String employeeId, LocalDate bookingDate);
	
	
	
	//@Query(value = "SELECT SeatRequest s WHERE s.seatId =:seatId")
		//@Query(value = "SELECT * FROM Booking.SeatRequest WHERE SeatId = seatId", nativeQuery = true)
//		@Query(value = "SELECT * FROM Booking.SeatRequest WHERE SeatRequest.SeatId ? ", nativeQuery = true)
		public List<SeatRequest> findSeatRequestBySeatId(SeatNumber seatId );

		// Update Approve
		public SeatRequest findByRequestStatusAndEmployeeIdAndBookingDateAndDeletedDateNull(int i,
				String employeeId, LocalDate bookingDate);
		
		// Baki ni reject
		public List<SeatRequest> findByEmployeeIdAndRequestStatusAndBookingDateAndDeletedDateNull(String employee,int requestStatus, LocalDate bookingDate);
//		@Transactional
//		@Modifying
//		@Query(value = "UPDATE Booking.SeatRequest  SET RequestStatus = 2  WHERE EmployeeId=employeeId AND SeatId=seatId And BookingDate=bookingDate", nativeQuery = true)
//        public void approve(String employeeId, SeatNumber seatId, LocalDate bookingDate, int requestStatus); 
}
