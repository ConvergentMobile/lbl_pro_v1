<!DOCTYPE html>
<%@page import="com.business.common.util.LBLConstants"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<script src="js/jquery-1.9.1.min.js"></script>
<script src="js/jquery.idTabs.min.js"></script>
<script src="js/jquery.slimscroll.min.js"></script>
<script src="js/jquery.screwdefaultbuttonsV2.js"></script>
<script src="js/jquery.cycle.all.js"></script>
<script src="js/functions.js"></script>
<script src="js/jquery.bpopup.min.js"></script>
<link rel="stylesheet" href="js/jquery.mCustomScrollbar.css">
<script src="js/jquery.mCustomScrollbar.js"></script>

<script type="text/javascript" src="js/canvasjs.min.js"></script>
<link rel="stylesheet" href="js/select2.css">
<script src="js/select2.js"></script>
<script src="js/select2.min.js"></script>

<style type="text/css">
.report_accuracy .box_title_right span {
  float: right;
  display: inline-block;
  font-size: 26px;
  color: #ffff66;
  font-weight: 600;
  margin: 4px 0 0 0px;
}
.select2-container--default .select2-selection--single .select2-selection__arrow b {
  border-color: #888 transparent transparent transparent;
  border-style: solid;
  border-width: 5px 4px 0 4px;
  height: 0;
  left: 50%;
  margin-left: -4px;
  margin-top: -2px;
  position: absolute;
  top: 50%;
  width: 0;
    margin: 29px 0px 0px;
}
.canvasjs-chart-canvas {
	height: 350px!important;
}

</style>
<script>
jQuery(document).ready(function(){
	jQuery(".selectjs").select2();
 });
</script>
<script type="text/javascript">
	window.onload = function () {
		var brandname=$("#brandname").val();
		
		var totalStores=$("#totalStores").val();
		totalStores=totalStores/4;
		totalStores=Math.round(totalStores);
		//alert(totalStores);
		
		
		//alert(brandname);
      var dps = []; 
     // alert(dps);
      var xVal = dps.length + 1;
   

      var yVal = 15;//dataPoints. 
      $.ajax({
			type : "get",
			url : "getGraphInfo.htm",
			cache : false,
			data:{
				brandname:brandname,
			},
			success : function(response) {
				
				var graph= $.parseJSON(response);
 

			$(graph).each(function(i,client){
				//alert(client.day);
				
		      	dps.push({x: new Date(client.year,client.month,client.day),
		      		y: client.listingCount});
		      	//alert(dps);
		      	xVal++;
		      	if (dps.length >  10 )
		      	{
		      		dps.shift();				
		      	}

			});
			// alert(dps);
			 CanvasJS.addColorSet("greenShades",
                [//colorSet Array

                "#B71C1C",
                              
                ]);
      var chart = new CanvasJS.Chart("chartContainer",{
    	  colorSet: "greenShades",
    	 
      	title :{
      		text: "Total Listings Found"
      	},
       
    	axisX: {
    		labelFontSize:15,
            interval:1,

      		valueFormatString: "MM/DD"
      	},
      	
      	axisY: {
      		
      		 lineThickness: 0,
      		 gridThickness: 1,
      		interval: totalStores
      	
      	},
      	data: [{
      		markerSize: 12,
      		lineThickness: 4,
      		markerBorderThickness: 2,
      		markerBorderColor : "white",
      		markerColor: "#0E0736",
      		type: "line",
      		dataPoints : dps
      	}]
      });

      chart.render();


			}
  	  });
    	

	
		 
	}
	

</script>



<script type="text/javascript">


