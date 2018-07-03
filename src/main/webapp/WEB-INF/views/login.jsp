<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title><spring:message code="login.title" /></title>
</head>
<body>
	<%@ include file="navigation.jsp"%>
	<jsp:include page="offerHeader.jsp">
		<jsp:param value="Logowanie" name="title" />
	</jsp:include>

	<section class="container">
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							<spring:message code="login.information" />
						</h3>
					</div>
					<div class="panel-body">
						<c:if test="${logout}">
							<div class="alert alert-danger">
								<spring:message code="login.logout.information" />
								<br />
							</div>
						</c:if>
						<c:if test="${not empty error}">
							<div class="alert alert-danger">
								<spring:message
									code="login.abstractUserDetailsAuthenticationProvider.badCredentials" />
								<br />
							</div>
						</c:if>
						<form action="<c:url value="/j_spring_security_check"></c:url>"
							method="post">
							<fieldset>
								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" />
								<div class="form-group">
									<input class="form-control" placeholder="Nazwa użytkownika"
										name='j_username' type="text">
								</div>
								<div class="form-group">
									<input class="form-control" placeholder="Hasło"
										name='j_password' type="password" value="">
								</div>
								<input class="btn btn-lg btn-success btn-clock" name='j_submit'
									type="submit" value="Zaloguj się">
							</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>

	</section>
</body>
</html>