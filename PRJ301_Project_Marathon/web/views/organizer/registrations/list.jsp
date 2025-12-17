<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Registration List</title>
    <link href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/font-awesome.min.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../../header.jsp"/>

<section>
    <div class="container">
        <div class="row">
            <div class="col-sm-12">
                <h2 class="title text-center">Registration List</h2>
            </div>
        </div>
        
        <div class="row">
            <div class="col-sm-12">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-calendar"></i> Event Information</h3>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-sm-4">
                                <p><strong>Event Name:</strong> ${event.eventName}</p>
                            </div>
                            <div class="col-sm-4">
                                <p><strong>Event Date:</strong> ${event.eventDate}</p>
                            </div>
                            <div class="col-sm-4">
                                <p><strong>Location:</strong> ${event.location}</p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-4">
                                <p><strong>Status:</strong> 
                                    <c:choose>
                                        <c:when test="${event.status == 'Open'}">
                                            <span class="label label-success">${event.status}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="label label-danger">${event.status}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="row">
            <div class="col-sm-12">
                <a href="${pageContext.request.contextPath}/organizer/events" class="btn btn-default"><i class="fa fa-arrow-left"></i> Back to Event List</a>
            </div>
        </div>
        
        <div class="row">
            <div class="col-sm-12">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Full Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Bib Number</th>
                                <th>Registration Date</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="reg" items="${registrations}">
                                <tr>
                                    <td>${reg.getRegistrationId()}</td>
                                    <td>${reg.runnerName}</td>
                                    <td>${reg.runnerEmail}</td>
                                    <td>${reg.runnerPhone}</td>
                                    <td>${reg.bibNumber}</td>
                                    <td>${reg.registrationDate}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${reg.status == 'Registered'}">
                                                <span class="label label-success">${reg.status}</span>
                                            </c:when>
                                            <c:when test="${reg.status == 'Rejected'}">
                                                <span class="label label-danger">${reg.status}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="label label-warning">${reg.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td style="white-space: nowrap;">
                                        <c:choose>
                                            <c:when test="${event.hasStarted()}">
                                                <!-- Event has started - show result entry only for approved registrations -->
                                                <c:if test="${reg.status == 'ACCEPTED'}">
                                                    <a href="${pageContext.request.contextPath}/organizer/registrations/result?registrationId=${reg.registrationId}&eventId=${event.eventId}"
                                                       class="btn btn-sm btn-primary"
                                                       style="margin: 0 !important; margin-right: 5px;">
                                                        <i class="fa fa-clock-o"></i> Enter Result
                                                    </a>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <!-- Event not started - show approve/reject and assign bib -->
                                                <c:if test="${reg.status == 'Registered'}">
                                                    <form method="post" action="${pageContext.request.contextPath}/organizer/registrations/approve"
                                                          style="display: inline-block; margin-right: 5px; vertical-align: middle; margin-top: 0; margin-bottom: 0;">
                                                        <input type="hidden" name="registrationId" value="${reg.registrationId}">
                                                        <input type="hidden" name="eventId" value="${event.eventId}">
                                                        <button type="submit" class="btn btn-sm btn-success" style="margin: 0 !important;">
                                                            <i class="fa fa-check"></i> Approve
                                                        </button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${reg.status == 'Rejected'}">
                                                    <form method="post" action="${pageContext.request.contextPath}/organizer/registrations/reject"
                                                          style="display: inline-block; margin-right: 5px; vertical-align: middle; margin-top: 0; margin-bottom: 0;">
                                                        <input type="hidden" name="registrationId" value="${reg.registrationId}">
                                                        <input type="hidden" name="eventId" value="${event.eventId}">
                                                        <button type="submit" class="btn btn-sm btn-danger"
                                                                onclick="return confirm('Are you sure you want to reject this registration?')"
                                                                style="margin: 0 !important;">
                                                            <i class="fa fa-times"></i> Reject
                                                        </button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${reg.status == 'ACCEPTED'}">
                                                    <form method="post" action="${pageContext.request.contextPath}/organizer/registrations/assign-bib"
                                                          style="display: inline-block; margin-right: 5px; vertical-align: middle; margin-top: 0; margin-bottom: 0;">
                                                        <input type="hidden" name="registrationId" value="${reg.registrationId}">
                                                        <input type="hidden" name="eventId" value="${event.eventId}">
                                                        <button type="submit" class="btn btn-sm btn-info" style="margin: 0 !important;">
                                                            <i class="fa fa-tag"></i> Assign Bib
                                                        </button>
                                                    </form>
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty registrations}">
                                <tr>
                                    <td colspan="8" class="text-center" style="padding: 20px;">
                                        <i class="fa fa-info-circle"></i> No registrations found for this event.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>
<jsp:include page="../../footer.jsp"/>
