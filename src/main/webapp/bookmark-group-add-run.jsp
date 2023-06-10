<%@page import="rto.Test_JDBC"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>북마크그룹 추가 수행</title>
</head>
<body>

  <script>
    
    
    function run_add_group() {
      alert("북마크 그룹 정보가 추가되었습니다.");
    }
    
    window.onload = function() {
        run_add_group();
      };
  </script>
  

<%

String name = request.getParameter("name");
String order = request.getParameter("order").trim();


System.out.println("add에서 보낸 아이디: " + name);
System.out.println("add에서 보낸 순서: " + order);




if(name != null && order != null){
		
	Test_JDBC bookmarkGroup = new Test_JDBC();
	bookmarkGroup.insert_BookmarkGroup(name, order);
	response.sendRedirect("bookmark-group.jsp"); 
} 
%>



  
</body>
</html>