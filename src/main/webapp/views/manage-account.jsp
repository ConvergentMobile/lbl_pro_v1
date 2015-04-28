<%@page import="com.business.common.util.LBLConstants"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
#passcss {
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
	$(document)
			.ready(
					function() {

						$('#selecctall').click(function(event) { //on click 
							if (this.checked) { // check select status
								$('.checkbox1').each(function() { //loop through each checkbox
									this.checked = true; //select all checkboxes with class "checkbox1"               
								});
							} else {
								$('.checkbox1').each(function() { //loop through each checkbox
									this.checked = false; //deselect all checkboxes with class "checkbox1"                       
								});
							}
						});

						var message = $("#bandNameSearch").val();
						if (message != null) {
							$('#brandSearchDetails').bPopup();
							$("ul li").removeClass("selected");
						}

						$("#createNewBrand").click(function() {
							$('#channel-name').val('');
							$('#brand').val('');
							$('#clientId').val('');
							$('#invoiced').val('');
							$('label>#partners').removeAttr('checked');

						});

						$("select#myDropdown").change(function() {
							$("td#channelNameDiv").show();
							var role = $("select#myDropdown").val();
							if (role === "3") {
								$("td#channelNameDiv").hide();
							}
							;
						});
						$("select#myDropdown").change(function() {
							$("td#channelNameDiv").show();
							var role = $("select#myDropdown").val();
							if (role === "3") {
								$("#brandnames").show();
								$("#brandvalues").hide();
								$("td#channelNameDiv").hide();
							}
							;
						});
						
					    $( "select#channelId" ).change(function () {
					    		alert("changed channel");
					    	  $.ajax({
								  type: "get",
								  url: "getChannelBrands.htm",
								  cache: false,    
								  data:'channelName=' +  $(this).val(),
								  success: function(response){
									  var brands = $.parseJSON(response);
							        	var html=' <div class="search">'+'<input type="text" value="" name="search_users" id="search_users" >'+
						         		 '<a href="#" class="btn_search">'+'<span>Search</span>'+'</a>'+'</div>'+'<ul class="uad-list">'+'<li class="all">'+
						         		 '<input type="checkbox" id="selecctall"> Select/unselect All</label>'+'</li>';
									  $(brands).each(function(i, client) {

											  html +=
									         		 '<li>'+'<label>'+
									         		'<input type="checkbox" class="checkbox1" name="brandID"  id="brandId" value="'+client.brandID+'">'+client.brandName+
								         			 '</label>'+'</li>'; 

						               });
									  html+='</ul>';
							        	console.log("data::"+html);
							         	
							         	$("#brandvalues").show();
							         	
							         	$("#brandnames").hide();
							         	$("#brandvalues").html(html);
									 

						    
						     },
									  error: function(response){  
										   alert('Error while fetching Brands for Channel..');
										  }
						    });
				 
						});
					
						
	
						$('#saveConfirmationId')
								.click(
										function(event) {
											data = $('.password').val();
											var len = data.length;
											mydropdown = $('#myDropdown');
											var count = 0;

											$('input[type=checkbox].checkbox1')
													.each(
															function() {
																if ($(this)
																		.is(
																				":checked")) {
																	++count;

																}

															});

											if (len < 1) {

												$("#PasswordPopUp").bPopup();
												$("ul li").removeClass(
														"selected");
												// alert("Password cannot be blank");
												// Prevent form submission
												event.preventDefault();
											} else if ($('.password').val() != $(
													'.confpass').val()) {
												$("#ConfirmPasswordPopUp")
														.bPopup();
												$("ul li").removeClass(
														"selected");
												//alert("Enter Confirm Password Same as Password");
												// Prevent form submission
												event.preventDefault();
											} else if (mydropdown.length == 0
													|| $(mydropdown).val() == "") {
												$("#rolePopUp").bPopup();
												$("ul li").removeClass(
														"selected");
												// alert("Please select  role");
												event.preventDefault();
											} else if ($(mydropdown).val() == "2"
													&& $("#channelName").val() == "") {
												$("#ChannelPopUp").bPopup();
												$("ul li").removeClass(
														"selected");
												// alert("Please select  Channel");
												event.preventDefault();
											} else if(count == 0){
									                  
									              $("#brandPopUp").bPopup();
									              $("ul li").removeClass("selected");
									              //alert("please select any one brand only");  
									              event.preventDefault();
									             } else{
									                    $('#displaySaveConfirmationpopup').bPopup();
									                    $("ul li").removeClass("selected");
									                    event.preventDefault();
									                  }
										});

						$("#savepopup").click(
								function(event) {
									var count = 0;
									$('input[type=checkbox]#checkbox1').each(
											function() {
												if ($(this).is(":checked")) {
													++count;
												}
											});
									if (count != 1 && count <= 1) {

										$("#submissionsPopUp").bPopup();
										$("ul li").removeClass("selected");
										//alert("please select at least one submissions");
										event.preventDefault();
									}

								});

						var isNewUser = $.session.get("isNewUser");
						//alert("isNewUser :: "+isNewUser);
						if (isNewUser === "true") {
							//alert("disableReadOnly");
							// enableReadOnly();

						} else {
							//alert("en");		 
							//disableReadOnly();   
						}

						$("a#edit").click(function() {
							$.session.set("isNewUser", false);
							//disableReadOnly();     	

						});

						$("a#create_user").click(function() {
							$.session.set("isNewUser", false);

						});

						$("a#manage-account").click(function() {
							$.session.set("isNewUser", true);
						});

						$("#editpopup").hide();
						$("#cancel").hide();
						$("#saveConfirmationId").hide();
						$("#savepopup").hide();
						$("#cancelpopup").hide();
						$("#createNewBrand").click(function() {
							$("#cancelpopup").show();
							$("#savepopup").show();
							$("#createNewBrand").hide();
							$("#editpopup").hide();
							disableBrandReadOnly();
						});
						$("#editpopup").click(function() {

							$("#cancelpopup").show();
							$("#savepopup").show();
							$("#createNewBrand").hide();
							$("#editpopup").hide();
							disableBrandReadOnly();
						});
						$("#cancelpopup").click(function() {

							$("#cancelChanges").bPopup();
							$("ul li").removeClass("selected");
						});

						if ($.session.get("isNewUser") === "true") {
							$("#cancel").hide();
							$("#edit").hide();
							$("#saveConfirmationId").hide();
							$("td#rolediv").hide();
							$("td#channelNameDiv").hide();
						} else {
							$("#cancel").show();
							$("#edit").hide();
							$("#saveConfirmationId").show();
							$("td#rolediv").show();
							$("td#channelNameDiv").show();
						}

						$("#edit")
								.click(
										function() {
											$('input[type=radio]#checkBoxValue')
													.each(
															function() {
																if ($(this)
																		.is(
																				":checked")) {
																	$("#cancel")
																			.show();
																	$("#edit")
																			.hide();
																	$(
																			"#saveConfirmationId")
																			.show();
																	$(
																			"#rolediv")
																			.Show();
																	$(
																			"#channelNameDiv")
																			.show();
																} else {

																	$(
																			"#userEditPopUp")
																			.bPopup();
																	$("ul li")
																			.removeClass(
																					"selected");
																	//alert("Please Select a User for Editing");
																	return false;
																}
															});

										});

						$("#cancel").click(function() {
							$.session.set("isNewUser", true);
							$("#cancelChanges").bPopup();
							$("ul li").removeClass("selected");
						});

						$("#cancelpopup").hide();
						$("#savepopup").hide();
						$("#createNewBrand").click(function() {
							$("#cancelpopup").show();
							$("#savepopup").show();
							$("#createNewBrand").hide();
							$("#editpopup").hide();
							disableBrandReadOnly();
						});
						$("#editpopup").click(function() {

							$("#cancelpopup").show();
							$("#savepopup").show();
							$("#createNewBrand").hide();
							$("#editpopup").hide();
							disableBrandReadOnly();
						});
						$("#cancelpopup").click(function() {

							$("#cancelChanges").bPopup();
							$("ul li").removeClass("selected");
						});
					});

	/* $(document).ready(function() {
	 if (window.history && window.history.pushState) {
	 window.history.pushState('forward', null, './#forward');
	 $(window).on('popstate', function() {
	 // alert('Back button was pressed.');
	 $("#browserBackButton").bPopup();
	 });
	 }  
	
	 });  */

	function enableReadOnly() {
		$("table > tbody > tr > td > div.user > input").attr("readonly", false);
	}

	function disableReadOnly() {
		$("table > tbody > tr > td > div.user > input").attr("readonly", false);
	}

	function disableBrandReadOnly() {
		$("table > tbody > tr > td > div.brands > input").attr("readonly",
				false);
	}
	function showbrandDetailsInfo() {

		$('#brandDetails').bPopup();

		$("ul li").removeClass("selected");
	}

	function searchBrandName() {
		$("#searchBrandNameFilterForm").attr('action', 'brandsSearch.htm');
	}

	function sendSubmitResult() {

		var form = document.getElementById('loginForm');
		form.action = "saveUser.htm";
		form.submit();
		$.session.set("isNewUser", true);
	}

	$(function() {
		$("#datepicker").datepicker();
	});

	function search() {
		$(".searchAndFilterForm").attr('action', 'usersSearch.htm');

	}

	function myfunction() {
		$.session.set("isNewUser", false);

		var form = "";
		var count = 0;
		$('input[type=radio]#checkBoxValue').each(function() {
			if ($(this).is(":checked")) {
				++count;
				form = $(this).val();
			}
		});

		if (form != "" && count == 1) {
			$(".searchAndFilterForm").attr('action', 'userShowInfo.htm');
			return true;
		} else {
			$('#showDetails').bPopup();
			$("ul li").removeClass("selected");
			//alert("please select the user");		
			return false;
		}
	}
