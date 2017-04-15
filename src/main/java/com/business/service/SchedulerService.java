package com.business.service;

import java.util.List;

import com.business.common.dto.PartnerDTO;
import com.business.common.dto.SchedulerDTO;

public interface SchedulerService {
	public List<SchedulerDTO> getScheduleListing();

	public List<PartnerDTO> getPartners();

	public void deleteSchedules(List<Integer> listIds);

	public boolean saveSchedule(SchedulerDTO dto);

}
