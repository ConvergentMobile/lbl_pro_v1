<!DOCTYPE html>
<%@page import="java.util.Calendar"%>
<%@page import="com.business.common.util.LBLConstants"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<link rel="stylesheet" href="js/jquery.mCustomScrollbar.css">
<script src="js/jquery.mCustomScrollbar.js"></script>
<link rel="stylesheet" href="js/select2.css">
<script src="js/select2.js"></script>
<style type="text/css">
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
<style type="text/css">
.report_citations .box_title_right span {
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
    margin: 30px 0px 0px;
}
.box-visibility-right {
    float: center;
    margin: 0 20px 20px 0;
}

</style>
<script>
jQuery(document).ready(function(){
	jQuery(".selectjs").select2();
 });
</script>
<script type="text/javascript" src="js/canvasjs.min.js"></script>
<script src="js/functions.js"></script>
	<script type="text/javascript">
	window.onload = function () {

      var dps = []; 
      var brandname=$("#brandname").val();
      var totalLocationCount=$("#totalLocationCount").val();
      totalLocationCount=totalLocationCount/4;
      totalLocationCount=Math.round(totalLocationCount);
      $.ajax({
			type : "get",
			url : "getCitationGraphInfo.htm",
			data:{
				brandname:brandname,
			},
			cache : false,
			success : function(response) {
				//alert(response);
				var graph= $.parseJSON(response);


			$(graph).each(function(i,client){
				//alert(i);
				//alert(client.day);
		      	dps.push({x: new Date(client.year,client.month,client.day),
		      		y: client.citationCount});
		      	//alert(dps);
		      
		      	if (graph.length >  10 )
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
    	  backgroundColor: "#E3E5E8",
      	title :{
      		text: "Total Citations "
      	},
       
    	axisX: {
    		gridThickness: 1,
    		 interval:0,
    		valueFormatString: "MM/DD"
      	},
      	
      	axisY: {
      		 lineThickness: 1,
      		 gridThickness: 0,
      		interval:totalLocationCount,
      	},
      	data: [{
      		lineThickness: 2,
      		markerSize: 20,
      		markerBorderThickness: 3,
      		markerBorderColor : "#0E0736",
      		markerColor: "white",
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

$(document).ready(function() {
	
	
	$("select#storevalue").change(function() {
		var brandname=$('#brandname').val();
		//alert(brandname);
	
		var store = $("#storevalue").val();
	 document.location.href = "reportscitation.htm?store="+store+"&&brandname="+brandname;
	});
	
	
	
});
function getPdfCitationReport(){
	var brandname=$('#brandname').val();
	//alert(brandname);
var store = $("#storeName").val();
//alert(store);
	document.location.href =  "citationPdfReport.htm?store="+store+"&&brand="+brandname;
}

function  clearFilter() {
	var brandname=$('#brandname').val();
	document.location.href = "reports-citation.htm?brand="+brandname;	
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
			<li class="si_business_listings"><a href="business-listings.htm">Business Listings</a></li>
			<li class="si_error_listings"><a href="listing-error.htm">Listing Errors</a></li>
			<li class="si_upload_export"><a href="upload-export.htm">Upload/Export</a></li>
			<li class="si_reports selected"><a href="reports.htm">Reports</a></li>
				<%
						Integer Role=(Integer)session.getAttribute("roleId");				
							if(Role==LBLConstants.CONVERGENT_MOBILE_ADMIN){						
						%>
			<li class="si_admin"><a href="admin-listings.htm">CM admin</a></li>
			<li class="si_mobile_profile "><a href="manage-account.htm">Manage Account</a></li>
			<%} %>
			<!-- <li class="si_schduler"><a href="scheduler.htm">Schedule</a></li> -->
			
				
			<!--<li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent Toolbox</a></li>-->
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
				<a href="citationreportxls.htm" class="ico_xls">XLS</a>
				<a href="#" class="ico_pdf" onclick="getPdfCitationReport()">PDF</a>
				<p>Convergent Mobile</p>
			</div>
			<!-- // subheader -->
			<div class="inner_box"> 
				<!-- box -->
				<div class="box box_red_title report_citations"> 
					<!-- title -->
					<div class="box_title">
						<h2>Citations Report</h2>
						<div class="box_title_right btr_2">
							<form class="box_title_form">
								<a href="#" class="lnk" onclick="clearFilter()" style="float:left">Clear Filter</a>
								<select name="store"  id="storevalue"  class="selectjs" style="width:150px; margin: 0px -15px 0px;">
											

												 <option value="" style="font-size: 10px">Select Store</option>
										
											<core:forEach items="${liststoreNames}" var="stores">
													<option value="${stores}" >${stores}</option>
												</core:forEach> 
										</select> 
							</form>
						</div>
					</div>
					<!-- // title -->
					<div class="box_graph">
						<div class="box_graph_title">
							<%-- 	 <% pageContext.setAttribute("monthin", java.util.Calendar.getInstance().get(java.util.Calendar.MONTH)-1); %> --%> 
							<p> <jsp:useBean id="date" class="java.util.Date" />
									<fmt:formatDate value="${date}" pattern="yyyy"
										var="currentYear" />
									<fmt:formatDate value="${date}" pattern="MMMM"
										var="currentMonth" />
									<fmt:formatDate value="${date}" pattern="MM" var="Month" />
									<fmt:formatDate value="${date}" pattern="yyyy" var="Year" />
									
									<core:set value="${ Month-1}" var="NextMonth"></core:set>
									<core:set value="${ Year-1}" var="NextYear"></core:set>
									
							<jsp:useBean id="monthNames" class="java.text.DateFormatSymbols" />
							<core:set value="${monthNames.months}" var="months" />
									<core:out value="${months[NextMonth-1]} ${NextYear}-${months[Month-1]} ${currentYear}" /><span>${brandname}</span></p>
							<span>Total Citations: ${totalLocationCount}</span>
							<input type="hidden" value="${totalLocationCount}" id="totalLocationCount">
								<input type="hidden" value="${storeName}" id="storeName">
						</div>
					<div id="chartContainer" style="height: 350px; width: 100%;"
								class=""></div>
					</div>
					<table width="100%" class="grid grid_17">
						<colgroup>
							<col width="9%"  />
								<col width="14%" />
								<col width="14%" />
								<col width="9%" />
								<col width="10%" />
								<col width="14%" />
								<col width="20%" />
								<col width="15%" />
						</colgroup>
						<thead>
							<tr>
									<th class="th_01"><div>Store #</div></th>
								<th class="th_02"><div>Business Name</div></th>
								<th class="th_02"><div>Address</div></th>
								<th class="th_02"><div>State</div></th>
								<th class="th_02"><div>Zip Code</div></th>
								<th class="th_02"><div>Phone Number</div></th>
								<th class="th_05"><div>Citations URL</div></th>
								<th class="th_06"><div>Domain Authority</div></th>
							</tr>
						</thead>
					</table>
					<div id="id_citation_report">
						<table width="100%" class="grid grid_17">
							<colgroup>
								<col width="9%"  />
								<col width="14%" />
								<col width="14%" />
								<col width="9%" />
								<col width="10%" />
								<col width="14%" />
								<col width="20%" />
								<col width="15%" />
							</colgroup>
							<tbody>
							<input type="hidden" name="brandname" value="${brandname}" id="brandname"> 
							<core:forEach items="${citationinfo}" var="bean" varStatus="i" >
								<core:set value="${i.count}" var="index" scope="page"></core:set>
								<core:set var="className" value=""></core:set>
									
								<tr class="odd ">
									<td class="td_01"><div>
									<core:out value="${bean.store}"></core:out></div></td>
									<td class="td_02"><div><core:out value="${bean.businesName}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${bean.address}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${bean.state}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${bean.zip}"></core:out></div></td>
									<td class="td_04"><div><core:out value="${bean.phone}"></core:out></div></td>
									<core:if test="${empty StoreUrllistvalue }">
									<td class="td_05"><div><core:out value="${bean.path}"></core:out></div></td>
									
									</core:if>
								
									<core:forEach items="${StoreUrllistvalue }" var="storeurls" begin="0" end="0">
									<core:set var="storeurl1"
											value="${fn:substring(storeurls, 0, 30)}"></core:set>
									<td class="td_05"><div><a href="${storeurls}" target="_blank">${storeurl1}</a></div></td>
									</core:forEach>
									
									
									<core:forEach items="${dAValues }" var="domainsValues" begin="0" end="0" >
									<td class="td_06"><div><core:out value="${domainsValues}"></core:out></div></td>
									</core:forEach>
									
									
								</tr>
								</core:forEach>
									<core:forEach items="${StoreUrllistvalue}" var="storeurls" varStatus="j" begin="1">
								<core:set value="${j.count}" var="val" scope="page"></core:set>
								<core:set var="className" value=""></core:set>
									
								<tr class="odd ">
									<td class="line"><div>
									</div></td>
									<td class="line" ><div><core:out value=""></core:out></div></td>
									<td class="line"><div><core:out value=""></core:out></div></td>
									<td class="line"><div><core:out value=""></core:out></div></td>
									<td class="line"><div><core:out value=""></core:out></div></td>
									<td class="line"><div><core:out value=""></core:out></div></td>
									
									<core:set var="storeurl1"
											value="${fn:substring(storeurls, 0, 30)}"></core:set>
									<td class="td_05"><div><a href="${storeurls}" target="_blank">${storeurl1}</a></div></td>
									
									
									<td class="line"><div>${dAValues[j.index]}</div></td>
								</tr>
								</core:forEach>
							
							</tbody>
						</table>
					</div>
					<div class="box-legend">
						 	<span style="text-align: left;" id="loadmore1"> 
						 	
						 	<core:if
									test="${mode eq 'mode' }">
									<span id="loadmore" onclick="loadmorestores()" style="color: red; margin-right:43px;float: left;font-size: 15px">Load More</span>
								
								</core:if>
								</span>
							
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