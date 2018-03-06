<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page language="java" import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
		i = 1;
		$(document)
				.ready(
						function() {
							$('a').click(function() {
								var strs = $(this).attr('name').split(",");
												if (strs[0] == "detils") {
													var dialogInstance = new BootstrapDialog({
																type:BootstrapDialog.TYPE_INFO,
																title:"流程详情",
																cssClass: 'taskList-dialog',
													            message: function(dialog) {
													                var $message = $('<div></div>');
													                var pageToLoad = dialog.getData('pageToLoad');
													                $message.load(pageToLoad);
													        
													                return $message;
													            },
													            data: {
													                'pageToLoad': '<%=basePath %>hisProcess/view/'+strs[1]+'.do',
													                'pageToLoad2': '<%=basePath %>hisProcess/viewMeeting/'+strs[1]+'.do',
													            },
													            buttons: [{
													            	icon: 'glyphicon glyphicon-ok',
													                label: '确定',
													                cssClass:'btn-warning',
													                hotkey:13,
													                draggable: true,
													                action: function(dialog){
													                	dialog.close();
													                }
													            },{
													            	icon: 'glyphicon glyphicon-ok',
													                label: '最近一次会议记录',
													                cssClass:'btn-warning',
													                hotkey:13,
													                draggable: true,
													                action: function(dialog){
													                	var pageToLoad2 = dialog.getData('pageToLoad2');
													                	var $message = $('<div></div>');
													                	$message.load(pageToLoad2);
													                	dialog.setMessage($message);
													                }
													            },{
													            	icon: 'glyphicon glyphicon-ok',
													                label: '取回',
													                cssClass:'btn-warning',
													                hotkey:13,
													                draggable: true,
													                action: function(dialog){
													                	var selectId=$('input:radio:checked').val();
													                	$.ajax({
													                		url:"task/retrieve/"+selectId+".do",
													                		type:"POST",
													                		dataType:"text",
													                		data:selectId,
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
													                				dialog.close();
													                			}
													                		}
													                		
													                	})
													                }
													            }]
													            
													        });
													dialogInstance.open();
															
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
									$('#templateName').val('');
									location.href = "hisProcess/initList.do";
								});
								

						});
		function openSubProcess(subprocessId){
			BootstrapDialog.show({
				type:BootstrapDialog.TYPE_INFO,
				title:"流程详情",
				cssClass: 'taskList-dialog',
	            message: function(dialog) {
	                var $message = $('<div></div>');
	                var pageToLoad = dialog.getData('pageToLoad');
	                $message.load(pageToLoad);
	        
	                return $message;
	            },
	            data: {
	                'pageToLoad': '<%=basePath %>hisProcess/view/'+subprocessId+'.do',
	                'pageToLoad2': '<%=basePath %>hisProcess/viewMeeting/'+subprocessId+'.do',
	            },
	            buttons: [{
	            	icon: 'glyphicon glyphicon-ok',
	                label: '确定',
	                cssClass:'btn-warning',
	                hotkey:13,
	                draggable: true,
	                action: function(dialog){
	                	dialog.close();
	                }
	            },{
	            	icon: 'glyphicon glyphicon-ok',
	                label: '最近一次会议记录',
	                cssClass:'btn-warning',
	                hotkey:13,
	                draggable: true,
	                action: function(dialog){
	                	var pageToLoad2 = dialog.getData('pageToLoad2');
	                	var $message = $('<div></div>');
	                	$message.load(pageToLoad2);
	                	dialog.setMessage($message);
	                }
	            },{
	            	icon: 'glyphicon glyphicon-ok',
	                label: '取回',
	                cssClass:'btn-warning',
	                hotkey:13,
	                draggable: true,
	                action: function(dialog){
	                	var selectId=$('input:radio:checked').val();
	                	$.ajax({
	                		url:"task/retrieve/"+selectId+".do",
	                		type:"POST",
	                		dataType:"text",
	                		data:selectId,
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
	                				dialog.close();
	                			}
	                		}
	                		
	                	})
	                }
	            }]
	            
	        });
		};
	</script>
	
	<div
		style="margin: 0 auto; width: 100%; padding-top: 20px; text-align: center; border: 0px solid #555"
		align="center">
		<table width="100%">
				<tr>
				<td width="30%"  style="text-align:left;">
					<div id="findTemplate" title="查询模型" class="template" style="width: 80%; text-align: left;" >
						<form id="conditionHisForm" action="<%=basePath%>hisProcess/showList.do" method="post">
							<div class="input-group input-group-sm">
								<input id="templateName"  name="templateName" class="form-control" type="text" value="${templateName}" placeholder="模板名称...">
								<span class="input-group-btn">
									<button class="btn btn-default" type="submit" >查询</button>
					 			</span>
								<span class="input-group-btn">
									<button class="btn btn-default" type="button" onclick="$('input[id=clsButton]').click();" >重置</button>
					 			</span>
					 			<input id="clsButton" type="button"  value="reset" style="display:none">
							</div>
						</form>
					</div>
				</td>
				</tr>
	</table>
	</div>	
	
	
	
	
	<hr>
	<div class="table-wrapper">
		<table id="um-table-reload" class="table table-condensed">
			<thead>
				<tr>
					<th><p class="text-primary">#</p></th>
					<th><p class="text-primary">id值</p></th>
					<th><p class="text-primary">流程名称</p></th>
					<th><p class="text-primary">模板</p></th>
					<th><p class="text-primary">业务主键</p></th>
					<th><p class="text-primary">开始时间</p></th>
					<th><p class="text-primary">结束时间</p></th>
					<th><p class="text-primary">处理时间 </p></th>
					<th><p class="text-primary">发起用户 </p></th>
					<th><p class="text-primary">结束或撤销</p></th>
					<th><p class="text-primary">子流程</p></th>
					<th><p class="text-primary">操作</p></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${templates.actHisProcessListVOs}" var="actdf"
					varStatus="status">
					<c:choose>
						<c:when test="${empty actdf.deleteReason}">
							<tr>
						</c:when>
						<c:otherwise>
							<tr class="warning">
						</c:otherwise>
					</c:choose>
						<td>${status.index + 1}</td>
						<td>${actdf.id}</td>
						<td>${actdf.name}</td>
						<td>${actdf.processDefinitionId}</td>
						<td>${actdf.businessKey}</td>
						<td><fmt:formatDate value="${actdf.startTime}" type="both"/></td>
						<td><fmt:formatDate value="${actdf.endTime}" type="both"/></td>
						<td>
						${actdf.durationInMillis}
						</td>
						<td>${actdf.startUserId}</td>
						<td>
							<c:choose>
								<c:when test="${empty actdf.endTime}">
									<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
								</c:when>
								<c:otherwise>
									<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:choose>
								<c:when test="${empty actdf.superProcessInstanceId}">
									<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
								</c:when>
								<c:otherwise>
									<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<div class="btn-group">
								<a name="detils,${actdf.id}" title="详情"><i class="glyphicon glyphicon-stats"></i></a>
								<a name="pic,${actdf.id},${actdf.processDefinitionId}" title="流程图"><i class="glyphicon glyphicon-eye-open"></i></a>
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
							<a href="proDef/showList.do?pageNo=1&templateName=${templateName}">First</a>
			    </li>
			    <li>
			    			<c:choose>
								<c:when test="${page.pageNo - 1 > 0}">
									<a href="proDef/showList.do?pageNo=${page.pageNo - 1}&templateName=${templateName}">Previous</a>
								</c:when>
								<c:when test="${page.pageNo - 1 <= 0}">
									<a href="proDef/showList.do?pageNo=1&templateName=${templateName}">Previous</a>
								</c:when>
							</c:choose>
			    </li>
			    <li>
			    			<c:choose>
								<c:when test="${page.totalPage==0}">
									<a href="proDef/showList.do?pageNo=${page.pageNo}&templateName=${templateName}">Next</a>
								</c:when>
								<c:when test="${page.pageNo + 1 < page.totalPage}">
									<a href="proDef/showList.do?pageNo=${page.pageNo + 1}&templateName=${templateName}">Next</a>
								</c:when>
								<c:when test="${page.pageNo + 1 >= page.totalPage}">
									<a href="proDef/showList.do?pageNo=${page.totalPage}&templateName=${templateName}">Next</a>
								</c:when>
							</c:choose>
			    </li>
			    <li>
			    			<c:choose>
								<c:when test="${page.totalPage==0}">
									<a href="proDef/showList.do?pageNo=${page.pageNo}&templateName=${templateName}">Last</a>
								</c:when>
								<c:otherwise>
									<a href="proDef/showList.do?pageNo=${page.totalPage}&templateName=${templateName}">Last</a>
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
