<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="zh-cn">
<head>
<%@ include file="/inc/inc.jsp"%>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <%-- <link rel="shortcut icon" href="${_baasBase }/images/citic.ico"> --%>
    <meta name="viewport" content="width=device-width; initial-scale=1;" />
    <title>南京国磐公众行业务运营支撑平台登录</title>
    <link href="${pageContext.request.contextPath}/theme/login/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/theme/login/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/theme/login/css/comm.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/theme/login/css/custom.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="${_baasBase }/js/datacheck.js" ></script>
	<script language="javascript" src="${pageContext.request.contextPath}/resources/spm_modules/app/login/messenger.js"></script>  
	<script language="javascript" src="${pageContext.request.contextPath}/resources/spm_modules/app/login/casLoginView.js"></script> 
	<script language="javascript" src="${pageContext.request.contextPath}/theme/login/js/login.js"></script> 

</head>
<body id="body">
	<div class="container bgs">
		<div class="logo">
			<img src="${pageContext.request.contextPath}/theme/login/imgs/logo.png">
			<h2>南京国磐公众行业务运营支撑平台</h2>
		</div>
		<form:form method="post" id="fm1" name="fm1" commandName="${commandName}" htmlEscape="true" style="height:50%;margin-top:4.2%;">
			<div class="login-wrap">
				<div class="login">
					<div class="login-top">
						欢迎登录系统
					</div>
					<div class="login-bottom">
						<div class="login-note" id="errorMsg" style="color:red;">
							<form:errors path="*" id="msg" cssClass="errors" element="div" htmlEscape="false" />
						</div>
						<div class="user">
							<p>用户名</p>
							<form:input cssErrorClass="error" id="username" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="off" htmlEscape="true" />
						</div>
						<div class="password">
							<p>密码</p>
							<form:password cssErrorClass="error" id="password" size="25" tabindex="2" path="password" accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" onkeydown="encryptPwd(event)" />
						</div>
						<input type="button" class="btn btn-info btn-zhuce" value="登录系统" accesskey="l" tabindex="4" onclick="javascript:dologin();">
					</div>
				</div>
			</div>
			<!-- <div class="login-user">
				<div class="user-center">
					<div class="user-list">
						<ul>
							<div class="login-note" id="errorMsg" style="color:red;">
								<form:errors path="*" id="msg" cssClass="errors" element="div" htmlEscape="false" />
							</div>
							<li class="mt-30">
								<form:input cssClass="int-ntext" cssErrorClass="error" id="username" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="off" htmlEscape="true"/><i class="icon iconfont">&#xe60c;</i></li>
							<li>
								<form:password cssClass="int-ntext" cssErrorClass="error" id="password" size="25" tabindex="2" path="password" accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" onkeydown="encryptPwd(event)" /><i class="icon iconfont">&#xe609;</i></li>
							<li class="pasw">
								<p><input id="rememberMe" name="rememberMe" type="checkbox" />自动登录</p>
							</li>
						</ul>
					</div>
					<div class="nwbtn">
						<input type="button" class="nlogin-btn" value="登 录" accesskey="l" tabindex="4" onclick="javascript:dologin();">
					</div>
				</div>
			</div> -->
			<input type="hidden" id="errorNum" name="errorNum" value="${errorNum}">
			<input type="hidden" name="lt" value="${loginTicket}" />
			<input type="hidden" name="execution" value="${flowExecutionKey}" />
			<input type="hidden" name="_eventId" value="submit" />
			<input type="hidden" name="sessionId" value="<%=request.getSession().getId()%>" />
		</form:form>
		<p class="tips">技术支持：北京东方国信科技股份有限公司</p>
	</div>
</body>
</html>
