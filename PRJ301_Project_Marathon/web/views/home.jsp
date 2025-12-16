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
            <c:if test="${not empty sessionScope.msg}">
                <div id="toast-success" class="toast success">
                    ${sessionScope.msg}
                </div>
                <c:remove var="msg" scope="session"/>
            </c:if>


            <!-- EVENT LIST -->
            <div class="marathon-grid">
                <c:forEach var="e" items="${events}">
                    <div class="marathon-card">
                        <h3>
                            <a href="eventdetail?id=${e.eventId}">
                                ${e.eventName}
                            </a>
                        </h3>

                        <p><strong>Date:</strong> ${e.eventDate}</p>
                        <p><strong>Location:</strong> ${e.location}</p>

                        <button class="register-btn"
                                onclick="openRegisterModal(${e.eventId}, '${e.eventName}')">
                            Register
                        </button>

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
        <div id="registerModal" class="modal-overlay">
            <div class="modal-box">
                <h3>Confirm Registration</h3>
                <p id="registerText"></p>

                <form id="registerForm" action="register" method="post">
                    <input type="hidden" name="eventId" id="registerEventId">
                    <div class="modal-actions">
                        <button type="button" class="btn-cancel"
                                onclick="closeRegisterModal()">Cancel</button>
                        <button type="submit" class="btn-confirm">
                            Confirm
                        </button>
                    </div>
                </form>
            </div>
        </div>

    </body>
</html>
<script>
    function openRegisterModal(eventId, eventName) {
        document.getElementById("registerEventId").value = eventId;
        document.getElementById("registerText").innerText =
                "Do you want to register for \"" + eventName + "\"?";
        document.getElementById("registerModal").style.display = "flex";
    }

    function closeRegisterModal() {
        document.getElementById("registerModal").style.display = "none";
    }

    // click ngoài modal để đóng
    window.addEventListener("click", function (e) {
        const modal = document.getElementById("registerModal");
        if (e.target === modal) {
            closeRegisterModal();
        }
    });
</script>
<script>
    setTimeout(() => {
        const toast = document.getElementById("toast-success");
        if (toast)
            toast.remove();
    }, 3500);
</script>