</script>


</head>

<div class="popup" id="PasswordPopUp" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Password cannot be blank.</div>
	</div>
</div>

<div class="popup" id="ConfirmPasswordPopUp" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Enter Confirm Password Same as Password.</div>
	</div>
</div>

<div class="popup" id="rolePopUp" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select role.</div>
	</div>
</div>

<div class="popup" id="ChannelPopUp" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select channel.</div>
	</div>
</div>

<div class="popup" id="brandPopUp" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select brands.</div>
	</div>
</div>


<div class="popup" id="showDetails" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select the user.</div>
	</div>
</div>

<div class="popup" id="browserBackButton" style="display: none">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Cancel
			Changes</span>
	</div>
	<div class="pp-subheader">
		You have selected to leave this page.<br>If you do so without
		saving, your changes will not be saved.
	</div>
	<div class="pp-body">
		<div class="buttons">
			<button onclick="return sendSubmitResult();" class="btn_dark_blue_2">Save
				& Exit</button>
			<a href="dash-board.htm" class="btn_dark_blue_2">Exit Page</a> <a
				href="#" class="btn_dark_blue_2 b-close">Continue Edit</a>
		</div>
	</div>
</div>
<div class="popup" id="submissionsPopUp" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select at least one submissions.</div>
	</div>
