package com.onerivet.deskbook.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
	//@Query(value = "SELECT COUNT(s) FROM Booking.SeatRequest s WHERE s.EmployeeId=:employeeId")
	public int countFindByEmployeeIdAndBookingDateAndDeletedDateNull(String employeeId, LocalDate bookingDate);
	
	public int findRequestStatusByEmployeeIdAndBookingDateAndDeletedDateNull(String employeeId, LocalDate bookingDate);
	
//		@Query(value = "SELECT * FROM Booking.SeatRequest WHERE SeatRequest.SeatId ? ", nativeQuery = true)
		public List<SeatRequest> findSeatRequestBySeatId(SeatNumber seatId );

		// Update Approve
		public SeatRequest findByRequestStatusAndEmployeeIdAndBookingDateAndDeletedDateNull(int i,
				String employeeId, LocalDate bookingDate);
		
		// Baki ni reject
		public List<SeatRequest> findByEmployeeIdAndRequestStatusAndBookingDateAndDeletedDateNull(String employee,int requestStatus, LocalDate bookingDate);
// Auto approve
		//@Query("SELECT s FROM SeatRequest s WHERE s.requestStatus=2 ORDER BY s.bookingDate asc")//, s.createdDate DESC")
	  // @Query(value = "SELECT TOP 1 * FROM Booking.SeatRequest s WHERE s.RequestStatus=2 ORDER BY s.BookingDate , s.CreatedDate DESC", nativeQuery = true)
		
		//@Query("SELECT s FROM SeatRequest s WHERE s.bookingDate =:bookingDate , ")
		public List<SeatRequest> findByBookingDateAndRequestStatusAndDeletedDateNull(LocalDate bookingDate, int requestStatus, Sort sort);
		
		
}
