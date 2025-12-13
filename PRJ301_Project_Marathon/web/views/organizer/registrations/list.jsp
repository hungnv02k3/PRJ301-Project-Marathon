<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Danh sách Đăng ký - Organizer</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
        }
        .event-info {
            background: #e7f3ff;
            padding: 15px;
            border-radius: 4px;
            margin: 20px 0;
        }
        .btn {
            display: inline-block;
            padding: 8px 16px;
            background: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            border: none;
            cursor: pointer;
            margin: 5px;
            font-size: 14px;
        }
        .btn:hover {
            background: #0056b3;
        }
        .btn-success {
            background: #28a745;
        }
        .btn-success:hover {
            background: #218838;
        }
        .btn-danger {
            background: #dc3545;
        }
        .btn-danger:hover {
            background: #c82333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #007bff;
            color: white;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .status {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
        }
        .status.registered {
            background: #d4edda;
            color: #155724;
        }
        .status.pending {
            background: #fff3cd;
            color: #856404;
        }
        .status.rejected {
            background: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Danh sách Đăng ký</h1>
        
        <div class="event-info">
            <h2>${event.name}</h2>
            <p><strong>Ngày tổ chức:</strong> ${event.eventDate}</p>
            <p><strong>Địa điểm:</strong> ${event.location}</p>
            <p><strong>Trạng thái:</strong> ${event.status}</p>
        </div>
        
        <div>
            <a href="${pageContext.request.contextPath}/organizer/events" class="btn">Về danh sách sự kiện</a>
        </div>
        
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Họ tên</th>
                    <th>Email</th>
                    <th>Số điện thoại</th>
                    <th>Bib Number</th>
                    <th>Ngày đăng ký</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="reg" items="${registrations}">
                    <tr>
                        <td>${reg.registrationId}</td>
                        <td>${reg.runnerName}</td>
                        <td>${reg.runnerEmail}</td>
                        <td>${reg.runnerPhone}</td>
                        <td>${reg.bibNumber}</td>
                        <td>${reg.registrationDate}</td>
                        <td>
                            <span class="status ${reg.status.toLowerCase()}">${reg.status}</span>
                        </td>
                        <td>
                            <c:if test="${reg.status != 'Registered'}">
                                <form method="post" action="${pageContext.request.contextPath}/organizer/registrations/approve" style="display:inline;">
                                    <input type="hidden" name="registrationId" value="${reg.registrationId}">
                                    <input type="hidden" name="eventId" value="${event.eventId}">
                                    <button type="submit" class="btn btn-success">Phê duyệt</button>
                                </form>
                            </c:if>
                            <c:if test="${reg.status != 'Rejected'}">
                                <form method="post" action="${pageContext.request.contextPath}/organizer/registrations/reject" style="display:inline;">
                                    <input type="hidden" name="registrationId" value="${reg.registrationId}">
                                    <input type="hidden" name="eventId" value="${event.eventId}">
                                    <button type="submit" class="btn btn-danger" onclick="return confirm('Bạn có chắc muốn từ chối đăng ký này?')">Từ chối</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty registrations}">
                    <tr>
                        <td colspan="8" style="text-align: center; padding: 20px;">
                            Chưa có đăng ký nào cho sự kiện này.
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</body>
</html>

