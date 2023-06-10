<%@page import="rto.Test_JDBC"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>북마크 삭제 수행</title>
</head>
<body>





<%
	String id = request.getParameter("id").trim();
	
	int ID = Integer.valueOf(id);
	Test_JDBC delete = new Test_JDBC();
	delete.delete_bookmark(ID); 
	response.sendRedirect("bookmark-list.jsp");
	

%>
</body>
</html>