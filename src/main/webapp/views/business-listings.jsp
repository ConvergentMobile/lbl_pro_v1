<%@page import="com.business.common.util.LBLConstants"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<script type="text/javascript" src="js/jquery.form.js"></script>
<link rel="stylesheet" href="js/jquery.mCustomScrollbar.css">
<script src="js/jquery.mCustomScrollbar.js"></script>
<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script> 
<script type="text/javascript" src="js/jquery.form.js"></script>
<!-- <script src="js/jquery.1.12.4-min.js"></script> -->
<%
	int currentPage = (Integer) request.getAttribute("currentPage");
	int numOfPages = (Integer) request.getAttribute("numOfPages");
%>

<!-- <script>
	$(document).ready(function() {
		//elements
		var progressbox = $('#progressbox');
		var progressbar = $('#progressbar');
		var statustxt = $('#statustxt');
		var submitbutton = $("#SubmitButton");
		var myform = $("#UploadForm");
		var output = $("#output");
		var completed = '0%';

		$(myform).ajaxForm({
			beforeSend : function() { //brfore sending form
				submitbutton.attr('disabled', ''); // disable upload button
				statustxt.empty();
				progressbox.slideDown(); //show progressbar
				progressbar.width(completed); //initial value 0% of progressbar
				statustxt.html(completed); //set status text
				statustxt.css('color', '#000'); //initial color of status text
			},
			uploadProgress : function(event, position, total, percentComplete) { //on progress
				progressbar.width(percentComplete + '%'); //update progressbar percent complete
				statustxt.html(percentComplete + '%'); //update status text
				if (percentComplete > 50) {
					statustxt.css('color', '#fff'); //change status text to white after 50%
				}
			},
			complete : function(response) { // on complete
				output.html(response.responseText); //update element with received data
				myform.resetForm(); // reset form
				submitbutton.removeAttr('disabled'); //enable submit button
				progressbox.slideUp(); // hide progressbar

			}
		});
		$("#showuploadpopup").hide();
	});
</script> -->

<script type="text/javascript">
var currentPage = '<%=currentPage%>';
var numOfPages = '<%=numOfPages%>';



<%-- var flag= '<%=flagvalue%>';

