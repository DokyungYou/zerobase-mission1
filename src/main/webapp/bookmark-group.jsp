<%@page import="java.util.List" %>  
<%@page import="rto.BookmarkService"%>
<%@page import="dto.BookmarkGroup_Object"%>


<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>북마크 그룹</title>

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
function goToAdd_Bookmark.jsp() {
    document.location.href = 'bookmark-group-add.jsp';
}
</script>






</head>
<body>

<h1>북마크 그룹</h1>
<p>
	<a href="main_wifi.jsp">홈</a> | 
	<a href="history.jsp">위치 히스토리 목록</a> |
	<a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a> | 	
	<a href="bookmark-list.jsp">북마크 보기</a> |
	<a href="bookmark-group.jsp">북마크 그룹 관리</a> 
</p>

<button onclick="location.href='bookmark-group-add.jsp'">북마크 그룹 이름 추가</button>

<br>





<table id="wifiTable">
<thead>
	<tr bgcolor="#34C3DF">
		<th style="text-align: center;">ID</th>
		<th style="text-align: center;">북마크 이름</th>
		<th style="text-align: center;">순서</th>
		<th style="text-align: center;">등록일자</th>
		<th style="text-align: center;">수정일자</th>
		<th style="text-align: center;">비고</th>
	</tr> 

</thead> 

<%
BookmarkService bookmarkService = new BookmarkService();
int totalCnt =bookmarkService.select_tableDataCount("bookmark_group");
if(totalCnt > 0){
	List<BookmarkGroup_Object> bookmarkList = bookmarkService.selectAll_BookmarkGroup();
	
	for(BookmarkGroup_Object bookmarkObject : bookmarkList ){
	String bookmarkName = bookmarkObject.getBookmarkName();
	%>
				

	<tr>
	<td><%=bookmarkObject.getGroup_id()%></td>
	<td><%=bookmarkObject.getBookmarkName()%></td>
	<td><%=bookmarkObject.getOrder()%></td>
	<td><%=bookmarkObject.getRegister_dttm() %></td>
	<td><%=bookmarkObject.getUpdate_dttm()%></td>
	<td style="text-align: center;">
	
	<!--북마크그룹이름, 순서만 보낼 수도 있으나, 에러가 덜 뜰 것같은 숫자(id)도 보내고 그걸로 수정, 삭제 처리하기로함  -->
	<a href="bookmark-group-edit.jsp?id=<%=bookmarkObject.getGroup_id()%>
	 &name=<%=bookmarkObject.getBookmarkName()%>
	 &order=<%=bookmarkObject.getOrder()%>">수정</a>
	 
	<a href="bookmark-group-delete.jsp?id=<%=bookmarkObject.getGroup_id()%>
	 &name=<%=bookmarkObject.getBookmarkName()%>
	 &order=<%=bookmarkObject.getOrder()%>">삭제</a>
	</td>
	</tr>
	
	
	

	
<%}}else{%>
	

	<tr>
		<td style="text-align: center;" colspan="6">정보가 존재하지 않습니다.</td>
	</tr>
	
<%}%>

	

</table>

</body>
</html>