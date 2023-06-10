<%@page import="rto.Test_JDBC"%>
<%@ page import="java.net.URLEncoder" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>북마크 그룹 추가</title>

<style>
#wifiTable {
  font-family: Arial, Helvetica, sans-serif;
  border-collapse: collapse;
  width: 100%;
}

#wifiTable td, #wifiTable th {
  border: 1px solid #ddd;
  padding: 8px;
}

#wifiTable tr:nth-child(even){background-color: #f2f2f2;}

#wifiTable tr:hover {background-color: #ddd;}

#wifiTable th {
  padding-top: 12px;
  padding-bottom: 12px;
  text-align: left;
  background-color: #04AA6D;
  color: white;
}
</style>




</head>
<body>

<h1>와이파이 정보 구하기</h1>

<p>
	<a href="main_wifi.jsp">홈</a> | 
	<a href="history.jsp">위치 히스토리 목록</a> |
	<a href="">Open API 와이파이 정보 가져오기</a> | <!-- 불러와서 db에 넣는 동작 -->	
	<a href="bookmark-list.jsp">즐겨 찾기 보기</a> |
	<a href="bookmark-group.jsp">즐겨 찾기 그룹 관리</a> 
</p>
<br>

<form action ="./bookmark-group-add.jsp" method="get">
	<table id="wifiTable">
   	<tr>
    	<th style="text-align: center;">북마크 이름</th>
    	<td><input type="text" name="bookmarkName"></td>
  	</tr>
  
   	<tr>
    	<th style="text-align: center;">순서</th>
    	<td ><input type="text" name="order"></td>
  	</tr>
  	<tr>
		<td colspan="2"  style="text-align: center;"> <button type="submit">추가</button> </td>
	</tr>

	</table>
</form>

<%
String bookmarkName = request.getParameter("bookmarkName");
String order = request.getParameter("order");

if(bookmarkName != null && order != null){
	
	String encodingName = URLEncoder.encode(bookmarkName,"UTF-8");
	String encodingOrder = URLEncoder.encode(order,"UTF-8");

	String destination = "bookmark-group-add-run.jsp?";
	String url = destination + "name="+ encodingName +"&order="+encodingOrder;
	
	response.sendRedirect(url);
}%>





</body>
</html>