function sortBystore(flag){
	var brandname=$('#brandname').val();
	//alert(brandname);

	document.location.href = "sByStore.htm?brand="+brandname+"&&flag="+flag;	
}
function sortByPercentage(flag){
	var brandname=$('#brandname').val();
	//alert(brandname);

	document.location.href = "sByAccracy.htm?brand="+brandname+"&&flag="+flag;	
}
function sortgoogle(flag){
	var brandname=$('#brandname').val();
	//alert(brandname);

	document.location.href = "sByGoolge.htm?brand="+brandname+"&&flag="+flag;	
}
function sortbing(flag){
	var brandname=$('#brandname').val();
	//alert(brandname);

	document.location.href = "sByBing.htm?brand="+brandname+"&&flag="+flag;	
}
function sortYahoo(flag){
	var brandname=$('#brandname').val();
	//alert(brandname);

	document.location.href = "sByYahoo.htm?brand="+brandname+"&&flag="+flag;	
}
function sortYelp(flag){
	var brandname=$('#brandname').val();
	//alert(brandname);

	document.location.href = "sByYelp.htm?brand="+brandname+"&&flag="+flag;	
}
function sortYp(flag){
	var brandname=$('#brandname').val();
	//alert(brandname);

	document.location.href = "sByYp.htm?brand="+brandname+"&&flag="+flag;	
}
function sortCitySearch(flag){
	var brandname=$('#brandname').val();
	//alert(brandname);

	document.location.href = "sByCitySearch.htm?brand="+brandname+"&&flag="+flag;	
}
function sortMapQuest(flag){
	var brandname=$('#brandname').val();
	//alert(brandname);

	document.location.href = "sByMapQuest.htm?brand="+brandname+"&&flag="+flag;	
}
function sortSuperPages(flag){
	var brandname=$('#brandname').val();
	//alert(brandname);

	document.location.href = "sBySuperPages.htm?brand="+brandname+"&&flag="+flag;	
}
function sortYellowBook(flag){
	var brandname=$('#brandname').val();
	//alert(brandname);

	document.location.href = "sByYellowBook.htm?brand="+brandname+"&&flag="+flag;	
}
function sortWhitePages(flag){
	var brandname=$('#brandname').val();
	//alert(brandname);

	document.location.href = "sByWhitePages.htm?brand="+brandname+"&&flag="+flag;	
}

  function runcheckreport() {
		var brandname=$('#brandname').val();
		//alert(brandname);
	
		 var store = $("#storeval").val(); 
		//var store = document.getElementById("storeval");
		//var clinetname = e.options[e.selectedIndex].value;
		//var store = e.options[e.selectedIndex].text;

			document.location.href = "checkreportview.htm?store="+store+"&&brandname="+brandname;
		
		//alert("store:::::::::::::"+store);
				
	}
  $(document).ready(function() {
	 // $(".loading").removelass("dsp_None");
	  
		$("select#store").change(function() {
			  var brandname=$('#brandname').val();
			   var store=$('#store').val();
			   var percentage=$('#percentage').val();
			   document.location.href = "getListingByStore.htm?store="+store+"&&brand="+brandname+"&&percentage="+percentage;
		});
		$("#loadmore").mouseover(function() {
			$("#loadmore").css({"color":"green"});
		});
		$("#loadmore").mouseout(function() {
			$("#loadmore").css({"color":"red"});
		});
		if(!$(".loading").hasClass("dsp_None")){
			  $(".loading").addClass("dsp_None");
		   }
  });
   /* function viewListing(){
	 
   } */
	/* function loadmorestores() {
	   $(".loading").removeClass("dsp_None");
	   $("#loadmore").css({"color":"green"});
	   setInterval(function(){ 
		   
		   if($(".odd").hasClass("dsp_None")){
			   $(".odd").removeClass("dsp_None");
			   }
		   if(!$(".loading").hasClass("dsp_None")){
		   $(".loading").addClass("dsp_None");
		   }
		   
	   }, 3000);
	   $("#loadmore").addClass("dsp_None");
	}
   
 */
 
 function loadmorestores() {
		var brandname=$('#brandname').val();
		//alert(brandname);

		document.location.href = "load-morestore.htm?brand="+brandname;	
		
	}
	function runaccuracyreport() {
		var brandname=$('#brandname').val();
		
			document.location.href = "reports-accuracy.htm?brand="+brandname;	
		
			
	}
	
	function accuracyXLs() {
		var brandname=$('#brandname').val();
	
			document.location.href = "accuracyreportxls.htm?brand="+brandname;	
		
			
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
				<li class="signout"><a href="logout.htm">SignOut</a></li>
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
				<li class="si_business_listings"><a
					href="business-listings.htm">Business Listings</a></li>
				<li class="si_error_listings"><a href="listing-error.htm">Listing
						Errors</a></li>
				<li class="si_upload_export"><a href="upload-export.htm">Upload/Export</a></li>
				<li class="si_reports selected"><a href="reports.htm">Reports</a></li>
				<%
					Integer Role = (Integer) session.getAttribute("roleId");
					if (Role == LBLConstants.CONVERGENT_MOBILE_ADMIN) {
				%>
				<li class="si_admin"><a href="admin-listings.htm">CM admin</a></li>
					<li class="si_mobile_profile "><a href="manage-account.htm">Manage
						Account</a></li>
				<%
					}
				%>
				<!-- 	<li class="si_schduler"><a href="scheduler.htm">Schedule</a></li> 
			

				<li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent
						Toolbox</a></li>-->
			</ul>
		<!-- // left side navigation --> 
		<!-- content area -->
		<div class="content" id="id_content">
			<div class="nav_pointer pos_01"></div>
			<!-- subheader -->
			<div class="subheader clearfix">
				<h1>REPORTS</h1>
					<a href="reports.htm" class="back">< Back to Reporting Criteria</a>
				<span>Export Report</span>
				<a href="#" onclick="accuracyXLs()" class="ico_xls">XLS</a>
				<p>Convergent Mobile</p>
			</div>
			<!-- // subheader -->
			<div class="inner_box"> 
				<!-- box -->
				<div class="box box_red_title report_accuracy"> 
					<!-- title -->
					<div class="box_title">
						<h2>Accuracy Report</h2>
						<p>${brandname}</p>
						<input type="hidden" name="brandname" id="brandname"
								value="${brandname}" />
						<div class="box_title_right btr_2">
							<form class="box_title_form">
								<a href="#" class="lnk" style="float:left;margin: 0px 43px 0px;" onclick="runaccuracyreport()">Clear Filter</a>
								<select name="store" id="store" class="selectjs" style="width: 150px;">
									<option value="">Select Store</option>

										<core:forEach items="${liststoreNames}" var="stores">
											<option value="${stores}">${stores}</option>
										</core:forEach>
								</select>
							</form>
						</div>
					</div>
					<!-- // title -->
					<div class="box-subtitle-1 pl30">Visibility - 10 Sources</div>
					<div class="box-visibility">
						<div class="box-visibility-left">
						<fmt:formatNumber value="${totalPercentage}" var="avgAccuracy" maxFractionDigits="0"></fmt:formatNumber>
							<p>Percent<br />Found</p>
							<span>${avgAccuracy}%</span>
							<table width="100%" class="grid grid_19">
							<input type="hidden" value="${totalStores}" name="totalStores" id="totalStores">
							
									<tr>
										<th class="th_01"><div></div></th>
										<th class="th_02"><div>Locations</div></th>
									</tr>
									
									<tr class="head">
										<td class="td_01"><div>Total</div></td>
										<td class="td_02"><div>${totalStores}</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>100-76%</div></td>
										<td class="td_02"><div>${percentageCategory1}</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>75-51%</div></td>
										<td class="td_02"><div>${percentageCategory2}</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>50-26%</div></td>
										<td class="td_02"><div>${percentageCategory3}</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>25-0%</div></td>
										<td class="td_02"><div>${percentageCategory4}</div></td>
									</tr>
							</table>
						</div>
						<div id="chartContainer" style="height: 350px; width: 60%;  margin: -18px 0px 0px;"
								class="box-visibility-right">
								<!-- <p class="title"></p>
							<img src="images/graph-02.png"> -->
								<!-- <div  >
  								</div> -->

							</div>
						<div class="floatfix"></div>
					</div>
					<div class="box-subtitle-1 pl30">Visibility By Location</div>
					<table width="100%" class="grid grid_18">
						<colgroup>
							<col width="10.5%" />
							<col width="10.5%" />
							<col width="8%" />
							<col width="8%" />
							<col width="8%" />
							<col width="8%" />
							<col width="8%" />
							<col width="8%" />
							<col width="8%" />
							<col width="8%" />
							<col width="8%" />
							<col width="8%" />
						</colgroup>
						<thead>
							<tr>
    							<th class="th_01" rowspan="2"><div onclick="sortBystore('${flagvalue}')">Store #</div></th>
							    <th class="th_02" rowspan="2"><div onclick="sortByPercentage('${flagvalue}')">Percent Found<br/>by Location</div></th>
							    <th class="th_03" colspan="10"><div>Listing Accuracy</div></th>
						  </tr>
							<tr>
								<th class="th_04 down"><div onclick="sortgoogle('${flagvalue}')"><img src="images/logo-acc-01.png"></div></th>
								<th class="th_04 up"><div onclick="sortbing('${flagvalue}')"><img src="images/logo1-foursquare.png"></div></th>
								<th class="th_04 down"><div onclick="sortYahoo('${flagvalue}')"><img src="images/logo-acc-03.png"></div></th>
								<th class="th_04 down"><div onclick="sortYelp('${flagvalue}')"><img src="images/logo-acc-04.png"></div></th>
								<th class="th_04 down"><div onclick="sortYp('${flagvalue}')"><img src="images/logo-acc-05.png"></div></th>
								<th class="th_04 up"><div onclick="sortCitySearch('${flagvalue}')"><img src="images/logo-acc-06.png"></div></th>
								<th class="th_04 down"><div onclick="sortMapQuest('${flagvalue}')"><img src="images/logo-acc-07.png"></div></th>
								<th class="th_04 down"><div onclick="sortSuperPages('${flagvalue}')"><img src="images/logo1-usplaces.png"></div></th>
								<th class="th_04 down"><div onclick="sortYellowBook('${flagvalue}')"><img src="images/logo1-yellowbot.png"></div></th>
								<th class="th_04 down"><div onclick="sortWhitePages('${flagvalue}')"><img src="images/logo1-ezlocal.png"></div></th>
							</tr>
						</thead>
					</table>
					<div id="id_accuracy_report">
						<table width="100%" class="grid grid_18">
							<colgroup>
								<col width="10.5%" />
								<col width="10.5%" />
								<col width="8%" />
								<col width="8%" />
								<col width="8%" />
								<col width="8%" />
								<col width="8%" />
								<col width="8%" />
								<col width="8%" />
								<col width="8%" />
								<col width="8%" />
								<col width="8%" />
							</colgroup>
							<tbody>
								<core:set var="total" value="${fn:length(accuracyreports)}"/>
									<core:forEach items="${accuracyreports}" var="accuracy" varStatus="i" >
									<core:set value="${i.count}" var="index" scope="page"></core:set>
										
										<input type="hidden" name="brandname"
											value="${accuracy.brandname}" id="brandname" />
											<core:set var="className" value=""></core:set>
									<core:if test="${index gt 10 }">
									<core:set var="className" value="dsp_None"></core:set>
									</core:if>
										<tr class="odd ">
										
											<td class="td_01"><div>
													<input type="hidden" name="brandname"
														value="${accuracy.brandname}" id="brandname" /> <a
														href="<core:url value="checkreportview.htm?store=${accuracy.store}&&brandname=${accuracy.brandname}"/>">${accuracy.store}</a>
													<input type="hidden" value="${accuracy.store}"
														id="storeval">
												</div></td>
											<td class="td_02"><div>${accuracy.accuracy}%</div></td>
											<%-- <td class="td_02"><div>${acc.count}%</div></td> --%>
											<core:set value="${accuracy.googleColourCode}"
												var="googleErrorsCount"></core:set>
											<core:set value="${accuracy.foursquareColourCode}"
												var="bingErrorsCount"></core:set>
												<core:set 
												var="count" scope="page"></core:set>

											<core:set value="${accuracy.yelpColourCode}"
												var="yelpErrorsCount"></core:set>
											<core:set value="${accuracy.yahooColourCode}"
												var="yahooErrorsCount"></core:set>
											<core:set value="${accuracy.ypColourCode}"
												var="ypErrorsCount"></core:set>
											<core:set value="${accuracy.citySearchColourCode}"
												var="citySearchErrorsCount"></core:set>
											<core:set value="${accuracy.mapquestColourCode}"
												var="mapquestErrorsCount"></core:set>
											<core:set value="${accuracy.uslocalColourCode}"
												var="superpagesErrorsCount"></core:set>
											<core:set value="${accuracy.yellobotColourCode}"
												var="yellobookErrorsCount"></core:set>
											<core:set value="${accuracy.ezlocalColourCode}"
												var="whitepagesErrorsCount"></core:set>


											<core:if
												test="${googleErrorsCount eq 'O' }">

												<td class="td_03"><div>
														<span class="orange"></span>
													</div></td>
											</core:if>
											<core:if test="${googleErrorsCount eq 'G'}">
											
												<td class="td_03"><div>
														<span class="green"></span>
													</div></td>
											</core:if>
											<core:if test="${googleErrorsCount ge 'R'}">
												<td class="td_03"><div>
														<span class="red"></span>
													</div></td>
											</core:if>
											<core:if test="${googleErrorsCount eq 'D' }">
												<td class="td_03"><div>
														<span class="dash"></span>
													</div></td>
											</core:if>

											<core:if
												test="${bingErrorsCount eq 'O'  }">

												<td class="td_03"><div>
														<span class="orange"></span>
													</div></td>
											</core:if>
											<core:if test="${bingErrorsCount eq 'G'}">
												<td class="td_03"><div>
														<span class="green"></span>
													</div></td>
											</core:if>
											<core:if test="${bingErrorsCount ge 'R'}">
												<td class="td_03"><div>
														<span class="red"></span>
													</div></td>
											</core:if>
											<core:if test="${bingErrorsCount eq 'D' }">
												<td class="td_03"><div>
														<span class="dash"></span>
													</div></td>
											</core:if>

											<core:if
												test="${yahooErrorsCount eq 'O'}">

												<td class="td_03"><div>
														<span class="orange"></span>
													</div></td>
											</core:if>
											<core:if test="${yahooErrorsCount eq 'G'}">
												<td class="td_03"><div>
														<span class="green"></span>
													</div></td>
											</core:if>
											<core:if test="${yahooErrorsCount ge 'R'}">
												<td class="td_03"><div>
														<span class="red"></span>
													</div></td>
											</core:if>
											<core:if test="${yahooErrorsCount eq 'D'  }">
												<td class="td_03"><div>
														<span class="dash"></span>
													</div></td>
											</core:if>

											<core:if
												test="${yelpErrorsCount eq 'O'}">

												<td class="td_03"><div>
														<span class="orange"></span>
													</div></td>
											</core:if>
											<core:if test="${yelpErrorsCount eq 'G'}">
												<td class="td_03"><div>
														<span class="green"></span>
													</div></td>
											</core:if>
											<core:if test="${yelpErrorsCount ge 'R'}">
												<td class="td_03"><div>
														<span class="red"></span>
													</div></td>
											</core:if>
											<core:if test="${yelpErrorsCount eq 'D'}">
												<td class="td_03"><div>
														<span class="dash"></span>
													</div></td>
											</core:if>

											<core:if
												test="${ypErrorsCount eq 'O'}">

												<td class="td_03"><div>
														<span class="orange"></span>
													</div></td>
											</core:if>
											<core:if test="${ypErrorsCount eq 'G'}">
												<td class="td_03"><div>
														<span class="green"></span>
													</div></td>
											</core:if>
											<core:if test="${ypErrorsCount ge 'R'}">
												<td class="td_03"><div>
														<span class="red"></span>
													</div></td>
											</core:if>
											<core:if test="${ypErrorsCount eq 'D' }">
												<td class="td_03"><div>
														<span class="dash"></span>
													</div></td>
											</core:if>

											<core:if
												test="${citySearchErrorsCount eq 'O' }">

												<td class="td_03"><div>
														<span class="orange"></span>
													</div></td>
											</core:if>
											<core:if test="${citySearchErrorsCount eq 'G'}">
												<td class="td_03"><div>
														<span class="green"></span>
													</div></td>
											</core:if>
											<core:if test="${citySearchErrorsCount ge 'R'}">
												<td class="td_03"><div>
														<span class="red"></span>
													</div></td>
											</core:if>

											<core:if test="${citySearchErrorsCount eq 'D' }">
												<td class="td_03"><div>
														<span class="dash"></span>
													</div></td>
											</core:if>


											<core:if
												test="${mapquestErrorsCount eq 'O' }">

												<td class="td_03"><div>
														<span class="orange"></span>
													</div></td>
											</core:if>
											<core:if test="${mapquestErrorsCount eq 'G'}">
												<td class="td_03"><div>
														<span class="green"></span>
													</div></td>
											</core:if>
											<core:if test="${mapquestErrorsCount ge 'R'}">
												<td class="td_03"><div>
														<span class="red"></span>
													</div></td>
											</core:if>
											<core:if test="${mapquestErrorsCount eq 'D'}">
												<td class="td_03"><div>
														<span class="dash"></span>
													</div></td>
											</core:if>

											<core:if
												test="${superpagesErrorsCount eq 'O' }">

												<td class="td_03"><div>
														<span class="orange"></span>
													</div></td>
											</core:if>
											<core:if test="${superpagesErrorsCount eq 'G'}">
												<td class="td_03"><div>
														<span class="green"></span>
													</div></td>
											</core:if>
											<core:if test="${superpagesErrorsCount ge 'R'}">
												<td class="td_03"><div>
														<span class="red"></span>
													</div></td>
											</core:if>
											<core:if test="${superpagesErrorsCount eq 'D'}">
												<td class="td_03"><div>
														<span class="dash"></span>
													</div></td>
											</core:if>

											<core:if
												test="${yellobookErrorsCount eq 'O' }">

												<td class="td_03"><div>
														<span class="orange"></span>
													</div></td>
											</core:if>
											<core:if test="${yellobookErrorsCount eq 'G'}">
												<td class="td_03"><div>
														<span class="green"></span>
													</div></td>
											</core:if>
											<core:if test="${yellobookErrorsCount ge 'R'}">
												<td class="td_03"><div>
														<span class="red"></span>
													</div></td>
											</core:if>
											<core:if test="${yellobookErrorsCount eq 'D' }">
												<td class="td_03"><div>
														<span class="dash"></span>
													</div></td>
											</core:if>


											<core:if
												test="${whitepagesErrorsCount eq 'O'  }">

												<td class="td_03"><div>
														<span class="orange"></span>
													</div></td>
											</core:if>
											<core:if test="${whitepagesErrorsCount eq 'G'}">
												<td class="td_03"><div>
														<span class="green"></span>
													</div></td>
											</core:if>
											<core:if test="${whitepagesErrorsCount ge 'R'}">
												<td class="td_03"><div>
														<span class="red"></span>
													</div></td>
											</core:if>

											<core:if test="${whitepagesErrorsCount eq 'D'}">
												<td class="td_03"><div>
														<span class="dash"></span>
													</div></td>
											</core:if>



										</tr>


									</core:forEach>							
							</tbody>
						</table>
					</div>
					<div class="box-legend">
							<span style="text-align: left;" id="loadmore1"> <core:if
									test="${mode eq 'mode' }">
									<span id="loadmore" onclick="loadmorestores()" style="color: red; margin-right: 200px">Load More</span>
								</core:if> </span>
							 <span class="green">Found with 100% accuracy</span> <span
								class="orange">Found with possible errors</span> <span
								class="red">Found with 100% inaccurate data</span> <span
								class="dash">Not found</span>
					</div>
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
<!-- // page wrapper -->

</body>
</html>