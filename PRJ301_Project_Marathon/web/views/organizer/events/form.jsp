<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><c:choose><c:when test="${event != null}">Sửa Sự kiện</c:when><c:otherwise>Tạo Sự kiện Mới</c:otherwise></c:choose> - Organizer</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }
        input[type="text"],
        input[type="date"],
        input[type="number"],
        textarea,
        select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            font-size: 14px;
        }
        textarea {
            min-height: 100px;
            resize: vertical;
        }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            background: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            border: none;
            cursor: pointer;
            margin: 10px 5px;
        }
        .btn:hover {
            background: #0056b3;
        }
        .btn-secondary {
            background: #6c757d;
        }
        .btn-secondary:hover {
            background: #5a6268;
        }
        .error {
            background: #f8d7da;
            color: #721c24;
            padding: 10px;
            border-radius: 4px;
            margin: 10px 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1><c:choose><c:when test="${event != null}">Sửa Sự kiện</c:when><c:otherwise>Tạo Sự kiện Mới</c:otherwise></c:choose></h1>
        
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        
        <form method="post" action="${pageContext.request.contextPath}/organizer/events/<c:choose><c:when test="${event != null}">edit</c:when><c:otherwise>add</c:otherwise></c:choose>">
            <c:if test="${event != null}">
                <input type="hidden" name="eventId" value="${event.eventId}">
            </c:if>
            
            <div class="form-group">
                <label for="name">Tên sự kiện *</label>
                <input type="text" id="name" name="name" value="${event.name}" required>
            </div>
            
            <div class="form-group">
                <label for="description">Mô tả</label>
                <textarea id="description" name="description">${event.description}</textarea>
            </div>
            
            <div class="form-group">
                <label for="eventDate">Ngày tổ chức *</label>
                <input type="date" id="eventDate" name="eventDate" value="${event.eventDate}" required>
            </div>
            
            <div class="form-group">
                <label for="location">Địa điểm *</label>
                <input type="text" id="location" name="location" value="${event.location}" required>
            </div>
            
            <div class="form-group">
                <label for="maxParticipants">Số lượng tham gia tối đa *</label>
                <input type="number" id="maxParticipants" name="maxParticipants" value="${event.maxParticipants}" min="1" required>
            </div>
            
            <div class="form-group">
                <label for="registrationDeadline">Hạn đăng ký *</label>
                <input type="date" id="registrationDeadline" name="registrationDeadline" value="${event.registrationDeadline}" required>
            </div>
            
            <div class="form-group">
                <label for="status">Trạng thái *</label>
                <select id="status" name="status" required>
                    <option value="Open" <c:if test="${event.status == 'Open'}">selected</c:if>>Open</option>
                    <option value="Closed" <c:if test="${event.status == 'Closed'}">selected</c:if>>Closed</option>
                </select>
            </div>
            
            <div>
                <button type="submit" class="btn">Lưu</button>
                <a href="${pageContext.request.contextPath}/organizer/events" class="btn btn-secondary">Hủy</a>
            </div>
        </form>
    </div>
</body>
</html>

