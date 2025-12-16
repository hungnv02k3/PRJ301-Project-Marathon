<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <title>My Profile</title>
        <link rel="stylesheet" href="/PRJ301_Project_Marathon/static/css/styles.css"/>
    </head>
    <body>

        <jsp:include page="header.jsp"/>

        <section class="profile-container">
            <h2>My Profile</h2>

            <c:if test="${not empty sessionScope.msg}">
                <div class="alert-success">${sessionScope.msg}</div>
                <c:remove var="msg" scope="session"/>
            </c:if>

            <form method="post" class="profile-form">
                <label>Full Name</label>
                <input type="text" name="fullName" value="${runner.fullName}" required>

                <label>Gender</label>
                <select name="gender">
                    <option ${runner.gender == 'Male' ? 'selected' : ''}>Male</option>
                    <option ${runner.gender == 'Female' ? 'selected' : ''}>Female</option>
                </select>

                <label>Date of Birth</label>
                <input type="date" name="dob" value="${runner.dob}" required>

                <label>Email</label>
                <input type="email" name="email" value="${runner.email}" required>

                <label>Phone</label>
                <input type="text" name="phone" value="${runner.phone}" required>

                <button type="submit">Update Profile</button>
            </form>
        </section>

        <jsp:include page="footer.jsp"/>

    </body>
</html>
