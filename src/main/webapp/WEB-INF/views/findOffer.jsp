<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title><spring:message code="findOffer.title" /></title>
</head>
<body>
	<%@ include file="navigation.jsp"%>
	<jsp:include page="offerHeader.jsp">
		<jsp:param value="Wyszukaj ofertę" name="title" />
	</jsp:include>

	<section class="container">
		<form class="form-horizontal">
			<fieldset>
				<legend>
					<spring:message code="findOffer.legend" />
				</legend>
				<div class="form-group">
					<label class="control-label col-lg-2" for="searchFor"> <spring:message
							code="findOffer.searchOffer.searchFor.label" />
					</label>
					<div class="col-lg-10">
						<input id="searchFor" name="searchFor" type="text"
							class="form-control" />
					</div>
				</div>
			</fieldset>
			<fieldset class="form-check">
				<label class="control-label col-lg-2" for="searchType"> <spring:message
						code="findOffer.searchOffer.searchType.label" /> &nbsp;
				</label><br>
				<br>
				<div class="form-check" style="text-indent: 2.5cm">
					<label class="form-check-label"> <input type="radio"
						class="form-check-input" name="searchType" id="optionName"
						value="name" checked> <spring:message
							code="findOffer.searchOffer.searchType.name.label" />
					</label>
				</div>
				<div class="form-check" style="text-indent: 2.5cm">
					<label class="form-check-label"> <input type="radio"
						class="form-check-input" name="searchType" id="optionVendor"
						value="vendor"> <spring:message
							code="findOffer.searchOffer.searchType.vendor.label" />
					</label>
				</div>
				<br>
				<div style="text-indent: 5cm">
					<button type="submit" id="btnSearch" class="btn btn-default"
						value="Szukaj">
						<span class="glyphicon glyphicon-search" /></span>
						<spring:message code="findOffer.searchOffer.button.label" />
					</button>
				</div>
			</fieldset>
		</form>

		<a href="<spring:url value="/offers" />" class="btn btn-default">
			<span class="glyphicon-hand-left glyphicon"></span> <spring:message
				code="findOffer.url.goToOffersPage" />
		</a>

	</section>
</body>
</html>