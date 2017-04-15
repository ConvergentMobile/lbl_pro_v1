package com.business.model.dataaccess;

import java.util.List;

import com.business.common.dto.PartnerDTO;
import com.business.common.dto.SchedulerDTO;

public interface SchedulerDao {
	public List<SchedulerDTO> getScheduleListing();

	public List<PartnerDTO> getPartners();

	public void deleteSchedules(List<Integer> listIds);

	public boolean saveSchedule(SchedulerDTO dto);

}
