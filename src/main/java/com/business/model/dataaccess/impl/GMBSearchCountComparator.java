package com.business.model.dataaccess.impl;

import java.util.Comparator;

import com.business.common.dto.InsightsGraphDTO;

public class GMBSearchCountComparator implements Comparator<InsightsGraphDTO> {

	public int compare(InsightsGraphDTO dto1, InsightsGraphDTO dto2) {
		if (dto1.getTotalSearchCount() > dto1.getTotalSearchCount()) {
			return 1;
		} else {
			return -1;
		}
	}

}
