<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<jsp:include page="../header.jsp"/>
<section>
    <div class="container">
        <div class="row">
            <div class="col-sm-12">
                <h2 class="title text-center">Dashboard - Marathon Management</h2>
            </div>
        </div>
        
        <div class="row">
            <div class="col-sm-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Total Events</h3>
                    </div>
                    <div class="panel-body">
                        <h1 class="text-center" style="color: #FE980F; font-size: 48px;">${totalEvents}</h1>
                    </div>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Total Registrations</h3>
                    </div>
                    <div class="panel-body">
                        <h1 class="text-center" style="color: #FE980F; font-size: 48px;">${totalRegistrations}</h1>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="row">
            <div class="col-sm-12">
                <a href="${pageContext.request.contextPath}/organizer/events/add" class="btn btn-primary"><i class="fa fa-plus"></i> Create New Event</a>
                <a href="${pageContext.request.contextPath}/organizer/events" class="btn btn-default"><i class="fa fa-list"></i> View All Events</a>
            </div>
        </div>
        
        <div class="row">
            <div class="col-sm-12">
                <h2 class="title text-center">Recent Events</h2>
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Event Name</th>
                                <th>Event Date</th>
                                <th>Location</th>
                                <th>Max Participants</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="event" items="${events}" begin="0" end="4">
                                <tr>
                                    <td>${event.eventId}</td>
                                    <td>${event.name}</td>
                                    <td>${event.eventDate}</td>
                                    <td>${event.location}</td>
                                    <td>${event.maxParticipants}</td>
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
                                        <a href="${pageContext.request.contextPath}/organizer/events/edit?id=${event.eventId}" class="btn btn-sm btn-primary"><i class="fa fa-edit"></i> Edit</a>
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
<jsp:include page="../footer.jsp"/>
