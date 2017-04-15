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
<link rel="stylesheet" href="css/bpopUp.css">
<link rel="stylesheet" href="css/navigation.css">
<link rel="stylesheet" href="css/common.css">
<link rel="stylesheet" href="css/grids.css">
<link rel="stylesheet" href="css/sidebar.css">
<script src="js/jquery-1.9.1.min.js"></script>
<script src="js/jquery.idTabs.min.js"></script>
<script src="js/jquery.slimscroll.min.js"></script>
<script src="js/jquery.screwdefaultbuttonsV2.js"></script>
<script src="js/jquery.cycle.all.js"></script>
<script src="js/functions.js"></script>
<script src="js/jquery.bpopup.min.js"></script>
<link rel="stylesheet" href="js/jquery.mCustomScrollbar.css">
<script src="js/jquery.mCustomScrollbar.js"></script>

<link rel="stylesheet" href="js/select2.css">
<script src="js/select2.js"></script>
<script src="js/select2.min.js"></script>
<script>
jQuery(document).ready(function(){
	jQuery(".selectjs").select2();
 });
</script>
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
  margin: :20px 0px 0px;
}
.select2-container--default .select2-selection--single .select2-selection__arrow {
  height: 26px;
  position: absolute;
  top: 1px;
  right: 1px;
  width: 20px;
  margin: 0px 0px 0px;
 /*  margin: -21px 0px 0px;
  margin: -21px 0px 0px; */
}

</style>
<script type="text/javascript">

$(document).ready(function() {
	$("select#storevalue").change(function() {
		var brandname=$('#brandname').val();
		//alert(brandname);
	
		var store = $("#storevalue").val();
	 document.location.href = "clientcheckreportview.htm?store="+store+"&&brandname="+brandname;
	});
	
			
	
	
});

	 
	 function runaccuracyreport() {
			
			var brandname=$('#brandname').val();
			
				  
					  document.location.href = "clientreports-accuracy.htm?brand="+brandname;	
					
				 
				
		}
	function comparelisting() {
		//alert("coming");
		var count = '';
		$('input[type=checkbox].checkbox1').each(function() {
			if ($(this).is(":checked")) {
				++count;

			}

		});
		//alert("conut"+count);
		if (count == 0 || count > 1) {
			$("#ListingPopUp").bPopup();
			$("ul li").removeClass("selected");
			// alert("Password cannot be blank");
			// Prevent form submission
			event.preventDefault();
		}
		if (count == 1) {
			var form = document.getElementById('checkreportform');
			form.action = "clientcomparelistings.htm";
			form.submit();
		}

	}
</script>
<script>
	$(function() {

		var moveLeft = 0;
		var moveDown = 0;
		$('.popper').hover(function(e) {

			var target = '#' + ($(this).attr('data-popbox'));

			$(target).show();
			moveLeft = $(this).outerWidth()-600;
			moveDown = ($(target).outerHeight()-160 / 2);
		}, function() {
			var target = '#' + ($(this).attr('data-popbox'));
			$(target).hide();
		});

		$('.popper')
				.mousemove(
						function(e) {
							var target = '#' + ($(this).attr('data-popbox'));

							leftD =  e.pageX + parseInt(moveLeft);
							maxRight = leftD + $(target).outerWidth();
							windowLeft = $(window).width() - 40;
							windowRight = 0;
							maxLeft = e.pageX
									- (parseInt(moveLeft)
											+ $(target).outerWidth() + 20);

							if (maxRight > windowLeft && maxLeft > windowRight) {
								leftD = maxLeft;
							}

							topD = e.pageY - parseInt(moveDown);
							maxBottom = parseInt(e.pageY + parseInt(moveDown)
									+ 20);
							windowBottom = parseInt(parseInt($(document)
									.scrollTop())
									+ parseInt($(window).height()));
							maxTop = topD;
							windowTop = parseInt($(document).scrollTop());
							if (maxBottom > windowBottom) {
								
								topD = windowBottom - $(target).outerHeight()
										- 20;
							} else if (maxTop < windowTop) {
								topD = windowTop + 20;
							}

							$(target).css('top', topD).css('left', leftD);

						});

	});
