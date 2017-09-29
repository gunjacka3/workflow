<%@page import="cn.com.workflow.user.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.workflow.common.Page"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%	
	String groupId=null;
	if(request.getAttribute("groupId")!=null){
		groupId=request.getAttribute("groupId").toString();
	}
	List<User> list = (List<User>) request.getAttribute("list");	
	
	String name="";
	String pageno ="";
	if(request.getAttribute("pageno")!=null){
		pageno=request.getAttribute("pageno").toString();
	}
	if(request.getAttribute("name")!=null){
		name=request.getAttribute("name").toString();
	}
	String groupPageSize="";
	if(request.getAttribute("groupPageSize")!=null){
		groupPageSize=request.getAttribute("groupPageSize").toString();
	}
	int pageSize=0;
	if(request.getAttribute("pageSize")!=null && !"".equals(request.getAttribute("pageSize"))){
		pageSize=new Integer(request.getAttribute("pageSize").toString());
	}
	Page _page=(Page)request.getAttribute("_page");
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
 	<div class="um-s-box">
 	 	<form action="<%=basePath%>/userList.action?groupPageSize=<%=groupPageSize %>&pageSize=<%=pageSize %>" method="post"> 	 		
			<span class="um-span"></span>
			<span class="um-span"></span>			
			<span class="um-span"></span>
		</form>
 	</div>
 	<div class="um-span-left">
		<input onclick="location.href='groupList.action?pageno=<%=pageno%>&name=<%=name%>&pageSize=<%=groupPageSize %>'" type="button" value="返回" class="um-btn" />
	</div>
 	<!-- <div class="um-span-right">
	<input onclick="location.href='groupList.action'" type="button" value="列表首页" class="um-btn" />
	</div> -->

	<div class="table-wrapper">
			<table id="um-table-reload" class="um-table">
				<thead>
					<tr>
						<th>序号</th>
						<th>组ID</th>
						<th>用户名</th>
						<th>用户姓氏</th>
						<th>用户名字</th>
						<th>用户电子邮件</th>
						<th>操作</th>									
					</tr>					
				</thead>
				<tbody>
					<%
					if (list != null && list.size() > 0) {
						for(int i=0;i<list.size();i++){
							User user = list.get(i);
				%>
					<tr>
						<td><%=i+1 %></td>
						<td><%=groupId %></td>
						<td><%=user.getUserId() %></td>
						<td><%=user.getFamilyName() %></td>
						<td><%=user.getGivenName() %></td>
						<td><%=user.getMail() %></td>
						<td><a href="JavaScript:deleteGroup('<%=user.getUserId() %>')">移除</td>
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
			 <a> 当前页【<%=_page.getPageNo()%>】/总页数【<%=_page.getTotalPage() %>】&nbsp &nbsp
			 	每页显示 &nbsp<select name="pageSize" id="pageSize" onChange="pageSize()" class="um-select-pageSize">
							<option value =""><%=_page.getPageSize()%></option>
							<% 
								if(_page.getPageSize()!=5){
							%>
								<option value ="5">5</option>
							<% 
								}
								if(_page.getPageSize()!=10){
							%>
								<option value ="10">10</option>
							<% 
								}
								if(_page.getPageSize()!=15){
							%>								
								<option value ="15">15</option>
							<% 
								}
								if(_page.getPageSize()!=20){
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
	function deleteGroup(id){  
		if(confirm("确定要移除该用户吗？")){
			window.location.href='moveUser.action?userId='+id+'&groupsId=<%=groupId %>&pageno=<%=pageno%>&name=<%=name%>&pagenoUser=<%=_page.getPageNo()%>&groupPageSize=<%=groupPageSize %>&pageSize=<%=pageSize %>';
			alert("移除成功");
		}else{
			window.location.reload();
		}
	} 
</script>   	  
<script type="text/javascript">
		function previous()
		{		
			if(<%=_page.getPageNo()%>==1 || <%=_page.getPageNo()%>==0){
				alert("您已经到达第一页");
			}
			else{	
				window.location.href="<%=basePath%>/up.action?pageFindUser=<%=_page.getPageNo()%>&groupId=<%=groupId%>&pageno=<%=pageno%>&name=<%=name%>&groupPageSize=<%=groupPageSize %>&pageSize=<%=pageSize %>";	
			}
		}
		function next()
		{	
			if(<%=_page.getPageNo()%>==<%=_page.getTotalPage()%> ||<%=_page.getTotalPage()%>==0){
				alert("您已经到达最后一页");
			}
			else{
				window.location.href="<%=basePath%>/nextPage.action?pageFindUser=<%=_page.getPageNo()%>&groupId=<%=groupId%>&pageno=<%=pageno%>&name=<%=name%>&groupPageSize=<%=groupPageSize %>&pageSize=<%=pageSize %>";	
			}
		}
		function toppage()
		{	
			if(<%=_page.getPageNo()%>==1 || <%=_page.getPageNo()%>==0){
				alert("您已经到达首页");
			}
			else
				window.location.href="<%=basePath%>/topPage.action?pageFindUser=<%=_page.getPageNo()%>&groupId=<%=groupId%>&pageno=<%=pageno%>&name=<%=name%>&groupPageSize=<%=groupPageSize %>&pageSize=<%=pageSize %>";	
		}
		function endpage()
		{		
			if(<%=_page.getPageNo()%>==<%=_page.getTotalPage()%> ||<%=_page.getTotalPage()%>==0){
				alert("您已经到达最后一页");
			}
			else{	
				window.location.href="<%=basePath%>/endPage.action?pagetotleFindUser=<%=_page.getTotalPage()%>&groupId=<%=groupId%>&pageno=<%=pageno%>&name=<%=name%>&groupPageSize=<%=groupPageSize %>&pageSize=<%=pageSize %>";	
			}
		}
		function pageSize(){
		var pageSize= document.getElementById("pageSize").value;
		window.location.href="<%=basePath%>/findUsers.action?pageSize="+pageSize+"&id=<%=groupId%>&pageno=<%=pageno%>&name=<%=name%>&groupPageSize=<%=groupPageSize %>";	
	}								
</script> 
</body>
</html>