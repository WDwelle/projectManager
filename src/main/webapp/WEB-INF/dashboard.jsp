<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- c:out ; c:forEach etc. --> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- Formatting (dates) --> 
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!-- form:form -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- for rendering errors on PUT routes -->
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>project</title>
    <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/main.css"> <!-- change to match your file/naming structure -->
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
   <div class="container mt-5">
		<h1>Project Manager</h1>
		<h5>Welcome, ${firstname}</h5>
		<a href="/logout"> Logout</a> <a href="/projects/add"> Add a
			project</a>

		<table class="table table-striped">
			<tr>
				<th>ID</th>
				<th>Project</th>
				<th>Team Lead</th>
				<th>Due Date</th>
				<th colspan="2">Actions</th>
			</tr>
			<c:forEach var="project" items="${projects }">
				<tr>
					<td>${project.id }</td>
					<td><a href="/projects/${project.id}">${project.title }</a></td>
					<td>${project.creator.firstname } ${project.creator.lastname }</td>
					<td><fmt:formatDate type="date" value="${project.duedate }" />

					</td>
					<c:choose>
						<c:when test="${project.creator.id == userId }">
							<td><a href="/projects/${project.id }/edit"
								class="btn btn-success">Edit</a></td>
							<td>
								<form action="/projects/${project.id }/delete" method="post">
									<input type="hidden" name="_method" value="delete"> <input
										type="submit" value="Delete" class="btn btn-danger" />
								</form>
							</td>
						</c:when>
						<c:otherwise>
						<td></td>
						<td></td>
						</c:otherwise>

					</c:choose>
			</c:forEach>

		</table>

	</div>
</body>
</html>