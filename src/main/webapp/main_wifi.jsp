<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.util.List" %>     
<%@ page import="rto.Test_JDBC" %>    
<%@ page import="dto.NearWifiObject" %>   


<%//객체는 하나만 생성하고 재활용하게끔
 Test_JDBC dbConn = new Test_JDBC();
%>


    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인화면</title>

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
	
<!-- 자바스크립트는 script태그 안에, 해당태그는 head태드 안에 -->	
	<script type="text/javascript">
    function showPosition() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                var latitude = position.coords.latitude;
                var longitude = position.coords.longitude;
                
                document.getElementById("LAT").value = latitude;
                document.getElementById("LNT").value = longitude;
                
                document.getElementById("myForm").submit();
            });
        } else {
            alert("해당 브라우저에서 지원하지 않습니다.");
        }
    }
    
    
    <!-- 뭐지 이거-->
//    function sendNumber(wifiNumber) {
//    var url = 'detail_wifi.jsp?wifiNumber=' + wifiNumber;
//    location.href = url;          
//    }
    
 
    //와이파이 불러오기 재실행 방지
        var isLoading = false;

        function loading() {
            if (isLoading) {
                return false;
            }

            isLoading = true;
            showLoadingMessage();

            var url = 'load-wifi.jsp';
            var xhr = new XMLHttpRequest();
            xhr.open('GET', url, true);
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    hideLoadingMessage();
                    showNotification('작업이 완료되었습니다.');
                    isLoading = false;
                }
            };
            xhr.send();

            return false;
        }


 
</script>

</head>



<body>


		
<h1>와이파이 정보 구하기</h1>
<br>
	<a href="main_wifi.jsp">홈</a> | 
	<a href="history.jsp">위치 히스토리 목록</a> |
	<a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a> |
	<a href="bookmark-list.jsp">북마크 보기</a> |
	<a href="bookmark-group.jsp">북마크 그룹 관리</a>  
	
	


<p>
<form action="./main_wifi.jsp" method="get">

	<label for="LAT">LAT:</label>
	<input type="text" id="LAT" value="0.0" name="lat">

	<label for="LNT">, LNT:</label>
	<input type="text" id="LNT" value="0.0" name="lnt">
	<button type="button" onclick="showPosition()">내 위치 가져오기</button>


<!-- 이 버튼 누를 때, 해당 좌표 기준으로 히스토리 테이블에 데이터 생성-->	
	<button type="submit" name="nearWifi_info" onclick="this.form.submit()">근처 WIPI 정보 보기</button>  
</form>



<br>

<table id="wifiTable">
<thead>
	<tr>
		<th style="text-align: center;">거리(Km)</th>
		<th style="text-align: center;">관리번호</th>
		<th style="text-align: center;">자치구</th>
		<th style="text-align: center;">와이파이명</th>
		<th style="text-align: center;">도로명주소</th>
		<th style="text-align: center;">상세주소</th>
		<th style="text-align: center;">설치위치(층)</th>
		<th style="text-align: center;">설치유형</th>
		<th style="text-align: center;">설치기관</th>
		<th style="text-align: center;">서비스구분</th>
		<th style="text-align: center;">망종류</th>
		<th style="text-align: center;">설치년도</th>
		<th style="text-align: center;">실내외구분</th>
		<th style="text-align: center;">WIFI접속환경</th>
		<th style="text-align: center;">X좌표</th>
		<th style="text-align: center;">Y좌표</th>
		<th style="text-align: center;">작업일자</th>
	</tr> 
</thead> 	


<%
String latV = request.getParameter("lat");
String lntV = request.getParameter("lnt");

if(latV != null && lntV != null){ 
	double lat = Double.valueOf(latV);
	double lnt = Double.valueOf(lntV);
	
    //히스토리 생성하는 메소드 호출
 	dbConn.insert_history(lat,lnt);

    //20개 뽑아오기
	List<NearWifiObject> wifiList = dbConn.selectNearWifi20(lat, lnt);

%>



<% //조건문 충족했을때만 나오게
for(NearWifiObject wifi : wifiList){%>
<!-- tbody<> 지우니까 교차 색 먹히는거 먹힌다. -->
	<tr>
        <td><%=wifi.getDistance() %></td>
        <td><%=wifi.getX_SWIFI_MGR_NO() %></td>
        <td><%=wifi.getX_SWIFI_WRDOFC() %></td>
        <td><a href="detail_wifi.jsp?mgrNo=<%=wifi.getX_SWIFI_MGR_NO()%>&distance=<%=wifi.getDistance() %>" ><%=wifi.getX_SWIFI_MAIN_NM()%></a></td>   
        <td><%=wifi.getX_SWIFI_ADRES1() %></td>
        <td><%=wifi.getX_SWIFI_ADRES2() %></td>
        <td><%=wifi.getX_SWIFI_INSTL_FLOOR() %></td>
        <td><%=wifi.getX_SWIFI_INSTL_TY() %></td>
        <td><%=wifi.getX_SWIFI_INSTL_MBY() %></td>
        <td><%=wifi.getX_SWIFI_SVC_SE() %></td>
        <td><%=wifi.getX_SWIFI_CMCWR() %></td>
        <td><%=wifi.getX_SWIFI_CNSTC_YEAR() %></td>
        <td><%=wifi.getX_SWIFI_INOUT_DOOR() %></td>
        <td><%=wifi.getX_SWIFI_REMARS3() %></td>
        <td><%=wifi.getLAT() %></td>
        <td><%=wifi.getLNT() %></td>
        <td><%=wifi.getWORK_DTTM() %></td>
	</tr>


	
	<%}}else{%>
	


<tr>
<td style="text-align: center;" colspan="17">위치 정보를 입력한 후에 조회해 주세요.</td>
</tr>	



</tbody>	
	
	
	<%}%>
</table>




</body>
</html>
