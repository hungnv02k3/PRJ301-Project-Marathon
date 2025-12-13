<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dashboard - Organizer</title>
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
        .stats {
            display: flex;
            gap: 20px;
            margin: 20px 0;
        }
        .stat-card {
            flex: 1;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 8px;
            border-left: 4px solid #007bff;
        }
        .stat-card h3 {
            margin: 0 0 10px 0;
            color: #666;
            font-size: 14px;
        }
        .stat-card .value {
            font-size: 32px;
            font-weight: bold;
            color: #007bff;
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
    </style>
</head>
<body>
    <div class="container">
        <h1>Dashboard - Quản lý Giải Chạy</h1>
        
        <div class="stats">
            <div class="stat-card">
                <h3>Tổng số sự kiện</h3>
                <div class="value">${totalEvents}</div>
            </div>
            <div class="stat-card">
                <h3>Tổng số đăng ký</h3>
                <div class="value">${totalRegistrations}</div>
            </div>
        </div>
        
        <div>
            <a href="${pageContext.request.contextPath}/organizer/events/add" class="btn">Tạo sự kiện mới</a>
            <a href="${pageContext.request.contextPath}/organizer/events" class="btn">Xem tất cả sự kiện</a>
        </div>
        
        <h2>Danh sách sự kiện gần đây</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên sự kiện</th>
                    <th>Ngày tổ chức</th>
                    <th>Địa điểm</th>
                    <th>Số lượng tối đa</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="event" items="${events}" begin="0" end="4">
                    <tr>
                        <td>${event.eventId}</td>
                        <td>${event.name}</td>
                        <td>${event.eventDate}</td>
                        <td>${event.location}</td>
                        <td>${event.maxParticipants}</td>
                        <td>
                            <span class="status ${event.status.toLowerCase()}">${event.status}</span>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/organizer/events/edit?id=${event.eventId}">Sửa</a> |
                            <a href="${pageContext.request.contextPath}/organizer/registrations?eventId=${event.eventId}">Xem đăng ký</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>

