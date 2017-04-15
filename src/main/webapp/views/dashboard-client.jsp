<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
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

<style type="text/css">
#passcss
 { 
	height: 31px;
	color: #4a555b;
	font-size: 14px;
	line-height: 31px;
	vertical-align: middle;
	color: #242C30;
	border: #88949a 1px solid;
	background: #fff url(../images/input_campaign.png) 0 0 repeat-x;
	width: 171px;
}
</style>
<%

  int currentPage = (Integer)request.getAttribute("currentPage");
	int numOfPages = (Integer)request.getAttribute("numOfPages");

%>

<script type="text/javascript">
var currentPage = '<%=currentPage%>';
var numOfPages = '<%=numOfPages%>';
$(document).ready(function() {
	$('#uploadfile').click(function(){
	var value = $('#file').val();
	alert(value);
	if(value==null){
		alert("please select file");
	}
		
	});
	
});

function sortByCount(flag){
	 document.location.href = "getClientCountSort.htm?flag="+flag;
}




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
    
    
    $("#cancel").hide(); 		
		$("#saveConfirmationId").hide(); 	
    
    $("div > a#edit").click(function() {
    	disableReadOnly();    	 
    	 $("#cancel").show(); 		
 		$("#saveConfirmationId").show(); 
 		$("#edit").hide(); 		
 	});
    
    $('#saveConfirmationId').click(function(event){
        
            data = $('.password').val();
            var len = data.length;
            
            if(len < 1) {
            	 $("#PasswordPopUp").bPopup();
    			 $("ul li").removeClass("selected");
                //alert("Password cannot be blank");
                // Prevent form submission
                event.preventDefault();
            }else if($('.password').val() != $('.confpass').val()) {            	
            	 $("#ConfirmPasswordPopUp").bPopup();
    			 $("ul li").removeClass("selected");
               // alert("Enter Confirm Password Same as Password");
                // Prevent form submission
                event.preventDefault();
            }else{
              $('#displaySaveConfirmationpopup').bPopup();
              $("ul li").removeClass("selected");
              event.preventDefault();
            } 
        });
});
function getStores(flag){
	 var checkedvalue='';
	 // check select status
			            $('.checkbox2').each(function() { //loop through each checkbox

			            	checkedvalue=this.checked;
			           //select all checkboxes with class "checkbox1"               
			            });
	 var businessname=$("#companyName").val();
	 var brands=$("#brands").val();
	 var locationAddress=$("#Addresslocation").val();
	 //locationstore
	 var locationstore=$("#locationstore").val();
	 var ZipCodelocation=$("#ZipCodelocation").val();
	 var Citylocation=$("#Citylocation").val();
	 var Phonelocation=$("#Phonelocation").val();
	 var Statelocation=$("#Statelocation").val();


	document.location.href = "clientsortByStore.htm?flag="+flag+"&&page="+currentPage+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;


}
function getBusinessname(flag){
	 var checkedvalue='';
	 // check select status
			            $('.checkbox2').each(function() { //loop through each checkbox

			            	checkedvalue=this.checked;
			           //select all checkboxes with class "checkbox1"               
			            });
			            var businessname=$("#companyName").val();
			       	 var brands=$("#brands").val();
			       	 var locationAddress=$("#Addresslocation").val();
			       	 //locationstore
			       	 var locationstore=$("#locationstore").val();
			       	 var ZipCodelocation=$("#ZipCodelocation").val();
			       	 var Citylocation=$("#Citylocation").val();
			       	 var Phonelocation=$("#Phonelocation").val();
			       	 var Statelocation=$("#Statelocation").val();
	document.location.href = "clientsortByBName.htm?flag="+flag+"&&page="+currentPage+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
}
function getAddress(flag){
	 var checkedvalue='';
	 // check select status
			            $('.checkbox2').each(function() { //loop through each checkbox

			            	checkedvalue=this.checked;
			           //select all checkboxes with class "checkbox1"               
			            });
			            var businessname=$("#companyName").val();
				       	 var brands=$("#brands").val();
				       	 var locationAddress=$("#Addresslocation").val();
				       	 //locationstore
				       	 var locationstore=$("#locationstore").val();
				       	 var ZipCodelocation=$("#ZipCodelocation").val();
				       	 var Citylocation=$("#Citylocation").val();
				       	 var Phonelocation=$("#Phonelocation").val();
				       	 var Statelocation=$("#Statelocation").val();
	document.location.href = "clientsortByAddress.htm?flag="+flag+"&&page="+currentPage+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
}
function getCity(flag){
	 var checkedvalue='';
	 // check select status
			            $('.checkbox2').each(function() { //loop through each checkbox

			            	checkedvalue=this.checked;
			           //select all checkboxes with class "checkbox1"               
			            });
			            var businessname=$("#companyName").val();
				       	 var brands=$("#brands").val();
				       	 var locationAddress=$("#Addresslocation").val();
				       	 //locationstore
				       	 var locationstore=$("#locationstore").val();
				       	 var ZipCodelocation=$("#ZipCodelocation").val();
				       	 var Citylocation=$("#Citylocation").val();
				       	 var Phonelocation=$("#Phonelocation").val();
				       	 var Statelocation=$("#Statelocation").val();
	document.location.href = "clientsortByCity.htm?flag="+flag+"&&page="+currentPage+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
}
function getState(flag){
	 var checkedvalue='';
	 // check select status
			            $('.checkbox2').each(function() { //loop through each checkbox

			            	checkedvalue=this.checked;
			           //select all checkboxes with class "checkbox1"               
			            });
			            var businessname=$("#companyName").val();
				       	 var brands=$("#brands").val();
				       	 var locationAddress=$("#Addresslocation").val();
				       	 //locationstore
				       	 var locationstore=$("#locationstore").val();
				       	 var ZipCodelocation=$("#ZipCodelocation").val();
				       	 var Citylocation=$("#Citylocation").val();
				       	 var Phonelocation=$("#Phonelocation").val();
				       	 var Statelocation=$("#Statelocation").val();
	document.location.href = "clientsortByState.htm?flag="+flag+"&&page="+currentPage+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
}

