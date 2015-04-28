<%@ taglib uri="http://www.springframework.org/tags/form"	prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0">
<title>Local Business Listings | Business Listings</title>
<link rel="stylesheet" href="css/global.css">
<link rel="stylesheet" href="fonts/fonts.css">
<link rel="stylesheet" href="css/navigation.css">
<link rel="stylesheet" href="css/common.css">
<link rel="stylesheet" href="css/grids.css">
<link rel="stylesheet" href="css/sidebar.css">
<link rel="stylesheet" href="css/bpopUp.css">
<script src="js/jquery-1.9.1.min.js"></script>
<script src="js/jquery.idTabs.min.js"></script>
<script src="js/jquery.slimscroll.min.js"></script>
<script src="js/jquery.screwdefaultbuttonsV2.js"></script>
<script src="js/jquery.bpopup.min.js"></script>
<script src="js/jquery.cycle.all.js"></script>
<script src="js/functions.js"></script>
<script src="js/jquery.session.js"></script>
<script type="text/javascript">
function saveChangepopup(){
	var editType = $.session.get("editType");
	var updateMsg = "Do you want to save the changes you have<br>entered for this location?";
	if (editType === "multiple") {
		updateMsg = "Do you want to save and apply all changes you have<br>entered to ALL locations selected?";
	}
	$("div#savePopupMessage").html(updateMsg);
  $('#editSaveChanges').bPopup();
  $("ul li").removeClass("selected");  
 }
 
function sendSubmitResult(){
  var form = document.getElementById('editBusinessForm');
     form.action = "updateClientBusiness.htm";    
     if ($.session.get("isSave") === "true") {
    	 $.session.get("isSave",false);
    	 form.action = "addClientLocation.htm";
     }
     form.submit();
 } 
 
 $(document).ready(function() {
	 var id = "location_information";
	 locatoinProfileUpdate(id);
	 
	$("ul.list-1 > li > a").click(function() {
		id = $(this).attr('id');
		locatoinProfileUpdate(id);
		enableReadOnly();
	});
	
	$("div > a#edit").click(function() {
		disableReadOnly(id+"_field");
	});
	
	 var multiUpdateValues = "";
		$("input.bussinessInfo").blur(function() {
			
			  if($(this).attr('readonly') !== "readonly"){
				 
			   var value = "";
			   var name = $(this).attr('name');
			   if($(this).attr("type") !== "checkbox"){
			    value = $(this).val();
			   }else {
			    value = $("input.bussinessInfo").is(":checked")?'Y':'N';
			   }
			   //alert("multiUpdateValues :: "+value);
			   if (value != "") {
			    if (multiUpdateValues.indexOf(name)>0) {
			     var multiUpdateValuesArr = multiUpdateValues.split(name);
			     multiUpdateValues = multiUpdateValuesArr[0]+multiUpdateValuesArr[1].substring(multiUpdateValuesArr[1].indexOf('|')+1);
			     //alert(multiUpdateValues);
			    }
			    multiUpdateValues = multiUpdateValues+name+"="+value+"|";
			    $("#multiUpdateString").val(multiUpdateValues);
			   }
			   //alert("multiUpdateValues :: "+$("#multiUpdateString").val());
			  }  
			 });
	
		
		$("#cancel").show();
		$("#save").show();
		$("#edit").click(function(){
			$("#cancel").show();
			$("#edit").hide();
			$("#save").show();
		});
		
		$("#cancel").click(function(){
			$("#cancelChanges").bPopup();
			$("ul li").removeClass("selected");
		});
	
	
});
 
 function locatoinProfileUpdate(id) {
	 $("ul.list-1 > li > a").removeClass('current');
		$("ul.list-1 > li > a#"+id).addClass('current');
		$("div#location_profile > table").hide();
		$("div#location_profile > table#"+id).show();
		
}
 
function disableReadOnly(id) {
	 $("table > tbody > tr > td > div > input#"+id).attr("readonly",false);
}

function enableReadOnly() {
	 $("table > tbody > tr > td > div > input").attr("readonly",false);
}
 
</script>
</head>

