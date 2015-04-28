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
<link rel="stylesheet" href="css/bpopUp.css">
<link rel="stylesheet" href="css/custom_pagination.css">
<script src="js/jquery-1.9.1.min.js"></script>
<script src="js/jquery.idTabs.min.js"></script>
<script src="js/jquery.slimscroll.min.js"></script>
<script src="js/jquery.screwdefaultbuttonsV2.js"></script>
<script src="js/jquery.cycle.all.js"></script>
<script src="js/functions.js"></script>
<script src="js/jquery.bpopup.min.js"></script>
<script src="js/jquery.jsort.0.4.min.js"></script>
<script src="js/jquery.session.js"></script>


<%

  int currentPage = (Integer)request.getAttribute("currentPage");
  int numOfPages = (Integer)request.getAttribute("numOfPages");

%>

<script type="text/javascript">
var currentPage = '<%=currentPage%>';
var numOfPages = '<%=numOfPages%>';
/* alert("numberOfPages :: "+numOfPages); */
$(document).ready(function() {
	$('#uploadfile').click(function(){
	var value = $('#file').val();
	alert(value);
	if(value==null){
		alert("please select file");
	}
		
	});
	
	/* $('#UploadFileName').click(function(event){
		   mydropdown = $('#myDropdown');
	if(mydropdown.length == 0 || $(mydropdown).val() == ""){
     alert("Please select  any one client");
     event.preventDefault();
		}
	   }); */
	
});
$(document).ready(function() {
    $('#selecctall').click(function(event) {  //on click 
        if(this.checked) { // check select status
            $('.checkbox1').each(function() { //loop through each checkbox
                this.checked = true;  //select all checkboxes with class "checkbox1"               
            });
        }else{
            $('.checkbox1').each(function() { //loop through each checkbox
                this.checked = false; //deselect all checkboxes with class "checkbox1"                       
            });         
        }
    });
    
});

function search() {
	$("#searchAndFilterForm").attr('action',
			'search.htm');
} 

function clearSearch() {
	/* $("#searchAndFilterForm").attr('action',
			'business-listings.htm'); */
	
	  var form = document.getElementById('searchAndFilterForm');
	     form.action = "business-listings.htm";
	     form.submit();
} 

function showErrorMessage( message,store,business){
	 $("#errorlist").text("");
	var mySplitResult = message.split("-");
	for(var i = 1; i < mySplitResult.length; i++){
		 var li = "<li>" + mySplitResult[i] + "</li>";
		
		 $("#errorlist").append(li);
	}
	 $("#displayStoreID").text(store);
	
	 $('#showErrorMessages').bPopup();	
	 $("ul li").removeClass("selected");
		$("#businessID").attr("href", "editBusinessInfo.htm?Id="+business);
}

function showUplaodPopup(){
	
	$('#showuploadpopup').bPopup();
	$("ul li").removeClass("selected");
}