alert("flag from request"+flag); --%>


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
	
	
	var exportMessage=$("#exportMessage").val();
	//alert(exportMessage);
	if(exportMessage!=''){
		$('#showexportMessage').bPopup();	
		 $('.checkbox1').each(function() { //loop through each checkbox
             this.checked = false; //deselect all checkboxes with class "checkbox1"                       
         });  
	}
    $('#selecctall').click(function(event) { 
    	//alert("coming");//on click 
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
	/* $('#progresspopup').bPopup(); */
	$("ul li").removeClass("selected");
}
/* function upload(){
	
	
	$('#progressbox').bPopup();
	$("ul li").removeClass("selected");
} */


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
 var allowedExtensions = new Array("xls","xlsx","csv");
 for(var ct=0;ct<allowedExtensions.length;ct++)
 {
  sample = v.lastIndexOf(allowedExtensions[ct]);
  if(sample != -1){return true;}
  }
 return false;
 }
 function ClearFilter(){
	 var businessname=$("#companyName").val();
	 businessname="";
	 var brands=$("#brands").val();
	 brands="";
	 var locationAddress=$("#Addresslocation").val();
	 locationAddress="";
	 var locationstore=$("#locationstore").val();
	 locationstore="";
	 var ZipCodelocation=$("#ZipCodelocation").val();
	 ZipCodelocation="";
	 var Citylocation=$("#Citylocation").val();
	 Citylocation="";
	 var flag="";
	 var checkedvalue='';
	 // check select status
			            $('.checkbox2').each(function() { //loop through each checkbox

			            	checkedvalue=this.checked;
			           //select all checkboxes with class "checkbox1"               
			            });
	 var currentPage="1";
	
	 var Phonelocation=$("#Phonelocation").val();
	 Phonelocation="";
	 var Statelocation=$("#Statelocation").val();
	 Statelocation="";
	
	 document.location.href = "businesslistings.htm?flag="+flag+"&&page="+currentPage+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
 }
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


	document.location.href = "sortByStore.htm?flag="+flag+"&&page="+currentPage+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;


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
	document.location.href = "sortByBName.htm?flag="+flag+"&&page="+currentPage+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
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
	document.location.href = "sortByAddress.htm?flag="+flag+"&&page="+currentPage+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
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
	document.location.href = "sortByCity.htm?flag="+flag+"&&page="+currentPage+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
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
	document.location.href = "sortByState.htm?flag="+flag+"&&page="+currentPage+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
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
	document.location.href = "sortByZip.htm?flag="+flag+"&&page="+currentPage+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
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
	document.location.href = "sortByPhone.htm?flag="+flag+"&&page="+currentPage+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
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
		    $("#errorMessage").text("Please select a file to upload");
		    return false;
		   }else{
			   $('#statusPopup').bPopup();
			   $('#showuploadpopup').hide();
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
    });  */
    
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
	function MoveToBusinessListings(){
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
		     form.action = "movetoListings.htm";
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
			}	;	
		};
	function sendExportSubmit(){
		var checkedvalue='';

        $('.checkbox2').each(function() { 

        	checkedvalue=this.checked;
        });
		 var serviceName = document.getElementById('serviceName').value;
		 
		 if(serviceName.length == 0 || $(serviceName).val() == ""){
			 $('#templatetemplatepopup').bPopup();
			 $("ul li").removeClass("selected");
			   //alert("Please select  any one template");
			   event.preventDefault();
		 }else{
			 document.getElementById('templateName').value=serviceName;
				
			  var form = document.getElementById('businessListform');
			     form.action = "exportBusinessInfo.htm?checkedvalue="+checkedvalue;
			     form.submit(); 
		 }
				
		}
	
	function addLocation() {
		$.session.set("isSave",true);
		document.location.href = "addLocation.htm";
	}
	
	function listingsPagination(page) {

  
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
		            	document.location.href="business-listings_page.htm?page="+page+"&&checkedvalue="+checkedvalue ;	
		            }
		           if(selecttype=='store'){
		        	   var checkedvalue='';
		        		 // check select status
		        				            $('.checkbox2').each(function() { //loop through each checkbox

		        				            	checkedvalue=this.checked;
		        				           //select all checkboxes with class "checkbox1"               
		        				            });
		        	   document.location.href = "sortByStore.htm?flag="+flag+"&&page="+page+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
		           }
		           if(selecttype== 'businessname'){
		        	   var checkedvalue='';
		        				            $('.checkbox2').each(function() { //loop through each checkbox

		        				            	checkedvalue=this.checked;
		        				            });
		        	   document.location.href = "sortByBName.htm?flag="+flag+"&&page="+page+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
		           }
		           if(selecttype== 'address'){
		        	   var checkedvalue='';
		        		 // check select status
		        				            $('.checkbox2').each(function() { //loop through each checkbox

		        				            	checkedvalue=this.checked;
		        				           //select all checkboxes with class "checkbox1"               
		        				            });
		        	   document.location.href = "sortByAddress.htm?flag="+flag+"&&page="+page+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
		           }
		           if(selecttype== 'city'){
		        	   var checkedvalue='';
		        				            $('.checkbox2').each(function() { //loop through each checkbox

		        				            	checkedvalue=this.checked;
		        				            });
		        	   document.location.href = "sortByCity.htm?flag="+flag+"&&page="+page+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
		           }
		           if(selecttype== 'state'){
		        	   var checkedvalue='';
		        				            $('.checkbox2').each(function() { //loop through each checkbox

		        				            	checkedvalue=this.checked;
		        				            });
		        	   document.location.href = "sortByState.htm?flag="+flag+"&&page="+page+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
		           }
		           if(selecttype== 'zip'){
		        	   var checkedvalue='';
		        				            $('.checkbox2').each(function() { //loop through each checkbox

		        				            	checkedvalue=this.checked;
		        				            });
		        	   document.location.href = "sortByZip.htm?flag="+flag+"&&page="+page+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
		           }
		           if(selecttype== 'phone'){
		        	   var checkedvalue='';
		        				            $('.checkbox2').each(function() { 

		        				            	checkedvalue=this.checked;
		        				            });
		        	   document.location.href = "sortByPhone.htm?flag="+flag+"&&page="+page+"&&cv="+checkedvalue+"&&bn="+businessname+"&&b="+brands+"&&la="+locationAddress+"&&s="+locationstore+"&&zl="+ZipCodelocation+"&&cl="+Citylocation+"&&pl="+Phonelocation+"&&sl="+Statelocation;
		           };


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

<div class="popup" id="exportrecordselectionpopup"
	style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select at least one record for
			export.</div>
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
<div class="popup" id="Deleterecordselectionpopup"
	style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select at least one record for
			delete.</div>
	</div>
