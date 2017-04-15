<%@page import="com.whitespark.ws.wsProcessJSON"%>
<%@page import="com.business.web.controller.BusinessController"%>
<%@ page import="java.io.*"%>
<%@ page import="java.net.*"%>
<%@ page import="org.w3c.dom.*"%>
<%@ page import="org.apache.log4j.*"%>

<%
	try {

		request.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		
         
        new wsProcessJSON().processJSON(request.getParameter("data"));
		
		%>

<%

	} catch (Exception e) {
		e.printStackTrace();
	}
%>
