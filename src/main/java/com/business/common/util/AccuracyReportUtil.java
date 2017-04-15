package com.business.common.util;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.business.common.dto.AccuracyDTO;
import com.business.common.dto.AccuracyGraphDTO;
import com.business.common.dto.BrandInfoDTO;
import com.business.model.pojo.AccuarcyGraphEntity;
import com.business.model.pojo.AccuracyPercentageEntity;
import com.business.service.BusinessService;

public class AccuracyReportUtil {
	static Logger logger = Logger.getLogger(AccuracyReportUtil.class);
	BusinessService service;

	public void scrapAccuracyReortInfo(BusinessService service) {
		this.service = service;

		List<BrandInfoDTO> clientNames = service.getClientNames();

		for (BrandInfoDTO braninfodto : clientNames) {
			String brandName = braninfodto.getBrandName();
			Integer brandID = braninfodto.getClientId();

			List<String> storeForBrand = service.getStoreForBrand(brandName);

			Integer size = storeForBrand.size();
			logger.info("Total stores found for " + brandName + ", are: "
					+ size);

			int totalAccuracy = 0;
			int totalACountForAvg = 0;

			for (String store : storeForBrand) {
				AccuracyDTO dto = new AccuracyDTO();
				Map<String, Integer> errorsCount = service
						.getErrorsCount(store);

				Integer googleErrors = errorsCount.get("google");
				Integer bingErrors = errorsCount.get("bing");
				Integer yelpErrors = errorsCount.get("yelp");
				Integer YahooErrors = errorsCount.get("yahoo");
				Integer ypErrors = errorsCount.get("yp");
				Integer citysearchErrors = errorsCount.get("citysearch");
				Integer mapquestErrors = errorsCount.get("mapquest");
				Integer superpagesErrors = errorsCount.get("superpages");
				Integer yellobookErrors = errorsCount.get("yellowbook");
				Integer whitepagesErrors = errorsCount.get("whitepages");
				int greenCount = 0;
				if (googleErrors != -1) {
					greenCount++;
				}
				if (bingErrors != -1) {
					greenCount++;
				}
				if (yelpErrors != -1) {
					greenCount++;
				}
				if (YahooErrors != -1) {
					greenCount++;
				}
				if (ypErrors != -1) {
					greenCount++;
				}
				if (citysearchErrors != -1) {
					greenCount++;
				}

				if (mapquestErrors != -1) {
					greenCount++;
				}

				if (superpagesErrors != -1) {
					greenCount++;
				}

				if (yellobookErrors != -1) {
					greenCount++;
				}

				if (whitepagesErrors != -1) {
					greenCount++;
				}

				int accuracy = greenCount % 10 * 10;
				/*
				 * logger.info("greenCount: " + greenCount + ", accuracy: " +
				 * accuracy);
				 */
				// accuracy = 100 - accuracy;

				if (greenCount == 0) {
					accuracy = 0;
				}

				if (googleErrors == -1) {
					dto.setGoogleColourCode("D");
				} else if (googleErrors == 0) {
					dto.setGoogleColourCode("G");
				} else if (googleErrors >= 7) {
					dto.setGoogleColourCode("R");
				} else {
					dto.setGoogleColourCode("O");
				}

				int googleAccuracy = getAccuracy(googleErrors);
				dto.setGoogleAccuracy(googleAccuracy);
				if (googleAccuracy != 0) {
					totalAccuracy = totalAccuracy + googleAccuracy;
					totalACountForAvg++;
				}

				if (bingErrors == -1) {
					dto.setBingColourCode("D");
				} else if (bingErrors == 0) {
					dto.setBingColourCode("G");
				} else if (bingErrors >= 7) {
					dto.setBingColourCode("R");
				} else {
					dto.setBingColourCode("O");
				}
				int bingAccuracy = getAccuracy(bingErrors);
				dto.setBingAccuracy(bingAccuracy);
				if (bingAccuracy != 0) {
					totalAccuracy = totalAccuracy + bingAccuracy;
					totalACountForAvg++;
				}

				if (yelpErrors == -1) {
					dto.setYelpColourCode("D");
				} else if (yelpErrors == 0) {
					dto.setYelpColourCode("G");
				} else if (yelpErrors >= 7) {
					dto.setYelpColourCode("R");
				} else {
					dto.setYelpColourCode("O");
				}
				int yelpAccuracy = getAccuracy(yelpErrors);
				dto.setYelpAccuracy(yelpAccuracy);
				if (yelpAccuracy != 0) {
					totalAccuracy = totalAccuracy + yelpAccuracy;
					totalACountForAvg++;
				}
				if (ypErrors == -1) {
					dto.setYpColourCode("D");
				} else if (ypErrors == 0) {
					dto.setYpColourCode("G");
				} else if (ypErrors >= 7) {
					dto.setYpColourCode("R");
				} else {
					dto.setYpColourCode("O");
				}
				int ypAccuracy = getAccuracy(ypErrors);
				dto.setYpAccuracy(ypAccuracy);
				if (ypAccuracy != 0) {
					totalAccuracy = totalAccuracy + ypAccuracy;
					totalACountForAvg++;
				}
				if (whitepagesErrors == -1) {
					dto.setWhitepagesColourCode("D");
				} else if (whitepagesErrors == 0) {
					dto.setWhitepagesColourCode("G");
				} else if (whitepagesErrors >= 7) {
					dto.setWhitepagesColourCode("R");
				} else {
					dto.setWhitepagesColourCode("O");
				}
				int whitepagesAccuarcy = getAccuracy(whitepagesErrors);
				dto.setWhitepagesAccuracy(whitepagesAccuarcy);
				if (whitepagesAccuarcy != 0) {
					totalAccuracy = totalAccuracy + whitepagesAccuarcy;
					totalACountForAvg++;
				}
				if (superpagesErrors == -1) {
					dto.setSuperpagesColourCode("D");
				} else if (superpagesErrors == 0) {
					dto.setSuperpagesColourCode("G");
				} else if (superpagesErrors >= 7) {
					dto.setSuperpagesColourCode("R");
				} else {
					dto.setSuperpagesColourCode("O");
				}
				int superpagesAccuarcy = getAccuracy(superpagesErrors);
				dto.setSuperPagesAccuracy(superpagesAccuarcy);
				if (superpagesAccuarcy != 0) {
					totalAccuracy = totalAccuracy + superpagesAccuarcy;
					totalACountForAvg++;
				}
				if (mapquestErrors == -1) {
					dto.setMapquestColourCode("D");
				} else if (mapquestErrors == 0) {
					dto.setMapquestColourCode("G");
				} else if (mapquestErrors >= 7) {
					dto.setMapquestColourCode("R");
				} else {
					dto.setMapquestColourCode("O");
				}
				int mapQuestAccuracy = getAccuracy(mapquestErrors);
				dto.setMapQuestAccuracy(mapQuestAccuracy);
				if (mapQuestAccuracy != 0) {
					totalAccuracy = totalAccuracy + mapQuestAccuracy;
					totalACountForAvg++;
				}
				if (citysearchErrors == -1) {
					dto.setCitySearchColourCode("D");
				} else if (citysearchErrors == 0) {
					dto.setCitySearchColourCode("G");
				} else if (citysearchErrors >= 7) {
					dto.setCitySearchColourCode("R");
				} else {
					dto.setCitySearchColourCode("O");
				}
				int citySearchAccuracy = getAccuracy(citysearchErrors);
				dto.setCitySearchAccuracy(citySearchAccuracy);
				if (citySearchAccuracy != 0) {
					totalAccuracy = totalAccuracy + citySearchAccuracy;
					totalACountForAvg++;
				}
				if (YahooErrors == -1) {
					dto.setYahooColourCode("D");
				} else if (YahooErrors == 0) {
					dto.setYahooColourCode("G");
				} else if (YahooErrors >= 7) {
					dto.setYahooColourCode("R");
				} else {
					dto.setYahooColourCode("O");
				}
				int yahooAccuracy = getAccuracy(YahooErrors);
				dto.setYahooAccuracy(yahooAccuracy);
				if (yahooAccuracy != 0) {
					totalAccuracy = totalAccuracy + yahooAccuracy;
					totalACountForAvg++;
				}
				if (yellobookErrors == -1) {
					dto.setYellowbookColourCode("D");
				} else if (YahooErrors == 0) {
					dto.setYellowbookColourCode("G");
				} else if (YahooErrors >= 7) {
					dto.setYellowbookColourCode("R");
				} else {
					dto.setYellowbookColourCode("O");
				}
				int yellowBookAccuracy = getAccuracy(yellobookErrors);
				dto.setYellowbookAccuracy(yellowBookAccuracy);
				if (yellowBookAccuracy != 0) {
					totalAccuracy = totalAccuracy + yellowBookAccuracy;
					totalACountForAvg++;
				}
				if (totalACountForAvg != 0) {
					int avg = totalAccuracy / totalACountForAvg;
					dto.setAverageAccuracy(avg);
				} else {
					dto.setAverageAccuracy(0);
				}

				dto.setStore(store);
				dto.setBrandname(brandName);
				dto.setAccuracy(accuracy);

				Integer listingid = service.isStoreExistInAccuracy(store,
						brandName);
				if (listingid != 0) {
					dto.setId(listingid);
					service.updateAccuracyInfo(dto);
				} else {
					service.saveAccuracyInfo(dto);
				}
			}
			

			int percentageCategory1 = service.getPercentagecategory1(brandName);
			int percentageCategory2 = service.getPercentagecategory2(brandName);
			int percentageCategory3 = service.getPercentagecategory3(brandName);
			int percentageCategory4 = service.getPercentagecategory4(brandName);
			double totalPercentage=service.getTotalPercentage(brandName);
			totalPercentage=Math.round(totalPercentage);
			AccuracyPercentageEntity percentageEntity=new AccuracyPercentageEntity();
			percentageEntity.setPercentage1(percentageCategory1);
			percentageEntity.setPercentage2(percentageCategory2);
			percentageEntity.setPercentage3(percentageCategory3);
			
			percentageEntity.setPercentage4(percentageCategory4);
			percentageEntity.setTotalPercentage(totalPercentage);
			percentageEntity.setBrandId(brandID);
			
			service.saveAccuracyPercentageInfo(percentageEntity);
			
			AccuracyGraphDTO accuracyGraphDTO = new AccuracyGraphDTO();
			accuracyGraphDTO.setBrandName(brandName);
			accuracyGraphDTO.setListingCount(size.longValue());
			accuracyGraphDTO.setDate(new Date());
			service.saveAccuracyGraphInfo(accuracyGraphDTO);

			logger.info("Calculating the accuracy percentages are completed for Brand: "
					+ brandName);
		}

	}

	int getAccuracy(int errorCount) {
		if (errorCount == -1) {
			return 0;
		}
		int inAccuracy = (int) ((errorCount * 100.0f) / 7);
		return 100 - inAccuracy;
	}
}