</script>
</head>
<div id="pop1" class="popbox">

	<p>${webAddress}</p>
</div>
<div class="popup" id="ListingPopUp" style="display: none;">
	<div class="pp-header">
		<!-- <div class="close"></div> -->
		<span class="buttonIndicator b-close"><span>X</span></span> <span>Warning!!</span>
	</div>
	<div class="pp-subheader"></div>
	<div class="pp-body">
		<div class="buttons">Please select One Listing to Compare.</div>
	</div>
</div>


<body id="office">
<!-- page wrapper -->
<div class="wrapper"> 
	<!-- header -->
	<div class="header">
			<a href="dashboardClient.htm" class="logo_lbl">Local Business Listings</a>
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
			<li class="si_dashboard"><a href="dashboardClient.htm">Dashboard</a></li>
			<!-- <li class="si_business_listings"><a href="clientbusinesslisting.htm">Business Listings</a></li> -->
			<li class="si_error_listings"><a href="listingClient-error.htm">Listing Errors</a></li>
			<li class="si_reports selected"><a href="client-reports.htm">Reports</a></li>
			<!--  <li class="si_toolbox"><a href="clntToolbox.htm">Convergent Toolbox</a></li>-->
		</ul>
		<!-- // left side navigation --> 
		<!-- content area -->
		<div class="content" id="id_content">
			<div class="nav_pointer pos_01"></div>
			<!-- subheader -->
			<div class="subheader clearfix">
				<h1>REPORTS</h1>
				<a href="client-reports.htm" class="back">< Back to Reporting Criteria</a>
				<a href="#" class="back" onclick="runaccuracyreport()">< Back to Accuracy Reporting</a>
				<span>Export Report</span>
				<a href="clreportxls.htm" class="ico_xls">XLS</a>
				<p>Convergent Mobile</p>
			</div>
			<!-- // subheader -->
			<div class="inner_box"> 
				<!-- box -->
				<div class="box box_red_title report_listings"> 
					<!-- title -->
					<div class="box_title">
					<core:forEach items="${lblportlisting}" var="lbllvaristing">
						<h2>Check Your Listings</h2>
						<p>${lbllvaristing.companyName }, #${lbllvaristing.store }</p>
						<div class="box_title_right">
							<div class="box_errors">
								<p>Directories<br>showing errors</p>
								<span>${directoryShowingErrors}</span>
							</div>
							<form class="box_title_form">
								 <select name="store"  id="storevalue"  class="selectjs" style="width:140px;">
											

												 <option value="">Select Store</option>
										
											<core:forEach items="${liststoreNames}" var="stores">
													<option value="${stores}" >${stores}</option>
												</core:forEach> 
										</select>
							</form>
						</div>
						</core:forEach>
					</div>
					<!-- // title -->
					<div class="box_map">
							<core:forEach items="${lblportlisting}" var="lbllisting">

								<iframe
									src="https://www.google.com/maps/embed/v1/place?key=${googleKey}
          							&q=${lbllisting.companyName },${lbllisting.locationAddress },${lbllisting.locationCity },
       							   ${lbllisting.locationState }"
									width="100%" height="230" frameborder="0" style="border: 0">
								</iframe>


								<div class="box_map_legend">

									<p>
										<span>${lbllisting.companyName }</span>
									<div class="box_map_legend_accuracy">
										<div class="accuracy-left">
											<p>
												Location<br>Accuracy
											</p>
											<fmt:formatNumber value="${locationAccuracy}" var="locAccuracy" maxFractionDigits="0"></fmt:formatNumber>
											<span >${locAccuracy}%</span>
										</div>
										<div class="accuracy-right">
											<p>
												Average<br>Accuracy
											</p>
												<fmt:formatNumber value="${totalPercentage}" var="avgAccuracy" maxFractionDigits="0"></fmt:formatNumber>
											<span >${avgAccuracy}%</span>
										</div>
									</div>
									<p class="details">${lbllisting.locationAddress }<br />
										${lbllisting.locationCity },${lbllisting.locationState }
										${lbllisting.locationZipCode }<br /> ${lbllisting.locationPhone }<br />
										
									</p>
									<core:set var="webAddress" value="${lbllisting.webAddress }"> </core:set>
									<core:set var="webAddress2" value="${fn:substring(webAddress, 0, 42)}" ></core:set>
									
									
									
									<p class="popper" data-popbox="pop1" >${webAddress2}</p>

								</div>
							</core:forEach>
						</div>
					<spring:form method="post" action="#" commandName="checkreport"
							id="checkreportform">
							<div class="" id="id_change_tacking" style="height:350px;">
					<table width="1500px" class="grid grid_15">
						<colgroup>
							<col width="4%"  />
							<col width="4%" />
							<col width="13%" />
							<col width="17%" />
							<col width="17%" />
							<col width="11%" />
							<col width="7%" />
							<col width="7%" />
							<col width="15%" />
							<col width="30%" />
							<col width="10%" />
						</colgroup>
						<thead>
							<tr>
								<th class="th_01"><div><!-- <input type="checkbox"> --></div></th>
								<th class="th_02"><div><img src="images/icon-!.png" align="!"></div></th>
								<th class="th_03"><div>Directory</div></th>
								<th class="th_04"><div>Business Name</div></th>
								<th class="th_05"><div>Address</div></th>
								<th class="th_06"><div>City</div></th>
								<th class="th_07"><div>State</div></th>
								<th class="th_08"><div>Zip</div></th>
								<th class="th_09"><div>Phone</div></th>
								<th class="th_09"><div>Webiste</div></th>
								<th class="th_01"><div>View Online</div></th>
							</tr>
						</thead>
					</table>
					<div id="">
						<table width="1500px" class="grid grid_15">
							<colgroup>
								<col width="4%"  />
								<col width="4%" />
								<col width="13%" />
								<col width="17%" />
								<col width="17%" />
								<col width="11%" />
								<col width="7%" />
								<col width="7%" />
								<col width="15%" />
								<col width="30%" />
								<col width="10%" />
							</colgroup>
							<tbody>
							<input type="hidden" value="${brandname }" id="brandname"  name="brandname"/>
										<core:set var="count" value="0" scope="page" />
										<core:forEach items="${listOfCheckreportInfo}" var="bean"
											varStatus="i">

											<tr class="odd">

												<core:set var="sublisting" value="${bean.directory }" />
												<core:set var="businessname" value="${bean.businessname }" />
												<core:set var="businessZip" value="${bean.zip }" />
												<core:set var="businessPhone" value="${bean.phone }" />
													<core:set var="businesscity" value="${bean.city}" scope="page" ></core:set>
													<core:set var="businessstate" value="${bean.state}" scope="page"></core:set>
													<core:set var="listingIdval" value="${bean.listingId }" />
													
												<core:choose>
													<core:when test="${sublisting eq 'LBL' or (empty businessname and  empty businessZip and empty businessPhone and empty businesscity and empty businessstate)}">
														<td class="td_01"><div style="display: none">
																<input type="checkbox" class="checkbox1"
																	value="${bean.listingId}" name="listingId">
															</div></td>
													</core:when>
													
													<core:otherwise>
														<td class="td_01"><div>
																<input type="checkbox" class="checkbox1"
																	value="${bean.listingId}" name="listingId">
															</div></td>

													</core:otherwise>

												</core:choose>
												

												<td class="td_01"><div></div></td>
								<input type="hidden" value="${brandname }" id="brandname" name="brandname"/> 
												<input type="hidden" value="${bean.store}" name="store">

													<input type="hidden" value="${bean.directory}" name="directory">


												<td class="td_03"><div>
														

														<core:set var="subStr"
															value="LBL,google,bing,yelp,yahoo,yp,citysearch,mapquest,superpages,yellowbook,whitepages" />
														<core:set var="subBrandList" value="${bean.directory }" />
														<core:set var="submissionValues"
															value="${fn:split(subStr, ',')}" />
														<core:forEach var="subValue" items="${submissionValues}">


															<label> <core:forEach var="brandStr"
																	items="${subBrandList}" varStatus="i">
																	<core:set var="brandStrArr"
																		value="${fn:split(brandStr,'_')}" />
																	<core:if test="${brandStrArr[0] eq subValue}">

																		<img src="images/logo1-${subValue}.png" alt="">

																	</core:if>
																</core:forEach>


															</label>
														</core:forEach>
													</div></td>
														<core:if test="${bean.directory eq 'LBL'}">
													<core:set var="address" value="${bean.address}"
														scope="page"></core:set>
														<core:set var="businessname" value="${bean.businessname}" scope="page" ></core:set>
													<core:set var="city" value="${bean.city}" scope="page" ></core:set>
													<core:set var="state" value="${bean.state}" scope="page"></core:set>
													<core:set var="zip" value="${bean.zip}" scope="page"></core:set>
													<core:set var="phone" value="${bean.phone}" scope="page"></core:set>
													<core:set var="storeUrl" value="${bean.website}" scope="page"></core:set>
													
													<core:set var="newbusinessname" value="${fn:toLowerCase(businessname)}"
														scope="page"></core:set>
													<core:set var="newaddress" value="${fn:toLowerCase(address)}"
														scope="page"></core:set>
													<core:set var="newcity" value="${fn:toLowerCase(city)}" scope="page" ></core:set>
													<core:set var="newstate" value="${fn:toLowerCase(state)}" scope="page"></core:set>
													<core:set var="newzip" value="${fn:toLowerCase(zip)}" scope="page"></core:set>
													<core:set var="newphone" value="${fn:toLowerCase(phone)}" scope="page"></core:set>
													<core:set var="newstoreUrl" value="${fn:toLowerCase(storeUrl)}" scope="page"></core:set>
												</core:if>
												<core:set var="addressColorCode" value="${bean.colourcode}" scope="page" ></core:set>
													<core:set var="businessname1" value="${bean.businessname}" scope="page" ></core:set>
												<core:set var="addreess1" value="${bean.address}"
													scope="request"></core:set>
												<core:set var="city1" value="${bean.city}" scope="request"></core:set>
												<core:set var="state1" value="${bean.state}" scope="request"></core:set>
												<core:set var="zipvalue" value="${bean.zip}" scope="request"></core:set>
												<core:set var="phone1" value="${bean.phone}" scope="request"></core:set>
												<core:set var="storeUrl1" value="${bean.website}" scope="request"></core:set>
												<core:set var="newbusinessname1" value="${fn:toLowerCase(businessname1)}"
														scope="page"></core:set>
												<core:set var="newaddreess1" value="${fn:toLowerCase(addreess1)}"
													scope="page"></core:set>
													<core:set var="newcity1" value="${fn:toLowerCase(city1)}" scope="page"></core:set>
												<core:set var="newstate1" value="${fn:toLowerCase(state1)}" scope="page"></core:set>
												<core:set var="newzipvalue" value="${fn:toLowerCase(zipvalue)}" scope="page"></core:set>
												<core:set var="newphone1" value="${fn:toLowerCase(phone1)}" scope="page"></core:set>
												<core:set var="newstoreUrl1" value="${fn:toLowerCase(storeUrl1)}" scope="page"></core:set>

												<core:choose>
													<core:when test="${newbusinessname eq newbusinessname1}">
														<td class="td_04"><div>
																<core:out value="${bean.businessname}"></core:out>
															</div></td>
													</core:when>
													<core:otherwise>
														<td class="td_04"><div>
																<span style="color: red"><core:out
																		value="${bean.businessname}"></core:out></span>
															</div></td>
													</core:otherwise>

											
												</core:choose>
												<core:if test="${bean.directory ne 'LBL' }">
												
												
												</core:if>
												<core:set value="${ addressColorCode}" var="colorcode"></core:set>
												<core:choose>
													<core:when test="${'B' eq colorcode}">
														<td class="td_05"><div>
																<core:out value="${bean.address}"></core:out>
															</div></td>
													</core:when>
													<core:otherwise>
													
													<core:if test="${bean.directory ne 'LBL'}">
													<td class="td_05"><div>
																<span style="color: red"><core:out
																		value="${bean.address}"></core:out></span>
															</div></td>
													
													</core:if>
														<core:if test="${bean.directory eq 'LBL'}">
													<td class="td_05"><div>
																<span ><core:out
																		value="${bean.address}"></core:out></span>
															</div></td>
													
													</core:if>
														
													</core:otherwise>

												</core:choose>
												<core:choose>
													<core:when test="${newcity eq newcity1}">
														<td class="td_06"><div>
																<core:out value="${bean.city}"></core:out>
															</div></td>
													</core:when>
													<core:otherwise>
														<td class="td_06"><div>
																<span style="color: red"><core:out
																		value="${bean.city}"></core:out></span>
															</div></td>
													</core:otherwise>

												</core:choose>
												<core:choose>
													<core:when test="${newstate eq newstate1}">
														<td class="td_07"><div>
																<core:out value="${bean.state}"></core:out>
															</div></td>
													</core:when>
													<core:otherwise>
														<td class="td_07"><div>
																<span style="color: red"><core:out
																		value="${bean.state}"></core:out></span>
															</div></td>
													</core:otherwise>

												</core:choose>

												<core:choose>
													<core:when test="${newzip eq newzipvalue}">
														<td class="td_08"><div>
																<core:out value="${bean.zip}"></core:out>
															</div></td>
													</core:when>
													<core:otherwise>
														<td class="td_08"><div>
																<span style="color: red"><core:out
																		value="${bean.zip}"></core:out></span>
															</div></td>
													</core:otherwise>

												</core:choose>


												<core:choose>
													<core:when test="${phone eq phone1}">
														<td class="td_09"><div>
																<core:out value="${bean.phone}"></core:out>
															</div></td>
													</core:when>
													<core:otherwise>
														<td class="td_09"><div>
																<span style="color: red"><core:out
																		value="${bean.phone}"></core:out></span>
															</div></td>
													</core:otherwise>

												</core:choose>
												
												<core:choose>
													<core:when test="${newstoreUrl eq newstoreUrl1}">
														<td class="td_04"><div>
																<core:out value="${bean.website}"></core:out>
															</div></td>
													</core:when>
													<core:otherwise>
														<td class="td_04"><div>
																<span style="color: red"><core:out
																		value="${bean.website}"></core:out></span>
															</div></td>
													</core:otherwise>

											
												</core:choose>
												<core:if test="${bean.directory eq 'LBL'}">

												<td class="td_01"><div>
														<a class="view" href="https://${bean.website}" target="_blank"></a>
													</div></td>
											
												</core:if>
												<core:set var="urlwebiste" value="${bean.website}"></core:set>
											
												
													<core:choose>
													<core:when test="${empty businessname and  empty businessZip}">

												<td class="td_01"><div>
														<a style="display: none" class="view" href="${bean.website}" target="_blank"></a>
													</div></td>
											
												</core:when>
												<core:when test="${empty urlwebiste or (urlwebiste eq 'NOT AVAILABLE')}">

												<td class="td_01"><div>
														<a style="display: none" class="view" href="${bean.website}" target="_blank"></a>
													</div></td>
											
												</core:when>
											
												<core:otherwise >
											
														<core:choose>
														<core:when test="${fn:contains(urlwebiste, 'http' )}">
														
														<td class="td_01"><div>
														<a class="view" href="${bean.website}" target="_blank"></a>
													</div></td>
														
														</core:when>
														<core:when test="${fn:contains(urlwebiste, 'https' )}">
														
														<td class="td_01"><div>
														<a class="view" href="${bean.website}" target="_blank"></a>
													</div></td>
														
														</core:when>
														<core:otherwise>
														<td class="td_01"><div>
														<a class="view" href="https://${bean.website}" target="_blank"></a>
													</div></td>
														
														</core:otherwise>
														
														
														</core:choose>
												
											
												</core:otherwise>
												
												</core:choose>
												</tr>

										</core:forEach>
							</tbody>
						</table>
						</div>
							</spring:form>
						
					</div>
				</div>
				<!-- // box --> 
				<div class="buttons-center">
						<button class="btn_dark_blue_2" onclick="comparelisting()">Compare
						Listings</button>
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