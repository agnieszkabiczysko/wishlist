<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title><spring:message code="lackOfPermission.title" /></title>
</head>
<body>
	<%@ include file="navigation.jsp"%>
	<jsp:include page="offerHeader.jsp">
		<jsp:param value="Brak uprawnień" name="title" />
	</jsp:include>
	<section>
		<div class="col-md-offset-5 col-md-4">
			<h4>
				<spring:message code="lackOfPermission.information" />
			</h4>
		</div>
	</section>
</body>
</html>