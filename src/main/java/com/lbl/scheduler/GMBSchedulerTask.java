package com.lbl.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.business.common.util.GMBClient;
import com.business.service.BusinessService;
import com.google.api.services.mybusiness.v3.model.Account;
import com.google.api.services.mybusiness.v3.model.Location;

@Component
public class GMBSchedulerTask {
	Logger logger = Logger.getLogger(SubmissionTask.class);
	@Autowired
	private BusinessService service;

	public void getGMBInsights() throws Exception {

		GMBClient gmbClient = new GMBClient(service);

		List<Account> listAccounts = gmbClient.listAccounts();
		logger.debug("Total Accounts Found is insights for  account: "
				+ listAccounts.size());

		Map<String, String> clientMap = new HashMap<String, String>();
		clientMap.put("112151379715199006045", "3471");
		clientMap.put("103586823811757517722", "3427");
		clientMap.put("101497916131701210690", "3225");
		clientMap.put("109074031771905912842", "4085");
		clientMap.put("105999931744411304059", "3520");
		clientMap.put("115422619543749222713", "3628");

		clientMap.put("111054657512683711456", "3991");
		clientMap.put("102400767872467993297", "3456");
		clientMap.put("115396074553568239475", "4094");
		clientMap.put("107893035143191834062", "3378");

		for (Account account : listAccounts) {
			String accountName = account.getAccountName();
			System.out.println("Account is:" + accountName);


			String name = account.getName();
			String[] accountIds = name.split("/");
			String googleAccountId = accountIds[1];

			if (!googleAccountId.equalsIgnoreCase("114515985899098925999") && clientMap.containsKey(googleAccountId)) {
				

				List<Location> listLocations = gmbClient.listLocations(account);

				Thread.sleep(2 * 1000);

				logger.debug("Total lcoations for account: " + accountName + " is "
						+ listLocations.size());
				
				String clientId = clientMap.get(googleAccountId);

				System.out.println("clientId: " + clientId);
				// update locations with google Mybusiness Data
				logger.debug("Updating LBL woth google account details");
				service.updateBusinessWithGoogleMB(clientId, listLocations);
				logger.debug("Fetching insights for  account: " + accountName);

				try {
					gmbClient.getReportInsights(account, googleAccountId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println("completed" + new Date());
		}
	}

}