</div>
<div class="popup" id="userEditPopUp" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please Select The User for Editing.</div>
	</div>
</div>
<div class="popup" id="cancelChanges" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Cancel
			Changes</span>
	</div>
	<div class="pp-subheader">
		You have selected to Cancel the changes you have entered.<br>If
		you click OK, any changes made will not be saved.
	</div>
	<div class="pp-body">
		<div class="buttons">
			<a href="manage-account.htm" class="btn_dark_blue_2">Ok</a> <a
				href="#" class="btn_dark_blue_2 b-close">Continue Edit</a>
		</div>
	</div>
</div>
<div class="popup" id="brandDetails" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Brand
			Details</span>
	</div>
	<div class="pp-subheader">
		<spring:form method="post" commandName="brand"
			id="searchBrandNameFilterForm">
			<label class="pr20">Brand Name</label>
			<!-- <input type="text" value="" name="search_brand" id="search_brand"> -->
			<spring:input path="brandName" id="search_brand" />
			<!-- <a href="#" class="btn_search"><span>Search</span></a> -->
			<button class="btn_search" onclick="searchBrandName()">
				<span>search</span>
			</button>
		</spring:form>
	</div>
	<div class="pp-body">
		<spring:form action="brandsSubmit.htm" commandName="brand"
			method="post">
			<table width="100%" class="grid grid_11 grid_11_1"
				style="width: 360px; margin: 10px auto;">
				<colgroup>
					<col width="55%" />
					<col width="45%" />
				</colgroup>
				<tbody>
					<tr>
						<td class="td_01"><div>
								<label for="channel-name">Channel Name</label>
							</div></td>
						<td class="td_02"><div class="brands">
								<!-- <input type="text" value="Convergent Mobil" id="channel-name" name="channel-name"> -->
								<spring:input path="channelName" id="channel-name" />
							</div></td>
					</tr>
					<tr>
						<td class="td_01"><div>
								<label for="brand-name">Brand Name</label>
							</div></td>
						<td class="td_02"><div class="brands">
								<!-- <input type="text" value="Liberty Tax Services" id="brand" name="brand"> -->
								<spring:input path="brandName" id="brand" />
							</div></td>
					</tr>
					<tr>
						<td class="td_01"><div>
								<label for="date_start">Client Id</label>
							</div></td>
						<td class="td_02"><div class="brands">
								<!-- <input type="text" name="date_start" id="datepicker" class="input_date_01" value="01/01/2014"> -->
								<spring:input path="clientId" id="clientId" />
							</div></td>
					</tr>
					<tr>
						<td class="td_01"><div>
								<label for="invoiced"># of Locations - Invoiced</label>
							</div></td>
						<td class="td_02"><div class="brands">
								<!-- <input type="text" value="4,500" id="invoiced" name="invoiced" style="width:75px"> -->
								<spring:input path="locationsInvoiced" id="invoiced"
									style="width:75px" />
							</div></td>
					</tr>
					<tr>
						<td class="td_01"><div>
								<label for="email"># of Locations - Uploaded</label>
							</div></td>
						<td class="td_02"><div>
								<core:if test=""></core:if>
							</div></td>
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
		<a href="#" id="createNewBrand" class="btn_dark_blue_2">Create New</a>
		<a href="#" id="editpopup" class="btn_dark_blue_2">edit</a> <input
			type="submit" id="savepopup" class="btn_dark_blue_2" value="save">
		<input type="reset" id="cancelpopup" class="btn_dark_blue_2"
			value="cancel">

	</div>
	</spring:form>
