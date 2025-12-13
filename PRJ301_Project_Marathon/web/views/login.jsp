<%-- 
    Document   : login
    Created on : Dec 12, 2025, 2:49:13 PM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div class="signup-form">
            <h2>New User Signup!</h2>
            <form action="signup" method="post">
                <input type="text" name="full_name" placeholder="Full Name" required/>

                <select name="gender" required>
                    <option value="">-- Select Gender --</option>
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                </select>

                <input type="date" name="dob" required/>
                <input type="email" name="email" placeholder="Email Address" required/>
                <input type="text" name="phone" placeholder="Phone Number" required/>
                <input type="password" name="password" placeholder="Password" required/>

                <button type="submit" class="btn btn-default">Signup</button>
            </form>
        </div>

    </body>
</html>
