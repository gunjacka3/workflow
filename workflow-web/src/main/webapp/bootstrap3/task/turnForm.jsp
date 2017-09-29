<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
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
<link rel="stylesheet" href="<%=basePath %>css/reset.css" type="text/css" >
<link rel="stylesheet" href="<%=basePath %>css/bootstrap.min.css" type="text/css" >
<link rel="stylesheet" href="<%=basePath%>css/default.css" type="text/css" />
<link rel="stylesheet" href="<%=basePath%>css/bootstrap-dialog.min.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/um-page2.css">
<script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath%>js/bootstrap.min.js"></script>
<script src="<%=basePath%>js/jquery-ui.js"></script>
<script src="<%=basePath%>js/bootstrap-dialog.min.js"></script>
<script src="<%=basePath %>js/jquery.validate.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=basePath %>pages/js/wzy.js"></script>
</head>
<body>
	<script type="text/javascript">
	$(document)
	.ready(
			function() {
				
			});
	</script>
		<table id="um-table-reload" class="table table-condensed">
				<tr>
					<td>
						<h4><span class="label label-primary">流程变量</span></h4>
					</td>
				</tr>
				<tr>
					<td>
					<div class="input-group input-group-sm">
					  <span class="input-group-addon" id="sizing-addon1">任务Id</span>
					  <input type="text" class="form-control" name="taskId" value="${taskId}" placeholder="taskId" aria-describedby="sizing-addon1" />
					</div>
					</td>
				</tr>
				<c:forEach items="${variables}" var="actVariables" varStatus="status">
					<tr>
						<td>
						<div class="input-group input-group-sm">
						  <span class="input-group-addon" id="sizing-addon2">${actVariables.filedName}</span>
						  <input type="text" class="form-control" name="${actVariables.filedName}" value="${actVariables.filedValue}" placeholder="${actVariables.filedName}" aria-describedby="sizing-addon3" />
						</div>
						</td>
					</tr>
				</c:forEach>
		</table>



		<form id="turn-form" >
			<input id="userIds" type="text" style="display:none">
			<table id="um-table-reload" class="table table-condensed">
					<tr>
						<td>
						<div class="input-group">
						  <span class="input-group-addon" id="basic-addon1">被转派人Id</span>
						  <input id="delegater" name="delegater" type="text" class="form-control" placeholder="被转派人Id..." aria-describedby="basic-addon1" required>
						</div>
						</td>
					</tr>
			</table>
		</form>
	</div>
</body>
</html>