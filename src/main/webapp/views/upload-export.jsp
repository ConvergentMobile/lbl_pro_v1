
<%@page import="com.business.common.util.LBLConstants"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-1.9.1.min.js"></script>
<script src="js/jquery.idTabs.min.js"></script>
<script src="js/jquery.slimscroll.min.js"></script>
<script src="js/jquery.screwdefaultbuttonsV2.js"></script>
<script src="js/jquery.cycle.all.js"></script>
<script src="js/functions.js"></script>
<script src="js/jquery.bpopup.min.js"></script>
<link rel="stylesheet" href="js/jquery.mCustomScrollbar.css">
<script src="js/jquery.mCustomScrollbar.js"></script>
<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<style type="text/css">

.box.upload .box_title_right {
    display: none;
}

</style>
<!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8/jquery.js" ></script>
<script src="http://malsup.github.com/jquery.form.js" ></script>
<script src="js/fileUploadScript.js" ></script> -->


<!-- <script>
	$(document).ready(function() {
		
		//elements
		var progressbox = $('#progressbox');
		var progressbar = $('#progressbar');
		var statustxt = $('#statustxt');
		var submitbutton = $("#SubmitButton");
		var myform = $("#UploadForm");
		var output = $("#output");
		var completed = '0%';

		$(myform).ajaxForm({
			beforeSend : function() { //brfore sending form
				submitbutton.attr('disabled', ''); // disable upload button
				statustxt.empty();
				progressbox.slideDown(); //show progressbar
				progressbar.width(completed); //initial value 0% of progressbar
				statustxt.html(completed); //set status text
				statustxt.css('color', '#000'); //initial color of status text
			},
			uploadProgress : function(event, position, total, percentComplete) { //on progress
				progressbar.width(percentComplete + '%'); //update progressbar percent complete
				statustxt.html(percentComplete + '%'); //update status text
				if (percentComplete > 50) {
					statustxt.css('color', '#fff'); //change status text to white after 50%
				}
			},
			complete : function(response) { // on complete
				output.html(response.responseText); //update element with received data
				myform.resetForm(); // reset form
				submitbutton.removeAttr('disabled'); //enable submit button
				progressbox.slideUp(); // hide progressbar

			}
			
		});


	
		
	});
</script>
 -->
<script type="text/javascript">
	function bulkExportBusiness() {

		var serviceName = document.getElementById("serviceName").value;
		var clientName = document.getElementById("clientName").value;
		if (serviceName.length == 0 || $(serviceName).val() == "") {
			$('#templatetemplatepopup').bPopup();
			$("ul li").removeClass("selected");

			//alert("Please select  any one template");
			event.preventDefault();
		}else 	if (clientName.length == 0 || $(clientName).val() == "") {
			$('#brandpopup').bPopup();
			$("ul li").removeClass("selected");

			//alert("Please select  any one template");
			event.preventDefault();
		}  else {
			document.location.href = "bulkExports?service=" + serviceName
					+ "&client=" + clientName;
		}
	}

	function sendSubmission() {
		var clientName = document.getElementById("clientName").value;
		if(clientName==''){
			$('#brandpopup').bPopup();
		}else{
			document.location.href = "sendSubmission.htm?client=" + clientName;
		}
		
	}
	function getJsonExport(){
		$('#container').empty();
		var serviceName = document.getElementById("serviceName").value;
		var clientName = document.getElementById("clientName").value;
		if (serviceName.length == 0 || $(serviceName).val() == "") {
			$('#templatetemplatepopup').bPopup();
			$("ul li").removeClass("selected");

			//alert("Please select  any one template");
			event.preventDefault();
		}else if(clientName==''){
			$('#brandpopup').bPopup();
		}else{
			 $.ajax({
					type : "get",
					url : "getJSOnExport.htm",
					cache : false,
					data:{
						client:clientName,service:serviceName
					},
					success : function(response) {
						//alert(response);
						var data = "text/json;charset=utf-8," + encodeURIComponent(response);
						$('#container').html('<a href="data:' + data + '" download="listings.json" class="btn_dark_blue_2">Json Download</a>');
						//$('<a href="data:' + data + '" download="listings.json" class="btn_dark_blue_2">ExportInJson</a>').html('#container');
					
						$('#container').show();
					}
			 });
			 
		}
	}
	function renewBrand() {
		var clientName = document.getElementById("clientName").value;
		if(clientName==''){
			$('#brandpopup').bPopup();
		}else{
			document.location.href = "renewListings.htm?client=" + clientName;
		}
		
	}

	$(document).ready(function() {
		
		/* $("#uploadSubmission").click(function(){
			///alert("coing");
			
		}); */
		var message = $("#uploadResultStatus").val();
		if (message != null) {
			$('#displayUploadResultStatus').bPopup();
			$("ul li").removeClass("selected");
		}

		var message = $("#uploadHeaderResult").val();
		if (message != null) {
			$('#displayUploadHeaderResult').bPopup();
			$("ul li").removeClass("selected");
		}

		$("#uploadSubmission").click(function() {
			//alert("coming");
			$("#errorMessageVal").html("");
			var fileName = $("#UploadFileName").val();
			if (fileName.length == 0) {
				$("#errorMessageVal").html("Please select a file to upload");
				return false;
			} else {
				$('#statusPopup').bPopup();
				return true;
			}
		});

		$("#popupcat").click(function() {
			$('#showuploadpopup').bPopup();
			$("ul li").removeClass("selected");
		});

	});

	function load_File(id, ext) {
		$("#errorMessage").text("");
		if (validateExtension(ext) == false) {
			var errorMessage = "Invalid file type. Supported types are xls, xlsx";
			$("#errorMessage").text(errorMessage);
			return;
		}
	}
	function validateExtension(v) {
		var allowedExtensions = new Array("xls", "xlsx");
		for (var ct = 0; ct < allowedExtensions.length; ct++) {
			sample = v.lastIndexOf(allowedExtensions[ct]);
			if (sample != -1) {
				return true;
			}
		}
		return false;
	}
	
	function runWsReport() {
		document.location.href = "runWsReport.htm";
	}
	
	function parseDomains() {
		document.location.href = "parseDomains.htm";
	}
	function closewindow(){
		window.location.reload();
	}
	
	/* function exportSprint() {
		document.location.href = "sprintreportxls.htm";
	} */
	
