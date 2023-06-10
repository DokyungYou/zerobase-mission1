<%@page import="rto.Test_JDBC"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>북마크 그룹 수정 수행</title>



</head>
<body>
  
 <script>
    window.onload = function() {
      run_edit_group();
    };
    
    function run_edit_group() {
      alert("북마크 그룹 정보가 수정되었습니다.");
    }
  </script>

<%

String id = request.getParameter("id");
String name = request.getParameter("name");
String order = request.getParameter("order");

System.out.println("edit에서 보낸 순서: "+ id);
System.out.println("edit에서 보낸 아이디: " + name);
System.out.println("edit에서 보낸 이름: " + order);




if(name != null && order != null && id != null){
	int ID = Integer.valueOf(id.trim());
	
	Test_JDBC update = new Test_JDBC();
		
	update.update_bookmarkGroup(name, order, ID);
	response.sendRedirect("bookmark-group.jsp");
} 
%>
</body>
</html>