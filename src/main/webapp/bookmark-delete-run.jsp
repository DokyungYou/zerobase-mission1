<%@page import="rto.BookmarkService"%>
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
	BookmarkService bookmarkService = new BookmarkService();
	bookmarkService.delete_bookmark(ID); 
	response.sendRedirect("bookmark-list.jsp");
	

%>
</body>
</html>