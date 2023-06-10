<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<form action="./formTest1.jsp">
		
		ID :
		<input type="text" name="id">
		
		<button type="submit">값 보내기</button>
	
	</form>


<%
	String id = request.getParameter("id");
%>

	<div><%=id %></div>



</body>
</html>