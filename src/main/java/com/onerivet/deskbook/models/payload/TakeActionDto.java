package com.onerivet.deskbook.models.payload;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TakeActionDto {
	  
	private String employeeId;// requested employee
       
	    private  LocalDate bookingDate;
	        
	    private int seatId;
	        
	    private int requestStatus;//Approve = 2, reject = 3
	       
	}