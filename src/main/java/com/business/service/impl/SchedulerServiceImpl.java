package com.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.PartnerDTO;
import com.business.common.dto.SchedulerDTO;
import com.business.model.dataaccess.SchedulerDao;
import com.business.service.SchedulerService;

@Service
public class SchedulerServiceImpl implements SchedulerService {
	@Autowired
	private SchedulerDao schedulerDao;

	@Transactional
	public List<SchedulerDTO> getScheduleListing() {
		return schedulerDao.getScheduleListing();
	}

	@Transactional
	public List<PartnerDTO> getPartners() {
		return schedulerDao.getPartners();
	}

	@Transactional
	public void deleteSchedules(List<Integer> listIds) {
		schedulerDao.deleteSchedules(listIds);
	}

	@Transactional
	public boolean saveSchedule(SchedulerDTO dto) {
		return schedulerDao.saveSchedule(dto);
	}

}
