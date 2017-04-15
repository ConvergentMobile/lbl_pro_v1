<%@page import="com.business.common.util.LBLConstants"%>
<%@ taglib uri="http://www.springframework.org/tags/form"	prefix="spring"%>
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
<link rel="stylesheet" href="css/jquery-ui.css">
<script src="js/jquery-1.9.1.min.js"></script>
<script src="js/jquery.idTabs.min.js"></script>
<script src="js/jquery.slimscroll.min.js"></script>
<script src="js/jquery.screwdefaultbuttonsV2.js"></script>
<script src="js/jquery.cycle.all.js"></script>
<script src="js/jquery.bpopup.min.js"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/functions.js"></script>
<script src="js/jquery.session.js"></script>


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
	width: 100%;
}
</style>

<script type="text/javascript">

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
	 
	  var form = document.getElementById('brandListform');
	     form.action = "branddelete";
	     form.submit();
	}
</script>


</head>


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


<div class="popup" id="displayDeleteConfirmationpopup"  style="display: none">
 <div class="pp-header">
  <!-- <div class="close"></div> -->
  <span class="buttonIndicator b-close"><span>X</span></span>
  Delete Location
 </div>
 <div class="pp-subheader">You have selected to Delete the selected brand(s).<br><span class="red">This will permanently remove the brand(s) and cannot be undone.</span></div>
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
	<spring:form action="branddelete.htm" commandName="brandchannel" id="brandListform" method="post">
	<div class="content_wrapper"> 
		<!-- left side navigation -->
		<ul class="ul_left_nav">
			<li class="si_dashboard"><a href="dash-board.htm">Dashboard</a></li>
			<li class="si_business_listings"><a href="business-listings.htm">Business Listings</a></li>
			<li class="si_error_listings"><a href="listing-error.htm">Listing Errors</a></li>
			<li class="si_upload_export"><a href="upload-export.htm">Upload/Export</a></li>
			<li class="si_reports"><a href="reports.htm">Reports</a></li>
			<li class="si_brand selected"><a href="brandchannel.htm">Manage Brands</a></li>
			<li class="si_mobile_profile "><a href="manage-account.htm" id="manage-account">Manage Account</a></li>
			<!--<li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent Toolbox</a></li>-->
		</ul>
		<!-- // left side navigation --> 
		<!-- content area -->
		<div class="content" id="id_content">
			<div class="nav_pointer pos_01"></div>
			<!-- subheader -->
			<div class="subheader clearfix">
				<h1>Brand Management</h1>
				<%-- <p>${channelName}</p> --%>
			</div>
			<!-- // subheader -->
					
			<div class="inner_box"> 
				<!-- box -->
				<div class="box box_red_title manage_account pb10"> 
					<!-- title -->
					<div class="box_title">
						
						<div class="box_title_right btr_2"> 
						
						</div>
					</div>
					<!-- // title -->
				
					<div class="h-separator"></div>				
					
					<table width="100%" class="grid grid_12">
						<colgroup>
							<col width="50%" />
							<col width="50%" />
							
						</colgroup>
						<thead>
							<tr>
								<th class="th_01"><div>Brand</div></th>
								<th class="th_02"><div>Channel</div></th>
								
							</tr>
						</thead>
					</table>
					
					<table width="100%" class="grid_12">
						<colgroup>
							<col width="33%" />
							<col width="33%" />
							<col width="33%" />
						</colgroup>
						<tbody>
							<tr>
								<td class="td_01"><div>
								<!-- 	<div class="search">
										<input type="text" value="" name="search_users" id="search_users">
										<a href="brandchannel.htm" ><span>click Brands</span></a>
									</div> -->
									<ul class="uad-list">
									
										<li class="all"><label><input type="checkbox" id="selecctall"> Select/unselect All</label></li>
										<core:forEach items="${clientNameInfo}" var="client">
										<li><label><input type="checkbox" class="checkbox1" value="${client.brandID}"
											name="brandID">${client.brandName}</label></li>
										</core:forEach>
										
									</ul>
								</div></td>
								<td class="td_02"><div>
									<!-- <div class="search">
										<input type="text" value="" name="search_users" id="search_users">
										<a href="#" class="btn_search"><span>Search</span></a>
									</div> -->
									<ul class="uad-list">
										<li class="all"><!-- <label><input type="checkbox" id="selecctall"> Select/unselect All</label> --></li>
										<core:forEach items="${channels}" var="channels">
										<li><label> <input type="checkbox" class="checkbox1" value="${channels.channelID}" name="channelIDvalue"> ${channels.channelName}</label></li>
										</core:forEach>
									</ul>
								</div></td>
							
							</tr>
						</tbody>
					</table>
						
					<div class="box-buttons-2">
						<a  onclick="showDeletePopup();"  class="btn_dark_blue_2" id="edit" >Delete</a>						
						<!-- <button type="submit" name="brands" class="btn_dark_blue_2" id="brands" >ShowBrands</button> -->
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
</spring:form>
<!-- // page wrapper -->

</body>
</html>