</div>

<div class="popup" id="templatetemplatepopup" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select ant one template.</div>
	</div>
</div>
<div class="popup" id="showexportMessage" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span></span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons" ><span style="color: green">Listings exported successfully.</span></div>
	</div>
</div>


<div class="popup" id="displayExportFilepopup" style="display: none">
	<div class="pp-header">
		<span class="buttonIndicator b-close"><span>X</span></span> Export
		File
	</div>
	<div class="pp-subheader">Your selected locations will be
		formatted in the template chosen below</div>
	<div class="pp-body">
		<div class="export">
			<select name="service" id="serviceName">
				<option value="">Choose Template</option>
				<option value="BingTemplate">Bing</option>
				<option value="GoogleTemplate">Google</option>
				<option value="MasterTemplate">Master Template</option>
				<option value="YPTemplate">YP</option>
				<option value="yelpTemplate">Yelp</option>
				<option value="yextTemplate">Yext</option>
				<option value="InfogroupTemplate">Infogroup</option>
				<option value="LocalezeTemplate">Localeze</option>
				<option value="AcxiomUsTemplate">Acxiom_US</option>
				<option value="AcxiomCanTemplate">Acxiom_Canada</option>

			</select>
			<button onclick="sendExportSubmit();" class="btn_dark_blue_2">Export</button>
		</div>
	</div>
</div>

<div class="popup error" id="showErrorMessages" style="display: none">
	<div class="pp-header">
		<!--  <div class="close"></div>  -->
		<span class="buttonIndicator b-close"><span>X</span></span> <img
			style="margin-top: 2px;" src="images/ico_title_error.png" /> <span>Listing
			Error</span>
		<!--  <span class="b-close"><span>X</span></span> -->
	</div>
	<div class="pp-subheader">
		Liberty Tax Service: Store # <span id="displayStoreID"></span>
	</div>
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
		Make sure you are using our <a href="download.htm">bulk upload
			template</a> for your data
	</div>
	<div id="progressbox">

		<div id="progressbar"></div>
		<div id="statustxt">0%</div>

	</div>

	<div id="output" style="display: none"></div>
	<div class="pp-body">
		<spring:form action="uploadBusiness.htm?page=businessListings"
			id="UploadForm" commandName="uploadBusiness"
			enctype="multipart/form-data">

			<div class="upload">
				<p id="errorMessage" style="color: red;"></p>
				<input type="file" id="UploadFileName" name="targetFile"
					class="btn_grey_2" value="Upload"
					onChange="load_File(this.id,this.value)" /> <input type="submit"
					id="uploadSubmission" class="btn_dark_blue_2" value="Upload"
					onclick="upload()" />
			</div>
		</spring:form>
	</div>
</div>

<core:if test="${not empty headerPopup}">
	<input type="hidden" value="${headerPopup}" id="uploadHeaderResult">
	<div class="popup" id="displayUploadHeaderResult" style="display: none">
		<div class="pp-header">
			<!-- <div class="close"></div> -->
			<span class="buttonIndicator b-close"><span>X</span></span> Upload
			File
		</div>
		<div class="pp-subheader">
			Make sure you are using our <a href="download.htm">bulk upload
				template</a> for your data
		</div>
		<div class="pp-body">
			<spring:form action="uploadBusiness.htm?page=businessListings"
				commandName="uploadBusiness" enctype="multipart/form-data">


				<div class="result error">${headerPopup}</div>

				<div class="upload">
					<p id="errorMessage" style="color: red;"></p>

					<input type="file" id="UploadFileName" name="targetFile"
						class="btn_grey_2" value="Upload"
						onChange="load_File(this.id,this.value)" /> <input type="submit"
						id="uploadSubmission" class="btn_dark_blue_2" value="Upload" />
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
			<span class="buttonIndicator b-close"><span>X</span></span> Upload
			File
		</div>
		<div class="pp-body">
			<div class="result success">${message}</div>
			<core:if test="${fn:contains(message, 'Upload complete')}">
				<div class="result-details">
					<ul>
						<li><span>${listingsUploaded }</span> Listings Uploaded</li>
						<li><span>${listingsCompleted }</span> Listings Completed</li>
						<li class="red"><span>${listingsWithErrors }</span> Listings
							with Errors</li>
					</ul>
					<a href="view-listings.htm" class="btn_dark_blue_2" style=" margin: 0px 0px 61px;">View
						Listings</a> <a href="view-errors.htm"
						style="margin-left: 251px; margin-top: -46px;"
						class="btn_dark_blue_2">View Errors</a>
				</div>
			</core:if>
		</div>
	</div>
