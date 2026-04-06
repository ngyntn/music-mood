<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>${editMode ? 'Sửa bài hát' : 'Thêm bài hát mới'} - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="/views/common/navbar.jsp" />

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow-sm">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">${editMode ? 'Chỉnh sửa thông tin bài hát' : 'Thêm bài hát mới'}</h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty errorMsg}">
                            <div class="alert alert-danger">${errorMsg}</div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/admin/songs?action=${editMode ? 'edit' : 'add'}" method="post">
                            <c:if test="${editMode}">
                                <input type="hidden" name="id" value="${song.id}">
                            </c:if>

                            <div class="mb-3">
                                <label class="form-label fw-bold">Tên bài hát *</label>
                                <input type="text" name="title" class="form-control" value="${song.title}" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold">Ca sĩ trình bày *</label>
                                <input type="text" name="artist" class="form-control" value="${song.artist}" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold">Đường dẫn file MP3 (URL) *</label>
                                <input type="text" name="audioUrl" class="form-control" value="${song.audioUrl}" placeholder="VD: https://example.com/music.mp3" required>
                                <small class="text-muted">Nhập link direct tới file audio.</small>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold">Đường dẫn ảnh bìa (Cover URL)</label>
                                <input type="text" name="imageUrl" class="form-control" value="${song.imageUrl}" placeholder="VD: https://example.com/cover.jpg">
                            </div>

                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/admin/songs" class="btn btn-secondary">Hủy bỏ</a>
                                <button type="submit" class="btn btn-success">Lưu bài hát</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>