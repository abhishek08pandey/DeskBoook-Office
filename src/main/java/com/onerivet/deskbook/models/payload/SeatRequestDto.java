package com.onerivet.deskbook.models.payload;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SeatRequestDto {

	private int seatId;
	private LocalDate bookingDate;
	private String reason;
	
	//private String seatType;
}
