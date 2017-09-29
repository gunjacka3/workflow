<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page language="java" import="java.util.*"%>
<%@page
	import="cn.com.workflow.common.vo.ActDefinitionVO"%>
<%@page
	import="cn.com.workflow.common.vo.ProcessDefinitionVO"%>
<%@page import="cn.com.workflow.common.Page"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'index.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="css/reset.css">
<link rel="stylesheet" type="text/css" href="css/um-page.css">
<link rel="stylesheet" href="<%=basePath%>css/default.css"
	type="text/css" />
<script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath%>js/lhgdialog.js" type="text/javascript"></script>
</head>

<body>
	<script type="text/javascript">
	
	$(document)
	.ready(
			function() {
				$("#create-process-form").submit(
						function() {
							
							$.ajax({
				                cache: true,
				                type: "POST",
				                url:"process/create.do",
				                data:$('#create-process-form').serialize(),
								dataType : "text",
								contentType : "text/plain",
				                async: false,
				                error: function(
										XMLHttpRequest,
										textStatus,
										errorThrown) {
				                	alert("XMLHttpRequest.status:"+XMLHttpRequest.status+"\n readyState:"+XMLHttpRequest.readyState+"\n textStatus:"+textStatus);
				                },
				                success: function(data) {
				                	if(data.ErrorType=='business' || data.ErrorType=='system' ){
				                		alert(data.message)
				                	}else{
					                    window.opener=null;
					                    window.open('','_self');
					                    window.close();
				                	}
				                }
				            });
						});
				
			});
			
		
	</script>
	<div  width="300" class="table-wrapper">
		<form id="create-process-form" >
		<input type="submit" value="发起" class="um-btn" />
		<hr/>
		<table width="50%" id="um-table-reload" class="um-table">
			<tr>
				<td >流程信息</td>
				<td >流程模板标示:</td>
				<td><input type="text" name="pdfKey" id="pdfKey" value="${pdfKey}" /></td>
			</tr>
		</table>
		<hr/>
			<table width="300px" id="um-table-reload" class="um-table">
				<thead>
					<tr>
						<th>序号</th>
						<th>字段</th>
						<th>值</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${actFromInfos}" var="actform" varStatus="status">
						<tr>
							<td>${status.index + 1}</td>
							<td>${actform.filedName}</td>
							<c:if test="${actform.filedWR=='1'}">
								<td><input type="text" name="${actform.filedName}.${actform.filedType}" id="${actform.filedName}.${actform.filedType}" value="${actform.filedValue}" /></td>
							</c:if>
							<c:if test="${actform.filedWR=='0'}">
								<td><input type="text" name="${actform.filedName}.${actform.filedType}" id="${actform.filedName}.${actform.filedType}" value="${actform.filedValue}"   readonly="true" /></td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
		</form>
	</div>
</body>
</html>