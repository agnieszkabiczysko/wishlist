<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title><spring:message code="wishlists.title" /></title>
</head>
<body>
	<%@ include file="navigation.jsp"%>
	<section>
		<div class="jumbotron">
			<div class="container">
				<h1>
					<spring:message code="wishlists.legend" />
				</h1>
				<c:choose>
					<c:when test="${search}">
						<p>
							<spring:message code="wishlists.user" />
							${user}
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<spring:message code="wishlists.myWishlists" />
						</p>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</section>

	<section class="container">
		<div class="row">

			<c:forEach items="${wishlists}" var="wishlist">
				<div class="col-sm-6 col-md-3" style="padding-bottom: 15px">
					<div class="thumbnail">
						<div class="caption">
							<h4>
								<a href="<spring:url value="/wishes/${wishlist.id}"/>"
									style="color: #333"> ${wishlist.name} </a>
							</h4>
							<p>
								<spring:message code="wishlists.listState" />
								${wishlist.state}
							</p>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
	</section>
</body>
</html>