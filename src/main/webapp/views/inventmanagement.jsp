
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
<script src="js/jquery-1.9.1.min.js"></script>
<script src="js/jquery.idTabs.min.js"></script>
<script src="js/jquery.slimscroll.min.js"></script>
<script src="js/jquery.screwdefaultbuttonsV2.js"></script>
<script src="js/jquery.cycle.all.js"></script>
<script src="js/functions.js"></script>
<script src="js/jquery.bpopup.min.js"></script>
<script src="js/jquery.jsort.0.4.min.js"></script>
<script src="js/jquery.session.js"></script>


<script type="text/javascript">
	$(document).ready(function() {

	});
</script>
<script type="text/javascript">
	
</script>

</head>
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
		<div class="content_wrapper">
			<!-- left side navigation -->
			<ul class="ul_left_nav">
				<li class="si_dashboard selected"><a href="dash-board.htm">Dashboard</a></li>
				<li class="si_business_listings"><a
					href="business-listings.htm">Business Listings</a></li>
				<li class="si_error_listings"><a href="listing-error.htm">Listing
						Errors</a></li>
				<li class="si_upload_export"><a href="upload-export.htm">Upload/Export</a></li>
				<li class="si_reports"><a href="reports.htm">Reports</a></li>
				<%
					Integer Role = (Integer) session.getAttribute("roleId");
					if (Role == LBLConstants.CONVERGENT_MOBILE_ADMIN) {
				%>
				<li class="si_brand"><a href="brandchannel.htm">ManageBrands</a></li>
				<%
					}
				%>
				
				<li class="si_schduler"><a href="scheduler.htm">Scheduler</a></li>
				<li class="si_mobile_profile "><a href="manage-account.htm">Manage
						Account</a></li>
				<li class="si_inventory"><a href="inventory.htm">Inventory
						Management</a></li>
				<!--  <li class="si_toolbox"><a href="getConvergentToolbox.htm">Convergent
						Toolbox</a></li>-->
				
			</ul>
			<!-- // left side navigation -->
			<!-- content area -->
			<spring:form method="post" commandName="searchBusiness"
				id="searchAndFilterForm">

				<div class="content" id="id_content">
					<div class="nav_pointer pos_01"></div>
					<!-- subheader -->
					<div class="sb_box">
						<div class="sb_box" style="padding: 5px 0 0;">
							<div class="subheader clearfix">
								<h1>Inventory Management</h1>
								<p></p>
							</div>

							<div class="inner_box">
								<!-- box -->
								<div class="box box_red_title brands_overview">
									<!-- title -->
									<div class="box_title">

										<div class="box_title_right">
											<span></span>
										</div>
									</div>
									<!-- // title -->
									<!-- <h2>Total Listings</h2> -->
									<table width="100%" class="grid grid_07"
										style="margin: 5px 0px;">
										<colgroup>
											<col width="15%" />
										</colgroup>
										<thead id="brandsTableHeaders">
											<tr>
												<th class="th_01"><div></div></th>
												<th class="th_01"><div>Acxiom</div></th>
												<th class="th_03"><div>Bing</div></th>
												<th class="th_04"><div>Factual</div></th>
												<th class="th_05"><div>Google</div></th>
												<th class="th_06"><div>InfoGroup</div></th>
												<th class="th_07"><div>NeuStar</div></th>
											</tr>

										</thead>
									</table>
									<div id="id_brands_overview">

										<table id="brandsTable" width="100%" class="grid grid_07">
											<colgroup>
												<col width="15%" />
											</colgroup>
											<tbody id="brandsInfolist">
												<tr>
													<th class="th_01"><div>Submitted</div></th>
													<core:forEach items="${contractedpartens}" var="partens"
														varStatus="i">
														<td class="td_01"><div>
																<core:out value="${partens.count}"></core:out>
															</div></td>

													</core:forEach>
												</tr>
												<tr>
													<th class="th_01"><div>Contracted</div></th>
													<core:forEach items="${contractedpartens}"
														var="contrctedpartens" varStatus="i">
														<td class="td_01"><div>
																<core:out value="${contrctedpartens.count}"></core:out>
															</div></td>

													</core:forEach>
												</tr>
											</tbody>
										</table>

									</div>
								</div>
							</div>
						</div>


					</div>
				</div>
				
				<div class="sidebar" id="id_sidebar">
					<div class="inner">
						<div class="sb_box_wrapper" style="margin-bottom: 14px;">

							<div class="sb_box" style="padding: 15px 0px;">

								<table width="100%" class="grid grid_09">
									<colgroup>
										<col width="20%" />
									</colgroup>
									<thead>
										<tr>
											<th class="th_01"><div></div></th>
											<th class="th_02"><div>Client1</div></th>
											<th class="th_03"><div>Client2</div></th>
											<th class="th_04"><div>Client3</div></th>
											<th class="th_05"><div>Client4</div></th>
											
										</tr>
									</thead>
								</table>
								<div class="sb_box_shadow">
									<div id="id_user_accounts">

										<table width="100%" class="grid grid_09">
											<colgroup>

												<col width="20%" />

											</colgroup>
											<tbody>
												<tr>
													<th class="th_01"><div>Locations Submitted</div></th>
													<core:forEach items="${allClientLocations}" var="client">
														<td class="td_01"><div>
																<core:out value="${client.count}"></core:out>
															</div></td>

													</core:forEach>
												</tr>
												<tr>
													<th class="th_01"><div>Locations Contracted</div></th>
													<core:forEach items="${allClientLocations}" var="client">
														<td class="td_01"><div>
																<core:out value="${client.count}"></core:out>
															</div></td>
													</core:forEach>
												</tr>


											</tbody>
										</table>
									</div>
									<div class="header">
										<table style="margin: -125px 0px 100px">
											<tbody>
												<tr>
													<td>
														<ul class="top_nav" style="margin: 0px 0px 100px">
															<li class="si_file " style="margin: 0px -140px 0px"><a
																href="addfile.htm"><br>
																<br>
																<span>Add File</span></a></li>

														</ul>
													</td>
													<td>
														<ul class="top_nav" style="margin: 0px 0px 100px">
															<li class="si_file " style="margin: 0px -40px 0px"><a
																href="addfile.htm"><br>
																<br>
																<span>Add File </span></a></li>

														</ul>
													</td>
													<td>
														<ul class="top_nav" style="margin: 0px 0px 100px">
															<li class="si_file "><a href="addfile.htm"><br>
																<br>
																<span>Add File</span></a></li>

														</ul>
													</td>
												</tr>
											</tbody>

										</table>
									</div>
								</div>
								<div class="filters">

									<div class="f_group">
										<label for="display_activity_for">Client</label>
										<spring:select path="client" id="display_activity_for">
										<option value="">Select Brand</option>
											<core:forEach items="${clientNameInfo}" var="client">
												<option value="${client.brandName}">${client.brandName}</option>
											</core:forEach>
										</spring:select>
									</div>

									<div class="f_group">
										<label for="display_activity_for">Client</label>
										<spring:select path="client" id="display_activity_for">
										<option value="">Select Brand</option>
											<core:forEach items="${clientNameInfo}" var="client">
												<option value="${client.brandName}">${client.brandName}</option>
											</core:forEach>
										</spring:select>

									</div>

									<div class="f_group">
										<label for="display_activity_for">Client</label>
										<spring:select path="client" id="display_activity_for">
										<option value="">Select Brand</option>
											<core:forEach items="${clientNameInfo}" var="client">
												<option value="${client.brandName}">${client.brandName}</option>
											</core:forEach>
										</spring:select>
									</div>
									<div class="sb_buttons_center" style="padding: 12px 0 12px">
										<button type="button" value="Loadlocations"
											class="btn_dark_blue_2">Loadlocations</button>
									</div>

								</div>

							</div>
						</div>
					</div>
				</div>

			</spring:form>
		</div>
		<div class="floatfix"></div>
	</div>
	<div class="clearfix"></div>

	<div class="footer">
		<div class="powered">Powered by</div>
	</div>
	

</body>
</html>