</core:if>

<div class="popup" id="displayDeleteConfirmationpopup"
	style="display: none">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> Delete
		Location
	</div>
	<div class="pp-subheader">
		You have selected to Delete the selected listing(s).<br> <span
			class="red">This will permanently remove the listing(s) and
			cannot be undone.</span>
	</div>
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
				<li class="si_business_listings selected"><a
					href="business-listings.htm">Business Listings</a></li>
				<li class="si_error_listings"><a href="listing-error.htm">Listing
						Errors</a></li>
				<li class="si_upload_export"><a href="upload-export.htm">Upload/Export</a></li>
				<li class="si_reports"><a href="reports.htm">Reports</a></li>
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
				<!-- <li class="si_schduler"><a href="scheduler.htm">Schedule</a></li> -->
				

				<!--  <li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent
						Toolbox</a></li>-->
			</ul>
			<!-- // left side navigation -->
			<!-- content area -->
			<div class="content" id="id_content">
				<div class="nav_pointer pos_01"></div>
				<!-- subheader -->
				<div class="subheader clearfix">
					<h1>BUSINESS LISTINGS</h1>
					<p>${channelName}</p>
				</div>
				<!-- // subheader -->
				
				  <script>
				$(document).ready(function(e) {
                    $( ".advanced" ).click(function() {
					  $( ".filters-2" ).toggle("slow");
					});
                });
			</script>
				
				<div class="inner_box">
				<spring:form method="post" commandName="businessSearch"
						id="searchAndFilterForm">
					<div class="quick-form">
                	<div class="styled-select">
                        <spring:select name="menu-4562" class="searchby" path="searchType">
                            <spring:option value="sbBR">Search by Brand Name</spring:option>
                            <spring:option value="sbBN">Search by Business Name</spring:option>
                            <spring:option value="sbS">Search by Store #</spring:option>
                            <spring:option value="sbP">Search by Phone Number</spring:option>
                        </spring:select>
                     </div>
                    <spring:input path="searchValue" class="search-field" placeholder="search text"/>
                    <input type="image" src="images/search-icon.png"  value="Apply FIlter" onclick="search()"/>
                    <a href="#" onclick="ClearFilter()" class="small">Clear</a>
                    <div class="advanced">Advanced Search</div>
                   
                </div>
				
					<input type="hidden" value="${companyName }" id="companyName" /> <input
						type="hidden" value="${locationstore }" id="locationstore" name="storeval"/> <input
						type="hidden" value="${locationAddress }" id="Addresslocation" />
					<input type="hidden" value="${locationState }" id="Statelocation" />
					<input type="hidden" value="${locationCity }" id="Citylocation" />
					<input type="hidden" value="${locationZipCode }"
						id="ZipCodelocation" /> <input type="hidden" name="brandval" value="${brands }"
						id="brands" /> <input type="hidden" value="${locationPhone }"
						id="Phonelocation" />
						<input type="hidden" value="${exportMessage }" id="exportMessage" />
						
					
						<div class="filters-2">
					<label><spring:input path="client" placeholder="Brand Name" /></label>
					<label><spring:input path="companyName" placeholder="Business Name" /></label>
					<label><spring:input path="store" placeholder="Store #" /></label>
					<label><spring:input path="locationPhone" placeholder="Phone" /></label>
					<label><spring:input path="locationAddress" placeholder="Address" /></label>
					<label><spring:input path="locationCity" placeholder="City" /></label>
					<label><!-- <select>
							<option></option>
						</select> --><spring:input path="locationState" placeholder="State" />
					</label>
					<label><spring:input path="locationZipCode" placeholder="Zip" /></label>
					
					<input type="hidden" value="${errormessage }" id="validErrorMessage">
					
					<!-- <input type="submit" value="Apply FIlter" onclick="search()" class="btn_dark_blue_2">
					<a href="#" onclick="ClearFilter()"  class="btn_dark_blue_2">CLEAR FIlter</a> -->
					
					<!-- <a href="#" class="btn_grey_3 plus"><span>Add/Remove Fields</span></a> -->
					</div>
					</spring:form>
				
				<!-- box -->
				<div class="box box_red_title business-listings">
					<!-- title -->
					<div class="box_title">
						<h2>Active Locations: ${activeListSize}</h2>
						<div class="box_title_right">
							<span>Locations With Errors: ${errorListSize}</span><a
								href="listing-error.htm" class="btr_button">FIX ERRORS</a>
						</div>
					</div>
					<spring:form name="box" id="businessListform" action=""
						method="POST">
						<!-- // title -->

						<input type="hidden" id="templateName" name="serviceName">

						<table width="100%" class="grid grid_13">
							<colgroup>
								<col width="6%" />
								<col width="15%" />
								<col width="24%" />
								<col width="18%" />
								<col width="13%" />
								<col width="7%" />
								<col width="6%" />
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
									<input type="hidden" value="${locationCity }" id="Citylocation" />
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
									<!-- 	<th class="th_01"><div><input type="checkbox" id="selecctall" class="checkbox2" checked="checked"></div></th>
								<th class="th_01"><div><input type="checkbox" id="selecctall" class="checkbox2"></div></th> -->
									<!-- <th class="th_02"><div>
											<img src="images/icon-!.png" align="!">
										</div></th> -->


									<th class="th_03" id="store"
										onclick="getStores('${flagvalue}')"><div>Store#</div></th>
									<th class="th_04" id="businessname"
										onclick="getBusinessname('${flagvalue}')"><div>Business
											Name</div></th>
									<th class="th_05" id="address"
										onclick="getAddress('${flagvalue}')"><div>Address</div></th>
									<th class="th_06" id="city" onclick="getCity('${flagvalue}')"><div>City</div></th>
									<th class="th_07" id="state" onclick="getState('${flagvalue}')"><div>State</div></th>
									<th class="th_08" id="zip" onclick="getZip('${flagvalue}')"><div>Zip</div></th>
									<th class="th_09" id="phone" onclick="getPhone('${flagvalue}')"><div>Phone</div></th>
								</tr>
							</thead>
						</table>
						<div id="id_business_listings" class="business_listings_cls">
							<table id="businessTable" width="100%" class="grid grid_13">
								<colgroup>
									<col width="6%" />
									<col width="15%" />
									<col width="24%" />
									<col width="18%" />
									<col width="13%" />
									<col width="7%" />
									<col width="6%" />
									<col width="11%" />
								</colgroup>
								<tbody id="businessInfo" class="listingsContent">
									<core:set value="${checked }" var="cheked" scope="page"></core:set>
									<core:forEach items="${allBusinessInfo}" var="bean"
										varStatus="i">
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

											<td class="td_03"><div>
													<core:out value="${bean.store}"></core:out>
												</div></td>
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
				<!-- // box -->
				<div id="business_listings_id" align="center">
					<core:if test="${numOfPages gt 1}">
						<span id="page_first" class="link"><a href="#"
							onclick="listingsPagination(1);"
							style="font-size: 14px; font-weight: 600">First</a> </span>&nbsp;
							<span id="page_prev" class="link"><a href="#"
							onclick="paginationPre();"
							style="font-size: 14px; font-weight: 600">Prev</a> </span>&nbsp;							
							<span id="pageNums" class="link"
							style="font-size: 14px; font-weight: 600"> </span>
						<span id="page_next" class="link"><a href="#"
							onclick="paginationNext(${numOfPages});"
							style="font-size: 14px; font-weight: 600">Next</a> </span>&nbsp;
							<span id="page_last" class="link"><a href="#"
							onclick="listingsPagination(${numOfPages});"
							style="font-size: 14px; font-weight: 600">Last</a> </span>&nbsp;
					</core:if>
				</div>
				<div class="buttons-center">
					<a href="#" onclick="sendEditResult()" class="btn_dark_blue_2">Edit</a>
					
					<a href="#" onclick="showExportPopup();" class="btn_dark_blue_2">Export</a>
					<!-- <button  onclick="downloadInfo();" class="btn_dark_blue_2" >Download</button> -->

					<%
						Boolean isViewOnly = (Boolean) session
									.getAttribute("isViewOnly");
							if (!isViewOnly) {
					%>
					<a href="#" onclick="showDeletePopup();" class="btn_dark_blue_2">Delete</a>
					<%
										
							if(Role==LBLConstants.CONVERGENT_MOBILE_ADMIN){						
						%>
					<!-- <a href="#" onclick="showUplaodPopup();" class="btn_dark_blue_2">Upload</a> -->
					<%
						}
					%>
					<a href="#" class="btn_dark_blue_2" onclick="addLocation();">Add
						Location</a>

					<%
						}
					%>
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
