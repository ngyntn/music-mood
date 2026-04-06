<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Bài hát - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="/views/common/navbar.jsp" />

    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Quản lý Bài hát</h2>
            <a href="${pageContext.request.contextPath}/admin/songs?action=add" class="btn btn-primary">+ Thêm Bài Hát</a>
        </div>

        <c:if test="${param.added == 'true'}"><div class="alert alert-success">Thêm thành công!</div></c:if>
        <c:if test="${param.updated == 'true'}"><div class="alert alert-success">Cập nhật thành công!</div></c:if>
        <c:if test="${param.deleted == 'true'}"><div class="alert alert-warning">Đã xóa bài hát!</div></c:if>

        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle">
                <thead class="table-dark">
                    <tr>
                        <th width="5%">ID</th>
                        <th width="10%">Ảnh bìa</th>
                        <th width="25%">Tên bài hát</th>
                        <th width="20%">Ca sĩ</th>
                        <th width="25%">File nhạc (Audio)</th>
                        <th width="15%">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="song" items="${songs}">
                        <tr>
                            <td>${song.id}</td>
                            <td><img src="${empty song.imageUrl ? 'https://via.placeholder.com/50' : song.imageUrl}" width="50" height="50" style="object-fit: cover;" class="rounded"></td>
                            <td class="fw-bold">${song.title}</td>
                            <td>${song.artist}</td>
                            <td>
                                <audio controls style="height: 30px; width: 200px;">
                                    <source src="${song.audioUrl}" type="audio/mpeg">
                                </audio>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/songs?action=edit&id=${song.id}" class="btn btn-sm btn-warning">Sửa</a>
                                <form action="${pageContext.request.contextPath}/admin/songs?action=delete" method="post" class="d-inline" onsubmit="return confirm('Bạn có chắc chắn muốn xóa bài này?');">
                                    <input type="hidden" name="id" value="${song.id}">
                                    <button type="submit" class="btn btn-sm btn-danger">Xóa</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty songs}">
                        <tr><td colspan="6" class="text-center">Chưa có bài hát nào trong hệ thống.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>