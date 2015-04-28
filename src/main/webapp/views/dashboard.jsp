 <%@ taglib uri="http://www.springframework.org/tags/form" 	prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0">
<title>Local Business Listings | Dashboard</title>
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
<script src="js/jquery.cycle.all.js"></script>
<script src="js/functions.js"></script>
<script src="js/jquery.bpopup.min.js"></script>
<script src="js/jquery.jsort.0.4.min.js"></script>
<script src="js/jquery.session.js"></script>


<script type="text/javascript">
function search() {
	$("#searchAndFilterForm").attr('action',
			'dashsearch.htm');
}

function searchUser() {
	 $("#searchAndFilterForm").attr('action',
				'dashUsersSearch.htm'); 		
		
	}
	
function searchListingActivityBrand() {
	 $("#searchAndFilterForm").attr('action',
				'searchListingActivity.htm'); 		
		
	}
	
function searchListingActivityByDate() {
	//alert("ji");
	 $("#searchAndFilterForm").attr('action',
				'searchListingActivity.htm');
	 var form = document.getElementById('searchAndFilterForm');
     form.action = "searchListingActivity.htm";
     form.submit();
	}
	
function  myfunction() {
	var form ="";
	var count = 0;
	$('input[type=radio]#checkBoxValue').each(function () {
		if($(this).is(":checked")){
			++count;
			form = $("#checkBoxValue").val();
		}
	});
	
	//alert("form value=="+count);
	if(form!="" && count==1){
	 $("#searchAndFilterForm").attr('action',
		'userInfo.htm'); 
	 $.session.set("isNewUser",false);
	 return true;
	}else{
		 $('#userDetails').bPopup();
		 $("ul li").removeClass("selected");
		//alert("please select the user ");		
		return false;
	}
}

function showUplaodPopup(){
	
	$('#showuploadpopup').bPopup();
}

function load_File(id,ext){
	 $("#errorMessage").text("");
	if(validateExtension(ext) == false)
	 {
	   var errorMessage="Invalid file type. Supported types are xls, xlsx";
	   $("#errorMessage").text(errorMessage);
	  return;
	  }
}
function validateExtension(v)
{
 var allowedExtensions = new Array("xls","xlsx");
 for(var ct=0;ct<allowedExtensions.length;ct++)
 {
  sample = v.lastIndexOf(allowedExtensions[ct]);
  if(sample != -1){return true;}
  }
 return false;
 }
 
 $(document).ready(function(){
	 $.session.set("currentPage",1);
	 $.session.set("isNewUser",true);
	 $.session.set("isUploadReport",false);
	
	
	 var isAsc = true;
	$("thead#brandsTableHeaders tr th.th_02").click(function(){
		
		var currentClass = $(this).attr('id');
		//alert(currentClass);
		var currentClassArr = currentClass.split("_");
		//alert(currentClassArr[1]);
		var sortById = "td.td_"+currentClassArr[1]+" div";
		//alert(sortById);
		
		//alert(isAsc);
		if(isAsc){
			isAsc = false;
			//alert(isAsc);
			$("table#brandsTable  tbody#brandsInfo").jSort({
		        sort_by: sortById,
		        item: 'tr',
		        order: 'desc'
		    });
		 
		 
		}else{
			isAsc = true;
			 $("table#brandsTable  tbody#brandsInfo").jSort({
			        sort_by: sortById,
			        item: 'tr',
			        order: 'asc'
			 });
		}
		 
	});
	 
	var message =$("#uploadResultStatus").val();
	if(message != null){
		$('#displayUploadResultStatus').bPopup();
		$("ul li").removeClass("selected");
	}
	
	
	var message =$("#uploadHeaderResult").val();
	if(message != null){
		$('#displayUploadHeaderResult').bPopup();
		$("ul li").removeClass("selected");
	}
	
	var msg =$("#displayDistrpopup").val();
	 if(msg != null){
	  $('#brandDetails').bPopup();
	  $("ul li").removeClass("selected");
	 }
	
	 $("#uploadSubmission").click(function() {
		   $("#errorMessage").text("");
		   var fileName =$("#UploadFileName").val();
		   if(fileName.length == 0){
		    $("#errorMessage").text("Please select any one file");
		    return false;
		   }else{
		    return true;
		   }
		   
		  });	
		$(".btr_button").click(function() {
			$("ul li").removeClass("selected");
		});		
			
		 
		 var isAsc = true;
		$("thead#activityTableHeaders tr th.th_02").click(function(){			
			var currentClass = $(this).attr('id');
			//alert(currentClass);
			var currentClassArr = currentClass.split("_");
			//alert(currentClassArr[1]);
			var sortById = "td.td_"+currentClassArr[1]+" div";
		
			if(isAsc){
				isAsc = false;
				//alert(isAsc);
				$("table#displayActivity  tbody#activityInfo").jSort({
			        sort_by: sortById,
			        item: 'tr',
			        order: 'desc'
			    });			 
			}else{
				isAsc = true;
				 $("table#displayActivity  tbody#activityInfo").jSort({
				        sort_by: sortById,
				        item: 'tr',
				        order: 'asc'
				 });
			}			 
		});
	
	});
