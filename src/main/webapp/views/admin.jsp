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

<link rel="stylesheet" href="js/jquery.mCustomScrollbar.css">
<script src="js/jquery.mCustomScrollbar.js"></script>
<script src="js/functions.js"></script>



<script type="text/javascript">


$(document).ready(function() {
	var message = $("#uploadHeaderResult").val();
	if (message != null) {
		$('#displayUploadHeaderResult').bPopup();
		$("ul li").removeClass("selected");
	}
			$('input[type=checkbox].checkbox1').click(function(){
				/* $('input.checkbox1').not(this)
				.prop('checked', false);  */
				var brandid = $('input:checkbox[name=id]:checked').val();

				$.ajax({
					type : "get",
					url : "getInactiveBrands.htm",
					cache : false,
					data :{
						brandid : brandid
					},
					success : function(
							response) {
						
						var brands = $
								.parseJSON(response);

						$(brands)
								.each(
										function(i,client) {
										//alert("inactive val"+	client.inactive);
										if(client.inactive == 'Y'){
											$('#yes').show();
											$('#inactive').hide();
										}
										if(client.inactive == 'N'){
											$('#yes').hide();
											$('#inactive').show();
										}

										});

					},
					error : function(
							response) {
						//alert('Error while fetching Brands for Channel..');
					}
				});
				
			});
			


	$("select#clientName").change(function() {
		var e = document.getElementById("clientName");
		//var clinetname = e.options[e.selectedIndex].value;
		var clinetname = e.options[e.selectedIndex].text;
			///var clinetname= $("select#clientNameId").val();
			//alert("clinetname:::::::::::"+clinetname);
			if(clinetname!= ''){
				document.location.href = "brandcategeroyserach.htm?clinetname="+clinetname;		
			}
				
				
			});
	$("select#brandnames").change(function() {
		var e = document.getElementById("brandnames");
		//var clinetname = e.options[e.selectedIndex].value;
		var clinetname = e.options[e.selectedIndex].text;
			///var clinetname= $("select#clientNameId").val();
			//alert("clinetname:::::::::::"+clinetname);
			/* if(clinetname!= ''){
				document.location.href = "brandinventoryserach.htm?clinetname="+clinetname;		
			} */
			
			$.ajax({
				type : "get",
				url : "brandinventoryserach.htm",
				cache : false,
				data :{
					clinetname : clinetname
				},
				success : function(
						response) {
					
					var brands = $
							.parseJSON(response);

							var html='';
					$(brands)
							.each(
									function(i,client) {


										html += '<li>'
												
												+ '<input type="radio" id="storevalue" name="store" value="'+client.store+'" >'
												+'<label for="sr1">'
												+ client.store
												+ '</label>'
												+ '</li>';

									});

					$("#storevalues")
							.html(html);

				},
				error : function(
						response) {
					//alert('Error while fetching Brands for Channel..');
				}
			});

				
				
			});
	
	$('#uploadfile').click(function(){
	var value = $('#file').val();
	//alert(value);
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

function searchListings() {
	$("#searchfiletrForm").attr('action',
	'dashListingsearch.htm');
} 
function SearchStoreListings() {
	var store=$("#storevalue").val();
	//alert("store"+store);
	/* $("#searchfiletrForm").attr('action',
	'dashListingsearch.htm'); */
	if(store!= ''){
		//document.location.href = "dashStoreListingsearch.htm?store="+store;	
		 var form = document.getElementById('searchstorefiletrForm');
	     form.action = "dashStoreListingsearch.htm";
	     form.submit(); 
	}
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
	
	 /* 
	 var inactive = $('#inactivevalue').val(); 
		alert("inactive"+inactive); */
	 
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
    
    //setPaginationBackground();
	 
 });
 function cancelRenewal(){
		var checkBoxCount = 0;
		$('input[type=checkbox].checkbox1').each(function () {
			if($(this).is(":checked")){
				//alert("coming");
				++checkBoxCount;
			}
		});
		//alert("coming");
		var editType = "single";
		if (checkBoxCount > 1) {
			editType = "multiple";
		}
		$.session.set("editType",editType);
		$.session.set("isSave",false);
		if(checkBoxCount == 0){
			$('#editBrandPopup').bPopup();
			 $("ul li").removeClass("selected");
			//alert("please select at least one record");		
			return false;
		}else{
			 var form = document.getElementById('businessListform');
		     form.action = "cancelRenewal.htm";
		     form.submit();
		}
 }
 function searchBrandName() {
		
		$("#searchAndBrandFilterForm").attr('action',
		'brandsandChannelSearch.htm');
	}
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
	     form.action = "deleteBrandInfo.htm";
	     form.submit();
	}
	function sendEditResult(){
		var checkBoxCount = 0;
		$('input[type=checkbox].checkbox1').each(function () {
			if($(this).is(":checked")){
				//alert("coming");
				++checkBoxCount;
			}
		});
		//alert("coming");
		var editType = "single";
		if (checkBoxCount > 1) {
			editType = "multiple";
		}
		$.session.set("editType",editType);
		$.session.set("isSave",false);
		if(checkBoxCount == 0){
			$('#editBrandPopup').bPopup();
			 $("ul li").removeClass("selected");
			//alert("please select at least one record");		
			return false;
		}else{
			 var form = document.getElementById('businessListform');
		     form.action = "editBrandInformation";
		     form.submit();
		}
		 
		}
		
	function inActiveBrands(){

		/* var inactive=$("#inactive").val(); */
		
		var checkBoxCount = 0;
		$('input[type=checkbox].checkbox1').each(function () {
			if($(this).is(":checked")){
			
				++checkBoxCount;
			}
		});


		  var form = document.getElementById('businessListform');
		     form.action = "inactive-brands.htm";
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
	
/* function addBrand() {
		$.session.set("isSave",true);
		
		$().bpopup;
		
		
	}  */
	
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
	function addBrand() {

		$('#addbrand').bPopup();

		$("ul li").removeClass("selected");
	} 



 
</script>
<script type="text/javascript">
$(document).ready(function() {
$("#popupcat").click(function() {
	$('#showuploadpopup').bPopup();
	$("ul li").removeClass("selected");
});

var message = $("#editbrandinadmin").val();
if (message != null) {
	$('#brandSearchDetails').bPopup();
	$("ul li").removeClass("selected");
}

});
</script>
</head>

<div class="popup" id="addbrand" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Brand
			Details</span>
	</div>


	<div class="pp-body">
		<spring:form action="addBrand.htm" commandName="BrandInfo"
			method="post">
			<table width="100%" class="grid grid_11 grid_11_1"
				style="width: 360px; margin: 10px auto;">
				<colgroup>
					<col width="55%" />
					<col width="45%" />
				</colgroup>
				<tbody>
				<tr>
					<td class="td_01"><div><label for="channel-name">Channel Name</label></div></td>
					<td class="td_02"><div class="brands"><!-- <input type="text" value="Convergent Mobil" id="channel-name" name="channel-name"> -->
					                   <spring:input path="channelName" id="channel-name"  /></div></td>
				</tr>
				<tr>
					<td class="td_01"><div><label for="brand-name">Brand Name</label></div></td>
					<td class="td_02"><div class="brands"><!-- <input type="text" value="Liberty Tax Services" id="brand" name="brand"> -->
					     <spring:input path="brandName" id="brand"  /></div></td>
				</tr>
				<tr>
					<td class="td_01"><div><label for="date_start">Client Id</label></div></td>
					<td class="td_02"><div class="brands"><!-- <input type="text" name="date_start" id="datepicker" class="input_date_01" value="01/01/2014"> -->
					     <spring:input path="clientId"  id="clientId" /> </div></td>
				</tr>
				
				<tr>
				<td class="td_01"><div><label for="date_start">Email</label></div></td>
					<td class="td_02"><div class="brands"><!-- <input type="text" name="date_start" id="datepicker" class="input_date_01" value="01/01/2014"> -->
					     <spring:input path="email"  id="email" /> </div></td>
				</tr>
				
				
				<tr>
					<td class="td_01"><div><label for="invoiced"># of Locations - Invoiced</label></div></td>
					<td class="td_02"><div class="brands"><!-- <input type="text" value="4,500" id="invoiced" name="invoiced" style="width:75px"> -->
					<spring:input path="locationsInvoiced" id="invoiced" style="width:75px" />
					  </div></td>
				</tr>
				<tr>
					<td class="td_01"><div><label for="email"># of Locations - Uploaded</label></div></td>
					<td class="td_02"><div><core:if test=""></core:if></div></td>
				</tr>
				</tbody>
			</table>
	</div>
	<div class="pp-subheader">Submissions</div>

	<div class="image-checkboxes">
		<label> <spring:checkbox path="submisions" id="checkbox1"
				value="Acxiom" /> <img src="images/logo1.png" alt=""></label> <label>
			<spring:checkbox path="submisions" id="checkbox1" value="InfoGroup" />
			<img src="images/logo1-02.png" alt="">
		</label> <label> <spring:checkbox path="submisions" id="checkbox1"
				value="Google" /> <img src="images/logo1-03.png" alt=""></label> <label>
			<spring:checkbox path="submisions" id="checkbox1" value="Factual" />
			<img src="images/logo1-06.png" alt="">
		</label> <label> <spring:checkbox path="submisions" id="checkbox1"
				value="Neustar" /> <img src="images/logo1-04.png" alt=""></label> <label>
			<spring:checkbox path="submisions" id="checkbox1" value="Bing" /> <img
			src="images/logo1-05.png" alt="">
		</label>
	</div>
	<div class="buttons buttons-2">
	
 <input
			type="submit" id="savepopup" class="btn_dark_blue_2" value="save">
		<input type="reset" id="cancelpopup" class="btn_dark_blue_2"
			value="cancel">

	</div>
	</spring:form>
</div>
<core:if test="${not empty editbrandinadmin}">
<input type="hidden" value="${editbrandinadmin}" id="editbrandinadmin">
	<div class="popup" id="brandSearchDetails" >
		<div class="pp-header">
			<!-- <div class="close"></div> -->
			<span class="buttonIndicator b-close"><span>X</span></span> <span>Brand
				Details</span>
		</div>
		
		<div class="pp-body">
			<spring:form action="addBrand.htm" commandName="BrandInfo"
				method="post">

				<spring:hidden path="saveType" value="update" />
				<table width="100%" class="grid grid_11 grid_11_1"
					style="width: 360px; margin: 10px auto;">
					<colgroup>
						<col width="55%" />
						<col width="45%" />
					</colgroup>
					<tbody>
						<tr>
					<td class="td_01"><div><label for="channel-name">Channel Name</label></div></td>
					<td class="td_02"><div class="brands"><!-- <input type="text" value="Convergent Mobil" id="channel-name" name="channel-name"> -->
					                   <spring:input path="channelName" id="channel-name"  /></div></td>
				</tr>
				<tr>
					<td class="td_01"><div><label for="brand-name">Brand Name</label></div></td>
					<td class="td_02"><div class="brands"><!-- <input type="text" value="Liberty Tax Services" id="brand" name="brand"> -->
					     <spring:input path="brandName" id="brand"  /></div></td>
				</tr>
				<tr>
					<td class="td_01"><div><label for="date_start">Client Id</label></div></td>
					<td class="td_02"><div class="brands"><!-- <input type="text" name="date_start" id="datepicker" class="input_date_01" value="01/01/2014"> -->
					     <spring:input path="clientId"  id="clientId" /> </div></td>
				</tr>
				
				<tr>
				<td class="td_01"><div><label for="date_start">Email</label></div></td>
					<td class="td_02"><div class="brands"><!-- <input type="text" name="date_start" id="datepicker" class="input_date_01" value="01/01/2014"> -->
					     <spring:input path="email"  id="email" /> </div></td>
				</tr>
				
				
				<tr>
					<td class="td_01"><div><label for="invoiced"># of Locations - Invoiced</label></div></td>
					<td class="td_02"><div class="brands"><!-- <input type="text" value="4,500" id="invoiced" name="invoiced" style="width:75px"> -->
					<spring:input path="locationsInvoiced" id="invoiced" style="width:75px" />
					  </div></td>
				</tr>
				<tr>
					<td class="td_01"><div><label for="email"># of Locations - Uploaded</label></div></td>
					<td class="td_02"><div><core:out value="${count}"></core:out></div></td>
				</tr>
					</tbody>
				</table>
		</div>
		<div class="pp-subheader">Submissions</div>
	<div class="image-checkboxes">

			<core:set var="subStr"
				value="Acxiom,InfoGroup,Google,Factual,Neustar,Bing" />
			<core:set var="subBrandList"
				value="${fn:split(BrandInfo.submisions,',') }" />
			<core:set var="submissionValues" value="${fn:split(subStr, ',')}" />
			<core:forEach var="subValue" items="${submissionValues}">
				<%-- <core:set var="subValueArr" value="${fn:split(brand.submisions,'_'}" /> --%>
				<core:set var="flag" value="true" />
				<label> <core:forEach var="brandStr" items="${subBrandList}"
						varStatus="i">
						<core:set var="brandStrArr" value="${fn:split(brandStr,'_')}" />
						<core:if test="${brandStrArr[0] eq subValue}">
							<core:set var="flag" value="false" />
						
							<spring:checkbox id="partners" path="submisions"
								value="${subValue}" checked="true" />
							<img src="images/logo1_${subValue}.png" alt="${subValue}">
						
							<core:if test="${brandStrArr[1] != 'null'}">
								<p>
									<core:out value="${brandStrArr[1]}" />
								</p>
							</core:if>
						</core:if>
					</core:forEach> <core:if test="${flag}">
						<spring:checkbox id="partners" path="submisions"
							value="${subValue}" checked="" />
						<img src="images/logo1_${subValue}.png" alt="">
					</core:if>

				</label>
			</core:forEach>

		</div>
		<div class="buttons buttons-2">
			<!-- 		<a  id="createNewBrand"  class="btn_dark_blue_2">Create New</a>
		<a href="#" id="editpopup" class="btn_dark_blue_2">edit</a>
		<input  type="submit" id="savepopup" class="btn_dark_blue_2" value="save">
		<input  type="reset"  id="cancelpopup" class="btn_dark_blue_2" value="cancel"> -->
			<input type="submit" class="btn_dark_blue_2" value="update">
			<input  type="reset"  id="cancelpopup" class="btn_dark_blue_2" value="cancel"> 
		</div>
		</spring:form>
	</div>
</core:if>

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
<div class="popup" id="editBrandPopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		 <span class="buttonIndicator b-close"><span>X</span></span>
		<span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">
		Please select at least one record for edit.				
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
				<option value="">Choose Template</option>
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
		<span class="buttonIndicator b-close"><span>X</span></span> Upload
		File
	</div>
	<div class="pp-subheader">
		Make sure you are using our <a href="categortytemplate.htm">CATEGORY TEMPLATE</a> for your data
	</div>
	<div class="pp-body">
		<spring:form action="businessCategory.htm"
			commandName="CatgoryBusiness" enctype="multipart/form-data">


			<div class="upload">
				<p id="errorMessage" style="color: red;"></p>

				<input type="file" name="targetFile" class="btn_grey_2"
					value="Upload" /> <input type="submit" class="btn_dark_blue_2"
					value="Upload" />
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
	<div class="pp-subheader">
		Make sure you are using our <a href="categortytemplate.htm">CATEGORY TEMPLATE</a> for your data
	</div>
	<div class="pp-body">
		<spring:form action="businessCategory.htm"
			commandName="CatgoryBusiness" enctype="multipart/form-data">

	<div class="result error">${headerPopup}</div>
			<div class="upload">
				<p id="errorMessage" style="color: red;"></p>

				<input type="file" name="targetFile" class="btn_grey_2"
					value="Upload" /> <input type="submit" class="btn_dark_blue_2"
					value="Upload" />
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
</head>

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
			<li class="si_business_listings "><a href="business-listings.htm">Business Listings</a></li>
			<li class="si_error_listings"><a href="listing-error.htm">Listing Errors</a></li>
			<li class="si_upload_export"><a href="upload-export.htm">Upload/Export</a></li>
			<li class="si_reports"><a href="reports.htm">Reports</a></li>
				<%
						Integer Role=(Integer)session.getAttribute("roleId");				
							if(Role==LBLConstants.CONVERGENT_MOBILE_ADMIN){						
						%>
			<li class="si_admin selected"><a href="admin-listings.htm">CM admin</a></li>
			<%} %>
			<!-- <li class="si_schduler"><a href="scheduler.htm">Schedule</a></li> -->
			<li class="si_mobile_profile "><a href="manage-account.htm">Manage Account</a></li>
				
			<!--<li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent Toolbox</a></li>-->
		</ul>
		<!-- // left side navigation --> 
		<!-- content area -->
		<div class="content" id="id_content">
			<div class="nav_pointer pos_01"></div>
			<!-- subheader -->
			<div class="subheader clearfix">
				<h1>CM ADMIN</h1>
				<!-- <a href="admin-listings.htm" class="back">Back to CM Admin</a> -->
				<p>${channelName}</p>
			</div>
			<!-- // subheader -->
			<div class="inner_box"> 
				<!-- box -->
				<div class="box box_red_title brand_mgn">
					<!-- title -->
					<div class="box_title">
						<h2>BRAND MANAGEMENT</h2>						
						<div class="box_title_right">
							
							<spring:form class="box_title_form" method="post" commandName="BrandInfo"
											id="searchAndBrandFilterForm">
									<spring:select path="channelName" id="channelId">
								<option value="">Select Channel</option>
								<core:forEach var="channellist" items="${channels}">
								
											
								<option value="${channellist}">${channellist}</option>
									
									
								</core:forEach>
								</spring:select>

								<spring:select path="brandName" >
								<option value="">Select Brands</option>
								<option value="All Brands">All Brands</option>
								<core:forEach items="${clientNameInfo}" var="client">
									<option value="${client.brandName}">${client.brandName}</option>
								</core:forEach>
								</spring:select>
								<button class="btr_button" onclick="searchBrandName()">Filter</button>
								<a href="admin-listings.htm" class="lnk">Clear Filter</a>
							</spring:form>
						</div>
					</div>
					<spring:form name="box" id="businessListform" method="POST"  >
									
									<div id="id_brand_mgn_wrapper" style="height:175px;">		
					<table width="1500px" class="grid grid_13" style="min-width:1400px">
						<colgroup>
							<col width="4%"  />
							<col width="13%" />
							<col width="11%" />
							<col width="10%" />
							<col width="10%" />
							<col width="10%" />
							<col width="10%" />
							<col width="10%" />
							<col width="10%" />
							<col width="12%" />
							<col width="10%" />
								<col width="12%" />
							
						</colgroup>
						<thead>
							<tr>
								<th class="th_01"><div><input type="checkbox" id="selecctall"></div></th>
								<th class="th_03"><div>Channel Name</div></th>
								<th class="th_03"><div>Brand Name</div></th>
								<th class="th_03"><div>Locations - Uploaded</div></th>
								<th class="th_03"><div>Locations - Invoiced</div></th>
								<th class="th_03"><div>Acxiom</div></th>
								<th class="th_03"><div>Factual</div></th>
								<th class="th_03"><div>Infogroup</div></th>
								<th class="th_03"><div>Localeze</div></th>
								<th class="th_03"><div>Google</div></th>
								<th class="th_03"><div>Bing</div></th>
								<th class="th_03"><div>Inactive</div></th>
							</tr>
						</thead>
					</table>
					<div id="id_brand_mgn" >
						<table   width="1500px" class="grid grid_13" style="min-width:1200px">
							<colgroup>
								<col width="4%"  />
								<col width="13%" />
								<col width="11%" />
								<col width="10%" />
								<col width="10%" />
								<col width="10%" />
								<col width="10%" />
								<col width="10%" />
								<col width="10%" />
								<col width="12%" />
								<col width="10%" />
								<col width="12%" />
							
							</colgroup>
						
							<tbody id="businessInfo" class="listingsContent">
							
							<core:forEach items="${brandListing}" var="bean" varStatus="i">
							
								<tr class="odd">

								<td class="td_01"><div><input type="checkbox" class="checkbox1"  value="${bean.brandID}" name="id" id="brandId"></div></td>
									
								<p><input type="hidden"  name="inactive" id="hiddenActiveValue"  value="<core:out value="${bean.inactive}"/>"/></p>
									<td class="td_03"><div ><core:out value="${bean.channelName}"></core:out></div></td>
									<td class="td_03"><div ><core:out value="${bean.brandName}"></core:out></div></td>
									<td class="td_04"><div><core:out value="${bean.locationCotracted}"></core:out></div></td>
								

									<td class="td_05"><div><core:out value="${bean.locationsInvoiced}"></core:out></div></td>
									<td class="td_06"><div><fmt:formatDate value="${bean.activeDate}" pattern="dd/MM/yyyy"></fmt:formatDate></div></td>
									<td class="td_07"><div><fmt:formatDate value="${bean.activeDate}" pattern="dd/MM/yyyy"></fmt:formatDate></div></td>
									<td class="td_08"><div><fmt:formatDate value="${bean.activeDate}" pattern="dd/MM/yyyy"></fmt:formatDate></div></td>
									<td class="td_09"><div><fmt:formatDate value="${bean.activeDate}" pattern="dd/MM/yyyy"></fmt:formatDate></div></td>
									<td class="td_09"><div><fmt:formatDate value="${bean.activeDate}" pattern="dd/MM/yyyy"></fmt:formatDate></div></td>
									<td class="td_09"><div><fmt:formatDate value="${bean.activeDate}" pattern="dd/MM/yyyy"></fmt:formatDate></div></td>
									<td class="td_04"><div><core:out value="${bean.inactive}"></core:out></div></td>
								</tr>
								
								</core:forEach>
							
							</tbody>
						</table>
						
					</div>
				</div>
					</div>
					
				<!-- // box --> 
				
				<div class="buttons-center">
					<a href="#" class="btn_dark_blue_2" onclick="addBrand();">Add Brand</a>
					<a href="#" class="btn_dark_blue_2" onclick="sendEditResult();">Edit</a>
					<a href="#" class="btn_dark_blue_2"  onclick="showDeletePopup();">Delete</a>
					<a href="#" class="btn_dark_blue_2" onclick="inActiveBrands()" id="inactive">Inactive</a>
					
					<a href="#" class="btn_dark_blue_2" style="display: none" id="yes">YES</a>
					<a href="#" class="btn_dark_blue_2" onclick="cancelRenewal()" >Cancel Renewal</a>
					
					
					<a href="custom-submissions.htm" class="btn_dark_blue_2">Customize Submissions</a>
				</div>
			
				</spring:form>
				<div class="box box_lightblue_title category_convers"> 
					<!-- title -->
					<div class="box_title">
						<h2>Category Conversion</h2>
						<div class="box_title_right">
							
								<select name="client" id="clientName">
								<option value="">Select Brands</option>
								<!-- <option value="All Brands" >All Brands</option> -->
								<core:forEach items="${clientNameInfo}" var="client">
									<option value="${client.brandName}" >${client.brandName}</option>
								</core:forEach>
							</select>
							<a href="admin-listings.htm" class="lnk" style="color: white">Clear Filter</a>
								<a class="btr_button" href="#" id="popupcat">Upload Category</a>
							
						</div>
					</div>
					<!-- // title -->
					<div id="id_brand_cgs_wrappers" style="height:155px;">	
					<table width="1500px" class="grid grid_13" style="min-width:1400px">
						<colgroup>
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
						
						</colgroup>
						<thead>
							<tr>
								<th class="th_03"><div>Client ID</div></th>
								<th class="th_03"><div>Category Label</div></th>
								<th class="th_03"><div>SIC</div></th>
								<th class="th_03"><div>SIC Description</div></th>
								<th class="th_03"><div>SYPH</div></th>
								<th class="th_03"><div>SYPH Description</div></th>
								<th class="th_03"><div>Factual ID</div></th>
								<th class="th_03"><div>Factual Category</div></th>
									<th class="th_03"><div>Apple category</div></th>
								<th class="th_03"><div>Google Category</div></th>
								<th class="th_03"><div>Bing Category</div></th>
								<th class="th_03"><div>Bing Category Description</div></th>
								<th class="th_03"><div>Yp Internet Heading</div></th>
								<th class="th_03"><div>Yp Description</div></th>
							
							</tr>
						</thead>
					</table>
					<div id="id_category_convers">
						<table width="1500px%" class="grid grid_13" style="min-width:1200px">
							<colgroup>
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							<col width="12%" />
							</colgroup>
							<tbody>
							<core:forEach items="${categeoryListing}" var="categeory">
								<tr class="odd">
									<td class="td_01"><div><core:out value="${categeory.clientId}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${categeory.categoryLabel}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${categeory.sicCode}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${categeory.sicDescription}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${categeory.syphCode}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${categeory.syphDescription}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${categeory.factualCategoryId}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${categeory.factualCategoryDescription}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${categeory.appleCategory}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${categeory.googleCategory}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${categeory.bingCategory}"></core:out></div></td>
									
									<td class="td_03"><div><core:out value="${categeory.bingCategoryDesc}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${categeory.ypInternetHeading}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${categeory.ypDesc}"></core:out></div></td>
									
								</tr>
							
								</core:forEach>
								
							</tbody> 
						</table>
					</div>
				</div>
				</div>
					<div class="box box_darkblue_title invest_mgn">
					<!-- title -->
					<div class="box_title">
						<h2>Inventory Management</h2>
					</div>
					<!-- // title -->
					<div class="box-invest-left">
						<div class="box-invest-left-top">
							<p class="total"><span>${totalSubmissions}</span>Total<br> Submissions</p>
							<p class="possible"><span>${possibleSubmissions}</span>Possible<br>Submissions</p>

							<table width="100%" class="grid grid_22">
						<colgroup>
						<col width="20%" />
						<col width="20%" />
						<col width="20%" />
						<col width="20%" />
						<col width="20%" />
						</colgroup>
						<thead>
							<tr>
								<th class="th_01"><div>&nbsp;</div></th>
								<th class="th_01"><div>Acxiom</div></th>
								<th class="th_01"><div>Factual</div></th>
								<th class="th_01"><div>Infogroup</div></th>
								<th class="th_01"><div>Localeze</div></th>
							</tr>
						</thead>
					</table>
					
						<table width="100%" class="grid grid_22">
							<colgroup>
							<col width="20%" />
							<col width="20%" />
							<col width="20%" />
							<col width="20%" />
							<col width="20%" />
							</colgroup>
							<tbody>
							
									<tr>
									<td class="td_01"><div>Submitted</div></td>
									<td class="td_02"><div>${contractedpartners}</div></td>
									<td class="td_02"><div>${contractedFactualpartners}</div></td>
									<td class="td_02"><div>${contractedInfoGrouppartners}</div></td>
									<td class="td_02"><div>${contractedLocalezepartners}</div></td>
								</tr>
								<tr>
									<td class="td_01"><div>Contracted</div></td>
									<td class="td_02"><div>${contractedpartners}</div></td>
									<td class="td_02"><div>${contractedFactualpartners}</div></td>
									<td class="td_02"><div>${contractedInfoGrouppartners}</div></td>
									<td class="td_02"><div>${contractedLocalezepartners}</div></td>
								</tr>
								<tr>
									<td class="td_01"><div>Not Submitted</div></td>
									<td class="td_02"><div>${contractedpartners}</div></td>
									<td class="td_02"><div>${contractedFactualpartners}</div></td>
									<td class="td_02"><div>${contractedInfoGrouppartners}</div></td>
									<td class="td_02"><div>${contractedLocalezepartners}</div></td>
								</tr>
								<tr>
									<td class="td_01"><div>Last Date <br />Submitted</div></td>
									<td class="td_02"><div>1/22/15</div></td>
									<td class="td_02"><div>1/22/15</div></td>
									<td class="td_02"><div>1/22/15</div></td>
									<td class="td_02"><div>1/22/15</div></td>
								</tr>
								
							</tbody>
						</table>

						</div>
						<div class="box-invest-left-bot">
							<div class="box-ivest-form">
							<spring:form commandName="searchBusiness" method="post" id="searchfiletrForm">
								<div class="filters">
									<spring:select path="client" id="brandnames">
								<option value="">Select Brands</option>
								
								<core:forEach items="${clientNameInfo}" var="client">
									<option value="${client.brandName}">${client.brandName}</option>
								</core:forEach>
								</spring:select>
									<spring:input path="store"  />
									<button  class="btn_search" onclick="searchListings()"><span>Search</span></button>
									<a href="admin-listings.htm" class="lnk" style="color: red">Clear </a>
										
								</div>
								
								</spring:form>
								
								<div class="box-invest-info">
									Store 12345 <span>Business Name</span> Fairfax, VA
								</div>
								<div class="box-invest-date">
									<ul>
										<li><span>Factual</span> 1/23/14</li>
										<li><span>Localeze</span> 1/23/14</li>
										<li><span>Infogroup</span> 1/23/14</li>
										<li><span>Acxiom</span> 1/23/14</li>
									</ul>
								</div>
							</div>
							<div class="box-view-listings">
							<spring:form commandName="searchBusiness" method="post" id="searchstorefiletrForm">
								<ul id="id_view_listings">
								

												<div id="storevalues"></div>
										
								</ul>
								<button class="btr_button" onclick="SearchStoreListings()" type="button" >View Listings</button>
								</spring:form>
							</div>
						</div>
					</div>

					<div class="box-invest-right">
						<table width="100%" class="grid grid_21">
							<colgroup>
								<col width="24%" />
								<col width="38%" />
								<col width="38%" />
							</colgroup>
							<thead>
								<tr>
									<th></th>
									<th><div>Locations Uploaded</div></th>
									<th><div>Locations Contracted</div></th>
								</tr>
							</thead>
						</table>
					<div id="id_invest_mgn2">
						<table width="100%" class="grid grid_21">
							<colgroup>
								<col width="24%" />
								<col width="38%" />
								<col width="38%" />
							</colgroup>
							<tbody>
								<core:forEach items="${brandsInfo}" var="brands" varStatus="i">	
								<tr>
									<td class="td_02"><div><core:out value="${brands.brands}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${brands.locationCount}"></core:out></div></td>
									<td class="td_03"><div><core:out value="${brands.locationCotracted}"></core:out></div></td>
								</tr>
								</core:forEach>
							

							</tbody>
						</table>
					</div>
					
					</div>
					
					<div class="clearfix"></div>
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
