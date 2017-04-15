
<%@page import="com.business.common.util.LBLConstants"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
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
<link rel="stylesheet" href="css/bpopUp.css">
<link rel="stylesheet" href="css/sidebar.css">
<script src="js/jquery-1.9.1.min.js"></script>
<script src="js/jquery.idTabs.min.js"></script>
<script src="js/jquery.slimscroll.min.js"></script>
<script src="js/jquery.screwdefaultbuttonsV2.js"></script>
<script src="js/jquery.cycle.all.js"></script>
<script src="js/functions.js"></script>
<script src="js/jquery.bpopup.min.js"></script>
<link rel="stylesheet" href="js/jquery.mCustomScrollbar.css">
<script src="js/jquery.mCustomScrollbar.js"></script>

<script type="text/javascript">
	function savesubmission() {

		var form = document.getElementById('customsubmissionform');
		form.action = "customsubmission.htm";
		form.submit();
	}

	$(document).ready(function() {
		var message = $("#editsubmission").val();
		//alert(message);
		if (message != null) {
			$('#editpopup').bPopup();
			$("ul li").removeClass("selected");
		}

		$('#everydaydrop').hide();
		$('#weeklydrop').hide();
		$('#monthlydrop').hide();
		$('#everydaydrop2').hide();
		$('#weeklydrop2').hide();
		$('#monthlydrop2').hide();
		$('#everydaydrop3').hide();
		$('#weeklydrop3').hide();
		$('#monthlydrop3').hide();
		$('#everydaydrop4').hide();
		$('#weeklydrop4').hide();
		$('#monthlydrop4').hide();
	});

	function onclik() {

		var acfrequency = $('#acxiomfreq').val();
		if (acfrequency == 'Daily') {
			$('#everydaydrop').show();
			$('#weeklydrop').hide();
			$('#monthlydrop').hide();
		} else if (acfrequency == 'Weekly') {
			$('#everydaydrop').hide();
			$('#weeklydrop').show();
			$('#monthlydrop').hide();
		} else {
			$('#everydaydrop').hide();
			$('#weeklydrop').hide();
			$('#monthlydrop').show();
		}
	}

	function secondclick() {

		var fafrequency = $('#factualfreq').val();
		if (fafrequency == 'Daily') {
			$('#everydaydrop2').show();
			$('#weeklydrop2').hide();
			$('#monthlydrop2').hide();
		} else if (fafrequency == 'Weekly') {
			$('#everydaydrop2').hide();
			$('#weeklydrop2').show();
			$('#monthlydrop2').hide();
		} else {
			$('#everydaydrop2').hide();
			$('#weeklydrop2').hide();
			$('#monthlydrop2').show();
		}

	}

	function thirdclick() {

		var infrequency = $('#infogroupfreq').val();
		if (infrequency == 'Daily') {
			$('#everydaydrop3').show();
			$('#weeklydrop3').hide();
			$('#monthlydrop3').hide();
		} else if (infrequency == 'Weekly') {
			$('#everydaydrop3').hide();
			$('#weeklydrop3').show();
			$('#monthlydrop3').hide();
		} else {
			$('#everydaydrop3').hide();
			$('#weeklydrop3').hide();
			$('#monthlydrop3').show();
		}

	}

	function fourthclick() {

		var lofrequency = $('#localezefreq').val();
		if (lofrequency == 'Daily') {
			$('#everydaydrop4').show();
			$('#weeklydrop4').hide();
			$('#monthlydrop4').hide();
		} else if (lofrequency == 'Weekly') {
			$('#everydaydrop4').hide();
			$('#weeklydrop4').show();
			$('#monthlydrop4').hide();
		} else {
			$('#everydaydrop4').hide();
			$('#weeklydrop4').hide();
			$('#monthlydrop4').show();
		}
	}

