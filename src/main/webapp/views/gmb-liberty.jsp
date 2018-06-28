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
<link rel="stylesheet" href="css/select2.css">

<script src="js/select2.js"></script>
<script src="js/select2.min.js"></script>

<script type="text/javascript" src="js/canvasjs.min.js"></script>
<script>
	jQuery(document).ready(function() {
		jQuery(".selectjs").select2();
	});
</script>
<script type="text/javascript">



	$( function() {

		$( "#tabs" ).tabs({
			
			beforeLoad: function( event, ui ) {
				$(".loading").show();
				ui.jqXHR.fail(function() {
					ui.panel.html(
						"Couldn't load this tab. We'll try to fix this as soon as possible. " +
						"If this wouldn't be a demo." );
				});
			},
			load: function( event, ui ) {
				$(".loading").hide();
			}
		
		});
	} );

	function runExcelReport(){
		var startDate = $('#sd').val();
		var enddate = $("#ed").val();
		var brandname = $('#brandVal').val();
		var state = $("#statevalue").val();
		if (startDate == '' || enddate == '') {
			$("#datewarningpopup").bPopup();
		} else {
			document.location.href = "runExcelReport.htm?state=" + state
					+ "&&brand=" + brandname + "&&start=" + startDate + "&&end=" + enddate;
		}
	}
	
	function generatePDF() {
		var url = $('#url').val();
		var fileName=$('#reportName').val();

		$(".loading").show();
			  $.ajax({
	            url : 'generatePDF.htm?url='+url+'&&report='+fileName,
	            success: function(response, status, xhr){
	            	//downloadresponse(response, status, xhr);
	            	$(".loading").hide();
	            }
	        }); 
		 
		
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
			$(".loading").show();
			document.location.href = "runGMBBrand.htm?state=" + state
					+ "&&brand=" + brandname+ "&&start=" + startDate + "&&end=" + enddate;
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

		<div class="loading dsp_None">
			<img src="images/load.gif" width="75px" height="75px" />
		</div>

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
			<!-- left side navigation -->
			<!-- // left side navigation -->
			<!-- content area -->
			<div>
				<div class="nav_pointer pos_01"></div>
				<!-- subheader -->
				<div class="content subheader">
					<h1>REPORTS</h1>
					<a href="reports.htm" class="back">< Back to Reporting Criteria</a>
					<span>Export Report</span> <a href="#" class="ico_xls" onclick="runExcelReport()">Excel</a>
					<p>Convergent Mobile</p>
					<a class="ico_pdf" href="http://api.html2pdfrocket.com/pdf?value=http://${url}&apikey=ff60aa04-d396-44bc-a262-e75b163071bc&FileName=${reportName}&JavascriptDelay=3000&MarginBottom=15&MarginLeft=0.25&MarginRight=0.25&MarginTop=0.25&PageWidth=215.9&PageHeight=279.4&FooterUrl=http://23.23.203.174/lbl_pro/pdfheader.htm">PDF</a>
				</div>

				<form id="reportsForm" action="/lbl_pro/reports.htm" method="post">

					<div class="nav_pointer pos_01"></div>
		
					<!-- // subheader -->
		
					<!-- box -->
					<!-- title -->
					<div class="box box_red_title box_report">
						<div class="box_title mb9">
							<h2>Google My Business Insights – Brand</h2>
						</div>
					

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
									<input
									type="hidden" id="url" value="${url}">
									<input
									type="hidden" id="reportName" value="${reportName}">
									

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
									</select> 
									<a href="#" onclick="filterReport()" class="btr_button">FIlter Listings</a>
								</div>
							</div>
							<!-- // subtitle -->
							<!-- wide_column -->
							
							<div id="tabs">
								<ul>
									<li><a href="#insights">Monthly Trends</a></li>
									<li><a href="${insightsURL}">Insights</a></li>
									<li><a href="${topBottomURL}">Top 10/Bottom 10</a></li>
								</ul>
								 
								<div id="insights" style="display:none;" class="wide_column_wrapper report_container" >

									<div class="box-report-graph">
										<h2>Search Trends</h2>
										<div style="width:100%; min-height: 250px; float: left;margin: 0 0% 0 0;" id="searchTrendsContainer">
											<!-- place for graphic -->
										</div>
									</div>
									
									<div class="box-report-graph">
										<h2>View Trends</h2>
										<div style="width:100%; min-height: 250px; float: left;margin: 0 0% 0 0;" id="viewTrendsContainer">
											<!-- place for graphic -->
										</div>
									</div>
									
									<div class="box-report-graph">
										<h2>Action Trends</h2>
										<div style="width:100%; min-height: 250px; float: left;margin: 0 0% 0 0;" id="actionTrendsContainer">
											<!-- place for graphic -->
										</div>
									</div>
								</div>

								<div class="clearfix"></div>
								<div class="clearfix"></div>
							</div>
							</div>
					

						<!-- // wide_column -->
					<!-- // box -->
			</form>
			</div>
		<!-- // content area -->

		<!-- // content wrapper -->
		<!-- footer -->
		
		<!-- // footer -->
	<!-- // page wrapper -->
	<div class="popup-shader" style="display: none"></div>
	<script type="text/javascript">
	
		var nd = ${noDataMessage};
	
		if(nd){
			$("#gmbpopup").bPopup();
		}
		
		$(function() {
			window.onload = function() {

			 
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


				
				viewTrends.render();
			    searchTrends.render();
				actionTrends.render(); 


			}
		},3000);
	</script>
</body>
</html>