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
<title>Local Business Listings | Reports</title>
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
	src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
  <link rel="stylesheet" href="css/prism.css">
  <link rel="stylesheet" href="css/chosen.css">
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
<script type="text/javascript" src="js/canvasjs.min.js"></script>
<script src="js/select2.js"></script>
<script>
	jQuery(document).ready(function() {
		jQuery(".selectjs").select2();
	});
</script>
<style type="text/css">

.grid_07 th {
  font-weight: bold;
  text-transform: inherit;
  text-align: left;
  vertical-align: top;
  text-shadow: #f6f7f8 0 1px 0;
  color: #4a555b;
  border-bottom: #b4babd 1px solid;
  background: #edeeef url(../images/th_grid_keyword.png) 0 0 repeat-x;
}
</style>
<script src="js/jquery.session.js"></script>

  <style type="text/css" media="all">
    /* fix rtl for demo */
    .chosen-rtl .chosen-drop { left: -9000px; }
  </style>
  <style type="text/css">
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
        margin: 22px -4px 19px;
}
  </style>


<script type="text/javascript">

	 $(document).ready(function() {

		

		
		if ($.session.get("isUploadReport") === "true") {
			$.session.set("isUploadReport",false);
			$('#upload').bPopup();
			$("ul li").removeClass("selected");
		}
		
		var selRptId = $("#report\\.id").val();
		
		if(selRptId == 2 || selRptId == 3){
			$("#branddrop").hide();
		}else{
			$("#branddrop").show();
		} 

		
			
		
		
		if(selRptId == 1 ||selRptId == 4 ||selRptId == 2 || selRptId == 3){
			$("#listingDrop").hide();
		}else{
			$("#listingDrop").show();
		} 

		if(selRptId==5){

			$("#label").show();
			$("#brandidvalue1").show();
			$("#runReport").hide();
			$("#runChangeTrackingReport").show();
			$("#clearsearch").show();
			$("#searchbutton").show();


		}

					if(selRptId==6){
						$("#storeDrop").show();
						$("#listingDrop").hide();
						$("#branddrop").show();
						$("#clientName").show();
						$("#startdate").hide();
						$("#runReport").hide();
						$("#enddate").hide();
						//$("#brandidvalue1").show();
						
						//$("#clearsearch").show();
					//$("#searchbutton").show();
						$("#runaccuracyReport").hide();
						$("#runcheckReport").show();


					}
					 if(selRptId==7){


						 $("#storeDrop").hide();
							$("#listingDrop").hide();
							$("#branddrop").show();
							$("#clientName").show();
							$("#startdate").hide();
							$("#runReport").hide();
							$("#enddate").hide();
							$("#runaccuracyReport").show();
							

					 }
					
					 if(selRptId==8){

						 $("#storeDrop").hide();
							$("#listingDrop").hide();
							$("#branddrop").show();
							$("#clientName").show();
							$("#startdate").hide();
							$("#runReport").hide();
							$("#enddate").hide();
							$("#runCitationReport").show();
						


					} 
					 if(selRptId==9){

							$("#storeDrop").show();
							$("#listingDrop").hide();
							$("#branddrop").show();
							$("#clientName").show();
							$("#startdate").hide();
							$("#runReport").hide();
							$("#enddate").hide();
							//$("#brandidvalue1").show();
							$("#directoryDrop").show();
							//$("#clearsearch").show();
							//$("#searchbutton").show();
							$("#runaccuracyReport").hide();
							$("#runcheckReport").hide();
							$("#runCompareReport").show();


					} 


	
		
		
		if ($.session.get("isDistrReport") === "true") {
			$.session.set("isDistrReport",false);
			$('#distrreport').bPopup();
			$("ul li").removeClass("selected");
		}
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
     	
     	
     	 $(function(){
 		    $('.pp-body').slimScroll({
 		        height: '300px'
 		    });
 		});
	
	function getParams() {
		var selRptId = $("#report\\.id").val();
		document.location.href = "reports.htm?reportId="+selRptId;		
	}
	function runChangeTrackingReport(){
		var form = document.getElementById("reportsForm");	

		form.action = 'runCtrakingReport?page=1';
		form.method = 'POST';			
		form.submit();
	}
	function runIt() {
		var startDate=$('#startDate').val();
		
		var enddate = $("#endDate").val();
		/* var brandname=$('#clientName').val();
	
		var store = $("#storesvalue").val(); */
		//var selRptId = $("#report\\.id").val();
	
		var form = document.getElementById("reportsForm");	

		form.action = 'runReport.htm?page=1';
		form.method = 'POST';			
		form.submit();		
	}
	
	function runcheckreport() {
		var brandname=$('#clientName').val();
		//alert(brandname);
	
		var store = $("#storevalue").val();
		if(brandname == '' || store== ''){
			$("#warningpopup").bPopup();
		}else{
			document.location.href = "checkreportview.htm?store="+store+"&&brandname="+brandname;
		}
		//alert("store:::::::::::::"+store);
				
	}
	
	
	function search() {
		
	var brandname=$('#clientName').val();
	//alert(brandname);
	var store = $("#storesvalue").val();
	/* var date=$("#storesvalue").val()
		var enddate=$("#endDate").val(); */
		var selRptId = $("#report\\.id").val();
		if(selRptId == 6){
			if(brandname == '' || store== ''){
				$("#warningpopup").bPopup();
			}else{
				$.ajax({
					type : "get",
					url : "getStoreBasedOnBrandsandStore.htm",
					cache : false,
					data : {
						brandname : brandname,
						store:store
					},
					success : function(
							response) {
						//alert(response);
						
						  var stores = response.split(", ");
						  
						  var html = '<option value=""></option>';
					      var len = stores.length;
					   
					      for ( var i = 0; i < len; i++) {
					       html += '<option value="' + stores[i].replace("[","").replace("]","")+ '">'
					         + stores[i].replace("[","").replace("]","")+ '</option>'; 
					      }
					      html += '</option>';
					 
					      //$("#storeDrop").show();

		 $('#stores_list_d').html(html);
					      $('#storevalue').html(html);


					},
					/* error : function(
							response) {
						alert('Error while ..');
					} */
				});
			}
		}
		if(selRptId == 5){
			if(brandname == '' || store== ''){
				$("#warningpopup").bPopup();
			}else{
				$.ajax({
					type : "get",
					url : "getStoreBasedOnBrandsandStore.htm",
					cache : false,
					data : {
						brandname : brandname,
						store:store
					},
					success : function(
							response) {
						//alert(response);
						
						  var stores = response.split(", ");
						  
						  var html = '<option value=""></option>';
					      var len = stores.length;
					   
					      for ( var i = 0; i < len; i++) {
					       html += '<option value="' + stores[i].replace("[","").replace("]","")+ '">'
					         + stores[i].replace("[","").replace("]","")+ '</option>'; 
					      }
					      html += '</option>';
					 
					      //$("#storeDrop").show();

		 $('#stores_list_d').html(html);
					      $('#storevalue').html(html);


					},
					/* error : function(
							response) {ri
						alert('Error while ..');
					} */
				});
			}
		}

		//alert("store:::::::::::::"+store);
		//document.location.href="getStoreBasedOnBrandsandStore.htm?brandname="+brandname+"&&store="+store ;	
		
	
	}
	function runaccuracyreport() {
		var brandname=$('#clientName').val();
		if(brandname== ''){
			$("#storewarningpopup").bPopup();
		}else{
			document.location.href = "reports-accuracy.htm?brand="+brandname;	
		}
			
	}
	function runCitationreport() {
		var brandname=$('#clientName').val();
		if(brandname== ''){
			$("#storewarningpopup").bPopup();
		}else{
			document.location.href = "reports-citation.htm?brand="+brandname;	
		}
			
	}
	function runComparereport() {
		var directory=$('#directory').val();
		var brandname=$('#clientName').val();

		//alert(directory);
		var store = $("#storevalue").val();
		//alert(store);
		if(store== '' || directory== ''){
			$("#direcorywarningpopup").bPopup();
		}else{
			document.location.href = "compare-listings.htm?store="+store+"&&directory="+directory+"&&brandname="+brandname;	
		}
			
	}
	function getUploadRepostDetails(id){
		//alert(id);
		id = "tr_"+id;
		var brand = $("tr#"+id+" > td#brand").text();
		var date = $("tr#"+id+" > td#date").text();
		//alert(brand + " :: "+date);
		$.session.set("isUploadReport",true);
		document.location.href="getUploadReportDetails.htm?brand="+brand+"&date="+date;
		
	}

	function getDistrReport(id){
		//alert(id);
		id = "tr_"+id;
		var brand = $("tr#"+id+" > td#brand").text();
		var date = $("tr#"+id+" > td#date").text();
		//alert(brand + " :: "+date);
		$.session.set("isDistrReport",true);
		document.location.href="distrreport.htm?brand="+brand+"&date="+date;
		
	}
 </script>


<script type="text/javascript">

 $(document).ready(
		 function() {



		  $('#clientName').change(function() {
		    	
			  $.ajax({
				  type: "get",
				  url: "products_ajax.htm",
				  cache: false,    
				  data:'categoryId=' +  $(this).val(),
				  datatype:"json",
				  success: function(response){



					  var stores = response.split(", ");
					  
					  var html = '<option value=""></option>';
				      var len = stores.length;
				   
				      for ( var i = 0; i < len; i++) {
				       html += '<option value="' + stores[i].replace("[","").replace("]","")+ '">'
				         + stores[i].replace("[","").replace("]","")+ '</option>'; 
				      }
				      html += '</option>';
				 
				      //$("#storeDrop").show();
				      $('#stores_list_d').html(html);
				      $('#storevalue').html(html);


		    
		     },
					 /*  error: function(response){  
						  alert("error::"+response);
						   alert('Error while request..');
						  } */
		    });
 
		}); 
			 
		  
		 });

 </script>
 </head>
  <div class="popup" id="direcorywarningpopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select Store and Direcory </div>
	</div>
</div>
 <div class="popup" id="datewarningpopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select Date</div>
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


<body id="office">


	<div class="popup" id="upload"
		style="display: none; width: 60%; height: 60%; margin-left: 262px;">
		<div class="pp-header">

			<span class="buttonIndicator b-close"><span>X</span></span>
		</div>

		<div class="pp-body">

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

			<table id="businessTable" width="100%" height="60%"
				class="grid grid_13">
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
							<td class="td_04"><div>
									<core:out value="${bean.companyName}"></core:out>
								</div></td>
							<td class="td_05"><div>
									<core:out value="${bean.locationAddress}"></core:out>
								</div></td>
							<td class="td_06"><div>
									<core:out value="${bean.locationCity}"></core:out>
								</div></td>
							<td class="td_07"><div>
									<core:out value="${bean.locationState}"></core:out>
								</div></td>
							<td class="td_08"><div>
									<core:out value="${bean.locationZipCode}"></core:out>
								</div></td>
							<td class="td_09"><div>
									<core:out value="${bean.locationPhone}"></core:out>
								</div></td>
						</tr>
					</core:forEach>
				</tbody>
			</table>
		</div>
	</div>

	<div class="popup" id="distrreport"
		style="display: none; width: 60%; height: 60%; margin-left: 262px;">
		<div class="pp-header">

			<span class="buttonIndicator b-close"><span>X</span></span>
		</div>

		<div class="pp-body">

			<table width="100%" class="grid grid_13">
				<colgroup>
					<col width="18%" />
					<col width="18%" />
					<col width="17%" />
					<col width="20%" />

				</colgroup>
				<thead id="businessTableHeaders">
					<tr>
						<th class="th_04"><div>Brand/Listing</div></th>
						<th class="th_05"><div>OfficeId</div></th>
						<th class="th_06"><div>Provider</div></th>
						<th class="th_07"><div>Last Submission</div></th>

					</tr>
				</thead>
			</table>

			<table id="businessTable" width="100%" height="60%"
				class="grid grid_13">
				<colgroup>
					<col width="18%" />
					<col width="18%" />
					<col width="17%" />
					<col width="20%" />
				</colgroup>
				<tbody id="businessInfo">
					<core:forEach items="${DistributionReports}" var="bean" varStatus="i">
						<tr class="odd">
							<td class="td_04"><div>
									<core:out value="${bean.brandName}"></core:out>
								</div></td>
							<td class="td_05"><div>
									<core:out value="${bean.clientId}"></core:out>
								</div></td>
							<td class="td_06"><div>
									<core:out value="${bean.partner}"></core:out>
								</div></td>
							<td class="td_07"><div>
									<core:out value="${bean.date}"></core:out>
								</div></td>

						</tr>
					</core:forEach>
				</tbody>
			</table>
		</div>
	</div>



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
		<div class="content_wrapper">
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
			<div class="content" id="id_content" style="width:100%">
				<div class="nav_pointer pos_01"></div>
				<!-- subheader -->
				<div class="subheader clearfix">
					<h1>REPORTS</h1>
				<a href="reports.htm" class="back"> Back to Reporting Criteria</a>
					<p>${channelName}</p>
				</div>
				<!-- // subheader -->
				<spring:form method="post" modelAttribute="reportForm" id="reportsForm">

					<div class="inner_box">
						<!-- box -->
						<div class="box box_red_title box_report">
							<!-- title -->
							<div class="box_title">
								<h2>Change Tracking Report</h2>
							</div>
							<!-- // title -->
							<!-- wide_column -->
							<div>
								<div class="" id="id_change_tacking" style="height:500px;">
									
								
							<table width="1700px" class="grid grid_13" style="min-width:1500px;" >
										<colgroup>
							<col width="12%" />
							<col width="15%" />
							<col width="25%" />
							<col width="25%" />
							<col width="20%" />
							<col width="12%" />
							<col width="12%" />
							<col width="15%" />
							<col width="30%" />
							<col width="20%" />
							<col width="12%" />
							
						</colgroup>
						<thead>
							<tr>
								<th class="th_03"><div></div></th>
								<th class="th_03"><div>Store#</div></th>
								<th class="th_03"><div>Business Name</div></th>
								<th class="th_03"><div>Address</div></th>
								<th class="th_03"><div>City</div></th>
								<th class="th_03"><div>State</div></th>
								<th class="th_03"><div>Zip</div></th>
								<th class="th_03"><div>Phone</div></th>
									<th class="th_03"><div>Website</div></th>
									<th class="th_03"><div>Hours Of Operation</div></th>
									<th class="th_03"><div>User</div></th>
										
								
							</tr>
						</thead>
					
						</table>
							<div id="" class="business_listings_cls" >
							<table  width="1700px" class="grid grid_13"> 
							<colgroup >
							<col width="12%" />
							<col width="15%" />
							<col width="25%" />
							<col width="25%" />
							<col width="20%" />
							<col width="12%" />
							<col width="12%" />
							<col width="15%" />
							<col width="30%" />
							<col width="20%" />
							<col width="12%" />
									
								</colgroup>
							<tbody>
							<core:set var="total" value="${fn:length(changeList)}"/>
							<core:forEach var="j" begin="1" end="${total }">
						

							<core:forEach items="${changeList}" var="client" varStatus="i"> 
								 <core:set value="${i.count}" var="index" scope="page"></core:set>
								 <tr>
								 <core:if test="${index eq j}">
								 
								<td class="td_01"><div>Current</div></td>
								
									<td ><div>${client.store}</div></td>
									<td ><div>${client.businessName}</div></td>
									<td ><div>${client.locationAddress}</div></td>
									<td ><div>${client.locationCity}</div></td>
									<td ><div>${client.locationState}</div></td>
									<td ><div>${client.locationZipCode}</div></td>
									<td ><div>${client.locationPhone}</div></td>
									<td ><div>${client.webSite}</div></td>
									
									<td ><div>${client.hourseOfOperation}</div></td>
									<td ><div>${client.user}</div></td>
									
									</core:if>
					
							</tr>
							</core:forEach>
							<core:forEach items="${changeList2}" var="client" varStatus="i" >
								 <core:set value="${i.count}" var="index"></core:set>
								  <core:if test="${index eq j}">
								  <tr>
								
								<td class="td_01"><div>Last</div></td>
								
									<td ><div>${client.store}</div></td>
									<td ><div>${client.businessName}</div></td>
									<td ><div>${client.locationAddress}</div></td>
									<td ><div>${client.locationCity}</div></td>
									<td ><div>${client.locationState}</div></td>
									<td ><div>${client.locationZipCode}</div></td>
									<td ><div>${client.locationPhone}</div></td>
									<td ><div>${client.webSite}</div></td>
									
									<td ><div>${client.hourseOfOperation}</div></td>
									<td ><div>${client.user}</div></td>
									
						
							</tr>
							<tr style="border-bottom: #dfe2e3 5px solid;">
									
									<td class="td_01"><div>Date</div></td>
									
								
								<td ><div><fmt:formatDate value="${client.date}" pattern="MM/dd/YYYY"/></div></td>
								<td ><div><fmt:formatDate value="${client.businessNameCDate}" pattern="MM/dd/YYYY"/></div></td>
								<td ><div><fmt:formatDate value="${client.locationAddressCDate}" pattern="MM/dd/YYYY"/></div></td>
								<td ><div><fmt:formatDate value="${client.locationCityCDate}" pattern="MM/dd/YYYY"/></div></td>
								<td ><div><fmt:formatDate value="${client.locationStateCDate}" pattern="MM/dd/YYYY"/></div></td>
								<td ><div><fmt:formatDate value="${client.locationZipCodeCDate}" pattern="MM/dd/YYYY"/></div></td>
								<td ><div><fmt:formatDate value="${client.locationPhoneCDate}" pattern="MM/dd/YYYY"/></div></td>
								<td ><div><fmt:formatDate value="${client.webSiteCDate}" pattern="MM/dd/YYYY"/></div></td>	
								
								<td  ><div><fmt:formatDate value="${client.hourseOfOperationCDate}" pattern="MM/dd/YYYY"/></div></td>
								<td  ><div><core:out value=""></core:out></div></td>
										
								</tr >
									</core:if>
							</core:forEach>
							
						</core:forEach>

								</tbody>

										</table>
									
								</div>

							

							</div>
							<!-- // wide_column -->
						</div>

						<!-- // box -->
					</div>
					<!-- // box -->
					<!-- convert icons wrapper -->
					<div class="c_icons_wrapper">
						<a href="#" class="lnk_convert_ico ico_xls">XLS</a> <a
							href="#" class="lnk_convert_ico ico_pdf">PDF</a> <a
							href="#" class="lnk_convert_ico ico_prn">Print</a>
					</div>
					<!-- // convert icons wrapper -->
			</div>
			<!-- // content area -->
			<!-- sidebar -->
		</div>
</div>

			</spring:form>
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