function searchBrandName() {
		
		$("#searchBrandFilterForm").attr('action',
		'brandChannelSearch.htm');
	}
	
	function editBrand() {

		var checkBoxCount = 0;
		$('input[type=checkbox].checkbox1').each(function() {
			if ($(this).is(":checked")) {
				//alert("coming");
				++checkBoxCount;
			}
		});
		//alert("coming");

		if (checkBoxCount == 0) {
			$('#editBrandPopup').bPopup();
			$("ul li").removeClass("selected");
			//alert("please select at least one record");		
			return false;
		} else {
			var form = document.getElementById('customform');
			form.action = "editSubmissionInformation";
			form.submit();
		}

	}
</script>
</head>

<body id="office">
	<!-- page wrapper -->
	<div class="wrapper">
		<!-- header -->
		<div class="header">
			<a href="dash-board.htm" class="logo_lbl">Local Business Listings</a>
			<ul class="top_nav">
				<li class="home"><a href="dash-board.htm">Home</a></li>
				<li class="help"><a href="help.htm">Help</a></li>
				<li class="signout"><a href="logout.htm">Signout</a></li>
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
				<li class="si_business_listings "><a
					href="business-listings.htm">Business Listings</a></li>
				<li class="si_error_listings"><a href="listing-error.htm">Listing
						Errors</a></li>
				<li class="si_upload_export"><a href="upload-export.htm">Upload/Export</a></li>
				<li class="si_reports"><a href="reports.htm">Reports</a></li>
				<%
					Integer Role = (Integer) session.getAttribute("roleId");
					if (Role == LBLConstants.CONVERGENT_MOBILE_ADMIN) {
				%>
				<li class="si_admin selected"><a href="admin-listings.htm">CM
						admin</a></li>
				<%
					}
				%>
		<!-- 		<li class="si_schduler"><a href="scheduler.htm">Schedule</a></li> 
				<li class="si_mobile_profile "><a href="manage-account.htm">Manage
						Account</a></li>

				<li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent
						Toolbox</a></li>-->
			</ul>
			<!-- // left side navigation -->
			<!-- content area -->
			<div class="content" id="id_content">
				<div class="nav_pointer pos_01"></div>
				<!-- subheader -->
				<div class="subheader clearfix">
					<h1>CM ADMIN</h1>
					<a href="admin-listings.htm" class="back">< Back to CM Admin</a>
					<p>Convergent Mobile</p>
				</div>
				<!-- // subheader -->
				<div class="inner_box">
					<!-- box -->
					<div class="box box_red_title custom_submissions">
						<!-- title -->
						<div class="box_title">
							<h2>Custom Submissions</h2>
							<div class="box_title_right">
							
							<spring:form class="box_title_form" method="post" commandName="BrandInfo" id="searchBrandFilterForm">
									<spring:select path="channelName" id="channelId">
								<option value="">Select Channel</option>
								<core:forEach var="channellist" items="${channels}">
								
											
								<option value="${channellist}">${channellist}</option>
									
									
								</core:forEach>
								</spring:select>

								<spring:select path="brandName" >
								<option value="">Select Brands</option>
								<option value="All Brands">All Brands</option>
								<core:forEach items="${clientNameInfo}" var="client">
									<option value="${client.brandName}">${client.brandName}</option>
								</core:forEach>
								</spring:select>
								<button class="btr_button" onclick="searchBrandName()">Filter</button>
								<a href="custom-submissions.htm" class="lnk">Clear Filter</a>
							</spring:form>
						</div>
							
						</div>
						<!-- // title -->
						<spring:form method="post" id="customform">
							<table width="100%" class="grid grid_13">
								<colgroup>
									<col width="4%" />
									<col width="14%" />
									<col width="14%" />
									<col width="17%" />
									<col width="17%" />
									<col width="17%" />
									<col width="17%" />
								</colgroup>
								<thead>
									<tr>
										<th class="th_01"><div>
												<input type="checkbox">
											</div></th>
										<th class="th_03"><div>Channel Name</div></th>
										<th class="th_03"><div>Brand Name</div></th>
										<th class="th_03"><div>Acxiom</div></th>
										<th class="th_03"><div>Factual</div></th>
										<th class="th_03"><div>Infogroup</div></th>
										<th class="th_03"><div>Localeze</div></th>
									</tr>
								</thead>
							</table>
							<div id="id_custom_submissions">
								<table width="100%" class="grid grid_13">
									<colgroup>
										<col width="4%" />
										<col width="14%" />
										<col width="14%" />
										<col width="17%" />
										<col width="17%" />
										<col width="17%" />
										<col width="17%" />
									</colgroup>
									<tbody>
										<core:forEach items="${submissionsBean}" var="submissions">
											<tr class="odd">
												<core:set value="${submissions.submissionId}"
													var="submissionid"></core:set>
												<td class="td_01"><div>
														<input type="checkbox" value="${submissions.submissionId}"
															name="submissionId" class="checkbox1">
													</div></td>
												<td class="td_03"><div>
														<core:out value="${submissions.brandName}"></core:out>
													</div></td>
												<td class="td_03"><div>
														<core:out value="${submissions.channelName}"></core:out>
													</div></td>
												<td class="td_03"><div>
														<core:out value="${submissions.acxiomFrequency}"></core:out>
														-
														<core:out value="${submissions.acxiomTiming}"></core:out>
													</div></td>
												<td class="td_03"><div>
														<core:out value="${submissions.factualFrequency}"></core:out>
														-
														<core:out value="${submissions.factualTiming}"></core:out>
													</div></td>
												<td class="td_03"><div>
														<core:out value="${submissions.infogroupFrequency}"></core:out>
														-
														<core:out value="${submissions.infogroupTiming}"></core:out>
													</div></td>
												<td class="td_03"><div>
														<core:out value="${submissions.localezeFrequency}"></core:out>
														-
														<core:out value="${submissions.localezeTiming}"></core:out>
													</div></td>
											</tr>

										</core:forEach>
									</tbody>
								</table>
							</div>
						</spring:form>
					</div>
					<!-- // box -->
					<div class="buttons-center">
						<a href="#" class="btn_dark_blue_2" onclick="editBrand();">Edit</a>
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

		<div class="popup" id="editBrandPopup" style="display: none;">
			<div class="pp-header">
				<!-- <div class="close"></div> -->
				<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
			</div>
			<div class="pp-subheader"></div>
			<div class="pp-body">
				<div class="buttons">Please select at least one record for
					edit.</div>
			</div>
		</div>
