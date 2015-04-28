
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0">
<title>Local Business Listings | Upload/Export</title>
<link rel="stylesheet" href="css/global.css">
<link rel="stylesheet" href="fonts/fonts.css">
<link rel="stylesheet" href="css/navigation.css">
<link rel="stylesheet" href="css/common.css">
<link rel="stylesheet" href="css/grids.css">
<link rel="stylesheet" href="css/sidebar.css">
<link rel="stylesheet" href="css/bpopUp.css">
</head>
<body style="background-color: white;">
	<div class="">
		<div class="header">
			<a href="dashboardClient.htm" class="logo_lbl">Local Business Listings</a>
			<ul class="top_nav">
				<li class="home"><a href="dash-board.htm">Home</a></li>
				<li class="help"><a href="clientHelp.htm">Help</a></li>
				<li class="signout"><a href="logout.htm">Signout</a></li>
				<li class="welcome">
					<p>
						Hello, <br> ${user}
					</p>
				</li>
			</ul>
		</div>
		<div class="content_wrapper">
			<ul class="ul_left_nav">
				<li class="si_dashboard"><a href="dashboardClient.htm">Dashboard</a></li>
			<!-- <li class="si_business_listings"><a href="clientbusinesslisting.htm">Business Listings</a></li> -->
			<li class="si_error_listings"><a href="listingClient-error.htm">Listing Errors</a></li>
			<li class="si_reports"><a href="client-reports.htm">Reports</a></li>
			<li class="si_toolbox"><a href="clntToolbox.htm">Convergent Toolbox</a></li>
			</ul>
			<br><br><br><br>
			<h1 align="center" style="color: red;">Currently We are working on this page...........</h1>
		</div>
		
		
	</div>
</body>
</html>