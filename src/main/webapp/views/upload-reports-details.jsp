<%@page import="com.business.common.util.LBLConstants"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0">
<title>Local Business Listings | Reports</title>
<link rel="stylesheet" href="css/global.css">
<link rel="stylesheet" href="fonts/fonts.css">
<link rel="stylesheet" href="css/navigation.css">
<link rel="stylesheet" href="css/common.css">
<link rel="stylesheet" href="css/grids.css">
<link rel="stylesheet" href="css/sidebar.css">
<link rel="stylesheet" href="js/jquery.mCustomScrollbar.css">
<script src="js/jquery.mCustomScrollbar.js"></script>
<link rel="stylesheet" href="css/jquery-ui.css">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>

<script src="js/jquery.idTabs.min.js"></script>
<script src="js/jquery.slimscroll.min.js"></script>
<script src="js/jquery.screwdefaultbuttonsV2.js"></script>
<script src="js/jquery.cycle.all.js"></script>
<script src="js/functions.js"></script>

</head>

<script type="text/javascript">
	 $(document).ready(function() {
		
	});
	 
	 $(function(){
		    $('#id_report_01').slimScroll({
		        height: '530px'
		    });
		});

     	$(function() {
	    $( "#startDate" ).datepicker({
		'format': 'm/d/yyyy',
		'autoclose': true    
	    });

	});

     	$(function() {
	    $( "#endDate" ).datepicker({
		'format': 'm/d/yyyy',
		'autoclose': true    
	    });

	});
	
	function getParams() {
		var selRptId = $("#report\\.id").val();
		document.location.href = "reports.htm?reportId="+selRptId;		
	}
	
	function runIt() {
		var form = document.getElementById("reportsForm");	

		form.action = 'runReport.htm?page=1';
		form.method = 'POST';			
		form.submit();		
	}
	
	function getUploadRepostDetails(id){
		alert(id);
		id = "tr_"+id;
		var brand = $("tr#"+id+" > td#brand").text();
		var date = $("tr#"+id+" > td#date").text();
		alert(brand + " :: "+date);
		document.location.href="getUploadReportDetails.htm?brand="+brand+"&date="+date;
	}

 </script>
 
<body id="office">
<!-- page wrapper -->
<div class="wrapper"> 
	<!-- header -->
	<div class="header"> <a href="dash-board.htm" class="logo_lbl">Local Business Listings</a>
		<ul class="top_nav">
			<li class="home"><a href="dash-board.htm">Home</a></li>
			<li class="help"><a href="#">Help</a></li>
			<li class="signout"><a href="logout.htm">Signout</a></li>
			<li class="welcome">
				<p>Hello, <br>
					${userName}</p>
			</li>
		</ul>
	</div>
	<!-- // header --> 
	<!-- content wrapper -->
	<div class="content_wrapper"> 
		<!-- left side navigation -->
		<ul class="ul_left_nav">
			<li class="si_dashboard"><a href="dash-board.htm">Dashboard</a></li>
			<li class="si_business_listings "><a href="business-listings.htm">Business Listings</a></li>
			<li class="si_upload_export"><a href="upload-export.htm">Upload/Export</a></li>
			<li class="si_reports selected"><a href="reports.htm">Reports</a></li>
				<%
						Integer Role=(Integer)session.getAttribute("roleId");				
							if(Role==LBLConstants.CONVERGENT_MOBILE_ADMIN){						
						%>
			<li class="si_admin"><a href="admin-listings.htm">CM admin</a></li>
				<li class="si_mobile_profile "><a href="manage-account.htm">Manage Account</a></li>
			<%} %>
<!-- 			<li class="si_schduler"><a href="scheduler.htm">Schedule</a></li> -->
		
				
			<!--<li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent Toolbox</a></li>-->
		</ul>
		<!-- // left side navigation --> 
		<!-- content area -->
		<div class="content" id="id_content">
    	<div class="nav_pointer pos_01"></div>
		  <!-- subheader -->
		  <div class="subheader clearfix">
			<h1>REPORTS</h1>
			<p>${channelName}</p>
		  </div>
		  <!-- // subheader -->
		  <table width="100%" class="grid grid_13">
						<colgroup>
							<col width="15%" />
							<col width="15%" />
							<col width="12%" />
							<col width="10%" />
							<col width="10%" />
							<col width="11%" />
						</colgroup>
						<thead id="businessTableHeaders">
							<tr>
								<th class="th_04"><div>Business Name</div></th>
								<th class="th_05"><div>Address</div></th>
								<th class="th_06"><div>City</div></th>
								<th class="th_07"><div>State</div></th>
								<th class="th_08"><div>Zip</div></th>
								<th class="th_09"><div>Phone</div></th>
							</tr>
						</thead>
					</table>
					<div id="id_business_listings">
						<table  id="businessTable" width="100%" class="grid grid_13">
							<colgroup>
								<col width="15%" />
								<col width="15%" />
								<col width="12%" />
								<col width="10%" />
								<col width="10%" />
								<col width="11%" />
							</colgroup>
							<tbody id="businessInfo">
						<core:forEach items="${uploadReports}" var="bean" varStatus="i">
								<tr class="odd">
									<td class="td_04"><div><core:out value="${bean.companyName}"></core:out></div></td>
									<td class="td_05"><div><core:out value="${bean.locationAddress}"></core:out></div></td>
									<td class="td_06"><div><core:out value="${bean.locationCity}"></core:out></div></td>
									<td class="td_07"><div><core:out value="${bean.locationState}"></core:out></div></td>
									<td class="td_08"><div><core:out value="${bean.locationZipCode}"></core:out></div></td>
									<td class="td_09"><div><core:out value="${bean.locationPhone}"></core:out></div></td>
								</tr>
						</core:forEach>
						</tbody>
						</table>
						</div>
					
      </div>      
    </div>
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