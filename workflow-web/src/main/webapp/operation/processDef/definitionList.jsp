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
																url : "proDef/"+strs[0]+ ".do",
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
																	location.href = "proDef/initDefinition.do";
																}
															})
												}else if(strs[0] =="pic"){
													window.open("<%=basePath%>proDef/"+strs[0]+ "/"+strs[1]+".do");
													//diagram-viewer/index.html?processDefinitionId=leave-jpa:1:22&processInstanceId=27
											}
											});
							
								$('#clsButton').click(function(){
									$('#templateName').val('');
									location.href = "proDef/initDefinition.do";
								});
								
								$("#create-dialog").dialog({
								      autoOpen: false,
								      height: 300,
								      width: 350,
								      modal: true,
								      buttons: {
								    	  "ok":function(){
								    		  
								    	  },
								    	  "cancel":function(){
								    		  
								    	  }
								    	 }
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
				<div id="table-wrapper" title="上传模型" class="template">
					<form action="proDef/showList.do" method="get">
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
				</td>
				<td align="right">
				全部模板最终版本打包
				<a href="<%=basePath %>proDef/download.do"
							target="_blank">>>点击下载<<</a>
				</td>
				</tr>
	</table>
	</div>
	<hr>
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
						<td><a href="<%=basePath %>proDef/start/${actdf.id}/${actdf.key}.do" target="_blank">${actdf.id}</a></td>
						<td>${actdf.name}</td>
						<td>${actdf.version}</a></td>
						<td><a href="<%=basePath %>proDef/export/${actdf.id}/bpmn.do"
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
								<option value="pic,${actdf.id}">流程图</option>
								<option value="export,${actdf.id}">导出</option>
								<option value="remove,${actdf.deploymentId}">移除发布</option>
						</select></td>
					</tr>
				</c:forEach>
				<tr>
					<div class="um-table-page fr">
						 <a
							href="proDef/showList.do?pageNo=1&templateName=${templateName}">首页</a>
						<c:choose>
							<c:when test="${page.pageNo - 1 > 0}">
								<a
									href="proDef/showList.do?pageNo=${page.pageNo - 1}&templateName=${templateName}">上一页</a>
							</c:when>
							<c:when test="${page.pageNo - 1 <= 0}">
								<a href="proDef/showList.do?pageNo=1&templateName=${templateName}">上一页</a>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${page.totalPage==0}">
								<a
									href="proDef/showList.do?pageNo=${page.pageNo}&templateName=${templateName}">下一页</a>
							</c:when>
							<c:when test="${page.pageNo + 1 < page.totalPage}">
								<a
									href="proDef/showList.do?pageNo=${page.pageNo + 1}&templateName=${templateName}">下一页</a>
							</c:when>
							<c:when test="${page.pageNo + 1 >= page.totalPage}">
								<a
									href="proDef/showList.do?pageNo=${page.totalPage}&templateName=${templateName}">下一页</a>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${page.totalPage==0}">
								<a
									href="proDef/showList.do?pageNo=${page.pageNo}&templateName=${templateName}">尾页</a>
							</c:when>
							<c:otherwise>
								<a
									href="proDef/showList.do?pageNo=${page.totalPage}&templateName=${templateName}">尾页</a>
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
