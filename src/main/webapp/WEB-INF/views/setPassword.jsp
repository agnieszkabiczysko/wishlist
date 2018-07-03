<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title><spring:message code="setPassword.title" /></title>
</head>
<body>
	<%@ include file="navigation.jsp"%>
	<jsp:include page="offerHeader.jsp">
		<jsp:param value="Ustawianie hasła" name="title" />
	</jsp:include>

	<section class="container">
		<form:form method="POST" modelAttribute="userPassword"
			class="form-horizontal">
			<fieldset>
				<legend>
					<spring:message code="setPassword.legend" />
				</legend>
				<form:errors path="*" cssClass="alert alert-danger" element="div" />
				<c:if test="${error}">
					<div class="alert alert-danger">
						<spring:message
							code="setPassword.abstractUserDetailsAuthenticationProvider.badPassword" />
						<br />
					</div>
				</c:if>

				<div class="form-group">
					<label class="control-label col-lg-2" for="password"> <spring:message
							code="setPassword.form.password.label" />
					</label>
					<div class="col-lg-10">
						<form:input path="password" id="password" type="password"
							class="form:input-large" />
						<form:errors cssClass="text-danger" />
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-lg-2" for="passwordConfirmation"> <spring:message
							code="setPassword.form.passwordConfirmation.label" />
					</label>
					<div class="col-lg-10">
						<form:input path="passwordConfirmation" id="passwordConfirmation" type="password"
							class="form:input-large" />
						<form:errors cssClass="text-danger" />
					</div>
				</div>

				<div class="form-group">
					<div class="col-lg-offset-2 col-lg-10">
						<input type="submit" id="setPassword" class="btn btn-primary"
							value="Zapisz hasło">
					</div>
				</div>

			</fieldset>
		</form:form>

		<a href="<spring:url value="/offers" />" class="btn btn-default">
			<span class="glyphicon-hand-left glyphicon"></span> <spring:message
				code="setPassword.url.goToOffersPage" />
		</a>

	</section>
</body>