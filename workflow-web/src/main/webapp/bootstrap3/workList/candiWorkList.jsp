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
<script src="<%=basePath%>js/bootstrap-dialog.min.js"></script>
<script src="<%=basePath %>js/jquery.validate.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=basePath %>pages/js/wzy.js"></script>
</head>
<body>
	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							$('a').click(function() {
								var strs = $(this).attr('name').split(",");
												if (strs[0] == "take") {
													BootstrapDialog.confirm({
											            title: '提示',
											            message: '确认领取该任务吗?',
											            type: BootstrapDialog.TYPE_INFO,
											            closable: true, 
											            draggable: true, 
											            btnCancelLabel: '拒绝', 
											            btnOKLabel: '同意', 
											            btnOKClass: 'btn-warning', 
											            callback: function(result) {
											            	if(result) {
												            	$.ajax({
																	url : "worklist/take/"+strs[1]+ ".do",
																	data : strs[1],
																	type : "POST",
																	async : false,
																	dataType : "text",
																	contentType : "text/plain",
													            	success:function(data){
											                			if(data!=""){
												                			var d=JSON.parse(data);
												                			if(d.message !=""){
												                				BootstrapDialog.show({
												                		            title: '异常',
												                		            message: d.message
												                		        });
												                			}
											                			}else{
												                			location.href = "worklist/initCandiWorkList.do";
											                			}
											                		}
																})
												            }
											            }
											        });		
															
													
												}else if(strs[0] =="pic"){
													BootstrapDialog.show({
														type:BootstrapDialog.TYPE_INFO,
														title:"查看流程图",
														cssClass: 'wide-dialog',
											            message: function(dialog) {
											                var $message = $('<div style="height:300px; overflow:auto"></div>');
											                var pageToLoad = dialog.getData('pageToLoad');
											                $message.load(pageToLoad);
											        
											                return $message;
											            },
											            data: {
											                'pageToLoad': '<%=basePath%>diagram-viewer/index2.jsp?processDefinitionId='+strs[2]+'&processInstanceId='+strs[1]
											            },
											            buttons: [{
											            	icon: 'glyphicon glyphicon-remove',
											                label: '确认',
											                cssClass:'btn-warning',
											                hotkey:13,
											                action: function(dialog){
											                	dialog.close();
											                }
											            }]
											        });
												}
											});
							
								$('#clsButton').click(function(){
									$('#customerName').val('');
									$('#bizType').val('');
									location.href = "worklist/initCandiWorkList.do";
								});
						});


	</script>
	<div
		style="margin: 0 auto; width: 100%; padding-top: 20px; text-align: center; border: 0px solid #555"
		align="center">
		<table width="100%">
				<tr>
					<td width="30%"  style="text-align:left;">
						<div id="findtodo" title="查询列表" class="template" style="width: 100%; text-align: left;" >
							<form id="conditionForm" action="<%=basePath%>worklist/queryCandiWorkList.do"  method="post">
							<table  class="table table-condensed">
								<tr>
									<td width="30%">
										<div class="input-group input-group-sm">
											<span class="input-group-addon" id="basic-addon1">客户名称</span>
											<input id="customerName"  name="customerName" aria-describedby="basic-addon1" class="form-control" type="text" value="${workListCondition.customerName}" placeholder="客户名称...">
										</div>
									</td>
									<td width="30%">
										<div class="input-group input-group-sm">
											<span class="input-group-addon" id="basic-addon2">业务类型</span>
											<input id="bizType"  name="bizType" aria-describedby="basic-addon2" class="form-control" type="text" value="${workListCondition.bizType}" placeholder="业务类型...">
										</div>
									</td>
									<td>
										<div class="btn-group btn-group-sm"  role="group" aria-label="...">
												<button class="btn btn-default" type="submit" >查询</button>
												<button class="btn btn-default" type="button" onclick="$('input[id=clsButton]').click();" >重置</button>
								 				<input id="clsButton" type="button"  value="reset" style="display:none">
										</div>
									</td>
								</tr>
							</table>
							</form>
						</div>
					</td>
				</tr>
		</table>
	</div>
	<div class="table-wrapper">
		<table id="um-table-reload" class="table table-condensed">
			<thead>
				<tr>
					<th><p class="text-primary">#</p></th>
					<th><p class="text-primary">任务Id</p></th>
					<th><p class="text-primary">流程Id</p></th>
					<th><p class="text-primary">任务名称</p></th>
					<th><p class="text-primary">创建时间</p></th>
					<th><p class="text-primary">模板Id</p></th>
					<th><p class="text-primary">节点Id</p></th>
					<th><p class="text-primary">客户名称</p></th>
					<th><p class="text-primary">操作</p></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${workList.actWorkListVOs}" var="actcwl"
					varStatus="status">
					<tr>
						<td>${status.index + 1}</td>
						<td>${actcwl.id}</td>
						<td>${actcwl.executionId}</td>
						<td>${actcwl.name}</td>
						<td><fmt:formatDate value="${actcwl.createTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td>${actcwl.processDefinitionId}</td>
						<td>${actcwl.taskDefinitionKey}</a></td>
						<td>${actcwl.processVariables.customerName}</td>
						<td>
							<div class="btn-group">
								<a name="take,${actcwl.id}" title="签收"><i class="glyphicon glyphicon-ok"></i></a>
								<a name="pic,${actcwl.executionId},${actcwl.processDefinitionId}" title="流程图"><i class="glyphicon glyphicon-eye-open"></i></a>
							</div>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div  class="um-table-page-down">
				<nav>
				  <ul class="pager">
				    <li>
								<a href="worklist/queryCandiWorkList?pageNo=1&customerName=${workListCondition.customerName}&bizType=${workListCondition.bizType}">First</a>
				    </li>
				    <li>
				    			<c:choose>
									<c:when test="${page.pageNo - 1 > 0}">
										<a href="worklist/queryCandiWorkList?pageNo=${page.pageNo - 1}&customerName=${workListCondition.customerName}&bizType=${workListCondition.bizType}">Previous</a>
									</c:when>
									<c:when test="${page.pageNo - 1 <= 0}">
										<a href="worklist/queryCandiWorkList?pageNo=1&customerName=${workListCondition.customerName}&bizType=${workListCondition.bizType}">Previous</a>
									</c:when>
								</c:choose>
				    </li>
				    <li>
				    			<c:choose>
									<c:when test="${page.totalPage==0}">
										<a href="worklist/queryCandiWorkList?pageNo=${page.pageNo}&customerName=${workListCondition.customerName}&bizType=${workListCondition.bizType}">Next</a>
									</c:when>
									<c:when test="${page.pageNo + 1 < page.totalPage}">
										<a href="worklist/queryCandiWorkList?pageNo=${page.pageNo + 1}&customerName=${workListCondition.customerName}&bizType=${workListCondition.bizType}">Next</a>
									</c:when>
									<c:when test="${page.pageNo + 1 >= page.totalPage}">
										<a href="worklist/queryCandiWorkList?pageNo=${page.totalPage}&customerName=${workListCondition.customerName}&bizType=${workListCondition.bizType}">Next</a>
									</c:when>
								</c:choose>
				    </li>
				    <li>
				    			<c:choose>
									<c:when test="${page.totalPage==0}">
										<a href="worklist/queryCandiWorkList?pageNo=${page.pageNo}&customerName=${workListCondition.customerName}&bizType=${workListCondition.bizType}">Last</a>
									</c:when>
									<c:otherwise>
										<a href="worklist/queryCandiWorkList?pageNo=${page.totalPage}&customerName=${workListCondition.customerName}&bizType=${workListCondition.bizType}">Last</a>
									</c:otherwise>
								</c:choose>
				    </li>
				    <li>
				    	<span label label-info>${page.pageNo}/${page.totalPage}</span>
				    </li>
				  </ul>
				</nav>
			</div>	
		
		
		
	</div>
</body>
</html>