function load_File(id,ext){
	 $("#errorMessage").text("");
	if(validateExtension(ext) == false)
	 {
	   var errorMessage="Invalid file type. Supported types are xls, xlsx";
	   $("#errorMessage").text(errorMessage);
	  return;
	  }
}
function validateExtension(v)
{
 var allowedExtensions = new Array("xls","xlsx");
 for(var ct=0;ct<allowedExtensions.length;ct++)
 {
  sample = v.lastIndexOf(allowedExtensions[ct]);
  if(sample != -1){return true;}
  }
 return false;
 }
 
 $(document).ready(function(){
	 
	var message =$("#uploadResultStatus").val();
	if(message != null){
		$('#displayUploadResultStatus').bPopup();
		$("ul li").removeClass("selected");
	}
	
	var message =$("#uploadHeaderResult").val();
	if(message != null){
		$('#displayUploadHeaderResult').bPopup();
		$("ul li").removeClass("selected");
	}
	
	 $("#uploadSubmission").click(function() {
		   $("#errorMessage").text("");
		   var fileName =$("#UploadFileName").val();
		   if(fileName.length == 0){
		    $("#errorMessage").text("Please select any one file");
		    return false;
		   }else{
		    return true;
		   }
		   
		  });
	 
	/*  $("table#businessTable tbody#businessInfo").jSort({
	        sort_by: 'td.td_03 div',
	        item: 'tr',
	        order: 'asc'
	    }); */
	 var isAsc = true;
	$("thead#businessTableHeaders tr th").click(function(){
		var currentClass = $(this).attr('class');
		//alert(currentClass);
		var currentClassArr = currentClass.split("_");
		//alert(currentClassArr[1]);
		var sortById = "td.td_"+currentClassArr[1]+" div";
		//alert(sortById);
		
		//alert(isAsc);
		if(isAsc){
			isAsc = false;
			//alert(isAsc);
			$("table#businessTable tbody#businessInfo").jSort({
		        sort_by: sortById,
		        item: 'tr',
		        order: 'desc'
		    });
		 
		 
		}else{
			isAsc = true;
			 $("table#businessTable tbody#businessInfo").jSort({
			        sort_by: sortById,
			        item: 'tr',
			        order: 'asc'
			    });
		}
		 
		}); 
	
	/* $('ul#business_listings_id').twbsPagination({
        totalPages: parseInt(numOfPages),
        visiblePages: 10,
        onPageClick: function (event, page) {
            document.location.href="business-listings_page.htm?page="+page;
        }
    }); */
    
    setPaginationBackground();
	 
 });
 
 function downloadInfo(){	 
	 $("#businessListform").attr("action", "downloadBusiness");
	}

	function showDeletePopup(){	 
		
		var form ="";
		var count = 0;
		$('input[type=checkbox].checkbox1').each(function () {
			if($(this).is(":checked")){
				++count;
				form = $(".checkbox1").val();
				}
		});		
		if(form!="" && count>=1){
			 $('#displayDeleteConfirmationpopup').bPopup();
			 $("ul li").removeClass("selected");
		 	return true;
			}else{
				$('#Deleterecordselectionpopup').bPopup();
				 $("ul li").removeClass("selected");
				//alert("please select at least one record");		
				return false;
			}		
		
	}
	function sendDeleteResult(){
	 
	  var form = document.getElementById('businessListform');
	     form.action = "deleteBusinessInfo";
	     form.submit();
	}
	function sendEditResult(){
		var checkBoxCount = 0;
		$('input[type=checkbox].checkbox1').each(function () {
			if($(this).is(":checked")){
				++checkBoxCount;
			}
		});
		var editType = "single";
		if (checkBoxCount > 1) {
			editType = "multiple";
		}
		$.session.set("editType",editType);
		$.session.set("isSave",false);
		  var form = document.getElementById('businessListform');
		     form.action = "editBusinessInfo";
		     form.submit();
		}
	
	function showExportPopup(){
		var form ="";
		var count = 0;
		$('input[type=checkbox].checkbox1').each(function () {
			if($(this).is(":checked")){
				++count;
				form = $(".checkbox1").val();
				}
		});		
		if(form!="" && count>=1){
			 $('#displayExportFilepopup').bPopup();			 
			 $("ul li").removeClass("selected");
		 	return true;
			}else{
				$('#exportrecordselectionpopup').bPopup();
				 $("ul li").removeClass("selected");
				//alert("please select at least one record");		
				return false;
			}		
		}
	function sendExportSubmit(){
		 var serviceName = document.getElementById('serviceName').value;
		 
		 if(serviceName.length == 0 || $(serviceName).val() == ""){
			 $('#templatetemplatepopup').bPopup();
			 $("ul li").removeClass("selected");
			   //alert("Please select  any one template");
			   event.preventDefault();
		 }else{
			 document.getElementById('templateName').value=serviceName;
				
			  var form = document.getElementById('businessListform');
			     form.action = "exportBusinessInfo";
			     form.submit(); 
		 }
				
		}
	
	function addLocation() {
		$.session.set("isSave",true);
		document.location.href = "addLocation.htm";
	}
	
	function listingsPagination(page) {
		//alert("page :: "+page);
		$.session.set("currentPage",page);
		document.location.href="business-listings_page.htm?page="+page;	
	}
	
	function paginationPre() {
		//var currentPageNum = getCurrentPageNum();
		//alert(currentPageNum);
		if (currentPage > 1) {
			--currentPage;
		}
		//alert(currentPageNum);
		listingsPagination(currentPage);
	}
	
	function paginationNext(lastPage) {
		//var currentPageNum = getCurrentPageNum();
		if (currentPage < lastPage) {
			++currentPage;
		}
		listingsPagination(currentPage);
	}
	
	/* function getCurrentPageNum() {
		return parseInt($.session.get("currentPage"));
	} */
	
	function setPaginationBackground() {
		//alert("visiblePages ::"+visiblePages);
		var pageNumStartIndex = 1;
		var pageNumEndIndex = 10;

		if (numOfPages < 10) {
		   pageNumEndIndex = numOfPages;
		  }
		if (currentPage > 10) {
			pageNumStartIndex = currentPage-10+1;
			pageNumEndIndex = currentPage;
		}
		var data = "";
		for ( var index = pageNumStartIndex; index <= pageNumEndIndex; index++ ) {
			
			data += "<span id='page_"+index/10+"' class='page_"+index+"'><a id='pageNums' href='#' onclick='listingsPagination("+index+");'>"
			+index+"</a></span>&nbsp;";
		}
		
		$("span#pageNums").html(data);
		
		var id = "page_"+currentPage;
		//alert("id :: "+id);
		$("div#business_listings_id span").css('background-color','');
		$("span."+id).css('background-color','#fff');
	}
 
