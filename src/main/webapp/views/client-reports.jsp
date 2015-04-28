<%@ taglib uri="http://www.springframework.org/tags/form" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0">
<title>Local Business Listings | Reports</title>
<link rel="stylesheet" href="css/global.css">
<link rel="stylesheet" href="fonts/fonts.css">
<link rel="stylesheet" href="css/navigation.css">
<link rel="stylesheet" href="css/common.css">
<link rel="stylesheet" href="css/grids.css">
<link rel="stylesheet" href="css/sidebar.css">
<link rel="stylesheet" href="css/jquery-ui.css">
<link rel="stylesheet" href="css/bpopUp.css">
 <link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
<script src="js/jquery.idTabs.min.js"></script>
<script src="js/jquery.slimscroll.min.js"></script>
<script src="js/jquery.screwdefaultbuttonsV2.js"></script>
<script src="js/jquery.cycle.all.js"></script>
<script src="js/functions.js"></script>
<script src="js/jquery.bpopup.min.js"></script>
<script src="js/jquery.session.js"></script>
</head>

<script type="text/javascript">
	 $(document).ready(function() {
		 if ($.session.get("isUploadReport") === "true") {
				$.session.set("isUploadReport",false);
				$('#upload').bPopup();
				$("ul li").removeClass("selected");
			}
	});
	 $(function(){
		    $('#id_report_01').slimScroll({
		        height: '530px'
		    });
		});
     	$(function() {
	    $( "#startDate" ).datepicker({
		'format': 'm/d/yyyy',
		'autoclose': true    
	    });

	});

     	$(function() {
	    $( "#endDate" ).datepicker({
		'format': 'm/d/yyyy',
		'autoclose': true    
	    });

	});
   	 $(function(){
		    $('.pp-body').slimScroll({
		        height: '300px'
		    });
		});
	
	function getParams() {
		var selRptId = $("#report\\.id").val();
		document.location.href = "client-reports.htm?reportId="+selRptId;		
	}
	
	function runIt() {
		var form = document.getElementById("reportsForm");	

		form.action = 'runClientReport.htm?page=1';
		form.method = 'POST';			
		form.submit();		
	}
	function getClientUploadRepostDetails(id){
		//alert(id);
		id = "tr_"+id;
		var brand = $("tr#"+id+" > td#brand").text();
		var date = $("tr#"+id+" > td#date").text();
		//alert(brand + " :: "+date);
		$.session.set("isUploadReport",true);
		document.location.href="getClientUploadReportDetails.htm?brand="+brand+"&date="+date;
		
	}
 </script>
 
<body id="office">


 <div class="popup" id="upload" style="display: none;width: 60%;height: 60%;margin-left:262px;" >
	<div class="pp-header">
		
		<span class="buttonIndicator b-close"><span>X</span></span>
	</div>
	
	<div class="pp-body">
			 
		  <table width="100%"  class="grid grid_13">
						<colgroup>
							<col width="15%" />
							<col width="15%" />
							<col width="12%" />
							<col width="10%" />
							<col width="10%" />
							<col width="11%" />
						</colgroup>
						<thead id="businessTableHeaders">
							<tr>
								<th class="th_04"><div>Business Name</div></th>
								<th class="th_05"><div>Address</div></th>
								<th class="th_06"><div>City</div></th>
								<th class="th_07"><div>State</div></th>
								<th class="th_08"><div>Zip</div></th>
								<th class="th_09"><div>Phone</div></th>
							</tr>
						</thead>
					</table>
				
						<table  id="businessTable" width="100%" height="60%" class="grid grid_13">
							<colgroup>
								<col width="15%" />
								<col width="15%" />
								<col width="12%" />
								<col width="10%" />
								<col width="10%" />
								<col width="11%" />
							</colgroup>
							<tbody id="businessInfo">
						<core:forEach items="${uploadReports}" var="bean" varStatus="i">
								<tr class="odd">
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




