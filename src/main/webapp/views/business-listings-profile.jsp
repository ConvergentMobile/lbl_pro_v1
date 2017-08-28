<%@page import="com.business.common.util.LBLConstants"%>
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
<link rel="stylesheet" href="js/jquery.mCustomScrollbar.css">
<script src="js/jquery.mCustomScrollbar.js"></script>
<link rel="stylesheet" href="js/jquery.mCustomScrollbar.css">
<script src="js/jquery.mCustomScrollbar.js"></script>
<script src="js/functions.js"></script>
<style>
	.error { 
		color: red; font-weight: bold; 
	}
	.box.location_profile .box-buttons {
    text-align: left;
}

 /* #location_information td:last-child span {
    margin: 20px 0 0 0!important;
    position: absolute;
    left: 30px;
}  */ 

</style>
<script type="text/javascript">

/* 
 function disableF5(e) { if ((e.which || e.keyCode) == 116 || (e.which || e.keyCode) == 82) e.preventDefault(); };

$(document).ready(function(){
	
     $(document).on("keydown", disableF5);
     
     /* window.onbeforeunload = function(e) {
    	 var listid=$("#listId").val();

    	 document.location.href = "editBusinessInfor.htm?listid="+listid;
    	}; 
/* });
 */
     

</script>
<script type = "text/javascript" >
$(document).ready(function(){
var listid=$("#listId").val();
history.pushState(null, null, 'editBusinessInfor.htm?listid='+listid);
window.addEventListener('popstate', function(event) {
	
history.pushState(null, null, 'editBusinessInfor.htm?listid='+listid);
});
});
</script> 
<script type="text/javascript">

function saveChangepopup(){
	
	var editType = $.session.get("editType");
	var updateMsg = "Do you want to save the changes you have<br>entered for this location?";
	if ($.session.get("isSave") === "true") {
		updateMsg = "Do you want to add this new location?";
	}else{		
		if (editType === "multiple") {
			updateMsg = "Do you want to save and apply all changes you have<br>entered to ALL locations selected?";
		}
	}
	
	$("div#savePopupMessage").html(updateMsg);
  $('#editSaveChanges').bPopup(); 
	
  $("ul li").removeClass("selected");
 }
 
