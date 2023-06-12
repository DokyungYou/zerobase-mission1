<%@page import="dto.NearWifiObject"%>
<%@page import="dto.BookmarkGroup_Object"%>

<%@page import="rto.WifiService"%>
<%@page import="rto.BookmarkService"%>



<%@ page import="java.util.List" %>  
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>와이파이 상세정보</title>

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
//관리번호 받아오기 
String X_SWIFI_MGR_NO = request.getParameter("mgrNo");  //이거 북마크 만들때 같이 매개변수로 넣기

//거리
String distance = request.getParameter("distance"); //이거 북마크 만들때 같이 매개변수로 넣기


WifiService wifiService = new WifiService();

NearWifiObject wifiDetail = wifiService.select_Wifi_Detail(X_SWIFI_MGR_NO);

%>




<script type="text/javascript">

var X_SWIFI_MGR_NO = '<%= X_SWIFI_MGR_NO %>';
var distance = '<%= distance %>';


function sendSelectValue(){
	var selectedValue = document.getElementById("bookmarkGroup").value;
   
	
	window.location.href =
    	"detail_wifi.jsp?selectedValue=" + encodeURIComponent(selectedValue)
    	+ "&wifi_no=" + encodeURIComponent(X_SWIFI_MGR_NO)+
    	"&wifi_distance=" + encodeURIComponent(distance);
	
		alert("북마크 정보를 추가하였습니다.");
}


</script>


</head>
<body>

<h1>와이파이 정보 구하기</h1>
<p>
	<a href="main_wifi.jsp">홈</a> | 
	<a href="history.jsp">위치 히스토리 목록</a> |
	<a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a> | 
	<a href="bookmark-list.jsp">북마크 보기</a> |
	<a href="bookmark-group.jsp">북마크 그룹 관리</a> 	
</p>




<%
BookmarkService bookmarkService = new BookmarkService();
List<BookmarkGroup_Object> groupList = bookmarkService.select_bookmark_NameID();
%>

<p>

<select id="bookmarkGroup">
<option disabled selected>북마크 그룹 이름 선택</option>

<%for(BookmarkGroup_Object bGroup : groupList){ %>
    <option value="<%=bGroup.getGroup_id()%>"> <%=bGroup.getBookmarkName()%></option>
    <%}%>
</select>
<button onclick="sendSelectValue()">북마크 추가하기</button>








<table id="wifiTable">

	 <tr>
    <th style="text-align: center;">거리(Km)</th>
    <td class="tg-0lax"><%=distance%></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">관리번호</th>
    <td><%=wifiDetail.getX_SWIFI_MGR_NO()%></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">자치구</th>
    <td><%=wifiDetail.getX_SWIFI_WRDOFC()%></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">와이파이명</th>
    <td><%=wifiDetail.getX_SWIFI_MAIN_NM()%></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">도로명주소</th>
    <td><%=wifiDetail.getX_SWIFI_ADRES1()%></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">상세주소</th>
    <td><%=wifiDetail.getX_SWIFI_ADRES2()%></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">설치위치(층)</th>
    <td><%=wifiDetail.getX_SWIFI_INSTL_FLOOR()%></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">설치유형</th>
    <td><%=wifiDetail.getX_SWIFI_INSTL_TY()%></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">설치기관</th>
    <td><%=wifiDetail.getX_SWIFI_INSTL_MBY()%></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">서비스구분</th>
    <td><%=wifiDetail.getX_SWIFI_SVC_SE()%></td>
  </tr>
   <tr>
    <th style="text-align: center;">망종류</th>
    <td><%=wifiDetail.getX_SWIFI_CMCWR()%></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">설치년도</th>
    <td><%=wifiDetail.getX_SWIFI_CNSTC_YEAR()%></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">실내외구분</th>
    <td><%=wifiDetail.getX_SWIFI_INOUT_DOOR()%></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">WIFI접속환경</th>
    <td><%=wifiDetail.getX_SWIFI_REMARS3()%></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">X좌표</th>
    <td><%=wifiDetail.getLAT()%></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">Y좌표</th>
    <td><%=wifiDetail.getLNT()%></td>
  </tr>
  
   <tr>
    <th style="text-align: center;">작업일자</th>
    <td><%=wifiDetail.getWORK_DTTM()%></td>
  </tr>
	

<tbody>


</tbody>
	
	
</table>



<% 
String id = request.getParameter("selectedValue"); 
String wifi_no = request.getParameter("wifi_no");
String wifi_distance = request.getParameter("wifi_distance");

if(id != null && wifi_no!= null && wifi_distance!=null){
	
	//select문의 것을 보내면서 새로운 창으로 온 거나 마찬가지여서 다른 창에서보낸, 이름과,번호가 null인 상태

//조건문에 따라 달리하고싶은데 의도한대로 안된다.
	if(id.trim().length()==0){
//		out.print("<script>alert('북마크그룹을 선택해주세요!');" + "history.back();"+"</script>");		

	}else{
		int ID = Integer.valueOf(id);

		bookmarkService.insertBookmarkList(ID, wifi_no, wifi_distance);
		
		response.sendRedirect("bookmark-list.jsp");
	}
}	


%>




</body>
</html>