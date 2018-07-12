<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title><spring:message code="wishes.title" /></title>
</head>
<body>
	<%@ include file="navigation.jsp"%>
	<jsp:include page="wishlistHeader.jsp">
		<jsp:param value="Życzenia użytkownika: ${user.userId}" name="title" />
	</jsp:include>

	<section class="container">

		<c:if test="${error}">
			<div class="alert alert-danger">
				<spring:message code="wishes.shareWishlist.badEmailAddress" />
				<br />
			</div>
		</c:if>

		<div class="row">
			<c:forEach items="${wishes}" var="wish">
				<div class="col-sm-6 col-md-3" style="padding-bottom: 15px">
					<div class="thumbnail">
						<img alt="image"
							src="<c:url value="/images/${wish.offer.id}.png"></c:url>"
							style="width: 100%" />
						<div class="caption">
							<h4>
								<a href="<spring:url value="/wish/${wish.id}" />" style="color: #333">${wish.offer.name} </a>
							</h4>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
	</section>

	<section class="container">

		<c:choose>

			<c:when test="${(wishlistIsEmpty && isWisher)}">
				<p>
					<spring:message code="wishes.emptyWishlist.informationForUser" />
					<a href="<spring:url value="/offers" />" class="btn btn-default">
						<span class="glyphicon-hand-left glyphicon"></span> <spring:message
							code="wishes.url.goToOffersPage" />
					</a>
				</p>
			</c:when>

			<c:when test="${(wishlistIsEmpty && !isWisher)}">
				<p>
					<spring:message code="wishes.emptyWishlist.information" />
				</p>
			</c:when>

			<c:when test="${(!wishlistIsEmpty && isWisher)}">
				<br>
				<br>
				<form:form method="post" modelAttribute="shareEmail"
					action="${pageContext.request.contextPath}/shareWishlist/${wishlistId}"
					class="form-horizontal">
					<fieldset>

						<form:errors path="*" cssClass="alert alert-danger" element="div" />
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" name="id"
							value="id" />

						<div class="form-group">
							<label class="control-label col-lg-2" for="email"> <spring:message
									code="wishes.share.email.label" />
							</label>
							<div class="col-lg-6">
								<form:input path="email" id="email" type="text"
									class="form-control" />
								<form:errors path="*" cssClass="alert alert-danger"
									element="div" />
							</div>
							<div>
								<button type="submit" id="submitSendList"
									class="btn btn-default" value="Wyślij">
									<span class="glyphicon glyphicon-send" /></span>
									<spring:message code="wishes.sendWishlist.button" />
								</button>
							</div>
						</div>
					</fieldset>
				</form:form>
			</c:when>
		</c:choose>

	</section>

</body>
</html>
