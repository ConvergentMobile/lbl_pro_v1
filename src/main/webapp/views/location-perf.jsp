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


	function filterReport(){
		var brandname=$('#brandVal').val();
		var duration=$('#duration').val();
		
		var store = $("#storevalue").val();
		if(brandname == '' || store== ''){
			$("#warningpopup").bPopup();
		} else{
			document.location.href = "locPerf.htm?s="+store+"&&b="+brandname+"&&d="+duration;
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
				<%
					Integer Role=(Integer)session.getAttribute("roleId");				
					if(Role==LBLConstants.CONVERGENT_MOBILE_ADMIN){						
						%>
				<li class="si_admin"><a href="admin-listings.htm">CM admin</a></li>
				<li class="si_mobile_profile "><a href="manage-account.htm">Manage
						Account</a></li>
				<%} %>
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
			

			<div class="inner_box">
			<!-- box -->
			<!-- title -->
			<div class="box box_red_title box_report">
				<div class="box_title mb9">
				
				<h2>Directory Performance – Location</h2>
			</div>
			<!-- // title -->
			
			<!-- subtitle -->
			<div class="box_subtitle_lightgray">
			  <p><strong>${brand}</strong>,${store} </p>  
			  	<input type="hidden" id="brandVal" value="${brand}">               

			</div>
			
			<div class="box_subtitle_darkgray">
			  <div class="filter-listings" >
			  	
				 
				 <label>For Last</label>
						<select name="store" id="storevalue">
										<option value="">Select Store</option>
									<core:forEach items="${storeList}" var="store">

										<option value="${store}">${store}</option>
									</core:forEach>
								</select>
						<select name="duration"
							id="duration">
								<option value="1">Last Month</option>
								<option value="3">3 Months Ago</option>
								<option value="6">6 Months Ago</option>
				</select>
			  	
			  	<a href="#" onclick="filterReport()" class="btr_button">Filter Report</a>
			  </div>
			</div>
			

			<!-- // subtitle -->
			<!-- wide_column -->
			  <div class="wide_column_wrapper report_container" style="background: #d7dbe0; margin-top:0px;padding-bottom: 20px; height: auto!important;">
			  
			  
			  		<div class="box-report-graph">
								<div style="width:100%; min-height: 250px; float: left;margin: 0 0 50px 0;" id="coverageContainer">
								</div>
								<div id="total" style="position:absolute;left:0px;top:125px;height:100%;width:100%;line-height:360px;text-align:center;color:grey;font-size:22px;"></div>
				
					<div class="clearfix"></div>
					</div>
					<div class="clearfix"></div>
						
					<div class="box-report-graph">
								<div style="width:100%; min-height: 250px; float: left;margin: 0 0 50px 0;" id="coverageBarContainer">
								</div>
					<div class="clearfix"></div>
					</div>


							<div class="wide_column_wrapper report_container"
							style="background: #d7dbe0; margin-top: 0px; padding-bottom: 20px; height: auto !important;">
							
							<div class="box-report-graph">
								<h4>Listing Accuracy</h4>
								<span style="color: #FFA500" >${averageAccuracy}%</span> Total Accuracy across the directories shown below 
							</div>
								
						
							<div class="box-report-graph">
								<h3>Accuracy Details</h3>
								<table width="100%">
								<colgroup>
									<col width="30%" />
									<col width="30%" />
									<col width="40%" />
								</colgroup>
								<tbody>

									<core:forEach items="${accuracy}" var="bean">
										<tr>
											<td class="td_07"><div>
													<core:out value="${bean.directory}"></core:out>
												</div></td>
											
											<td class="td_08"><div>
													<core:out value="${bean.provided_Name}"></core:out>
													<core:out value="${bean.street_Address}"></core:out>
													<core:out value="${bean.location_Address}"></core:out>
													<core:out value="${bean.city}"></core:out>
													<core:out value="${bean.listing_Phone}"></core:out>
													<core:out value="${bean.provided_PostalZipCode}"></core:out>
												</div></td>
											<td class="td_09"><div>
												<core:out value="${bean.average_Match}"></core:out>%
											</div></td>

										</tr>
									</core:forEach>
								</tbody>
							</table>
							<table>
							
							</table>
								<div class="clearfix"></div>
							</div>
						</div>
					</div>
					<div class="clearfix"></div>

				</div>
			  </div>
			  <!-- // wide_column -->
			</div>
			<!-- // box -->
			
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

<script type="text/javascript">

$(function() {
	window.onload = function() {
		
		var coveragePer =  ${coverage};
		var total =  ${totalListings};
		var possible =  ${possibleListings};
		var increment =  ${increment};
		var months =  ${months};
		
		var coverageChart = new CanvasJS.Chart("coverageContainer", {
			dataPointWidth: 12,
			animationEnabled: true,
			radius: "90%", 
	        innerRadius: "85%",
			title:{
				text: "Listings Coverage",
			},
			subtitles:[
				   		{
				   			text: "You currently have " + total.toLocaleString() + " listings out of a possible " + possible.toLocaleString() + " listings."
				   			+" Your Listing Coverage has increased by "+ increment + "% since a " + months + " months ago",
				            verticalAlign: "bottom",
				   			fontSize: 15
				   	}
				   	],
			data: [{
				indexLabelPlacement: "inside",
				type: "doughnut",
				dataPoints: [
				         	{ y: coveragePer, label: "" , color : "#00BFFF"},
							{ y: 100-coveragePer, label: "" , color : "#EAFFFF"},
				]
			}]
		});
		coverageChart.render();
		
		var dps = coverageChart.options.data[0].dataPoints;
		var sum = 0;     
		        
		for(var i = 0; i < 1; i++){
		        
		   sum += dps[i].y;                
		        
		}
		                               
		document.getElementById("total").innerHTML = sum+"%";
	
		
	var google =  ${google};
	var facebook =  ${facebook};
	var yellowpages =  ${yellowpages};
	var bing =  ${bing};
	var yahoo =  ${yahoo};
	
	var googleDiff =  ${googleDiff};
	var facebookDiff =  ${facebookDiff};
	var bingDiff =  ${bingDiff};
	var yellowpagesDiff =  ${yellowpagesDiff};
	var yahooDiff =  ${yahooDiff};
	
		
		
var chart = new CanvasJS.Chart("coverageBarContainer", {
	 dataPointWidth: 15,
	animationEnabled : true,
	axisX : {
		labelFontSize : 15,
		indexLabelPlacement: "outside",
		lineThickness:0,
		tickThickness:0,
	},
	axisY : {
    	maximum: 100,
		gridColor : "white",
		tickColor : "white",
		labelAngle: 0,
		lineThickness:0,
		tickThickness:0,
		valueFormatString:" "
	},
	title:{
		text: "Coverage %",
	},
	data : [{
		
			type : "stackedBar100",
	        indexLabel: "{y}%",
	        indexLabelFontSize: 15,
			dataPoints : [
		    {
			y : google,
			label : "Google",
			color : "#00BFFF"
			}, 
			{
			y : facebook,
			label : "Facebook",
			color : "#00BFFF"
			}, 
			{
			y : yellowpages,
			label : "Yellowpages",
			color : "#00BFFF"
			}, 
			{
			y : bing,
			label : "Bing",
			color : "#00BFFF"
			}, 
			{
			y : yahoo,
			label : "Yahoo",
			color : "#00BFFF"
			}
	       ]
		},
		 {
	        type: "stackedBar100",
	        indexLabel: "{y}%",
	        indexLabelFontSize: 15,
	        dataPoints: [
            	{
	  			y : googleDiff,
	  			label : "Google",
	  			color : "#C0C0C0"
	  			}, 
	  			{
	  			y : facebookDiff,
	  			label : "Facebook",
	  			color : "#C0C0C0"
	  			}, 
	  			{
	  			y : yellowpagesDiff,
	  			label : "Yellowpages",
	  			color : "#C0C0C0"
	  			}, 
	  			{
	  			y : bingDiff,
	  			label : "Bing",
	  			color : "#C0C0C0"
	  			}, 
	  			{
	  			y : yahooDiff,
	  			label : "Yahoo",
	  			color : "#C0C0C0"
	  			},  
	  	        ]
	     	 },
	      ]
});

chart.render();




}
	
});
	
</script>
</body>
</html>