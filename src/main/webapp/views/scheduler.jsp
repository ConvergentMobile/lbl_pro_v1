
<%@page import="com.business.common.util.LBLConstants"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
<link rel="stylesheet" href="css/jquery-ui.css">
<link rel="stylesheet" href="js/jquery.mCustomScrollbar.css">
<script src="js/jquery.mCustomScrollbar.js"></script>
<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">

<script
	src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
<script src="js/jquery.idTabs.min.js"></script>
<script src="js/jquery.slimscroll.min.js"></script>
<script src="js/jquery.screwdefaultbuttonsV2.js"></script>
<script src="js/jquery.cycle.all.js"></script>
<script src="js/functions.js"></script>
<script src="js/jquery.bpopup.min.js"></script>
<script src="js/jquery.session.js"></script>

<script type="text/javascript">
	$(function() {
		$('#schedulerForm').submit(
				function() {

					if ($("#clientName").val() === ""
							|| $("#partnerName").val() === ""
							&& $("#date").val() === ""
							|| $("#hours").val() === ""
							|| $(".input_date_01").val() === "") {

						$("#schedule").bPopup();

						return false;
					}

				});

	});
	$(function() {
		$("#ScheduleTime").datepicker({
			'format' : 'm/d/yyyy',
			'autoclose' : true
		});

	});

	function showDeletePopup() {

		var form = "";
		var count = 0;
		$('input[type=checkbox].checkbox1').each(function() {
			if ($(this).is(":checked")) {
				++count;
				form = $(".checkbox1").val();
			}
		});

		if (form != "" && count >= 1) {
			$('#displayDeleteConfirmationpopup').bPopup();
			$("ul li").removeClass("selected");

			return true;
		} else {
			$('#Deleterecordselectionpopup').bPopup();
			$("ul li").removeClass("selected");
			//alert("please select at least one record");		
			return false;
		}

	}
	function sendDeleteResult() {

		var form = document.getElementById('schedulerForm');
		form.action = "deleteBusinessInfotest";
		form.submit();
	}
</script>

</head>
<div class="popup" id="Deleterecordselectionpopup"
	style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select record for delete.</div>
	</div>
</div>

<div class="popup" id="displayDeleteConfirmationpopup"
	style="display: none">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> Delete
		Location
	</div>
	<div class="pp-subheader">
		You have selected to Delete the Schedule(s).<br> <span
			class="red">This will permanently remove the Schedule(s) and
			cannot be undone.</span>
	</div>
	<div class="pp-body">
		<div class="buttons">
			<button onclick="sendDeleteResult();" class="btn_red_2">Continue</button>
			<button class="btn_dark_blue_2 b-close">Cancel</button>
		</div>
	</div>
</div>
<div class="popup" id="schedule" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please Select All Details !!</div>
	</div>
