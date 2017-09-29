<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	request.setAttribute("ctx", basePath);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>My Struts2 Testing JSP 'demo.jsp' starting page</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<SCRIPT type="text/javascript" src="<%=path%>/scripts/jquery-1.8.0.min.js"></SCRIPT>
	<SCRIPT type="text/javascript">
		jQuery(document).ready(function(){
			alert("Demo Start");
			loadSearch();			
		});

		function loadSearch(){
			jQuery.post('<%=path%>/demo/show.action',{
				'teacherVO.id':'4',
				'teacherVO.name':'Linux',
				'teacherVO.address':'Beijing',
				'teacherVO.year':'2013-03-22',
				'ttime':new Date().getMilliseconds()+''
			},function(flag){
		 		alert(flag);
		 	});
		}
	</SCRIPT>
  </head>
  <body>
    This is my Struts2 page.<br/>
    相对路径：<%=path%>
  <br/>
  </body>
</html>
