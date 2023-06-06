package com.onerivet.deskbook.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onerivet.deskbook.models.entity.Employee;
import com.onerivet.deskbook.models.entity.ModeOfWork;
import com.onerivet.deskbook.models.entity.SeatConfiguration;
import com.onerivet.deskbook.models.entity.SeatNumber;
import com.onerivet.deskbook.models.entity.SeatRequest;
import com.onerivet.deskbook.models.payload.RequestHistoryDto;
import com.onerivet.deskbook.models.payload.SeatRequestDto;
import com.onerivet.deskbook.repository.SeatConfigurationRepo;
import com.onerivet.deskbook.repository.SeatRequestRepo;
import com.onerivet.deskbook.services.RequestHistoryService;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class RequestHistoryImpl  implements RequestHistoryService{
	
	
	@Autowired
	private SeatRequestRepo seatRequestRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired private SeatConfigurationRepo seatConfigurationRepo;
	
	public List<RequestHistoryDto> getRequestHistory(String employeeId) {
		
	//	return this.seatRequestRepo.findRequestHistoryById(id).stream().map((rh) -> this.modelMapper.map(rh, RequestHistoryDto.class)).collect(Collectors.toList());
		SeatConfiguration seatConfiguration = seatConfigurationRepo.findByEmployeeAndDeletedByNull(new Employee(employeeId));
		
		ModeOfWork modeOfWork = seatConfiguration.getEmployee().getModeOfWork();
		System.out.println(modeOfWork);
		
		System.out.println("mode");
		
		List<SeatRequest> sr = this.seatRequestRepo.findSeatRequestBySeatId(seatConfiguration.getSeatNumber());
		System.out.println("11mode");
		
		//List<SeatRequestDto> collect = sr.stream().map((seat)->modelMapper.map(seat, SeatRequestDto.class)).collect(Collectors.toList());
		
		System.out.println("22mode");
		
		
		List<RequestHistoryDto> list = new ArrayList<>();
		ListIterator<SeatRequest> listIterator = sr.listIterator();
		System.out.println("33mode");
		
//		while(listIterator.hasNext()) {
//			listIterator.next();
//			SeatRequest seatRequest = listIterator.next();
//			System.out.println("44mode");
//			requestHistoryDto.setName(seatRequest.getEmployee().getFirstName() + "" +seatRequest.getEmployee().getLastName());
//			requestHistoryDto.setDeskNo(seatRequest.getSeatId().getColumn().getColumnName() + "" + seatRequest.getSeatId().getSeatNumber());
//			requestHistoryDto.setEmailId(seatRequest.getEmployee().getEmailId());
//			requestHistoryDto.setRequestFor(seatRequest.getBookingDate());
//			requestHistoryDto.setRequestDate(seatRequest.getCreatedDate());
//			requestHistoryDto.setFloorNo(seatRequest.getSeatId().getColumn().getFloor().getId());
//			requestHistoryDto.setStatus(seatRequest.getRequestStatus());
//			
//			
//			System.out.println("55mode");
//			list.add(requestHistoryDto);
//		}
		System.out.println("66mode");
		for(int i=0; i<sr.size(); i++ ) {
			RequestHistoryDto requestHistoryDto = new RequestHistoryDto();
			requestHistoryDto.setName(sr.get(i).getEmployee().getFirstName() + "" + sr.get(i).getEmployee().getLastName());
			requestHistoryDto.setDeskNo(sr.get(i).getSeatId().getColumn().getColumnName() + "" + sr.get(i).getSeatId().getSeatNumber());
			requestHistoryDto.setEmailId(sr.get(i).getEmployee().getEmailId());
			requestHistoryDto.setRequestFor(sr.get(i).getBookingDate());
			requestHistoryDto.setRequestDate(sr.get(i).getCreatedDate());
			requestHistoryDto.setFloorNo(sr.get(i).getSeatId().getColumn().getFloor().getId());
			requestHistoryDto.setStatus(sr.get(i).getRequestStatus());
			
			
			list.add(requestHistoryDto);
		}
		return list;
	}

}