<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
<jsp:include page="../../header.jsp"/>
</head>
<body>
<section>
    <div class="container">
        <div class="row">
            <div class="col-sm-12">
                <h2 class="title text-center">Routes - ${event.name}</h2>
            </div>
        </div>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <i class="fa fa-exclamation-circle"></i> ${error}
            </div>
        </c:if>
        
        <div class="row">
            <div class="col-sm-12">
                <a href="${pageContext.request.contextPath}/organizer/routes/add?eventId=${event.eventId}" class="btn btn-primary" style="margin-right: 10px;">
                    <i class="fa fa-plus"></i> Create New Route
                </a>
                <a href="${pageContext.request.contextPath}/organizer/events" class="btn btn-primary">
                    <i class="fa fa-arrow-left"></i> Back to Events
                </a>
            </div>
        </div>
        
        <div class="row" style="margin-top: 20px;">
            <div class="col-sm-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Routes List</h3>
                    </div>
                    <div class="panel-body">
                        <c:choose>
                            <c:when test="${empty routes}">
                                <p class="text-center">No routes found. <a href="${pageContext.request.contextPath}/organizer/routes/add?eventId=${event.eventId}">Create one</a></p>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table table-striped table-bordered">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Distance (km)</th>
                                                <th>Description</th>
                                                <th>Checkpoints</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="route" items="${routes}">
                                                <tr>
                                                    <td>${route.routeId}</td>
                                                    <td><fmt:formatNumber value="${route.distanceKm}" pattern="#,##0.00" /> km</td>
                                                    <td>${route.description}</td>
                                                    <td>${route.checkpointCount} checkpoint(s)</td>
                                                    <td style="white-space: nowrap;">
                                                        <a href="${pageContext.request.contextPath}/organizer/routes/view?id=${route.routeId}" 
                                                           class="btn btn-sm btn-info" style="margin: 0 !important; margin-right: 5px;">
                                                            <i class="fa fa-map"></i> View Map
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/organizer/checkpoints?routeId=${route.routeId}" 
                                                           class="btn btn-sm btn-primary" style="margin: 0 !important; margin-right: 5px;">
                                                            <i class="fa fa-map-marker"></i> Checkpoints
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/organizer/routes/edit?id=${route.routeId}" 
                                                           class="btn btn-sm btn-warning" style="margin: 0 !important; margin-right: 5px;">
                                                            <i class="fa fa-edit"></i> Edit
                                                        </a>
                                                        <form method="post" action="${pageContext.request.contextPath}/organizer/routes/delete" style="display: inline-block; margin-right: 5px; vertical-align: middle; margin-top: 0; margin-bottom: 0;">
                                                            <input type="hidden" name="id" value="${route.routeId}">
                                                            <button type="submit" class="btn btn-sm btn-danger" 
                                                                    onclick="return confirm('Are you sure you want to delete this route? All checkpoints will also be deleted.');"
                                                                    style="margin: 0 !important;">
                                                                <i class="fa fa-trash"></i> Delete
                                                            </button>
                                                        </form>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<jsp:include page="../../footer.jsp"/>

