<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Playlist của tôi - MusicMood</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="/views/common/navbar.jsp" />

    <div class="container">
        <div class="row mb-4">
            <div class="col-md-6">
                <h2>Playlist Của Tôi</h2>
            </div>
            <div class="col-md-6 text-md-end">
                <form action="${pageContext.request.contextPath}/playlist" method="post" class="d-flex justify-content-end">
                    <input type="text" name="playlistName" class="form-control w-50 me-2" placeholder="Tên playlist mới..." required>
                    <button type="submit" class="btn btn-success">Tạo mới</button>
                </form>
            </div>
        </div>

        <c:if test="${not empty errorMsg}"><div class="alert alert-danger">${errorMsg}</div></c:if>
        <c:if test="${param.created == 'true'}"><div class="alert alert-success">Tạo Playlist thành công!</div></c:if>

        <div class="row row-cols-1 row-cols-md-4 g-4 mt-2">
            <c:if test="${empty playlists}">
                <div class="col-12"><p class="text-muted">Bạn chưa có Playlist nào.</p></div>
            </c:if>
            <c:forEach var="p" items="${playlists}">
                <div class="col">
                    <div class="card h-100 border-primary text-center shadow-sm">
                        <div class="card-body d-flex flex-column justify-content-center py-5">
                            <h5 class="card-title text-primary">${p.name}</h5>
                        </div>
                        <div class="card-footer bg-white border-top-0 pb-3">
                            <a href="${pageContext.request.contextPath}/playlist?id=${p.id}" class="btn btn-outline-primary btn-sm w-100 mb-2">Mở Playlist</a>

                            <form action="${pageContext.request.contextPath}/playlist?action=delete" method="post" onsubmit="return confirm('Xóa playlist này?');">
                                <input type="hidden" name="playlistId" value="${p.id}">
                                <button type="submit" class="btn btn-outline-danger btn-sm w-100">Xóa</button>
                            </form>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>