function sendSubmitResult(){
	
  var form = document.getElementById('editBusinessForm');
     form.action = "updateBusiness.htm";
     if ($.session.get("isSave") === "true") {
    	 $.session.get("isSave",false);
    	 form.action = "addLocation.htm";
     }
     
    /*  if($.session.get("editErrorInfo") === "editErrorInfo"){   
    	 $.session.set("editErrorInfo","");
    	 form.action = "updateErrorBusiness.htm";
     } */
     form.submit();
 } 

 $(document).ready(function() {

	 var id = "location_information";
	 locatoinProfileUpdate(id);	
	 diableReadOnlyForAddLocation(id+"_field");	 
	$("ul.list-1 > li > a").click(function() {
		id = $(this).attr('id');
		locatoinProfileUpdate(id);
		enableReadOnly();
		diableReadOnlyForAddLocation(id+"_field");
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
		  // alert("multiUpdateValues :: "+value);
		   if (value != "") {
		    if (multiUpdateValues.indexOf(name)>0) {
		     var multiUpdateValuesArr = multiUpdateValues.split(name);
		     multiUpdateValues = multiUpdateValuesArr[0]+multiUpdateValuesArr[1].substring(multiUpdateValuesArr[1].indexOf('|')+1);
		    // alert(multiUpdateValues);
		    }
		    multiUpdateValues = multiUpdateValues+name+"="+value+"|";
		    $("#multiUpdateString").val(multiUpdateValues);
		   }
		   //alert("multiUpdateValues :: "+$("#multiUpdateString").val());
		  }  
		 });
	
	$("#cancel").show();
	$("#save").show();	
	if ($.session.get("isSave") === "true"){
		$("#cancel").show();
		$("#edit").hide();
		$("#save").show();
	}		
	$("#edit").click(function(){
		$("#cancel").show();
		$("#edit").hide();
		$("#save").show();
	});

	$("#cancel").click(function(){
		$("#cancelChanges").bPopup();
		$("ul li").removeClass("selected");
	});
	
	
	/*  var message =$("#validationSearch").val();
	    if(message != null){
	     $('#validationSearchpop').bPopup();
	     $("ul li").removeClass("selected");
	    } */




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

function disableReadOnly() {
	 $("table > tbody > tr > td > div > input").attr("readonly",false);
}

function diableReadOnlyForAddLocation(id){
	if ($.session.get("isSave") === "true") {
		 //alert($.session.get("isSave"));
		 disableReadOnly(id);
	}
}
 
</script>
</head>
<%-- <core:if test="${validation eq 'yes'}">
<input type="hidden" value="${validation}" id="validationSearch">
<div class="popup" id="validationSearchpop" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		 <span class="buttonIndicator b-close"><span>X</span></span>
		<span>Validation Errors</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
	
		<div class="buttons">
			<a href="#" class="btn_dark_blue_2 b-close">Ok</a>			
		</div>
	</div>
</div>
</core:if> --%>
<div class="popup" id="cancelChanges" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		 <span class="buttonIndicator b-close"><span>X</span></span>
		<span>Cancel Changes</span>
	</div>
	<div class="pp-subheader">You have selected to Cancel the changes you have entered.<br>If you click OK, any changes made will not be saved.</div>
	<div class="pp-body">
		<div class="buttons">
			<a href="businesstorelistings.htm" class="btn_dark_blue_2">Ok</a>
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
<body id="office"   >
<spring:form action="updateBusiness.htm" id="editBusinessForm" commandName="businessInfo" >
<!-- page wrapper -->
<div class="wrapper"> 
	<!-- header -->
	<div class="header"> <a href="dash-board.htm" class="logo_lbl">Local Business Listings</a>
		<ul class="top_nav">
			<li class="home"><a href="dash-board.htm">Home</a></li>
			<li class="help"><a href="help.htm">Help</a></li>
			<li class="signout"><a href="logout.htm">Signout</a></li>
			<li class="welcome">
				<p>Hello, <br>
					${userName}</p>
			</li>
		</ul>
	</div>
	<!-- // header --> 
	<!-- content wrapper -->
	<div class="content_wrapper no-sidebar"> 
		<!-- left side navigation -->
		<ul class="ul_left_nav">
			<li class="si_dashboard"><a href="dash-board.htm">Dashboard</a></li>
			<li class="si_business_listings selected"><a href="business-listings.htm">Business Listings</a></li>
			<li class="si_error_listings"><a href="listing-error.htm">Listing Errors</a></li>
			<li class="si_upload_export"><a href="upload-export.htm">Upload/Export</a></li>
			<li class="si_reports"><a href="reports.htm">Reports</a></li>
				<%
						Integer Role=(Integer)session.getAttribute("roleId");				
							if(Role==LBLConstants.CONVERGENT_MOBILE_ADMIN){						
						%>
			<li class="si_admin"><a href="admin-listings.htm">CM admin</a></li>
			<li class="si_mobile_profile "><a href="manage-account.htm">Manage Account</a></li>
				
			<%} %>
		<!-- 	<li class="si_schduler"><a href="scheduler.htm">Schedule</a></li> -->
			
			<!--<li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent Toolbox</a></li>-->
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
					<%-- <core:out value="${listId}"></core:out> --%>
					<input type="hidden" value="${listId}" name="listId" id="listId">
					<input type="hidden" value="${brandName }" name="brandval">
					<input type="hidden" value="${storeVal }" name="storeVal">
						<div class="box_title"><h2 style="text-align: left;">Location profile</h2></div>
						<!-- <a href="business-listings.htm" class="lp-back"><span>Back to Business Listings</span></a> -->
						<div class="lp-subtitle-text">
							<div class="text-1">${businessInfo.client}</div>
							<div class="text-2">Store #${businessInfo.store}</div>
						</div>
						<div class="lp-closed">
							<label><spring:checkbox class="bussinessInfo"  path="locationClosed" value="Y"/> Location Closed</label>
							<spring:hidden path="id"/>
						</div>
						<ul class="list-1">
							<%-- <core:if test="${not empty errorListInfo }"><li><a href="#" id="error_information"><span style="color: red;">Error Information</span></a></li></core:if> --%>
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
							 <a href="#" onclick="saveChangepopup();" id="save" class="btn_dark_blue_2">Save</a>
			    				 
						<% }%>

						</div>
					</div>
			
					<div class="box-content" style="margin: 0px -80px 0px">
						<div class="bc-inner" id="location_profile">

							<table  id="location_information" width="100%" class=" grid_10 localinfo">
								<colgroup>
									<col width="27%" />
									<col width="25%" />
									<col width="48%"/>
								</colgroup>
								<thead>
								
									<tr>
										<th class="th_01"><div>Location Information</div>
										</th>
										<th class="th_02"><spring:hidden path="multiUpdateString"	id="multiUpdateString" /></th>

										<th class="th_03"> <div>Error Message</div></th>
									</tr>
								</thead>
								<colgroup>
									<col width="27%" />
									<col width="25%" />
									<col width="48%"/>
								</colgroup>
								<tbody>
							<%-- 	<spring:hidden  path="id" value="${listId}" id="listId"/> --%>
									<input type="hidden" value="${listId}" name="listId" id="listId">
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Client">
																</tag:message><span style="color: red">*</span>
																<%-- <span  style="color: red;    margin: 12px -10px 0px; font-size: 12px;display: block;">${clientId_Error}</span> --%>
																</div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" path="clientId" id="location_information_field" /></div></td>
										
										  <td class="errors">${clientId_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Store">
																</tag:message><span style="color: red">*</span>
																</div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" path="store" id="location_information_field" /></div></td>
										 <td class="errors">${store_Error}</td>
									</tr>
									
										
										<spring:hidden class="bussinessInfo" id="location_information_field"   path="actionCode" />
										
									
										<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.countryCode">
															</tag:message><span style="color: red">*</span>
															
															</div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="countryCode" /></div></td>
										 <td class="errors">${countryCode_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.CompanyName">
													</tag:message><span style="color: red">*</span>
													</div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="companyName" /></div></td>
										
										 <td class="errors">${companyName_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.AlternativeName">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="alternativeName" /></div></td>
										 <td class="errors">${alternativeName_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.AnchorOrHostBusiness">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="anchorOrHostBusiness" /></div></td>
										 <td class="errors">${anchorOrHostBusiness_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Locationaddress">
											</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="locationAddress" /></div></td>
										 <td class="errors">${locationAddress_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Suite">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="suite" /></div></td>
										 <td class="errors">${suite_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.LocationCity">
										</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="locationCity"/></div></td>
										 <td class="errors">${locationCity_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.LocationState">
											</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="locationState"/></div></td>
										 <td class="errors">${locationState_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.LocationZipCode">
											</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="locationZipCode"/></div></td>
										 <td class="errors">${locationZipCode_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.LocationPhone">
											</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="locationPhone"/></div></td>
										 <td class="errors">${locationPhone_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Fax">
													</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="fax"/> </div></td>
										<td class="errors">${fax_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.TollFree">
													</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="tollFree"/></div></td>
										 <td class="errors">${tollFree_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.TTY">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="tty"/> </div></td>
										 <td class="errors">${tty_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.MobileNumber">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="mobileNumber" /></div></td>
										 <td class="errors">${mobileNumber_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.AdditionalNumber">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="additionalNumber"/></div></td>
										 <td class="errors">${additionalNumber_Error}</td>
									</tr>
										<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.LocationEmail">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="locationEmail"/></div></td>
										 <td class="errors">${locationEmail_Error}</td>
									</tr>								
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ShortWebAddress">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="shortWebAddress"/></div></td>
										 <td class="errors">${shortWebAddress_Error}</td>
									
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.WebAddress">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="webAddress"/></div></td>
										 <td class="errors">${webAddress_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Category1">
													</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="category1"/></div></td>
										 <td class="errors">${category1_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Category2">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="category2"/></div></td>
										 <td class="errors">${category2_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Category3">
													</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="category3"/> </div></td>
										 <td class="errors">${category3_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Category4">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="category4"/></div></td>
										 <td class="errors">${category4_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Category5">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_information_field"   path="category5"/></div></td>
										 <td class="errors">${category5_Error}</td>
										
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.LogoLink">
												</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="location_information_field"   path="logoLink"/></div></td>
										 <td class="errors">${logoLink_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ServiceArea">
												</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="location_information_field"   path="serviceArea" style="height: 105px;"/></div></td>
										 <td class="errors">${serviceArea_Error}</td>
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
										<td ><div>&nbsp;</div></td>
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
					
							<table id="location_content" width="100%" class=" grid_10 localinfo">
								<colgroup>
									<col width="27%" />
									<col width="25%" />
									<col width="48%"/>
								</colgroup>
								<thead>
									<tr>
										<th class="th_01"><div>Location Content</div></th>
										<th class="th_02"><div></div></th>
										<th class="th_03"><div>Error Message</div></th>
									</tr>
								</thead>
								<colgroup>
									<col width="27%" />
									<col width="25%" />
									<col width="48%"/>
								</colgroup>
								<tbody>			
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.PrimaryContactFirstName">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_content_field"   path="primaryContactFirstName" style="margin: 0px -45px 0px;"/></div></td>
										 <td class="errors">${primaryContactFirstName_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message  code="businessApp.lable.PrimaryContactLastName">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_content_field"   path="primaryContactLastName" style="margin: 0px -45px 0px;"/></div></td>
										 <td class="errors">${primaryContactLastName_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ContactTitle">
									</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_content_field"   path="contactTitle" style="margin: 0px -45px 0px;"/></div></td>
										 <td class="errors">${contactTitle_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ContactEmail">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_content_field"   path="contactEmail" style="margin: 0px -45px 0px;"/></div></td>
										 <td class="errors">${contactEmail_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.LocationEmployeeSize">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_content_field"    path="locationEmployeeSize" style="margin: 0px -45px 0px;"/></div></td>
										 <td class="errors">${locationEmployeeSize_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.TitleManagerOrOwner">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_content_field"   path="title_ManagerOrOwner" style="margin: 0px -45px 0px;"/></div></td>
										 <td class="errors">${title_ManagerOrOwner_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ProfessionalTitle">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_content_field"   path="professionalTitle" style="margin: 0px -45px 0px;"/></div></td>
										 <td class="errors">${professionalTitle_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ProfessionalAssociations">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="location_content_field"   path="professionalAssociations" style="margin: 0px -45px 0px;"/></div></td>
										 <td class="errors">${professionalAssociations_Error}</td>
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
										<td ><div>&nbsp;</div></td>
									</tr>
									</tbody>
							</table>
						
							<table id="hours_of_operation" width="100%" class=" grid_10 localinfo">
								<colgroup>
									<col width="27%" />
									<col width="25%" />
									<col width="48%"/>
								</colgroup>
								<thead>
									<tr>
										<th class="th_01"><div>Hours Of Operation</div></th>
										<th class="th_02"><div></div></th>
										<th class="th_03"><div>Error Message</div></th>
									</tr>
								</thead>
								<colgroup>
									<col width="27%" />
									<col width="25%" />
									<col width="48%"/>
								</colgroup>
								<tbody>	
									
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.MondayOpen">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="mondayOpen" style="margin: 0px -100px 0px;" /> </div></td>
										 <td class="errors">${mondayOpen_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.MondayClose">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="mondayClose" style="margin: 0px -100px 0px;"/></div></td>
										 <td class="errors">${mondayClose_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.TuesdayOpen">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="tuesdayOpen" style="margin: 0px -100px 0px;"/></div></td>
										 <td class="errors">${tuesdayOpen_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.TuesdayClose">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="tuesdayClose" style="margin: 0px -100px 0px;"/></div></td>
										 <td class="errors">${tuesdayClose_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.WednesdayOpen">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="wednesdayOpen" style="margin: 0px -100px 0px;"/></div></td>
										 <td class="errors">${wednesdayOpen_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.WednesdayClose">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="wednesdayClose" style="margin: 0px -100px 0px;"/></div></td>
										 <td class="errors">${wednesdayClose_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ThursdayOpen">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="thursdayOpen" style="margin: 0px -100px 0px;"/></div></td>
										 <td class="errors">${thursdayOpen_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ThursdayClose">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="thursdayClose" style="margin: 0px -100px 0px;"/></div></td>
										<td class="errors">${thursdayClose_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.FridayOpen">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="fridayOpen" style="margin: 0px -100px 0px;"/></div></td>
										 <td class="errors">${fridayOpen_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.FridayClose">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="fridayClose" style="margin: 0px -100px 0px;"/></div></td>
										 <td class="errors">${fridayClose_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.SaturdayOpen">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="saturdayOpen" style="margin: 0px -100px 0px;"/></div></td>
										 <td class="errors">${saturdayOpen_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.SaturdayClose">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="saturdayClose" style="margin: 0px -100px 0px;"/></div></td>
										 <td class="errors">${saturdayClose_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.SundayOpen">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="sundayOpen" style="margin: 0px -100px 0px;" /></div></td>
										 <td class="errors">${sundayOpen_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.SundayClose">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="hours_of_operation_field"   path="sundayClose" style="margin: 0px -100px 0px;"/></div></td>
										 <td class="errors">${sundayClose_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
									</tr>
					
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
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
										<td ><div>&nbsp;</div></td>
									</tr>
									</tbody>
							</table>
						
							<table id="payment_methods" width="100%" class=" grid_10 localinfo">
								<colgroup>
									<col width="27%" />
									<col width="25%" />
									<col width="48%"/>
								</colgroup>
								<thead>
									<tr>
										<th class="th_01"><div>Payment Methods</div></th>
										<th class="th_02"><div></div></th>
										<th class="th_03"><div>Error Message</div></th>
									</tr>
								</thead>
								<colgroup>
									<col width="27%" />
									<col width="25%" />
									<col width="48%"/>
								</colgroup>
								<tbody>	
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.AMEX">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="aMEX" style="margin: 0px -100px 0px;"/></div></td>
										<td class="errors">${aMEX_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Discover">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"    path="discover" style="margin: 0px -100px 0px;"/></div></td>
										<td class="errors">${discover_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Visa">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="visa" style="margin: 0px -100px 0px;"/> </div></td>
										<td class="errors">${visa_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.MasterCard">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input  class="bussinessInfo" id="payment_methods_field"   path="masterCard" style="margin: 0px -100px 0px;"/> </div></td>
										<td class="errors">${masterCard_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.DinersClub">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="dinersClub" style="margin: 0px -100px 0px;"/></div></td>
										<td class="errors">${dinersClub_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.DebitCard">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="debitCard" style="margin: 0px -100px 0px;"/> </div></td>
										<td class="errors">${debitCard_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.StoreCard">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="storeCard" style="margin: 0px -100px 0px;"/></div></td>
										<td class="errors">${storeCard_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.OtherCard">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="otherCard" style="margin: 0px -100px 0px;"/></div></td>
										<td class="errors">${otherCard_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Cash">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="cash" style="margin: 0px -100px 0px;"/></div></td>
										<td class="errors">${cash_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Check">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="check" style="margin: 0px -100px 0px;"/> </div></td>
										<td class="errors">${check_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.TravelersCheck">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="travelersCheck" style="margin: 0px -100px 0px;"/></div></td>
										<td class="errors">${travelersCheck_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Financing">
											</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="financing" style="margin: 0px -100px 0px;"/></div></td>
										<td class="errors">${financing_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.GoogleCheckout">
												</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="googleCheckout" style="margin: 0px -100px 0px;"/></div></td>
										<td class="errors">${googleCheckout_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Invoice">
										</tag:message>
										</div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="invoice" style="margin: 0px -100px 0px;"/></div></td>
										<td class="errors">${invoice_Error}</td>
									</tr>
									
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.PayPal">
										</tag:message></div></td>
										<td class="td_02"><div><spring:input class="bussinessInfo" id="payment_methods_field"   path="payPal" style="margin: 0px -100px 0px;"/></div></td>
										<td class="errors">${payPal_Error}</td>
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
										<td ><div>&nbsp;</div></td>
									</tr>
									</tbody>
							</table>
						
							<table  id="social_links" width="100%" class=" grid_10 localinfo">
								<colgroup>
									<col width="27%" />
									<col width="25%" />
									<col width="48%"/>
								</colgroup>
								<thead>
									<tr>
										<th class="th_01"><div>Social Links</div></th>
										<th class="th_02"><div></div></th>
										<th class="th_03"><div>Error Message</div></th>
									</tr>
								</thead>
								<colgroup>
									<col width="27%" />
									<col width="25%" />
									<col width="48%"/>
								</colgroup>
								<tbody>	
									
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.CouponLink">
										</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="social_links_field"   path="couponLink" /> </div></td>
										<td class="errors">${couponLink_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.TwitterLink">
											</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="social_links_field"   path="twitterLink" /></div></td>
										<td class="errors">${twitterLink_Error}</td>
									</tr>
									<tr>
									<td class="td_01"><div><tag:message code="businessApp.lable.LinkedInLink">
									</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="social_links_field"   path="linkedInLink" /></div></td>
										<td class="errors">${linkedInLink_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.FacebookLink">
										</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="social_links_field"   path="facebookLink" /></div></td>
										<td class="errors">${facebookLink_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.foursquareLink">
									</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="social_links_field"   path="foursquareLink" /></div></td>
										<td class="errors">${foursquareLink_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.YouTubeOrVideoLink">
										</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="social_links_field"   path="youTubeOrVideoLink" /></div></td>
										<td class="errors">${youTubeOrVideoLink_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.GooglePlusLink">
										</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="social_links_field"   path="googlePlusLink" /> </div></td>
										<td class="errors">${googlePlusLink_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.MyspaceLink">
										</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="social_links_field"   path="myspaceLink" /></div></td>
										<td class="errors">${myspaceLink_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.PinterestLink">
										</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="social_links_field"   path="pinteristLink" /></div></td>
										<td class="errors">${pinteristLink_Error}</td>
									</tr>
									
										<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.helpLink">
										</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="social_links_field"   path="yelpLink" /></div></td>
										<td class="errors">${helpLink_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.instagramLink">
										</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="social_links_field"   path="instagramLink" /></div></td>
										<td class="errors">${instagramLink_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.menuLink">
										</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="social_links_field"   path="menuLink" /></div></td>
										<td class="errors">${menuLink_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
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
										<td ><div>&nbsp;</div></td>
									</tr>
									</tbody>
							</table>
						
							<table  id="enhanced_content" width="100%" class=" grid_10 localinfo">
								<colgroup>
									<col width="27%" />
									<col width="25%" />
									<col width="48%"/>
								</colgroup>
								<thead>
									<tr>
										<th class="th_01"><div>Enhanced Content</div></th>
										<th class="th_02"><div></div></th>
										<th class="th_03" style="margin: :0px -20px 0px"> <div>Error Message</div></th>
									</tr>
								</thead>
								<colgroup>
									<col width="27%" />
									<col width="25%" />
									<col width="48%"/>
								</colgroup>
								<tbody>	
									
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Products">
										</tag:message></div></td>
										<td class="td_02" ><div><spring:textarea class="bussinessInfo" id="enhanced_content_field" rows="10" cols="30"   path="products" style="padding-top: 0px; width:100%; height :105px;    " /> </div></td>
										<td class="errors">${products_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Services">
										</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="enhanced_content_field" rows="10" cols="30"   path="services" style="padding-top: 0px; width:100%; height :105px;    " /></div></td>
										<td class="errors">${services_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ProductsOrServicescombined">
										</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="enhanced_content_field" rows="10" cols="30"   path="productsOrServices_combined" style="padding-top: 0px; width:100%; height :105px;" /></div></td>
										<td class="errors">${productsOrServices_combined_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Brands">
										</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="enhanced_content_field"  rows="10" cols="30"  path="brands" style="padding-top: 0px; width:100%; height :105px;    "/></div></td>
										<td class="errors">${brands_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Keywords">
											</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="enhanced_content_field" rows="10" cols="30"    path="keywords" style="padding-top: 0px; width:100%; height :105px;    " /></div></td>
										<td class="errors">${keywords_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Languages">
										</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="enhanced_content_field" rows="10" cols="30"   path="languages" style="padding-top: 0px; width:100%; height :60px;    " /></div></td>
										<td class="errors">${languages_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.YearEstablished">
										</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="enhanced_content_field"  rows="10" cols="30"  path="yearEstablished" style="padding-top: 0px; width:100%; height :60px;    " /></div></td>
										<td class="errors">${yearEstablished_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.Tagline">
												</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="enhanced_content_field" rows="10" cols="30"   path="tagline" style="padding-top: 0px; width:100%; height :60px;    " /></div></td>
										<td class="errors">${tagline_Error}</td>
									</tr>
									
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.BusinessDescription">
										</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="enhanced_content_field" rows="10" cols="30"  path="businessDescription" style="padding-top: 0px; width:100%; height :105px;    " /></div></td>
										<td class="errors">${businessDescription_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.businessDescriptionShort">
										</tag:message><span style="color: red">*</span></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="enhanced_content_field" rows="10" cols="30"  path="businessDescriptionShort" style="padding-top: 0px; width:100%; height :105px;    " /></div>
										
										</td>
										<td class="errors">${businessDescriptionShort_Error}</td>
									</tr>
									<tr>
										<td class="td_01"><div><tag:message code="businessApp.lable.ADDRESSPRIVACYFLAG">
										</tag:message></div></td>
										<td class="td_02"><div><spring:textarea class="bussinessInfo" id="enhanced_content_field"  rows="10" cols="30"  path="ADDRESSPRIVACYFLAG" style="padding-top: 0px; width:100%; height :60px;    " /></div></td>
										<td class="errors">${ADDRESSPRIVACYFLAG_Error}</td>
									</tr>	
									<tr>
										<td class="td_01"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
										<td class="td_02"><div>&nbsp;</div></td>
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
										<td ><div>&nbsp;</div></td>
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
										<td ><div>&nbsp;</div></td>
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