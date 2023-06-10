<%@ page import="java.util.List" %>   
<%@ page import="rto.Test_JDBC" %>   
<%@ page import="dto.HistoryObject" %>  
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>히스토리 기록</title>
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



<script>
//a태그가 아닌 버튼태그는 아마 직접 변수로 못 보내니까
//현재 url뒤에 붙여서 새롭게 창 자체를 이동해서 받는건가?
function sendValue(value) {
  var currentUrl = window.location.href; //이것 자체가 현재 창을 뜻하는듯하다.
  var newUrl = currentUrl + '?parameter=' + encodeURIComponent(value);
  window.location.href = newUrl;
}
</script>


</head>

<body>
<h1>위치 히스토리 목록</h1>
<p>
	<a href="main_wifi.jsp">홈</a> | 
	<a href="history.jsp">위치 히스토리 목록</a> |
	<a href="load-wifi.jsp" onclick="return loading()">Open API 와이파이 정보 가져오기</a>  <!-- 자바스크립트 함수로 만들기(자바 메소드 호출, 알림창 생성 -->	
</p>	
	



<table id="wifiTable">

<thead>
	<tr bgcolor="#34C3DF">
		<th style="text-align: center;">ID</th>
		<th style="text-align: center;">X좌표</th>
		<th style="text-align: center;">Y좌표</th>
		<th style="text-align: center;">조회일자</th>
		<th style="text-align: center;">비고</th>
	</tr> 
</thead> 

<%
Test_JDBC history = new Test_JDBC();
int totalCnt = history.select_tableDataCount("history");
if(totalCnt > 0){
	List<HistoryObject> historyList = history.selectAll_history();
	
	for(HistoryObject historyObject : historyList ){
		
	//버튼으로 아이디 값을 현재 창으로 보내게 하고, 그 값을 받을 변수 만들고, 변수가 null아닌 조건에서 메소드 호출하게끔 만들자	
%>
				

	<tr>
	<td><%=historyObject.getID()%></td>
	<td><%=historyObject.getX()%></td>
	<td><%=historyObject.getY()%></td>
	<td><%=historyObject.getInquiryDate() %></td>
	<td  style="text-align: center;">
	<button onclick="sendValue(<%=historyObject.getID()%>)">삭제</button>
	</td>

	</tr>
	
	
	

	
<%}}else{%>
	
<tbody>

	<tr>
		<td colspan="6" style="text-align: center;">정보가 존재하지 않습니다.</td>
	</tr>
	
<%}%>
</tbody>
	

</table>

	
<%
String value = request.getParameter("parameter");

if(value != null){
	int id = Integer.valueOf(value);
	Test_JDBC historyD = new Test_JDBC();
	historyD.delete_history(id);
	response.sendRedirect(request.getRequestURI());
}

%>





</body>
</html>