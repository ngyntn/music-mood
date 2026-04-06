<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/home">🎵 MusicMood</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">Trang chủ</a>
                </li>
                <c:if test="${not empty sessionScope.loggedUser}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/playlist">Playlist của tôi</a>
                    </li>
                </c:if>
                <c:if test="${sessionScope.userRole == 'ADMIN'}">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="adminMenu" role="button" data-bs-toggle="dropdown">
                            Quản trị
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/songs">Quản lý Bài hát</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/users">Quản lý Người dùng</a></li>
                        </ul>
                    </li>
                </c:if>
            </ul>

            <ul class="navbar-nav">
                <c:choose>
                    <c:when test="${not empty sessionScope.loggedUser}">
                        <li class="nav-item">
                            <span class="nav-link text-light">Chào, ${sessionScope.loggedUser.username}</span>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-danger" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/register">Đăng ký</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>