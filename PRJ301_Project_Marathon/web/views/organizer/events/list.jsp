<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
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
                <a href="${pageContext.request.contextPath}/organizer/dashboard" class="btn btn-default"><i class="fa fa-arrow-left"></i> Back to Dashboard</a>
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
                                            <c:otherwise>
                                                <span class="label label-danger">${event.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${event.hasStarted()}">
                                                <span class="text-muted"><i class="fa fa-info-circle"></i> Started</span>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${pageContext.request.contextPath}/organizer/events/edit?id=${event.eventId}" class="btn btn-sm btn-primary"><i class="fa fa-edit"></i> Edit</a>
                                                <form method="post" action="${pageContext.request.contextPath}/organizer/events/delete" style="display:inline;">
                                                    <input type="hidden" name="eventId" value="${event.eventId}">
                                                    <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this event?')"><i class="fa fa-trash"></i> Delete</button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:if test="${event.status != 'Closed'}">
                                            <form method="post" action="${pageContext.request.contextPath}/organizer/events/close" style="display:inline;">
                                                <input type="hidden" name="eventId" value="${event.eventId}">
                                                <button type="submit" class="btn btn-sm btn-warning" onclick="return confirm('Are you sure you want to close this event?')"><i class="fa fa-lock"></i> Close</button>
                                            </form>
                                        </c:if>
                                        <a href="${pageContext.request.contextPath}/organizer/registrations?eventId=${event.eventId}" class="btn btn-sm btn-info"><i class="fa fa-users"></i> Registrations</a>
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
