<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page language="java" import="java.util.*"%>
<%@page
	import="cn.com.workflow.common.vo.ActDefinitionVO"%>
<%@page
	import="cn.com.workflow.common.vo.ProcessDefinitionVO"%>
<%@page import="cn.com.workflow.common.Page"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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

												if (strs[0] == "take") {
													$
															.ajax({
																url : "worklist/take/"+strs[1]+ ".do",
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
																	alert("claim task["+strs[1]+"] success!");
																	location.href = "worklist/initWorkList.do";
																}
															})
												
												}else if (strs[0] == "handle") {
													$.dialog({
														id:'001',
														title:'任务处理',
														autoOpen: false,
														modal:true,
														lock: true,
														height:500, 
														width:850,
														background: '#000', /* 背景色 */
														opacity: 0.5,       /* 透明度 */
														content:"url:<%=basePath%>task/handle/"+strs[1]+ ".do",
													    close: function(){
													    	this.reload();
													    }
													});
												
											}else if(strs[0] =="pic"){
														
														$.dialog({
																title:'流程图',
																modal:true,
																lock: true,
																height:400, 
																width:1250,
																background: '#000', /* 背景色 */
																opacity: 0.5,       /* 透明度 */
																content:"url:<%=basePath%>diagram-viewer/index2.html?processDefinitionId="+strs[2]+"&processInstanceId="+strs[1],
																buttons: {
															        "Ok": function() {
															          $(this).dialog("close");
															        }
															      }
																
															});
														
														}
											});
							
								$('#clsButton').click(function(){
									$('#customerName').val('');
									$('#bizType').val('');
									location.href = "worklist/initWorkList.do";
								});

						});


	</script>
	<div
		style="margin: 0 auto; width: 100%; padding-top: 20px; text-align: center; border: 0px solid #555"
		align="center">
		<table>
				<tr>
	<td>

		<div id="table-wrapper" title="查询" class="template" align="right">
			<form id="conditionForm" action="<%=basePath%>worklist/queryWorkList.do" 
				method="post">
				<table>
					<tr>
						<td>客户名称：</td>
						<td><input id="customerName" name="customerName" type="text" value="${workListCondition.customerName}"/></td>
						<td>业务类型：</td>
						<td><input id="bizType" name="bizType" type="text"  value="${workListCondition.bizType}"/></td>
						<td><input type="submit" value="查询" class="um-btn"></td>
					</tr>
				</table>
			</form>
		</div>
	
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
					<th>任务id</th>
					<th>流程id</th>
					<th>任务名称</th>
					<th>创建时间</th>
					<th>模板id</th>
					<th>taskDefinitionKey</th>
					<th>客户名称</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${workList.actWorkListVOs}" var="actcwl"
					varStatus="status">
					<tr>
						<td>${status.index + 1}</td>
						<td>${actcwl.id}</td>
						<td>${actcwl.executionId}</td>
						<td><a href="<%=basePath %>task/handle/${actcwl.id}.do">${actcwl.name}</a></td>
						<td><fmt:formatDate value="${actcwl.createTime}" type="date"/></td>
						<td>${actcwl.processDefinitionId}</td>
						<td>${actcwl.taskDefinitionKey}</a></td>
						<td>${actcwl.processVariables.customerName}</td>
						<td><select id="sel">
								<option value="">--操作--</option>
								<option value="handle,${actcwl.id}">处理</option>
								<option value="view,${actcwl.id}">查看</option>
								<option value="replace,${actcwl.id}">拒签</option>
								<option value="pic,${actcwl.executionId},${actcwl.processDefinitionId},${actcwl.id}">流程图</option>
						</select></td>
					</tr>
				</c:forEach>
				<tr>
					<div class="um-table-page fr">
						 <a
							href="worklist/queryworkList?pageNo=1">首页</a>
						<c:choose>
							<c:when test="${page.pageNo - 1 > 0}">
								<a
									href="worklist/queryworkList?pageNo=${page.pageNo - 1}">上一页</a>
							</c:when>
							<c:when test="${page.pageNo - 1 <= 0}">
								<a href="worklist/queryworkList?pageNo=1">上一页</a>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${page.totalPage==0}">
								<a
									href="worklist/queryworkList?pageNo=${page.pageNo}">下一页</a>
							</c:when>
							<c:when test="${page.pageNo + 1 < page.totalPage}">
								<a
									href="worklist/queryworkList?pageNo=${page.pageNo + 1}">下一页</a>
							</c:when>
							<c:when test="${page.pageNo + 1 >= page.totalPage}">
								<a
									href="worklist/queryworkList?pageNo=${page.totalPage}">下一页</a>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${page.totalPage==0}">
								<a
									href="worklist/queryworkList?pageNo=${page.pageNo}">尾页</a>
							</c:when>
							<c:otherwise>
								<a
									href="worklist/queryworkList?pageNo=${page.totalPage}">尾页</a>
							</c:otherwise>
						</c:choose>
						<a  href="#">共 ${page.totalPage}页 </a> <a  href="#">第
							${page.pageNo} 页</a>
					</div>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>
