<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.Date" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Checkpoints - ${route.description}</title>
        <link href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/static/css/font-awesome.min.css" rel="stylesheet">
    </head>
    <body>

        <jsp:include page="../../header.jsp"/>

        <section>
            <div class="container">
                <div class="row">
                    <div class="col-sm-12">
                        <h2 class="title text-center">Checkpoints - ${route.description}</h2>
                    </div>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        <i class="fa fa-exclamation-circle"></i> ${error}
                    </div>
                </c:if>

                <div class="row">
                    <div class="col-sm-12">
                        <c:if test="${!event.hasStarted()}">
                            <a href="${pageContext.request.contextPath}/organizer/checkpoints/add?routeId=${route.routeId}" class="btn btn-primary" style="margin-right: 10px; margin-top: 0; margin-bottom: 0; vertical-align: top;">
                                <i class="fa fa-plus"></i> Add Checkpoint
                            </a>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/organizer/routes/view?id=${route.routeId}" class="btn btn-info" style="margin-right: 10px; margin-top: 0; margin-bottom: 0; vertical-align: top;">
                            <i class="fa fa-map"></i> View Route Map
                        </a>
                        <a href="${pageContext.request.contextPath}/organizer/routes?eventId=${event.eventId}" class="btn btn-default" style="margin-top: 0; margin-bottom: 0; vertical-align: top;">
                            <i class="fa fa-arrow-left"></i> Back to Routes
                        </a>
                    </div>
                </div>

                <div class="row" style="margin-top: 20px;">
                    <div class="col-sm-12">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">Checkpoints List</h3>
                            </div>
                            <div class="panel-body">
                                <c:choose>
                                    <c:when test="${empty checkpoints}">
                                        <p class="text-center">No checkpoints found. 
                                            <c:if test="${!event.hasStarted()}">
                                                <a href="${pageContext.request.contextPath}/organizer/checkpoints/add?routeId=${route.routeId}">Add one</a>
                                            </c:if>
                                        </p>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="table-responsive">
                                            <table class="table table-striped table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th>Order</th>
                                                        <th>Name</th>
                                                        <th>Coordinates</th>
                                                        <th>Actions</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="checkpoint" items="${checkpoints}">
                                                        <tr>
                                                            <td>${checkpoint.sequenceOrder}</td>
                                                            <td>${checkpoint.cpName}</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${checkpoint.latitude != null && checkpoint.longitude != null}">
                                                                        Lat: ${checkpoint.latitude}, Lng: ${checkpoint.longitude}
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="text-muted">Not set</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <c:if test="${!event.hasStarted()}">
                                                                    <a href="${pageContext.request.contextPath}/organizer/checkpoints/edit?id=${checkpoint.cpId}" 
                                                                       class="btn btn-sm btn-warning" style="margin-right: 5px;">
                                                                        <i class="fa fa-edit"></i> Edit
                                                                    </a>
                                                                    <a href="${pageContext.request.contextPath}/organizer/checkpoints/delete?id=${checkpoint.cpId}" 
                                                                       class="btn btn-sm btn-danger" 
                                                                       onclick="return confirm('Are you sure you want to delete this checkpoint?');">
                                                                        <i class="fa fa-trash"></i> Delete
                                                                    </a>
                                                                </c:if>
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

