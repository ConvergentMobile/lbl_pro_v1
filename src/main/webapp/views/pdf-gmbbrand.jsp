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
<link rel="stylesheet" href="css/common-pdf.css">
<link rel="stylesheet" href="css/common-gmb.css">
<link rel="stylesheet" href="css/grids-pdf.css">
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

.clear-background {
	background-image: none;
}

.no-background {
	background: none;
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

.trend-graph {
	padding: 30px;
	clear: both;
	background: #fff;
}

.trend-graph.shadow {
	box-shadow: none;
	margin: 0 0 20px;
}

.trend-graph h2 {
	text-align: center;
	color: #4e5b62;
	font-size: 30px;
	margin: 0 0 20px;
	font: 30px 'Open Sans';
	text-transform: uppercase;
	background-image: none;
	background: none;
}

.box-report-graph h2 {
	text-align: center;
	color: #4e5b62;
	font-size: 30px;
	margin: 0 0 20px;
	font: 30px 'Open Sans';
	text-transform: uppercase;
	background-image: none;
	background: none;
}


.trend-graph .left-col {
	min-height: 350px;
	width: 65%;
	float: left;
	margin: 0 0% 0 0;
}
.box_green_title .box_title{
	background-color: #006400;
	border-top: 1px solid #efbdb4
}


</style>
</head>
<body id="office" class="mw980">

	<!-- page wrapper -->
	<div>
		<!-- header -->
		<div class="header clear-background">
			<img align="left" src="${channelImage}" width="140.2" height="72">
			<img src="images/lbl-logo-pdf.png" width="400.4" height="65">
			<img align="right" src="${brandImage}" width="140.2" height="72">
		</div>
		<!-- // header -->

		<!-- // left side navigation -->
		<!-- content area -->
		<div id="id_content" style="min-height: 804px;">
			<div class="nav_pointer pos_01"></div>

			<!-- // subheader -->

			<div class="inner_box">
				<!-- box -->
				<!-- title -->
				<div class="box box_red_title box_report">
					<div class="box_title mb9">
						<h2>Google My Business Insights – Brand</h2>
					</div>

					<div class="box_subtitle_lightgray">

						<core:choose>
							<core:when test="${state eq null }">
								<p>
									<strong>${brand}</strong>, ALL Locations <span class="divider"></span>
									${start} - ${end}
								</p>
							</core:when>
							<core:when test="${state eq ''}">
								<p>
									<strong>${brand}</strong>, ALL Locations <span class="divider"></span>
									${start} - ${end}
								</p>
							</core:when>
							<core:otherwise>
								<p>

									<strong>${brand}</strong>, ${state} <span class="divider"></span>
									Last 12 Months – YOY
								</p>
							</core:otherwise>
						</core:choose>

						<input type="hidden" id="storeVal" value="${store}"> <input
							type="hidden" id="brandVal" value="${brand}"> <input
							type="hidden" id="directVal" value="${direct}"> <input
							type="hidden" id="discoveryVal" value="${discovery}"> <input
							type="hidden" id="sd" value="${sd}"> <input type="hidden"
							id="ed" value="${ed}">
					</div>
					<!-- // subtitle -->
					<!-- wide_column -->
					<div class="wide_column_wrapper"
						style="height: auto;">
						<div class="trend-graph">
							<h2>Search Trends</h2>
							<div
								style="width: 100%; min-height: 250px; float: left; margin: 0 0% 0 0;"
								id="searchTrendsContainer">
								<!-- place for graphic -->
							</div>

						</div>
						<div class="trend-graph ">
							<h2>View Trends</h2>
							<div
								style="width: 100%; min-height: 250px; float: left; margin: 0 0% 0 0;"
								id="viewTrendsContainer">
								<!-- place for graphic -->
							</div>

						</div>
						<div class="trend-graph">
							<h2>Action Trends</h2>
							<div
								style="width: 100%; min-height: 250px; float: left; margin: 0 0% 0 0;"
								id="actionTrendsContainer">
								<!-- place for graphic -->
							</div>
						</div>
						<div style="height:	59px; width:100%; clear:both;"></div>
						<div class="description" id="id_report_01">
						
							<div class="box_title mb9">
								<h2>Google My Business Insights – Brand</h2>
							</div>

							<div class="box-report-graph no-background">
								<h2 class="no-background">How customers search for YOUR
									LOCATIONS</h2>
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

							<div style="padding: 0 0 20px;">
								<div class="table-top10" style="width: 80%;">
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
													<td class="td_04"><div>
															<fmt:formatNumber type="number" maxFractionDigits="3"
																value="${ttSearch.topDirectCount}" />
														</div></td>
													<td class="td_05"><div>
															<fmt:formatNumber type="number" maxFractionDigits="3"
																value="${ttSearch.topDiscoveryCount}" />
														</div></td>
													<td class="td_06"><div>
															<fmt:formatNumber type="number" maxFractionDigits="3"
																value="${ttSearch.totalTopSearchCount}" />
														</div></td>
												</tr>
											</core:forEach>
										</tbody>
									</table>
								</div>
								
								<div class="table-bot10" style="width: 80%;">
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
										<tbody>
											<core:forEach items="${bottomTenSearches}" var="btSearch"
												varStatus="loopStatus">

												<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
													<td class="td_01"><div>${btSearch.store}</div></td>
													<td class="td_02"><div>${btSearch.city}</div></td>
													<td class="td_03"><div>${btSearch.state}</div></td>
													<td class="td_04"><div>
															<fmt:formatNumber type="number" maxFractionDigits="3"
																value="${btSearch.bottomDirectCount}" />
														</div></td>
													<td class="td_05"><div>
															<fmt:formatNumber type="number" maxFractionDigits="3"
																value="${btSearch.bottomDiscoveryCount}" />
														</div></td>
													<td class="td_06"><div>
															<fmt:formatNumber type="number" maxFractionDigits="3"
																value="${btSearch.totalBottomSearchCount}" />
														</div></td>
												</tr>
											</core:forEach>
										</tbody>
									</table>
								</div>

								<div class="clearfix"></div>
							</div>
						</div>
						<div style="height:	40px; width:100%; clear:both;"></div>
						<div class="box box_red_title box_report">
							<div class="box_title mb9">
								<h2>Google My Business Insights – Brand</h2>
							</div>
							<div class="box-report-graph clear-background">

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
							<div style="padding: 0 0 20px;">
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
													<td class="td_04"><div>
															<fmt:formatNumber type="number" maxFractionDigits="3"
																value="${ttView.topViewSearchCount}" />
														</div></td>
													<td class="td_05"><div>
															<fmt:formatNumber type="number" maxFractionDigits="3"
																value="${ttView.topViewMapsCount}" />
														</div></td>
													<td class="td_06"><div>
															<fmt:formatNumber type="number" maxFractionDigits="3"
																value="${ttView.totalTopViewsCount}" />
														</div></td>
												</tr>
											</core:forEach>
										</tbody>
									</table>
								</div>
								<div class="table-bot10" style="width: 80%;">
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
										
										<tbody>
											<core:forEach items="${bottomTenViews}" var="btView"
												varStatus="loopStatus">

												<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
													<td class="td_01"><div>${btView.store}</div></td>
													<td class="td_02"><div>${btView.city}</div></td>
													<td class="td_03"><div>${btView.state}</div></td>
													<td class="td_04"><div>
															<fmt:formatNumber type="number" maxFractionDigits="3"
																value="${btView.bottomViewSearchCount}" />
														</div></td>
													<td class="td_05"><div>
															<fmt:formatNumber type="number" maxFractionDigits="3"
																value="${btView.bottomViewMapsCount}" />
														</div></td>
													<td class="td_06"><div>
															<fmt:formatNumber type="number" maxFractionDigits="3"
																value="${btView.totalBottomViewsCount}" />
														</div></td>
												</tr>
											</core:forEach>
										</tbody>
									</table>
								</div>

								<div class="clearfix"></div>
							</div>
						</div>
					</div>
					<div style="height:	0px; width:100%; clear:both;"></div>
					<div class="box box_red_title box_report">
						<div class="box_title mb9">
							<h2>Google My Business Insights – Brand</h2>
						</div>
						<div class="box-report-graph clear-background">
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
											<td class="td_02"><div>
													<fmt:formatNumber type="number" maxFractionDigits="3"
														value="${ttAction.topWebsiteCount}" />
												</div></td>
											<td class="td_02"><div>
													<fmt:formatNumber type="number" maxFractionDigits="3"
														value="${ttAction.topDirectionsCount}" />
												</div></td>
											<td class="td_02"><div>
													<fmt:formatNumber type="number" maxFractionDigits="3"
														value="${ttAction.topCallsCount}" />
												</div></td>
											<td class="td_05"><div>
													<fmt:formatNumber type="number" maxFractionDigits="3"
														value="${ttAction.totalTopActionsCount}" />
												</div></td>
										</tr>
									</core:forEach>
								</tbody>
							</table>
						</div>
						<div class="table-bot10" style="width: 80%;">
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
								
								<tbody>

									<core:forEach items="${bottomTenActions}" var="btAction"
										varStatus="loopStatus">
										<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
											<td class="td_01"><div>${btAction.store}</div></td>
											<td class="td_02"><div>${btAction.city}</div></td>
											<td class="td_02"><div>${btAction.state}</div></td>
											<td class="td_02"><div>
													<fmt:formatNumber type="number" maxFractionDigits="3"
														value="${btAction.bottomWebsiteCount}" />
												</div></td>
											<td class="td_02"><div>
													<fmt:formatNumber type="number" maxFractionDigits="3"
														value="${btAction.bottomDirectionsCount}" />
												</div></td>
											<td class="td_02"><div>
													<fmt:formatNumber type="number" maxFractionDigits="3"
														value="${btAction.bottomCallsCount}" />
												</div></td>
											<td class="td_05"><div>
													<fmt:formatNumber type="number" maxFractionDigits="3"
														value="${btAction.totalBottomActionsCount}" />
												</div></td>
										</tr>
									</core:forEach>
								</tbody>
							</table>
							
						</div>
						<!-- // box -->
						<div class="clearfix"></div>
					<div style="height:	25px; width:100%; clear:both;"></div>
					
					<div class="box box_green_title box_report">
						<div class="box_title mb9">
								<h2>Bing Places Analytics – Brand</h2>
						</div>
					<div class="box-report-graph">
						<h2>Bing Places Analytics</h2>
						<h3>Bing Impressions</h3>
						<div style="width:100%; min-height: 250px; float: left;margin: 0 0 50px 0;" id="bingAnalyticsContainer">
						<!-- place for graphic -->
						</div>
						<div class="clearfix"></div>
					</div>
					
					<div class="table-top10" style="width: 100%;">
										<h2>Top 10 Locations</h2>
										<table class="grid grid_17" width="100%">
											<colgroup>
												<col width="25%">
												<col width="25%">
												<col width="25%">
												<col width="25%">
											</colgroup>
											<thead>
												<tr>
													<th class="th_01"><div>Store #</div></th>
													<th class="th_02"><div>City</div></th>
													<th class="th_02"><div>State</div></th>
													<th class="th_05"><div>Total</div></th>
												</tr>
											</thead>
											<tbody>
												<core:forEach items="${topAnalytics}" var="ttAnalytics"
													varStatus="loopStatus">
													<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
														<td class="td_01"><div>${ttAnalytics.store}</div></td>
														<td class="td_02"><div>${ttAnalytics.city}</div></td>
														<td class="td_02"><div>${ttAnalytics.state}</div></td>
														<td class="td_02"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${ttAnalytics.impressionCount}"/></div></td>
													</tr>
												</core:forEach>
											</tbody>
										</table>
									</div>
									
										<div class="table-top10" style="width: 100%;">
										<h2>Bottom 10 Locations</h2>
										<table class="grid grid_17" width="100%">
											<colgroup>
												<col width="25%">
												<col width="25%">
												<col width="25%">
												<col width="25%">
											</colgroup>
											
											<tbody>
												<core:forEach items="${bAnalytics}" var="bAnalytic"
													varStatus="loopStatus">
													<tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
														<td class="td_01"><div>${bAnalytic.store}</div></td>
														<td class="td_02"><div>${bAnalytic.city}</div></td>
														<td class="td_02"><div>${bAnalytic.state}</div></td>
														<td class="td_02"><div><fmt:formatNumber type="number"
																maxFractionDigits="3" value="${bAnalytic.impressionCount}"/></div></td>
													</tr>
												</core:forEach>
											</tbody>
										</table>
									</div>
					
				</div>
				</div>
			</div>
			

		</div>
		</div>
	</div>
	<div class="clearfix"></div>
	<!-- // page wrapper -->
	<div class="popup-shader" style="display: none"></div>
	<script type="text/javascript">
	
		var nd = ${noDataMessage};
		
		
		if(nd){
			$("#gmbpopup").bPopup();
		}
		$(function() {
			setTimeout(function() {
			    }, 3000);
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

				$.each(list, function(index11, value11) {
					//alert( index + ": " + value.toString() );
					dpsView1.push(value11);
				});

				var list2 = ${mapsHistory};

				$.each(list2, function(index12, value12) {
					//alert( index + ": " + value.toString() );
					dpsView2.push(value12);
				});

				var dpsActions1 = [];
				var dpsActions2 = [];
				var dpsActions3 = [];

				var list3 = ${websitesHistory};
				$.each(list3, function(index13, value13) {
					//alert( index + ": " + value.toString() );
					dpsActions1.push(value13);
				});

				var list4 = ${directionsHistory}
				;
				$.each(list4, function(index14, value14) {
					//alert( index + ": " + value.toString() );
					dpsActions2.push(value14);
				});
				var list5 =${callsHistory};

				$.each(list5, function(index15, value15) {
					//alert( index + ": " + value.toString() );
					dpsActions3.push(value15);
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
						fontSize : 16,
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
						fontSize : 16,
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
				
			 	var preSearchTrends = [];
				var curSearchTrends = [];

				var listspt = ${searchPreTrends};
				$.each(listspt, function(index3, value3) {
					preSearchTrends.push(value3);
				}); 
				
				var listsct = ${searchCurTrends};
				$.each(listsct, function(index4, value4) {
					curSearchTrends.push(value4);
				}); 
				
				var searchTrends = new CanvasJS.Chart("searchTrendsContainer",
				{
		            animationEnabled: true,
		            axisY : {
						gridColor : "white",
						tickColor : "white",
					},
					data: [ 
					{
						type: "column",	
						name: "Previous Year",
						showInLegend: true, 
						markerSize : 0,
						dataPoints :preSearchTrends 
					},
					{
						type: "column",	
						name: "Current Year",
						showInLegend: true,
						markerSize : 0,
						dataPoints:curSearchTrends
					}
					],
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
					},
		        });
			
			    var preViewTrends = [];
				var curViewTrends = [];

				var listvpt = ${viewPreTrends};
				$.each(listvpt, function(index5, value5) {
					preViewTrends.push(value5);
				}); 
				
				var listvct = ${viewCurTrends};
				$.each(listvct, function(index6, value6) {
					curViewTrends.push(value6);
				}); 
				
				var viewTrends = new CanvasJS.Chart("viewTrendsContainer",
						{
				            animationEnabled: true,
				            axisY : {
								gridColor : "white",
								tickColor : "white",
							},
							axisY2: {
								gridColor : "white",
								tickColor : "white",
							},
							data: [ 
							{
								type: "column",	
								name: "Previous Year",
								showInLegend: true, 
								markerSize : 0,
								dataPoints : preViewTrends 
							},
							{
								type: "column",	
								name: "Current Year",
								showInLegend: true,
								markerSize : 0,
								dataPoints: curViewTrends
							}
							],
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
							},
				        });

			 	
				var preActionTrends = [];
				var curActionTrends = [];

				var listapt = ${actionPreTrends};
				$.each(listapt, function(index7, value7) {
					//alert( index + ": " + value.toString() );
					preActionTrends.push(value7);
				}); 
				
				var listact = ${actionCurTrends};
				$.each(listact, function(index8, value8) {
					//alert( index + ": " + value.toString() );
					curActionTrends.push(value8);
				}); 
				
				var actionTrends = new CanvasJS.Chart("actionTrendsContainer",
						{
				            animationEnabled: true,
				            axisY : {
								gridColor : "white",
								tickColor : "white",
							},
							axisY2: {
								gridColor : "white",
								tickColor : "white",
							},
							data: [ 
							{
								type: "column",	
								name: "Previous Year",
								showInLegend: true, 
								markerSize : 0,
								dataPoints : preActionTrends 
							},
							{
								type: "column",	
								name: "Current Year",
								showInLegend: true,
								markerSize : 0,
								dataPoints: curActionTrends
							}
							],
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
							},
				        }); 

			 	var anlyticsData = [];
	
				var analyticsList = ${anlytics};
				$.each(analyticsList, function(indexA1, valueA1) {
					anlyticsData.push(valueA1);
				});
				
				 var bingAnalytics = new CanvasJS.Chart("bingAnalyticsContainer",
						    {
							 animationEnabled : true,
								axisX : {
									minimum: min,
				    	        	maximum: max,
				    	        	valueFormatString: "MMM/DD",
									interval:1,
									labelAngle: -45,
									labelAutoFit: true,
									intervalType: "week",
								},
								axisY : {
									interval : 500,
									gridColor : "white",
									tickColor : "white",
								},
						      data: [
						      {
								type : "area",
								color : "#5DC2F9",
								markerSize : 0,
								label : "",
						        
						        dataPoints:anlyticsData
						      }
						      ]
						    });

				bingAnalytics.render(); 

				
				viewTrends.render();
			    searchTrends.render();
				actionTrends.render(); 
				

				chart.render();
				viewChart.render();
				actionsChart.render();
			}
		});
		
	</script>
</body>
</html>