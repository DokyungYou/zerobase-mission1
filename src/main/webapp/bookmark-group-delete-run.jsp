<%@page import="rto.Test_JDBC"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>북마크 그룹 삭제 수행</title>




<% String group_ID = request.getParameter("delete"); 
System.out.println(group_ID);
if(group_ID != null){
	int id = Integer.valueOf(group_ID.trim());
	Test_JDBC delete = new Test_JDBC();
		
	delete.delete_bookmarkGroup(id);
	response.sendRedirect("bookmark-group.jsp");	
} 
%>



</head>
<body>




</body>
</html>