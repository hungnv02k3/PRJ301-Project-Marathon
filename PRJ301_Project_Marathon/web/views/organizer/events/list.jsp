<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Danh sách Sự kiện - Organizer</title>
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
        .btn {
            display: inline-block;
            padding: 10px 20px;
            background: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            margin: 10px 5px;
        }
        .btn:hover {
            background: #0056b3;
        }
        .btn-danger {
            background: #dc3545;
        }
        .btn-danger:hover {
            background: #c82333;
        }
        .btn-warning {
            background: #ffc107;
            color: #000;
        }
        .btn-warning:hover {
            background: #e0a800;
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
        .status.open {
            background: #d4edda;
            color: #155724;
        }
        .status.closed {
            background: #f8d7da;
            color: #721c24;
        }
        .error {
            background: #f8d7da;
            color: #721c24;
            padding: 10px;
            border-radius: 4px;
            margin: 10px 0;
        }
        .actions {
            display: flex;
            gap: 5px;
        }
        .actions a {
            margin: 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Danh sách Sự kiện</h1>
        
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        
        <div>
            <a href="${pageContext.request.contextPath}/organizer/dashboard" class="btn">Về Dashboard</a>
            <a href="${pageContext.request.contextPath}/organizer/events/add" class="btn">Tạo sự kiện mới</a>
        </div>
        
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên sự kiện</th>
                    <th>Mô tả</th>
                    <th>Ngày tổ chức</th>
                    <th>Địa điểm</th>
                    <th>Số lượng tối đa</th>
                    <th>Hạn đăng ký</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="event" items="${events}">
                    <tr>
                        <td>${event.eventId}</td>
                        <td>${event.name}</td>
                        <td>${event.description}</td>
                        <td>${event.eventDate}</td>
                        <td>${event.location}</td>
                        <td>${event.maxParticipants}</td>
                        <td>${event.registrationDeadline}</td>
                        <td>
                            <span class="status ${event.status.toLowerCase()}">${event.status}</span>
                        </td>
                        <td>
                            <div class="actions">
                                <c:choose>
                                    <c:when test="${event.hasStarted()}">
                                        <span style="color: #999;">Đã bắt đầu</span>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/organizer/events/edit?id=${event.eventId}" class="btn">Sửa</a>
                                        <form method="post" action="${pageContext.request.contextPath}/organizer/events/delete" style="display:inline;">
                                            <input type="hidden" name="eventId" value="${event.eventId}">
                                            <button type="submit" class="btn btn-danger" onclick="return confirm('Bạn có chắc muốn xóa sự kiện này?')">Xóa</button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${event.status != 'Closed'}">
                                    <form method="post" action="${pageContext.request.contextPath}/organizer/events/close" style="display:inline;">
                                        <input type="hidden" name="eventId" value="${event.eventId}">
                                        <button type="submit" class="btn btn-warning" onclick="return confirm('Bạn có chắc muốn đóng sự kiện này?')">Đóng</button>
                                    </form>
                                </c:if>
                                <a href="${pageContext.request.contextPath}/organizer/registrations?eventId=${event.eventId}" class="btn">Xem đăng ký</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>

