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
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/reset.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/um-page.css">
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
				$("#submit-form").submit(
						function() {
							var json_data=" ";
							if(""!=$('#submit-form').serialize()){
								json_data=$('#submit-form').serialize();
								json_data = decodeURIComponent(json_data,true);
							}
							var taskId=$('#taskId_value').val();
							var sel=$("#submit_type").val();
							if(sel=="complete"){
								$.ajax({
					                cache: true,
					                dataType:"text",
					                type: "POST",
					                url:"task/submit/"+taskId+".do",
					                data:json_data,
									contentType : "text/plain",
					                async: false,
					                error: function(
											XMLHttpRequest,
											textStatus,
											errorThrown) {
					                	alert("XMLHttpRequest.status:"+XMLHttpRequest.status+"\n readyState:"+XMLHttpRequest.readyState+"\n textStatus:"+textStatus);
					                },
					                success: function(data) {
					                	var api = frameElement.api;
					                	api.close();
					                }
					            });
							}else if(sel=="previous"){
								alert("previous");
							}else if(sel=="save"){
								$.ajax({
					                cache: true,
					                dataType:"text",
					                type: "POST",
					                url:"task/save/"+taskId+".do",
					                data:json_data,
									contentType : "text/plain",
					                async: false,
					                error: function(
											XMLHttpRequest,
											textStatus,
											errorThrown) {
					                	alert("XMLHttpRequest.status:"+XMLHttpRequest.status+"\n readyState:"+XMLHttpRequest.readyState+"\n textStatus:"+textStatus);
					                },
					                success: function(data) {
					                	location.href = "worklist/initWorkList.do";
					                }
					            });
							}else if(sel=="first"){
								$.ajax({
					                cache: true,
					                dataType:"text",
					                type: "POST",
					                url:"task/submitByBack/first/"+taskId+".do",
					                data:json_data,
									contentType : "text/plain",
					                async: false,
					                error: function(
											XMLHttpRequest,
											textStatus,
											errorThrown) {
					                	alert("XMLHttpRequest.status:"+XMLHttpRequest.status+"\n readyState:"+XMLHttpRequest.readyState+"\n textStatus:"+textStatus);
					                },
					                success: function(data) {
					                	location.href = "worklist/initWorkList.do";
					                }
					            });
							}else if(sel=="cancel"){
								alert("cancel");
							}else{
								alert("请选择提交类型");
							}
						});
				
			});
			
		
	</script>
	<div  width="300" class="table-wrapper">
		
		<hr/>
			<div style="font-size:14px"><legend>流程业务信息</legend><input style="border-style:none" value="" /></div>
			<div class="grid1_of_4" style=" margin-right: 100%">
			<input style="border-style:none" value="taskId" name="taskId"/>
			<input class="um-input" type="text" value="${taskId}" id="taskId_value" disabled="disabled"/>
			<c:forEach items="${variables}" var="actVariables" varStatus="status">
				   <span>
				    	<input style="border-style:none" value="${actVariables.filedName}" name="${actVariables.filedName}_name"/>
				    	<input class="um-input" type="text" value="${actVariables.filedValue}" name="${actVariables.filedName}" disabled="disabled"/>
				   </span>
			</c:forEach>
			</div> 
			<br/>
			
		<div style="font-size:14px"><legend>表单录入</legend><input style="border-style:none" value="" /></div>
		<form id="submit-form"  onsubmit="return false" >
			<input type="submit" value="提交" class="um-btn" />
			<select id="submit_type" class="um-select">
			  <option value ="complete" selected="selected">完成</option>
			  <option value ="save" >保存</option>
			  <option value ="previous">退回前一节点</option>
			  <option value="first">退回首节点</option>
			  <option value="appoint">退回指定节点</option>
			  <option value="cancel">撤销</option>
			</select>
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
								<td><input  type="text" name="${actform.filedName}.${actform.filedType}" id="${actform.filedName}.${actform.filedType}" value="${actform.filedValue}"  readonly="true"/></td>
							</c:if>
						</tr>
					</c:forEach>
					<tr>
					<td colspan="2">意见</td>
					<td><textarea  name="opinion.string" id="opinion.string" rows="3" >${opinion}</textarea></td>
					</tr>
				</tbody>
			</table>
			
		</form>
	</div>
</body>
</html>