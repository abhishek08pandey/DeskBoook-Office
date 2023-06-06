package com.onerivet.deskbook.services;

import java.util.List;

import com.onerivet.deskbook.models.payload.RequestHistoryDto;

public interface RequestHistoryService {
	public List<RequestHistoryDto> getRequestHistory(String employeeId);
}
