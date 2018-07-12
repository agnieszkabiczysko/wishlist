<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title><spring:message code="addNewOffer.title" /></title>
</head>
<body>
	<%@ include file="navigation.jsp"%>
	<jsp:include page="offerHeader.jsp">
		<jsp:param value="Oferty" name="title" />
	</jsp:include>
	
	<section>
		<div class="jumbotron">
			<div class="container">
				<c:choose>
					<c:when test="${edit}">
						<p>
							<spring:message code="addNewOffer.update.decription" />
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<spring:message code="addNewOffer.add.decription" />
						</p>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</section>
	<section class="container">
		<spring:url value="/add" var="newOfferUrl" />
		<form:form action="${newOfferUrl}?${_csrf.parameterName}=${_csrf.token}" method="post" modelAttribute="offer"
			class="form-horizontal" enctype="multipart/form-data">
			<form:input type="hidden" path="id" id="id" />
			<fieldset>
				<legend>
					<spring:message code="addNewOffer.legend" />
				</legend>
				<form:errors path="*" cssClass="alert alert-danger" element="div" />

				<div class="form-group">
					<label class="control-label col-lg-2" for="name"> <spring:message
							code="addNewOffer.form.name.label" />
					</label>
					<div class="col-lg-10">
						<form:input path="name" id="name" type="text"
							class="form:input-large" />
						<form:errors path="name" cssClass="text-danger" />
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-lg-2" for="description"> <spring:message
							code="addNewOffer.form.description.label" />
					</label>
					<div class="col-lg-10">
						<form:input path="description" id="description" type="text"
							class="form:input-large" />
						<form:errors path="description" cssClass="text-danger" />
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-lg-2" for="vendor"> <spring:message
							code="addNewOffer.form.vendor.label" />
					</label>
					<div class="col-lg-10">
						<form:input path="vendor" id="vendor" type="text"
							class="form:input-large" />
						<form:errors path="vendor" cssClass="text-danger" />
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-lg-2" for="image"> <spring:message
							code="addNewOffer.form.image.label" />
					</label>
					<div class="col-lg-10">
						<form:input path="image" id="image" type="file"
							class="form:input-large" />
					</div>
				</div>



				<div class="form-group">
					<div class="col-lg-offset-2 col-lg-10">
						<c:choose>
							<c:when test="${edit}">
								<input type="submit" id="updateOffer" class="btn btn-primary"
									value="Aktualizuj" />
							</c:when>
							<c:otherwise>
								<input type="submit" id="addOffer" class="btn btn-primary"
									value="Dodaj" />
							</c:otherwise>
						</c:choose>
					</div>
				</div>

			</fieldset>
		</form:form>

		<a href="<spring:url value="/offers" />" class="btn btn-default">
			<span class="glyphicon-hand-left glyphicon"></span> <spring:message
				code="addNewOffer.url.goToOffersPage" />
		</a>
	</section>
</body>
</html>