</div>

<core:if test="${bandNameSearch eq 'yes'}">
	<input type="hidden" value="${bandNameSearch}" id="bandNameSearch">
	<div class="popup" id="brandSearchDetails" style="display: none;">
		<div class="pp-header">
			<!-- <div class="close"></div> -->
			<span class="buttonIndicator b-close"><span>X</span></span> <span>Brand
				Details</span>
		</div>
		<div class="pp-subheader">
			<spring:form method="post" commandName="brand"
				id="searchBrandNameFilterForm">
				<label class="pr20">Brand Name</label>
				<!-- <input type="text" value="" name="search_brand" id="search_brand"> -->
				<spring:input path="brandName" id="search_brand" />
				<!-- <a href="#" class="btn_search"><span>Search</span></a> -->
				<button class="btn_search" onclick="searchBrandName()">
					<span>search</span>
				</button>
			</spring:form>
		</div>
		<div class="pp-body">
			<spring:form action="brandsSubmit.htm" commandName="brand"
				method="post">

				<span> ${ message } </span>

				<spring:hidden path="saveType" value="update" />
				<table width="100%" class="grid grid_11 grid_11_1"
					style="width: 360px; margin: 10px auto;">
					<colgroup>
						<col width="55%" />
						<col width="45%" />
					</colgroup>
					<tbody>
						<tr>
							<td class="td_01"><div>
									<label for="channel-name">Channel Name</label>
								</div></td>
							<td class="td_02"><div>
									<!-- <input type="text" value="Convergent Mobil" id="channel-name" name="channel-name"> -->
									<spring:input path="channelName" id="channel-name"
										readonly="true" />
								</div></td>
						</tr>
						<tr>
							<td class="td_01"><div>
									<label for="brand-name">Brand Name</label>
								</div></td>
							<td class="td_02"><div>
									<!-- <input type="text" value="Liberty Tax Services" id="brand" name="brand"> -->
									<spring:input path="brandName" id="brand" />
								</div></td>
						</tr>
						<tr>
							<td class="td_01"><div>
									<label for="date_start">Client Id</label>
								</div></td>
							<td class="td_02"><div class="brands">
									<!-- <input type="text" name="date_start" id="datepicker" class="input_date_01" value="01/01/2014"> -->
									<spring:input path="clientId" id="clientId" />
								</div></td>
						</tr>
						<tr>
							<td class="td_01"><div>
									<label for="invoiced"># of Locations - Invoiced</label>
								</div></td>
							<td class="td_02"><div>
									<!-- <input type="text" value="4,500" id="invoiced" name="invoiced" style="width:75px"> -->
									<spring:input path="locationsInvoiced" id="invoiced"
										style="width:75px" />
								</div></td>
						</tr>
						<tr>
							<td class="td_01"><div>
									<label for="email"># of Locations - Uploaded</label>
								</div></td>
							<td class="td_02"><div>
									<core:out value="${locationsUploaded}"></core:out>
								</div></td>
						</tr>
					</tbody>
				</table>
		</div>
		<div class="pp-subheader">Submissions</div>

		<div class="image-checkboxes">

			<core:set var="subStr"
				value="Acxiom,InfoGroup,Google,Factual,Neustar,Bing" />
			<core:set var="subBrandList"
				value="${fn:split(brand.submisions,',') }" />
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
							<img src="images/logo1_${subValue}.png" alt="">
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
			<input type="submit" class="btn_dark_blue_2" value="save">
		</div>
		</spring:form>
	</div>
