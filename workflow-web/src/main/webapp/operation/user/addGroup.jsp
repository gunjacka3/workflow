<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@ page language="java" pageEncoding="UTF-8"%> 
<%@ page contentType="text/html; charset=utf-8" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
	String  userId = request.getParameter("userId");
	List<HashMap> list = (List<HashMap>) request.getAttribute("list");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/reset.css">
<link rel="stylesheet" type="text/css" href="css/um-page.css">
<title>jQuery EasyUI</title>
</head>
<body>
	<% 
		if(request.getAttribute("msg")!=null){
	%>
		<script >
				alert('<%=request.getAttribute("msg")%>');
		</script>
	<% 
		}
	%>
 	<form action="<%=basePath %>/addGroups.action?userId=<%=userId %>" method="post">
	<input type="hidden" value="" name="id"/>
	<input type="hidden" id="btn" />
	<div class="dialog-box">
		 <span class="d-span"><label>机构名称:</label><select name="groupId" id ="groupId" class="um-select">	
			<%
					if (list != null && list.size() > 0) {
						for(int i=0;i<list.size();i++){
							HashMap u=(HashMap)list.get(i);
			%>
		 	<option value ="<%=u.get("id") %>"><%=u.get("name") %></option>
			<%
					}
				} 
			%>
	 		</select>
	 	</span> 
		<input type="submit" value ="提交" class="um-btn mr15 mt15 mb15 fr"/>
	</div>
	</form>
</body>
</html>
					
				