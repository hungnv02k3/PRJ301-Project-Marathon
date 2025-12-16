<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <title>${event.eventName}</title>
        <link rel="stylesheet" href="/PRJ301_Project_Marathon/static/css/event-detail.css"/>

    </head>
    <body>

        <jsp:include page="header.jsp"/>

        <div class="event-detail">

            <h1>${event.eventName}</h1>

            <p><strong>Date:</strong> ${event.eventDate}</p>
            <p><strong>Start time:</strong> ${event.eventStartTime}</p>
            <p><strong>Location:</strong> ${event.location}</p>
            <p><strong>Registration deadline:</strong> ${event.registrationDeadline}</p>
            <p><strong>Status:</strong> ${event.status}</p>

            <p><strong>Registered:</strong>
                ${registeredCount}
                <c:if test="${event.maxParticipants > 0}">
                    / ${event.maxParticipants}
                </c:if>
            </p>

            <c:choose>

                <c:when test="${empty sessionScope.account}">
                    <a href="login" class="btn-login">Login to register</a>
                </c:when>

                <c:when test="${alreadyRegistered}">
                    <button class="btn-disabled" disabled>
                        Already Registered
                    </button>
                </c:when>

                <c:when test="${isExpired}">
                    <button class="btn-disabled" disabled>
                        Registration Closed
                    </button>
                </c:when>

                <c:when test="${isFull}">
                    <button class="btn-disabled" disabled>
                        Event Full
                    </button>
                </c:when>

                <c:otherwise>
                    <form action="register" method="post">
                        <input type="hidden" name="eventId" value="${event.eventId}">
                        <button type="submit" class="btn-register">
                            Register Now
                        </button>
                    </form>
                </c:otherwise>

            </c:choose>


        </div>

        <jsp:include page="footer.jsp"/>

    </body>
</html>
