<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <title>Runner Signup</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>

<jsp:include page="header.jsp"/>

<section id="form">
    <div class="container">
        <div class="row">
            <div class="col-sm-4 col-sm-offset-4">
                <div class="signup-form">

                    <h2>Runner Signup</h2>

                    <form action="${pageContext.request.contextPath}/signup-runner"
                          method="post">

                        <input type="text" name="username"
                               placeholder="Username" required>

                        <input type="password" name="password"
                               placeholder="Password" required>

                        <input type="text" name="fullName"
                               placeholder="Full name" required>

                        <select name="gender">
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                        </select>

                        <input type="date" name="dob" required>

                        <input type="email" name="email"
                               placeholder="Email" required>

                        <input type="text" name="phone"
                               placeholder="Phone" required>

                        <button type="submit">Sign up</button>
                    </form>

                    <c:if test="${not empty error}">
                        <p style="color:red; text-align:center;">
                            ${error}
                        </p>
                    </c:if>

                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="footer.jsp"/>

</body>
</html>