</script>


</head>


<div class="popup" id="showuploadpopup" style="display: none">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> Upload
		File
	</div>
	<div class="pp-subheader">
		Make sure you are using our <a href="download.htm">bulk upload
			template</a> for your data
	</div>
	<div class="pp-body">
		<spring:form action="catgoryBusiness.htm"
			commandName="CatgoryBusiness" enctype="multipart/form-data">


			<div class="upload">
				<p id="errorMessage" style="color: red;"></p>

				<input type="file" name="targetFile" class="btn_grey_2"
					value="Upload" /> <input type="submit" class="btn_dark_blue_2"
					value="Upload" />
			</div>
		</spring:form>
	</div>
</div>

<div class="popup" id="statusPopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Message</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons" ><span style="color: green">Uploading is in progress. This process may take several minutes. Please wait for upload to complete...</span></div>
	</div>
</div>
<div class="popup" id="brandpopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select at Brand for Submit.</div>
	</div>
</div>

<div class="popup" id="templatetemplatepopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select at least one template.</div>
	</div>
</div>

<core:if test="${not empty headerPopup}">
	<input type="hidden" value="${headerPopup}" id="uploadHeaderResult">
	<div class="popup" id="displayUploadHeaderResult" style="display: none">
		<div class="pp-header">
			<!-- <div class="close"></div> -->
			<span class="buttonIndicator b-close"><span>X</span></span> Upload
			File
		</div>
		<div class="pp-subheader">
			Make sure you are using our <a href="download.htm">bulk upload
				template</a> for your data
		</div>
		<div class="pp-body">
			<spring:form action="uploadBusiness.htm?page=uploadExport"
				commandName="uploadBusiness" enctype="multipart/form-data">


				<div class="result error">${headerPopup}</div>

				<div class="upload">
					<p id="errorMessage" style="color: red;"></p>

					<input type="file" id="UploadFileName" name="targetFile"
						class="btn_grey_2" value="Upload"
						onChange="load_File(this.id,this.value)" /> <input type="submit"
						id="uploadSubmission" class="btn_dark_blue_2" value="Upload" />
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
			<span class="buttonIndicator b-close"><span onclick="closewindow();">X</span></span> Upload
			File
		</div>
		<div class="pp-body">
			<!-- <div class="result success"></div> -->
			<div class="result success">${message}</div>
			<core:if test="${fn:contains(message, 'Upload complete')}">
				<div class="result-details">
					<ul>
						<li><span>${listingsUploaded }</span> Listings Uploaded</li>
						<li><span>${listingsCompleted }</span> Listings Completed</li>
						<li class="red"><span>${listingsWithErrors }</span> Listings
							with Errors</li>
					</ul>
					<a href="view-listings.htm" class="btn_dark_blue_2" style=" margin: 0px 0px 61px;">View
						Listings</a> <a href="view-errors.htm"
						style="margin-left: 251px; margin-top: -46px;"
						class="btn_dark_blue_2">View Errors</a>
				</div>
			</core:if>
		</div>
	</div>