</script>
</head>

<div class="popup" id="exportrecordselectionpopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		 <span class="buttonIndicator b-close"><span>X</span></span>
		<span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">
		Please select at least one record for export.
				
		</div>
	</div>
</div>

<div class="popup" id="Deleterecordselectionpopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		 <span class="buttonIndicator b-close"><span>X</span></span>
		<span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">
		Please select at least one record for delete.				
		</div>
	</div>
</div>

<div class="popup" id="templatetemplatepopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		 <span class="buttonIndicator b-close"><span>X</span></span>
		<span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">
		Please select ant one template.				
		</div>
	</div>
</div>

<div class="popup" id="displayExportFilepopup" style="display: none">
	<div class="pp-header">
		 <span class="buttonIndicator b-close"><span>X</span></span>
		Export File
	</div>
	<div class="pp-subheader">Your selected locations will be formatted in the template chosen below</div>
	<div class="pp-body">
		<div class="export">
			<select name="service" id="serviceName">
				<option value="">Choose Tempate</option>
				<option value="BingTemplate">Bing</option>
				<option value="GoogleTemplate">Google</option>
				<option value="MasterTemplate">Master Template</option>
				
			</select> 
			 <button onclick="sendExportSubmit();" class="btn_dark_blue_2">Export</button>
		</div>
	</div>
</div>

<div class="popup error" id="showErrorMessages" style="display: none">
	<div class="pp-header">
		<!--  <div class="close"></div>  -->
		<span class="buttonIndicator b-close"><span>X</span></span>
		<img style="margin-top: 2px;" src="images/ico_title_error.png"/>
		<span>Listing Error</span>
		<!--  <span class="b-close"><span>X</span></span> -->
	</div>
	<div class="pp-subheader">Liberty Tax Service: Store # <span id="displayStoreID"></span></div>
	<div class="pp-body">
		<div id="result-details" class="result-details">
		
			 <ul id="errorlist">
				
			</ul>  
			<a href="#" id="businessID" class="btn_dark_blue_2">Fix Errors</a>
		</div>
	</div>
</div>

<div class="popup" id="showuploadpopup" style="display: none">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span>
		Upload File
	</div>
	<div class="pp-subheader">Make sure you are using our <a href="download.htm">bulk upload template</a> for your data</div>
	<div class="pp-body">
	<spring:form action="uploadBusiness.htm?page=businessListings" commandName="uploadBusiness"
			enctype="multipart/form-data">  
			
		<div class="upload">
		<p id="errorMessage" style="color: red;"></p>			
			<input  type="file" id="UploadFileName"  name="targetFile" class="btn_grey_2" value="Upload" onChange="load_File(this.id,this.value)" />
			<input type="submit" id="uploadSubmission" class="btn_dark_blue_2" value="Upload" />
		</div>
	</spring:form>	
	</div>
</div>

<core:if test="${not empty headerPopup}">
<input type="hidden" value="${headerPopup}" id="uploadHeaderResult">
<div class="popup" id="displayUploadHeaderResult" style="display: none">
		<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span>
		Upload File
	</div>
	<div class="pp-subheader">Make sure you are using our <a href="download.htm">bulk upload template</a> for your data</div>
	<div class="pp-body">
	<spring:form action="uploadBusiness.htm?page=businessListings" commandName="uploadBusiness"
			enctype="multipart/form-data">
			
		
		<div class="result error">${headerPopup}</div>
		
			<div class="upload">
		<p id="errorMessage" style="color: red;"></p>
			
			<input  type="file" id="UploadFileName"  name="targetFile" class="btn_grey_2" value="Upload" onChange="load_File(this.id,this.value)" />
			<input type="submit" id="uploadSubmission" class="btn_dark_blue_2" value="Upload" />
		</div>
	</spring:form>	
	</div>
