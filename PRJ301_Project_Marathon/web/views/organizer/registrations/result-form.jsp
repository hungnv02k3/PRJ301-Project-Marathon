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
                <h2 class="title text-center">Enter Race Result</h2>
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
                    <div class="panel-heading">
                        <h3 class="panel-title">Runner Information</h3>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-sm-6">
                                <p><strong>Name:</strong> ${registration.runnerName}</p>
                            </div>
                            <div class="col-sm-6">
                                <p><strong>Bib Number:</strong> ${registration.bibNumber}</p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p><strong>Event:</strong> ${event.name}</p>
                            </div>
                            <div class="col-sm-6">
                                <p><strong>Event Date:</strong> ${event.eventDate}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="row">
            <div class="col-sm-8 col-sm-offset-2">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <form method="post" action="${pageContext.request.contextPath}/organizer/registrations/result/save">
                            <input type="hidden" name="registrationId" value="${registration.registrationId}">
                            <input type="hidden" name="eventId" value="${event.eventId}">
                            
                            <div class="form-group">
                                <label>Event Start Time</label>
                                <p class="form-control-static">
                                    <c:choose>
                                        <c:when test="${event.eventStartTime != null}">
                                            <fmt:formatDate value="${event.eventStartTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatDate value="${event.eventDate}" pattern="yyyy-MM-dd" /> 00:00:00
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                                <small class="help-block">All runners start at the same time</small>
                            </div>
                            
                            <div class="form-group">
                                <label for="finishTime">Finish Time *</label>
                                <input type="datetime-local" step="1" class="form-control" id="finishTime" name="finishTime" 
                                       <c:if test="${result != null && result.finishTime != null}">
                                       value="<fmt:formatDate value="${result.finishTime}" pattern="yyyy-MM-dd'T'HH:mm:ss" />"
                                       </c:if> required>
                                <small class="help-block">Format: YYYY-MM-DDTHH:MM:SS (select down to seconds)</small>
                            </div>
                            
                            <c:if test="${result != null && result.netTime != null}">
                                <div class="alert alert-info">
                                    <strong>Current Result:</strong><br>
                                    Net Time: ${result.formattedNetTime}<br>
                                    <c:if test="${result.rankingOverall != null}">
                                        Ranking: #${result.rankingOverall}
                                    </c:if>
                                </div>
                            </c:if>
                            
                            <div class="form-group">
                                <button type="submit" class="btn btn-primary"><i class="fa fa-save"></i> Save Result</button>
                                <a href="${pageContext.request.contextPath}/organizer/registrations?eventId=${event.eventId}" class="btn btn-default"><i class="fa fa-times"></i> Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<jsp:include page="../../footer.jsp"/>

