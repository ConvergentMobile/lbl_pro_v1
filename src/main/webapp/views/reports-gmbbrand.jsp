<!DOCTYPE html>
<%@page import="java.util.Calendar"%>
<%@page import="com.business.common.util.LBLConstants"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0">
<title>Local Business Listings | Reports</title>
<link rel="stylesheet" href="css/global.css">
<link rel="stylesheet" href="fonts/fonts.css">
<link rel="stylesheet" href="css/navigation.css">
<link rel="stylesheet" href="css/common.css">
<link rel="stylesheet" href="css/common-gmb.css">
<link rel="stylesheet" href="css/grids.css">
<link rel="stylesheet" href="css/sidebar.css">
<link rel="stylesheet" href="css/bpopUp.css">
<link rel="stylesheet" href="css/jquery-ui.css">
<link rel="stylesheet" href="css/style.css">

<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>

<script
	src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
<script src="js/jquery.idTabs.min.js"></script>
<script src="js/jquery.slimscroll.min.js"></script>
<script src="js/jquery.screwdefaultbuttonsV2.js"></script>
<script src="js/jquery.cycle.all.js"></script>
<script src="js/functions.js"></script>
<script src="js/jquery.bpopup.min.js"></script>
<link rel="stylesheet" href="js/jquery.mCustomScrollbar.css">
<script src="js/jquery.mCustomScrollbar.js"></script>
<link rel="stylesheet" href="js/select2.css">
<script src="js/select2.js"></script>

<script type="text/javascript" src="js/canvasjs.min.js"></script>
<script src="js/select2.js"></script>
<script src="js/select2.min.js"></script>
<script>
	jQuery(document).ready(function() {
		jQuery(".selectjs").select2();
	});
</script>
<script type="text/javascript">

	function runExcelReport(){
		var startDate = $('#sd').val();
		var enddate = $("#ed").val();
		var brandname = $('#brandVal').val();
		var state = $("#statevalue").val();
		if (startDate == '' || enddate == '') {
			$("#datewarningpopup").bPopup();
		} else {
			document.location.href = "runExcelReport.htm?state=" + state
					+ "&&brand=" + brandname + "&&brand=" + brandname
					+ "&&start=" + startDate + "&&end=" + enddate;
		}
	}


	$(function() {
		$("#startDate").datepicker({
		 	maxDate: new Date(),
			'format' : 'm/d/yyyy',
			'autoclose' : true
		});

	});

	$(function() {
		$("#endDate").datepicker({
			maxDate: new Date(),
			'format' : 'm/d/yyyy',
			'autoclose' : true
		});

	});

	function filterReport() {
		var startDate = $('#startDate').val();
		var enddate = $("#endDate").val();
		var brandname = $('#brandVal').val();
		var state = $("#statevalue").val();

		if (startDate == '' || enddate == '') {
			$("#datewarningpopup").bPopup();
		} else {
			document.location.href = "runGMBBrand.htm?state=" + state
					+ "&&brand=" + brandname + "&&brand=" + brandname
					+ "&&start=" + startDate + "&&end=" + enddate;

		}

	}
</script>

<style type="text/css">
.canvasjs-chart-credit {
	display: none;
}

.creditText {
	display: none;
}

.dsp_None {
	display: none;
}

.loading {
	position: fixed;
	height: 100%;
	width: 100%;
	top: 0;
	left: 0;
	background: rgba(0, 0, 0, 0.6);
	z-index: 9999;
	font-size: 20px;
	text-align: center;
	padding-top: 200px;
	color: #fff;
}
</style>
</head>
<body id="office" class="mw980">
	<!-- page wrapper -->

	<div class="popup" id="datewarningpopup" style="display: none;">
		<div class="pp-header">
			<!-- <div class="close"></div> -->
			<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
		</div>
		<div class="pp-subheader"></div>
		<div class="pp-body">
			<div class="buttons">Please select Start and End Date</div>
		</div>
	</div>
	<div class="popup" id="warningpopup" style="display: none;">
		<div class="pp-header">
			<!-- <div class="close"></div> -->
			<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
		</div>
		<div class="pp-subheader"></div>
		<div class="pp-body">
			<div class="buttons">Please select Brand</div>
		</div>
	</div>

	<div class="popup" id="gmbpopup" style="display: none;">
		<div class="pp-header">
			<!-- <div class="close"></div> -->
			<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
		</div>
		<div class="pp-subheader"></div>
		<div class="pp-body">
			<div class="buttons">Google Insights are not available for your locations. Contact Support if you wish to have Google added to your program. </div>
		</div>
	</div>
