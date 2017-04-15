package com.business.model.dataaccess;

import java.util.List;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.ChannelNameDTO;

public interface ManageBrandDao {
	List<ChannelNameDTO> getChannel();

	List<BrandInfoDTO> getBrandsByChannelID(Integer channelID);

	void deleteBrands(List<Integer> listofbrand);

	void deleteChannel(Integer channelID);
	void deleteChannels(List<Integer> listofchannels);
}