</script>


</head>

<div class="popup" id="userDetails" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		 <span class="buttonIndicator b-close"><span>X</span></span>
		<span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">
		Please select the user.
				
		</div>
	</div>
</div>



<core:if test="${showDistrpopup ne null }">
<input type="hidden" value="${showDistrpopup}" id="displayDistrpopup">
<div class="popup" id="brandDetails" style="display: none;">
 <div class="pp-header">
  <!-- <div class="close"></div> -->
  <span class="buttonIndicator b-close"><span>X</span></span>
  <span>Brand Details</span>
 </div>
 <core:forEach items="${brandNameInfo}" var="brandInfo" varStatus="i">
 <div class="pp-body">
 
  <table width="100%" class="grid grid_11 grid_11_1" style="width:360px; margin: 10px auto;">
   <colgroup>
    <col width="55%"  />
    <col width="45%" />
   </colgroup>
   <tbody>
    <tr>
     <td class="td_01"><div><label for="channel-name">Channel Name</label></div></td>
     <td class="td_02"><div><input type="text" value="${brandInfo.channelName }" readonly="true" id="channel-name" name="channel-name">
                        <%-- <spring:input path="channelName" id="channel-name" /> --%></div></td>
    </tr>
    <tr>
     <td class="td_01"><div><label for="brand-name">Brand Name</label></div></td>
     <td class="td_02"><div><input type="text" value="${brandInfo.brandName }" readonly="true" id="brand" name="brand">
          <%-- <spring:input path="brandName" id="brand"  />--%></div> </td>
    </tr>
  	<tr>
		<td class="td_01"><div><label for="date_start">Client Id</label></div></td>
		<td class="td_02"><div><input type="text" value="${ brandInfo.clientId }" readonly="true"  id="date_start"  >
					     <%-- <spring:input path="clientId"  id="clientId"  /> --%> </div></td>
	</tr>
    <tr>
     <td class="td_01"><div><label for="invoiced"># of Locations - Invoiced</label></div></td>
     <td class="td_02"><div><input type="text" value="${brandInfo.locationsInvoiced }" readonly="true" id="invoiced" name="invoiced" style="width:75px">
     <%-- <spring:input path="locationsInvoiced" id="invoiced" style="width:75px" /> --%>
       </div></td>
    </tr>
    <tr>
     <td class="td_01"><div><label for="email"># of Locations - Uploaded</label></div></td>
     <td class="td_02"><div><core:out value="${locationsUploaded}"></core:out> </div></td>
    </tr>
   </tbody>
  </table>
  
 </div>
 <div class="pp-subheader">Submissions</div>
 
 <div class="image-checkboxes">
 
 <core:forEach var="brandStr" items="${fn:split(brandInfo.submisions, ',')}">
     <core:if test="${not empty brandStr}">
        <core:set var="brandArr" value="${fn:split(brandStr,'_')}"/>
        <label> <img src="images/logo1_${brandArr[0]}.png" alt=""><%-- <core:out value="${num}" /> --%>
        <core:if test="${brandArr[1] != 'null'}">
  <p><core:out value="${brandArr[1]}"/></p></core:if></label>
     </core:if> 
  </core:forEach>
 
  
 </div>
 </core:forEach>
 
</div>
</core:if>


