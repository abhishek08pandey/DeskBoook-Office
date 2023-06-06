package com.onerivet.deskbook.models.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatOwnerDetailsDto {

	// owner amd book person
	//
	private String firstName;
	private String lastName;
	private String designation;
	private String emailId;
	//A person who get seat by approving owner
	private int countOfRequest;
	
	//private int requestStatus;
	
	private TemporarySeatOwnerDto temporarySeatOwner;
	
}
