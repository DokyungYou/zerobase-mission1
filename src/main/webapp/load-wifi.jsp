<%@page import="rto.TestGsonFromAPI"%>
<%@ page import="rto.WifiService" %>  

<%@ page import="java.util.Date" %>
 


<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>와이파이 불러오기</title>




</head>
<body>




<%
	 WifiService wifiService = new WifiService();
	 TestGsonFromAPI openApi = new TestGsonFromAPI();
	 
	 long startTime = System.nanoTime();
	 wifiService.dbInsertAll();
	 long endTime = System.nanoTime();
	 
	 int totalCnt = openApi.get_Total_Cnt();

	 long executionTime = (endTime - startTime)/1000000000 ;
		
%>




  	<h2 style="text-align: center; font-weight: bold;"><%=totalCnt%>개의 WIFI 정보를 정상적으로 저장하였습니다.</h2>
	<div style="text-align: center;">
	<a href ="main_wifi.jsp" class="text-align">홈으로 돌아가기</a>
	
	<br>
	약 <%=executionTime%>초 소요
	</div>	
	
	

</body>
</html>