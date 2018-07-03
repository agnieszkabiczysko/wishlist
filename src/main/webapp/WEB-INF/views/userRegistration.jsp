<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title><spring:message code="userRegistration.title" /></title>
</head>
<body>
	<%@ include file="navigation.jsp"%>
	<jsp:include page="offerHeader.jsp">
		<jsp:param value="Rejestracja użytkownika" name="title" />
	</jsp:include>

	<section class="container">
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							<spring:message code="userRegistration.panel.title" />
						</h3>
					</div>
					<div class="panel-body">
						<form:form method="POST" modelAttribute="user"
							class="form-horizontal">
							<form:input type="hidden" path="id" id="id" />
							<fieldset>

								<form:errors path="*" cssClass="alert alert-danger"
									element="div" />
								<c:if test="${userNameAlreadyExists}">
									<div class="alert alert-danger">
										<spring:message
											code="user.Registration.abstractUserDetailsAuthenticationProvider.idAlreadyExists" />
										<br />
									</div>
								</c:if>

								<div class="form-group">
									<label class="control-label col-lg-3" for="userId"> <spring:message
											code="userRegistration.form.userId.label" />
									</label>
									<div class="col-lg-4">
										<form:input path="userId" id="userId" type="text"
											class="form:input-large" />
										<form:errors path="userId" cssClass="text-danger" />
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-lg-3" for="firstname">
										<spring:message code="userRegistration.form.firstname.label" />
									</label>
									<div class="col-lg-4">
										<form:input path="firstname" id="firstname" type="text"
											class="form:input-large" />
										<form:errors path="firstname" cssClass="text-danger" />
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-lg-3" for="lastname"> <spring:message
											code="userRegistration.form.lastname.label" />
									</label>
									<div class="col-lg-4">
										<form:input path="lastname" id="lastname" type="text"
											class="form:input-large" />
										<form:errors path="lastname" cssClass="text-danger" />
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-lg-3" for="email"> <spring:message
											code="userRegistration.form.email.label" />
									</label>
									<div class="col-lg-4">
										<form:input path="email" id="email" type="text"
											class="form:input-large" />
										<form:errors path="email" cssClass="text-danger" />
									</div>
								</div>

								<div class="form-group">
									<div class="col-lg-offset-5 col-lg-10">
										<input type="submit" id="registerSubmit"
											class="btn btn-primary" value="Zarejestruj">
									</div>
								</div>

							</fieldset>
						</form:form>

						<a href="<spring:url value="offers" />" class="btn btn-default">
							<span class="glyphicon-hand-left glyphicon"></span> <spring:message
								code="userReistration.url.goToOffersPage" />
						</a>
					</div>
				</div>
			</div>
		</div>
	</section>

</body>