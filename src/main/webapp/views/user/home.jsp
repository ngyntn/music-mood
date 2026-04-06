<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>MusicMood - Nghe nhạc trực tuyến</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="/views/common/navbar.jsp" />

    <div class="container">
        <div class="row justify-content-center mb-5">
            <div class="col-md-6">
                <form action="${pageContext.request.contextPath}/home" method="get" class="d-flex">
                    <input class="form-control me-2" type="search" name="q" placeholder="Tìm kiếm bài hát, ca sĩ..." value="${keyword}">
                    <button class="btn btn-outline-primary" type="submit">Tìm</button>
                </form>
            </div>
        </div>

        <h4 class="mb-4">
            <c:choose>
                <c:when test="${searchMode}">Kết quả tìm kiếm cho: "${keyword}"</c:when>
                <c:otherwise>Khám phá bài hát mới</c:otherwise>
            </c:choose>
        </h4>

        <div class="row row-cols-1 row-cols-md-3 row-cols-lg-4 g-4">
            <c:if test="${empty songs}">
                <div class="col-12"><p class="text-muted">Không tìm thấy bài hát nào.</p></div>
            </c:if>

            <c:forEach var="song" items="${songs}">
                <div class="col">
                    <div class="card h-100 shadow-sm">
                        <img src="${empty song.imageUrl ? 'https://via.placeholder.com/300x300.png?text=No+Image' : song.imageUrl}" class="card-img-top" alt="Cover" style="height: 200px; object-fit: cover;">
                        <div class="card-body text-center">
                            <h6 class="card-title text-truncate" title="${song.title}">${song.title}</h6>
                            <p class="card-text text-muted small">${song.artist}</p>
                            <audio controls class="w-100 mt-2" style="height: 35px;">
                                <source src="${song.audioUrl}" type="audio/mpeg">
                                Trình duyệt không hỗ trợ audio.
                            </audio>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>