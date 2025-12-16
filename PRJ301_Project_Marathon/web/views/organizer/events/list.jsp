<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Event List</title>
    <link href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/font-awesome.min.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../../header.jsp"/>

<section>
    <div class="container">
        <div class="row">
            <div class="col-sm-12">
                <h2 class="title text-center">Event List</h2>
            </div>
        </div>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <i class="fa fa-exclamation-circle"></i> ${error}
            </div>
        </c:if>
        
        <div class="row">
            <div class="col-sm-12">
                <a href="${pageContext.request.contextPath}/organizer/dashboard" class="btn btn-primary" style="margin-right: 10px;"><i class="fa fa-arrow-left"></i> Back to Dashboard</a>
                <a href="${pageContext.request.contextPath}/organizer/events/add" class="btn btn-primary"><i class="fa fa-plus"></i> Create New Event</a>
            </div>
        </div>
        
        <div class="row">
            <div class="col-sm-12">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Event Name</th>
                                <th>Description</th>
                                <th>Event Date</th>
                                <th>Location</th>
                                <th>Max Participants</th>
                                <th>Registration Deadline</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="event" items="${events}">
                                <tr>
                                    <td>${event.eventId}</td>
                                    <td>${event.name}</td>
                                    <td>${event.description}</td>
                                    <td>${event.eventDate}</td>
                                    <td>${event.location}</td>
                                    <td>${event.maxParticipants}</td>
                                    <td>${event.registrationDeadline}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${event.status == 'Open'}">
                                                <span class="label label-success">${event.status}</span>
                                            </c:when>
                                            <c:when test="${event.status == 'PAUSE'}">
                                                <span class="label label-warning">${event.status}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="label label-danger">${event.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td style="white-space: nowrap;">
                                        <c:choose>
                                            <c:when test="${event.hasStarted()}">
                                                <span class="text-muted" style="display: inline-block; margin-right: 5px; vertical-align: middle;"><i class="fa fa-info-circle"></i> Started</span>
                                            </c:when>
                                            <c:when test="${event.status == 'Closed'}">
                                                <span class="text-muted" style="display: inline-block; margin-right: 5px; vertical-align: middle;"><i class="fa fa-lock"></i> Closed</span>
                                            </c:when>
                                            <c:otherwise>
                                                <form method="get" action="${pageContext.request.contextPath}/organizer/events/edit" style="display: inline-block; margin-right: 5px; vertical-align: middle; margin-top: 0; margin-bottom: 0;">
                                                    <input type="hidden" name="id" value="${event.eventId}">
                                                    <button type="submit" class="btn btn-sm btn-primary" style="margin: 0 !important;"><i class="fa fa-edit"></i> Edit</button>
                                                </form>
                                                <c:if test="${event.status != 'PAUSE'}">
                                                    <form method="post" action="${pageContext.request.contextPath}/organizer/events/pause" style="display: inline-block; margin-right: 5px; vertical-align: middle; margin-top: 0; margin-bottom: 0;">
                                                        <input type="hidden" name="eventId" value="${event.eventId}">
                                                        <button type="submit" class="btn btn-sm btn-warning" onclick="return confirm('Are you sure you want to pause this event?')" style="margin: 0 !important;"><i class="fa fa-pause"></i> Pause</button>
                                                    </form>
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:if test="${event.status != 'Closed'}">
                                            <form method="post" action="${pageContext.request.contextPath}/organizer/events/close" style="display: inline-block; margin-right: 5px; vertical-align: middle; margin-top: 0; margin-bottom: 0;">
                                                <input type="hidden" name="eventId" value="${event.eventId}">
                                                <button type="submit" class="btn btn-sm btn-warning" onclick="return confirm('Are you sure you want to close this event?')" style="margin: 0 !important;"><i class="fa fa-lock"></i> Close</button>
                                            </form>
                                        </c:if>
                                        <form method="get" action="${pageContext.request.contextPath}/organizer/registrations" style="display: inline-block; margin-right: 5px; vertical-align: middle; margin-top: 0; margin-bottom: 0;">
                                            <input type="hidden" name="eventId" value="${event.eventId}">
                                            <button type="submit" class="btn btn-sm btn-info" style="margin: 0 !important;"></i> Registrations</button>
                                        </form>
                                        <form method="get" action="${pageContext.request.contextPath}/organizer/routes" style="display: inline-block; vertical-align: middle; margin-top: 0; margin-bottom: 0;">
                                            <input type="hidden" name="eventId" value="${event.eventId}">
                                            <button type="submit" class="btn btn-sm btn-success" style="margin: 0 !important;"><i class="fa fa-map"></i> Routes</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>
<jsp:include page="../../footer.jsp"/>
