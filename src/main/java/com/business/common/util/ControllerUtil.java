package com.business.common.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.business.common.dto.BrandInfoDTO;
import com.business.common.dto.CheckReportDTO;
import com.business.common.dto.LblErrorDTO;
import com.business.common.dto.LocalBusinessDTO;
import com.business.common.dto.SearchDomainDTO;
import com.business.common.dto.UsersDTO;
import com.business.model.pojo.RoleEntity;
import com.business.model.pojo.StatesListEntity;
import com.business.service.BusinessService;
import com.business.web.bean.UsersBean;

/**
 * 
 * @author Vasanth
 *
 */

@Component
public class ControllerUtil {
	
	@Autowired
	private static BusinessService businessService;

	/**
	 * 
	 * CommonPart for manage account page
	 * 
	 * 
	 * @param model
	 * @param request
	 */

	public void setUserAndBussinessDataToModel(Model model, HttpServletRequest request,BusinessService service) {
		HttpSession session = request.getSession();
		Integer role = (Integer) session.getAttribute("roleId");
		List<UsersDTO> usersList = service.getAllUsersList(role);
		List<BrandInfoDTO> clientNames = null;
		if (role==LBLConstants.CONVERGENT_MOBILE_ADMIN) {
			clientNames = service.getClientNames();
		} else if (role==LBLConstants.CHANNEL_ADMIN || role==LBLConstants.PURIST) {
			String channelName = (String) session.getAttribute("channelName");
			clientNames = service.getChannelBasedClients(channelName);
		}
		ObjectMapper mapper = new ObjectMapper();
		/*List<LocalBusinessDTO> stateNames = service.getStates();*/
		List<StatesListEntity> stateNames = service.getStateList();
		List<LocalBusinessDTO> storeNames = service.getStore();
		String stateNamesvalue = "";
		try {
			stateNamesvalue = mapper.writeValueAsString(stateNames);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("stateNamesInfor", stateNamesvalue);
		model.addAttribute("stateNamesInfo", stateNames);
		String storeNamesvalue = "";
		try {
			storeNamesvalue = mapper.writeValueAsString(storeNames);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("storeNamesInfor", storeNamesvalue);
		model.addAttribute("storeNamesInfo", storeNames);
		String clientNamesvalues = "";
		try {
			clientNamesvalues = mapper.writeValueAsString(clientNames);
		} catch (Exception e) {
			e.printStackTrace();
		}
 
		model.addAttribute("clientNameInfor", clientNamesvalues);
		model.addAttribute("clientNameInfo", clientNames);
		model.addAttribute("usersListInfo", usersList);
		model.addAttribute("adminUser", new UsersBean());
	}
	/**
	 * getUploadDropDown 
	 * 
	 * @param model
	 * @param request
	 * @param service
	 */
	public void getUploadDropDown(Model model, HttpServletRequest request,BusinessService service){
		HttpSession session = request.getSession();
		Integer role = (Integer) session.getAttribute("roleId");
		List<BrandInfoDTO> clientNames = null;
		if (role==LBLConstants.CONVERGENT_MOBILE_ADMIN) {
			clientNames = service.getClientNames();
		} else if (role==LBLConstants.CHANNEL_ADMIN || role==LBLConstants.PURIST) {
			String channelName1 = (String) session.getAttribute("channelName");
			clientNames = service.getChannelBasedClients(channelName1);
		}else if (role == LBLConstants.CLIENT_ADMIN){
			
			clientNames = service.getClientbrnds();
		}
		model.addAttribute("clientNameInfo", clientNames);
		
	}
	
	/**
	 * saveOrUpdateChannel
	 * 
	 * @param bean
	 * @param startDateValue
	 * @param message
	 * @return
	 */
	public Integer saveOrUpdateChannel(UsersBean bean, Date startDateValue,
			String message,BusinessService service) {
		Integer channelID = service.getChannelIdByName(bean.getChannelName());
		if (channelID == 0) {
			channelID = service.saveChannel(bean.getChannelName(),
					startDateValue);
			message = "New Channel Name added successfully...";
		} else {
			service.updateChannel(bean.getChannelName(), startDateValue,
					channelID);
			message = "Old ChannelName updated Successfully...";
		}
		return channelID;
	}
	
	/**
	 * saveOrUpdateBrandAndChannel
	 * 
	 * @param bean
	 * @param message
	 * @param startDateValue
	 * @return
	 */
	public String saveOrUpdateBrandAndChannel(UsersBean bean, String message,
			Date startDateValue,BusinessService service) {
		Integer channelID = saveOrUpdateChannel(bean, startDateValue, message,service);
		if( bean.getSaveType()==null){
			boolean clientExistis = service.isClientExistis(bean.getBrandName());
			if (clientExistis) {
				message="BrandName is already Exist";
				return message;
			}
		}
		message = saveOrUpdateBrand(bean, message, startDateValue, channelID,service);
		return message;
	}
	
	/**
	 * saveOrUpdateBrand
	 * 
	 * @param bean
	 * @param message
	 * @param startDateValue
	 * @param channelID
	 * @return
	 */
	public String saveOrUpdateBrand(UsersBean bean, String message,
			Date startDateValue, Integer channelID,BusinessService service) {


		String partnerActive = "Y";
		String submissions = bean.getSubmisions();
		System.out.println("submissions"+submissions );
		Integer clientId = bean.getClientId();
		if(bean.getSaveType()==null){
			boolean clientExistis = service.isClientExistis(bean.getBrandName());
			if (clientExistis) {
				message="BrandName is already Exist";
				return message;
			}
		}

		List<String> brandAllSubmissions = service.getBrandAllSubmissions(bean
				.getBrandName());
		if (submissions != null) {
			BrandInfoDTO brandInfo = service.getBrandInfoByBrandName(bean.getBrandName());


			int brandId = brandInfo!=null?brandInfo.getBrandID():0;
			int maxBrandId = service.getMaxBrandId();
			for (String submission : submissions.split(",")) {
				if (submission.length() == 0) {
					continue;
				}
				BrandInfoDTO brandInfoDTO = service
						.getBrandByBrandNameAndPartner(bean.getBrandName(),
								submission);
				if (brandInfoDTO == null) {
					if (brandId == 0) {
						brandId = maxBrandId+1;
					}
					service.saveBrand(bean.getBrandName(), startDateValue,
							bean.getLocationsInvoiced(), submission, channelID,
							partnerActive,clientId,brandId,bean.getEmail());
					message = "New brand name is added successfully...";
					
				} else {
					if (!brandAllSubmissions.isEmpty()
							&& brandAllSubmissions.contains(submission)) {
						brandAllSubmissions.remove(submission);
					}
					message = updateBrand(brandInfoDTO.getBrandID(),brandInfoDTO.getInactive(),
							bean.getBrandName(), brandInfoDTO.getStartDate(),
							bean.getLocationsInvoiced(), channelID,
							partnerActive, submission,service,clientId,brandInfoDTO.getId(),bean.getEmail());
				}
			}
		}

		for (String submission : brandAllSubmissions) {
			BrandInfoDTO brandInfoDTO = service.getBrandByBrandNameAndPartner(
					bean.getBrandName(), submission);
			if (brandInfoDTO != null) {
				partnerActive = "N";
				message = updateBrand(brandInfoDTO.getBrandID(),brandInfoDTO.getInactive(),
						brandInfoDTO.getBrandName(),
						brandInfoDTO.getStartDate(),
						brandInfoDTO.getLocationsInvoiced(), channelID,
						partnerActive, submission,service,clientId,brandInfoDTO.getId(),brandInfoDTO.getEmail());
			}
		}
		return message;
	}

	private String updateBrand(Integer brandID, String inactive,
			String brandName, Date startDate, String locationsInvoiced,
			Integer channelID, String partnerActive, String submission,
			BusinessService service, Integer clientId, Integer id, String email) {
		String message;
		service.updateBrand(brandID, brandName, startDate,inactive,
				locationsInvoiced, submission, channelID, partnerActive,clientId,id,email);
		message = "Old brand name updated successfully...";
		return message;
	}


	/**
	 * updateBrand
	 * 
	 * @param channelID
	 * @param partnerActive
	 * @param submission
	 * @param integer 
	 * @param brandInfoDTO
	 * @return
	 */
	public String updateBrand(Integer brandId, String brandName,
			Date brandStartDate, String locationsInvoiced, Integer channelID,
			String partnerActive, String submission,BusinessService service,Integer clientId, Integer id) {
		String message;
		service.updateBrand(brandId, brandName, brandStartDate,
				locationsInvoiced, submission, channelID, partnerActive,clientId,id);
		message = "Old brand name updated successfully...";
		return message;
	}
	
	/**
	 * this will get the channels based on role
	 */
	public void getChannels(Model model, HttpSession session,BusinessService service) {
		Integer role = (Integer) session.getAttribute("roleId");
		if (role==LBLConstants.CONVERGENT_MOBILE_ADMIN) {
			List<String> channelNames = service.getChannels();
			model.addAttribute("channels", channelNames);
		} else {
			String channelName = (String) session.getAttribute("channelName");
			List<String> channelNames = service.getChannels(channelName);
			model.addAttribute("channels", channelNames);
		}
	}
	
	/**
	 * this wil return roles to viewlayer
	 * 
	 * @return
	 */
	
	public void getRoles(Model model,BusinessService service) {	
		
		List<RoleEntity> roles=service.getRoles();
		model.addAttribute("roles",roles);
	
		
	}

	/**
	 * getNumOfPages
	 * @param size
	 * @return
	 */
	public int getNumOfPages(int size) {		
		return ((size/LBLConstants.MAX_RECORDS_PER_PAGE)+(size%LBLConstants.MAX_RECORDS_PER_PAGE == 0?0:1));
	}

	/**
	 * getBusinessInfoSubList
	 * @param listOfBusinessInfo
	 * @param pageNum
	 * @return
	 */
	public List<LocalBusinessDTO> getBusinessInfoSubList(
			List<LocalBusinessDTO> listOfBusinessInfo, int pageNum) {
		int fromIndex = (pageNum-1)*LBLConstants.MAX_RECORDS_PER_PAGE;	
		int endIndex = fromIndex+LBLConstants.MAX_RECORDS_PER_PAGE;
		if (listOfBusinessInfo.size()/LBLConstants.MAX_RECORDS_PER_PAGE == pageNum-1) {
			endIndex = listOfBusinessInfo.size();
		}		
		return listOfBusinessInfo.subList(fromIndex, endIndex);
	}
	
	
	
	/**
	 * getErrorInfoSubList
	 * @param listOfBusinessInfo
	 * @param pageNum
	 * @return
	 */
	public List<LblErrorDTO> getErrorInfoSubList(
			List<LblErrorDTO> listOfBusinessInfo, int pageNum) {
		int fromIndex = (pageNum-1)*LBLConstants.MAX_RECORDS_PER_PAGE;	
		int endIndex = fromIndex+LBLConstants.MAX_RECORDS_PER_PAGE;
		if (listOfBusinessInfo.size()/LBLConstants.MAX_RECORDS_PER_PAGE == pageNum-1) {
			endIndex = listOfBusinessInfo.size();
		}		
		return listOfBusinessInfo.subList(fromIndex, endIndex);
	}

	
	/**
	 * listingsAddAttributes
	 * @param pageNum
	 * @param model
	 * @param request
	 * @param listOfBusinessInfo
	 */
	public void listingsAddAttributes(int pageNum, Model model,
			HttpServletRequest request,
			List<LocalBusinessDTO> listOfBusinessInfo) {
		int numOfPages = getNumOfPages(listOfBusinessInfo.size());
		model.addAttribute("numOfPages", numOfPages);
		model.addAttribute("allBusinessInfo", getBusinessInfoSubList(listOfBusinessInfo,pageNum));
		model.addAttribute("businessSearch", new LocalBusinessDTO());
		model.addAttribute("checkreportSearch", new CheckReportDTO());
		request.setAttribute("currentPage", pageNum);
		request.setAttribute("numOfPages", numOfPages);
	}
	
	
	/**
	 * listingsAddAttributes
	 * @param pageNum
	 * @param model
	 * @param request
	 * @param listOfBusinessInfo
	 */
	public void listingAddAttributes(int pageNum, Model model,
			HttpServletRequest request,
			List<LblErrorDTO> listOfBusinessInfo) {
		int numOfPages = getNumOfPages(listOfBusinessInfo.size());
		model.addAttribute("numOfPages", numOfPages);
		model.addAttribute("allBusinessInfo", getErrorInfoSubList(listOfBusinessInfo,pageNum));
		model.addAttribute("businessSearch", new LocalBusinessDTO());
		request.setAttribute("currentPage", pageNum);
		request.setAttribute("numOfPages", numOfPages);
	}
	public void sendTemplate(String userName, String absolutePath,
			HSSFWorkbook workbook,HttpServletRequest request,String reportName,String email) throws FileNotFoundException, IOException {
		String toMail=LBLConstants.SUPPORT_MAIL;
		MailTemplateUtil.sendMailWithReceipt("vasanth.odesk@gmail.com", absolutePath, workbook,reportName,toMail);
		/*Test test=new Test();
		test.emailsend(absolutePath);*/
		
	}

	
	
	
	

}
