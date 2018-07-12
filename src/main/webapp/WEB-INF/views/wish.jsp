<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<html>
<head>
	<link rel="stylesheet" href="<spring:url value="/resources/styles/offers.css" />" />
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<title>
		<spring:message code="wish.title" />
	</title>
</head>
<body>
	<%@ include file="navigation.jsp"%>
	<jsp:include page="wishlistHeader.jsp">
		<jsp:param value="Szczegóły" name="title" />
	</jsp:include>

	<section class="container">
		<div class="row">
			<div class="col-md-6">
				<img alt="image" src="<c:url value="/images/${wish.offer.id}.png"></c:url>" style="width: 100%" />
			</div>
			<div class="col-sm-6 col-md-6" style="padding-bottom: 15px">
				<div class="thumbnail">
					<div class="caption">
						<h3>${wish.offer.name}</h3>
						<hr>
						<p>
							<b><spring:message code="wish.offerDesription" />:</b>
							${wish.offer.description}
						</p>
						<p>
							<b><spring:message code="wish.offerVendor" /></b>
							${wish.offer.vendor}
						</p>

						<br />

						<security:authorize access="isAuthenticated()">
							<c:choose>
								<c:when test="${isPurchased}">
									<p>
										<b><spring:message code="wish.wishIsPurchased" /></b>
									</p>
									<br>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${canBuy}">
											<a href='<spring:url value="/fulfil/${wish.id}" />'
												class="btn btn-default"> <span
												class="glyphicon glyphicon-heart-empty" /></span> <spring:message
													code="wish.WantsToBuy" />
											</a>
										</c:when>
									</c:choose>
								</c:otherwise>
							</c:choose>

						</security:authorize>

						<a href="<spring:url value="/myWishlists"/>"
							class="btn btn-default"> <span
							class="glyphicon-hand-left glyphicon" /></span> <spring:message
								code="wish.comeBackToWishlists" />
						</a>

					</div>
				</div>
			</div>
		</div>
	</section>

</body>
</html>