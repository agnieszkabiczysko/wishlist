<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title><spring:message code="createWishlist.title" /></title>
</head>
<body>
	<%@ include file="navigation.jsp"%>
	<jsp:include page="wishlistHeader.jsp">
		<jsp:param value="Stwórz swoją listę życzeń" name="title" />
	</jsp:include>

	<section class="container">
		<form:form method="POST" modelAttribute="wishlist"
			class="form-horizontal">
			<fieldset>
				<legend>
					<spring:message code="createWishlist.legend" />
				</legend>
				<form:errors path="*" cssClass="alert alert-danger" element="div" />
				<form:input type="hidden" path="id" id="id" />
				<div class="form-group">
					<label class="control-label col-lg-2" for="name"> <spring:message
							code="createWishlist.form.name.label" />
					</label>
					<div class="col-lg-10">
						<form:input path="name" id="name" type="name"
							class="form:input-large" />
						<form:errors cssClass="text-danger" />
					</div>
				</div>
			</fieldset>

			<fieldset class="form-check">
				<label class="control-label col-lg-2" for="name"> <spring:message
						code="createWishlist.formCheck.title" /> &nbsp;
				</label><br>
				<br>
				<div class="form-check" style="text-indent: 2.5cm">
					<label class="form-check-label"> <form:radiobutton
							id="publicState" path="state" value="PUBLIC"
							class="form-check-input" /> <spring:message
							code="createWishlist.formCheck.public.label" />
					</label>
				</div>
				<div class="form-check" style="text-indent: 2.5cm">
					<label class="form-check-label"> <form:radiobutton
							id="sharedState" path="state" value="SHARED"
							class="form-check-input" /> <spring:message
							code="createWishlist.formCheck.shared.label" />
					</label>
				</div>
				<div class="form-check" style="text-indent: 2.5cm">
					<label class="form-check-label"> <form:radiobutton
							id="privateState" path="state" value="PRIVATE"
							class="form-check-input" /> <spring:message
							code="createWishlist.formCheck.private.label" />
					</label>
				</div>
				<br>
			</fieldset>

			<div style="text-indent: 5cm">
				<button type="submit" id="btnCreateWishlist" class="btn btn-default"
					value="Stwórz">
					<span class="glyphicon glyphicon-ok" /></span>
					<spring:message code="createWishlist.create.button" />
				</button>
			</div>
		</form:form>
	</section>
</body>
</html>