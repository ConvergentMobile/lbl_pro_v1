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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
</head>

<body id="office">
<!-- page wrapper -->
<div class="wrapper"> 
	<!-- header -->
	<div class="header"> <a href="dashboard.html" class="logo_lbl">Local Business Listings</a>
		<ul class="top_nav">
			<li class="home"><a href="dashboard-corporate.html">Home</a></li>
			<li class="help"><a href="#">Help</a></li>
			<li class="signout"><a href="#">Signout</a></li>
			<li class="welcome">
				<p>Hello, <br>
					John Brown</p>
			</li>
		</ul>
	</div>
	<!-- // header --> 
	<!-- content wrapper -->
	<div class="content_wrapper no-sidebar"> 
		<!-- left side navigation -->
		<ul class="ul_left_nav">
			<li class="si_dashboard"><a href="dashboard.html">Dashboard</a></li>
			<li class="si_business_listings selected"><a href="business-listings-profile.html">Business Listings</a></li>
			<li class="si_upload_export"><a href="upload-export.html">Upload/Export</a></li>
			<li class="si_reports"><a href="reports.html">Reports</a></li>
			<li class="si_mobile_profile "><a href="manage-account.html">Manage Account</a></li>
			<li class="si_toolbox"><a href="toolbox-corporate.html">Convergent Toolbox</a></li>
		</ul>
		<!-- // left side navigation --> 
		<!-- content area -->
		<div class="content" id="id_content">
			<div class="nav_pointer pos_01"></div>
			<!-- subheader -->
			<div class="subheader clearfix">
				<h1>BUSINESS LISTING</h1>
				<p>Convergent Mobile</p>
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
            	<div class="quick-form">
                	<div class="styled-select">
                        <select name="menu-4562" class="searchby">
                             <option value="BrandName">Search by Brand Name</option>
                            <option value="Business">Search by Basiness Name</option>
                            <option value="Store">Search by Store #</option>
                            <option value="phone">Search by Phone Number</option>
                        </select>
                    </div>
                    <input type="text" class="search-field" placeholder="search text">
                    <input type="image" src="images/search-icon.png">
                    <div href="#" class="small">Clear</div>
                    <div class="advanced">Advanced Search</div>
                </div>
            
				<div class="filters-2">
					<input placeholder="Brand Name" type="text">
					<input placeholder="Business Name"  type="text">
					<input placeholder="Store #"  type="text">
					<input placeholder="Phone #"  type="text"><br>
					<input placeholder="Address"  type="text">
					<input placeholder="City"  type="text">
                    <input placeholder="State"  type="text">
                    <input placeholder="Zip"  type="text"><br>
					<a href="#" class="btn_dark_blue_2">Apply FIlter</a>
                    <a href="#" class="btn_dark_blue_2">Clear FIlter</a>
					<a href="#" class="btn_grey_3 plus"><span>Add/Remove Fields</span></a>
				</div>
				<!-- box -->
				<div class="box box_red_title business-listings"> 
					<!-- title -->
					<div class="box_title">
						<h2>Liberty Tax Service / VA </h2>
					</div>
					<!-- // title -->
					<table width="100%" class="grid grid_13">
						<thead>
							<tr>
								<th class="th_01"><div><input type="checkbox"></div></th>
								<th class="th_03"><div>Store#</div></th>
								<th class="th_04"><div>Business Name</div></th>
								<th class="th_05"><div>Address</div></th>
								<th class="th_06"><div>City</div></th>
								<th class="th_07"><div>State</div></th>
								<th class="th_08"><div>Zip</div></th>
								<th class="th_09"><div>Phone</div></th>
							</tr>
						</thead>
					</table>
					<div id="id_business_listings">
						<table width="100%" class="grid grid_13">
							<tbody>
								<tr class="odd">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>123 Main St</div></td>
									<td class="td_06"><div>Overland Park</div></td>
									<td class="td_07"><div>KS</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="even">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>980 Some Road</div></td>
									<td class="td_06"><div>Pittsburgh</div></td>
									<td class="td_07"><div>PA</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="odd">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>123 Main St</div></td>
									<td class="td_06"><div>Overland Park</div></td>
									<td class="td_07"><div>KS</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="even">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>980 Some Road</div></td>
									<td class="td_06"><div>Pittsburgh</div></td>
									<td class="td_07"><div>PA</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="odd">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>123 Main St</div></td>
									<td class="td_06"><div>Overland Park</div></td>
									<td class="td_07"><div>KS</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="even">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>980 Some Road</div></td>
									<td class="td_06"><div>Pittsburgh</div></td>
									<td class="td_07"><div>PA</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="odd">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>123 Main St</div></td>
									<td class="td_06"><div>Overland Park</div></td>
									<td class="td_07"><div>KS</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="even">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>980 Some Road</div></td>
									<td class="td_06"><div>Pittsburgh</div></td>
									<td class="td_07"><div>PA</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="odd">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>123 Main St</div></td>
									<td class="td_06"><div>Overland Park</div></td>
									<td class="td_07"><div>KS</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="even">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>980 Some Road</div></td>
									<td class="td_06"><div>Pittsburgh</div></td>
									<td class="td_07"><div>PA</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="odd">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>123 Main St</div></td>
									<td class="td_06"><div>Overland Park</div></td>
									<td class="td_07"><div>KS</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="even">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>980 Some Road</div></td>
									<td class="td_06"><div>Pittsburgh</div></td>
									<td class="td_07"><div>PA</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="odd">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>123 Main St</div></td>
									<td class="td_06"><div>Overland Park</div></td>
									<td class="td_07"><div>KS</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="even">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>980 Some Road</div></td>
									<td class="td_06"><div>Pittsburgh</div></td>
									<td class="td_07"><div>PA</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="odd">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>123 Main St</div></td>
									<td class="td_06"><div>Overland Park</div></td>
									<td class="td_07"><div>KS</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="even">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>980 Some Road</div></td>
									<td class="td_06"><div>Pittsburgh</div></td>
									<td class="td_07"><div>PA</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="odd">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>123 Main St</div></td>
									<td class="td_06"><div>Overland Park</div></td>
									<td class="td_07"><div>KS</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="even">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>980 Some Road</div></td>
									<td class="td_06"><div>Pittsburgh</div></td>
									<td class="td_07"><div>PA</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="odd">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>123 Main St</div></td>
									<td class="td_06"><div>Overland Park</div></td>
									<td class="td_07"><div>KS</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="even">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>980 Some Road</div></td>
									<td class="td_06"><div>Pittsburgh</div></td>
									<td class="td_07"><div>PA</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="odd">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>123 Main St</div></td>
									<td class="td_06"><div>Overland Park</div></td>
									<td class="td_07"><div>KS</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
								<tr class="even">
									<td class="td_01"><div><input type="checkbox"></div></td>
									<td class="td_03"><div>1234567890</div></td>
									<td class="td_04"><div>Liberty Tax Service</div></td>
									<td class="td_05"><div>980 Some Road</div></td>
									<td class="td_06"><div>Pittsburgh</div></td>
									<td class="td_07"><div>PA</div></td>
									<td class="td_08"><div>12345</div></td>
									<td class="td_09"><div>123-456-7890</div></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<!-- // box --> 
				<div class="buttons-center">
					<a href="#" class="btn_dark_blue_2">Edit</a>
					<a href="#" class="btn_dark_blue_2">Download</a>
					<a href="#" class="btn_dark_blue_2">Upload</a>
					<a href="#" class="btn_dark_blue_2">Export</a>
					<a href="#" class="btn_dark_blue_2">Delete</a>
					<a href="#" class="btn_dark_blue_2">Add/Create Location</a>
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