<div class="popup" id="cancelChanges" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		 <span class="buttonIndicator b-close"><span>X</span></span>
		<span>Cancel Changes</span>
	</div>
	<div class="pp-subheader">You have selected to Cancel the changes you have entered.<br>If you click OK, any changes made will not be saved.</div>
	<div class="pp-body">
		<div class="buttons">
			<a href="dashboardClient.htm" class="btn_dark_blue_2">Ok</a>
			<a href="#" class="btn_dark_blue_2 b-close">Continue Edit</a>
		</div>
	</div>
</div>

<div class="popup" id="editSaveChanges" style="display: none;">
 <div class="pp-header">
  <!-- <div class="close"></div> -->
  <span class="buttonIndicator b-close"><span>X</span></span>
  <span>Save Changes</span>
 </div>
 <div class="pp-subheader" id="savePopupMessage"></div>
 <div class="pp-body">
  <div class="buttons">
    <button onclick="sendSubmitResult();" class="btn_dark_blue_2">Ok</button>
    <button class="btn_dark_blue_2 b-close">Cancel</button>
  </div>
 </div>
</div>
<body id="office">
<spring:form action="updateClientBusiness.htm" id="editBusinessForm" commandName="businessInfo" >
<!-- page wrapper -->
<div class="wrapper"> 
	<!-- header -->
	<div class="header"> <a href="dashboardClient.htm" class="logo_lbl">Local Business Listings</a>
		<ul class="top_nav">
			<li class="home"><a href="dashboardClient.htm">Home</a></li>
			<li class="help"><a href="clientHelp.htm">Help</a></li>
			<li class="signout"><a href="logout.htm">Signout</a></li>
			<li class="welcome">
				<p>Hello, <br>
					${user}</p>
			</li>
		</ul>
	</div>
	<!-- // header --> 
	<!-- content wrapper -->
	<div class="content_wrapper no-sidebar"> 
		<!-- left side navigation -->
		<ul class="ul_left_nav">
			<li class="si_dashboard"><a href="dashboardClient.htm">Dashboard</a></li>
			<!-- <li class="si_business_listings selected"><a href="clientbusinesslisting.htm">Business Listings</a></li> -->
			<li class="si_error_listings"><a href="listingClient-error.htm">Listing Errors</a></li>
			<li class="si_reports"><a href="client-reports.htm">Reports</a></li>
			
			<li class="si_toolbox"><a href="clntToolbox.htm">Convergent Toolbox</a></li>
		</ul>
		<!-- // left side navigation --> 
		<!-- content area -->
		
		<div class="content" id="id_content">
			<div class="nav_pointer pos_01"></div>
			<!-- subheader -->
			<div class="subheader clearfix">
				<h1>BUSINESS LISTING</h1>
				<p>${channelName}</p>
			</div>
			<!-- // subheader -->
			<div class="inner_box"> 
				
				<div class="box box_red_title location_profile"> 
					<div class="box-sidebar">
						<div class="box_title"><h2>Location profile</h2></div>
						<!-- <a href="business-listings.htm" class="lp-back"><span>Back to Business Listings</span></a> -->
						<div class="lp-subtitle-text">
							<div class="text-1">${businessInfo.client}</div>
							<div class="text-2">Store #${businessInfo.store}</div>
						</div>
						<div class="lp-closed">
							<label><spring:checkbox class="bussinessInfo" path="locationClosed" value="Y"/> Location Closed</label>
							<spring:hidden path="id"/>
						</div>
						<ul class="list-1">
						<core:choose>
						<core:when test="${fn:contains(LocationInformation_Error, 'true')}"><li><a href="#" id="location_information">
						<span style="color: red;">Location Information</span></a></li></core:when>
						<core:otherwise>
						<li><a href="#" id="location_information">
						<span >Location Information</span></a></li>
						</core:otherwise>
						
						</core:choose>	
						
							<core:choose>
						<core:when test="${fn:contains(LocationContact_Error, 'true')}"><li><a href="#" id="location_content"><span  style="color: red;">Location Contact</span></a></li></core:when>
						<core:otherwise>
						<li><a href="#" id="location_content"><span>Location Contact</span></a></li>
						</core:otherwise>
						
						</core:choose>	
							<core:choose>
						<core:when test="${fn:contains(HoursofOperation_Error, 'true')}"><li><a href="#" id="hours_of_operation"><span  style="color: red;">Hours of Operation</span></a></li></core:when>
						<core:otherwise>
						<li><a href="#" id="hours_of_operation"><span>Hours of Operation</span></a></li>
						</core:otherwise>
						
						</core:choose>	
							<core:choose>
						<core:when test="${fn:contains(PaymentMethods_Error, 'true')}"><li><a href="#" id="payment_methods"><span style="color: red;">Payment Methods</span></a></li></core:when>
						<core:otherwise>
						<li><a href="#" id="payment_methods"><span>Payment Methods</span></a></li>
						</core:otherwise>
						
						</core:choose>	
							<core:choose>
						<core:when test="${fn:contains(SocialLinks_Error, 'true')}"><li><a href="#" id="social_links"><span style="color: red;">Social Links</span></a></li></core:when>
						<core:otherwise>
						<li><a href="#" id="social_links"><span>Social Links</span></a></li>
						</core:otherwise>
						
						</core:choose>	
						
								<core:choose>
						<core:when test="${fn:contains(EnhancedContent_Error, 'true')}"><li><a href="#" id="enhanced_content"><span style="color: red;">Enhanced Content</span></a></li></core:when>
						<core:otherwise>
						<li><a href="#" id="enhanced_content"><span>Enhanced Content</span></a></li>
						</core:otherwise>						
						</core:choose>	
						</ul>
						<span style="color: red;padding:13px 50px 10px;">*required fields</span>
					
						
						<div class="box-buttons">
							<!-- <a id="edit" href="#" class="btn_dark_blue_2">Edit</a> -->
							<a id="cancel" href="#" class="btn_dark_blue_2">Cancel</a>
							
							<%	Boolean isViewOnly = (Boolean) session.getAttribute("isViewOnly");
							if(!isViewOnly) { %>
														
      					   		 <a href="#" id="save" onclick="saveChangepopup();" class="btn_dark_blue_2">Save</a>
							<% }%>

						</div>
					</div>
			
					<div class="box-content">
						<div class="bc-inner" id="location_profile">
						
							<table  id="location_information" width="110%" class="grid grid_10">
								<colgroup>
									<col width="35%" />
									<col width="40%" />
									<col width="35%"/>
								</colgroup>
								<thead>
								
									<tr>
										<th class="th_01" colspan="2"><div>Location Information</div>
										<spring:hidden path="multiUpdateString"	id="multiUpdateString" />
										</th>
										<th class="th_02" colspan="2"> <div>ErrorMessage</div></th>
									</tr>
								</thead>
								<tbody>
									
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Client">
																</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" path="clientId" id="location_information_field" readonly="true" /></div></td>
										
										<td><span  style="color: red">${clientId_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Store">
																</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" path="store" id="location_information_field"  /></div></td>
										<td><span  style="color: red">${store_Error}</span></td>
									</tr>
									
										
										<spring:hidden class="bussinessInfo" id="location_information_field"   path="actionCode" />
										
									
										<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.countryCode">
															</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="countryCode" /></div></td>
										<td><span  style="color: red">${countryCode_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.CompanyName">
													</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="companyName" /></div></td>
										
										<td><span  style="color: red">${companyName_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.AlternativeName">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="alternativeName" /></div></td>
										<td><span  style="color: red">${alternativeName_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.AnchorOrHostBusiness">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="anchorOrHostBusiness" /></div></td>
										<td><span  style="color: red">${anchorOrHostBusiness_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Locationaddress">
											</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="locationAddress" /></div></td>
										<td><span  style="color: red">${locationAddress_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Suite">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="suite" /></div></td>
										<td><span  style="color: red">${suite_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.LocationCity">
										</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="locationCity" /></div></td>
										<td><span  style="color: red">${locationCity_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.LocationState">
											</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="locationState" /></div></td>
										<td><span  style="color: red">${locationState_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.LocationZipCode">
											</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="locationZipCode" /></div></td>
										<td><span  style="color: red">${locationZipCode_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.LocationPhone">
											</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="locationPhone" /></div></td>
										<td><span  style="color: red">${locationPhone_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Fax">
													</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="fax" /> </div></td>
										<td><span  style="color: red">${fax_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.TollFree">
													</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="tollFree" /></div></td>
										<td><span  style="color: red">${tollFree_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.TTY">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="tty" /> </div></td>
										<td><span  style="color: red">${tty_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.MobileNumber">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="mobileNumber" /></div></td>
										<td><span  style="color: red">${mobileNumber_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.AdditionalNumber">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="additionalNumber" /></div></td>
										<td><span  style="color: red">${additionalNumber_Error}</span></td>
									</tr>
										<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.LocationEmail">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="locationEmail" /></div></td>
										<td><span  style="color: red">${locationEmail_Error}</span></td>
									</tr>								
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ShortWebAddress">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="shortWebAddress" /></div></td>
										<td><span  style="color: red">${shortWebAddress_Error}</span></td>
									</tr>
									
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.WebAddress">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="webAddress" /></div></td>
										<td><span  style="color: red">${webAddress_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Category1">
													</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="category1" /></div></td>
										<td><span  style="color: red">${category1_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Category2">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="category2" /></div></td>
										<td><span  style="color: red">${category2_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Category3">
													</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="category3" /> </div></td>
										<td><span  style="color: red">${category3_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Category4">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="category4" /></div></td>
										<td><span  style="color: red">${category4_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Category5">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="category5" /></div></td>
										<td><span  style="color: red">${category5_Error}</span></td>
										
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.LogoLink">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="logoLink" /></div></td>
										<td><span  style="color: red">${logoLink_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ServiceArea">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="serviceArea" /></div></td>
										<td><span  style="color: red">${serviceArea_Error}</span></td>
									</tr>									
																		
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
									</tr>
					
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td ><div>&nbsp;</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td><div>&nbsp;</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td ><div>&nbsp;</div></td>
									</tr>
								</tbody>
							</table>
						
							<table id="location_content" width="110%" class="grid grid_10">
										<colgroup>
									<col width="35%" />
									<col width="40%" />
									<col width="35%"/>
								</colgroup>
								<thead>
									<tr>
										<th class="th_01" colspan="2"><div>Location Content</div></th>
										<th class="th_02" colspan="2"> <div>ErrorMessage</div></th>
									</tr>
								</thead>
								<tbody>			
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.PrimaryContactFirstName">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_content_field"   path="primaryContactFirstName" /></div></td>
										<td><span  style="color: red">${primaryContactFirstName_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message  code="businessApp.lable.PrimaryContactLastName">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_content_field"   path="primaryContactLastName" /></div></td>
										<td><span  style="color: red">${primaryContactLastName_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ContactTitle">
									</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_content_field"   path="contactTitle" /></div></td>
										<td><span  style="color: red">${contactTitle_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ContactEmail">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_content_field"   path="contactEmail" /></div></td>
										<td><span  style="color: red">${contactEmail_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.LocationEmployeeSize">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_content_field"    path="locationEmployeeSize" /></div></td>
										<td><span  style="color: red">${locationEmployeeSize_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.TitleManagerOrOwner">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_content_field"   path="title_ManagerOrOwner" /></div></td>
										<td><span  style="color: red">${title_ManagerOrOwner_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ProfessionalTitle">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_content_field"   path="professionalTitle" /></div></td>
										<td><span  style="color: red">${professionalTitle_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ProfessionalAssociations">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_content_field"   path="professionalAssociations" /></div></td>
										<td><span  style="color: red">${professionalAssociations_Error}</span></td>
									</tr>
									
									</tbody>
							</table>
						
							<table id="hours_of_operation" width="110%" class="grid grid_10">
								<colgroup>
									<col width="35%" />
									<col width="40%" />
									<col width="35%"/>
								</colgroup>
								<thead>
									<tr>
										<th class="th_01" colspan="2"><div>Hours Of Operation</div></th>
										<th class="th_02" colspan="2"> <div>ErrorMessage</div></th>
									</tr>
								</thead>
								<tbody>	
									
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.MondayOpen">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="mondayOpen" /> </div></td>
										<td><span  style="color: red">${mondayOpen_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.MondayClose">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="mondayClose" /></div></td>
										<td><span  style="color: red">${mondayClose_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.TuesdayOpen">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="tuesdayOpen" /></div></td>
										<td><span  style="color: red">${tuesdayOpen_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.TuesdayClose">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="tuesdayClose" /></div></td>
										<td><span  style="color: red">${tuesdayClose_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.WednesdayOpen">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="wednesdayOpen" /></div></td>
										<td><span  style="color: red">${wednesdayOpen_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.WednesdayClose">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="wednesdayClose" /></div></td>
										<td><span  style="color: red">${wednesdayClose_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ThursdayOpen">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="thursdayOpen" /></div></td>
										<td><span  style="color: red">${thursdayOpen_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ThursdayClose">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="thursdayClose" /></div></td>
										<td><span  style="color: red">${thursdayClose_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.FridayOpen">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="fridayOpen" /></div></td>
										<td><span  style="color: red">${fridayOpen_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.FridayClose">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="fridayClose" /></div></td>
										<td><span  style="color: red">${fridayClose_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.SaturdayOpen">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="saturdayOpen" /></div></td>
										<td><span  style="color: red">${saturdayOpen_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.SaturdayClose">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="saturdayClose" /></div></td>
										<td><span  style="color: red">${saturdayClose_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.SundayOpen">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="sundayOpen" /></div></td>
										<td><span  style="color: red">${sundayOpen_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.SundayClose">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="sundayClose" /></div></td>
										<td><span  style="color: red">${sundayClose_Error}</span></td>
									</tr>
									
									</tbody>
							</table>
						
							<table id="payment_methods" width="110%" class="grid grid_10">
								<colgroup>
									<col width="35%" />
									<col width="40%" />
									<col width="35%"/>
								</colgroup>
								<thead>
									<tr>
										<th class="th_01" colspan="2"><div>Payment Methods</div></th>
										<th class="th_02" colspan="2"> <div>ErrorMessage</div></th>
									</tr>
								</thead>
								<tbody>	
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.AMEX">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="aMEX" /></div></td>
										<td><span  style="color: red">${aMEX_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Discover">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"    path="discover" /></div></td>
										<td><span  style="color: red">${discover_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Visa">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="visa" /> </div></td>
										<td><span  style="color: red">${visa_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.MasterCard">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input  class="bussinessInfo" id="payment_methods_field"   path="masterCard" /> </div></td>
										<td><span  style="color: red">${masterCard_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.DinersClub">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="dinersClub" /></div></td>
										<td><span  style="color: red">${dinersClub_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.DebitCard">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="debitCard" /> </div></td>
										<td><span  style="color: red">${debitCard_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.StoreCard">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="storeCard" /></div></td>
										<td><span  style="color: red">${storeCard_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.OtherCard">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="otherCard" /></div></td>
										<td><span  style="color: red">${otherCard_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Cash">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="cash" /></div></td>
										<td><span  style="color: red">${cash_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Check">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="check" /> </div></td>
										<td><span  style="color: red">${check_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.TravelersCheck">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="travelersCheck" /></div></td>
										<td><span  style="color: red">${travelersCheck_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Financing">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="financing" /></div></td>
										<td><span  style="color: red">${financing_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.GoogleCheckout">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="googleCheckout" /></div></td>
										<td><span  style="color: red">${googleCheckout_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Invoice">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="invoice" /></div></td>
										<td><span  style="color: red">${invoice_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.PayPal">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="payPal" /></div></td>
										<td><span  style="color: red">${payPal_Error}</span></td>
									</tr>
																		
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
									</tr>
					
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td ><div>&nbsp;</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td><div>&nbsp;</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td ><div>&nbsp;</div></td>
									</tr>
									</tbody>
							</table>
						
							<table  id="social_links" width="110%" class="grid grid_10">
								<colgroup>
									<col width="35%" />
									<col width="40%" />
									<col width="35%"/>
								</colgroup>
								<thead>
									<tr>
										<th class="th_01" colspan="2"><div>Social Links</div></th>
										<th class="th_02" colspan="2"> <div>ErrorMessage</div></th>
									</tr>
								</thead>
								<tbody>	
									
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.CouponLink">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="social_links_field"   path="couponLink" /> </div></td>
										<td><span  style="color: red">${couponLink_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.TwitterLink">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="social_links_field"   path="twitterLink" /></div></td>
										<td><span  style="color: red">${twitterLink_Error}</span></td>
									</tr>
									<tr>
									<td class="td_01"><div><tag:message code="businessApp.lable.LinkedInLink">
									</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="social_links_field"   path="linkedInLink" /></div></td>
										<td><span  style="color: red">${linkedInLink_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.FacebookLink">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="social_links_field"   path="facebookLink" /></div></td>
										<td><span  style="color: red">${facebookLink_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.AlternateSocialLink">
									</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="social_links_field"   path="alternateSocialLink" /></div></td>
										<td><span  style="color: red">${alternateSocialLink_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.YouTubeOrVideoLink">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="social_links_field"   path="youTubeOrVideoLink" /></div></td>
										<td><span  style="color: red">${youTubeOrVideoLink_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.GooglePlusLink">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="social_links_field"   path="googlePlusLink" /> </div></td>
										<td><span  style="color: red">${googlePlusLink_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.MyspaceLink">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="social_links_field"   path="myspaceLink" /></div></td>
										<td><span  style="color: red">${myspaceLink_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.PinterestLink">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="social_links_field"   path="pinteristLink" /></div></td>
										<td><span  style="color: red">${pinteristLink_Error}</span></td>
									</tr>
									
									</tbody>
							</table>
						
							<table  id="enhanced_content" width="110%" class="grid grid_10">
										<colgroup>
									<col width="35%" />
									<col width="40%" />
									<col width="35%"/>
								</colgroup>
								<thead>
									<tr>
										<th class="th_01" colspan="2"><div>Enhanced Content</div></th>
										<th class="th_02" colspan="2"> <div>ErrorMessage</div></th>
									</tr>
								</thead>
								<tbody>	
									
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Products">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="enhanced_content_field"   path="products" /> </div></td>
										<td><span  style="color: red">${products_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Services">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="enhanced_content_field"   path="services" /></div></td>
										<td><span  style="color: red">${services_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ProductsOrServicescombined">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="enhanced_content_field"   path="productsOrServices_combined" /></div></td>
										<td><span  style="color: red">${productsOrServices_combined_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Brands">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="enhanced_content_field"   path="brands" /></div></td>
										<td><span  style="color: red">${brands_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Keywords">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="enhanced_content_field"   path="keywords" /></div></td>
										<td><span  style="color: red">${keywords_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Languages">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="enhanced_content_field"   path="languages" /></div></td>
										<td><span  style="color: red">${languages_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.YearEstablished">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="enhanced_content_field"   path="yearEstablished" /></div></td>
										<td><span  style="color: red">${yearEstablished_Error}</span></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Tagline">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="enhanced_content_field"   path="tagline" /></div></td>
										<td><span  style="color: red">${tagline_Error}</span></td>
									</tr>
									
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.BusinessDescription">
										</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="enhanced_content_field"   path="businessDescription" /></div></td>
										<td><div  style="color: red">${businessDescription_Error}</div></td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ADDRESSPRIVACYFLAG">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="enhanced_content_field"   path="ADDRESSPRIVACYFLAG" /></div></td>
										<td><span  style="color: red">${ADDRESSPRIVACYFLAG_Error}</span></td>
									</tr>
									
									
								</tbody>
							</table>
						</div>
					</div>
					
					<div class="clearfix"></div>
					
				</div>
					
				
			</div>
		</div>
		
		<!-- // content area --> 

		<div class="clearfix"></div>
	<!-- // content wrapper --> 
	<!-- footer -->
	<div class="footer">
		<div class="powered">Powered by</div>
	</div>
	<!-- // footer --> 
</div>
<!-- // page wrapper -->

</div>
</spring:form>
</body>
</html>