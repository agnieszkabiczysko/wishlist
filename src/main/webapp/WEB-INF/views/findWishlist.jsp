<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title><spring:message code="findWishlist.title" /></title>
</head>
<body>
	<%@ include file="navigation.jsp"%>
	<jsp:include page="wishlistHeader.jsp">
		<jsp:param value="Wyszukaj listy życzeń" name="title" />
	</jsp:include>

	<section class="container">
		<form class="form-horizontal">
			<fieldset>
				<legend>
					<spring:message code="findWishlist.legend" />
				</legend>
				<div class="form-group">
					<label class="control-label col-lg-2" for="searchFor"> <spring:message
							code="findWishlist.searchWishlist.searchFor.label" />
					</label>
					<div class="col-lg-10">
						<input id="searchFor" name="searchFor" type="text"
							class="form-control" />
					</div>
				</div>
			</fieldset>
			<fieldset class="form-check">
				<label class="control-label col-lg-2" for="name"> <spring:message
						code="findWishlist.searchWishlist.searchType.label" /> &nbsp;
				</label><br>
				<br>
				<div class="form-check" style="text-indent: 2.5cm">
					<label class="form-check-label"> <input type="radio"
						class="form-check-input" name="searchType" id="optionUser"
						value="USERID" checked> <spring:message
							code="findWishlist.searchWishlist.formCheck.searchType.user" />
					</label>
				</div>
				<div class="form-check" style="text-indent: 2.5cm">
					<label class="form-check-label"> <input type="radio"
						class="form-check-input" name="searchType" id="optionEmail"
						value="EMAIL"> <spring:message
							code="findWishlist.searchWishlist.formCheck.searchType.email" />
					</label>
				</div>
				<br>
				<div style="text-indent: 5cm">
					<button type="submit" id="btnSearch" class="btn btn-default"
						value="Szukaj">
						<span class="glyphicon glyphicon-search" /></span>
						<spring:message code="findWishlist.searchWishlist.button.label" />
					</button>
				</div>
			</fieldset>
		</form>

	</section>
</body>
</html>