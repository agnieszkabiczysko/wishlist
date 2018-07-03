<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<html>
<head>

<link rel="stylesheet"
	href="<spring:url value="/resources/styles/offers.css" />" />
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title><spring:message code="offer.title" /></title>
</head>
<body>
	<%@ include file="navigation.jsp"%>
	<jsp:include page="offerHeader.jsp">
		<jsp:param value="Szczegóły oferty" name="title" />
	</jsp:include>

	<section class="container">
		<div class="row">
			<div class="col-md-6">
				<img alt="image"
					src="<c:url value="/images/${offer.id}.png"></c:url>"
					style="width: 100%" />
			</div>
			<div class="col-sm-6 col-md-6" style="padding-bottom: 15px">
				<div class="thumbnail">
					<div class="caption">
						<h3>${offer.name}</h3>
						<hr>
						<p>
							<b><spring:message code="offer.offerDescritpion" /></b>
							${offer.description}
						</p>
						<p>
							<b><spring:message code="offer.offerVendor" /></b>
							${offer.vendor}
						</p>
						<br /> <a href="<spring:url value="/offers"/>"
							class="btn btn-default"> <span
							class="glyphicon-hand-left glyphicon" /></span> <spring:message
								code="offer.url.goToOffersPage" />
						</a>

						<security:authorize access="isAuthenticated()">

							<c:choose>
								<c:when test="${canManage}">
									<a href='<spring:url value="/edit/${offer.id}" />'
										class="btn btn-default"> <span
										class="glyphicon glyphicon-edit" /></span> <spring:message
											code="offer.url.goToEditPage" />
									</a>
								</c:when>
							</c:choose>


							<br>
							<br>
							<c:choose>
								<c:when test="${userHasCurrentWishlist}">
									<a href='<spring:url value="/markWish/${offer.id}" />'
										class="btn btn-default"> <span
										class="glyphicon glyphicon-plus" /></span> <spring:message
											code="offer.url.addtoCurrentWishlist" />
									</a>
								</c:when>
							</c:choose>


							<!-- Trigger the modal with a button -->
							<button id="wishlistChoiceButton" type="button"
								class="btn btn-default" data-toggle="modal"
								data-target="#myModal">
								<span class="glyphicon glyphicon-th-list"></span>
								<spring:message code="offer.url.addtoChodenWishlist" />
							</button>


							<!-- Modal -->
							<div id="myModal" class="modal fade" role="dialog">
								<div class="modal-dialog">

									<!-- Modal content-->
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal">&times;</button>
											<h4 class="modal-title">
												<spring:message code="offer.modal.title" />
											</h4>
										</div>
										<div class="modal-body">
											<form method="POST" class="form-horizontal"
												action='<spring:url value="/selectWishlist/${offer.id}" />'>
												<input type="hidden" name="${_csrf.parameterName}"
													value="${_csrf.token}" />
												<fieldset class="form-check">

													<c:forEach items="${wishlists}" var="wishlist">
														<div class="form-check">
															<label class="form-check-label"> <input
																type="radio" class="form-check-input" name="wishlistId"
																id="${wishlist.name}" value="${wishlist.id}" checked>
																${wishlist.name}
															</label>
														</div>
													</c:forEach>

												</fieldset>
												<div class="modal-footer">
													<button id="closeButton" type="button"
														class="btn btn-default" data-dismiss="modal">
														<spring:message code="offer.modal.close.button" />
													</button>
													<button id="submitButton" type="submit"
														class="btn btn-primary">
														<spring:message code="offer.modal.save.button" />
													</button>
												</div>
											</form>
										</div>

									</div>

								</div>
							</div>

						</security:authorize>

					</div>
				</div>
			</div>
		</div>
	</section>
</body>
</html>
