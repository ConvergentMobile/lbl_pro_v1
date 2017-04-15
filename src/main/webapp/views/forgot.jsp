<%@ taglib uri="http://www.springframework.org/tags/form"	prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="tag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core"%>
<html>
<head>
	<meta charset="utf-8">
	<meta content="width=device-width,initial-scale=1.0" name="viewport">
	<title>Liberty Tax Service</title>
	
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
	  <a class="logo_lbl" href="dash-board.htm">Local Business Listings</a>
	</header>
<spring:form action="forgotPass.htm" method="post" commandName="loginCommand" >
	<div id="body" class="" >
		<div class="inner" style="height:380px;">
		<div class="content">
			<div class="inner">
				<div class="login-form" >
				
				<span>${message}</span>
					
					<spring:input align="center" class="login"  path="userName" value="Username" onfocus="if (this.value=='Username'){this.value=''}" onblur="if (this.value==''){this.value='Username'}" />
					
					
					
					<input type="submit" align="center" value="Send"><br>
					
					
					
					<!-- <a href="#" class="forgot">Forgot password?</a> -->
				</div>					
			</div>
		</div>
		
		<div class="sidebar">
			<div class="inner">
		
									
			</div>
		</div>
		
		<div class="clearer"></div>
		</div>
	</div>
	</spring:form>

	
</div>
</body>
</html>
