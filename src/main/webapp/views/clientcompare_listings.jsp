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
<!-- <style type="text/css">

.select2-container--default .select2-selection--single {
    background-color: #fff;
    border: 1px solid #aaa;
    background: url(../images/input_campaign.png) 0 0 repeat-x #fff;
    _border-radius: 4px;
        margin: -68px 0px 0px;
        
}
.select2-container--open .select2-dropdown--below {
    border-top: none;
    border-top-left-radius: 0;
    border-top-right-radius: 0;
        width: 150px;
    margin: -46px 0px 0px;
}
.select2-container--default .select2-selection--single .select2-selection__arrow {
    height: 26px;
    position: absolute;
    top: 1px;
    right: 1px;
    width: 20px;
    margin: -21px 0px 0px;
    margin: -21px 0px 0px;
}
.select2-container--default .select2-selection--single .select2-selection__arrow {
    height: 26px;
    position: absolute;
    top: 1px;
    right: 1px;
    width: 20px;
 
    margin: -67px 0px 0px;
}
</style> -->
<style type="text/css">
.listing_compare .box_title_right span {
    float: left;
    display: inline-block;
    font-size: 26px;
    color: #ffff66;
    font-weight: 600;
    margin: 0 0 0 15px;
}

.select2-container--default .select2-selection--single .select2-selection__arrow b {
  border-color: #888 transparent transparent transparent;
  border-style: solid;
  border-width: 5px 4px 0 4px;
  height: 0;
  left: 50%;
  margin-left: -4px;
  margin-top: 20px;
  position: absolute;
  top: 50%;
  width: 0;
}
</style>
<script>
	jQuery(document).ready(function() {
		jQuery(".selectjs").select2();
	});
</script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$("select#storevalue")
								.change(
										function() {
											var store = $("#storevalue").val();
											var directory = $("#directory")
													.val();
											var brandname = $("#brandname")
													.val();

											document.location.href = "clientcompare-listings.htm?store="
													+ store
													+ "&&directory="
													+ directory
													+ "&&brandname="
													+ brandname;
										});
						$("select#directory")
						.change(
								function() {
									var store = $("#store").val();
									var directory = $("#directory")
											.val();
									var brandname = $("#brandname")
											.val();

									document.location.href = "clientcompare-listings.htm?store="
											+ store
											+ "&&directory="
											+ directory
											+ "&&brandname="
											+ brandname;
								});

					});
	function viewListing() {
		var form = document.getElementById('searchstorefiletrForm');
		form.action = "dashClientStoreListingsearch.htm";
		form.submit();
	}
	/* function viewReport() {
		//alert("coming");
		

	} */
</script>
<script type="text/javascript">
	window.onload = function() {
		//var dps = []; 
		// alert(dps);
		//var xVal = dps.length + 1;
		//var yVal = 15;
		var accuracy = $("#CorrectAccuracy").val();
		//alert(inAccuracy);
	
		
		var inAccuracy = 100 - accuracy;
		
		CanvasJS.addColorSet("greenShades", [//colorSet Array

		"#cc3300", "green",

		]);
		var chart = new CanvasJS.Chart("chartContainer", {
			colorSet : "greenShades",
			legend : {
				maxWidth : 250,
				itemWidth : 120
			},
			data : [ {
				type : "pie",
				showInLegend : false,
				//indexLabelFontColor: "red", 
				//indexLabel: "{label} {y}%",
				//toolTipContent:"{legendText} {y}%",
				dataPoints : [

				{
					y : inAccuracy,
					//indexLabel : "Inaccurate",
					lable : "Inaccurate"
				}, {
					y : accuracy,
					//indexLabel : "Accurate",
					lable : "Accurate"
				} ]

			} ]
		});
		chart.render();

	}
