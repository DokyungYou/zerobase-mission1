<%@page import="java.awt.print.Book"%>
<%@page import="rto.Test_JDBC"%>
<%@page import="dto.Bookmark"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>북마크 삭제</title>



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
//나중에 아이디만 받는걸로 수정하자
String bookmarkID = request.getParameter("id_B"); 

System.out.println("받아온" + bookmarkID);


Test_JDBC delete = new Test_JDBC();

int id = Integer.valueOf(bookmarkID.trim());
Bookmark deleteObject = delete.select_bookmarkDelete(id);

System.out.println("바꾼" + id);
%>




<script>


    function delete_Bookmark(){

		var ID = "<%=id%>";
    	alert("삭제되었습니다.");
    	document.location.href ="bookmark-delete-run.jsp?id=" + ID; //숫자니까 인코딩은 굳이 안해도 됨
    	
    }
</script>




</head>
<body>

<h1>북마크 삭제</h1>
<p>
	<a href="main_wifi.jsp">홈</a> | 
	<a href="history.jsp">위치 히스토리 목록</a> |
	<a href="">Open API 와이파이 정보 가져오기</a> | 
	<a href="bookmark-list.jsp">즐겨 찾기 보기</a> |
	<a href="bookmark-group.jsp">즐겨 찾기 그룹 관리</a> 	
</p>







북마크를 삭제하시겠습니까?
<br></br>

<table id="wifiTable">

 <tr>
    <th style="text-align: center;">북마크 이름</th>
    <td class="tg-0lax"><%=deleteObject.getBookmarkName()%></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">와이파이명</th>
    <td class="tg-0lax"><a href="detail_wifi.jsp?mgrNo=<%=deleteObject.getWifiNum()%>&distance=<%=deleteObject.getDistance()%>" ><%=deleteObject.getWifiName()%></a></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">등록일자</th>
    <td class="tg-0lax"><%=deleteObject.getRegisterDTTM()%></td>
  </tr>
  
   <tr>
		<td  style="text-align: center;" colspan="2">
		<a href="bookmark-list.jsp">돌아가기</a>|
		<button type="button" onclick="delete_Bookmark()">삭제</button>
		</td>
	</tr>

</table>




</body>
</html>