</core:if>

<div class="popup" id="displaySaveConfirmationpopup"
	style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Save
			Changes</span>
	</div>
	<div class="pp-subheader">
		Do you want to save the changes you have<br>entered for this
		user?
	</div>
	<div class="pp-body">
		<div class="buttons">
			<button onclick="sendSubmitResult();" class="btn_dark_blue_2">Ok</button>
			<span class="btn_dark_blue_2 b-close">Continue Edit</span>
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
		<spring:form action="adminUser.htm" commandName="adminUser"
			id="loginForm" class="searchAndFilterForm">
			<div class="content_wrapper">
				<!-- left side navigation -->
				<ul class="ul_left_nav">
					<li class="si_dashboard"><a href="dash-board.htm">Dashboard</a></li>
					<li class="si_business_listings"><a
						href="business-listings.htm">Business Listings</a></li>
					<li class="si_error_listings"><a href="listing-error.htm">Listing
							Errors</a></li>
					<li class="si_upload_export"><a href="upload-export.htm">Upload/Export</a></li>
					<li class="si_reports"><a href="reports.htm">Reports</a></li>
					<li class="si_mobile_profile selected"><a
						href="manage-account.htm" id="manage-account">Manage Account</a></li>
					<li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent
							Toolbox</a></li>
				</ul>
				<!-- // left side navigation -->
				<!-- content area -->
				<div class="content" id="id_content">
					<div class="nav_pointer pos_01"></div>
					<!-- subheader -->
					<div class="subheader clearfix">
						<h1>Manage Account</h1>

						<%-- <core:out value="${adminUser.brandID}"></core:out> --%>

						<p>${channelName}</p>
					</div>
					<!-- // subheader -->

					<div class="inner_box">
						<!-- box -->
						<div class="box box_red_title manage_account pb10">
							<!-- title -->
							<div class="box_title">
								<h2>User Account Details</h2>
								<div class="box_title_right btr_2">
									<%
										Integer Role = (Integer) session.getAttribute("roleId");
											if (Role == LBLConstants.CONVERGENT_MOBILE_ADMIN) {
									%>

									<a href="#" onclick="showbrandDetailsInfo();"
										class="btr_button">Brand Admin</a> <a
										href="manage-account.htm" class="btr_button" id="create_user">Create
										User</a>
									<%
										} else {
									%>

									<%	Boolean isViewOnly = (Boolean) session.getAttribute("isViewOnly");
								if(!isViewOnly) { %>
									<a href="manage-account.htm" class="btr_button"
										style="margin-top: 12px;" id="create_user">Create User</a>
									<% }%>

									<% } %>
								</div>
							</div>
							<!-- // title -->
							<table id="user" width="100%" class="grid grid_11 mt15">
								<colgroup>
									<col width="17%" />
									<col width="32%" />
									<col width="22%" />
									<col width="32%" />
								</colgroup>
								<tbody>
									<tr>
										<td class="td_01"><div>
												<label for="name">Username</label>
											</div></td>
										<td class="td_02"><div class="user">
												<spring:hidden path="userID" />
												<spring:input path="userName" />
											</div></td>
										<td class="td_03">
											<div class="user">
												<label for="last-name">View Only</label>
											</div>
										<td class="td_04">
											<div>
												<core:choose>												
													<core:when test="${adminUser.viewOnly == 'Y'}">
														<span style="float: left"> 
														<input type="checkbox" name="viewonly" checked="checked">
														</span>
													</core:when>
													<core:otherwise>
														<span style="float: left"> 
														<input type="checkbox" name="viewonly">
														</span>
													</core:otherwise>
												</core:choose>
											</div>
										</td>

									</tr>
									<tr>
										<td class="td_01"><div>
												<label for="name">Name</label>
											</div></td>
										<td class="td_02"><div class="user">
												<spring:input path="name" id="searchField" />
											</div></td>
										<td class="td_03"><div>
												<label for="last-name">Last Name</label>
											</div></td>
										<td class="td_04"><div class="user">
												<spring:input id="lastName" path="lastName" />
											</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>
												<label for="password">Password</label>
											</div></td>
										<td class="td_02"><div class="user">
												<spring:password path="passWord" id="passcss"
													class="password" showPassword="true" />
											</div></td>
										<td class="td_03"><div>
												<label for="confirm-password">Confirm Password</label>
											</div></td>
										<td class="td_04"><div class="user">
												<spring:password path="confirmPassWord" id="passcss"
													class="confpass" showPassword="true" />
											</div></td>
									</tr>
									<tr>
										<td class="td_01"><div>
												<label for="email">Email</label>
											</div></td>
										<td class="td_02"><div class="user">
												<spring:input path="email" />
											</div></td>
										<td class="td_03"><div>
												<label for="phone">Phone</label>
											</div></td>
										<td class="td_04"><div class="user">
												<spring:input path="phone" />
											</div></td>
									</tr>
								</tbody>
							</table>
							<div class="h-separator"></div>
							<table width="100%" class="grid grid_11 mb10">
								<colgroup>
									<col width="33%" />
									<col width="33%" />
									<col width="33%" />
								</colgroup>
								<tbody>
									<tr>

										<td class="td_01" id="rolediv"><div>
												<label for="current-access-levels">Roles</label>
											</div></td>
										<td class="td_02" colspan="2" id="rolediv"><div>
												<spring:select path="roleId" id="myDropdown">

													<option value="">Select Role</option>
													<core:forEach items="${roles}" var="roleValue">

														<core:choose>
															<core:when test="${roleValue.roleId == adminUser.roleId}">
																<option value="${roleValue.roleId}" selected="selected">${roleValue.roleName}</option>
															</core:when>
															<core:otherwise>
																<option value="${roleValue.roleId}">${roleValue.roleName}</option>
															</core:otherwise>
														</core:choose>


													</core:forEach>
												</spring:select>
											</div></td>

									</tr>
									<core:if test="${not empty channels}">
										<tr>
											<td class="td_01" id="channelNameDiv"><div>
													<label for="channel-name">Channel Name</label>
												</div></td>
											<td class="td_02" id="channelNameDiv"><div>
													<spring:select path="channelName" id="channelId">
														<option value="">Select Channel</option>ue
													<core:forEach var="channellist" items="${channels}">
															<core:choose>
																<core:when
																	test="${channellist eq adminUser.channelName}">
																	<option value="${channellist}" selected="selected">${channellist}</option>
																</core:when>
																<core:otherwise>
																	<option value="${channellist}">${channellist}</option>
																</core:otherwise>
															</core:choose>

														</core:forEach>

													</spring:select>
												</div></td>
											<td class="td_03">
												<!-- <div><input type="checkbox"> View Access Only</div> -->
											</td>
										</tr>
									</core:if>
								</tbody>
							</table>

							<div class="box-subtitle-2">User Permissions</div>

							<table width="100%" class="grid grid_12">
								<colgroup>
									<col width="33%" />
									<col width="33%" />
									<col width="33%" />
								</colgroup>
								<thead>
									<tr>
										<th class="th_01"><div>Client Name</div></th>
										<th class="th_02"><div>States</div></th>
										<th class="th_03"><div>Store # or Address</div></th>
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
										<td class="td_01"><div id="brandnames" >
												<div class="search">
													<input type="text" value="" name="search_users"
														id="search_users"> <a href="#" class="btn_search"><span>Search</span></a>
												</div>
												<ul class="uad-list">

													<li class="all"><label><input type="checkbox"
															id="selecctall"> Select/unselect All</label></li>
													<core:forEach items="${clientNameInfo}" var="client">
														<li><label> <core:set var="pageCount"
																	value="0" scope="page"></core:set> <core:set
																	var="count" value="0" scope="page" /> 
																	<core:if test="${adminUser.brandID == null}">
																	<input type="checkbox" class="checkbox1"
																		value="${client.brandID}" name="brandID" id="brandId">${client.brandName} 
                       												</core:if> 
                       											<core:forEach items="${adminUser.brandID}"
																	var="brands">

																	<core:if test="${client.brandID eq brands}">
																		<core:set var="count" value="${count + 1}"
																			scope="page" />
																		<input type="checkbox" class="checkbox1"
																			value="${client.brandID}" name="brandID" id="brandId"
																			checked="checked">${client.brandName}
                          										 </core:if>

																</core:forEach> <core:if
																	test="${adminUser.brandID != null && pageCount == count}">
																	<input type="checkbox" class="checkbox1"
																		value="${client.brandID}" name="brandID" id="brandId">${client.brandName}
                    									 </core:if>
														</label></li>
													</core:forEach>




												</ul>
											</div>
											
											<div id="brandvalues" style="display: none">
								
									
									</div>
											
											</td>
										<td class="td_02"><div>
												<div class="search">
													<input type="text" value="" name="search_users"
														id="search_users"> <a href="#" class="btn_search"><span>Search</span></a>
												</div>
												<ul class="uad-list">
													<li class="all">
														<!-- <label><input type="checkbox" id="selecctall"> Select/unselect All</label> -->
													</li>
													<core:forEach items="${stateNamesInfo}" var="state">
														<li><label> <%-- <input type="checkbox" class="checkbox1" value="${state.id}" name="id"> --%>
																${state.locationState}
														</label></li>
													</core:forEach>
												</ul>
											</div></td>
										<td class="td_03"><div>
												<div class="search">
													<input type="text" value="" name="search_users"
														id="search_users"> <a href="#" class="btn_search"><span>Search</span></a>
												</div>
												<ul class="uad-list">
													<li class="all">
														<!-- <label><input type="checkbox" id="selecctall"> Select/unselect All</label> -->
													</li>
													<core:forEach items="${storeNamesInfo}" var="store">
														<li><label> <%-- <input type="checkbox" class="checkbox1" value="${store.id}" name="id"> --%>
																${store.store}
														</label></li>
													</core:forEach>
												</ul>
											</div></td>
									</tr>
								</tbody>
							</table>

							<div class="box-buttons-2">
								<a href="#" class="btn_dark_blue_2" id="edit">Edit</a> <a
									id="cancel" href="#" class="btn_dark_blue_2">Cancel</a>

								<%	Boolean isViewOnly = (Boolean) session.getAttribute("isViewOnly");
							if(!isViewOnly) { %>
								<a href="#" onclick="saveConfirmation();"
									id="saveConfirmationId" class="btn_dark_blue_2">Save</a>
								<% }%>


							</div>

						</div>
						<!-- // box -->

					</div>
				</div>
				<!-- // content area -->
				<!-- sidebar -->
				<div class="sidebar" id="id_sidebar" style="min-height: 781px;">
					<div class="inner">

						<div class="sb_box_wrapper">
							<!-- title -->
							<div class="sb_title sb_title_ico ico_sb_user">
								<h2 class="">User Accounts</h2>
							</div>
							<!-- // title -->
							<!-- sidebar box -->
							<div class="sb_box">
								<div class="sb_box" style="padding: 5px 0 0;">
									<table width="100%" class="grid grid_08"
										style="margin: 5px 19px;">
										<colgroup>
											<col width="40%" />
											<col width="60%" />
										</colgroup>
										<tbody>
											<tr>
												<td class="td_01"><div>
														<label for="search_users">Search Users</label>
													</div></td>
												<td class="td_02"><div>

														<spring:input path="searchName"
															style="width:65%; margin-right:5px" />
														<!-- <div  onclick="search()"><span 	class="btn_search">Search</span></div> -->
														<button class="btn_search" onclick="search()">
															<span>search</span>
														</button>
													</div></td>
											</tr>
										</tbody>
									</table>
									<table width="100%" class="grid grid_09">
										<colgroup>
											<col width="12%" />
											<col width="30%" />
											<col width="30%" />
											<col width="28%" />
										</colgroup>
										<thead>
											<tr>
												<th class="th_01"><div></div></th>
												<th class="th_02"><div>Name</div></th>
												<th class="th_04"><div>User Name</div></th>
												<th class="th_04"><div>
														<a href="manage-account.htm" class="lnk_advs">Clear
															Search</a>
													</div></th>
											</tr>
										</thead>
									</table>
									<div class="sb_box_shadow">
										<div id="id_user_accounts-2">

											<core:forEach items="${usersListInfo}" var="users">
												<table width="100%" class="grid grid_09">
													<colgroup>
														<col width="12%" />
														<col width="38%" />
														<col width="50%" />
													</colgroup>
													<tbody>
														<tr>
															<td class="td_01"><div>
																	<input type="radio" id="checkBoxValue"
																		value="${users.userID}" name="checkbox">
																</div></td>
															<td class="td_02"><div>${users.name}&nbsp;
																	${users.lastName}</div></td>
															<td class="td_02"><div>${users.userName}</div></td>
														</tr>

													</tbody>
												</table>
											</core:forEach>
										</div>
									</div>
									<div class="sb_buttons_center">
										<!-- <a href="#" class="btn_dark_blue_2">Show Details</a> -->
										<input type="submit" onclick="return myfunction();"
											value="Show Details" class="btn_dark_blue_2">
									</div>
								</div>
								<!-- // sidebar box -->
							</div>
						</div>
					</div>
					<!-- // sidebar -->
					<div class="floatfix"></div>
				</div>
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