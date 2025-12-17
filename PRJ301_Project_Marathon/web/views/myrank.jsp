<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <title>My Rank</title>
        <link rel="stylesheet" href="/PRJ301_Project_Marathon/static/css/styles.css"/>
    </head>
    <body>

        <jsp:include page="header.jsp"/>

        <!-- PAGE TITLE -->
        <section class="rank-banner">
            <h1>My Marathon Results</h1>
            <p>Track your performance & ranking</p>
        </section>

        <!-- RESULT TABLE -->
        <section class="rank-container">

            <table class="rank-table">
                <thead>
                    <tr>
                        <th>Event</th>
                        <th>Date</th>
                        <th>Location</th>
                        <th>Net Time</th>
                        <th>Rank</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="r" items="${results}">
                        <tr>
                            <td class="event-name">${r.eventName}</td>
                            <td>${r.eventDate}</td>
                            <td>${r.location}</td>
                            <td>${r.netTime} sec</td>
                            <td class="rank-cell">
                                <c:choose>
                                    <c:when test="${r.rankingOverall == 1}">#${r.rankingOverall}-Champion</c:when>
                                    <c:when test="${r.rankingOverall == 2}">#${r.rankingOverall}-Silver</c:when>
                                    <c:when test="${r.rankingOverall == 3}">#${r.rankingOverall}-Cooper</c:when>
                                    <c:otherwise>#${r.rankingOverall}</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty results}">
                        <tr>
                            <td colspan="5" class="rank-empty">
                                You have not participated in any marathon yet.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>

        </section>

        <jsp:include page="footer.jsp"/>

    </body>
</html>
