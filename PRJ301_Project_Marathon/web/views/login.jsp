<%-- 
    Document   : login
    Created on : Dec 12, 2025, 2:49:13 PM
    Author     : User
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <jsp:include page="header.jsp"/>

        <section id="form">
            <div class="container">
                <div class="row">
                    <div class="col-sm-4 col-sm-offset-4">
                        <div class="signup-form">

                            <h2>User Login</h2>

                            <form action="login"
                                  method="post">

                                <input type="text" name="username"
                                       placeholder="Username" required>

                                <input type="password" name="password"
                                       placeholder="Password" required>

                                <button type="submit">Login</button>
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
