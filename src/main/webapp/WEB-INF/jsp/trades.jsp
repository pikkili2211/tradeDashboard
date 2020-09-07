<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Trade Dashboard</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
	<div align="center" class="container">
	
		<div>			
			<hr class="my-4">
			<h3>Trades</h3>
		</div>
		<table class="table table-striped">

			<tr>
				<th>Trade Id</th>
				<th>Version</th>
				<th>Counter-Party Id</th>
				<th>Book-Id</th>
				<th>Maturity Date</th>
				<th>Created Date</th>
				<th>Expired</th>
			</tr>
			<c:forEach var="trade" items="${trades}">
				<tr>
					<td><c:out value="${trade.tradeId}" /></td>
					<td><c:out value="${trade.version}" /></td>
					<td><c:out value="${trade.counterPartyId}" /></td>
					<td><c:out value="${trade.bookId}" /></td>
					<td><c:out value="${trade.maturityDate}" /></td>
					<td><c:out value="${trade.createdDate}" /></td>
					<td><c:out value="${trade.expired}" /></td>
				</tr>
			</c:forEach>
		</table>
	</div>


</body>
</html>