</script>
<script type="text/javascript" src="js/canvasjs.min.js"></script>
<script>
	$(function() {

		var moveLeft = 0;
		var moveDown = 0;
		$('a.popper').hover(function(e) {

			var target = '#' + ($(this).attr('data-popbox'));

			$(target).show();
			moveLeft = $(this).outerWidth() - 200;
			moveDown = ($(target).outerHeight() - 160 / 2);
		}, function() {
			var target = '#' + ($(this).attr('data-popbox'));
			$(target).hide();
		});

		$('a.popper')
				.mousemove(
						function(e) {
							var target = '#' + ($(this).attr('data-popbox'));

							leftD = e.pageX + parseInt(moveLeft);
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
<div id="pop2" class="popbox">

	<p>${storeUrl}</p>
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

					<!-- <span>Export Report</span>
				<a href="" class="ico_pdf">PDF</a> -->
					<p>Convergent Mobile</p>
				</div>
				<!-- // subheader -->
				<div class="inner_box">
					<!-- box -->
					<div class="box box_red_title listing_compare">
						<spring:form commandName="searchBusiness" method="post"
							id="searchstorefiletrForm">
							<core:forEach items="${lblportlisting}" var="lbllisting">
								<input type="hidden" name="accuracy" value="${accuracy}"
									id="accuracy" />
								<!-- title -->
								<div class="box_title">
									<h2>Compare Listings</h2>
									<p>${lbllisting.companyName }, #${lbllisting.store}</p>
									<input type="hidden" value="${lbllisting.store}" id="store" name="store">
									<div class="box_title_right btr_2">
										<div class="box_errors">
											<p>
												Directories<br>showing errors
											</p>
											<span>${directoryShowingErrors}</span>
											<core:set value="${((count)/7)*100}" var="countvalue"></core:set>
											<input type="hidden" name="accuracyVal" value="${LocationAccuracy}" id="CorrectAccuracy"> 
											<input type="hidden" name="accuracyVal" value="${countvalue}" id="accuracyVal"> 
											<%-- <span>${countvalue}</span> --%>
										</div>
									</div>
								</div>
								<!-- // title -->

								<div class="box-compare">
									<div class="hr"></div>
									<div class="box-compare-left">

										<img src="images/logo-lbl-125.png">
										<iframe
											src="https://www.google.com/maps/embed/v1/place?key=${googleKey}
          &q=${lbllisting.companyName },${lbllisting.locationAddress },${lbllisting.locationCity },
          ${lbllisting.locationState }"
											width="100%" height="230" frameborder="0" style="border: 0">
										</iframe>
										<input type="hidden" name="store" value="${lbllisting.store }" />
										<ul>

											<core:set var="companyName"
												value="${lbllisting.companyName }"></core:set>
											<core:set var="companyName2"
												value="${fn:toUpperCase(companyName)}"></core:set>
											<core:set var="locationAddress"
												value="${lbllisting.locationAddress }"></core:set>
											<core:set var="locationCity"
												value="${lbllisting.locationCity }"></core:set>
											<core:set var="locationState"
												value="${lbllisting.locationState }"></core:set>
											<core:set var="store" value="${lbllisting.store }"></core:set>
											<core:set var="locationPhone"
												value="${lbllisting.locationPhone }"></core:set>
											<core:set var="webAddressurl"
												value="${lbllisting.webAddress }"></core:set>
												<core:set var="locationZipCode"
												value="${lbllisting.locationZipCode }"></core:set>
												
												<core:set var="newlocationAddress"
												value="${fn:toUpperCase(locationAddress)}"></core:set>
											<core:set var="newlocationCity"
												value="${fn:toUpperCase(locationCity) }"></core:set>
											<core:set var="newlocationState"
												value="${fn:toUpperCase(locationState) }"></core:set>
											<core:set var="newstore" value="${fn:toUpperCase(store) }"></core:set>
											<core:set var="locationPhone"
												value="${lbllisting.locationPhone }"></core:set>
											<core:set var="newwebAddressurl"
												value="${fn:toUpperCase(webAddress) }" scope="page"></core:set>
												<core:set var="newlocationZipCode"
												value="${fn:toUpperCase(locationZipCode) }"></core:set>
												
												<core:set value="${fn:substringAfter(newwebAddressurl, 'www.')}" var="substringLBLUrl" scope="page"></core:set>


											<li>${companyName2 }</li>
											<li>${lbllisting.locationAddress }</li>
											<li>${lbllisting.locationCity },
												${lbllisting.locationState } ${lbllisting.locationZipCode }</li>
											<li>${lbllisting.locationPhone }</li>
											<core:set var="webAddress" value="${lbllisting.webAddress }">
											</core:set>
											<core:set var="webAddress2"
												value="${fn:substring(webAddress, 0, 42)}"></core:set>
											<li>
											<core:choose>
										<core:when test="${fn:contains(webAddress, 'http')}">
											<a href="${lbllisting.webAddress }"
												class="popper" data-popbox="pop1" target="_blank">${webAddress2 }</a>
											</core:when>
											<core:when test="${fn:contains(webAddress, 'https')}">
											<a href="${lbllisting.webAddress }"
												class="popper" data-popbox="pop1" target="_blank">${webAddress2 }</a>
											</core:when>

										<core:otherwise >
										<a href="http://${lbllisting.webAddress }"
												class="popper" data-popbox="pop1" target="_blank">${webAddress2 }</a>
											
										
										</core:otherwise>
											</core:choose>
											
											
											</li>


										</ul>
							</core:forEach>
							<button class="btn_dark_blue_2" onclick="viewListing()">View
								Details</button>
					</div>
					
					<div class="box-compare-right">
						<input type="hidden" value="${brandname }" id="brandname"
							name="brandname" />
							<core:set value="${directory }" var="directory"></core:set>
								<core:if test="${not empty message }">
							<select name="directory" id="directory"  >
								
									<core:if test="${directory eq 'google'}">
										<option value="google">Google</option>
											<option value="bing" >Bing</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="yp">Yp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest">Mapquest</option>
													<option value="superpages">Superpages</option>
													<option value="yellowbook">Yellowbook</option>
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'bing'}">
									<option value="bing" >Bing</option>
										<option value="google">Google</option>
											
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="yp">Yp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest">Mapquest</option>
													<option value="superpages">Superpages</option>
													<option value="yellowbook">Yellowbook</option>
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'yahoo'}">
									<option value="yahoo" >Yahoo</option>
									
										<option value="google">Google</option>
											<option value="bing" >Bing</option>
													
													<option value="yelp" >Yelp</option>
													<option value="yp">Yp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest">Mapquest</option>
													<option value="superpages">Superpages</option>
													<option value="yellowbook">Yellowbook</option>
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'yelp'}">
									<option value="yelp" >Yelp</option>
										<option value="google">Google</option>
											<option value="bing" >Bing</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yp">Yp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest">Mapquest</option>
													<option value="superpages">Superpages</option>
													<option value="yellowbook">Yellowbook</option>
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'yp'}">
									<option value="yp">Yp</option>
									
										<option value="google">Google</option>
											<option value="bing" >Bing</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest">Mapquest</option>
													<option value="superpages">Superpages</option>
													<option value="yellowbook">Yellowbook</option>
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'citysearch'}">
									<option value="citysearch" >Citysearch</option>
									
										<option value="google">Google</option>
											<option value="bing" >Bing</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="yp">Yp</option>
													
													<option value="mapquest">Mapquest</option>
													<option value="superpages">Superpages</option>
													<option value="yellowbook">Yellowbook</option>
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'mapquest'}">
									<option value="mapquest">Mapquest</option>
									
										<option value="google">Google</option>
											<option value="bing" >Bing</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="yp">Yp</option>
													<option value="citysearch" >Citysearch</option>
													
													<option value="superpages">Superpages</option>
													<option value="yellowbook">Yellowbook</option>
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'superpages'}">
									<option value="superpages">Superpages</option>
								
										<option value="google">Google</option>
												<option value="bing" >Bing</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="yp">Yp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest">Mapquest</option>
													
													<option value="yellowbook">Yellowbook</option>
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'yellowbook'}">
										<option value="yellowbook">Yellowbook</option>
									
										<option value="google">Google</option>
											<option value="bing" >Bing</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="yp">Yp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest">Mapquest</option>
													<option value="superpages">Superpages</option>
												
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'whitepages'}">
									<option value="whitepages">Whitepages</option>
									
										<option value="google">Google</option>
											<option value="bing" >Bing</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="yp">Yp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest">Mapquest</option>
													<option value="superpages">Superpages</option>
													<option value="yellowbook">Yellowbook</option>
													
									</core:if>
									
							</select>
							<select name="store" id="storevalue" class="selectjs"
								style="width: 150px;" >


								<option value="">Select Store</option>

								<core:forEach items="${liststoreNames}" var="stores">
									<option value="${stores}">${stores}</option>
								</core:forEach>
							</select>
							<div style="margin: 40px 40px 0px">
							<span style="color: red;margin: 0px 40px 0px">${message}</span></div>
							</core:if>
							
						<core:forEach items="${checkreportlisting}" var="otherlisting">
							<select name="directory" id="directory"  >
								<%-- <option value="${otherlisting.directory }">${otherlisting.directory }</option> --%>
								<core:set value="${otherlisting.directory }" var="directory"></core:set>
							
									<core:if test="${directory eq 'google'}">
										<option value="google">Google</option>
											<option value="bing" >Bing</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="yp">Yp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest">Mapquest</option>
													<option value="superpages">Superpages</option>
													<option value="yellowbook">Yellowbook</option>
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'bing'}">
									<option value="bing" >Bing</option>
										<option value="google">Google</option>
											
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="yp">Yp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest">Mapquest</option>
													<option value="superpages">Superpages</option>
													<option value="yellowbook">Yellowbook</option>
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'yahoo'}">
									<option value="yahoo" >Yahoo</option>
									
										<option value="google">Google</option>
											<option value="bing" >Bing</option>
													
													<option value="yelp" >Yelp</option>
													<option value="yp">Yp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest">Mapquest</option>
													<option value="superpages">Superpages</option>
													<option value="yellowbook">Yellowbook</option>
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'yelp'}">
									<option value="yelp" >Yelp</option>
										<option value="google">Google</option>
											<option value="bing" >Bing</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yp">Yp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest">Mapquest</option>
													<option value="superpages">Superpages</option>
													<option value="yellowbook">Yellowbook</option>
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'yp'}">
									<option value="yp">Yp</option>
									
										<option value="google">Google</option>
											<option value="bing" >Bing</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest">Mapquest</option>
													<option value="superpages">Superpages</option>
													<option value="yellowbook">Yellowbook</option>
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'citysearch'}">
									<option value="citysearch" >Citysearch</option>
									
										<option value="google">Google</option>
											<option value="bing" >Bing</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="yp">Yp</option>
													
													<option value="mapquest">Mapquest</option>
													<option value="superpages">Superpages</option>
													<option value="yellowbook">Yellowbook</option>
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'mapquest'}">
									<option value="mapquest">Mapquest</option>
									
										<option value="google">Google</option>
											<option value="bing" >Bing</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="yp">Yp</option>
													<option value="citysearch" >Citysearch</option>
													
													<option value="superpages">Superpages</option>
													<option value="yellowbook">Yellowbook</option>
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'superpages'}">
									<option value="superpages">Superpages</option>
								
										<option value="google">Google</option>
												<option value="bing" >Bing</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="yp">Yp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest">Mapquest</option>

													<option value="yellowbook">Yellowbook</option>
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'yellowbook'}">
										<option value="yellowbook">Yellowbook</option>
									
										<option value="google">Google</option>
											<option value="bing" >Bing</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="yp">Yp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest">Mapquest</option>
													<option value="superpages">Superpages</option>
												
													<option value="whitepages">Whitepages</option>
									</core:if>
									<core:if test="${directory eq 'whitepages'}">
									<option value="whitepages">Whitepages</option>
									
										<option value="google">Google</option>
											<option value="bing" >Bing</option>
													<option value="yahoo" >Yahoo</option>
													<option value="yelp" >Yelp</option>
													<option value="yp">Yp</option>
													<option value="citysearch" >Citysearch</option>
													<option value="mapquest">Mapquest</option>
													<option value="superpages">Superpages</option>
													<option value="yellowbook">Yellowbook</option>
													
									</core:if>
									
							
												
							</select>

							<select name="store" id="storevalue" class="selectjs"
								style="width: 150px;">


								<option value="">Select Store</option>

								<core:forEach items="${liststoreNames}" var="stores">
									<option value="${stores}">${stores}</option>
								</core:forEach>
							</select>
						
							<input type="hidden" name="directory"
								value="${otherlisting.directory }" id="directory">
							<!-- 	<button class="btr_button" onclick="viewReport()" type="button">View
								Report</button>
 -->
							<iframe 
								src="https://www.google.com/maps/embed/v1/place?key=${googleKey}
          		&q=${otherlisting.businessname },${otherlisting.address },${otherlisting.city },${otherlisting.state }"
								width="100%" height="230" frameborder="0" style="border: 0;"></iframe>
							<ul>

								<core:set var="businessname"
									value="${otherlisting.businessname }"></core:set>
								<core:set var="businessname2"
									value="${fn:toUpperCase(businessname)}"></core:set>
								<core:set var="address" value="${otherlisting.address }"></core:set>
								<core:set var="city" value="${otherlisting.city }"></core:set>
								<core:set var="state" value="${otherlisting.state }"></core:set>
								<core:set var="checkstore" value="${otherlisting.store }"></core:set>
								<core:set var="phone" value="${otherlisting.phone }"></core:set>
								<core:set var="storeUrl" value="${otherlisting.website }"></core:set>
								<core:set var="zip" value="${otherlisting.zip }"></core:set>
				
								
								
								
								<core:set var="newaddress" value="${fn:toUpperCase(address) }"></core:set>
								<core:set var="newcity" value="${fn:toUpperCase(city) }"></core:set>
								<core:set var="newstate" value="${fn:toUpperCase(state) }"></core:set>
								<core:set var="newcheckstore" value="${fn:toUpperCase(checkstore) }"></core:set>
								<core:set var="newphone" value="${fn:toUpperCase(phone) }"></core:set>
								<core:set var="newstoreUrl" value="${fn:toUpperCase(storeUrl) }" scope="page"></core:set>
								<core:set var="newzip" value="${fn:toUpperCase(zip) }"></core:set>
								<core:set value="${WebsiteUrl }" var="myurl"></core:set>
								
							<core:set value="${LBLwebAddress }" var="myLBLurl"></core:set>
								<core:choose>
									<core:when test="${companyName2 eq businessname2}">
											
										<li>${businessname2 }</li>
									</core:when>
									<core:otherwise>
										<li><span style="color: red">${businessname2}</span></li>
									</core:otherwise>

								</core:choose>
								<core:choose>
									<core:when test="${adressColorCode eq 'B'}">
										<li>${otherlisting.address }</li>
									</core:when>
									<core:otherwise>
										<li><span style="color: red">${otherlisting.address }</span></li>
									</core:otherwise>

								</core:choose>

								<core:choose>
									<core:when
										test="${(newlocationCity eq newcity) and (newlocationState eq newstate) and (newzip eq newlocationZipCode) }">
										<li>${otherlisting.city }, 
										${otherlisting.state } ${otherlisting.zip }</li>
									</core:when>
									<core:otherwise>
										<li><span style="color: red">${otherlisting.city }, 
										${otherlisting.state } ${otherlisting.zip }
												</span></li>
									</core:otherwise>

								</core:choose>

								<core:choose>
									<core:when test="${locationPhone eq phone }">
										<li>${otherlisting.phone }</li>
									</core:when>
									<core:otherwise>
										<li><span style="color: red">${otherlisting.phone }</span></li>
									</core:otherwise>

								</core:choose>
							
								<core:choose>
									<core:when test="${myLBLurl eq myurl }">
										<core:set var="storeurl" value="${otherlisting.website }">
										</core:set>
										<core:set var="storeurl1"
											value="${fn:substring(storeurl, 0, 42)}"></core:set>
										<li>
									
										<core:choose>
											<core:when test="${empty storeurl}">
											<a href="#" target="_blank"
											class="popper" data-popbox="pop2">NOT AVAILABLE</a>
											</core:when>
												<core:when test="${storeurl eq (NULL) }">
											<a href="#" target="_blank"
											class="popper" data-popbox="pop2">NOT AVAILABLE</a>
											</core:when>
										<core:when test="${fn:contains(storeurl, 'http' )}">
											<a href="${storeurl}" target="_blank"
											class="popper" data-popbox="pop2">${storeurl1}</a>
											</core:when>
											<core:when test="${fn:contains(storeurl, 'https')}">
										<span ><a
												href="${storeurl}" target="_blank"
												class="popper" data-popbox="pop2">${storeurl1 }</a></span>
										</core:when>

										<core:otherwise >
										<a href="https://${storeurl}" target="_blank"
											class="popper" data-popbox="pop2">${storeurl1}</a>
											
										
										</core:otherwise>
											</core:choose>
										
									</li>
									</core:when>
									<core:otherwise>
									
										<core:set var="storeurl" value="${otherlisting.website }">
										</core:set>
										<core:set var="storeurl1"
											value="${fn:substring(storeurl, 0, 42)}"></core:set>
										<li>
										<core:choose>
										
										<core:when test="${empty storeurl }">
											<span style="color: red"><a href="#" target="_blank"
											class="popper" data-popbox="pop2"  style="color: red">NOT AVAILABLE</a></span>
											</core:when>
												<core:when test="${storeurl eq (NULL) }">
											<span style="color: red"><a href="#" target="_blank" 
											class="popper" data-popbox="pop2" style="color: red">NOT AVAILABLE</a></span>
											</core:when>
										<core:when test="${fn:contains(storeurl, 'http')}">
										<span style="color: red"><a
												href="${storeurl}" target="_blank"
												class="popper" data-popbox="pop2" style="color: red">${storeurl1}</a></span>
										</core:when>
										<core:when test="${fn:contains(storeurl, 'https')}">
										<span style="color: red"><a
												href="${storeurl}" target="_blank"
												class="popper" data-popbox="pop2" style="color: red">${storeurl1 }</a></span>
										</core:when>
										<core:otherwise>
										<span style="color: red"><a
												href="https://${storeurl}" target="_blank"
												class="popper" data-popbox="pop2" style="color: red">${storeurl1}</a></span>
										</core:otherwise>
										</core:choose>
										
  
										</li>
									</core:otherwise>

								</core:choose>
							</ul>
				<core:set value="${otherlisting.storeUrl}" var="websiteurlonline"></core:set>
			
				<core:choose>
				<core:when test="${empty websiteurlonline or websiteurlonline eq 'NOT AVAILABLE'}">
				<a class="btn_dark_blue_2" style="display: none"  href="${otherlisting.storeUrl}" target="_blank">View
							Online</a>
				</core:when>
				<core:otherwise>
				
				<a class="btn_dark_blue_2"  href="${otherlisting.storeUrl}" target="_blank">View
							Online</a>
				
				</core:otherwise>
				
				</core:choose>
						
									</core:forEach>
					</div>
					<div class="floatfix"></div>
					<div class="box-compare-result">
						<p class="title">Accuracy Score</p>
						<core:set value="${ accuracy}" var="countval"></core:set>
						<core:choose>
						<core:when test="${ empty countval }">
						<p class="percent">0%</p>
						
						</core:when>
						<core:otherwise>
						<fmt:formatNumber value="${countvalue }" var="accuracyPercentage" maxFractionDigits="0" ></fmt:formatNumber>
							<p class="percent">${LocationAccuracy}%</p>
						</core:otherwise>
						</core:choose>
						<div id="chartContainer"
							style="height: 300px; width: 100%; margin-right: -1px;margin: -12px 0px 0px"
							class="box-visibility-right">
							<!-- <p class="title"></p>
							<img src="images/graph-02.png"> -->
							<!-- <div  >
  								</div> -->

						</div>
						<span class="green">Accurate</span> <span class="red">Inaccurate</span>
					</div>
					<div class="floatfix"></div>
				</div>
			</div>
			</spring:form>
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
	<!-- // page wrapper -->

</body>
</html>