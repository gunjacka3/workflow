<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="cn.com.workflow.user.User"%>
<%@page import="cn.com.workflow.user.UserServiceImpl"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="cn.com.workflow.common.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%  	
	String groupMap=null;
	String name="";
	if(request.getAttribute("name")!=null){
		name=request.getAttribute("name").toString();
	}
	if(request.getAttribute("stringMap")!=null){
		groupMap =request.getAttribute("stringMap").toString();
	}
	int pageSize=0;
	if(request.getAttribute("pageSize")!=null && !"".equals(request.getAttribute("pageSize"))){
		pageSize=new Integer(request.getAttribute("pageSize").toString());
	}
	Page groupPage=(Page)request.getAttribute("groupPage");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<base href="<%=basePath%>">
<title>My JSP 'list.jsp' starting page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	    
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
		String msg=null;
		if(request.getAttribute("msg")!=null){
			msg=request.getAttribute("msg").toString();
	%>
		<script type="text/javascript">
			alert('<%=msg%>');
		</script>
	<% 
		}
	%>
 	<div class="um-s-box">
 	 	<form action="<%=basePath%>/groupList.action?pageSize=<%=pageSize %>" method="post"> 	 		
			<span class="um-span"><label>机构名称：</label><input type="text" name="name" value="<%=name%>" class="um-input" /></span>
				<input type="submit" value="查询" class="um-btn" />
		</form>
 	</div>
 	<div class="um-span-right">
 	<input id="addWindow" type="button" value="新增" class="um-btn" />
	<input onclick="location.href='groupList.action?pageSize=<%=pageSize %>'" type="button" value="列表首页" class="um-btn" />
	</div>

	<div class="table-wrapper">
			<table id="um-table-reload" class="um-table">
				<thead>
					<tr>
						<th>序号</th>
						<th>机构名称</th>
						<th>parent</th>
						<th>相关信息</th>
						<th>操作</th>									
					</tr>					
				</thead>
				<tbody>
					<%
						List list = (List) request.getAttribute("list");
						if (list != null && list.size()>0) {
							for (int i = 0; i < list.size(); i++) {
								HashMap u = (HashMap)list.get(i);
								Object parent=u.get("parent");
								if(parent==null){
									parent="";
								}
					%>
					<tr>
						<td><%=i+1 %></td>
						<td><%=u.get("name") %></td>
						<td><%=parent %></td>
						<td><a href="findUsers.action?id=<%=u.get("id")%>&name=<%=name%>&pageno=<%=groupPage.getPageNo()%>&groupPageSize=<%=pageSize %>">组内用户</td>
						<td><a href="JavaScript:deleteGroup('<%=u.get("id")%>')">删除</a></td>

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
    	<div class="um-table-page fl">
			 <input type="button" value="首页" onclick="toppage()" class="um-page-btn">
    		 <input type="button" value="上一页" onclick="previous()" class="um-page-btn">
			 <a> 当前页【<%=groupPage.getPageNo()%>】/总页数【<%=groupPage.getTotalPage() %>】&nbsp &nbsp
			 	每页显示 &nbsp<select name="pageSize" id="pageSize" onChange="pageSize()" class="um-select-pageSize">
							<option value =""><%=groupPage.getPageSize()%></option>
							<% 
								if(groupPage.getPageSize()!=5){
							%>
								<option value ="5">5</option>
							<% 
								}
								if(groupPage.getPageSize()!=10){
							%>
								<option value ="10">10</option>
							<% 
								}
								if(groupPage.getPageSize()!=15){
							%>								
								<option value ="15">15</option>
							<% 
								}
								if(groupPage.getPageSize()!=20){
							%>	
								<option value ="20">20</option>
							<% 
								}
							%>									
		 				</select>
		 		 &nbsp条数据
		 	</a>
			 <input type="button" value="下一页" onclick="next()" class="um-page-btn">
			 <input type="button" value="尾页" onclick="endpage()" class="um-page-btn">			 
		</div>   
		<script type="text/javascript">
		function previous()
		{		
			if(<%=groupPage.getPageNo()%>==1 || <%=groupPage.getPageNo()%>==0){
				alert("您已经到达第一页");
			}
			else{	
				window.location.href="<%=basePath%>/up.action?pageGroup=<%=groupPage.getPageNo()%>&pageMap=<%=groupMap%>&pageSize=<%=pageSize %>";	
			}
		}
		function next()
		{	
			if(<%=groupPage.getPageNo()%>==<%=groupPage.getTotalPage()%> ||<%=groupPage.getTotalPage()%>==0){
				alert("您已经到达最后一页");
			}
			else{
				window.location.href="<%=basePath%>/nextPage.action?pageGroup=<%=groupPage.getPageNo()%>&pageMap=<%=groupMap%>&pageSize=<%=pageSize %>";	
			}
		}
		function toppage()
		{	
			if(<%=groupPage.getPageNo()%>==1 || <%=groupPage.getPageNo()%>==0){
				alert("您已经到达首页");
			}
			else
				window.location.href="<%=basePath%>/topPage.action?pageGroup=<%=groupPage.getPageNo()%>&pageMap=<%=groupMap%>&pageSize=<%=pageSize %>";	
		}
		function endpage()
		{		
			if(<%=groupPage.getPageNo()%>==<%=groupPage.getTotalPage()%> ||<%=groupPage.getTotalPage()%>==0){
				alert("您已经到达最后一页");
			}
			else{	
				window.location.href="<%=basePath%>/endPage.action?pagetotleGroup=<%=groupPage.getTotalPage()%>&pageMap=<%=groupMap%>&pageSize=<%=pageSize %>";	
			}
		}
		function deleteGroup(id){  
		 		if(confirm("确定要删除吗？")){
					window.location.href='deleteGroup.action?id='+id+'&pageno=<%=groupPage.getPageNo()%>&pageSize=<%=pageSize %>';
				}else{
					window.location.href='<%=basePath %>/groupList.action?pageno=<%=groupPage.getPageNo()%>&pageSize=<%=pageSize %>&pageMap=<%=groupMap%>';
					//window.location.reload();
					
				}
		  }
		function pageSize(){
			var pageSize= document.getElementById("pageSize").value;
			window.location.href="<%=basePath%>/groupList.action?pageSize="+pageSize+"&pageno=<%=groupPage.getPageNo()%>";	
		}								
		</script> 
<script>
$(document).ready(function(){
	$(".um-table tbody tr:odd").addClass("odd");
	var flag=false;
	$("#addWindow").click(function(){
		$.dialog({
			title:'新增',
			lock: true,
			background: '#000', /* 背景色 */
			opacity: 0.5,       /* 透明度 */
			content:"url:<%=basePath%>/operation/group/add.jsp",
			icon: 'error.gif',
			//okVal:"确定",
			//ok: function () {
				//flag=true;
				//var win=this.iframe.contentWindow;
				//var doc=win.document;
				//$("#btn",doc).click();
				//window.location.href='<%=basePath %>/groupList.action?pageno=<%=groupPage.getTotalPage()%>';
				//return false;
			//},
			cancel: function () {
				flag=true;
				var win=this.iframe.contentWindow;
				var doc=win.document;
				$("#btn",doc).click();
				window.location.href='<%=basePath %>/groupList.action?pageno=<%=groupPage.getPageNo()%>&pageSize=<%=pageSize %>&pageMap=<%=groupMap%>';
				//window.location.reload();
			},
		});
	});
})
</script>
</body>
</html>