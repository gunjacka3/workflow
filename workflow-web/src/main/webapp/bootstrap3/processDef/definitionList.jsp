<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page language="java" import="java.util.*"%>
<%@ page import="cn.com.workflow.common.Page"%>
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
</head>

<body>
	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							$('a').click(function() {
								var strs = $(this).attr('name').split(",");
												
												if (strs[0] == "remove") {
															BootstrapDialog.confirm({
													            title: '警告',
													            message: '取消部署会清除运行流程数据，确定取消部署吗?',
													            type: BootstrapDialog.TYPE_WARNING,
													            closable: true, 
													            draggable: true, 
													            btnCancelLabel: '拒绝', 
													            btnOKLabel: '同意', 
													            btnOKClass: 'btn-warning', 
													            callback: function(result) {
													            	if(result) {
														            	$.ajax({
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
														            }else {
														                
														            }
													            }
													        });		
															
															
												}else if(strs[0] =="pic"){
													var $textAndPic = $('<div style="height:300px; overflow:auto"></div>');
													var url="<%=basePath%>proDef/pic/"+strs[1]+ ".do"
											        $textAndPic.append('<img src='+url+' />');
													BootstrapDialog.show({
														type:BootstrapDialog.TYPE_INFO,
														title:"流程图查看",
														cssClass: 'wide-dialog',
														closable:false,
														message:$textAndPic,
											            buttons: [{
											            	icon: 'glyphicon glyphicon-ok',
											                label: '确定',
											                cssClass:'btn-warning',
											                hotkey:13,
											                draggable: true,
											                action: function(dialog){
											                			dialog.close();
											                	}
											            }]
											        });
												}else if(strs[0]=="test"){
													BootstrapDialog.show({
														type:BootstrapDialog.TYPE_INFO,
														title:"发起测试",
														cssClass: 'login-dialog',
											            message: function(dialog) {
											                var $message = $('<div></div>');
											                var pageToLoad = dialog.getData('pageToLoad');
											                $message.load(pageToLoad);
											        
											                return $message;
											            },
											            data: {
											                'pageToLoad': '<%=basePath %>proDef/start/'+strs[1]+'/'+strs[2]+'.do'
											            },
											            buttons: [{
											            	icon: 'glyphicon glyphicon-ok',
											                label: '确定',
											                cssClass:'btn-warning',
											                hotkey:13,
											                draggable: true,
											                action: function(dialog){
																if(""!=$('#create-process-form').serialize()){
																	json_data=$('#create-process-form').serialize();
																	json_data = decodeURIComponent(json_data,true);
																}
											                	$.ajax({
											                		url:"process/create.do",
											                		type:"POST",
											                		dataType:"text",
											                		data:json_data,
											                		contentType : "text/plain",
											                		success:function(data){
											                			dialog.close();
											                		}
											                		
											                	})
											                }
											            },{
											            	icon: 'glyphicon glyphicon-remove',
											                label: '取消',
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
									location.href = "proDef/initDefinition.do";
								});
						});

	</script>
	<div
		style="margin: 0 auto; width: 100%; padding-top: 20px; text-align: center; border: 0px solid #555"
		align="center">
		<table width="100%">
				<tr>
				<td width="30%"  style="text-align:left;">
					<div id="findTemplate" title="查询模型" class="template" style="width: 80%; text-align: left;" >
						<form action="proDef/showList.do" method="get">
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
				<td align="right">
					<button type="button" class="btn btn-info btn-sm" onclick="javascript:window.open('<%=basePath %>proDef/download.do')">
  						<span class="glyphicon glyphicon-download-alt"></span>&nbsp;模板最终版本
					</button>
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
					<th><p class="text-primary">版本号</p></th>
					<th><p class="text-primary">模板文件</p></th>
					<th><p class="text-primary">模板主键</p></th>
					<th><p class="text-primary">部署时间</p></th>
					<th><p class="text-primary">是否激活</p></th>
					<th><p class="text-primary">操作</p></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${templates.actDefinitionVOs}" var="actdf"
					varStatus="status">
					<c:choose>
						<c:when test="${actdf.suspensionState!='1'}">
							<tr class="warning">
						</c:when>
						<c:otherwise>
							<tr>
						</c:otherwise>
					</c:choose>
						<td>${status.index + 1}</td>
						<td>${actdf.id}</td>
						<td>${actdf.name}</td>
						<td>V:${actdf.version}</a></td>
						<td><a href="<%=basePath %>proDef/export/${actdf.id}/bpmn.do"
							target="_blank"><abbr title="点击下载"><kbd>xml</kbd></abbr></a></td>
						<td>${actdf.key}&nbsp;&nbsp;<span class="badge primary-bg radius-all-100">${actdf.keysCount}</span></td>
						<td><fmt:formatDate value="${actdf.deploymentTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td>
							<c:choose>
								<c:when test="${actdf.suspensionState!='1'}">
									<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
								</c:when>
								<c:otherwise>
									<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<div class="btn-group">
								<a name="export,${actdf.id}" title="导出"><i class="glyphicon glyphicon-share-alt"></i></a>
								<a name="remove,${actdf.deploymentId}" title="取消部署"><i class="glyphicon glyphicon-remove-sign"></i></a>
								<a name="pic,${actdf.id}" title="流程图"><i class="glyphicon glyphicon-eye-open"></i></a>
								<a name="test,${actdf.id},${actdf.key}" title="测试"><i class="glyphicon glyphicon-pencil"></i></a>
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
