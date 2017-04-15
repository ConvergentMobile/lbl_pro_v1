<%@page import="java.util.Calendar"%>
<%@page import="com.business.common.util.LBLConstants"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
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
			url : "getClientCitationGraphInfo.htm",
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
    		valueFormatString: "DD/MMM"
    			
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
	 document.location.href = "clientreportscitation.htm?store="+store+"&&brandname="+brandname;
	});
	$("#loadmore").mouseover(function() {
		$("#loadmore").css({"color":"green"});
	});
	$("#loadmore").mouseout(function() {
		$("#loadmore").css({"color":"red"});
	});	
	
	
});
function loadmorestores() {
	var brandname=$('#brandname').val();
	//alert(brandname);

	document.location.href =  "clientloadmore-reports-citation.htm?brand="+brandname;
	}

function  clearFilter() {
	var brandname=$('#brandname').val();
	document.location.href = "clientreports-citation.htm?brand="+brandname;	
}
</script>
</head>

<body id="office">
<!-- page wrapper -->
<div class="wrapper"> 
	<!-- header -->
	<div class="header">
			<a href="dashboardClient.htm" class="logo_lbl">Local Business Listings</a>
		<ul class="top_nav">
			<li class="home"><a href="dashboardClient.htm">Home</a></li>
			<li class="help"><a href="clientHelp.htm">Help</a></li>
			<li class="signout"><a href="logout.htm">Signout</a></li>
			<li class="welcome">
				<p>Hello, <br>
					${user}</p>
			</li>
		</ul>
		</div>
	<!-- // header --> 
	<!-- content wrapper -->
	<div class="content_wrapper no-sidebar"> 
		<!-- left side navigation -->
	<ul class="ul_left_nav">
			<li class="si_dashboard"><a href="dashboardClient.htm">Dashboard</a></li>
			<!-- <li class="si_business_listings"><a href="clientbusinesslisting.htm">Business Listings</a></li> -->
			<li class="si_error_listings"><a href="listingClient-error.htm">Listing Errors</a></li>
			<li class="si_reports selected"><a href="client-reports.htm">Reports</a></li>
			<!--  <li class="si_toolbox"><a href="clntToolbox.htm">Convergent Toolbox</a></li>-->
		</ul>
		<!-- // left side navigation --> 
		<!-- content area -->
		<div class="content" id="id_content">
			<div class="nav_pointer pos_01"></div>
			<!-- subheader -->
			<div class="subheader clearfix">
				<h1>REPORTS</h1>
				<a href="client-reports.htm" class="back">< Back to Reporting Criteria</a>
				<span>Export Report</span>
				<!-- <a href="citationreportpdf.htm" class="ico_pdf">PDF</a> -->
				<a href="clientcitationreportxls.htm" class="ico_xls">XLS</a>
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
							<a href="#" onclick="clearFilter()" class="lnk">Clear Filter</a>
								<select name="store"  id="storevalue"  class="selectjs" style="width:150px; margin: 0px -15px 0px;">
											

												 <option value="" style="font-size: 10px">Select Store</option>
										
											<core:forEach items="${liststoreNames}" var="stores">
													<option value="${stores}" >${stores}</option>
												</core:forEach> 
										</select> 
								<!-- <label>from</label><input type="text"><a href=""></a>
								<label>to</label><input type="text"><a href=""></a>	 -->		
								<!-- <a class="btr_button" href="#">View Results</a> -->
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
						</div>
						<div id="chartContainer" style="height: 350px; width: 60%;margin: 0px 207px 0px;"
								class="box-visibility-right">
								<!-- <p class="title"></p>
							<img src="images/graph-02.png"> -->
								<!-- <div  >
  								</div> -->

							</div>
					</div>
					<table width="100%" class="grid grid_16">
						<colgroup>
							<col width="10%"  />
							<col width="15%" />
							<col width="22%" />
							<col width="10%" />
							<col width="10%" />
							<col width="15%" />
							<col width="17%" />
							
						</colgroup>
						<thead>
							<tr>
								<th class="th_01"><div>Store #</div></th>
								<th class="th_02"><div>Business Name</div></th>
								<th class="th_02"><div>Address</div></th>
								<th class="th_02"><div>State</div></th>
								<th class="th_02"><div>Zip Code</div></th>
								<th class="th_02"><div>Phone Number</div></th>
								<th class="th_05"><div>Citations by Location</div></th>
								
							</tr>
						</thead>
					</table>
					<div id="id_citation_report">
						<table width="100%" class="grid grid_16">
							<colgroup>
							<col width="10%"  />
							<col width="15%" />
							<col width="22%" />
							<col width="10%" />
							<col width="10%" />
							<col width="15%" />
							<col width="17%" />
							
							</colgroup>
							<tbody>
							<input type="hidden" name="brandname" value="${brandname}" id="brandname"> 
							<core:forEach items="${citationinfo}" var="bean" varStatus="i">
								<core:set value="${i.count}" var="index" scope="page"></core:set>
								<core:set var="className" value=""></core:set>
									<core:if test="${index gt 10 }">
									<core:set var="className" value="dsp_None"></core:set>
									</core:if>                         
								<tr class="odd ">
									<td class="td_01"><div>
									<a href="<core:url value="clientreportbyStorecitation.htm?store=${bean.store}&&brandname=${brandname}"/>">${bean.store}</a></div></td>
									<td class="td_02"><div><core:out value="${bean.businesName}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${bean.address}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${bean.state}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${bean.zip}"></core:out></div></td>
									<td class="td_04"><div><core:out value="${bean.phone}"></core:out></div></td>
									
									<td class="td_05"><div>	<a href="<core:url value="clientreportbyStorecitation.htm?store=${bean.store}&&brandname=${brandname}"/>">${bean.pathCount}</a></div></td>
								
									
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
<div class="loading dsp_None">
			<img src="images/loading.gif" /> <br />
			 loading!
		</div>
</body>
</html>