<!-- page wrapper -->
<div class="wrapper"> 
	<!-- header -->
	<div class="header"> <a href="dashboardClient.htm" class="logo_lbl">Local Business Listings</a>
		<ul class="top_nav">
			<li class="home"><a href="dash-board.htm">Home</a></li>
			<li class="help"><a href="#">Help</a></li>
			<li class="signout"><a href="logout.htm">Signout</a></li>
			<li class="welcome">
				<p>Hello, <br>
					${user}</p>
			</li>
		</ul>
	</div>
	<!-- // header --> 
	<!-- content wrapper -->
	<div class="content_wrapper"> 
		<!-- left side navigation -->
		<ul class="ul_left_nav">
			<li class="si_dashboard"><a href="dashboardClient.htm">Dashboard</a></li>
			<!-- <li class="si_business_listings"><a href="clientbusinesslisting.htm">Business Listings</a></li> -->
			<li class="si_error_listings"><a href="listingClient-error.htm">Listing Errors</a></li>
			<li class="si_reports selected"><a href="client-reports.htm">Reports</a></li>
			<li class="si_toolbox"><a href="clntToolbox.htm">Convergent Toolbox</a></li>
		</ul>
		<!-- // left side navigation --> 
		<!-- content area -->
		<div class="content" id="id_content">
    	<div class="nav_pointer pos_01"></div>
		  <!-- subheader -->
		  <div class="subheader clearfix">
			<h1>REPORTS</h1>
			<p>${channelName}</p>
		  </div>
		  <!-- // subheader -->
		<spring:form method="post" modelAttribute="reportForm" id="reportsForm">		
		  		  
			<div class="inner_box">
			<!-- box -->
			<div class="box box_red_title box_report">
				<!-- title -->
				<div class="box_title mb9">
				<h2>View Report</h2>
			  </div>
			  <!-- // title -->
			  <!-- wide_column -->
					<div class="wide_column_wrapper report_container">
						<div class="description" id="id_report_01">
							<core:if test="${reportForm.reportRows == null}">
								<p>Generate reports using the tools on the right.</p>
							</core:if>

							<core:if test="${fn:length(reportForm.reportRows) <= 0}">
								<p>No data found.</p>
							</core:if>

							<core:if test="${fn:length(reportForm.reportRows) > 0}">
								<table width="100%" class="grid grid_07">
									<core:forEach var="reportColumn"
										items="${reportForm.reportColumnHeaders}" varStatus="loopStatus1">
										<th class="th_01"><a
											href="javascript:runSort(${currentPage}, ${loopStatus1.count})"><core:out
													value="${reportColumn}" /></a></th>
									</core:forEach>

									<core:set var="colCnt"
										value="${fn:length(reportForm.reportColumnHeaders)}" />
										
									<core:forEach var="reportDataRow" items="${reportForm.reportRows}"
										varStatus="loopStatus">

										<core:set var="rowidx" value="${loopStatus.index % 2}" />

										<tr class="row${rowidx}" id="tr_${loopStatus.index}">
											<core:forEach begin="0" end="${colCnt-1}" varStatus="i">

														<core:if test="${i.index == 0}">
															<core:choose>
																<core:when test="${reportType eq 'UploadReport' }">
																	<td class="td_01" id="brand"><a href="#" onclick="getClientUploadRepostDetails(${loopStatus.index})"><core:out
																				value="${reportDataRow.field1}" /></a></td>
																</core:when>
																<core:otherwise>
																	<td class="td_01" id="brand"><core:out
																			value="${reportDataRow.field1}" /></td>
																</core:otherwise>
															</core:choose>

														</core:if>

														<core:if test="${i.index == 1}">
													<td class="td_01"><core:out
															value="${reportDataRow.field2}" /></td>
												</core:if>

												<core:if test="${i.index == 2}">
													<td class="td_01" id="date"><core:out
															value="${reportDataRow.field3}" /></td>
												</core:if>

												<core:if test="${i.index == 3}">
													<td class="td_01"><core:out
															value="${reportDataRow.field4}" /></td>
												</core:if>

												<core:if test="${i.index == 4}">
													<td class="td_01"><core:out
															value="${reportDataRow.field5}" /></td>
												</core:if>

												<core:if test="${i.index == 5}">
													<td class="td_01"><core:out
															value="${reportDataRow.field6}" /></td>
												</core:if>
											</core:forEach>
										</tr>
									</core:forEach>
								</table>
							</core:if>
						</div>

						<core:if test="${reportForm.reportRows != null}">
						
							<br />

							<%--For displaying Page numbers.
		  						  The when condition does not display a link for the current page--%>
							<table border="0" class="table-2 ">
								<tr>
									<%--For displaying Previous link except for the 1st page --%>
									<core:if test="${currentPage != 1}">
										<td><a href="#"
											onclick="return runReport(${currentPage-1})"><strong>Previous</strong></a></td>
									</core:if>

									<core:forEach begin="${currentPageStart}" end="${currentPageEnd}"
										var="i">
										<core:choose>
											<core:when test="${currentPage eq i}">
												<td><strong>${i}</strong></td>
											</core:when>
											<core:otherwise>
												<td><a href="#" onclick="return runReport(${i})">${i}</a></td>
											</core:otherwise>
										</core:choose>
									</core:forEach>
									<%--For displaying Next link --%>

									<core:if test="${currentPage lt noOfPages}">
										<td><a href="#"
											onclick="return runReport(${currentPage+1})"><strong>Next</strong></a></td>
									</core:if>
								</tr>
							</table>
						</core:if>
					</div>
					<!-- // wide_column -->
				</div>

				<!-- // box -->			</div>
			<!-- // box -->
			<!-- convert icons wrapper -->
			<div class="c_icons_wrapper">
				<a href="#" class="lnk_convert_ico ico_xls">XLS</a>
			  <a href="#" class="lnk_convert_ico ico_pdf">PDF</a>
			  <a href="#" class="lnk_convert_ico ico_prn">Print</a>
			</div>
			<!-- // convert icons wrapper -->
	
		</div>
		<!-- // content area --> 
		<!-- sidebar -->
		<div class="sidebar" id="id_sidebar">
    	<div class="inner">
      	<!-- title -->
        <div class="sb_title no-bg">
        	<h2 class="">Select Report</h2>
          <div class="select_wrapper_01">
			<spring:select path="report.id" onchange="return getParams();">
				<spring:option value="">Select a Report</spring:option>
				<spring:options items="${reports}" itemValue="id" itemLabel="name" />
			</spring:select>          
          </div>
        </div>
        <!-- // title -->
        <!-- sidebar box type 1 -->
        <div class="sb_box_01 sb_box_h01">
        	
				<h3>
					<core:out value="${reportForm.report.name}" />
				</h3>
				
          	<div class="form_wrapper_01">
          		<table class="grid grid_02 grid_02_1" width="100%">
           		 <colgroup>
				<col width="48%" />
				<col width="52%" />
            		</colgroup>
			<tbody>

				<tr>
				<td class="td_01"><label for="date_start">Start Date</label></td>
				<td class="td_02"><spring:input path="startDate" class="input_date_01" />
				</td>
			      </tr>
				<tr>
				<td class="td_01"><label for="date_end">End Date</label></td>
				<td class="td_02"><spring:input path="endDate" class="input_date_01" />
				</td>
			      </tr>
			     
				<core:forEach var="rp" items="${reportForm.report.params}" varStatus="loopStatus">
					<tr>
						<td class="td_01"><label><core:out value="${rp.paramLabel}" /></label></td>
						<td class="td_02">
							<spring:input path="report.params[${loopStatus.index}].paramValue" />
						</td>
					</tr>
				</core:forEach>	
            		</tbody>
            </table>
          </div>
          <div class="button_wrapper_02">
          	 <center><a href="#" id="runReport" onclick="runIt()" class="btn_dark_blue btn_report_01_lnk">Run Report</a></center>
          </div>
        </div>
        <!-- // sidebar box type 1 -->
	</spring:form>
   </div>      
    </div>
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