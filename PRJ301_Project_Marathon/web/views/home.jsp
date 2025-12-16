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

<section class="banner">
    <h1>Available Marathon Events</h1>
    <p>Register now before the deadline!</p>
</section>

<section class="race-container">

    <c:forEach var="r" items="${races}">
        <div class="race-card">
            <img src="images/races/${r.image}" alt="${r.raceName}">

            <div class="race-info">
                <h3>${r.raceName}</h3>
                <p><strong>Location:</strong> ${r.location}</p>
                <p><strong>Distance:</strong> ${r.distance}</p>
                <p><strong>Date:</strong> ${r.startDate} → ${r.endDate}</p>
                <p class="fee">${r.fee} VND</p>

                <a href="register-race?raceId=${r.raceId}" class="btn">
                    Register Now
                </a>
            </div>
        </div>
    </c:forEach>

    <c:if test="${empty races}">
        <p class="empty">No races available for registration.</p>
    </c:if>

</section>

<jsp:include page="footer.jsp"/>

</body>
</html>
