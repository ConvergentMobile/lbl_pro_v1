package com.business.common.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.ChannelNameDTO;
import com.business.common.dto.CustomSubmissionsDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.SchedulerDTO;
import com.business.model.pojo.ExportReportEntity;
import com.business.service.BusinessService;
import com.partners.acxiom.AcxiomClient;
import com.partners.factual.FactualClient;
import com.partners.infogroup.InfogroupClient;
import com.partners.neustar.NeustarLocalezeClient;

public class SubmissionDemonThread extends Thread {
	Logger logger = Logger.getLogger(SubmissionDemonThread.class);

	CustomSubmissionsDTO schedulerDTO;
	BusinessService service;

	public SubmissionDemonThread(BusinessService service,
			CustomSubmissionsDTO dto) {
		this.schedulerDTO = dto;
		this.service = service;
	}

	public void run() {

		String brandName = schedulerDTO.getBrandName();

		Map<String, List<BrandInfoDTO>> allActiveBrandsMap = service
				.getActivePartners(brandName);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM");
		SubmissionUtil submissionUtil = new SubmissionUtil();
		Date date = new Date();
		String threadName = brandName + "_" + sdf.format(date);
		Thread.currentThread().setName(threadName);

		String infoGroupRecurrenecy = schedulerDTO.getInfogroupFrequency();
		Date infoGroupScheduledDate = schedulerDTO.getInfogroupScheduledDate();
		String factualRecurrenecy = schedulerDTO.getFactualFrequency();
		Date factualScheduledDate = schedulerDTO.getFactualScheduledDate();
		String acxiomRecurrenecy = schedulerDTO.getAcxiomFrequency();
		Date acxiomScheduledDate = schedulerDTO.getAcxiomScheduledDate();
		String localezeRecurrenecy = schedulerDTO.getLocalezeFrequency();
		Date localezeScheduledDate = schedulerDTO.getLocalezeScheduledDate();

		String toMail = LBLConstants.NOTIFY_MAIL;
		List<BrandInfoDTO> brandInfoDTOs = allActiveBrandsMap.get(brandName);

		if (brandInfoDTOs != null) {

			for (BrandInfoDTO brandInfoDTO : brandInfoDTOs) {
				int numberOfFailedRecords = 0;
				boolean isAPI=false;
				logger.info("Triggering Scheduled Submission for: " + brandName);

				if (brandInfoDTO.getSubmisions() != null) {
					String partner = brandInfoDTO.getSubmisions();

					List<LocalBusinessDTO> localBusinessDTOs = service
							.getListOfBussinessByBrandName(brandName);

					int numberOfRecords = localBusinessDTOs.size();
					String lblAcxiom = LBLConstants.LBL_ACXIOM;

					if (partner.equalsIgnoreCase(lblAcxiom)) {
						isAPI = true;

						if (isToday(acxiomScheduledDate)) {

							logger.info("Writing the data to excel for: "
									+ partner + " for Brand: "
									+ brandInfoDTO.getBrandName());
							String path = LBLConstants.ACXIOM_FILE_PATH;
							try {
								Random rand = new Random();
								int randomNumber = rand.nextInt(1000) + 1;
								String brand = brandInfoDTO.getBrandName();
								path = path.concat("_" + brand + "_"
										+ randomNumber + ".csv");
								try {

									
									submissionUtil.initializeService(service);

									submissionUtil.write(localBusinessDTOs,
											path);
									AcxiomClient.initializeAcxiomService();
									AcxiomClient.uploadAcxiomFile(path,
											brandInfoDTO.getClientId());
									logger.debug("Uploading the Data to Acxiom for Brand "
											+ brand + " is successful");
									insertIntoExportInfo(brandInfoDTO,
											brandName, numberOfRecords);
									
									SubmissionUtil.sendMail(null, brandName,
											partner);
									
									submissionUtil.updateRenewal(localBusinessDTOs, partner,brandName);
								} catch (Exception e) {
									logger.error("There was a error while Uplaoding the Data to Acxiom for Brand "
											+ brand);
									numberOfRecords = 0;
									String subject = brandName
											+ " Submission Status to "
											+ partner + " is FAILED";
									MailUtil.sendNotification(toMail, "",
											subject);
									e.printStackTrace();
								}
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								Date nextScheduledDate = getNextScheduledDate(
										acxiomRecurrenecy, acxiomScheduledDate);
								schedulerDTO
										.setAcxiomLastRunDate(acxiomScheduledDate);
								schedulerDTO
										.setAcxiomScheduledDate(nextScheduledDate);

								try {
									service.updateCustomSubmissions(
											schedulerDTO, brandName);
									logger.info("Updating schedule is successful");
								} catch (Exception e) {
									logger.error(
											"There was a failure while updating schedule to run next time",
											e);
								}
								// remove file
								File file = new File(path);
								if (file.exists()) {
									logger.debug("deleting the file after submitting to acxiom: "
											+ path);
									file.delete();
								}
							}
						}
					}
					if (partner.equalsIgnoreCase(LBLConstants.LBL_INFOGROUP)) {
						isAPI = true;
						if (isToday(infoGroupScheduledDate)) {
							logger.info("Sending info to " + partner
									+ ", and the total businesses are: "
									+ numberOfRecords);
							try {

								Map<String, List<String>> errorDetails = InfogroupClient
										.postToInfogroup(localBusinessDTOs,service);
								List<String> list = errorDetails
										.get("errorDetails");
								SubmissionUtil.sendMail(list, brandName,
										partner);
								
								numberOfRecords = numberOfRecords - list.size();

								insertIntoExportInfo(brandInfoDTO, brandName,
										numberOfRecords);
								submissionUtil.updateRenewal(localBusinessDTOs, partner,brandName);

							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								Date nextScheduledDate = getNextScheduledDate(
										infoGroupRecurrenecy,
										infoGroupScheduledDate);
								schedulerDTO
										.setInfogroupLastRunDate(infoGroupScheduledDate);
								schedulerDTO
										.setInfogroupScheduledDate(nextScheduledDate);

								try {
									service.updateCustomSubmissions(
											schedulerDTO, brandName);
									logger.info("Updating schedule is successful");
								} catch (Exception e) {
									logger.info(
											"There was a failure while updating schedule to run next time",
											e);
								}
							}
						}

					}
					if (partner.equalsIgnoreCase(LBLConstants.LBL_FACTUAL)) {
						isAPI = true;

						if (isToday(factualScheduledDate)) {
							logger.info("Sending info to " + partner
									+ ", and the total businesses are: "
									+ numberOfRecords);

							try {

								FactualClient factualClient = new FactualClient();
								Map<String, List<String>> errorDetails = factualClient
										.exportToFactual(localBusinessDTOs,
												service);

								List<String> list = errorDetails
										.get("errorDetails");
								SubmissionUtil.sendMail(list, brandName,
										partner);
								numberOfRecords = numberOfRecords - list.size();
								insertIntoExportInfo(brandInfoDTO, brandName,
										numberOfRecords);
								submissionUtil.updateRenewal(localBusinessDTOs, partner,brandName);

							} catch (Exception e) {
								logger.error("There was a issue while submitting data to Factual for Brand: "
										+ brandName);
							} finally {
								Date nextScheduledDate = getNextScheduledDate(
										factualRecurrenecy,
										factualScheduledDate);
								schedulerDTO
										.setFactualLastRunDate(factualScheduledDate);
								schedulerDTO
										.setFactualScheduledDate(nextScheduledDate);

								try {
									service.updateCustomSubmissions(
											schedulerDTO, brandName);
									logger.info("Updating schedule is successful");
								} catch (Exception e) {
									logger.info(
											"There was a failure while updating schedule to run next time",
											e);
								}
							}

						}
					}
					if (partner.equalsIgnoreCase(LBLConstants.LBL_NEUSTAR)) {
						isAPI = true;
						if (isToday(localezeScheduledDate)) {

							logger.info("Sending info to " + partner
									+ ", and the total businesses are: "
									+ numberOfRecords);

							try {

								Map<String, List<String>> errorDetails = NeustarLocalezeClient
										.neustarLocalezeAPI(service,
												localBusinessDTOs);
								List<String> list = errorDetails
										.get("errorDetails");
								SubmissionUtil.sendMail(list, brandName,
										partner);
								numberOfRecords = numberOfRecords - list.size();
								insertIntoExportInfo(brandInfoDTO, brandName,
										numberOfRecords);
								submissionUtil.updateRenewal(localBusinessDTOs, partner,brandName);

							} catch (Exception e) {
								logger.error("There was a issue while submitting data to Localeze for Brand: "
										+ brandName);

							} finally {
								Date nextScheduledDate = getNextScheduledDate(
										localezeRecurrenecy,
										localezeScheduledDate);
								schedulerDTO
										.setLocalezeLastRunDate(localezeScheduledDate);
								schedulerDTO
										.setLocalezeScheduledDate(nextScheduledDate);

								try {
									service.updateCustomSubmissions(
											schedulerDTO, brandName);
									logger.info("Updating schedule is successful");
								} catch (Exception e) {
									logger.info(
											"There was a failure while updating schedule to run next time",
											e);
								}
							}

						}
					}

				}
			}
		}
	}

