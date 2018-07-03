<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title><spring:message code="error.title" /></title>
</head>
<body>
	<%@ include file="navigation.jsp"%>
	<jsp:include page="wishlistHeader.jsp">
		<jsp:param value="Brak wyników" name="title" />
	</jsp:include>
	<section>
		<div class="col-md-offset-5 col-md-4">
			<h4>
				<spring:message code="noResultsFound.information" />
			</h4>
		</div>
	</section>
	<a href="<spring:url value="/searchWishlist" />"
		class="btn btn-default"> <span
		class="glyphicon-hand-left glyphicon"></span> <spring:message
			code="noResultsFound.url.goToSearchPage" />
	</a>
</body>
</html>