<input type="hidden" value="${editsubmission}" id="editsubmission">
		<core:if test="${not empty editsubmission}">

			<input type="hidden" value="${editsubmission}" id="editsubmission">

			<div class="popup-shader" style="display: block; display: none"
				id="shader"></div>
			<div class="popup" style="display: none" id="editpopup">
				<div class="pp-header">
					<div class="buttonIndicator b-close">X</div>
					<span>${brand}, ${channel} </span>
				</div>
				<table width="100%" class="grid grid_20">
					<colgroup>
						<col width="33%" />
						<col width="33%" />
						<col width="33%" />
					</colgroup>
					<thead>
						<tr>
							<th class="th_01"><div>Source</div></th>
							<th class="th_02"><div>Frequency</div></th>
							<th class="th_03"><div>Timing</div></th>
						</tr>
					</thead>
				</table>
				<spring:form method="post" id="customsubmissionform" commandName="">

					<input type="hidden" value="${submissionId}" name="submissionId">

					<table width="100%" class="grid_20">
						<colgroup>
							<col width="33%" />
							<col width="33%" />
							<col width="33%" />
						</colgroup>
						<tbody>
							<tr>
								<td class="td_01">
									<div>Acxiom</div>
								</td>
								<td class="td_02"><div>
										<div class="select">
											<select name="acxiomfreq" id="acxiomfreq"
												onchange="onclik();">
												<option value="Daily">Daily</option>
												<option value="Weekly">Weekly</option>
												<option value="Monthly">Monthly</option>
											</select>
										</div>
									</div></td>
								<td class="td_03"><div>
										<div class="select" id="everydaydrop">
											<select name="acxiomEtime" id="acxiomtime">
											<option value=""></option>
												<option value="Everyday">Everyday</option>
											</select>
										</div>

										<div class="select" id="weeklydrop">
											<select name="acxiomWtime" id="acxiomtime">
											<option value=""></option>
												<option value="monday">Monday</option>
												<option value="Tuesday">Tuesday</option>
												<option value="Wednesday">Wednesday</option>
												<option value="Thursday">Thursday</option>
												<option value="Friday">Friday</option>
												<option value="Saturday">Saturday</option>
												<option value="Sunday">Sunday</option>
											</select>
										</div>


										<div class="select" id="monthlydrop">
											<select name="acxiomMtime" id="acxiomtime">
												<option value=""></option>
												<option value="1">1</option>
												<option value="2">2</option>
												<option value="3">3</option>
												<option value="4">4</option>
												<option value="5">5</option>
												<option value="6">6</option>
												<option value="7">7</option>
												<option value="8">8</option>
												<option value="9">9</option>
												<option value="10">10</option>
												<option value="11">11</option>
												<option value="12">12</option>
												<option value="13">13</option>
												<option value="14">14</option>
												<option value="15">15</option>
												<option value="16">16</option>
												<option value="17">17</option>
												<option value="18">18</option>
												<option value="19">19</option>
												<option value="20">20</option>
												<option value="21">21</option>
												<option value="22">22</option>
												<option value="23">23</option>
												<option value="24">24</option>
												<option value="25">25</option>
												<option value="26">26</option>
												<option value="27">27</option>
												<option value="28">28</option>
												<option value="29">29</option>
												<option value="30">30</option>
											</select>
										</div>

									</div></td>
							</tr>
							<tr>
								<td class="td_01">
									<div>Factual</div>
								</td>
								<td class="td_02"><div>
										<div class="select">
											<select name="factualfreq" id="factualfreq"
												onchange="secondclick();">
												<option value="Daily">Daily</option>
												<option value="Weekly">Weekly</option>
												<option value="Monthly">Monthly</option>
											</select>

										</div>
									</div></td>
								<td class="td_03"><div>
										<div class="select" id="everydaydrop2">
											<select name="factualEtime" id="factualtime">
												<option value=""></option>
												<option value="Everyday">Everyday</option>
											</select>
										</div>

										<div class="select" id="weeklydrop2">

											<select name="factualWtime" id="factualtime">
												<option value=""></option>
												<option value="monday">Monday</option>
												<option value="Tuesday">Tuesday</option>
												<option value="Wednesday">Wednesday</option>
												<option value="Thursday">Thursday</option>
												<option value="Friday">Friday</option>
												<option value="Saturday">Saturday</option>
												<option value="Sunday">Sunday</option>
											</select>
										</div>

										<div class="select" id="monthlydrop2">
											<select name="factualMtime" id="factualtime">
												<option value=""></option>
												<option value="1">1</option>
												<option value="2">2</option>
												<option value="3">3</option>
												<option value="4">4</option>
												<option value="5">5</option>
												<option value="6">6</option>
												<option value="7">7</option>
												<option value="8">8</option>
												<option value="9">9</option>
												<option value="10">10</option>
												<option value="11">11</option>
												<option value="12">12</option>
												<option value="13">13</option>
												<option value="14">14</option>
												<option value="15">15</option>
												<option value="16">16</option>
												<option value="17">17</option>
												<option value="18">18</option>
												<option value="19">19</option>
												<option value="20">20</option>
												<option value="21">21</option>
												<option value="22">22</option>
												<option value="23">23</option>
												<option value="24">24</option>
												<option value="25">25</option>
												<option value="26">26</option>
												<option value="27">27</option>
												<option value="28">28</option>
												<option value="29">29</option>
												<option value="30">30</option>
											</select>
										</div>
									</div></td>
							</tr>
							<tr>
								<td class="td_01">
									<div>Infogroup</div>
								</td>
								<td class="td_02"><div>
										<div class="select">
											<select name="infogroupfreq" id="infogroupfreq"
												onchange="thirdclick();">
												<option value="Daily">Daily</option>
												<option value="Weekly">Weekly</option>
												<option value="Monthly">Monthly</option>
											</select>
										</div>
									</div></td>
								<td class="td_03"><div>
										<div class="select" id="everydaydrop3">
											<select name="infogroupEtime" id="infogrouptime">
												<option value=""></option>
												<option value="Everyday">Everyday</option>
											</select>
										</div>

										<div class="select" id="weeklydrop3">

											<select name="infogroupWtime" id="infogrouptime">
												<option value=""></option>
												<option value="monday">Monday</option>
												<option value="Tuesday">Tuesday</option>
												<option value="Wednesday">Wednesday</option>
												<option value="Thursday">Thursday</option>
												<option value="Friday">Friday</option>
												<option value="Saturday">Saturday</option>
												<option value="Sunday">Sunday</option>
											</select>
										</div>
										<div class="select" id="monthlydrop3">
											<select name="infogroupMtime" id="infogrouptime">
												<option value=""></option>
												<option value="1">1</option>
												<option value="2">2</option>
												<option value="3">3</option>
												<option value="4">4</option>
												<option value="5">5</option>
												<option value="6">6</option>
												<option value="7">7</option>
												<option value="8">8</option>
												<option value="9">9</option>
												<option value="10">10</option>
												<option value="11">11</option>
												<option value="12">12</option>
												<option value="13">13</option>
												<option value="14">14</option>
												<option value="15">15</option>
												<option value="16">16</option>
												<option value="17">17</option>
												<option value="18">18</option>
												<option value="19">19</option>
												<option value="20">20</option>
												<option value="21">21</option>
												<option value="22">22</option>
												<option value="23">23</option>
												<option value="24">24</option>
												<option value="25">25</option>
												<option value="26">26</option>
												<option value="27">27</option>
												<option value="28">28</option>
												<option value="29">29</option>
												<option value="30">30</option>
											</select>
										</div>

									</div></td>
							</tr>
							<tr>
								<td class="td_01">
									<div>Localeze</div>
								</td>
								<td class="td_02"><div>
										<div class="select">
											<select name="localezefreq" id="localezefreq"
												onchange="fourthclick();">
												<option value="Daily">Daily</option>
												<option value="Weekly">Weekly</option>
												<option value="Monthly">Monthly</option>
											</select>
										</div>
									</div></td>
								<td class="td_03"><div>
										<div class="select" id="everydaydrop4">
											<select name="localezeEtime" id="localezetime">
												<option value=""></option>
												<option value="Everyday">Everyday</option>
											</select>
										</div>
										<div class="select" id="weeklydrop4">

											<select name="localezeWtime" id="localezetime">
												<option value=""></option>
												<option value="monday">monday</option>
												<option value="Tuesday">Tuesday</option>
												<option value="Wednesday">Wednesday</option>
												<option value="Thursday">Thursday</option>
												<option value="Friday">Friday</option>
												<option value="Saturday">Saturday</option>
												<option value="Sunday">Sunday</option>
											</select>
										</div>
										<div class="select" id="monthlydrop4">
											<select name="localezeMtime" id="localezetime">
												<option value=""></option>
												<option value="1">1</option>
												<option value="2">2</option>
												<option value="3">3</option>
												<option value="4">4</option>
												<option value="5">5</option>
												<option value="6">6</option>
												<option value="7">7</option>
												<option value="8">8</option>
												<option value="9">9</option>
												<option value="10">10</option>
												<option value="11">11</option>
												<option value="12">12</option>
												<option value="13">13</option>
												<option value="14">14</option>
												<option value="15">15</option>
												<option value="16">16</option>
												<option value="17">17</option>
												<option value="18">18</option>
												<option value="19">19</option>
												<option value="20">20</option>
												<option value="21">21</option>
												<option value="22">22</option>
												<option value="23">23</option>
												<option value="24">24</option>
												<option value="25">25</option>
												<option value="26">26</option>
												<option value="27">27</option>
												<option value="28">28</option>
												<option value="29">29</option>
												<option value="30">30</option>
											</select>
										</div>
									</div></td>
							</tr>
						</tbody>
					</table>
					<div class="buttons buttons-2">
						<a href="#" onclick="savesubmission();" class="btn_dark_blue_2">Save</a><a
							href="#" class="btn_dark_blue_2">Cancel</a>
					</div>
				</spring:form>

			</div>
		</core:if>
	</div>
</body>
</html>