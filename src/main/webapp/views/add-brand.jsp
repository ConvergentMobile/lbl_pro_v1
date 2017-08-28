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
<style>
	.error { 
		color: red; font-weight: bold; 
	}
</style>
<script type="text/javascript">
$(document).ready(function() {
	//alert("coming");
	var savetype=$("#saveType").val();
	//alert("savetype"+savetype);
	if(savetype == 'update'){
		$("#addplace").hide();
		$("#addcenter").hide();
		$("#addside").hide();
		$("#addmain").hide();
		$("#editplace").show();
		$("#editmain").show();
		$("#editcenter").show();
		$("#editside").show();
	}
});

function saveChangepopup(){
	var editType = $.session.get("editType");
	var updateMsg = "Do you want to save the changes you have<br>entered for this location?";
	if ($.session.get("isSave") === "true") {
		updateMsg = "Do you want to add this new Brand?";
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
     form.action = "updateBrand.htm";
     if ($.session.get("isSave") === "true") {
    	 $.session.get("isSave",false);
    	 form.action = "addBrand.htm";
     }
     
   
     form.submit();
 } 


/*  $(document).ready(function() {
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
	}); */
	
	 /* var multiUpdateValues = ""; */
	/* $("input.bussinessInfo").blur(function() {
		
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
		 }); */
	
/* 	$("#cancel").show();
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
	}); */
	
	
	/*  var message =$("#validationSearch").val();
	    if(message != null){
	     $('#validationSearchpop').bPopup();
	     $("ul li").removeClass("selected");
	    } */
	
	
/* });
 
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
  */
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
			<a href="business-listings.htm" class="btn_dark_blue_2">Ok</a>
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

<!-- page wrapper -->
<div class="wrapper"> 
	<!-- header -->
	<div class="header"> <a href="dashboard.htm" class="logo_lbl">Local Business Listings</a>
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
			<li class="si_business_listings "><a href="business-listings.htm">Business Listings</a></li>
			<li class="si_error_listings"><a href="listing-error.htm">Listing Errors</a></li>
			<li class="si_upload_export"><a href="upload-export.htm">Upload/Export</a></li>
			<li class="si_reports"><a href="reports.htm">Reports</a></li>
				<%
						Integer Role=(Integer)session.getAttribute("roleId");				
							if(Role==LBLConstants.CONVERGENT_MOBILE_ADMIN){						
						%>
			<li class="si_admin selected"><a href="admin-listings.htm">CM admin</a></li>
			<%} %>
			<li class="si_schduler"><a href="scheduler.htm">Schedule</a></li>
			<li class="si_mobile_profile "><a href="manage-account.htm">Manage Account</a></li>
				
			<!--<li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent Toolbox</a></li>-->
		</ul>
		<!-- // left side navigation --> 
		<!-- content area -->
		
		<div class="content" id="id_content">
			<div class="nav_pointer pos_01"></div>
			<!-- subheader -->
			<div class="subheader clearfix">
				<h1 id="addmain">Add Brand</h1>
				<h1 id="editmain" style="display: none">Edit Brand</h1>
				<p>${channelName}</p>
			</div>
			<!-- // subheader -->
			<div class="inner_box"> 
				
				<div class="box box_red_title location_profile"> 
					<div class="box-sidebar">
					
						<div class="box_title" id="addside"><h2>Add Brand</h2></div>
						

						<div class="box_title" id="editside" style="display: none"><h2>Edit Brand</h2></div>
						<!-- <a href="business-listings.htm" class="lp-back"><span>Back to Business Listings</span></a> -->
						<div >
							<div class="text-1">${businessInfo.client}</div>
							<div class="text-2">${businessInfo.store}</div>
						</div>
					<%-- 	<div class="lp-closed">
							<label><spring:checkbox class="bussinessInfo"  path="locationClosed" value="Y"/> Location Closed
							<spring:hidden path="id"/></label>
						</div> --%>
						<ul class="list-1">
							<%-- <core:if test="${not empty errorListInfo }"><li><a href="#" id="error_information"><span style="color: red;">Error Information</span></a></li></core:if> --%>
						<core:choose>
						<core:when test="${fn:contains(LocationInformation_Error, 'true')}"><li><a href="#" id="location_information">
						<span style="color: red;" id="add">Add Brand</span>
						<span style="color: red;" id="edit" style="display: none">Edit Brand</span></a></li></core:when>
						<core:otherwise>
						<li><a href="#" id="location_information">
						<span id="addplace">Add Brand</span>
						<span id="editplace" style="display: none">Edit Brand</span></a></li>
						</core:otherwise>
						
						</core:choose>	
						
							<core:choose>
						<core:when test="${fn:contains(LocationContact_Error, 'true')}"><li><a href="#" id="location_content"><span  style="color: red;"></span></a></li></core:when>
						<core:otherwise>
						<li><a href="#" id="location_content"><span></span></a></li>
						</core:otherwise>
						
						</core:choose>	
							<core:choose>
						<core:when test="${fn:contains(HoursofOperation_Error, 'true')}"><li><a href="#" id="hours_of_operation"><span  style="color: red;"></span></a></li></core:when>
						<core:otherwise>
						<li><a href="#" id="hours_of_operation"><span></span></a></li>
						</core:otherwise>
						
						</core:choose>	
							<core:choose>
						<core:when test="${fn:contains(PaymentMethods_Error, 'true')}"><li><a href="#" id="payment_methods"><span style="color: red;"></span></a></li></core:when>
						<core:otherwise>
						<li><a href="#" id="payment_methods"><span></span></a></li>
						</core:otherwise>
						
						</core:choose>	
							<core:choose>
						<core:when test="${fn:contains(SocialLinks_Error, 'true')}"><li><a href="#" id="social_links"><span style="color: red;"></span></a></li></core:when>
						<core:otherwise>
						<li><a href="#" id="social_links"><span></span></a></li>
						</core:otherwise>
						
						</core:choose>	
						
								<core:choose>
						<core:when test="${fn:contains(EnhancedContent_Error, 'true')}"><li><a href="#" id="enhanced_content"><span style="color: red;"></span></a></li></core:when>
						<core:otherwise>
						<li><a href="#" id="enhanced_content"><span></span></a></li>
						</core:otherwise>						
						</core:choose>	
							
						</ul>
<!-- 						
						<span style="color: red;padding:13px 50px 10px;">*required fields</span> -->
				
					<div class="box-buttons">
							

						</div>
					</div>
			
					<div class="box-content">
						<div  id="location_profile" class="pp-body">
						
			<spring:form action="addBrand.htm" commandName="BrandInfo"
			method="post">
			<spring:hidden path="saveType" id="saveType" />
								<table id="location_information"  width="100%" class="grid grid_11 grid_11_1"
				style="width: 360px; margin: 10px auto;">
				<colgroup>
					<col width="55%" />
					<col width="45%" />
				</colgroup>
								<thead>
								
									<tr>
										<th class="th_01" colspan="2"><div id="addcenter">Add Brand</div>
										<div id="editcenter" style="display: none">Edit Brand</div>
										<%-- <spring:hidden path="multiUpdateString"	id="multiUpdateString" /> --%>
										</th>
										<th class="th_02" colspan="2"> <div></div></th>
									</tr>
								</thead>
								<tbody>
									
									<tr>
					<td class="td_01"><div><label for="channel-name">Channel Name</label></div></td>
					<td class="td_02"><div class="brands"><!-- <input type="text" value="Convergent Mobil" id="channel-name" name="channel-name"> -->
					                   <spring:input path="channelName" id="channel-name"  /> 
					                   <input type="file" id="UploadFileName" name="channelImage" class="btn_grey_2" value="Upload ChannelImage" />
					               </div></td>

					             
				</tr>
				<tr>
					<td class="td_01"><div><label for="brand-name">Brand Name</label></div></td>
					<td class="td_02"><div class="brands"><!-- <input type="text" value="Liberty Tax Services" id="brand" name="brand"> -->
					     <spring:input path="brandName" id="brand"  /></div></td>
				</tr>
				<tr>
					<td class="td_01"><div><label for="date_start">Client Id</label></div></td>
					<td class="td_02"><div class="brands"><!-- <input type="text" name="date_start" id="datepicker" class="input_date_01" value="01/01/2014"> -->
					     <spring:input path="clientId"  id="clientId" /> </div></td>
				</tr>
				
				<tr>
				<td class="td_01"><div><label for="date_start">Email</label></div></td>
					<td class="td_02"><div class="brands"><!-- <input type="text" name="date_start" id="datepicker" class="input_date_01" value="01/01/2014"> -->
					     <spring:input path="email"  id="email" /> </div></td>
				</tr>
				
				
				<tr>
					<td class="td_01"><div><label for="invoiced"># of Locations - Invoiced</label></div></td>
					<td class="td_02"><div class="brands"><!-- <input type="text" value="4,500" id="invoiced" name="invoiced" style="width:75px"> -->
					<spring:input path="locationsInvoiced" id="invoiced" style="width:75px" />
					  </div></td>
				</tr>
				<tr>
					<td class="td_01"><div><label for="email"># of Locations - Uploaded</label></div></td>
					<td class="td_02"><div>${count }</div></td>
				</tr>
							
						</tbody>
								
							</table>
						
						<div class="pp-subheader"  >Submissions</div>

				<div class="image-checkboxes">

			<core:set var="subStr"
				value="Acxiom,InfoGroup,Google,Factual,Neustar,Bing" />
			<core:set var="subBrandList"
				value="${fn:split(BrandInfo.submisions,',') }" />
			<core:set var="submissionValues" value="${fn:split(subStr, ',')}" />
			<core:forEach var="subValue" items="${submissionValues}">
				<%-- <core:set var="subValueArr" value="${fn:split(brand.submisions,'_'}" /> --%>
				<core:set var="flag" value="true" />
				<label> <core:forEach var="brandStr" items="${subBrandList}"
						varStatus="i">
						<core:set var="brandStrArr" value="${fn:split(brandStr,'_')}" />
						<core:if test="${brandStrArr[0] eq subValue}">
							<core:set var="flag" value="false" />
						
							<spring:checkbox id="partners" path="submisions"
								value="${subValue}" checked="true" />
							<img src="images/logo1_${subValue}.png" alt="${subValue}">
						
							<core:if test="${brandStrArr[1] != 'null'}">
								<p>
									<core:out value="${brandStrArr[1]}" />
								</p>
							</core:if>
						</core:if>
					</core:forEach> <core:if test="${flag}">
						<spring:checkbox id="partners" path="submisions"
							value="${subValue}" checked="" />
						<img src="images/logo1_${subValue}.png" alt="">
					</core:if>

				</label>
			</core:forEach>

		</div>
	
		

			<div class="buttons buttons-2"  style="margin: 0px 170px 20px; ">
		 <input
			type="submit" id="savepopup" class="btn_dark_blue_2" value="save">
		<input type="reset" id="cancelpopup" class="btn_dark_blue_2"
			value="cancel">

				</div>
					</spring:form>
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

</body>
</html>