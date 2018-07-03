<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<body>
	<nav class="navbar navbar-default" role="navigation">
		<div class="container-fluid">
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">

					<li role="presentation"><a
						href='<spring:url value="/offers" />'> <spring:message
								code="navigation.homePage" /></a></li>
					<li role="presentation"><a
						href='<spring:url value="/searchOffer" />'> <spring:message
								code="navigation.searchOffer" /></a></li>
					<li role="presentation"><a
						href='<spring:url value="/searchWishlist" />'> <spring:message
								code="navigation.searchWishlist" /></a></li>

					<security:authorize access="isAuthenticated()">
						<li role="presentation"><a href='<spring:url value="/add" />'>
								<spring:message code="navigation.addOffer" />
						</a></li>
						<li role="presentation"><a
							href='<spring:url value="/createWishlist" />'> <spring:message
									code="navigation.createWishlist" /></a></li>
						<li role="presentation"><a
							href='<spring:url value="/myWishlists" />'> <spring:message
									code="navigation.myWishlists" /></a></li>
						<li role="presentation"><a
							href='<spring:url value="/myFriends" />'> <spring:message
									code="navigation.myFriends" />
						</a></li>

					</security:authorize>

				</ul>

				<security:authorize access="isAnonymous()">
					<form action="<c:url value="/register"></c:url>"
						class="navbar-form navbar-right">
						<input id="register" type="submit"
							class="btn btn-default navbar-btn navbar-right"
							value="Zarejestruj się">
					</form>
					<form action="<c:url value="/login"></c:url>"
						class="navbar-form navbar-right">
						<input id="login" type="submit"
							class="btn btn-default navbar-btn navbar-right"
							value="Zaloguj się">
					</form>
				</security:authorize>

				<security:authorize access="isAuthenticated()">
					<form action="<c:url value="/j_spring_security_logout"></c:url>"
						method="post" class="navbar-form navbar-right">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /><input id="logout" type="submit"
							class="btn btn-default navbar-btn navbar-right"
							value="Wyloguj się">
					</form>
				</security:authorize>
			</div>
		</div>
	</nav>
</body>
</html>