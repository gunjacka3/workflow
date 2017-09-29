<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<form id="selectUser-form" >
	<table id="select-user-table-reload" class="table table-condensed">
			<c:forEach items="${nextUsers.selectUserInfos}" var="selectUserInfo" varStatus="status1">
					<tr>
						<td>
							<h4><span class="label label-primary">节点名称</span></h4>
						</td>
						<td>
							<h4>${selectUserInfo.actityName}</h4>
						</td>
					</tr>
					<tr>
						<td>
							<h4><span class="label label-primary">选人类型</span></h4>
						</td>
						<td>
							<h4>${selectUserInfo.selectType}</h4>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<table id="um-table-reload" class="table table-condensed">
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
	</table>
	</form>
