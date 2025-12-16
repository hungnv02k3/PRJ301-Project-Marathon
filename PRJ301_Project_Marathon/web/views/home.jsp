<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Marathon Home</title>
        <link rel="stylesheet" href="/PRJ301_Project_Marathon/static/css/home.css"/>
    </head>
    <body>

        <jsp:include page="header.jsp"/>

        <section class="marathon-banner">
            <h1>Available Marathon Events</h1>
            <p>Register now before the deadline!</p>
        </section>

        <section class="marathon-home">

            <!-- SEARCH -->
            <div class="marathon-search">
                <form action="home" method="get">
                    <input type="text" name="keyword"
                           placeholder="Search marathon..."
                           value="${param.keyword}">
                    <button type="submit">Search</button>
                </form>
            </div>

            <!-- EVENT LIST -->
            <div class="marathon-grid">
                <c:forEach var="e" items="${events}">
                    <div class="marathon-card">
                        <h3>${e.eventName}</h3>
                        <p><strong>Date:</strong> ${e.eventDate}</p>
                        <p><strong>Location:</strong> ${e.location}</p>

                        <form action="register" method="post">
                            <input type="hidden" name="eventId" value="${e.eventId}">
                            <button type="submit">Register</button>
                        </form>
                    </div>
                </c:forEach>
            </div>

            <!-- EMPTY -->
            <c:if test="${empty events}">
                <p class="marathon-empty">No marathon available.</p>
            </c:if>

            <!-- PAGINATION -->
            <div class="marathon-pagination">
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="home?page=${i}&keyword=${param.keyword}"
                       class="${i == page ? 'active' : ''}">
                        ${i}
                    </a>
                </c:forEach>
            </div>

        </section>

        <jsp:include page="footer.jsp"/>

    </body>
</html>
