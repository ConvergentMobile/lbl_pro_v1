package com.business.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.ChannelNameDTO;
import com.business.model.dataaccess.ManageBrandDao;
import com.business.service.ManageBrandService;
@Service
public class ManageBrandServiceImpl implements ManageBrandService{
@Autowired
private ManageBrandDao manageBrandDao;
	@Transactional
	public List<ChannelNameDTO> getChannel() {
		
		return manageBrandDao.getChannel();
	}

	@Transactional
	public List<BrandInfoDTO> getBrandsByChannelID(Integer channelID) {
		
		return manageBrandDao.getBrandsByChannelID(channelID);
	}

	@Transactional
	public void deleteBrands(List<Integer> listofbrand) {
		
		manageBrandDao.deleteBrands(listofbrand);
	}

	@Transactional
	public void deleteChannel(Integer channelID) {
		manageBrandDao.deleteChannel(channelID);
		
	}

	@Transactional
	public void deleteChannels(List<Integer> listofchannels) {
		manageBrandDao.deleteChannels(listofchannels);
		
	}

}
