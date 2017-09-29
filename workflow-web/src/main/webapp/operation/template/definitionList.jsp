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
<%
	//String  usercd= (String) request.getAttribute("usercd");	
	//Page _page =(Page)request.getAttribute("_page");
	//int pageSize=0;
	//if(request.getAttribute("pageSize")!=null && !"".equals(request.getAttribute("pageSize"))){
	//	pageSize=new Integer(request.getAttribute("pageSize").toString());
	//}
	//String  templateName=(String) request.getAttribute("templateName");
	//String templateName = "";
	//if(request.getAttribute("templateName")!=null){
	//	templateName=request.getAttribute("templateName").toString();
	//}
	//List<ProcessDefinitionVO> list = (List<ProcessDefinitionVO>) request.getAttribute("list");
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
		i = 1;
		$(document)
				.ready(
						function() {

							$("select")
									.change(
											function() {
												var strs = $(this).children(
														'option:selected')
														.val().split(",");

												if (strs[0] == "remove") {
													$
															.ajax({
																url : "model/"+strs[0]+ ".do",
																data : strs[1],
																type : "POST",
																async : false,
																dataType : "text",
																contentType : "text/plain",
																error : function(
																		XMLHttpRequest,
																		textStatus,
																		errorThrown) {
																	alert(XMLHttpRequest.status);
																	alert(XMLHttpRequest.readyState);
																	alert(textStatus);
																},
																success : function(
																		json) {
																	location.href = "model/initDefinition.do";
																}
															})
												}else if(strs[0] == "edit") {
													window.open("<%=basePath%>modeler.html?modelId="+strs[1]);
													
												}else if(strs[0] == "deploy") {
													
													$.ajax({
																url : "model/"+strs[0]
																		+ ".do",
																data : strs[1],
																type : "POST",
																async : false,
																dataType : "text",
																contentType : "text/plain",
																error : function(
																		XMLHttpRequest,
																		textStatus,
																		errorThrown) {
																	alert(XMLHttpRequest.status);
																	alert(XMLHttpRequest.readyState);
																	alert(textStatus);
																},
																success : function(
																		json) {
																	alert("deploy model["+strs[1]+"] success!");
																	location.href = "model/initDefinition.do";
																}
															})
												}else if(strs[0] =="pic"){
													window.open("<%=basePath%>model/"+strs[0]+ "/"+strs[1]+".do");
													//diagram-viewer/index.html?processDefinitionId=leave-jpa:1:22&processInstanceId=27
											}
											});
							
								$('#clsButton').click(function(){
									$('#templateName').val('');
									location.href = "model/initDefinition.do";
								});

						});

		function del_1(o) {
			document.getElementById("newUpload1").removeChild(
					document.getElementById("div_" + o));
		}
		

	</script>
	<div
		style="margin: 0 auto; width: 100%; padding-top: 20px; text-align: center; border: 0px solid #555"
		align="center">
		<table>
				<tr>
				<td>
			<div id="table-wrapper" title="创建模型" class="template" align="left">
			<form name="userForm1" action="model/upload.do" 
				enctype="multipart/form-data" method="post">
				<table>
					<tr>
					<td><input type="submit" value="上传"> <!-- <input type="button" id="btn_add1" value="增加一行">  -->
						</td>
						<td>
							<div id="newUpload1">
								<input type="file" name="file">
							</div>
						</td>
						
					</tr>
				</table>
			</form>
			</div>
		
	</td>
	<td>

		<div id="table-wrapper" title="创建模型" class="template" align="right">
			<form id="modelForm" action="<%=basePath%>model/create.do" target="_blank"
				method="post">
				<table>
					<tr>
						<td>名称：</td>
						<td><input id="name" name="name" type="text" /></td>
						<td>KEY：</td>
						<td><input id="key" name="key" type="text" /></td>
						<td>描述：</td>
						<td><textarea id="description" name="description"
								style="width: 150px; height: 20px;"></textarea></td>
						<td><input type="submit" value="创建"></td>
					</tr>
				</table>
			</form>
		</div>
	
	</td>
	</tr>
	</table>
	</div>
	<hr>
	<div id="table-wrapper" title="上传模型" class="template">
		<form action="model/showList.do" method="get">
			<table>
				<tr>
					<td>模板名称：</td>
					<td><input type="text" name="templateName" id="templateName"
						value="${templateName}" /></td>
					<td><input type="submit" value="查询" class="um-btn" /></td>
					<td><input id="clsButton" type="button"  value="reset" class="um-btn"></td>
				</tr>
			</table>
		</form>
	</div>
	<div class="table-wrapper">
		<table id="um-table-reload" class="um-table">
			<thead>
				<tr>
					<th>序号</th>
					<th>id值</th>
					<th>流程名称</th>
					<th>版本号</th>
					<th>模板文件</th>
					<th>模板主键</th>
					<th>是否已部署</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${templates.actDefinitionVOs}" var="actdf"
					varStatus="status">
					<tr>
						<td>${status.index + 1}</td>
						<td>${actdf.id}</td>
						<td>${actdf.name}</td>
						<td>${actdf.version}</a></td>
						<td><a href="<%=basePath %>model/export/${actdf.id}/bpmn.do"
							target="_blank"> xml</a></td>
						<td>${actdf.key}</td>
						<td>
							<c:choose>
								<c:when test="${empty actdf.deploymentId}">
									否
								</c:when>
								<c:otherwise>
									是
								</c:otherwise>
							</c:choose>
						</td>
						<td><select id="sel">
								<option value="">--操作--</option>
								<option value="edit,${actdf.id}">修改</option>
								<option value="deploy,${actdf.id}">部署</option>
								<option value="remove,${actdf.id}">移除</option>
								<option value="pic,${actdf.id}">流程图</option>
								<option value="export,${actdf.id}">导出</option>
						</select></td>
					</tr>
				</c:forEach>
				<tr>
					<div class="um-table-page fr">
						 <a
							href="model/showList.do?pageNo=1&templateName=${templateName}">首页</a>
						<c:choose>
							<c:when test="${page.pageNo - 1 > 0}">
								<a
									href="model/showList.do?pageNo=${page.pageNo - 1}&templateName=${templateName}">上一页</a>
							</c:when>
							<c:when test="${page.pageNo - 1 <= 0}">
								<a href="model/showList.do?pageNo=1&templateName=${templateName}">上一页</a>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${page.totalPage==0}">
								<a
									href="model/showList.do?pageNo=${page.pageNo}&templateName=${templateName}">下一页</a>
							</c:when>
							<c:when test="${page.pageNo + 1 < page.totalPage}">
								<a
									href="model/showList.do?pageNo=${page.pageNo + 1}&templateName=${templateName}">下一页</a>
							</c:when>
							<c:when test="${page.pageNo + 1 >= page.totalPage}">
								<a
									href="model/showList.do?pageNo=${page.totalPage}&templateName=${templateName}">下一页</a>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${page.totalPage==0}">
								<a
									href="model/showList.do?pageNo=${page.pageNo}&templateName=${templateName}">尾页</a>
							</c:when>
							<c:otherwise>
								<a
									href="model/showList.do?pageNo=${page.totalPage}&templateName=${templateName}">尾页</a>
							</c:otherwise>
						</c:choose>
						<font size="2">共 ${page.totalPage}页 </font> <font size="2">第
							${page.pageNo} 页</font>
					</div>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>
