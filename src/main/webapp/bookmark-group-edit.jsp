<%@page import="okio.Utf8"%>
<%@page import="javax.swing.text.Document"%>
<%@page import="rto.Test_JDBC"%>
<%@ page import="java.net.URLEncoder" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>북마크 그룹 수정</title>

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

System.out.println("------확인용 출력문-----");
System.out.println("group에서 바로 보낸 id: " + id);
System.out.println("group에서 바로 보낸 name: "+ name);
System.out.println("group에서 바로 보낸 order: "+ order);
%>



</head>
<body>



<h1>북마크 수정 </h1>
<p>
	<a href="main_wifi.jsp">홈</a> | 
	<a href="">위치 히스토리 목록</a> |
	<a href="load-wifi.jsp" onclick="return loading()">Open API 와이파이 정보 가져오기</a> | <!-- 자바스크립트 함수로 만들기(자바 메소드 호출, 알림창 생성 -->	
	<a href="bookmark-list.jsp">즐겨 찾기 보기</a> |
	<a href="bookmark-group.jsp">즐겨 찾기 그룹 관리</a> 
</p>

<P>북마크 그룹 이름을 수정하시겠습니까?</P>

<form action="./bookmark-group-edit.jsp" method="get">
<input type="hidden" value="<%=id%>" name="updateId">
<table id="wifiTable">


<!-- input태그의 value값은 ""로 감싸야한다. 안감쌌더니, 공백만나는 기준 뒤의 문자는 전달이 안됐었음 -->
 <tr>
    <th style="text-align: center;">북마크 이름</th>
							
    <td>  <input type="text" value="<%=name%>" name="updateName">  </td>
 </tr>
  
  <tr>
    <th style="text-align: center;">순서</th>
    <td>  <input type="text" value="<%=order%>" name="updateOrder"> </td>
  </tr> 
	<tr>
	<td style="text-align: center;" colspan="2">
	 <a href="bookmark-group.jsp">돌아가기</a> |
	 <button type="submit" onclick="alert('북마크 그룹 정보를 수정하였습니다.'); this.form.submit()">수정</button> </td>	
	</tr>

</table>
</form>




<%
//수정버튼 클릭 시 input태그의 값 전송해서 받고
String update_Id = request.getParameter("updateId");
String update_name = request.getParameter("updateName");
String update_order = request.getParameter("updateOrder");


System.out.println("input태그에서 보낸 아이디: " + update_Id);
System.out.println("input태그에서 보낸 이름: " + update_name);
System.out.println("input태그에서 보낸 순서: "+ update_order);

//id도 같이 보내기
//문제점 드디어 찾았다.
//null이 안되게 겨우 해결 -> 인코딩 문제 발생 -> 인코딩으로 감쌋더니 가용하지않음-> 한꺼번에 인코딩하지말고 따로 인코딩한 뒤 합쳤다. -> 해결
if(update_Id != null && update_name != null && update_order != null){ 
	
	String encodingID = URLEncoder.encode(update_Id,"UTF-8");
	String encodingName = URLEncoder.encode(update_name,"UTF-8");
	String encodingOrder = URLEncoder.encode(update_order,"UTF-8");
	
	
	
	String destination = "bookmark-group-edit-run.jsp?";
	String url = destination + "id=" + encodingID + "&name=" + encodingName + "&order=" + encodingOrder;
	
	response.sendRedirect(url);
}

%>



</body>
</html>