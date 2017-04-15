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
<link rel="stylesheet" href="rcss/global.css">
<link rel="stylesheet" href="rfonts/fonts.css">
<link rel="stylesheet" href="rcss/navigation.css">
<link rel="stylesheet" href="rcss/common.css">
<link rel="stylesheet" href="rcss/grids.css">
<link rel="stylesheet" href="rcss/sidebar.css">
<script src="rjs/jquery-1.9.1.min.js"></script>
<script src="rjs/jquery.idTabs.min.js"></script>
<script src="rjs/jquery.slimscroll.min.js"></script>
<script src="rjs/jquery.screwdefaultbuttonsV2.js"></script>
<script src="rjs/jquery.cycle.all.js"></script>
<script src="rjs/functions.js"></script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script src="rjs/charts4.js"></script>
<script src="rjs/highcharts.js"></script>
<script src="rjs/highcharts-3d.js"></script>
<link rel="stylesheet" href="js/select2.css">
<script src="js/select2.js"></script>
<script src="js/select2.min.js"></script>
</head>
<script>
jQuery(document).ready(function(){
	jQuery(".selectjs").select2();
 });
function getStores(){
	var store = $("#storevalue").val();
	var brandname=$('#brandname').val();
	document.location.href = "runCitaionStoreReport.htm?store="+store+"&&brandname="+brandname;
}
</script>
<style type="text/css">
tspan{
font:15px/14px OpenSansRegular, Arial, Helvetica, sans-serif;

}
text{
font-size: 16px;
}
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

.box_title .box_title_right{
	float: right;
	padding: 0 20px;
	height: 51px;
	color: #fff;
	line-height: 51px;
	text-shadow: 0 1px #a01d0b;
	background: url(../images/bg_01.png) left no-repeat #bd220d;
	border-radius: 0 3px 0 0;
	}
</style>
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
				<a href="#" class="ico_xls">XLS</a>
				<p>Convergent Mobile</p>
			</div>
			<!-- // subheader -->
			<div class="inner_box"> 
				<!-- box -->
				<div class="box box_red_title report_accuracy"> 
					<!-- title -->
					<div class="box_title">
						<h2>LISTING CITATION</h2>
						<p>${brandname}</p>
						<div class="box_title_right">
							<form class="box_title_form">
								<select name="store"  id="storevalue"  class="selectjs" style="width:150px; margin: 0px -15px 0px;" onchange="getStores()">
											

												 <option value="" style="font-size: 10px">Select Store</option>
										
											<core:forEach items="${liststoreNames}" var="stores">
													<option value="${stores}" >${stores}</option>
												</core:forEach> 
										</select> 
											<input type="hidden" name="store" id="store"
								value="${storeName}" />
						<input type="hidden" name="brandname" value="${brandname}" id="brandname"> 
							</form>
						</div>
					</div>
					<!-- // title -->
					<table class="listing_cituation">
						<tr>
							<th class="th_01">LBL Pro Address<span>Store #${storeName}</span></th>
							<core:forEach items="${businessDTOs }" var="bean">
							<th class="th_02">${bean.locationAddress }, ${bean.locationState } ${bean.locationZipCode } ${bean.locationPhone }<br>${bean.webAddress }</th>
							
							</core:forEach>
							
						</tr>
						<tr>
							<td class="td_01" width="50%">
								<h1>Store Citation</h1>
								<div id="container"></div>
							</td>
							<td class="td_02" width="50%">
								<h1>Brand Citations</h1>
								<div id="container2"></div>
							</td>
						</tr>
					</table>
					<table class="listing_cituation_bot">
						<colgroup>
							<col width="50%"></col>
							<col width="25%"></col>
							<col width="25%"></col>
						</colgroup>
						<tr>
							<th>Sources</th>
							<th>Citation URL™</th>
							<th>Domain Authority</th>
						</tr>
					
					

							<core:set value="${StoreUrllistvalue.size()}" var="StoreUrllistvalues"></core:set>
					
						<core:choose>
						<core:when test="${StoreUrllistvalues  ==0}">
								<tr>
							<td class="td_01" style=" height: 12px"></td>
						<td class="td_01" style=" height: 12px"></td>
							<td class="td_01" style=" height: 12px"></td>
						</tr>
						<tr>
							<td class="td_01" style=" height: 12px"></td>
						<td class="td_01" style=" height: 12px"></td>
							<td class="td_01" style=" height: 12px"></td>
						</tr>
						<tr>
							<td class="td_01" style=" height: 12px"></td>
						<td class="td_01" style=" height: 12px"></td>
							<td class="td_01" style=" height: 12px"></td>
						</tr>
						<tr>
							<td class="td_01" style=" height: 12px"></td>
						<td class="td_01" style=" height: 12px"></td>
							<td class="td_01" style=" height: 12px"></td>
						</tr>
						<tr>
						<td class="td_01" style=" height: 12px"></td>
						<td class="td_01" style=" height: 12px"></td>
							<td class="td_01" style=" height: 12px"></td>
						</tr>
						<tr>
						<td class="td_01" style=" height: 12px"></td>
						<td class="td_01" style=" height: 12px"></td>
							<td class="td_01" style=" height: 12px"></td>
						</tr>
						<tr>
							<td class="td_01" style=" height: 12px"></td>
						<td class="td_01" style=" height: 12px"></td>
							<td class="td_01" style=" height: 12px"></td>
						</tr>
						<tr>
								<td class="td_01" style=" height: 12px"></td>
						<td class="td_01" style=" height: 12px"></td>
							<td class="td_01" style=" height: 12px"></td>
						</tr>
						<tr>
						<td class="td_01" style=" height: 12px"></td>
						<td class="td_01" style=" height: 12px"></td>
							<td class="td_01" style=" height: 12px"></td>
						</tr>
						<tr>
							<td class="td_01" style=" height: 12px"></td>
						<td class="td_01" style=" height: 12px"></td>
							<td class="td_01" style=" height: 12px"></td>
						</tr>
					

						</core:when>
						<core:otherwise>
						
						<core:forEach items="${citationinfo}" var="bean" varStatus="i" >
								<core:set value="${i.count}" var="index" scope="page"></core:set>
								<core:set var="className" value=""></core:set>
									
								<tr class="odd ">
									
									
									

									<core:if test="${empty StoreUrllistvalue }">
									<td class="td_01"><div>
									<core:out value="${bean.paths}"></core:out></div></td>
									<td class="td_05"><div><core:out value="${bean.paths}"></core:out></div></td>
									
									</core:if>
								
									<core:forEach items="${StoreUrllistvalue }" var="storeurls" begin="0" end="0">
									<core:set var="storeurl1"
											value="${fn:substring(storeurls, 0, 30)}"></core:set>
											<td class="td_01"><div>
									<core:out value="${storeurls.value}"></core:out></div></td>
									<td class="td_05"><div><a href="${storeurls.key}" target="_blank">${storeurl1}</a></div></td>
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
								

							<core:set var="storeurl1"
											value="${fn:substring(storeurls, 0, 30)}"></core:set>
											<td class="td_01"><div>
									<core:out value="${storeurls.value}"></core:out></div></td>
									<td class="td_05"><div><a href="${storeurls.key}" target="_blank">${storeurl1}</a></div></td>
									
									
									<td class="line"><div>${dAValues[j.index]}</div></td>
								</tr>
								</core:forEach>
								

						
						</core:otherwise>
						
						
						</core:choose>

					
					</table>
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