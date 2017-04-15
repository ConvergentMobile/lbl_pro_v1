<!DOCTYPE html>
<%@page import="java.util.Calendar"%>
<%@page import="com.business.common.util.LBLConstants"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

<script type="text/javascript" src="js/jquery.canvasjs.min.js"></script>
<script src="js/select2.js"></script>
<script src="js/select2.min.js"></script>
<script>
	jQuery(document).ready(function() {
		jQuery(".selectjs").select2();
	});
</script>


<script type="text/javascript">


$(function() {
    $( "#startDate" ).datepicker({
    	maxDate: new Date(),
	'format': 'm/d/yyyy',
	'autoclose': true    
    });

});

 	$(function() {
    $( "#endDate" ).datepicker({
     maxDate: new Date(),
	'format': 'm/d/yyyy',
	'autoclose': true    
    });
    
 	});
 	

	function filterReport(){
		var startDate=$('#startDate').val();
		var enddate = $("#endDate").val();
		var brandname=$('#brandVal').val();
		var store = $('#storevalue').val();
		
		if(startDate == '' || enddate== ''){
			$("#datewarningpopup").bPopup();
		} else{
			document.location.href = "filterGMBLocation.htm?store="+store+"&&brand="+brandname+"&&start="+startDate+"&&end="+enddate;
		}
	}
 	
</script>
 
<style type="text/css">

.canvasjs-chart-credit {
   display: none;
}

.dsp_None{
display: none;
}
 .loading {
    position: fixed;
    height: 100%;
    width: 100%;
    top:0;
    left: 0;
    background: rgba(0, 0, 0, 0.6);
    z-index:9999;
    font-size: 20px;
    text-align: center;
    padding-top: 200px;
    color: #fff;
}
</style>
</head>
<body id="office" class="mw1200">
<!-- page wrapper -->

 <div class="popup" id="datewarningpopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select Start and  End Date</div>
	</div>
</div>

	<div class="popup" id="gmbpopup" style="display: none;">
		<div class="pp-header">
			<!-- <div class="close"></div> -->
			<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
		</div>
		<div class="pp-subheader"></div>
		<div class="pp-body">
			<div class="buttons">Google Insights are not available for your location. Contact Support if you wish to have Google added to your program. </div>
		</div>
	</div>

<div class="popup" id="warningpopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select Brand and Store </div>
	</div>