function getZip(flag){
	 var checkedvalue='';
	 // check select status
			            $('.checkbox2').each(function() { //loop through each checkbox

			            	checkedvalue=this.checked;
			           //select all checkboxes with class "checkbox1"               
			            });
			            var businessname=$("#companyName").val();
				       	 var brands=$("#brands").val();
				       	 var locationAddress=$("#Addresslocation").val();
				       	 //locationstore
				       	 var locationstore=$("#locationstore").val();
				       	 var ZipCodelocation=$("#ZipCodelocation").val();
				       	 var Citylocation=$("#Citylocation").val();
				       	 var Phonelocation=$("#Phonelocation").val();
				       	 var Statelocation=$("#Statelocation").val();
	document.location.href = "clientsortByZip.htm?flag="+flag+"&&page="+currentPage+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
}
function getPhone(flag){
	 var checkedvalue='';
	 // check select status
			            $('.checkbox2').each(function() { //loop through each checkbox

			            	checkedvalue=this.checked;
			           //select all checkboxes with class "checkbox1"               
			            });
			            var businessname=$("#companyName").val();
				       	 var brands=$("#brands").val();
				       	 var locationAddress=$("#Addresslocation").val();
				       	 //locationstore
				       	 var locationstore=$("#locationstore").val();
				       	 var ZipCodelocation=$("#ZipCodelocation").val();
				       	 var Citylocation=$("#Citylocation").val();
				       	 var Phonelocation=$("#Phonelocation").val();
				       	 var Statelocation=$("#Statelocation").val();
	document.location.href = "clientsortByPhone.htm?flag="+flag+"&&page="+currentPage+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
}

function sendSubmitResult(){
	 var form = document.getElementById('loginForm');
	    form.action = "saveClientUser.htm";
	    form.submit();
	}
function addLocation() {
	$.session.set("isSave",true);
	document.location.href = "addClientLocation.htm";
}

function disableReadOnly() {
	$("table > tbody > tr > td > div.user > input").attr("readonly",false);
}

function search() {
	$("#searchAndFilterForm").attr('action',
			'clientBusinessSearch.htm');
} 

function searchListingActivityBrand() {
	 $("#searchBrandForm").attr('action',
				'searchClientListingActivity.htm'); 		
		
	}
	
