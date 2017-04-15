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
	
	
	<script type="text/javascript">
	$(document).ready(function() {
		 $('#savePassConfirmation').click(function(event){
	    	 data = $('.passWord').val();
	         var len = data.length;
	    
	       		if(len < 1) {
	                 alert("Password cannot be blank");
	                 // Prevent form submission
	                 event.preventDefault();
	             }else if($('.passWord').val() != $('.confirmPassWord').val()) {
	                 alert("Enter Confirm Password Same as Password");
	                 // Prevent form submission
	                 event.preventDefault();
	             }
	       		});
	});
	
	</script>
	
	
</head>
<body>
<div id="page" class="login">

	<header id="header">
	  <a class="logo_lbl" href="#">Local Business Listings</a>
	</header>
<spring:form action="resetPassword.htm" method="post" commandName="loginCommand" >
	<div id="body" class="" >
		<div class="inner" style="height:380px;">
		<div class="content">
			<div class="inner">
				<div class="login-form" >					
					
					<spring:input align="center" class="passWord"  path="passWord" value="Password" onfocus="if (this.value=='Password'){this.value=''}" onblur="if (this.value==''){this.value='Password'}"/>
					<spring:input align="center" class="confirmPassWord"  path="confirmPassWord" value="confirmPassWord" onfocus="if (this.value=='confirmPassWord'){this.value=''}" onblur="if (this.value==''){this.value='confirmPassWord'}"/>					
					<spring:hidden path="email"/>
					<input type="submit" align="center" id="savePassConfirmation" value="Send"><br>
					
					
					
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