</div>
<div class="popup" id="storewarningpopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select Brand </div>
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
				<li class="si_business_listings "><a href="business-listings.htm">Business Listings</a></li>
				<li class="si_error_listings"><a href="listing-error.htm">Listing
						Errors</a></li>
				<li class="si_upload_export"><a href="upload-export.htm">Upload/Export</a></li>
				<li class="si_reports selected"><a href="reports.htm">Reports</a></li>
			<li class="si_admin"><a href="admin-listings.htm">CM admin</a></li>
			<li class="si_mobile_profile "><a href="manage-account.htm">Manage Account</a></li>
		<!-- 	<li class="si_schduler"><a href="scheduler.htm">Schedule</a></li> -->
			<!--<li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent Toolbox</a></li>-->
			</ul>
		<!-- // left side navigation --> 
		<!-- content area -->
		<div class="content" id="id_content" style="min-height: 804px;">
    	<div class="nav_pointer pos_01"></div>
		  <!-- subheader -->
			<div class="subheader clearfix">
				<h1>REPORTS</h1>
					<a href="reports.htm" class="back">< Back to Reporting Criteria</a>
				<p>Convergent Mobile</p>
			</div>
			
			<!-- // subheader -->
		  <input name="uploadreportpopup" value="" id="uploadreportpopup" type="hidden">
		  <form id="reportsForm" action="/lbl_pro/reports.htm" method="post">
			<div class="inner_box">
			<!-- box -->
			<!-- title -->
			<div class="box box_red_title box_report">
				<div class="box_title mb9">
				
				<h2>Google My Business Insights – Location</h2>
			</div>
			<!-- // title -->
			
			<!-- subtitle -->
			<div class="box_subtitle_lightgray">
			  <p><strong>${brand}</strong>, ${store} <span class="divider"></span> ${start} - ${end}</p>  
			  <input type="hidden" id="brandVal" value="${brand}">     
			   <input type="hidden" id="directVal" value="${direct}">     
			    <input type="hidden" id="discoveryVal" value="${discovery}">
			    <input type="hidden" id="storeVal" value="${store}">            
  
			</div>
			<div class="box_subtitle_darkgray">
			  <div class="filter-listings" >
			  	from <input type="text" id="startDate" class="input_date_01"> to <input type="text" id="endDate" class="input_date_01">

			  	<select name="store"  id="storevalue">
						<core:forEach items="${storeList}" var="store">
								<option value="${store}" >${store}</option>
						</core:forEach> 
				 </select> 
			  	
			  	<a href="#" onclick="filterReport()" class="btr_button">FIlter Listings</a>
			  </div>
			</div>
			<!-- // subtitle -->
			<!-- wide_column -->
			  <div class="wide_column_wrapper report_container" style="background: #d7dbe0; margin-top:0px;padding-bottom: 20px; height: auto!important;">
				<div class="description" id="id_report_01">

					<div class="box-report-graph shadow">
						<h2>How customers search for you business</h2>
						<div class="left-col" id="searchContainer">
							<!-- place for graphic -->
						</div>
						<div class="right-col">
							<p class="red-title">Direct: <strong><fmt:formatNumber type="number" maxFractionDigits="3" value="${direct}" /></strong></p>
							<p>Customers who find your listings searching for your business name or address</p>
							<p class="blue-title">Discovery: <strong><fmt:formatNumber type="number" maxFractionDigits="3" value="${discovery}" /></strong></p>
							<p>Customers who find your listings searching for a category, product, or service</p>
						</div>
						<div class="clearfix"></div>
					</div>

	
					<div class="box-report-graph shadow">
						<h2>Where customers view your business on Google</h2>
						<h3>The Google services that customers use to find your business</h3>
						<div class="left-col" id="viewContainer">
							<!-- place for graphic -->
						</div>
						<div class="right-col">
							<table class="grid grid_25">
								<tr>
									<td></td>
									<td>Listing on Search </td>
									<td><strong><fmt:formatNumber type="number" maxFractionDigits="3" value="${search}" /></strong></td>
								</tr>
								<tr>
									<td></td>
									<td>Listing on Maps </td>
									<td><strong><fmt:formatNumber type="number" maxFractionDigits="3" value="${maps}" /></strong></td>
								</tr>
								<tr>
									<td></td>
									<td><strong>Total Views</strong></td>
									<td><strong><fmt:formatNumber type="number" maxFractionDigits="3" value="${views}" /></strong></td>
								</tr>
							</table>

						</div>
						<div class="clearfix"></div>
					</div>

					<div class="box-report-graph">
						<h2>Customer actions</h2>
						<h3>The most common actions that customers take on your listing</h3>
						<div class="left-col" id="actionsContainer" >
							<!-- place for graphic -->
						</div>
						<div class="right-col">
							<table class="grid grid_25">
								<tr>
									<td>Visit Your Website </td>
									<td><strong><fmt:formatNumber type="number" maxFractionDigits="3" value="${website}" /></strong></td>
								</tr>
								<tr>
									<td>Request Directions  </td>
									<td><strong><fmt:formatNumber type="number" maxFractionDigits="3" value="${directions}" /></strong></td>
								</tr>
								<tr>
									<td>Call You  </td>
									<td><strong><fmt:formatNumber type="number" maxFractionDigits="3" value="${calls}" /></strong></td>
								</tr>
								<tr>
									<td><strong>Total Actions</strong></td>
									<td><strong><fmt:formatNumber type="number" maxFractionDigits="3" value="${actions}" /></strong></td>
								</tr>
							</table>

						</div>

						<div class="clearfix"></div>
					</div>

				</div>
			  </div>
			  <!-- // wide_column -->
			</div>
			<!-- // box -->
			
		  </div>
		</form></div>
		<!-- // content area --> 
		
	<div class="clearfix"></div>
	<!-- // content wrapper --> 
	<!-- footer -->
	<div class="footer">
		<div class="powered">Powered by</div>
	</div>
	<!-- // footer --> 
</div>
<script type="text/javascript">
var nd = ${noDataMessage};

if(nd){
	$("#gmbpopup").bPopup();
}

