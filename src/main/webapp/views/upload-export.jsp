 <%@page import="com.business.common.util.LBLConstants"%>
<%@ taglib uri="http://www.springframework.org/tags/form" 	prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0">
<title>Local Business Listings | Upload/Export</title>
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

<script type="text/javascript">

function bulkExportBusiness() {
	
	 var serviceName= document.getElementById("serviceName").value;
	 if(serviceName.length == 0 || $(serviceName).val() == ""){
		 $('#templatetemplatepopup').bPopup();
			$("ul li").removeClass("selected");
		
		   //alert("Please select  any one template");
		   event.preventDefault();
		}else{			
		 document.location.href = "bulkExports?service="+serviceName;
		}	
	}
	
function sendSubmission() {
	 document.location.href = "sendSubmission.htm";
	}	
	
$(document).ready(function(){
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
 });
 

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


</script>


</head>

<div class="popup" id="templatetemplatepopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		 <span class="buttonIndicator b-close"><span>X</span></span>
		<span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">
		Please select ant one template.				
		</div>
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
	<spring:form action="uploadBusiness.htm?page=uploadExport" commandName="uploadBusiness"
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
		<!-- <div class="result success"></div> -->
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
	<div class="content_wrapper no-sidebar"> 
		<!-- left side navigation -->
		<ul class="ul_left_nav">
			<li class="si_dashboard"><a href="dash-board.htm">Dashboard</a></li>
			<li class="si_business_listings"><a href="business-listings.htm">Business Listings</a></li>
			<li class="si_error_listings"><a href="listing-error.htm">Listing Errors</a></li>
			<li class="si_upload_export selected"><a href="upload-export.htm">Upload/Export</a></li>
			<li class="si_reports"><a href="reports.htm">Reports</a></li>
			<li class="si_mobile_profile "><a href="manage-account.htm">Manage Account</a></li>
			<li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent Toolbox</a></li>
		</ul>
		<!-- // left side navigation --> 
		<!-- content area -->
		<div class="content" id="id_content">
			<div class="nav_pointer pos_01"></div>
			<!-- subheader -->
			<div class="subheader clearfix">
				<h1>UPLOAD / EXPORT</h1>
				<p>${channelName}</p>
			</div>
			<!-- // subheader -->
			<div class="inner_box"> 
				<!-- box -->
				<div class="box box_red_title upload"> 
					<!-- title -->
					<div class="box_title">
						<h2>Upload</h2>
					</div>
					<!-- // title -->
					<div class="box_subtitle_text">
						All uploads must use the formatting from this <a href="download.htm">bulk upload template</a>.<br>Questions on the bulk template? Please visit the <a href="help.htm">Help page</a>, or <a href="MAILTO:LBLPro@convergentmobile.com" >Contact Us</a>.
					</div>
					<spring:form action="uploadBusiness.htm?page=uploadExport" commandName="uploadBusiness"
					enctype="multipart/form-data">
					<div class="upload_client">	
					
					
				<%-- 	Client To Upload :<select name="client"  id="myDropdown">
									<option value="">select client</option>
									<core:forEach items="${clientNameInfo}" var="client">
										<option value="${client.brandName}">${client.brandName}</option>
									</core:forEach>
								</select> --%>
								</div>
					 <div class="box_upload">	
								

								<p id="errorMessage" style="color: red;"></p>
					
						<input  type="file" id="UploadFileName"  name="targetFile" class="btn_grey_2" value="Upload" onChange="load_File(this.id,this.value)" style="padding: 10;  height: 43px;"/>
							<div style="margin-top: -37px;margin-left: 335px;">
							
								<%	Boolean isViewOnly = (Boolean) session.getAttribute("isViewOnly");
							if(!isViewOnly) { %>
								<input type="submit" id="uploadSubmission" class="btn_dark_blue_2" value="Upload" />
							<% } %>

							</div>
				</div>
					</spring:form> 
				</div>
				<!-- // box --> 
				<!-- box -->
				<div class="box box_darkblue_title export" style="margin-bottom:0">
					<!-- title -->
					<div class="box_title">
						<h2>Export</h2>
					</div>
					<!-- // title -->
					<div class="box_subtitle_text">
						Select the template for location formatting. All locations from your current account will be exported.<br>To export specific locations go to the <a href="business-listings.htm">Business Listings</a> page.
					</div>
					<div class="box_export">
						<select name="service" id="serviceName">
			    <option value="">Choose Tempate</option>
			     <option value="BingTemplate">Bing</option>
			    <option value="GoogleTemplate">Google</option>
			    <option value="MasterTemplate">Master Template</option>
			    
			   <!--  <option value="Neustar">Neustar</option>
			    <option value="InfoGroup">InfoGroup</option>
			    <option value="Factual">Factual</option> 
			    <option value="Acxiom">Acxiom</option>
			    <option value="Yahoo">Yahoo</option>
			    <option value="Bing">Bing</option>
			    <option value="Google">Google</option>
			    <option value="TBD">TBD</option> -->
			   </select>
						<a onclick="bulkExportBusiness();" class="btn_dark_blue_2" id="bulkExport">Export</a>
						<p><a href="MAILTO:LBLPro@convergentmobile.com">Contact Us</a> if there are additional formats needed for Exporting.</p>
						<div>
							<%
							Integer Role=(Integer)session.getAttribute("roleId");				
							if(Role==LBLConstants.CONVERGENT_MOBILE_ADMIN){						
							%>
							<a href="#" onclick="sendSubmission();" class="btn_dark_blue_2">Send Submissions</a>
							<%}%>
						</div>
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
											<input type="text" value="" name="brand_name" id="brand_name">
										</div></td>
								</tr>
								<tr>
									<td class="td_01"><div>
											<label for="business_name">Business Name</label>
										</div></td>
									<td class="td_02"><div>
											<input type="text" value="" name="business_name" id="business_name">
										</div></td>
								</tr>
								<tr>
									<td class="td_01"><div>
											<label for="store_number">Store Number</label>
										</div></td>
									<td class="td_02"><div>
											<input type="text" value="" name="store_number" id="store_number">
										</div></td>
								</tr>
								<tr>
									<td class="td_01"><div>
											<label for="phone_number">Phone Number</label>
										</div></td>
									<td class="td_02"><div>
											<input type="text" value="" name="phone_number" id="phone_number">
										</div></td>
								</tr>
								<tr>
									<td class="td_01"><div></div></td>
									<td class="td_02"><div>
											<a href="#" class="btn_dark_blue_2">Search</a>
											<a href="#" class="lnk_advs">Advanced<br> Search</a>
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
											<input type="text" value="" name="search_users" id="search_users" style="width:65%; margin-right:5px">
											<a href="#" class="btn_search"><span>Search</span></a>
										</div></td>
								</tr>
							</tbody>
						</table>
						<table width="100%" class="grid grid_09">
							<colgroup>
								<col width="12%" />
								<col width="38%" />
								<col width="50%" />
							</colgroup>
							<thead>
								<tr>
									<th class="th_01"><div></div></th>
									<th class="th_02"><div>Name</div></th>
									<th class="th_04"><div>User Name</div></th>
								</tr>
							</thead>
						</table>
						<div class="sb_box_shadow">
							<div id="id_user_accounts">
								<table width="100%" class="grid grid_09">
									<colgroup>
										<col width="12%" />
										<col width="38%" />
										<col width="50%" />
									</colgroup>
									<tbody>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>Anamda Smith</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>John Brown</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>Matt Walker</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>Alexandra Brown</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>Maureen Hanton</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>John Novak</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>Anamda Smith</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>John Brown</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>Matt Walker</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>Alexandra Brown</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>Maureen Hanton</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>John Novak</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>Anamda Smith</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>John Brown</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>Matt Walker</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>Alexandra Brown</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>Maureen Hanton</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										<tr>
											<td class="td_01"><div><input type="checkbox"></div></td>
											<td class="td_02"><div>John Novak</div></td>
											<td class="td_02"><div>asmith</div></td>
										</tr>
										
									</tbody>
								</table>
							</div>
						</div>
						<div class="sb_buttons_center">
							<a href="#" class="btn_dark_blue_2">Expand</a>
						</div>
					</div>
					<!-- // sidebar box --> 
				</div>
			</div>
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
<div class="popup-shader" style="display: none"></div>
<div class="popup" style="display: none">
	<div class="pp-header">
		<div class="close"></div>
		Upload File
	</div>
	<div class="pp-subheader">Make sure you are using our <a href="#">bulk upload template</a> for your data</div>
	<div class="pp-body">
		<div class="upload">
			<a href="#" class="btn_grey_2">Choose file</a>
			<span>No file selected</span>
			<a href="#" class="btn_dark_blue_2">Upload</a>
		</div>
	</div>
</div>


<div class="popup" style="display:none">
	<div class="pp-header">
		<div class="close"></div>
		Upload File
	</div>
	<div class="pp-body">
		<div class="result success">Upload complete, results can be seen in Business Listings.</div>
		<div class="result-details">
			<ul>
				<li><span>1234</span> Listings Uploaded</li>
				<li><span>1234</span> Listings Completed</li>
				<li class="red"><span>19</span> Listings with Errors</li>
			</ul>
			<a href="#" class="btn_dark_blue_2">View Listings</a>
		</div>
	</div>
</div>

<div class="popup" style="display: none">
	<div class="pp-header">
		<div class="close"></div>
		Upload File
	</div>
	<div class="pp-subheader">Make sure you are using our <a href="#">bulk upload template</a> for your data</div>
	<div class="pp-body">
		<div class="result error">Upload incomplete. Error code: 12345678</div>
		<div class="upload">
			<a href="#" class="btn_grey_2">Choose file</a>
			<span>No file selected</span>
			<a href="#" class="btn_dark_blue_2">Upload</a>
		</div>
	</div>
</div>

</body>
</html>