<div class="popup" id="showuploadpopup" style="display: none">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span>
		Upload File
	</div>
	<div class="pp-subheader">Make sure you are using our <a href="download.htm">bulk upload template</a> for your data</div>
	<div class="pp-body">
	<spring:form action="uploadBusiness.htm?page=dashboard" commandName="uploadBusiness"
			enctype="multipart/form-data">
		

			<div class="upload">
		<p id="errorMessage" style="color: red;"></p>
			
			<input  type="file" id="UploadFileName"  name="targetFile" class="btn_grey_2" value="Upload" onChange="load_File(this.id,this.value)" />
			<input type="submit" id="uploadSubmission" class="btn_dark_blue_2" value="Upload" />
		</div>
	</spring:form>	
	</div>
</div>

<core:if test="${not empty headerPopup}">
<input type="hidden" value="${headerPopup}" id="uploadHeaderResult">
<div class="popup" id="displayUploadHeaderResult" style="display: none">
		<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span>
		Upload File
	</div>
	<div class="pp-subheader">Make sure you are using our <a href="download.htm">bulk upload template</a> for your data</div>
	<div class="pp-body">
	<spring:form action="uploadBusiness.htm?page=dashboard" commandName="uploadBusiness"
			enctype="multipart/form-data">
			
		
		<div class="result error">${headerPopup}</div>
		
			<div class="upload">
		<p id="errorMessage" style="color: red;"></p>
			
			<input  type="file" id="UploadFileName"  name="targetFile" class="btn_grey_2" value="Upload" onChange="load_File(this.id,this.value)" />
			<input type="submit" id="uploadSubmission" class="btn_dark_blue_2" value="Upload" />
		</div>
	</spring:form>	
	</div>
</div>
</core:if>

<core:if test="${showPopup ne null }">
<input type="hidden" value="${showPopup}" id="uploadResultStatus">
<div class="popup" id="displayUploadResultStatus" style="display: none">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span>
		Upload File
	</div>
	<div class="pp-body">
		<!-- <div class="result success">Upload complete, results can be seen in Business Listings.</div> -->
		<div class="result success">${message}</div>
		<core:if test="${fn:contains(message, 'Upload complete')}">
		<div class="result-details">
			<ul>
				<li><span>${listingsUploaded }</span> Listings Uploaded</li>
				<li><span>${listingsCompleted }</span> Listings Completed</li>
				<li class="red"><span>${listingsWithErrors }</span> Listings with Errors</li>
			</ul>
			<a href="view-listings.htm" class="btn_dark_blue_2">View Listings</a>
			<a href="view-errors.htm" style=" margin-left: 251px;margin-top: -46px;" class="btn_dark_blue_2">View Errors</a>
		</div>
		</core:if>
	</div>
</div>
</core:if>

