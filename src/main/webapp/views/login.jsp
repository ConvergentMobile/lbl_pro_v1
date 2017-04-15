<%@ taglib uri="http://www.springframework.org/tags/form"	prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<html>
<head>
	<meta charset="utf-8">
	<meta content="width=device-width,initial-scale=1.0" name="viewport">
	<meta http-equiv="Pragma" content="no-cache"> 
 <meta http-equiv="Cache-Control"      content="no-cache"> 
<meta http-equiv="Expires" content="Sat, 01 Dec 2012 00:00:00 GMT">
	<title>Local Business Listings</title>
	
  <link rel="stylesheet" type="text/css" href="css/reset.css">
	<link rel="stylesheet" href="fonts/fonts.css">
  <link rel="stylesheet" type="text/css" href="css/login.css">

	<script src="js/jquery-1.9.1.min.js"></script>
	<style type="text/css">

	.error{
color:red;
font-size: 25px;

}
	
	</style>
	
	
</head>
<body>
<div id="page" class="login">

	<header id="header">
	  <a class="logo_lbl" href="#">Local Business Listings</a>
	</header>
<spring:form action="login.htm" method="post" commandName="loginCommand" >
	<div id="body" class="">
		<div class="inner">
		<div class="content">
			<div class="inner">
				<div class="login-form">
				
				<core:if test="${not empty invalidUser}">
  						<div  class="error" align="center">
  					<tag:message  code="businessApp.lable.badCredentials" ></tag:message>
					</div>
					 </core:if>
				<span style="color: green;">${message}</span>
					<h2>Customer Login</h2>
					
					<spring:input class="login"  path="userName" value="Username" onfocus="if (this.value=='Username'){this.value=''}" onblur="if (this.value==''){this.value='Username'}" />
					
					<spring:password class="pass"  path="passWord" value="Password" onfocus="if (this.value=='Password'){this.value=''}" onblur="if (this.value==''){this.value='Password'}" />
					
					<input type="submit" value="Login"><br>
					
					<input type="checkbox"> Remember me
					
					<a href="forgot.htm" class="forgot">Forgot password?</a>
				</div>					
			</div>
		</div>
		
		<div class="sidebar">
			<div class="inner">
				
				<div class="block-3 know">
					<img src="images/icon-question.png">
					<div class="header">Did you know &hellip; </div>
					<div class="body"> &hellip; 4 out of 5 smartphone users search specifically for contact info (phone numbers, maps, and driving directions)</div>
				</div>
				
				<div class="block-3 forget">
					<img src="images/icon-remember.png">
					<div class="header">Don't forget &hellip; </div>
					<div class="body">&hellip; it is important to use a local phone number for your locations. 73% of mobile users prefer a local number vs a toll-free number.</div>
				</div>
									
			</div>
		</div>
		
		<div class="clearer"></div>
		</div>
	</div>
	</spring:form>

	<footer id="footer">
		<div class="inner">
			<h2>WELCOME TO LBL Pro</h2>
			<p>
      	This account login screen is the first step for you to take control of your online business listings. The local search ecosystem is chaotic and ever-changing. LBL Pro is  simple, flexible and effective.
      </p>
			<p>
      	Now, you have a single point of contact to upload, manage, review and verify your online business listing information and content, enabling consistent distribution of your data to the top data providers, search engines and online directories.
      </p>
		
			<a href="#" class="powered">Powered by</a>
		</div>
	</footer>
	
</div>
</body>
</html>