</div>
</core:if>

<core:if test="${showPopup ne null }">
<input type="hidden" value="${showPopup}" id="uploadResultStatus">
<div class="popup" id="displayUploadResultStatus" style="display: none">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span>
		Upload File
	</div>
	<div class="pp-body">
		<div class="result success">${message}</div>
		<core:if test="${fn:contains(message, 'Upload complete')}">
		<div class="result-details">
			<ul>
				<li><span>${listingsUploaded }</span> Listings Uploaded</li>
				<li><span>${listingsCompleted }</span> Listings Completed</li>
				<li class="red"><span>${listingsWithErrors }</span> Listings with Errors</li>
			</ul>
			<a href="view-listings.htm" class="btn_dark_blue_2">View Listings</a>
			<a href="view-errors.htm" style=" margin-left: 251px;margin-top: -46px;" class="btn_dark_blue_2">View Errors</a>
		</div>
		</core:if>
	</div>
</div>
</core:if>

<div class="popup" id="displayDeleteConfirmationpopup"  style="display: none">
 <div class="pp-header">
  <!-- <div class="close"></div> -->
  <span class="buttonIndicator b-close"><span>X</span></span>
  Delete Location
 </div>
 <div class="pp-subheader">You have selected to Delete the selected listing(s).<br><span class="red">This will permanently remove the listing(s) and cannot be undone.</span></div>
 <div class="pp-body">
  <div class="buttons">
   <button onclick="sendDeleteResult();" class="btn_red_2">Continue</button>
   <button class="btn_dark_blue_2 b-close">Cancel</button>
  </div>
 </div>
</div>