</div>


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
		<div class="content_wrapper">
			<!-- left side navigation -->
			<ul class="ul_left_nav">
				<li class="si_dashboard "><a href="dash-board.htm">Dashboard</a></li>
				<li class="si_business_listings"><a
					href="business-listings.htm">Business Listings</a></li>
				<li class="si_error_listings"><a href="listing-error.htm">Listing
						Errors</a></li>
				<li class="si_upload_export"><a href="upload-export.htm">Upload/Export</a></li>
				<li class="si_reports"><a href="reports.htm">Reports</a></li>
				<%
					Integer Role = (Integer) session.getAttribute("roleId");
					if (Role == LBLConstants.CONVERGENT_MOBILE_ADMIN) {
				%>
				<li class="si_admin"><a href="admin-listings.htm">CM admin</a></li>
				<%
					}
				%>
			<!-- 	<li class="si_schduler selected"><a href="scheduler.htm">Schedule</a></li> 
				<li class="si_mobile_profile "><a href="manage-account.htm">Manage
						Account</a></li>

				<li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent
						Toolbox</a></li>-->
			</ul>
			<!-- // left side navigation -->
			<!-- content area -->
			<spring:form method="post" action="schdulersave.htm"
				commandName="scheduler" id="schedulerForm">
				<div class="content" id="id_content">
					<div class="nav_pointer pos_01"></div>
					<!-- subheader -->
					<div class="subheader clearfix">
						<h1>Add Schedule</h1>
						<%-- <p>${channelName}</p> --%>
					</div>
					<!-- // subheader -->
					<div class="inner_box">
						<!-- box -->
						<div class="box box_red_title brands_overview">
							<!-- title -->
							<div class="box_title">

								<div class="">
									<h2>Add Schedule</h2>

								</div>
							</div>
							<!-- // title -->
							<table id="" width="110%" class="grid grid_10"
								style="border-bottom: 0px #dfe2e3">
								<colgroup>
									<col width="35%" />
									<col width="40%" />
									<col width="35%" />
								</colgroup>
								<thead>

								</thead>
								<tbody>

									<tr>
										<td><div>
												<label for="display_activity_for">Brand</label>
											</div></td>
										<td class=""><div>
												<select name="brandID" id="clientName">
													<option value="">Select Brand</option>
													<core:forEach items="${clientNameInfo}" var="client">
														<option value="${client.brandID}">${client.brandName}</option>
													</core:forEach>
												</select>
											</div></td>



									</tr>
									<tr>
										<td><div>
												<label for="display_activity_for">Partners</label>
											</div></td>
										<td class=""><div>

												<select name="partnerId" id="partnerName">
													<option value="">Select Partner</option>
													<core:forEach items="${partners}" var="partner">
														<option value="${partner.partnerId}">${partner.partnerName}</option>

													</core:forEach>
												</select>
											</div></td>
									</tr>
									<tr>
										<td><div>
												<label for="display_activity_for">Recurring</label>
											</div></td>
										<td class=""><div>

												<select name="recurring">
													<option>Select</option>
													<option value="0">Once</option>
													<option value="7">Weekly</option>
													<option value="30">Monthly</option>

												</select>
											</div></td>
									</tr>
									<tr>
										<td><div>
												<label for="display_activity_for">Hours</label>
											</div></td>
										<td class="td_02"><div>
												<select name="hours">
													<option>Select</option>
													<%
														for (int i = 0; i <= 24; i++) {
													%>
													<option value="<%=i%>:00"><%=i%>:00
													</option>
													<%
														}
													%>

												</select>
											</div></td>



									</tr>
									<tr>
										<td><div>
												<label for="date_start">Date</label>
											</div></td>
										<td><div>
												<spring:input path="ScheduleTime" class="input_date_01"
													style="margin: 0px 0px;" />

											</div></td>
									</tr>

								</tbody>
							</table>
							<div class="buttons buttons-2" style="margin: 0px 170px 20px;">
								<input type="submit" id="savepopup" class="btn_dark_blue_2"
									value="add">



							</div>
							<%-- 	<div id="">
								<div class="f_group">
									<label for="display_activity_for">Brand</label> <select
										name="brandID" id="clientName">
										<option value="">Select Brand</option>
										<core:forEach items="${clientNameInfo}" var="client">
											<option value="${client.brandID}">${client.brandName}</option>
										</core:forEach>
									</select>
								</div>
								<br>
								<div class="f_group" style="margin-center: 200px">
									<label for="display_activity_for">Partners</label> <select
										name="partnerId" id="partnerName">
										<option value="">Select Partner</option>
										<core:forEach items="${partners}" var="partner">
											<option value="${partner.partnerId}">${partner.partnerName}</option>

										</core:forEach>
									</select>
								</div>
								<br>


								<div class="f_group" style="margin-center: 300px">
									<label for="display_activity_for">Recurring</label> <select
										name="recurring">
										<option>Select</option>
										<option value="0">Once</option>
										<option value="7">Weekly</option>
										<option value="30">Monthly</option>

									</select>
								</div>
								<br>
								<!-- </div> -->
								<div class="f_group" style="margin-center: 100px">
									<label for="display_activity_for">Hours</label> <select
										name="hours">
										<option>Select</option>
										<%
											for (int i = 0; i <= 24; i++) {
										%>
										<option value="<%=i%>:00"><%=i%>:00
										</option>
										<%
											}
										%>

									</select>
								</div>
								<br>

								<!-- </div> -->

								<label for="date_start">Date</label>
								<spring:input path="ScheduleTime" class="input_date_01" />
								<br>

								   <label for="date_start">Date</label><spring:input path="ScheduleTime"  id='datepicker'/>  
								<br>
								<input type="submit" class="btn_dark_blue_2" value="Add" style="margin-left: 20px">
							</div> --%>
						</div>
						<!-- // box -->
						<!-- box -->
						<div class="box box_red_title brands_overview"
							style="margin-bottom: 0">
							<!-- title -->
							<div class="box_title">
								<h2>Schedules</h2>
							</div>
							<!-- // title -->
							<div class="filters">
								<div class="f_group"></div>
								<div class="f_group"></div>
							</div>
							<table width="100%" class="grid grid_07 grid_07_1">
								<colgroup>
									<col width="10%" />
									<col width="16%" />
									<col width="18%" />
									<col width="16%" />
									<col width="25%" />
									<col width="30%" />
									<!-- <col width="15%" /> -->

								</colgroup>
								<thead id="">
									<tr>
										<th><div></div></th>
										<th><div>Brand</div></th>
										<th><div>Partner</div></th>
										<th><div>Recurring</div></th>
										<th><div>Scheduled On</div></th>
										<th><div>Next Scheduled</div></th>
										<!-- <th class="th_02" id="th_07"><div>Hours</div></th> -->
									</tr>
									`
								</thead>
							</table>
							<div id="id_listing_activity">

								<table id="displayActivity" width="100%"
									class="grid grid_07 grid_07_1">
									<colgroup>
										<col width="10%" />
										<col width="16%" />
										<col width="18%" />
										<col width="16%" />
										<col width="25%" />
										<col width="30%" />
										<!-- 	<col width="15%" /> -->
									</colgroup>
									<tbody id="activityInfo">

										<core:forEach items="${schduleractivity}" var="activity"
											varStatus="i">
											<tr class="odd">
												<td class=""><div style="margin-left: 20px;">
														<input type="checkbox" class="checkbox1"
															value="${activity.schdulerId}" name="schdulerId">
													</div></td>
												<td class=""><div>
														<core:out value="${activity.brandName}"></core:out>
													</div></td>
												<td class=""><div>
														<core:out value="${activity.partnerName}"></core:out>
													</div></td>
												<td class=""><div>
														<core:if test="${activity.recurring == 0}">
															<core:out value="One Time"></core:out>
														</core:if>
														<core:if test="${activity.recurring == 7}">
															<core:out value="Weekly"></core:out>
														</core:if>
														<core:if test="${activity.recurring == 30}">
															<core:out value="Monthly"></core:out>
														</core:if>
													</div></td>
												<td class=""><div>
														<fmt:formatDate value="${activity.scheduleTime}"
															pattern="MM-dd-yyyy"></fmt:formatDate>
													</div></td>

												<td class=""><div>
														<fmt:formatDate value="${activity.nextScheduleTime}"
															pattern="MM-dd-yyyy"></fmt:formatDate>
													</div></td>
												<%-- <td class="td_07"><div>
														<core:out value="${activity.hours}"></core:out>
													</div></td> --%>
											</tr>
										</core:forEach>
									</tbody>
								</table>

							</div>
						</div>
					</div>
					<div class="buttons-center">
						<a href="#" onclick="showDeletePopup();" class="btn_dark_blue_2"
							style="margin: 15px 20px -15px;">Delete</a>

					</div>
				</div>
				<!-- // content area -->
				<!-- sidebar -->
				<!-- <div class="sidebar" id="id_sidebar">
					<div class="inner">
						<div class="sb_box_wrapper" style="margin-bottom: 14px;">
							title
							<div class="sb_title sb_title_ico ico_sb_search">
								<h2 class="">Quick Search</h2>
							</div>
							// title
							sidebar box
							<div class="sb_box" style="padding: 15px 0px;"></div>
							// sidebar box
						</div>
						<div class="sb_box_wrapper" style="margin-bottom: 5px;">
							title
							<div class="sb_title sb_title_ico ico_sb_user">
								<h2 class="">User Accounts</h2>
							</div>
							// title
							sidebar box
							<div class="sb_box">
								<div class="sb_box" style="padding: 5px 0 0;">


									<div class="sb_box_shadow">
										<div id="id_user_accounts"></div>
									</div>
									<div class="sb_buttons_center" style="padding: 12px 0 12px">
										<input type="submit" onclick="return myfunction();"
											value="Expand" class="btn_dark_blue_2">
									</div>
								</div>
								// sidebar box
							</div>

						</div> -->
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