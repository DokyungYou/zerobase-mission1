<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>북마크 그룹 삭제</title>

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

<%
//bookmark-group에서 보낸 것 받아오기
String id = request.getParameter("id");
String name= request.getParameter("name"); 
String order = request.getParameter("order"); 
%>



<script>
    function run_delete_group() {
    	var groupID = '<%=id%>';
    	document.location.href ='bookmark-group-delete-run.jsp?delete='+ groupID;
    	alert("북마크 그룹 정보를 삭제하였습니다.");
    }
</script>



</head>

<body>

<h1>위치 히스토리 목록</h1>
<p>
	<a href="main_wifi.jsp">홈</a> | 
	<a href="history.jsp">위치 히스토리 목록</a> |
	<a href="load-wifi.jsp" onclick="return loading()">Open API 와이파이 정보 가져오기</a> | <!-- 자바스크립트 함수로 만들기(자바 메소드 호출, 알림창 생성 -->	
	<a href="bookmark-list.jsp">즐겨 찾기 보기</a> |
	<a href="bookmark-group.jsp">즐겨 찾기 그룹 관리</a> 
	
	
	<!-- 로딩 메시지를 표시할 요소 -->
    <div id="loading-message" style="display: none;">
        와이파이 정보를 가져오는 중입니다...
    </div>
	


북마크 그룹 이름을 삭제하시겠습니까?<br></br>






<%-- ./는 상대경로  --%>
<form action="./bookmark-group-delete.jsp" method="get">
	<table id="wifiTable">
 	<tr>
    	<th class="tg-i9cv">북마크 이름</th>  <%-- input태그의 value에 <%=name%>를 ""로 안 감싸면 공백 이후로는 잘려서 나옴  --%>
    	<td class="tg-0lax">  <input type="text" id="bName" value="<%=name%>" name="deleteName">  </td>
 	</tr>
 
  	<tr>
    	<th class="tg-i9cv">순서</th>
    	<td class="tg-0lax">  <input type="text" id="bName" value="<%=order%>" name="deleteOrder">  </td>
  	</tr>
 
	<tr>
		<td style="text-align: center;" colspan="2"> <a href="bookmark-group.jsp">돌아가기</a> |
		<button type="button"  onclick="run_delete_group()">삭제</button> </td>	
	</tr>

	</table>
</form> 







</body>
</html>