function searchListingActivityByDate() {
	//alert("ji");
	 $("#searchBrandForm").attr('action',
				'searchClientListingActivity.htm');
	 var form = document.getElementById('searchBrandForm');
    form.action = "searchClientListingActivity.htm";
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
	$("#businessID").attr("href", "editClientBusinessInfo.htm?Id="+business);
	$('#showErrorMessages').bPopup();
	$("ul li").removeClass("selected");
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
	 
	 
	 $("#cancel").click(function(){	 	
	 		$("#cancelChanges").bPopup();
	 		$("ul li").removeClass("selected");
	 	});
	 
	 
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
		    $("#errorMessage").text("Please select a file to upload");
		    return false;
		   }else{
			   $('#statusPopup').bPopup();
			   $('#showuploadpopup').hide();
		    return true;
		   }
		   
		  });
	 
	
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
	setPaginationBackground();
	
	 var isAsc = true;
		$("thead#activityTableHeaders tr th.th_02").click(function(){			
			var currentClass = $(this).attr('id');
			//alert(currentClass);
			var currentClassArr = currentClass.split("_");
			//alert(currentClassArr[1]);
			var sortById = "td.td_"+currentClassArr[1]+" div";
		
			if(isAsc){
				isAsc = false;
				//alert(isAsc);
				$("table#displayActivity  tbody#activityInfo").jSort({
			        sort_by: sortById,
			        item: 'tr',
			        order: 'desc'
			    });			 
			}else{
				isAsc = true;
				 $("table#displayActivity  tbody#activityInfo").jSort({
				        sort_by: sortById,
				        item: 'tr',
				        order: 'asc'
				 });
			}			 
		});
	
 });
 

	function downloadInfo() {

		$("#businessListform").attr("action", "downloadClientBusiness");
	}

	function showDeletePopup() {

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
	function sendDeleteResult() {

		var form = document.getElementById('businessListform');
		form.action = "deleteClientBusinessInfo";
		form.submit();
	}

	function sendEditResult() {
		var checkBoxCount = 0;
		$('input[type=checkbox].checkbox1').each(function() {
			if ($(this).is(":checked")) {
				++checkBoxCount;
			}
		});
		var editType = "single";
		if (checkBoxCount > 1) {
			editType = "multiple";
		}
		$.session.set("editType", editType);
		$.session.set("isSave", false);
		var form = document.getElementById('businessListform');
		form.action = "editClientBusinessInfo";
		form.submit();
	}

	function showExportPopup() {

		
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
	function sendExportSubmit() {
		var checkedvalue='';

        $('.checkbox2').each(function() { 

        	checkedvalue=this.checked;
        });
       
		var serviceName = document.getElementById('serviceName').value;
		 if(serviceName.length == 0 || $(serviceName).val() == ""){
			   alert("Please select  any one template");
			   event.preventDefault();
		 }else{
		document.getElementById('templateName').value = serviceName;

		var form = document.getElementById('businessListform');
		form.action = "exportClientBusinessInfo.htm?checkedvalue="+checkedvalue;
		form.submit();
		 }
	}
	
	function listingsPagination(page) {

		$.session.set("currentPage",page);
			var checkedvalue='';

	            $('.checkbox2').each(function() { 

	            	checkedvalue=this.checked;
	            });
	           
	            $.session.set("currentPage",page);
	    		var flag=$("#flagidvalue").val();
	    		if(flag == 'ASC'){
	    			flag="DESC";
	    		}else{
	    			flag = 'ASC';
	    		}
	    		  var businessname=$("#companyName").val();
			       	 var brands=$("#brands").val();
			       	 var locationAddress=$("#Addresslocation").val();
			       	 //locationstore
			       	 var locationstore=$("#locationstore").val();
			       	 var ZipCodelocation=$("#ZipCodelocation").val();
			       	 var Citylocation=$("#Citylocation").val();
			       	 var Phonelocation=$("#Phonelocation").val();
			       	 var Statelocation=$("#Statelocation").val();
	            var selecttype=$("#selectType").val();
	            //alert(selecttype);
	            if(selecttype ==''){
	            	document.location.href="client-dashboard_page.htm?page="+page+"&&checkedvalue="+checkedvalue ;	
	            }
	           if(selecttype=='store'){
	        	   var checkedvalue='';
	        		 // check select status
	        				            $('.checkbox2').each(function() { //loop through each checkbox

	        				            	checkedvalue=this.checked;
	        				           //select all checkboxes with class "checkbox1"               
	        				            });
	        	   document.location.href = "clientsortByStore.htm?flag="+flag+"&&page="+page+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
	           }
	           if(selecttype== 'businessname'){
	        	   var checkedvalue='';
	        				            $('.checkbox2').each(function() { //loop through each checkbox

	        				            	checkedvalue=this.checked;
	        				            });
	        	   document.location.href = "clientsortByBName.htm?flag="+flag+"&&page="+page+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
	           }
	           if(selecttype== 'address'){
	        	   var checkedvalue='';
	        		 // check select status
	        				            $('.checkbox2').each(function() { //loop through each checkbox

	        				            	checkedvalue=this.checked;
	        				           //select all checkboxes with class "checkbox1"               
	        				            });
	        	   document.location.href = "clientsortByAddress.htm?flag="+flag+"&&page="+page+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
	           }
	           if(selecttype== 'city'){
	        	   var checkedvalue='';
	        				            $('.checkbox2').each(function() { //loop through each checkbox

	        				            	checkedvalue=this.checked;
	        				            });
	        	   document.location.href = "clientsortByCity.htm?flag="+flag+"&&page="+page+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
	           }
	           if(selecttype== 'state'){
	        	   var checkedvalue='';
	        				            $('.checkbox2').each(function() { //loop through each checkbox

	        				            	checkedvalue=this.checked;
	        				            });
	        	   document.location.href = "clientsortByState.htm?flag="+flag+"&&page="+page+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
	           }
	           if(selecttype== 'zip'){
	        	   var checkedvalue='';
	        				            $('.checkbox2').each(function() { //loop through each checkbox

	        				            	checkedvalue=this.checked;
	        				            });
	        	   document.location.href = "clientsortByZip.htm?flag="+flag+"&&page="+page+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
	           }
	           if(selecttype== 'phone'){
	        	   var checkedvalue='';
	        				            $('.checkbox2').each(function() { 

	        				            	checkedvalue=this.checked;
	        				            });
	        	   document.location.href = "clientsortByPhone.htm?flag="+flag+"&&page="+page+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
	           };
		//alert("page :: "+page);
		/* $.session.set("currentPage",page);
		document.location.href="client-dashboard_page.htm?page="+page; */	
	}
	
	function paginationPre() {
		//var currentPageNum = getCurrentPageNum();
		//alert(currentPageNum);
		if (currentPage > 1) {
			--currentPage;
		}
		//alert(kNum);
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

<div class="popup" id="PasswordPopUp" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		 <span class="buttonIndicator b-close"><span>X</span></span>
		<span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">
		Password cannot be blank.
				
		</div>
	</div>
</div>
<div class="popup" id="statusPopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span >Message</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons"><span style="color: green">Uploading is in progress. This process may take several minutes. Please wait for upload to complete...</span></div>
	</div>
</div>
<div class="popup" id="cancelChanges" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		 <span class="buttonIndicator b-close"><span>X</span></span>
		<span>Cancel Changes</span>
	</div>
	<div class="pp-subheader">You have selected to Cancel the changes you have entered.<br>If you click OK, any changes made will not be saved.</div>
	<div class="pp-body">
		<div class="buttons">
			<a href="dashboardClient.htm" class="btn_dark_blue_2">Ok</a>
			<a href="#" class="btn_dark_blue_2 b-close">Continue Edit</a>
		</div>
	</div>
</div>

<div class="popup" id="ConfirmPasswordPopUp" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		 <span class="buttonIndicator b-close"><span>X</span></span>
		<span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">
		Enter Confirm Password Same as Password.
				
		</div>
	</div>
</div>
<div class="popup" id="exportrecordselectionpopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		 <span class="buttonIndicator b-close"><span>X</span></span>
		<span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">
		please select at least one record for export.
				
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
		please select at least one record for delete.				
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
		<span class="buttonIndicator b-close"><span>X</span></span>
		Upload File
	</div>
	<div class="pp-subheader">Make sure you are using our <a href="download.htm">bulk upload template</a> for your data</div>
	<div class="pp-body">
	<spring:form action="uploadBusiness.htm?page=dashboardclient" commandName="uploadBusiness"
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
	<spring:form action="uploadBusiness.htm?page=dashboardclient" commandName="uploadBusiness"
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
			<a href="clientViewListing.htm" class="btn_dark_blue_2">View Listings</a>
			<a href="clientListing-error.htm" style=" margin-left: 251px;margin-top: -46px;" class="btn_dark_blue_2">View Errors</a>
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
   <span class="btn_dark_blue_2 b-close">Cancel</span>
  </div>
 </div>
</div>

<div class="popup" id="displaySaveConfirmationpopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span>
		<span>Save Changes</span>
	</div>
	<div class="pp-subheader">Do you want to save the changes you have<br>entered for this location?</div>
	<div class="pp-body">
		<div class="buttons">
			 <button onclick="sendSubmitResult();" class="btn_dark_blue_2">Ok</button>
			 <button class="btn_dark_blue_2 b-close">Continue Edit</button>
		</div>
	</div>
</div>

<body id="office">
<!-- page wrapper -->
<div class="wrapper"> 
	<!-- header -->
	<div class="header"> <a href="dashboardClient.htm" class="logo_lbl">Local Business Listings</a>
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
			<li class="si_dashboard selected"><a href="dashboardClient.htm">Dashboard</a></li>
			<!-- <li class="si_business_listings"><a href="clientbusinesslisting.htm">Business Listings</a></li> -->
			<li class="si_error_listings"><a href="listingClient-error.htm">Listing Errors</a></li>
			<li class="si_reports"><a href="client-reports.htm">Reports</a></li>
			<!-- <li class="si_toolbox"><a href="clntToolbox.htm">Convergent Toolbox</a></li> -->
		</ul>
		<!-- // left side navigation --> 
		<!-- content area -->
		<div class="content" id="id_content">
			<div class="nav_pointer pos_01"></div>
			<!-- subheader -->
			<div class="subheader clearfix">
				<h1>DASHBOARD</h1>
				<p>${channelName}</p>
			</div>
			<!-- // subheader -->
			<div class="inner_box"> 
			<input type="hidden" value="${companyName }" id="companyName" /> <input
						type="hidden" value="${locationstore }" id="locationstore" name="storeval"/> <input
						type="hidden" value="${locationAddress }" id="Addresslocation" />
					<input type="hidden" value="${locationState }" id="Statelocation" />
					<input type="hidden" value="${locationCity }" id="Citylocation" />
					<input type="hidden" value="${locationZipCode }"
						id="ZipCodelocation" /> <input type="hidden" name="brandval" value="${brands }"
						id="brands" /> <input type="hidden" value="${locationPhone }"
						id="Phonelocation" />
						
				<spring:form method="post" commandName="businessSearch"  id="searchAndFilterForm">
				<div class="filters-2">
					<label>Brand Name&nbsp;<spring:input path="client"  style="width:140px"/></label>
					<label>Business Name&nbsp;<spring:input path="companyName" style="width:140px"/></label>
					<label>Store #&nbsp;<spring:input path="store" style="width:55px"/></label>
					<label>Phone #&nbsp;<spring:input path="locationPhone" style="width:140px"/></label>
					<label>Address&nbsp;<spring:input path="locationAddress" style="width:140px"/></label>
					<label>City&nbsp;<spring:input path="locationCity" style="width:100px"/></label>
					<label>State&nbsp;<!-- <select>
							<option></option>
						</select> --><spring:input path="locationState" style="width:100px"/>
					</label>
					<label>Zip&nbsp;<spring:input path="locationZipCode" style="width:55px"/></label>
					<!-- <a href="#" class="btn_dark_blue_2">Apply FIlter</a> -->
					<input type="submit" value="Apply FIlter" onclick="search()" class="btn_dark_blue_2">
					<a href="dashboardClient.htm"  class="btn_dark_blue_2">CLEAR FIlter</a>
					<!-- <a href="#" class="btn_grey_3 plus"><span>Add/Remove Fields</span></a> -->
						
				</div>
				</spring:form>
				<!-- box -->
				<div class="box box_red_title business-listings"> 
					<!-- title -->
					<div class="box_title">
						<h2>Active Locations: ${activeListSize}</h2>						
						<div class="box_title_right"> <span>Locations With Errors: ${errorListSize}</span><a href="listingClient-error.htm"  class="btr_button">FIX ERRORS</a> </div>
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
									<core:set value="${checked }" var="cheked" scope="page"></core:set>
										<input type="hidden" value="${checked }" />
										<input type="hidden" value="${flagvalue }" id="flagidvalue" />

										<input type="hidden" value="${companyName }" id="companyName" />
										<input type="hidden" value="${locationstore }"
											id="locationstore" name="storeval" />
										<input type="hidden" value="${locationAddress }"
											id="Addresslocation" />
										<input type="hidden" value="${locationState }"
											id="Statelocation" />
										<input type="hidden" value="${locationCity }"
											id="Citylocation" />
										<input type="hidden" value="${locationZipCode }"
											id="ZipCodelocation" />
										<input type="hidden" name="brandval" value="${brands }"
											id="brands" />
										<input type="hidden" value="${locationPhone }"
											id="Phonelocation" />

										<input type="hidden" value="${selectType }" id="selectType" />
								<tr class="odd">
									<core:choose>

										<core:when
											test="${cheked eq 'true' and checkedvalue eq 'true'}">
											<th class="th_01"><div>
													<input type="checkbox" id="selecctall" class="checkbox2"
														checked="checked">
												</div></th>

										</core:when>
										<core:otherwise>
											<th class="th_01"><div>
													<input type="checkbox" id="selecctall" class="checkbox2">
												</div></th>

										</core:otherwise>
									</core:choose>
								<th class="th_02"><div><img src="images/icon-!.png" align="!"></div></th>
								<th class="th_03" onclick="getStores('${flagvalue}')"><div >Store#</div></th>
								<th class="th_04" onclick="getBusinessname('${flagvalue}')"><div>Business Name</div></th>
								<th class="th_05" onclick="getAddress('${flagvalue}')"><div>Address</div></th>
								<th class="th_06" onclick="getCity('${flagvalue}')"><div>City</div></th>
								<th class="th_07" onclick="getState('${flagvalue}')"><div>State</div></th>
								<th class="th_08" onclick="getZip('${flagvalue}')"><div>Zip</div></th>
								<th class="th_09" onclick="getPhone('${flagvalue}')"><div>Phone</div></th>
							</tr>
						</thead>
					</table>
					<div id="id_business_listings-2">
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
							<tbody id="businessInfo">
							<core:forEach items="${allBusinessInfo}" var="bean" varStatus="i">
						
								<tr class="odd">
									<input type="hidden" value="${checked }" />

										<tr class="odd">
											<core:choose>

												<core:when
													test="${cheked eq 'true' and checkedvalue eq 'true'}">
													<td class="td_01"><div>
															<input type="checkbox" class="checkbox1"
																checked="checked" value="${bean.id}" name="id">
														</div></td>

												</core:when>
												<core:otherwise>
													<td class="td_01"><div>
															<input type="checkbox" class="checkbox1"
																value="${bean.id}" name="id">
														</div></td>

												</core:otherwise>
											</core:choose>
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
							<span id="page_first" class="next"><a href="#" onclick="listingsPagination(1);" style="font-size: 14px ; font-weight: 600">first</a> </span>&nbsp;
							<span id="page_prev"><a href="#" onclick="paginationPre();" style="font-size: 14px ; font-weight: 600">prev</a> </span>&nbsp;
							
							<span id="pageNums" style="font-size: 14px ; font-weight: 600">
							
							</span>
							<span id="page_next"><a href="#" onclick="paginationNext(${numOfPages});" style="font-size: 14px ; font-weight: 600">next</a> </span>&nbsp;
							<span id="page_last"><a href="#" onclick="listingsPagination(${numOfPages});" style="font-size: 14px ; font-weight: 600">last</a> </span>&nbsp;
					</core:if>
				</div>
				<div class="buttons-center">
						<a href="#" onclick="sendEditResult()"   class="btn_dark_blue_2">Edit</a>
			    				<!--  <button  onclick="downloadInfo();" class="btn_dark_blue_2" >Download</button> -->
			    				
			    			<%	Boolean isViewOnly = (Boolean) session.getAttribute("isViewOnly");
							if(!isViewOnly) { %>
								<a href="#" onclick="showUplaodPopup();" class="btn_dark_blue_2">Upload</a>
								<a href="#" onclick="showDeletePopup();" class="btn_dark_blue_2" >Delete</a>
			    		 		<a href="#" class="btn_dark_blue_2" onclick="addLocation();">Add/Create Location</a>
							<% }%>
			    				
			   			 <a href="#" onclick="showExportPopup();" class="btn_dark_blue_2">Export</a>

				</div>
			</spring:form>
			</div>
			<div class="inner-box-2">
				<div class="inner-box-left">
					<!-- box -->
					<div class="box box_darkblue_title listing_activity" style="margin-bottom:0">
						<!-- title -->
						<div class="box_title">
							<h2>Listing Activity</h2>
						</div>
						<spring:form method="post" commandName="searchBusiness"
									id="searchBrandForm">
						<!-- // title -->
							<div class="filters">
								<div class="f_group">
									<label for="search_brands">Search Brands</label>
									 <spring:input path="brands" style="width:100px;" />
									<button class="btn_search"
										onclick="searchListingActivityBrand()">
										<span>search</span>
									</button>
								</div>
								<div class="f_group">
									<label for="display_activity_for">Display Activity for</label>
									<spring:select path="dateRange" id="display_activity_for"
										onchange="searchListingActivityByDate()">
										<spring:option value="all">all</spring:option>
										<spring:option value="this month">this month</spring:option>
										<spring:option value="last month">last month</spring:option>
										<spring:option value="last 3 months">last 3 months</spring:option>

									</spring:select>
								</div>
							</div>
							</spring:form>
				     <table width="100%" class="grid grid_07 grid_07_1">
      <colgroup>
      <col width="20%" />
      <col width="25%" />
      <col width="35%" />
      <col width="20%" />
      </colgroup>
      <thead id="activityTableHeaders">
       <tr>
        <th class="th_02" id="th_02"><div>Date</div></th>
        <th class="th_02" id="th_03"><div>Brand Name</div></th>
        <th class="th_02" id="th_04"><div>Activity Description</div></th>
        <th class="th_02" id="th_05" onclick="sortByCount('${flagvalue}')"><div>Count</div></th>
       </tr>
      </thead>
     </table>
     <div id="id_listing_activity">
     
      <table id="displayActivity" width="100%" class="grid grid_07 grid_07_1">
       <colgroup>
       <col width="20%"  />
       <col width="25%" />
       <col width="35%" />
       <col width="20%" />
       </colgroup>
       <tbody id="activityInfo">
      
         <core:forEach items="${listingActivityInfo}" var="activity" varStatus="i">
        <tr class="odd">
         <td class="td_02"><div><core:out value="${activity.date}"></core:out></div></td>
         <td class="td_03"><div><core:out value="${activity.brandName}"></core:out></div></td>
         <td class="td_04"><div><core:out value="${activity.activityDescription}"></core:out></div></td>
         <td class="td_05"><div><core:out value="${activity.count}"></core:out></div></td>
        </tr>
  
          </core:forEach>
        </tbody>
    
       
      </table>
   
     </div>
					</div>
				</div>
				<div class="inner-box-right">
					<div class="inner">
						<div class="sb_box_wrapper">
							<!-- title -->
							<div class="sb_title sb_title_ico ico_sb_user">
								<h2 class="">User Profile</h2>
							</div>
							<!-- // title --> 
							<!-- sidebar box -->
							<div class="sb_box sb_white sb_rounded sb_padding_20">
								<spring:form action="adminUser.htm" commandName="adminUser" id="loginForm" >
								<table width="100%" class="grid grid_14 mb5">
									<colgroup>
										<col width="45%"  />
										<col width="55%" />
									</colgroup>
									<tbody>
										<tr>
											<td><div>Name, Lastname</div></td>
											<td><div class="user"><spring:input readonly="true"  path="fullName" id="searchField" /> </div></td>
										</tr>
										<tr>
											<td><div>User Name</div></td>
											<td><div ><spring:input path="userName" readonly="true"  /></div></td>
										</tr>
										<tr>
											<td><div>Password</div></td>
											<td><div class="user"><spring:password path="passWord" readonly="true"  id="passcss" class="password" showPassword="true" /></div></td>
										</tr>
										<tr>
											<td><div>Confirm Password</div></td>
											<td><div class="user"><spring:password path="confirmPassWord" readonly="true"  id="passcss" class="confpass"  showPassword="true" /></div></td>
										</tr>
										<tr>
											<td><div>Phone</div></td>
											<td><div class="user"><spring:input   path="phone" readonly="true"  /></div></td>
										</tr>
										<tr>
											<td><div>Email</div></td>
											<td><div class="user"> <spring:input  path="email"  readonly="true" /></div></td>
										</tr>
									</tbody>
									<spring:hidden path="userID" />
									<spring:hidden path="brandID"/>
								</table>
								
								<div class="sb_buttons_center-2" >
									<a href="#" id="edit" class="btn_dark_blue_2">Edit</a>
									<a id="cancel" href="#" class="btn_dark_blue_2">Cancel</a>
									<a href="#" onclick="saveConfirmation();" id="saveConfirmationId" class="btn_dark_blue_2">Save</a>
								</div>
								</spring:form>
							</div>
							<!-- // sidebar box --> 
						</div>
					</div>
					</div>
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