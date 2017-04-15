<!DOCTYPE html>
<%@page import="com.business.common.util.LBLConstants"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<link rel="stylesheet" href="css/grids.css">
<link rel="stylesheet" href="css/sidebar.css">
<link rel="stylesheet" href="css/bpopUp.css">
<link rel="stylesheet" href="css/jquery-ui.css">
 <link rel="stylesheet" href="css/style.css">
 
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
.grid_07 td {
    color: #4e5b62;
    font-family: "OpenSansSemibold", Verdana, Geneva, sans-serif;
    text-transform: inherit;
        font-size: 13px;
}


.grid_07 td.td_02 {
    color: #4e5b62;
    font-family: "OpenSansSemibold", Verdana, Geneva, sans-serif;
    text-transform: inherit;
        font-size: 13px;
        text-align: center;
}


  </style>
<script src="js/jquery.session.js"></script>
<script type="text/javascript">

	 $(document).ready(function() {
var uploadreportpopup=$("#uploadreportpopup").val();
//alert(uploadreportpopup);
if(uploadreportpopup!=''){
	$('#upload').bPopup();
	$("ul li").removeClass("selected");
}
		
		/* if ($.session.get("isUploadReport") === "true") {
			$.session.set("isUploadReport",false);
			$('#upload').bPopup();
			$("ul li").removeClass("selected");
		} */
		
		var selRptId = $("#report\\.id").val();
		//alert(selRptId);
		
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
		if(selRptId==10){

			//$("#label").show();
			//$("#brandidvalue1").show();
			$("#runReport").hide();
			$("#runOverrideReport").show();
			//$("#clearsearch").show();
			///$("#searchbutton").show();
			$("#listingDrop").hide();

		}
		if(selRptId==11){

			$("#storeDrop").show();
			$("#listingDrop").hide();
			$("#branddrop").show();
			$("#clientName").show();
			$("#startdate").hide();
			$("#runVislibilityReport").show();
			$("#enddate").hide();
			$("#runReport").hide();

		}
		if(selRptId==12){

			$("#storeDrop").show();
			$("#listingDrop").hide();
			$("#branddrop").show();
			$("#clientName").show();
			$("#startdate").hide();
			$("#runAccuracyStoreReport").show();
			$("#enddate").hide();
			$("#runReport").hide();

		}
		if(selRptId==13){

			$("#storeDrop").show();
			$("#listingDrop").hide();
			$("#branddrop").show();
			$("#clientName").show();
			$("#startdate").hide();
			$("#runCitationStoreReport").show();
			$("#enddate").hide();
			$("#runReport").hide();

		}
		 if(selRptId==1){
						$("#storeDrop").show();
						$("#listingDrop").hide();
						$("#branddrop").show();
						$("#clientName").show();
						$("#startdate").hide();
						$("#runrenewalReport").show();
						$("#enddate").hide();
						$("#runReport").hide();

		} 

					if(selRptId==6 ){
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
							$("#startdate").show();
							$("#enddate").show();
							$("#rungmbBrandReport").show();
							$("#runReport").hide();
					 }
					
					 if(selRptId==8){

						 $("#storeDrop").show();
							$("#listingDrop").hide();
							$("#branddrop").show();
							$("#clientName").show();
							$("#startdate").show();
							$("#enddate").show();
							$("#rungmbStoreReport").show();
							$("#runReport").hide();

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
	    	maxDate: new Date(),
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
		var brandname=$('#clientName').val();
		//alert(brandname);
	
		var store = $("#stores_list_d").val();
		if(brandname == '' || store== ''){
			$("#warningpopup").bPopup();
		}else{
		var form = document.getElementById("reportsForm");	

		form.action = 'runCtrakingReport.htm?page=1';
		form.method = 'POST';			
		form.submit();
		}
	}
	
	
	function runOverrideReport() {
		var brandname=$('#clientName').val();
		
		
		var form = document.getElementById("reportsForm");	

		form.action = 'runOverridenReport.htm?page=1';
		form.method = 'POST';			
		form.submit();
		
	}
	function runSummaryReport() {
		var brandname=$('#clientName').val();
		//alert(brandname);
	
		var store = $("#storevalue").val();
		if(brandname == ''){
			$("#storewarningpopup").bPopup();
		}else{
	
		var form = document.getElementById("reportsForm");	

		form.action = 'runSummaryReport.htm?page=1';
		form.method = 'POST';			
		form.submit();
		}
		
	}
	function runVislibilityReport() {
		var brandname=$('#clientName').val();
		//alert(brandname);
	
		var store = $("#storevalue").val();
		if(brandname == '' || store== ''){
			$("#warningpopup").bPopup();
		}else{
			document.location.href = "runVisiblityReport.htm?store="+store+"&&brandname="+brandname;
		}
	}
	function runAccuracyStoreReport() {
		var brandname=$('#clientName').val();
		//alert(brandname);
	
		var store = $("#storevalue").val();
		if(brandname == '' || store== ''){
			$("#warningpopup").bPopup();
		}else{
			document.location.href = "runAccuracyReport.htm?store="+store+"&&brandname="+brandname;
		}
	}
	function runCitationStoreReport() {
		var brandname=$('#clientName').val();
		//alert(brandname);
	
		var store = $("#storevalue").val();
		if(brandname == '' || store== ''){
			$("#warningpopup").bPopup();
		}else{
			document.location.href = "runCitaionStoreReport.htm?store="+store+"&&brandname="+brandname;
		}
	}
	
	function runIt() {
		var startDate=$('#startDate').val();
		
		var enddate = $("#endDate").val();

	
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
		$(".loading").removeClass("dsp_None");
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
		var date = $("tr#"+id+" > td#input").text();
		//alert(date);
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
	function rungmbStoreReport(){
		var startDate=$('#startDate').val();
		var enddate = $("#endDate").val();
		var brandname=$('#clientName').val();
		var store = $("#storevalue").val();
		if(startDate == '' || enddate== ''){
			$("#datewarningpopup").bPopup();
		}
		else if(brandname == '' || store== ''){
			$("#warningpopup").bPopup();
		} else{
			document.location.href = "runGMBLocation.htm?store="+store+"&&brand="+brandname+"&&start="+startDate+"&&end="+enddate;
		}
	}
	function rungmbBrandReport(){
		var startDate=$('#startDate').val();
		var enddate = $("#endDate").val();
		var brandname=$('#clientName').val();
		if(startDate == '' || enddate== ''){
			$("#datewarningpopup").bPopup();
		}
		else if(brandname == ''){
			$("#storewarningpopup").bPopup();
		} else{
			document.location.href = "runGMBBrand.htm?brand="+brandname+"&&start="+startDate+"&&end="+enddate;
		}
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

<body id="office" class="mw980">
<!-- page wrapper -->
<div class="popup" id="direcorywarningpopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select store and directory</div>
	</div>
</div>
 <div class="popup" id="datewarningpopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select Dates</div>
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
		<div class="content" id="id_content">
    	<div class="nav_pointer pos_01"></div>
		  <!-- subheader -->
		  <div class="subheader clearfix">
			<h1>REPORTS</h1>
			<p>${channelName}</p>
		  </div>
		  <!-- // subheader -->
		  <input type="hidden" name="uploadreportpopup" value="${uploadreportpopup }" id="uploadreportpopup">
		  <spring:form method="post" modelAttribute="reportForm" id="reportsForm">
			<div class="inner_box">
			<!-- box -->
			<div class="box box_red_title box_report">
				<!-- title -->
				<div class="box_title mb9">
				<h2>View Report</h2>
			  </div>
			  <!-- // title -->
			  <!-- wide_column -->
			  <div class="wide_column_wrapper report_container">
				<div class="description" id="id_report_01">
				 <core:if test="${reportForm.reportRows == null}">
										<p>Generate reports using the tools on the right.</p>
										<%if( Role==LBLConstants.PURIST ){ %>
										<p style="color: red">Purist Data is a restricted version of LBL Pro. To gain access to the full version of LBL Pro, please contact Convergent Mobile support, 
										<a href="MAILTO:support@convergentmobile.com">support@convergentmobile.com</a></p></br>
										<%} %>
									
									</core:if>
									<core:if test="${fn:length(reportForm.reportRows) <= 0}">
										<p>No data found.</p>
									</core:if>
									
									<core:if test="${fn:length(reportForm.reportRows) > 0}">
										<table width="100%" class="grid grid_07">
											<core:forEach var="reportColumn"
												items="${reportForm.reportColumnHeaders}"
												varStatus="loopStatus1">
												<th class="th_01" ><a style="margin: 0px 30px 0px"
													href="javascript:runSort(${currentPage}, ${loopStatus1.count})"><core:out
															value="${reportColumn}" /></a></th>
											</core:forEach>

											<core:set var="colCnt"
												value="${fn:length(reportForm.reportColumnHeaders)}" />

											<core:forEach var="reportDataRow"
												items="${reportForm.reportRows}" varStatus="loopStatus">

												<core:set var="rowidx" value="${loopStatus.index % 2}" />

												<tr class="row${rowidx}" id="tr_${loopStatus.index}">
													<core:forEach begin="0" end="${colCnt-1}" varStatus="i">
														<core:if test="${i.index == 0}">
															<core:choose>
															
																<core:when test="${reportType eq 'UploadReport' }">
																	<td class="td_01" id="brand">
																	<a style="font-size: 13px" 
																	href="<core:url value="getUploadReportDetails.htm?brand=${reportDataRow.field1}&&date=${reportDataRow.field3}"/>">
																		<core:out
																				value="${reportDataRow.field1}" /></a></td>
																</core:when>
																<core:otherwise>
																	<td class="td_01" id="brand"><core:out
																			value="${reportDataRow.field1}" /></td>
																</core:otherwise>
															</core:choose>

														</core:if>

														<core:if test="${i.index == 1}">
															<td class="td_02"><core:out
																	value="${reportDataRow.field2}" /></td>
														</core:if>

														<core:if test="${i.index == 2}">
															<td class="td_02" id="date">
															<input type="hidden" value="${reportDataRow.field3}" id="datefieldvalue">
															<core:choose>
															
															<core:when test="${reportType eq 'UploadReport' }">
															
																<core:set value="${reportDataRow.field3}" var="datefield" />
																<fmt:parseDate value="${datefield }" var="datevalue" pattern="yyyy-MM-dd HH:mm:ss"/>
																<fmt:formatDate value="${datevalue }" var="datevalue" pattern="MM/dd/yyyy"/> 
															<core:out
																	value="${datevalue}" />
															
															</core:when>
															<core:otherwise>
															<core:set value="${reportDataRow.field3}" var="template" />
															
															<core:choose>
															<core:when test="${fn:containsIgnoreCase(template, 'Template')}">
															<core:set var="templateReplace" value="${fn:replace(template, 'Template', '')}"  scope="page"/> 
                               									<core:out
																	value="${templateReplace}" />
															</core:when>
															<core:when test="${fn:contains(templateReplace, '_')}">
															<core:set var="templateReplaceVal" value="${fn:replace(templateReplace, '_', '')}"  scope="page"/> 
                               									<core:out
																	value="${templateReplaceVal}" />
															</core:when>
															<core:otherwise>
																<core:out
																	value="${reportDataRow.field3}" />
															</core:otherwise>
															
															</core:choose>
															</core:otherwise>

															</core:choose>
															
															</td>
														</core:if>

														<core:if test="${i.index == 3}">
															<core:choose>
																<core:when test="${reportType eq 'DistributionReport' }">
																	<td class="td_02" id="distrbrand"><a href="#"
																		onclick="getDistrReport(${loopStatus.index})"><core:out
																				value="${reportDataRow.field4}" /></a></td>
																</core:when>
																<core:otherwise>
																	<td class="td_02" id="distrbrand"><core:out
																			value="${reportDataRow.field4}" /></td>
																</core:otherwise>
															</core:choose>

														</core:if>

														<core:if test="${i.index == 4}">
															<td class="td_02"><core:out
																	value="${reportDataRow.field5}" /></td>
														</core:if>

														<core:if test="${i.index == 5}">
															<td class="td_02"><core:out
																	value="${reportDataRow.field6}" /></td>
														</core:if>
														<core:if test="${i.index == 6}">
															<td class="td_02"><core:out
																	value="${reportDataRow.field7}" /></td>
														</core:if>
														<core:if test="${i.index == 7}">
															<td class="td_02"><core:out
																	value="${reportDataRow.field8}" /></td>
														</core:if>
														<core:if test="${i.index == 8}">
															<td class="td_02"><core:out
																	value="${reportDataRow.field9}" /></td>
														</core:if>

														<core:if test="${i.index == 9}">
															<td class="td_02"><core:out
																	value="${reportDataRow.field10}" /></td>
														</core:if>


													</core:forEach>
												</tr>
											</core:forEach>
										</table>
									</core:if>
				</div>
			  </div>
			  <core:if test="${reportForm.reportRows != null}">

									<br />

									<%--For displaying Page numbers.
		 						   The when condition does not display a link for the current page--%>
									<table border="0" class="table-2 ">
										<tr>
											<%--For displaying Previous link except for the 1st page --%>
											<core:if test="${currentPage != 1}">
												<td><a href="#"
													onclick="return runReport(${currentPage-1})"><strong>Previous</strong></a></td>
											</core:if>

											<core:forEach begin="${currentPageStart}"
												end="${currentPageEnd}" var="i">
												<core:choose>
													<core:when test="${currentPage eq i}">
														<td><strong>${i}</strong></td>
													</core:when>
													<core:otherwise>
														<td><a href="#" onclick="return runReport(${i})">${i}</a></td>
													</core:otherwise>
												</core:choose>
											</core:forEach>

											<%--For displaying Next link --%>

											<core:if test="${currentPage lt noOfPages}">
												<td><a href="#"
													onclick="return runReport(${currentPage+1})"><strong>Next</strong></a></td>
											</core:if>
										</tr>
									</table>
								</core:if>
			  <!-- // wide_column -->
			</div>
			<!-- // box -->
			<!-- convert icons wrapper -->
			<div class="c_icons_wrapper">
			<a href="reportxls.htm" class="lnk_convert_ico ico_xls">XLS</a>
			  <a href="reportpdf.htm" class="lnk_convert_ico ico_pdf">PDF</a>
			  <a href="#" class="lnk_convert_ico ico_prn">Print</a>
			</div>
			<!-- // convert icons wrapper -->
	
	
	
		  </div>
		</div>
		<!-- // content area --> 
		<!-- sidebar -->
		<div class="sidebar" id="id_sidebar">
    	<div class="inner">
      	<!-- title -->
        <div class="sb_title no-bg" style="min-width:365px">
        	<h2 class="">Select Report</h2>
          <div class="select_wrapper_01">
          <%if( Role==LBLConstants.PURIST ){ %>
          	 <spring:select class="" path="report.id" onchange="return getParams();" disabled="true">
								<spring:option value="">Select a Report</spring:option>
								<spring:options items="${reports}" itemValue="id"
									itemLabel="name" />
							</spring:select>
							<%}else{%>
								 <spring:select class="" path="report.id" onchange="return getParams();">
								<spring:option value="">Select a Report</spring:option>
								<spring:options items="${reports}" itemValue="id"
									itemLabel="name" />
									</spring:select>
							<%}%>
          </div>
        </div>
        <!-- // title -->
        <!-- sidebar box type 1 -->
        <div class="sb_box_01 sb_box_h01">
          <div class="sb_box_header"><h3 >
							<core:out value="${reportForm.report.name}" />
						</h3></div>
          <div class="form_wrapper_01">
          	<table class="grid grid_02 grid_02_1" width="100%">
            <colgroup>
            	<col width="48%" />
              <col width="52%" />
            </colgroup>
            <tbody>
            	<core:url var="findProductForCat" value="/products_ajax.htm" />
									<tr id="startdate">
										<td class="td_01"><label for="date_start">Start
												Date</label></td>
										<td class="td_02"><spring:input path="startDate" id="startDate"
												class="input_date_01" /></td>
									</tr>
									<tr id="enddate">
										<td class="td_01"><label for="date_end">End Date</label></td>
										<td class="td_02"><spring:input path="endDate" id="endDate"
												class="input_date_01" /></td>
									</tr>

									<tr id="branddrop">
										<td class="td_01"><label>Brand Name</label></td>
										<%if(Role==LBLConstants.PURIST){ %>
										<td class="td_02"><select name="client" id="clientName" disabled="disabled">
												<option value="">SELECT BRANDS</option>

												<core:forEach items="${clientNameInfo}" var="client">
													<core:choose>
														<core:when test="${client.brandName == reportForm.brandName}">
															<option value="${client.brandName}" selected="selected">${client.brandName}</option>
														</core:when>
														<core:otherwise>
															<option value="${client.brandName}">${client.brandName}</option>
														</core:otherwise>
													</core:choose>
												</core:forEach>
										</select><%}else{ %>
											<td class="td_02"><select name="client" id="clientName">
												<option value="">SELECT BRANDS</option>

												<core:forEach items="${clientNameInfo}" var="client">
													<core:choose>
														<core:when test="${client.brandName == reportForm.brandName}">
															<option value="${client.brandName}" selected="selected">${client.brandName}</option>
														</core:when>
														<core:otherwise>
															<option value="${client.brandName}">${client.brandName}</option>
														</core:otherwise>
													</core:choose>
												</core:forEach>
										</select>
										<%} %>
										</tr>
								<tr>
								<td class="td_01" style="display: none;" id="label"  class="search"><label for="date_end">Search Store #</label></td>
									<td  style="display: none;" id="brandidvalue1">
									
									<input type="text" name="store"  id="storesvalue" > </td> 
										<td style="display: none;" id="searchbutton">
														<a href="#" style="margin: 0px -17px 0px" 
														class="btn_search"  ><span onclick ="search()">Search</span></a></td>
									</tr>
									
									<tr id="listingDrop" >
										<td class="td_01"><label>Store #</label></td>
										<td class="td_02">
											 <select class="select-width" name="StoreValues" id="stores_list_d"  multiple="multiple" style="height: 100px">
												<option value="">Select Store</option>
												<core:forEach items="${storeNames}" var="store">
													<option value="${store.store}" >${store.store}</option>
												</core:forEach>
										</select>
										
										</td>
					
									</tr>
									<tr>
														
									
									<td style="display: none;" id="clearsearch"><a href="" style="margin: 0px 327px 0px" onclick="">Clear</a></td>
									
									</tr>
									
										<tr id="storeDrop" style="display: none">
										<td class="td_01"><label>Listings Stores</label></td>
										<td class="td_02" >
											 <select name="storeVal"  id="storevalue" >

												 <option value=""></option>
										
											<core:forEach items="${liststoreNames}" var="stores">
													<option value="${stores}" >${stores}</option>
												</core:forEach> 
										</select>
										
										</td>
									</tr>
									<tr id="directoryDrop" style="display: none">
										<td class="td_01"><label>Listings Directory</label></td>
										<td class="td_02" >
											 <select name="directory"  id="directory"  >
											

												 	<option value=""></option>

													<option value="google" >Google</option>
													<option value="foursquare" >foursquare</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="yp" >Yp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest" >Mapquest</option>
													<option value="usplaces" >usplaces</option>
													<option value="yellowbot" >yellowbot</option>
													<option value="ezlocal" >ezlocal</option>
													
												
										</select>
										
										</td>
									</tr>

									<core:forEach var="rp" items="${reportForm.report.params}"
										varStatus="loopStatus">
										<tr>
											<td class="td_01"><label><core:out
														value="${rp.paramLabel}" /></label></td>
											<td class="td_02"><spring:input
													path="report.params[${loopStatus.index}].paramValue" /></td>
										</tr>
									</core:forEach>
            </tbody>
            </table>
          </div>
          <div class="button_wrapper_02">
          	 <center><p style="color: red;font-size: 15px;    margin: 0px 0px 20px;">${error }</p>
							<core:set value="${error }" var="errorMessage"></core:set>
							
								<a href="#" id="runReport" onclick="runIt()"
									class="btn_dark_blue btn_report_01_lnk">Run Report</a>
								<a href="#" id="runcheckReport" onclick="runcheckreport()" style="display: none"
									class="btn_dark_blue btn_report_01_lnk">Run check Report</a>
									<a href="#" id="runaccuracyReport" onclick="runaccuracyreport()" style="display: none"
									class="btn_dark_blue btn_report_01_lnk">Run Accuracy Report</a>
										<a href="#" id="runCompareReport" onclick="runComparereport()" style="display: none"
									class="btn_dark_blue btn_report_01_lnk">Run Compare Report</a>
									<a href="#" id="runCitationReport" onclick="runCitationreport()" style="display: none"
									class="btn_dark_blue btn_report_01_lnk">Run Citation Report</a>
									<a href="#" id="runChangeTrackingReport" onclick="runChangeTrackingReport()" style="display: none"
									class="btn_dark_blue btn_report_01_lnk">Run ChangeTracking Report</a>
									<a href="#" id="runrenewalReport" onclick="runRenewalReport()" style="display: none"
									class="btn_dark_blue btn_report_01_lnk">Run Renewal Report</a>
									
										<a href="#" id="runOverrideReport" onclick="runOverrideReport()" style="display: none"
									class="btn_dark_blue btn_report_01_lnk">Run Override Report</a>
									<a href="#" id="runSummaryReport" onclick="runSummaryReport()" style="display: none"
									class="btn_dark_blue btn_report_01_lnk">Run Summary Report</a>
									<a href="#" id="runVislibilityReport" onclick="runVislibilityReport()" style="display: none"
									class="btn_dark_blue btn_report_01_lnk">Run Visibility Report</a>
									<a href="#" id="runAccuracyStoreReport" onclick="runAccuracyStoreReport()" style="display: none"
									class="btn_dark_blue btn_report_01_lnk">Run AcccuracyStore Report</a>
										<a href="#" id="runCitationStoreReport" onclick="runCitationStoreReport()" style="display: none"
									class="btn_dark_blue btn_report_01_lnk">Run CitationStore Report</a>
									
									<a href="#" id="rungmbStoreReport" onclick="rungmbStoreReport()" style="display: none"
									class="btn_dark_blue btn_report_01_lnk">Run Report</a>
										<a href="#" id="rungmbBrandReport" onclick="rungmbBrandReport()" style="display: none"
									class="btn_dark_blue btn_report_01_lnk">Run Report</a>
									
									</center>
										
								
          </div>
        </div>
        <!-- // sidebar box type 1 -->
	</spring:form>
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
<div class="popup-shader" style="display: none"></div>
<div class="popup" style="display: none">
	<div class="pp-header">
		<div class="close"></div>
		Upload File
	</div>
	<div class="pp-subheader">Make sure you are using our <a href="#">bulk upload template</a> for your data</div>
	<div class="pp-body">
		<div class="upload">
			<a href="#" class="btn_grey_2">Choose file</a>
			<span>No file selected</span>
			<a href="#" class="btn_dark_blue_2">Upload</a>
		</div>
	</div>
</div>


<div class="popup" style="display:none">
	<div class="pp-header">
		<div class="close"></div>
		Upload File
	</div>
	<div class="pp-body">
		<div class="result success">Upload complete, results can be seen in Business Listings.</div>
		<div class="result-details">
			<ul>
				<li><span>1234</span> Listings Uploaded</li>
				<li><span>1234</span> Listings Completed</li>
				<li class="red"><span>19</span> Listings with Errors</li>
			</ul>
			<a href="#" class="btn_dark_blue_2">View Listings</a>
		</div>
	</div>
</div>

<div class="popup" style="display: none">
	<div class="pp-header">
		<div class="close"></div>
		Upload File
	</div>
	<div class="pp-subheader">Make sure you are using our <a href="#">bulk upload template</a> for your data</div>
	<div class="pp-body">
		<div class="result error">Upload incomplete. Error code: 12345678</div>
		<div class="upload">
			<a href="#" class="btn_grey_2">Choose file</a>
			<span>No file selected</span>
			<a href="#" class="btn_dark_blue_2">Upload</a>
		</div>
	</div>
</div>

</body>
</html>