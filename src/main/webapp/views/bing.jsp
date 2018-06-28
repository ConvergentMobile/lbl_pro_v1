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

<link rel="stylesheet" href="js/select2.css">

<script src="js/select2.js"></script>
<script src="js/select2.min.js"></script>

<script type="text/javascript" src="js/canvasjs.min.js"></script>

</head>
<body>
<div class="wide_column_wrapper report_container" >
																
									<div class="box-report-graph">
										<h2>Bing Analytics</h2>
										<h3>Bing Impressions</h3>
										<div style="width:100%; min-height: 250px; float: left;margin: 0 0 50px 0;" id="bingAnalyticsContainer">
											<!-- place for graphic -->
										</div>

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
											<thead>
												<tr>
													<th class="th_01"><div>Store #</div></th>
													<th class="th_02"><div>City</div></th>
													<th class="th_02"><div>State</div></th>
													<th class="th_05"><div>Total</div></th>
												</tr>
											</thead>
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

								<div class="clearfix"></div>


									
	
<script type="text/javascript">
	
			
		    var minY = ${minYear};
		    var minM = ${minMonth};
		    var minD = ${minDate};

		    var maxY = ${maxYear};
		    var maxM = ${maxMonth};
		    var maxD = ${maxDate};
			var min = new Date(minY, minM,minD);
			var max = new Date(maxY,maxM,maxD);
			
			
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


	</script>
</body>
</html>