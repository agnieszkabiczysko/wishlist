<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title><spring:message code="emailSent.title" /></title>
</head>
<body>
	<%@ include file="navigation.jsp"%>
	<jsp:include page="offerHeader.jsp">
		<jsp:param value="Rejestracja w toku" name="title" />
	</jsp:include>

	<section class="container">
		<form method="POST" class="form-horizontal">
			<fieldset>
				<legend>
					<spring:message code="emailSent.legend" />
				</legend>
				<c:choose>
					<c:when test="${inactiveMail}">
						<p>
							<spring:message code="emailSent.inactiveMail" />
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<spring:message code="emailSent.informactionAboutLink" />
						</p>
					</c:otherwise>
				</c:choose>
				<p>
					<spring:message code="emailSent.resendMail" />
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<button type="submit" id="btnSendMail" class="btn btn-default"
						value="Wyślij ponownie">
						<span class="glyphicon glyphicon-send"></span>
						<spring:message code="emailSent.sendAgain" />
					</button>
				</p>
			</fieldset>
		</form>

		<a href="<spring:url value="/offers" />" class="btn btn-default">
			<span class="glyphicon-hand-left glyphicon"></span> <spring:message
				code="emailSent.goToOffersPage" />
		</a>
	</section>
</body>
</html>