<body id="office">
<!-- page wrapper -->
<div class="wrapper"> 
	<!-- header -->
	<div class="header"> <a href="dash-board.htm" class="logo_lbl">Local Business Listings</a>
		<ul class="top_nav">
			<li class="home"><a href="dash-board.htm">Home</a></li>
			<li class="help"><a href="help.htm">Help</a></li>
			<li class="signout"><a href="logout.htm">Signout</a></li>
			<li class="welcome">
				<p>Hello, <br>
					${userName}</p>
			</li>
		</ul>
	</div>
	<!-- // header --> 
	<!-- content wrapper -->
	<div class="content_wrapper no-sidebar"> 
		<!-- left side navigation -->
		<ul class="ul_left_nav">
			<li class="si_dashboard"><a href="dash-board.htm">Dashboard</a></li>
			<li class="si_business_listings selected"><a href="business-listings.htm">Business Listings</a></li>
			<li class="si_error_listings"><a href="listing-error.htm">Listing Errors</a></li>
			<li class="si_upload_export"><a href="upload-export.htm">Upload/Export</a></li>
			<li class="si_reports"><a href="reports.htm">Reports</a></li>
			<li class="si_mobile_profile "><a href="manage-account.htm">Manage Account</a></li>
			<li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent Toolbox</a></li>
		</ul>
		<!-- // left side navigation --> 
		<!-- content area -->
		<div class="content" id="id_content">
			<div class="nav_pointer pos_01"></div>
			<!-- subheader -->
			<div class="subheader clearfix">
				<h1>BUSINESS LISTING</h1>
				<p>${channelName}</p>
			</div>
			<!-- // subheader -->
			<div class="inner_box"> 
			<spring:form method="post" commandName="businessSearch"  id="searchAndFilterForm">
				<div class="filters-2">
					<label>Brand Name&nbsp;<spring:input path="client"  style="width:140px"/></label>
					<label>Business Name&nbsp;<spring:input path="companyName" style="width:140px"/></label>
					<label>Store #&nbsp;<spring:input path="store" style="width:55px"/></label>
					<label>Zip&nbsp;<spring:input path="locationZipCode" style="width:55px"/></label>
					<label>Phone #&nbsp;<spring:input path="locationPhone" style="width:140px"/></label>
					<label>Address&nbsp;<spring:input path="locationAddress" style="width:140px"/></label>
					<label>City&nbsp;<spring:input path="locationCity" style="width:100px"/></label>
					<label>State&nbsp;<!-- <select>
							<option></option>
						</select> --><spring:input path="locationState" style="width:100px"/>
					</label>
					
					
					<input type="submit" value="Apply FIlter" onclick="search()" class="btn_dark_blue_2">
					<a href="business-listings.htm"   class="btn_dark_blue_2">CLEAR FIlter</a>
					
					<a href="#" class="btn_grey_3 plus"><span>Add/Remove Fields</span></a>
					</spring:form>
				</div>
				<!-- box -->
				<div class="box box_red_title business-listings"> 
					<!-- title -->
					<div class="box_title">
						<h2>Active Locations: ${activeListSize}</h2>						
						<div class="box_title_right"> <span>Locations With Errors: ${errorListSize}</span><a href="listing-error.htm"  class="btr_button">FIX ERRORS</a> </div>
					</div>
					<spring:form name="box" id="businessListform" action="" method="POST">
					<!-- // title -->
									
				<input type="hidden" id="templateName" name="serviceName">						
										
					<table width="100%" class="grid grid_13">
						<colgroup>
							<col width="5%"  />
							<col width="5%" />
							<col width="12%" />
							<col width="15%" />
							<col width="15%" />
							<col width="12%" />
							<col width="10%" />
							<col width="10%" />
							<col width="11%" />
						</colgroup>
						<thead id="businessTableHeaders">
							<tr>
								<th class="th_01"><div><input type="checkbox" id="selecctall"></div></th>
								<th class="th_02"><div><img src="images/icon-!.png" align="!"></div></th>
								<th class="th_03"><div >Store#</div></th>
								<th class="th_04"><div>Business Name</div></th>
								<th class="th_05"><div>Address</div></th>
								<th class="th_06"><div>City</div></th>
								<th class="th_07"><div>State</div></th>
								<th class="th_08"><div>Zip</div></th>
								<th class="th_09"><div>Phone</div></th>
							</tr>
						</thead>
					</table>
					<div id="id_business_listings" class="business_listings_cls">
						<table  id="businessTable" width="100%" class="grid grid_13">
							<colgroup>
								<col width="5%"  />
								<col width="5%" />
								<col width="12%" />
								<col width="15%" />
								<col width="15%" />
								<col width="12%" />
								<col width="10%" />
								<col width="10%" />
								<col width="11%" />
							</colgroup>
							<tbody id="businessInfo" class="listingsContent">
							
							<core:forEach items="${allBusinessInfo}" var="bean" varStatus="i">
							
								<tr class="odd">
									<td class="td_01"><div><input type="checkbox" class="checkbox1"  value="${bean.id}" name="id"></div></td>
									<td class="td_02"><div><core:if test="${not empty bean.isCorrectFormat  }"><a href="#" style="color:red;font-size: 14px;"  onclick="showErrorMessage('${bean.inCorrectValues}','${bean.store}','${bean.id}');" ><img src="images/icon-!.png" align="!"></a></core:if></div></td>
									<td class="td_03"><div ><core:out value="${bean.store}"></core:out></div></td>
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
				<!-- // box --> 
				<div id="business_listings_id" align="center">
					 <core:if test="${numOfPages gt 1}">
							<span id="page_first" class="next"><a href="#" onclick="listingsPagination(1);">first</a> </span>&nbsp;
							<span id="page_prev"><a href="#" onclick="paginationPre();">prev</a> </span>&nbsp;							
							<span id="pageNums">	</span>
							<span id="page_next"><a href="#" onclick="paginationNext(${numOfPages});">next</a> </span>&nbsp;
							<span id="page_last"><a href="#" onclick="listingsPagination(${numOfPages});">last</a> </span>&nbsp;
					</core:if>
				</div>
				<div class="buttons-center">
								<a href="#" onclick="sendEditResult()"   class="btn_dark_blue_2">Edit</a>
								 <a href="#" onclick="showExportPopup();" class="btn_dark_blue_2">Export</a>
			    				 <!-- <button  onclick="downloadInfo();" class="btn_dark_blue_2" >Download</button> -->
			   			
			   			<%	Boolean isViewOnly = (Boolean) session.getAttribute("isViewOnly");
						if(!isViewOnly) { %>
							<a href="#" onclick="showDeletePopup();" class="btn_dark_blue_2" >Delete</a>
							<a href="#" onclick="showUplaodPopup();" class="btn_dark_blue_2">Upload</a>
			    			<a href="#" class="btn_dark_blue_2" onclick="addLocation();">Add/Create Location</a>
			    				 
						<% }%>
				</div>
				</spring:form>
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
