<%@page import="com.business.common.util.LBLConstants"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


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
<link rel="stylesheet" href="css/bpopUp.css">
<link rel="stylesheet" href="css/jquery-ui.css">
 <link rel="stylesheet" href="css/style.css">
 

<script
	src="js/jquery-1.4.4.min.js"></script>

  <link rel="stylesheet" href="css/prism.css">
  <link rel="stylesheet" href="css/chosen.css">

<script src="js/jquery.idTabs.min.js"></script>
<script src="js/jquery.slimscroll.min.js"></script>
<script src="js/jquery.screwdefaultbuttonsV2.js"></script>
<script src="js/jquery.cycle.all.js"></script>
<script src="js/functions.js"></script>
<script src="js/jquery.bpopup.min.js"></script>
<link rel="stylesheet" href="js/jquery.mCustomScrollbar.css">
<script src="js/jquery.mCustomScrollbar.js"></script>
<link rel="stylesheet" href="js/select2.css">
<script type="text/javascript" src="js/canvasjs.min.js"></script>
<script src="js/select2.js"></script>
<script>
jQuery(document).ready(function(){
	jQuery(".selectjs").select2();
 });
</script>
<script src="js/functions.js"></script>
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
				<li class="si_business_listings "><a
					href="business-listings.htm">Business Listings</a></li>
				<li class="si_error_listings"><a href="listing-error.htm">Listing
						Errors</a></li>
				<li class="si_upload_export"><a href="upload-export.htm">Upload/Export</a></li>
				<li class="si_reports selected"><a href="reports.htm">Reports</a></li>
					<%
						Integer Role=(Integer)session.getAttribute("roleId");				
							if(Role==LBLConstants.CONVERGENT_MOBILE_ADMIN){						
						%>
			<li class="si_admin"><a href="admin-listings.htm">CM admin</a></li>
			<li class="si_mobile_profile "><a href="manage-account.htm">Manage Account</a></li>
			<%} %>
		<!-- 	<li class="si_schduler"><a href="scheduler.htm">Schedule</a></li> -->
			
				
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
			<!-- 	<a href="" class="ico_pdf">PDF</a> -->
				<a href="summaryReportDownload.htm" class="ico_xls">XLS</a>
				<p>Convergent Mobile</p>
			</div>
			<!-- // subheader -->
			<div class="inner_box"> 
				<!-- box -->
				<div class="box box_red_title renewal"> 
					<!-- title -->
					<div class="box_title">
						<h2>Summary Report</h2>
					</div>
					<!-- // title -->
					<table width="100%" class="grid grid_23">
						<colgroup>
							<col width="15%" />
							<col width="16%" />
							<col width="17%" />
							<col width="13%" />
							<col width="13%" />
							<col width="13%" />
							<col width="13%" />
						</colgroup>
						<thead>
							<tr>
								<th><div>Store # (Listing ID)</div></th>
								<th><div>Brand</div></th>
								<th><div>Business Name</div></th>
								<th class="th_01"><div><img src="images/logo-acxiom.png"></div></th>
								<th class="th_01"><div><img src="images/logo-factual.png"></div></th>
								<th class="th_01"><div><img src="images/logo-infogroup.png"></div></th>
								<th class="th_01"><div><img src="images/logo-neustar.png"></div></th>
							</tr>
						</thead>
					</table>
					<div id="id_report_renewal">
						<table width="100%" class="grid grid_23">
							<colgroup>
								<col width="15%" />
								<col width="16%" />
								<col width="17%" />
								<col width="13%" />
								<col width="13%" />
								<col width="13%" />
								<col width="13%" />
							</colgroup>
							<tbody>
							<core:forEach items="${summaryReports }" var="client">
							<tr class="odd">
									<td><div>${client.store}</div></td>
									<td><div>${client.brandName}</div></td>
									<td><div>${client.businessName}</div></td>
									<fmt:formatDate value="${client.acxiomRenewalDate}" pattern="MM/dd/yyyy" var="acxiomRenewalDate"/>
									<td class="td_01"><div>${acxiomRenewalDate}</div></td>
									<fmt:formatDate value="${client.factualRenewalDate}" pattern="MM/dd/yyyy" var="factualRenewalDate"/>
									<td class="td_01"><div>${factualRenewalDate}</div></td>
									<fmt:formatDate value="${client.infogroupRenewalDate}" pattern="MM/dd/yyyy" var="infogroupRenewalDate"/>
									<td class="td_01"><div>${infogroupRenewalDate}</div></td>
									<fmt:formatDate value="${client.localezeRenewalDate}" pattern="MM/dd/yyyy" var="localezeRenewalDate"/>
									<td class="td_01"><div>${localezeRenewalDate}</div></td>
								</tr>
								
							</core:forEach>
								

								
							</tbody>
						</table>
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