</head>
<body id="office" class="mw980">

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
				<li class="si_reports selected"><a href="reports.htm">Reports</a></li>
				<li class="si_admin"><a href="admin-listings.htm">CM admin</a></li>
				<li class="si_mobile_profile "><a href="manage-account.htm">Manage
						Account</a></li>
			</ul>
			<!-- // left side navigation -->
			<!-- content area -->
			<div class="content" id="id_content" style="min-height: 804px;">
				<div class="nav_pointer pos_01"></div>
				<!-- subheader -->
				<div class="subheader clearfix">
					<h1>REPORTS</h1>
					<a href="reports.htm" class="back">< Back to Reporting Criteria</a>
					<span>Export Report</span> <a href="#" class="ico_xls" onclick="runExcelReport()">Excel</a>
					<p>Convergent Mobile</p>
					
				<a class="ico_pdf" href="http://api.html2pdfrocket.com/pdf?value=http://${url}&apikey=109e93d8-268f-495a-ad47-45fcaac321c3&FileName=${brand}&JavascriptDelay=3000&HeaderUrl=http://23.23.203.174/lbl_pro/pdfheader.htm">PDF</a> 

				</div>
				<!-- // subheader -->
				<input name="uploadreportpopup" value="" id="uploadreportpopup"
					type="hidden">
				<form id="reportsForm" action="/lbl_pro/reports.htm" method="post">
					<div class="inner_box">
						<!-- box -->
						<!-- title -->
						<div class="box box_red_title box_report">
							<div class="box_title mb9">
								<h2>Google My Business Insights – Brand</h2>
							</div>
							<!-- // title -->
							
				

							<!-- subtitle -->
							<div class="box_subtitle_lightgray">

								<core:choose>
									<core:when test="${state eq null }">
										<p>
											<strong>${brand}</strong>, ALL Locations <span
												class="divider"></span> ${start} - ${end}
										</p>
									</core:when>
									<core:when test="${state eq ''}">
										<p>
											<strong>${brand}</strong>, ALL Locations <span
												class="divider"></span> ${start} - ${end}
										</p>
									</core:when>
									<core:otherwise>
										<p>
											<strong>${brand}</strong>, ${state} <span class="divider"></span>
											${start} - ${end}
										</p>
									</core:otherwise>
								</core:choose>

								<input type="hidden" id="storeVal" value="${store}"> <input
									type="hidden" id="brandVal" value="${brand}"> <input
									type="hidden" id="directVal" value="${direct}"> <input
									type="hidden" id="discoveryVal" value="${discovery}">
									<input
									type="hidden" id="sd" value="${sd}">
									<input
									type="hidden" id="ed" value="${ed}">
							</div>
							<div class="box_subtitle_darkgray">
								<div class="filter-listings">
									from <input type="text" id="startDate" class="input_date_01">
									to <input type="text" id="endDate" class="input_date_01">
									<select name="state" id="statevalue">
										<option value="all">All Locations</option>
										<core:forEach items="${statesList}" var="states">
											<option value="${states}">${states}</option>
										</core:forEach>
									</select> <a href="#" onclick="filterReport()" class="btr_button">FIlter
										Listings</a>
								</div>
							</div>
							<!-- // subtitle -->
							<!-- wide_column -->
							<div class="wide_column_wrapper report_container"
								style="height: auto;">
								<div class="description" id="id_report_01">

									<div class="box-report-graph">
										<h2>How customers search for YOUR LOCATIONS</h2>
										<div class="left-col" id="searchContainer">
											<!-- place for graphic -->
										</div>
										<div class="right-col">
											<core:set var="val" value="46563746375" />
											<p class="red-title">
												Direct: <strong><fmt:formatNumber type="number"
														maxFractionDigits="3" value="${direct}" /></strong>
											</p>
											<p>Customers who find your listings searching for your
												business name or address</p>
											<p class="blue-title">
												Discovery: <strong><fmt:formatNumber type="number"
														maxFractionDigits="3" value="${discovery}" /></strong>
											</p>
											<p>Customers who find your listings searching for a
												category, product, or service</p>
										</div>
										<div class="clearfix"></div>
									</div>

									<div style="padding: 0 0 20px; background: #d7dbe0;">
										<div class="table-top10" style="width: 100%;">
											<h2>Top 10 Locations</h2>
											<table class="grid grid_17" width="100%">
												<colgroup>
													<col width="17%">
													<col width="30%">
													<col width="8%">
													<col width="15%">
													<col width="15%">
													<col width="15%">
												</colgroup>
												<thead>
													<tr>
														<th class="th_01"><div>Store #</div></th>
														<th class="th_02"><div>City</div></th>
														<th class="th_02"><div>State</div></th>
														<th class="th_02"><div>Direct</div></th>
														<th class="th_02"><div>Discovery</div></th>
														<th class="th_05"><div>Total</div></th>
													</tr>
												</thead>
												<tbody>
													<core:forEach items="${topTenSearches}" var="ttSearch"
														varStatus="loopStatus">

														<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
															<td class="td_01"><div>${ttSearch.store}</div></td>
															<td class="td_02"><div>${ttSearch.city}</div></td>
															<td class="td_03"><div>${ttSearch.state}</div></td>
															<td class="td_04"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${ttSearch.topDirectCount}"/></div></td>
															<td class="td_05"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${ttSearch.topDiscoveryCount}"/></div></td>
															<td class="td_06"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${ttSearch.totalTopSearchCount}"/></div></td>
														</tr>
													</core:forEach>
												</tbody>
											</table>
										</div>
										<div class="table-bot10" style="width: 100%;">
											<h2>Bottom 10 Locations</h2>
											<table class="grid grid_17" width="100%">
												<colgroup>
													<col width="17%">
													<col width="30%">
													<col width="8%">
													<col width="15%">
													<col width="15%">
													<col width="15%">
												</colgroup>
												<thead>
													<tr>
														<th class="th_01"><div>Store #</div></th>
														<th class="th_02"><div>City</div></th>
														<th class="th_02"><div>State</div></th>
														<th class="th_02"><div>Direct</div></th>
														<th class="th_02"><div>Discovery</div></th>
														<th class="th_05"><div>Total</div></th>
													</tr>
												</thead>
												<tbody>
													<core:forEach items="${bottomTenSearches}" var="btSearch"
														varStatus="loopStatus">

														<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
															<td class="td_01"><div>${btSearch.store}</div></td>
															<td class="td_02"><div>${btSearch.city}</div></td>
															<td class="td_03"><div>${btSearch.state}</div></td>
															<td class="td_04"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${btSearch.bottomDirectCount}"/></div></td>
															<td class="td_05"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${btSearch.bottomDiscoveryCount}"/></div></td>
															<td class="td_06"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${btSearch.totalBottomSearchCount}"/></div></td>
														</tr>
													</core:forEach>
												</tbody>
											</table>
										</div>
										<div class="clearfix"></div>
									</div>
									<div class="box-report-graph">
										<h2>Where customers view your locations on Google</h2>
										<h3>The Google services that customers use to find your
											locations</h3>
										<div class="left-col" id="viewContainer">
											<!-- place for graphic -->
										</div>
										<div class="right-col">
											<table class="grid grid_25">
												<tr>
													<td>Listing on Search</td>
													<td><strong><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${search}" /></strong></td>
												</tr>
												<tr>
													<td>Listing on Maps</td>
													<td><strong><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${maps}" /></strong></td>
												</tr>
												<tr>
													<td><strong>Total Views</strong></td>
													<td><strong><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${views}" /></strong></td>
												</tr>
											</table>

										</div>
										<div class="clearfix"></div>
									</div>
									<div style="padding: 0 0 20px; background: #d7dbe0;">
										<div class="table-top10" style="width: 100%;">
											<h2>Top 10 Locations</h2>
											<table class="grid grid_17" width="100%">
												<colgroup>
													<col width="17%">
													<col width="30%">
													<col width="8%">
													<col width="15%">
													<col width="15%">
													<col width="15%">
												</colgroup>
												<thead>
													<tr>
														<th class="th_01"><div>Store #</div></th>
														<th class="th_02"><div>City</div></th>
														<th class="th_02"><div>State</div></th>
														<th class="th_02"><div>Search</div></th>
														<th class="th_02"><div>Maps</div></th>
														<th class="th_05"><div>Total</div></th>
													</tr>
												</thead>
												<tbody>

													<core:forEach items="${topTenViews}" var="ttView"
														varStatus="loopStatus">

														<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
															<td class="td_01"><div>${ttView.store}</div></td>
															<td class="td_02"><div>${ttView.city}</div></td>
															<td class="td_03"><div>${ttView.state}</div></td>
															<td class="td_04"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${ttView.topViewSearchCount}"/></div></td>
															<td class="td_05"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${ttView.topViewMapsCount}"/></div></td>
															<td class="td_06"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${ttView.totalTopViewsCount}"/></div></td>
														</tr>
													</core:forEach>
												</tbody>
											</table>
										</div>
										<div class="table-bot10" style="width: 100%;">
											<h2>Bottom 10 Locations</h2>
											<table class="grid grid_17" width="100%">
												<colgroup>
													<col width="17%">
													<col width="30%">
													<col width="8%">
													<col width="15%">
													<col width="15%">
													<col width="15%">
												</colgroup>
												<thead>
													<tr>
														<th class="th_01"><div>Store #</div></th>
														<th class="th_02"><div>City</div></th>
														<th class="th_02"><div>State</div></th>
														<th class="th_02"><div>Search</div></th>
														<th class="th_02"><div>Maps</div></th>
														<th class="th_05"><div>Total</div></th>
													</tr>
												</thead>
												<tbody>
													<core:forEach items="${bottomTenViews}" var="btView"
														varStatus="loopStatus">

														<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
															<td class="td_01"><div>${btView.store}</div></td>
															<td class="td_02"><div>${btView.city}</div></td>
															<td class="td_03"><div>${btView.state}</div></td>
															<td class="td_04"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${btView.bottomViewSearchCount}"/></div></td>
															<td class="td_05"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${btView.bottomViewMapsCount}"/></div></td>
															<td class="td_06"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${btView.totalBottomViewsCount}"/></div></td>
														</tr>
													</core:forEach>
												</tbody>
											</table>
										</div>
										<div class="clearfix"></div>
									</div>

									<div class="box-report-graph">
										<h2>Customer actions</h2>
										<h3>The most common actions that customers take on your
											listings</h3>
										<div class="left-col" id="actionsContainer">
											<!-- place for graphic -->
										</div>
										<div class="right-col">
											<table class="grid grid_25">
												<tr>
													<td>Visit Your Website</td>
													<td><strong><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${website}" /></strong></td>
												</tr>
												<tr>
													<td>Request Directions</td>
													<td><strong><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${directions}" /></strong></td>
												</tr>
												<tr>
													<td>Call You</td>
													<td><strong><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${calls}" /></strong></td>
												</tr>
												<tr>
													<td><strong>Total Actions</strong></td>
													<td><strong><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${actions}" /></strong></td>
												</tr>
											</table>

										</div>
										<div class="clearfix"></div>
									</div>

									<div class="table-top10" style="width: 100%;">
										<h2>Top 10 Locations</h2>
										<table class="grid grid_17" width="100%">
											<colgroup>
												<col width="17%">
												<col width="30%">
												<col width="9%">
												<col width="11%">
												<col width="11%">
												<col width="11%">
												<col width="11%">
											</colgroup>
											<thead>
												<tr>
													<th class="th_01"><div>Store #</div></th>
													<th class="th_02"><div>City</div></th>
													<th class="th_02"><div>State</div></th>
													<th class="th_02"><div>Website</div></th>
													<th class="th_02"><div>Directions</div></th>
													<th class="th_02"><div>Phone</div></th>
													<th class="th_05"><div>Total</div></th>
												</tr>
											</thead>
											<tbody>
												<core:forEach items="${topTenActions}" var="ttAction"
													varStatus="loopStatus">
													<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
														<td class="td_01"><div>${ttAction.store}</div></td>
														<td class="td_02"><div>${ttAction.city}</div></td>
														<td class="td_02"><div>${ttAction.state}</div></td>
														<td class="td_02"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${ttAction.topWebsiteCount}"/></div></td>
														<td class="td_02"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${ttAction.topDirectionsCount}"/></div></td>
														<td class="td_02"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${ttAction.topCallsCount}"/></div></td>
														<td class="td_05"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${ttAction.totalTopActionsCount}"/></div></td>
													</tr>
												</core:forEach>
											</tbody>
										</table>
									</div>
									<div class="table-bot10" style="width: 100%;">
										<h2>Bottom 10 Locations</h2>
										<table class="grid grid_17" width="100%">
											<colgroup>
												<col width="17%">
												<col width="30%">
												<col width="9%">
												<col width="11%">
												<col width="11%">
												<col width="11%">
												<col width="11%">
											</colgroup>
											<thead>
												<tr>
													<th class="th_01"><div>Store #</div></th>
													<th class="th_02"><div>City</div></th>
													<th class="th_02"><div>State</div></th>
													<th class="th_02"><div>Website</div></th>
													<th class="th_02"><div>Directions</div></th>
													<th class="th_02"><div>Phone</div></th>
													<th class="th_05"><div>Total</div></th>
												</tr>
											</thead>
											<tbody>

												<core:forEach items="${bottomTenActions}" var="btAction"
													varStatus="loopStatus">
													<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
														<td class="td_01"><div>${btAction.store}</div></td>
														<td class="td_02"><div>${btAction.city}</div></td>
														<td class="td_02"><div>${btAction.state}</div></td>
														<td class="td_02"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${btAction.bottomWebsiteCount}"/></div></td>
														<td class="td_02"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${btAction.bottomDirectionsCount}"/></div></td>
														<td class="td_02"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${btAction.bottomCallsCount}"/></div></td>
														<td class="td_05"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${btAction.totalBottomActionsCount}"/></div></td>
													</tr>
												</core:forEach>
											</tbody>
										</table>
									</div>
								</div>

								<div class="clearfix"></div>
							</div>
						</div>
						<!-- // wide_column -->
					</div>
					<!-- // box -->
			</div>
			</form>
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
	<div class="popup-shader" style="display: none"></div>
	<script type="text/javascript">
	
		var nd = ${noDataMessage};
		
		
		if(nd){
			$("#gmbpopup").bPopup();
		}
		$(function() {
			window.onload = function() {
				
			   	
			    var minY = ${minYear};
			    var minM = ${minMonth};
			    var minD = ${minDate};

			    var maxY = ${maxYear};
			    var maxM = ${maxMonth};
			    var maxD = ${maxDate};
				var min = new Date(minY, minM,minD);
				var max = new Date(maxY,maxM,maxD);
				
				var days = ${days};
				
				var yAction = ${yActions};
				var yView = ${yViews};
				var ySearch = ${ySearches};
				
				var intType="";
				if(days <= 14) {
					intType = "day";
				} else if(days > 14 && days < 90){
					intType = "week";
				}else {
					intType = "month";
				}

				var dpsView1 = [];
				var dpsView2 = [];

				var list = ${searchHistory};

				$.each(list, function(index, value) {
					//alert( index + ": " + value.toString() );
					dpsView1.push(value);
				});

				var list2 = ${mapsHistory};

				$.each(list2, function(index1, value1) {
					//alert( index + ": " + value.toString() );
					dpsView2.push(value1);
				});

				var dpsActions1 = [];
				var dpsActions2 = [];
				var dpsActions3 = [];

				var list3 = ${websitesHistory};
				$.each(list3, function(index3, value3) {
					//alert( index + ": " + value.toString() );
					dpsActions1.push(value3);
				});

				var list4 = ${directionsHistory}
				;
				$.each(list4, function(index4, value4) {
					//alert( index + ": " + value.toString() );
					dpsActions2.push(value4);
				});
				var list5 =${callsHistory};

				$.each(list5, function(index5, value5) {
					//alert( index + ": " + value.toString() );
					dpsActions3.push(value5);
				});

				var dirVal = $("#directVal").val();
				var disVal = $('#discoveryVal').val();

				var chart = new CanvasJS.Chart("searchContainer", {
					animationEnabled : true,
					axisX : {
						labelFontSize : 18,
					},
					axisY : {
						minimum: 0,
	    	        	maximum: Number(disVal),
						gridColor : "white",
						tickColor : "white",
						labelAngle: 0,
						interval : ySearch,
					},
					data : [ {
						type : "bar",
						dataPoints : [ {
							y : Number(disVal),
							label : "Discovery",
							color : "#314051"
						}, {
							y : Number(dirVal),
							label : "Direct",
							color : "#cc2f12"
						}, ]
					}, ]
				});

				var viewChart = new CanvasJS.Chart("viewContainer", {

					animationEnabled : true,
					axisX : {
						minimum: min,
	    	        	maximum: max,
	    	        	valueFormatString: "MMM/DD",
						interval:1,
						labelAngle: -45,
						labelAutoFit: true,
						intervalType: intType,
					},
					axisY : {
						interval : yView,
						gridColor : "white",
						tickColor : "white",
					},
					data : [ {
						name : "search",
						showInLegend : true,
						legendMarkerType : "square",
						type : "area",
						color : "#d94136",
						markerSize : 0,
						dataPoints : dpsView1
					}, {
						name : "maps",
						showInLegend : true,
						legendMarkerType : "square",
						type : "area",
						color : "#ffbe00",
						markerSize : 0,
						label : "",
						dataPoints : dpsView2
					} ],
					legend : {
						fontSize : 20,
						cursor : "pointer",
						itemclick : function(e) {
							if (typeof (e.dataSeries.visible) === "undefined"
									|| e.dataSeries.visible) {
								e.dataSeries.visible = false;
							} else {
								e.dataSeries.visible = true;
							}
						}
					}
				});

				var actionsChart = new CanvasJS.Chart("actionsContainer", {
					animationEnabled : true,
					axisX : {
						minimum: min,
	    	        	maximum: max,
	    	        	valueFormatString: "MMM/DD",
						interval:1,
						labelAngle: -45,
						 labelAutoFit: true,
						intervalType: intType,
					},
					axisY : {
						interval : yAction,
						gridColor : "white",
						tickColor : "white",
					},
					data : [ {
						name : "website",
						showInLegend : true,
						legendMarkerType : "square",
						type : "area",
						color : "#278D14",
						markerSize : 0,
						dataPoints : dpsActions1
					}, {
						name : "directions",
						showInLegend : true,
						legendMarkerType : "square",
						type : "area",
						color : "#D2F38E",
						markerSize : 0,
						label : "",
						dataPoints : dpsActions2
					}, {
						name : "calls",
						showInLegend : true,
						legendMarkerType : "square",
						type : "area",
						color : "#5DC2F9",
						markerSize : 0,
						label : "",
						dataPoints : dpsActions3
					} ],
					legend : {
						fontSize : 20,
						cursor : "pointer",
						itemclick : function(e) {
							if (typeof (e.dataSeries.visible) === "undefined"
									|| e.dataSeries.visible) {
								e.dataSeries.visible = false;
							} else {
								e.dataSeries.visible = true;
							}
						}
					}
				});

				chart.render();
				viewChart.render();
				actionsChart.render();
			}
		},3000);
	</script>
</body>
</html>