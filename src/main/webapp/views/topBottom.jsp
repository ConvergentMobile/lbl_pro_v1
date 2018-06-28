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
<html id="page">
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
<link rel="stylesheet" href="js/jquery.mCustomScrollbar.css">
<script src="js/jquery.mCustomScrollbar.js"></script>
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

<link rel="stylesheet" href="css/select2.css">

<script src="js/select2.js"></script>
<script src="js/select2.min.js"></script>
<script>
	jQuery(document).ready(function() {
		jQuery(".selectjs").select2();
	});
</script>
<script type="text/javascript" src="js/canvasjs.min.js"></script>

</head>
<body>
<div class="wide_column_wrapper report_container" >
									<div style="padding: 0 0 20px; background: #d7dbe0;">
										<div class="table-top10" style="width: 100%;">
											<h2>Searches - Top 10 Locations</h2>
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
											<h2>Searches - Bottom 10 Locations</h2>
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
											<div class="clearfix"></div>
										</div>
										</div>
										<div class="clearfix"></div>
										
										<div style="padding: 0 0 20px; background: #d7dbe0;">
										<div class="table-top10" style="width: 100%;">
											<h2>Views - Top 10 Locations</h2>
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
											<h2>Views - Bottom 10 Locations</h2>
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
									
									<div class="table-top10" style="width: 100%;">
										<h2>Actions - Top 10 Locations</h2>
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
										<h2>Actions - Bottom 10 Locations</h2>
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
									
										<div class="clearfix"></div>
										
									</div>
									</div>
</body>
</html>