$(function () {
	

	window.onload = function () {
	    	
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

		
		var list = ${viewsHistory};
		$.each(list, function( index, value ) {
			//alert( index + ": " + value.toString() );
			dpsView1.push(value);
		});

		var list2 = ${actionsHistory};
		$.each(list2, function( index1, value1 ) {
			//alert( index + ": " + value.toString() );
			dpsView2.push(value1);
		});
		
		
 		 var dpsActions1 = []; 
		 var dpsActions2= []; 
		 var dpsActions3 = []; 
		
		var list3 = ${websitesHistory};
		$.each(list3, function( index3, value3 ) {
			//alert( index + ": " + value.toString() );
			dpsActions1.push(value3);
		});

		var list4 = ${directionsHistory};
		$.each(list4, function( index4, value4 ) {
			//alert( index + ": " + value.toString() );
			dpsActions2.push(value4);
		});
		var list5 = ${callsHistory};
		$.each(list5, function( index5, value5 ) {
			//alert( index + ": " + value.toString() );
			dpsActions3.push(value5);
		});
		
		var dirVal = $("#directVal").val();
		var disVal = $('#discoveryVal').val();
		
	    var chart = new CanvasJS.Chart("searchContainer",
	    {
	      animationEnabled: true,
	     	axisX: {
				labelFontSize: 18,
			},
			axisY: {
				labelFontSize: 0,
				gridColor: "white",
				tickColor: "white",
			},
	      data: [
	      {        
	        type: "bar",     
	        dataPoints: [
	        	{y: Number(disVal), 
	        	   label: "Discovery",
	        	   color: "#314051" },
	       		{y: Number(dirVal),
	        	   label: "Direct", 
	        	   color: "#cc2f12"},
	        ]
	      },
	      ]
	    });
	    
	    var viewChart = new CanvasJS.Chart("viewContainer",
	    		{
	    	       
	    	        animationEnabled: true,
	    	        axisX:{      
	    	        	minimum: min,
	    	        	maximum: max,
	    	        	valueFormatString: "MMM/DD",
						interval:1,
						labelAngle: -45,
						labelAutoFit: true,
						intervalType: intType,
	    	        },
	    	        axisY: {
	    	        	interval: yView,
						gridColor: "white",
						tickColor: "white",
					},
	    	        data: [
	    	               {        
	    	            name: "search",
	    	            showInLegend: true,
	    	            legendMarkerType: "square",
	    	            type: "area",
	    	            color: "#d94136",
	    	            markerSize: 0,
	    	            dataPoints : dpsView1
	    	        },
	    	        {        
	    	            name: "maps",
	    	            showInLegend: true,
	    	            legendMarkerType: "square",
	    	            type: "area",
	    	            color: "#ffbe00",
	    	            markerSize: 0,
	    	            label: "",
	    	            dataPoints : dpsView2
	    	        }
	    	        ],
	    			legend: {
	    				fontSize: 20,
	    				cursor: "pointer",
	    				itemclick: function (e) {
	    					if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
	    						e.dataSeries.visible = false;
	    					} else {
	    						e.dataSeries.visible = true;
	    				}
	    					viewChart.render();
	    				}
	    			}
	    	    });
	    
	    
	    var actionsChart = new CanvasJS.Chart("actionsContainer",
	    	    {
	    	        animationEnabled: true,
	    	        axisX:{
	    	        	minimum: min,
	    	        	maximum: max,
	    	        	valueFormatString: "MMM/DD",
						interval:1,
						labelAngle: -45,
						labelAutoFit: true,
						intervalType: intType,
	    	        },
	    	        axisY:{      
	    	        	interval: yAction,
						gridColor: "white",
						tickColor: "white",
	    	        },
	    	        data: [{        
	    	            name: "website",
	    	            showInLegend: true,
	    	            legendMarkerType: "square",
	    	            type: "area",
	    	            color: "#278D14",
	    	            markerSize: 0,
	    	            dataPoints : dpsActions1
	    	        },
	    	        {        
	    	            name: "directions",
	    	            showInLegend: true,
	    	            legendMarkerType: "square",
	    	            type: "area",
	    	            color: "#D2F38E",
	    	            markerSize: 0,
	    	            label: "",
	    	            dataPoints : dpsActions2
	    	        },
	    	        { 
	    	            name: "calls",
	    	            showInLegend: true,
	    	            legendMarkerType: "square",
	    	            type: "area",
	    	            color: "#5DC2F9",
	    	            markerSize: 0,
	    	            label: "",
	    	            dataPoints : dpsActions3
	    	        }
	    	        ],
	    	        legend: {
	    	        	fontSize: 20,
	    				cursor: "pointer",
	    				itemclick: function (e) {
	    					if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
	    						e.dataSeries.visible = false;
	    					} else {
	    						e.dataSeries.visible = true;
	    				}
	    					actionsChart.render();
	    				}
	    			}
	    	    });

		chart.render();
		viewChart.render();
		actionsChart.render();
	}     
	
});
</script>
</body>
</html>