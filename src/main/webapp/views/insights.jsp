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

<script type="text/javascript" src="js/canvasjs.min.js"></script>
<script>
	jQuery(document).ready(function() {
		jQuery(".selectjs").select2();
	});
</script>
</head>
<body>
<div class="wide_column_wrapper report_container" >
									<div class="box-report-graph">
										<h2>How customers search for YOUR LOCATIONS</h2>
										<div class="left-col" id="searchContainer">
											<!-- place for graphic -->
										</div>
										<div class="right-col">
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
									</div>
									</div> 
									
	
<script type="text/javascript">
	

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

				var list4 = ${directionsHistory};
				$.each(list4, function(index14, value14) {
					//alert( index + ": " + value.toString() );
					dpsActions2.push(value14);
				});
				var list5 =${callsHistory};

				$.each(list5, function(index15, value15) {
					//alert( index + ": " + value.toString() );
					dpsActions3.push(value15);
				});

				var dirVal = ${direct};
				var disVal = ${discovery};

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
						dataPoints : dpsView2
					} ],
					legend: {
						fontSize : 20,
			            cursor:"pointer",
			            itemclick : function(e){
			              if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible){
			                e.dataSeries.visible = false;
			              }
			              else{
			                e.dataSeries.visible = true;
			              }
			              viewChart.render();
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
							actionsChart.render();
						}
					}
				});

				
				viewChart.render();
				actionsChart.render();
				chart.render();

	</script>
</body>
</html>