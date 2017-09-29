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



		<form id="submit-form" >
			<input id="userIds" type="text" style="display:none">
			<table id="um-table-reload" class="table table-condensed">
					<tr>
						<td>
							<h4><span class="label label-primary">表单变量</span></h4>
						</td>
					</tr>
					<c:forEach items="${actFromInfos}" var="actform" varStatus="status">
						<tr>
							<td>
							<c:if test='${actform.filedType!="enum"}'>
								<div class="input-group input-group-sm">
								  <span class="input-group-addon" id="sizing-addon3">${actform.filedName}</span>
								  <input type="text" class="form-control" name="${actform.filedId}.${actform.filedType}" value="${actform.filedValue}" placeholder="${actform.filedName}" aria-describedby="sizing-addon3" />
								</div>
							</c:if>
							<c:if test='${actform.filedType=="enum"}'>
								
								<div class="input-group input-group-sm">
								<span class="input-group-addon" id="sizing-addonx">${actform.filedName}</span>
							      <select id="${actform.filedId}" name ="${actform.filedId}" class="form-control" aria-describedby="sizing-addonx">
								       <c:forEach items="${actform.values}" var="value" varStatus="status" >
								       		<c:if test='${status.index==0}'>
								       			<option value ="${value.valueId}" selected="selected">${value.valueName}</option>
								       		</c:if>
								       		<c:if test='${status.index!=0}'>
								       		  	<option value ="${value.valueId}">${value.valueName}</option>
								       		</c:if>
								       </c:forEach>
							      </select>
				   				</div>
							
							</c:if>
							</td>
							<td align="right" width="20%">
								<button type="button" class="btn btn-info btn-sm" onclick="javascript:;">
  									${actform.filedType}
								</button>
							</td>
						</tr>
					</c:forEach>
					<tr>
						<td>
							<h4><span class="label label-primary">结论</span></h4>
							<div class="form-group">
						      <select id="conclusion" name ="conclusion" class="form-control">
						          <option value ="0" selected="selected">同意</option>
						          <option value ="1">拒绝</option>
						      </select>
			   				</div>
						</td>
						<td>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td>
							<h4><span class="label label-primary">意见</span></h4>
							<textarea style="resize: none;"  name="opinion" class="form-control" rows="4">${opinion}</textarea>
						</td>
					</tr>
					<tr>
						<td>
							<h4><span class="label label-primary">提交类型</span></h4>
							<div class="form-group">
						      <select id="submitType" name ="submitType" class="form-control">
						      		<c:forEach items="${actActions}" var="actionType" >
								          <option value ="${actionType.valueId}">${actionType.valueName}</option>
								    </c:forEach>
						      </select>
			   				</div>
						</td>
						<td>
							&nbsp;
						</td>
					</tr>
					
					<c:if test="${empty nextUsers}">
						<tr>
							<td colspan="4">
								<h4><span class="label label-danger">下一岗位获取失败,请检查异常信息[${message}]</span></h4>
							</td>
						</tr>
					</c:if>
					<c:if test="${not empty nextUsers}">
						<c:forEach items="${nextUsers.selectUserInfos}" var="selectUserInfo" varStatus="status1">
							<tr>
								<td colspan="4">
									<h4><span class="label label-primary">下一任务人</span></h4>
								</td>
							</tr>
							<tr>
								<td>
									<h4><span class="label label-primary">下一节点名称</span></h4>
								</td>
								<td>
									<h4>${selectUserInfo.actityName}</h4>
								</td>
								<td>
									<h4><span class="label label-primary">选人类型</span></h4>
								</td>
								<td>
									<h4>${selectUserInfo.selectType}</h4>
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<table id="um-table-reload2" class="table table-condensed">
										<c:forEach items="${selectUserInfo.roles}" var="roles" varStatus="status2">
											<tr>
												<td>
													<h4><span class="label label-primary">岗位名称</span></h4>
												</td>
												<td>
													<h4>${roles.valueName}</h4>
												</td>
											</tr>
											<tr>
												<td>
													<h4><span class="label label-primary">人员:</span></h4>
												</td>
												<td>
													<c:choose>
														<c:when test="${selectUserInfo.selectType=='01'}">
															<c:forEach items="${roles.values}" var="users" varStatus="status3">
																<div class="radio">
																  <label>
																    <input type="radio" name="${selectUserInfo.userVariable}" id="optionsRadios1" value="${users.valueId}" >
																    ${users.valueName}
																  </label>
																</div>
															</c:forEach>
														</c:when>
														<c:when test="${selectUserInfo.selectType=='02'}">
															<c:forEach items="${roles.values}" var="users" varStatus="status3">
																<div class="checkbox">
																  <label>
																    <input name="${selectUserInfo.userVariable}" type="checkbox" value="${users.valueId}">
																    ${users.valueName}
																  </label>
																</div>
															</c:forEach>
														</c:when>
														<c:when test="${selectUserInfo.selectType=='00'}">
															<h4><span class="label label-primary">不需要选人</span></h4>
														</c:when>
														<c:otherwise>
															<h4><span class="label label-danger">${selectUserInfo.selectType}未定义</span></h4>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</c:forEach>
									</table>
								</td>
							</tr>	
					</c:forEach>
				</c:if>
		</table>
	</form>
	</div>
</body>
</html>