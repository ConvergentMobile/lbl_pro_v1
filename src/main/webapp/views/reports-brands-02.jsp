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
<script src="rjs/charts2.js"></script>
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
<script type="text/javascript">
function getStores(){
	var store = $("#storeval").val();
	var brandname=$('#brandname').val();
	document.location.href = "runVisiblityReport.htm?store="+store+"&&brandname="+brandname;
}
function runaccuracyreport() {
	
	var brandname=$('#brandname').val();
	
	
			  document.location.href = "reports-accuracy.htm?brand="+brandname;	
			
		 
		
}
</script>
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
					<a href="#" class="back" onclick="runaccuracyreport()">< Back to Accuracy Reporting</a>
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
						<h2>BRAND VISABILITY Report</h2>
						<p>${brandname}</p>
						<div class="box_title_right">
						<input type="hidden" name="brandname" id="brandname"
								value="${brandname}" />
								<input type="hidden" name="store" id="store"
								value="${store}" />
										<input type="hidden" name="totalPercentage" id="totalPercentage"
								value="${totalPercentage}" />
							<form class="box_title_form">
								<select name="store" id="storeval" class="selectjs" style="width: 150px;" onchange="getStores()">
									<option value="">Select Store</option>

										<core:forEach items="${liststoreNames}" var="stores">
											<option value="${stores}">${stores}</option>
										</core:forEach>
								</select>
								

							</form>
						</div>
					</div>
					<!-- // title -->
					<table class="brand_report white">
						<tr>
								<core:set var="test" value="${fn:length(liststoreNames)}"></core:set>
							<th colspan="2" class="th_01">TOTAL LOCATIONS UPLOADED IN LBL PRO <span>${test }</span></th>
							<th class="th_02">Aggregators</th>
						</tr>
						<tr>
							<td class="td_01">
								<h1>Total Listings Found</h1>
								<span>${totalPercentage }%</span>
								<div id="container"></div>
							</td>
							<td class="td_02">
								<h1>Total Listings</h1>
								<div id="container13"></div>
							</td>
							<td class="td_03">
								<table>
									<tr>
										<td><img src="images/infogroup.png"><p>Coming Soom.</p></td>
										<td><img src="images/neustar.png"><p>Coming Soom.</p></td>
									</tr>
									<tr>
										<td><img src="images/acxiom.png"><p>Coming Soom.</p></td>
										<td><img src="images/factual.png"><p>Coming Soom.</p></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
				<!-- // box --> 

				<div class="box"> 

					<table class="brand_report_all gray_border">
						<tr>
							<td>
								<img src="images/google.png">
								 <span>${googleErrors}%</span>
								<div id="container2"></div>
							</td>
							<td>
								<img src="images/logo1-foursquare.png">
								<span>${bingErrors}%</span>
								<div id="container3"></div>
							</td>
							<td>
								<img src="images/yahoo.png">
								<span>${YahooErrors}%</span>
								<div id="container4"></div>
							</td>
							<td>
								<img src="images/yelp.png">
								<span>${yelpErrors}%</span>
								<div id="container5"></div>
							</td>
							<td>
								<img src="images/mapquest.png">
								<span>${mapquestErrors}%</span>
								<div id="container6"></div>
							</td>
						</tr>
						<tr>
							<td>
								<img src="images/yp.png">
								<span>${ypErrors}%</span>
								<div id="container7"></div>
							</td>
							<td>
								<img src="images/logo1-usplaces.png">
								<span>${superpagesErrors}%</span>
								<div id="container8"></div>
							</td>
							<td>
								<img src="images/citysearch.png">
								<span>${citysearchErrors}%</span>
								<div id="container9"></div>
							</td>
							<td>
								<img src="images/logo1-yellowbot.png">
								<span>${yellobookErrors}%</span>
								<div id="container10"></div>
							</td>
							<td>
								<img src="images/logo1-ezlocal.png">
								<span>${whitepagesErrors}%</span>
								<div id="container11"></div>
							</td>
						</tr>
					</table>

				</div>
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