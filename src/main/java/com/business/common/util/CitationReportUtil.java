package com.business.common.util;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.business.common.dto.AccuracyDTO;
import com.business.common.dto.AccuracyGraphDTO;
import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.model.pojo.CitationGraphEntity;
import com.business.model.pojo.CitationReportEntity;
import com.business.service.BusinessService;

public class CitationReportUtil {
	static Logger logger = Logger.getLogger(CitationReportUtil.class);
	BusinessService service;

	public void scrapCitationReortInfo(BusinessService service) {
		this.service = service;
		List<BrandInfoDTO> Brands = service.getClientNames();
		for (BrandInfoDTO brandInfoDTO : Brands) {
			int pathCountForBrand =0;
			logger.info("Start: Preparing the Citation Report data for: " + brandInfoDTO.getBrandName());
			
			Integer brandID = brandInfoDTO.getClientId();
			List<LocalBusinessDTO> listOfBussinessinfo = service
					.getListOfBussinessByBrandId(brandID);
			Map<String, Integer> pathCountMapForStores = service
					.getPathCountMapForStores(listOfBussinessinfo);
			for (LocalBusinessDTO localBusinessDTO : listOfBussinessinfo) {
				CitationReportEntity citationReportEntity = new CitationReportEntity();
				citationReportEntity.setBrandId(brandID);
				citationReportEntity.setAddress(localBusinessDTO
						.getLocationAddress());
				citationReportEntity.setBusinesName(localBusinessDTO
						.getCompanyName());
				citationReportEntity
						.setCity(localBusinessDTO.getLocationCity());
				citationReportEntity.setState(localBusinessDTO
						.getLocationState());
				citationReportEntity.setPhone(localBusinessDTO
						.getLocationPhone());
				citationReportEntity.setZip(localBusinessDTO
						.getLocationZipCode());
				String store = localBusinessDTO.getStore();
				citationReportEntity.setStore(store);
				Map<String, List<String>> paths = service.getPathFromSearch(
						store, brandID);
				List<String> list = paths.get(store);
				StringBuffer pathsval = new StringBuffer();
				int countpath = 0;
				for (String string : list) {
					countpath++;

					pathsval.append(string);
					if (list.size() > 1 && countpath != list.size()) {
						pathsval.append(",");
					}

				}

				citationReportEntity.setPaths(pathsval.toString());

				Map<String, List<String>> domains = service
						.getDomainAuthorities(store, brandID);
				List<String> domainslist = domains.get(store);
				StringBuffer domainAuthorities = new StringBuffer();
				int count = 0;
				for (String string : domainslist) {
					count++;
					domainAuthorities.append(string);
					if (domainslist.size() > 1 && count != domainslist.size()) {
						domainAuthorities.append(",");
					}

				}
				citationReportEntity.setDomainAuthority(domainAuthorities
						.toString());

				Integer pathCount = pathCountMapForStores.get(store);
				citationReportEntity.setPathCount(pathCount);

				Integer citationId = service.isStoreAndBrandExistInCitation(
						store, brandID);
				if (citationId != 0) {
					citationReportEntity.setCitationId(citationId);
					service.updateCitationreportInfo(citationReportEntity);
				} else {
					service.saveCitationreportInfo(citationReportEntity);
				}
				pathCountForBrand = pathCountForBrand+pathCount;

			}

			int pathCount = pathCountMapForStores.size();
			CitationGraphEntity graphEntity = new CitationGraphEntity();
			graphEntity.setBrandId(brandID);
			graphEntity.setBrandName(brandInfoDTO.getBrandName());
			graphEntity.setDate(new Date());
			graphEntity.setCitationCount(pathCountForBrand);
			service.saveCitationGraphInfo(graphEntity);
			
			logger.info("End: Preparing the Citation Report data for: " + brandInfoDTO.getBrandName());
		}

	}

}
