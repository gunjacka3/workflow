<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page language="java" import="java.util.*"%>
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
<link rel="stylesheet" href="<%=basePath %>css/reset.css" type="text/css" >
<link rel="stylesheet" href="<%=basePath %>css/bootstrap.min.css" type="text/css" >
<link rel="stylesheet" href="<%=basePath%>css/default.css" type="text/css" />
<link rel="stylesheet" href="<%=basePath%>css/bootstrap-dialog.min.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/um-page2.css">
<script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath%>js/bootstrap.min.js"></script>
<script src="<%=basePath%>js/jquery-ui.js"></script>
<script src="<%=basePath%>js/jquery-ui.js"></script>
<script src="<%=basePath%>js/bootstrap-dialog.min.js"></script>
<script src="<%=basePath %>js/jquery.validate.min.js" type="text/javascript"></script>
</head>
<body>
<script type="text/javascript">
	
</script>

	<div class="table-wrapper">
		<table id="um-table-reload" class="table table-condensed">
			<thead>
				<tr>
					<th><p class="text-primary">#</p></th>
					<th><p class="text-primary">任务类型</p></th>
					<th><p class="text-primary">任务名称</p></th>
					<th><p class="text-primary">处理人</p></th>
					<th><p class="text-primary">开始时间</p></th>
					<th><p class="text-primary">结束时间</p></th>
					<th><p class="text-primary">意见</p></th>
					
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${hisTasks.actHisActInstListVOs}" var="actdf"
					varStatus="status">
					<tr>
						<td>
							<c:choose>
								<c:when test="${empty actdf.taskId}">
									<input name="selectId" type="radio" value="" disabled="disabled"/>
								</c:when>
								<c:otherwise>
									<input name="selectId" type="radio" value="${actdf.taskId}" />
								</c:otherwise>
							</c:choose>
						</td> 
						<td>
							<c:choose>
								<c:when test="${actdf.activityType=='userTask'}">
									<button type="button" class="btn btn-default btn-sm" onclick="">
				  						<span class="glyphicon glyphicon-user""></span>
									</button>
								</c:when>
								<c:when test="${actdf.activityType=='startEvent'}">
									<button type="button" class="btn btn-default btn-sm" onclick="">
				  						<span class="glyphicon glyphicon-play-circle""></span>
									</button>
								</c:when>
								<c:when test="${actdf.activityType=='endEvent'}">
									<button type="button" class="btn btn-default btn-sm" onclick="">
				  						<span class="glyphicon glyphicon-off""></span>
									</button>
								</c:when>
								<c:when test="${actdf.activityType=='serviceTask'}">
									<button type="button" class="btn btn-default btn-sm" onclick="">
				  						<span class="glyphicon glyphicon-arrow-right""></span>
									</button>
								</c:when>
								<c:when test="${actdf.activityType=='manualTask'}">
									<button type="button" class="btn btn-default btn-sm" onclick="">
				  						<span class="glyphicon glyphicon-forward""></span>
									</button>
								</c:when>
								<c:when test="${actdf.activityType=='callActivity'}">
									<button type="button" class="btn btn-default btn-sm" onclick="javascript:openSubProcess(${actdf.calledProcessInstanceId})">
				  						<span class="glyphicon glyphicon-list-alt""></span>
									</button>
								</c:when>
								<c:otherwise>
									<button type="button" class="btn btn-default btn-sm" onclick="">
				  						<span class="glyphicon glyphicon-user""></span>
									</button>
								</c:otherwise>
							</c:choose>
							
						</td>
						<td>${actdf.activityName}</td>
						<td>
							<c:choose>
								<c:when test="${empty actdf.assignee}">
									无
								</c:when>
								<c:otherwise>
									${actdf.assignee}
								</c:otherwise>
							</c:choose>
						</td>
						<td><fmt:formatDate value="${actdf.startTime}" type="both"/></td>
						<td><fmt:formatDate value="${actdf.endTime}" type="both"/></td>
						<td>${actdf.opinion}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>
