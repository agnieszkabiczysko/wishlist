<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title><spring:message code="friends.title" /></title>
</head>
<body>
	<%@ include file="navigation.jsp"%>
	<jsp:include page="wishlistHeader.jsp">
		<jsp:param value="Moi przyjaciele to" name="title" />
	</jsp:include>

	<section class="container">
		<div class="row">
			<c:forEach items="${friends}" var="friend">
				<div class="col-sm-6 col-md-3" style="padding-bottom: 15px">
					<div class="thumbnail">
						<div class="caption">
							<h4>
								<a href="<spring:url value="/wishlists?userId=${friend.id}"/>"
									style="color: #333">${friend.userId} </a>
							</h4>
						</div>
					</div>
				</div>
			</c:forEach>

		</div>
	</section>
</body>
</html>