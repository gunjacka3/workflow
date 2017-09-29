<%@page import="cn.com.workflow.user.UserServiceImpl"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="org.activiti.engine.identity.Group" %>
<%@page import="cn.com.workflow.common.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%	
	String  userId = request.getParameter("userId");
	List<Group> list = (List<Group>) request.getAttribute("list");	
	
	String pageMap="";
	String pageno ="";
	String familyName="";
	String givenName ="";
	if(request.getAttribute("pageno")!=null){
		pageno=request.getAttribute("pageno").toString();
	}
	if(request.getAttribute("pageMap")!=null){
		pageMap=request.getAttribute("pageMap").toString();
	}
	if(request.getAttribute("familyName")!=null){
		familyName=request.getAttribute("familyName").toString();
	}
	if(request.getAttribute("givenName")!=null){
		givenName=request.getAttribute("givenName").toString();
	}
	String userPageSize="";
	if(request.getAttribute("userPageSize")!=null){
		userPageSize=request.getAttribute("userPageSize").toString();
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<base href="<%=basePath%>">
<title>My JSP 'list.jsp' starting page</title>	    
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="css/reset.css">
<link rel="stylesheet" type="text/css" href="css/um-page.css">
<link rel="stylesheet" href="<%=basePath %>/css/default.css" type="text/css" />
<script src="<%=basePath %>/js/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath %>/js/lhgdialog.js" type="text/javascript"></script>
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
 	<div class="um-s-box">
 	 	<form action="<%=basePath%>/userList.action"> 	 		
			<span class="um-span"></span>
			<span class="um-span"></span>			
			<span class="um-span"></label>
				
		 	</span>
		</form>
 	</div>
 	<div class="um-span-left">
		<input onclick="location.href='userList.action?&pageno=<%=pageno%>&pageMap=<%=pageMap%>&familyName=<%=familyName%>&givenName=<%=givenName%>&pageSize=<%=userPageSize %>'" type="button" value="返回" class="um-btn" />
	</div>
 	<div class="um-span-right">
 		<input id="addWindow" type="button" value="添加新组" class="um-btn" />
		<!-- <input onclick="location.href='userList.action'" type="button" value="列表首页" class="um-btn" /> -->
	</div>

	<div class="table-wrapper">
			<table id="um-table-reload" class="um-table">
				<thead>
					<tr>
						<th>序号</th>
						<th>用户名</th>
						<th>机构名称</th>
						<th>操作</th>									
					</tr>					
				</thead>
				<tbody>
					<%
					if (list != null && list.size() > 0) {
						for(int i=0;i<list.size();i++){
							Group u=(Group)list.get(i);
				%>
					<tr>
						<td><%=i+1 %></td>
						<td><%=userId %></td>
						<td><%=u.getName() %></td>
						<td><a href="JavaScript:deleteGroup('<%=u.getId() %>')">退出该组</td> 
					</tr>
					<%
							}
							
						}else{
					%>
					<div class="um-span-center" style="color:#F00;font-size:20px">※    没有找到相关信息</div>  
					<%
						}
					%>
				</tbody>
			</table>
		</div>
<script type="text/javascript">
	function deleteGroup(id){  
		if(confirm("确定要退出该组吗？")){
			window.location.href='quitGroups.action?groupsId='+id+'&userId=<%=userId %>&pageno=<%=pageno%>&pageMap=<%=pageMap%>&familyName=<%=familyName%>&givenName=<%=givenName%>&userPageSize=<%=userPageSize %>';
		}else{
			window.location.reload();
		}
	} 
</script>    	  
<script>
$(document).ready(function(){
	$(".um-table tbody tr:odd").addClass("odd");
	var flag=false;
	$("#addWindow").click(function(){
		var a="a";
		$.dialog({
			title:'添加新组',
			lock: true,
			background: '#000', /* 背景色 */
			opacity: 0.5,       /* 透明度 */
			content:"url:groupList.action?a="+a+"&&userId=<%=userId %>",
			icon: 'error.gif',
			cancel: function () {
				flag=true;
				var win=this.iframe.contentWindow;
				var doc=win.document;
				$("#btn",doc).click();
				window.location.href='findGroups.action?userId=<%=userId %>&pageno=<%=pageno%>&pageMap=<%=pageMap%>&familyName=<%=familyName%>&givenName=<%=givenName%>&userPageSize=<%=userPageSize%>';
			},
		});
	})
})
</script>
</body>
</html>