	public Date getNextScheduledDate(String recurrncy, Date scheduledDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(scheduledDate);
		if (recurrncy.equalsIgnoreCase("weekly")) {
			cal.add(Calendar.DATE, 7);
		}
		if (recurrncy.equalsIgnoreCase("daily")) {
			cal.add(Calendar.DATE, 1);
		}
		if (recurrncy.equalsIgnoreCase("monthly")) {
			cal.add(Calendar.DATE, 30);
		}
		return cal.getTime();
	}

	void insertIntoExportInfo(BrandInfoDTO brandInfoDTO, String brandName,
			int numberOfRecords) {
		java.util.Date currentDate = DateUtil
				.getCurrentDate("MM/dd/yyyy HH:mm:ss");
		if (brandInfoDTO.getActiveDate() == null) {
			brandInfoDTO.setActiveDate(currentDate);
			service.updateBrandWithActiveDate(brandInfoDTO);
		}
		ChannelNameDTO channelNameDTO = service.getChannelById(brandInfoDTO
				.getChannelID());
		String channelName = "Convergent Mobile";
		if (channelNameDTO != null) {
			channelName = channelNameDTO.getChannelName();
		}

		ExportReportEntity exportReportEntity = new ExportReportEntity();
		exportReportEntity.setBrandName(brandName);
		exportReportEntity.setUserName("CustomSubmission");
		exportReportEntity.setPartner(brandInfoDTO.getSubmisions());
		exportReportEntity.setChannelName(channelName);
		exportReportEntity.setNumberOfRecords(Long.valueOf(numberOfRecords));
		exportReportEntity.setDate(currentDate);
		service.saveExpostInfo(exportReportEntity);
	}

	private boolean isToday(Date scheduledDate) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(new Date());
		cal2.setTime(scheduledDate);
		cal2.add(Calendar.HOUR,-10);
		boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2
						.get(Calendar.DAY_OF_YEAR);

		logger.info("isToday?: " + sameDay);
		return sameDay;

	}
}