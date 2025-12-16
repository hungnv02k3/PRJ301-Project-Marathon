<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <title>My Registrations</title>
        <link rel="stylesheet" href="/PRJ301_Project_Marathon/static/css/styles.css"/>
    </head>
    <body>

        <jsp:include page="header.jsp"/>

        <div class="container">
             <section class="rank-banner">
            <h1>My List Registrations</h1>
            <p>Track your Registrations</p>
        </section>

            <table class="reg-table">
                <tr>
                    <th>Event</th>
                    <th>Date</th>
                    <th>Location</th>
                    <th>Registered On</th>
                    <th>Bib</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>

                <c:forEach var="r" items="${registrations}">
                    <tr>
                        <td>${r.eventName}</td>
                        <td>${r.eventDate}</td>
                        <td>${r.location}</td>
                        <td>${r.registrationDate}</td>
                        <td>
                            <c:choose>
                                <c:when test="${empty r.bibNumber}">
                                    -
                                </c:when>
                                <c:otherwise>
                                    ${r.bibNumber}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <span class="status ${r.status}">
                                ${r.status}
                            </span>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${r.status == 'Registered'}">
                                    <form action="cancel-registration" method="post"
                                          onsubmit="return confirm('Are you sure you want to cancel this registration?')">
                                        <input type="hidden" name="registrationId" value="${r.registrationId}">
                                        <button class="btn-cancel">Cancel</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    -
                                </c:otherwise>
                            </c:choose>
                        </td>

                    </tr>
                </c:forEach>
            </table>

            <c:if test="${empty registrations}">
                <p>You have not registered for any event.</p>
            </c:if>
        </div>

        <jsp:include page="footer.jsp"/>

    </body>
</html>