</core:if>
<body id="office">
	<!-- page wrapper -->
	<div class="wrapper">
		<!-- header -->
		<div class="header">
			<a href="dash-board.htm" class="logo_lbl">Local Business Listings</a>
			<ul class="top_nav">
				<li class="home"><a href="dash-board.htm">Home</a></li>
				<li class="help"><a href="help.htm">Help</a></li>
				<li class="signout"><a href="logout.htm" >Signout</a></li>
				<li class="welcome">
					<p>
						Hello, <br> ${userName}
					</p>
				</li>
			</ul>
		</div>
		<!-- // header -->
		<!-- content wrapper -->
		<div class="content_wrapper no-sidebar">
			<!-- left side navigation -->
			<ul class="ul_left_nav">
				<li class="si_dashboard"><a href="dash-board.htm">Dashboard</a></li>
				<li class="si_business_listings"><a
					href="business-listings.htm">Business Listings</a></li>
				<li class="si_error_listings"><a href="listing-error.htm">Listing
						Errors</a></li>
				<li class="si_upload_export selected"><a
					href="upload-export.htm ">Upload/Export</a></li>
				<li class="si_reports"><a href="reports.htm">Reports</a></li>
				<%
						Integer Roleid=(Integer)session.getAttribute("roleId");				
							if(Roleid==LBLConstants.CONVERGENT_MOBILE_ADMIN){						
						%>
				<li class="si_admin"><a href="admin-listings.htm">CM admin</a></li>
				<li class="si_mobile_profile "><a href="manage-account.htm">Manage
						Account</a></li>
				<%} %>
			<!-- 	<li class="si_schduler"><a href="scheduler.htm">Schedule</a></li> 
				

				<li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent
						Toolbox</a></li>-->
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
							<div class="box_title_right">
								<span></span>
							</div>
						</div>
						<!-- // title -->
						<div class="box_subtitle_text">
							All uploads must use the formatting from this <a
								href="download.htm">bulk upload template</a>.<br>Questions
							on the bulk template? Please visit the <a href="help.htm">Help
								page</a>, or <a href="MAILTO:LBLPro@convergentmobile.com">Contact
								Us</a>.
						</div>
						
						<!-- <p id="errorMessage" style="color: red;"></p> -->
						<spring:form action="uploadBusiness.htm?page=uploadExport"
							commandName="uploadBusiness" enctype="multipart/form-data"
							id="UploadForm">
							

					
								<div id="progressbox">
								
									<div id="progressbar"></div>
									<div id="statustxt">0%</div>
									
								</div>
								
						<!-- //<div id="output" style="display: none"></div> -->

							<div class="box_upload">


								

								<input type="file" id="UploadFileName" name="targetFile"
									class="btn_grey_2" value="Upload"
									onChange="load_File(this.id,this.value)"
									style="padding: 10; height: 43px;" />
									<p id="errorMessageVal" style="color: red;"></p>
								<div style="margin-top: -37px; margin-left: 335px;">
								

									<%
										Boolean isViewOnly = (Boolean) session
													.getAttribute("isViewOnly");
											if (!isViewOnly) {
									%>
									
									
									<input type="submit" id="uploadSubmission"
										class="btn_dark_blue_2" value="Upload" />
									<%
										}
									%>

								</div>
							</div>
							<%-- <div style="height: 70px">
							<div class="box_upload" style="width: 570px;" >
								<spring:form action="catgoryBusiness.htm"
									commandName="CatgoryBusiness" enctype="multipart/form-data">


									<p id="errorMessage" style="color: red;"></p>

									<input type="file" name="targetFile" class="btn_grey_2"
										value="Upload" style="padding: 10; height: 43px;"  />
										<div style="margin-top: -37px; margin-left: 335px;">
									<input type="submit" class="btn_dark_blue_2"
										value="Upload Categories" /></div>

								</spring:form>
							</div>
							</div> --%>
						</spring:form>

					</div>
					<!-- // box -->
					<!-- box -->
					<div class="box box_darkblue_title export" style="margin-bottom: 0">
						<!-- title -->
						<div class="box_title">
							<h2>Export</h2>
						</div>
						<!-- // title -->
						<div class="box_subtitle_text">
							Select the template for location formatting. All locations from
							your current account will be exported.<br>To export specific
							locations go to the <a href="business-listings.htm">Business
								Listings</a> page.
						</div>
						<div class="box_export1">
							<select name="client" id="clientName">
								<option value="">SELECT BRANDS</option>
								<option value="All Brands">All Brands</option>
								<core:forEach items="${clientNameInfo}" var="client">
									<option value="${client.brandName}">${client.brandName}</option>
								</core:forEach>
							</select>
						</div>
						<div class="box_export">
							<select name="service" id="serviceName" onchange="getJsonExport()">
								<option value="">Choose Template</option>
								<option value="BingTemplate">Bing</option>
								<option value="GoogleTemplate">Google</option>
								<option value="MasterTemplate">Master Template</option>
								<option value="YPTemplate">YP</option>
									<option value="yelpTemplate">Yelp</option>
										<option value="yextTemplate">Yext</option>
										<option value="InfogroupTemplate">Infogroup</option>
										<option value="LocalezeTemplate">Localeze</option>
										<option value="AcxiomUsTemplate">Acxiom_US</option>
										<option value="AcxiomCanTemplate">Acxiom_Canada</option>
										<option value="AppleTemplate">Apple Template</option>
									
							</select> <a onclick="bulkExportBusiness();"  class="btn_dark_blue_2"
								id="bulkExport">Export</a>
								
							<p>
								<a href="MAILTO:LBLPro@convergentmobile.com">Contact Us</a> if
								there are additional formats needed for Exporting.
							</p>
							<div>
								<%
									Integer Role = (Integer) session.getAttribute("roleId");
									if (Role == LBLConstants.CONVERGENT_MOBILE_ADMIN) {
								%>
								<a href="#" onclick="sendSubmission();" class="btn_dark_blue_2">Send
									Submissions</a>
									<!--  <a href="#" onclick="runWsReport();"
									class="btn_dark_blue_2">Run Whitespark</a> -->
									
							<!-- 	 <a href="#" onclick="parseDomains();"
									class="btn_dark_blue_2">Scrap</a> -->
								 <!--  <a href="sprintreportxls.htm" 
									class="btn_dark_blue_2">Download Sprint</a>
									 <a href="#"  onclick="renewBrand()"
									class="btn_dark_blue_2">Renew Brand</a> -->
							<div id="container" style=" display: none"></div>
										<!--  <a href="runAccuracyReportdata.htm" 
									class="btn_dark_blue_2">Accuracy</a>
									 <a href="runFailedScrapingdata.htm" 
									class="btn_dark_blue_2">Scrap Failed</a>
									  <a href="runAccuracyReportdata.htm" 
									class="btn_dark_blue_2">Accuracy</a>
									 <a href="updateCheckreport.htm" 
									class="btn_dark_blue_2">Citation</a>renewListings.htm -->
								<%
				
									}
								%>
							</div>
						</div>

					</div>
				</div>
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
			<div class="pp-subheader">
				Make sure you are using our <a href="#">bulk upload template</a> for
				your data
			</div>
			<div class="pp-body">
				<div class="upload">
					<a href="#" class="btn_grey_2">Choose file</a> <span>No file
						selected</span> <a href="#" class="btn_dark_blue_2">Upload</a>
				</div>
			</div>
		</div>


		<div class="popup" style="display: none">
			<div class="pp-header">
				<div class="close"></div>
				Upload File
			</div>
			<div class="pp-body">
				<div class="result success">Upload complete, results can be
					seen in Business Listings.</div>
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
			<div class="pp-subheader">
				Make sure you are using our <a href="#">bulk upload template</a> for
				your data
			</div>
			<div class="pp-body">
				<div class="result error">Upload incomplete. Error code:
					12345678</div>
				<div class="upload">
					<a href="#" class="btn_grey_2">Choose file</a> <span>No file
						selected</span> <a href="#" class="btn_dark_blue_2">Upload</a>
				</div>
			</div>
		</div>
</body>
</html>