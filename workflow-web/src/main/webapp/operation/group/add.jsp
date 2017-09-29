<%@ page language="java" pageEncoding="UTF-8"%> 
<%@ page contentType="text/html; charset=utf-8" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
	String name="";
	String id="";
	if(request.getAttribute("name")!=null){
		name=request.getAttribute("name").toString();
	}
	if(request.getAttribute("id")!=null){
		id=request.getAttribute("id").toString();
	}
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
		String msg=null;
		if(request.getAttribute("msg")!=null && !"".equals(request.getAttribute("msg"))){
			msg=request.getAttribute("msg").toString();
	%>
		<script >
				alert('<%=msg%>');
		</script>
	<% 
		}
	%>
 	<form action="<%=basePath %>/addGroup.action" onsubmit="return isOr()" method="post">
	<input type="hidden" value="" name="id"/>
	<input type="hidden" id="btn" />
	<div class="dialog-box">
		<span class="d-span"><label>机构名称:</label><input type="text" value="<%=name %>" id="name" name="group.groupName" class="um-input"/></span>
		<span class="d-span"><label>上级机构:</label><input type="text" value="<%=id %>" id="groupId" name="group.groupId" class="um-input"/></span>
		
		<input type="submit" value ="提交" class="um-btn mr15 mt15 mb15 fr"/>
		<input type="reset" value = "重置" class="um-btn mr15 mt15 mb15 fr" /></th>
	</div>
	</form>
</body>
<script type="text/javascript">
	function isOr(){
		var n= document.getElementById("name").value;
		var g= document.getElementById("groupId").value;
		
		if(n!=null && n!=""){
			return true;
		}else{
			alert("请输入机构名称");
			return false;
		}
		//window.location.href="<%=basePath%>/addGroup.action?group.groupId="+g+"&group.groupName="+n+"";	
	}
	 	
</script>
</html>
					
				