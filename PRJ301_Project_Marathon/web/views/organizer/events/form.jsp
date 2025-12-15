<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
<jsp:include page="../../header.jsp"/>
<section>
    <div class="container">
        <div class="row">
            <div class="col-sm-12">
                <h2 class="title text-center"><c:choose><c:when test="${event != null}">Edit Event</c:when><c:otherwise>Create New Event</c:otherwise></c:choose></h2>
            </div>
        </div>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <i class="fa fa-exclamation-circle"></i> ${error}
            </div>
        </c:if>
        
        <div class="row">
            <div class="col-sm-8 col-sm-offset-2">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <form method="post" action="${pageContext.request.contextPath}/organizer/events/<c:choose><c:when test="${event != null}">edit</c:when><c:otherwise>add</c:otherwise></c:choose>">
                            <c:if test="${event != null}">
                                <input type="hidden" name="eventId" value="${event.eventId}">
                            </c:if>
                            
                            <div class="form-group">
                                <label for="name">Event Name *</label>
                                <input type="text" class="form-control" id="name" name="name" value="${event.name}" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="description">Description</label>
                                <textarea class="form-control" id="description" name="description" rows="4">${event.description}</textarea>
                            </div>
                            
                            <div class="form-group">
                                <label for="eventStartTime">Event Start Time *</label>
                                <input type="datetime-local" step="1" class="form-control" id="eventStartTime" name="eventStartTime" 
                                       <c:choose>
                                           <c:when test="${event != null && event.eventStartTime != null}">
                                               value="<fmt:formatDate value="${event.eventStartTime}" pattern="yyyy-MM-dd'T'HH:mm:ss" />"
                                           </c:when>
                                           <c:when test="${event != null && event.eventDate != null}">
                                               value="<fmt:formatDate value="${event.eventDate}" pattern="yyyy-MM-dd" />T00:00:00"
                                           </c:when>
                                       </c:choose> required>
                                <small class="help-block">Format: YYYY-MM-DDTHH:MM:SS (select down to seconds)</small>
                            </div>
                            
                            <div class="form-group">
                                <label for="location">Location *</label>
                                <input type="text" class="form-control" id="location" name="location" value="${event.location}" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="maxParticipants">Max Participants *</label>
                                <input type="number" class="form-control" id="maxParticipants" name="maxParticipants" value="${event.maxParticipants}" min="1" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="registrationDeadline">Registration Deadline *</label>
                                <input type="datetime-local" step="1" class="form-control" id="registrationDeadline" name="registrationDeadline" 
                                       <c:choose>
                                           <c:when test="${event != null && event.registrationDeadline != null}">
                                               value="<fmt:formatDate value="${event.registrationDeadline}" pattern="yyyy-MM-dd" />T23:59:59"
                                           </c:when>
                                       </c:choose> required>
                                <small class="help-block">Must be before or equal to Event Start Time</small>
                            </div>
                            
                            <div class="form-group">
                                <label for="status">Status *</label>
                                <select class="form-control" id="status" name="status" required>
                                    <option value="Open" <c:if test="${event.status == 'Open'}">selected</c:if>>Open</option>
                                    <option value="Closed" <c:if test="${event.status == 'Closed'}">selected</c:if>>Closed</option>
                                </select>
                            </div>
                            
                            <div class="form-group">
                                <button type="submit" class="btn btn-primary" style="margin-right: 10px;"><i class="fa fa-save"></i> Save</button>
                                <a href="${pageContext.request.contextPath}/organizer/events" class="btn btn-primary"><i class="fa fa-times"></i> Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<jsp:include page="../../footer.jsp"/>
