<%@ page import="java.util.List" %>     
<%@page import="rto.BookmarkService"%>
<%@ page import="dto.Bookmark" %> 

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>북마크</title>

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

<h1>북마크</h1>
<p>
	<a href="main_wifi.jsp">홈</a> | 
	<a href="history.jsp">위치 히스토리 목록</a> |
	<a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a> |
	<a href="bookmark-list.jsp">북마크 보기</a> |
	<a href="bookmark-group.jsp">북마크 그룹 관리</a> 
	
</p>

<table id="wifiTable">
<thead>
	<tr bgcolor="#34C3DF">
		<th style="text-align: center;">ID</th>
		<th style="text-align: center;">북마크 이름</th>
		<th style="text-align: center;">와이파이명</th>
		<th style="text-align: center;">등록일자</th>
		<th style="text-align: center;">비고</th>
	</tr> 
</thead> 

<%

BookmarkService bookmark = new BookmarkService();
int totalCnt = bookmark.select_tableDataCount("bookmark_list");

if(totalCnt > 0){
	List<Bookmark> bookmarkList = bookmark.selectAll_BookmarkList();
	for(Bookmark bookMark :bookmarkList){ 
			System.out.println("와이파이번호: " + bookMark.getWifiNum()+"/확인용출력문");
	
	%>
		

	<tr>
	<td><%=bookMark.getBookmarkID()%></td>
	<td><%=bookMark.getBookmarkName()%></td>
	<td><a href="detail_wifi.jsp?mgrNo=<%=bookMark.getWifiNum()%>&distance=<%=bookMark.getDistance()%>"><%=bookMark.getWifiName()%></a></td> 	
	<td><%=bookMark.getRegisterDTTM() %></td>
	
	<td style="text-align: center;" ><a href="bookmark-delete.jsp?id_B=<%=bookMark.getBookmarkID()%>">삭제</a></td>
	</tr>

	
	 
	
	



<%}}else{%>


	<tr>
	<td  style="text-align: center;" colspan="5"> 정보가 존재하지 않습니다. </td>
	</tr>
<%}%>


</table>

</body>
</html>