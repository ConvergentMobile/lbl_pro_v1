package com.business.common.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.service.BusinessService;
import com.whitespark.ws.ScrapUtil;

public class SmartyStreetsThread extends Thread{
public Logger logger = Logger.getLogger(ScrappingThread.class);
	
	BusinessService service = null;
	List<LocalBusinessDTO> correctBusinessList=null;
	List<LocalBusinessDTO> updateBusinesList=null;
	List<LblErrorDTO> inCorrectBusinessList=null;
	List<LblErrorDTO> listOfErrorUpdatesByActionCode=null;
	/*public SmartyStreetsThread(BusinessService service, ) {
		this.service = service;
	}
*/
	public SmartyStreetsThread(BusinessService service2,
			List<LocalBusinessDTO> correctRecords,
			List<LocalBusinessDTO> updateBusinessRecords,
			List<LblErrorDTO> inCorrectDataList,List<LblErrorDTO> listOfErrorUpdatesByActionCode) {
		this.correctBusinessList = correctRecords;
		this.updateBusinesList=updateBusinessRecords;
		this.service=service2;
		this.inCorrectBusinessList=inCorrectDataList;
	
	}

	public void run() {
		logger.info("start ::  SmartyStreetsThread  ");
		SmartyStreetsUtil util = new SmartyStreetsUtil();
		try {
			Thread.sleep(300*1000);
			util.validateAndUpdateResult(this.service,
					correctBusinessList,
					updateBusinesList,
					inCorrectBusinessList,listOfErrorUpdatesByActionCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("end ::  SmartyStreetsThread  ");
	}

}