<body id="office">
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
	<div class="content_wrapper"> 
		<!-- left side navigation -->
		<ul class="ul_left_nav">
			<li class="si_dashboard selected"><a href="dash-board.htm">Dashboard</a></li>
			<li class="si_business_listings"><a href="business-listings.htm">Business Listings</a></li>
			<li class="si_error_listings"><a href="listing-error.htm">Listing Errors</a></li>
			<li class="si_upload_export"><a href="upload-export.htm">Upload/Export</a></li>
			<li class="si_reports"><a href="reports.htm">Reports</a></li>
			<li class="si_mobile_profile "><a href="manage-account.htm">Manage Account</a></li>
			<li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent Toolbox</a></li>
		</ul>
		<!-- // left side navigation --> 
		<!-- content area -->
		<spring:form method="post" commandName="searchBusiness"
			id="searchAndFilterForm">
		<div class="content" id="id_content">
			<div class="nav_pointer pos_01"></div>
			<!-- subheader -->
			<div class="subheader clearfix">
				<h1>DASHBOARD</h1>
				<p> ${channelName} </p>
			</div>
			<!-- // subheader -->
			<div class="inner_box"> 
				<!-- box -->
				<div class="box box_red_title brands_overview"> 
					<!-- title -->
					<div class="box_title">
						<h2>Brands Overview</h2>
						<div class="box_title_right"> <span>Total: ${brandSize } brands</span> 
						<%
						Boolean isViewOnly = (Boolean) session.getAttribute("isViewOnly");
						if(!isViewOnly) { %>
							<a href="#" onclick="showUplaodPopup();" class="btr_button">Upload</a>
						<% }
			
						%>
						 </div>

					</div>
					<!-- // title -->
					<table width="100%" class="grid grid_07">
						<colgroup>
						<col width="8%"  />
						<col width="40%" />
						<col width="40%" />
						<col width="12%" />
						</colgroup>
						<thead id="brandsTableHeaders">
							<tr>
								<th class="th_01"><div>Distr.</div></th>
								<th class="th_02" id="th_02"><div>Brand Name</div></th>
								<th class="th_02" id="th_03"><div>Total Locations</div></th>
								<th class="th_04"><div>Edit</div></th>
							</tr>
						</thead>
					</table>
					<div id="id_brands_overview">
					
						<table id="brandsTable" width="100%" class="grid grid_07">					
							<colgroup>
							<col width="8%"  />
							<col width="40%" />
							<col width="40%" />
							<col width="12%" />
							</colgroup>
							<tbody id="brandsInfo">		
								<core:forEach items="${brandsInfo}" var="brands" varStatus="i">					
							
								<tr class="odd">
									<td class="td_01" ><div><a href="distrBrands.htm?distr=${brands.brands}"   class="lnk_distr">Distr</a></div></td>
									<td class="td_02"><div><core:out value="${brands.brands}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${brands.brandsCount}"></core:out></div></td>
									<td class="td_04"><div><a href="editbrandInfo.htm?brand=${brands.brands}" class="lnk_edit_small">Edit</a></div></td>
								</tr>
							
								</core:forEach>
								</tbody>
						</table>
					
					</div>
				</div>
				<!-- // box --> 
				<!-- box -->
				<div class="box box_darkblue_title listing_activity" style="margin-bottom:0">
     <!-- title -->
     <div class="box_title">
      <h2>Listing Activity</h2>
     </div>
     <!-- // title -->
     <div class="filters">
      <div class="f_group">
       <label for="search_brands">Search Brands</label>
       <spring:input path="brands" style="width:100px;" />
		<button class="btn_search" onclick="searchListingActivityBrand()"><span>search</span></button>
      </div>
      <div class="f_group">
       <label for="display_activity_for">Display Activity for</label>
       <spring:select path="dateRange" id="display_activity_for" onchange="searchListingActivityByDate()">
        <spring:option value="all">all</spring:option>
        <spring:option value="this month">this month</spring:option>
        <spring:option value="last month">last month</spring:option>
         <spring:option value="last 3 months">last 3 months</spring:option>
        
       </spring:select>
      </div>
     </div>
     <table width="100%" class="grid grid_07 grid_07_1">
      <colgroup>
      <col width="20%" />
      <col width="25%" />
      <col width="35%" />
      <col width="20%" />
      </colgroup>
      <thead id="activityTableHeaders">
       <tr>
        <th class="th_02" id="th_02"><div>Date</div></th>
        <th class="th_02" id="th_03"><div>Brand Name</div></th>
        <th class="th_02" id="th_04"><div>Activity Description</div></th>
        <th class="th_02" id="th_05"><div>Count</div></th>
       </tr>
      </thead>
     </table>
     <div id="id_listing_activity">
     
      <table id="displayActivity" width="100%" class="grid grid_07 grid_07_1">
       <colgroup>
       <col width="20%"  />
       <col width="25%" />
       <col width="35%" />
       <col width="20%" />
       </colgroup>
       <tbody id="activityInfo">
      
         <core:forEach items="${listingActivityInfo}" var="activity" varStatus="i">
        <tr class="odd">
         <td class="td_02"><div><core:out value="${activity.date}"></core:out></div></td>
         <td class="td_03"><div><core:out value="${activity.brandName}"></core:out></div></td>
         <td class="td_04"><div><core:out value="${activity.activityDescription}"></core:out></div></td>
         <td class="td_05"><div><core:out value="${activity.count}"></core:out></div></td>
        </tr>
  
          </core:forEach>
        </tbody>
    
       
      </table>
   
     </div>
    </div>
			</div>
		</div>
		<!-- // content area --> 
		<!-- sidebar -->
		<div class="sidebar" id="id_sidebar">
			<div class="inner"> 
				<div class="sb_box_wrapper">
					<!-- title -->
					<div class="sb_title sb_title_ico ico_sb_search">
						<h2 class="">Quick Search</h2>
					</div>
					<!-- // title --> 
					<!-- sidebar box -->
					<div class="sb_box" style="padding: 15px 0px;">
						<table width="100%" class="grid grid_08" style="margin: 5px 19px;">
							<colgroup>
								<col width="32%"  />
								<col width="68%" />
							</colgroup>
							<tbody>
								<tr>
									<td class="td_01"><div>
											<label for="brand_name">Brand Name</label>
										</div></td>
									<td class="td_02"><div>
									<spring:input path="client" />
											
										</div></td>
								</tr>
								<tr>
									<td class="td_01"><div>
											<label for="business_name">Business Name</label>
										</div></td>
									<td class="td_02"><div>
									<spring:input path="companyName"  />
											
										</div></td>
								</tr>
								<tr>
									<td class="td_01"><div>
											<label for="store_number">Store Number</label>
										</div></td>
									<td class="td_02"><div>
									
									<spring:input path="store"  />
											
										</div></td>
								</tr>
								<tr>
									<td class="td_01"><div>
											<label for="phone_number">Phone Number</label>
										</div></td>
									<td class="td_02"><div>
									<spring:input path="locationPhone"  />
										
										</div></td>
								</tr>
								<tr>
									<td class="td_01"><div></div></td>
									<td class="td_02"><div>
									<input type="submit"
									value="Search" onclick="search()" class="btn_dark_blue_2"/>
										<!-- 	<a href="#" >Search</a> -->
											<a href="business-listings.htm" class="lnk_advs">Advanced<br> Search</a>
										</div></td>
								</tr>
							</tbody>
						</table>
					</div>
					<!-- // sidebar box --> 
				</div>
				<div class="sb_box_wrapper">
					<!-- title -->
					<div class="sb_title sb_title_ico ico_sb_user">
						<h2 class="">User Accounts</h2>
					</div>
					<!-- // title --> 
					<!-- sidebar box -->
					<div class="sb_box">
						<div class="sb_box" style="padding: 5px 0 0;">
						<table width="100%" class="grid grid_08" style="margin: 5px 19px;">
							<colgroup>
								<col width="40%"  />
								<col width="60%" />
							</colgroup>
							<tbody>
								<tr>
									<td class="td_01"><div>
											<label for="search_users">Search Users</label>
										</div></td>
									<td class="td_02"><div>
											<spring:input path="searchName" style="width:65%; margin-right:5px" />
											<button class="btn_search" onclick="searchUser()"><span>search</span></button>
										</div></td>
								</tr>
							</tbody>
						</table>
						<table width="100%" class="grid grid_09">
							<colgroup>
								<col width="12%" />
								<col width="30%" />
								<col width="30%" />
								<col width="28%" />
							</colgroup>
							<thead>
								<tr>
									<th class="th_01"><div></div></th>
									<th class="th_02"><div>Name</div></th>
									<th class="th_04"><div>User Name</div></th>
									<th class="th_04"><div><a href="dash-board.htm" class="lnk_advs">Clear Search</a></div></th>
									
								</tr>
							</thead>
						</table>
						<div class="sb_box_shadow">
							<div id="id_user_accounts">
							<core:forEach items="${usersListInfo}" var="users">
								<table width="100%" class="grid grid_09">
									<colgroup>
										<col width="12%" />
										<col width="38%" />
										<col width="50%" />
									</colgroup>
									<tbody>
										<tr>
											<td class="td_01"><div><input type="radio" value="${users.userID}" id="checkBoxValue" name="userID"></div></td>
											<td class="td_02"><div >${users.name}&nbsp; ${users.lastName}</div></td>
											<td class="td_02"><div >${users.userName}</div></td>
										</tr>										
										
									</tbody>
								</table>
								
							</core:forEach>
							</div>
						</div>
						<div class="sb_buttons_center">
							<input type="submit" onclick="return myfunction();" value="Expand" class="btn_dark_blue_2">
						</div>
					</div>
					<!-- // sidebar box --> 
				</div>
			</div>
			</spring:form>
		</div>
		<!-- // sidebar -->
		<div class="floatfix"></div>
	</div>
		<div class="clearfix"></div>
	<!-- // content wrapper --> 
	<!-- footer -->
	<div class="footer">
		<div class="powered">Powered by</div>
	</div>
	<!-- // footer --> 
</div>
<!-- // page wrapper -->

</body>
</html>