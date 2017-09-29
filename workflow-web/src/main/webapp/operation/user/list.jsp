<%@page import="cn.com.workflow.user.User"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="cn.com.workflow.common.Page"%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%
	String stringMap =request.getAttribute("stringMap").toString();
	Page userPage=	(Page)request.getAttribute("userPage");
	
	String familyName = "";
	String givenName = "";
	if(request.getAttribute("familyName")!=null){
		familyName=request.getAttribute("familyName").toString();
	}
	if(request.getAttribute("givenName")!=null){
		givenName = request.getAttribute("givenName").toString();
	}
	int pageSize=0;
	if(request.getAttribute("pageSize")!=null && !"".equals(request.getAttribute("pageSize"))){
		pageSize=new Integer(request.getAttribute("pageSize").toString());
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
 	<div class="um-s-box">
 	 	<form action="<%=basePath%>/userList.action?pageSize=<%=pageSize %>" method="post"> 	 		
			<span class="um-span"><label>姓氏：</label><input type="text" name="familyName" value="<%=familyName %>" class="um-input" /></span>
			<span class="um-span"><label>名字：</label><input type="text" name="givenName" value="<%=givenName %>" class="um-input" /></span>			
				<input type="submit" value="查询" class="um-btn" />
		</form>
 	</div>
 	<div class="um-span-right">
 	<input id="addWindow" type="button" value="新增" class="um-btn" />
	<input onclick="location.href='userList.action?pageSize=<%=pageSize %>'" type="button" value="列表首页" class="um-btn" />
	</div>

	<div class="table-wrapper">
			<table id="um-table-reload" class="um-table">
				<thead>
					<tr>
						<th>序号</th>
						<th>用户名</th>
						<th>姓</th>
						<th>名</th>
						<th>E-mailL</th>
						<th>相关信息</th>
						<th>操作</th>									
					</tr>					
				</thead>
				<tbody>
					<%
					List list = (List) request.getAttribute("list");
					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							HashMap u = (HashMap)list.get(i);
				%>
					<tr>
						<td><%=i+1 %></td>
						<td><%=u.get("id") %></td>
						<td><%=u.get("familyName") %></td>
						<td><%=u.get("givenName") %></td>
						<td><%=u.get("mail") %></td>
						<td><a href="findGroups.action?userId=<%=u.get("id")%>&pageno=<%=userPage.getPageNo()%>&pageMap=<%=stringMap%>&familyName=<%=familyName%>&givenName=<%=givenName%>&userPageSize=<%=pageSize%>">显示所在组</td>
						<td>
							<select id="operation" name="x">
								<option value ="">--操作--</option>	
								<option value ="update,<%=u.get("id")%>" >密码重置</option>															
								<option value ="delete,<%=u.get("id")%>" >删除</option>																	 
	 						</select><br/>
						</td>
						
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
			 <a> 当前页【<%=userPage.getPageNo()%>】/总页数【<%=userPage.getTotalPage() %>】&nbsp &nbsp
			 	每页显示 &nbsp<select name="pageSize" id="pageSize" onChange="pageSize()" class="um-select-pageSize">
							<option value =""><%=userPage.getPageSize()%></option>
							<% 
								if(userPage.getPageSize()!=5){
							%>
								<option value ="5">5</option>
							<% 
								}
								if(userPage.getPageSize()!=10){
							%>
								<option value ="10">10</option>
							<% 
								}
								if(userPage.getPageSize()!=15){
							%>								
								<option value ="15">15</option>
							<% 
								}
								if(userPage.getPageSize()!=20){
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
			if(<%=userPage.getPageNo()%>==1 || <%=userPage.getPageNo()%>==0){
				alert("您已经到达第一页");
			}
			else{	
				window.location.href="<%=basePath%>/up.action?pageUser=<%=userPage.getPageNo()%>&pageMap=<%=stringMap%>&familyName=<%=familyName%>&givenName=<%=givenName%>&pageSize=<%=pageSize %>";	
			}
		}
		function next()
		{	
			if(<%=userPage.getPageNo()%>>=<%=userPage.getTotalPage()%>){
				alert("您已经到达最后一页");
			}
			else{
				window.location.href="<%=basePath%>/nextPage.action?pageUser=<%=userPage.getPageNo()%>&pageMap=<%=stringMap%>&familyName=<%=familyName%>&givenName=<%=givenName%>&pageSize=<%=pageSize %>";	
			}
		}
		function toppage()
		{	
			if(<%=userPage.getPageNo()%>==1 || <%=userPage.getPageNo()%>==0){
				alert("您已经到达首页");
			}
			else
				window.location.href="<%=basePath%>/topPage.action?pageUser=<%=userPage.getPageNo()%>&pageMap=<%=stringMap%>&familyName=<%=familyName%>&givenName=<%=givenName%>&pageSize=<%=pageSize %>";	
		}
		function endpage()
		{		
			if(<%=userPage.getPageNo()%>>=<%=userPage.getTotalPage()%>){
				alert("您已经到达最后一页");
			}
			else{	
				window.location.href="<%=basePath%>/endPage.action?pagetotleUser=<%=userPage.getTotalPage()%>&pageMap=<%=stringMap%>&familyName=<%=familyName%>&givenName=<%=givenName%>&pageSize=<%=pageSize %>";	
			}
		}
		function pageSize(){
		var pageSize= document.getElementById("pageSize").value;
		window.location.href="<%=basePath%>/userList.action?pageSize="+pageSize+"&pageMap=<%=stringMap%>&familyName=<%=familyName%>&givenName=<%=givenName%>";	
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
			content:"url:<%=basePath%>/operation/user/add.jsp",
			icon: 'error.gif',
			//okVal:"确定",
			//ok: function () {
				//flag=true;
				//var win=this.iframe.contentWindow;
				//var doc=win.document;
				//$("#btn",doc).click();
				//window.location.href='<%=basePath %>/userList.action';
				//return false;
			//},
			cancel: function () {
				flag=true;
				var win=this.iframe.contentWindow;
				var doc=win.document;
				$("#btn",doc).click();
				window.location.reload();
			},
		});
	})
	$("table.um-table select").each(function(){
				$(this).change(function(){
				
					var sel=$(this).val();
					var list=sel.split(",");
					var val=list[0];
					var id=list[1];
					var reset="reset";
					if(val == "update"){
						if(confirm("确定要重置密码吗？")){
							window.location.href='updateUser.action?id='+id+'&reset='+reset+'&pageno=<%=userPage.getPageNo()%>&pageMap=<%=stringMap%>&familyName=<%=familyName%>&givenName=<%=givenName%>&pageSize=<%=pageSize %>';
							alert("重置成功!  初始密码：【000000】");
						}else{
							window.location.reload();
						}
					}
				if(val == "delete"){
					if(confirm("确定要删除吗？")){
						window.location.href='deleteUser.action?id='+id+'&pageno=<%=userPage.getPageNo()%>&pageMap=<%=stringMap%>&familyName=<%=familyName%>&givenName=<%=givenName%>&pageSize=<%=pageSize %>';
						alert("删除成功");
					}else{
						window.location.reload();
					}
				}
			})
	})
})
</script>
<!-- dd -->
</body>
</html>