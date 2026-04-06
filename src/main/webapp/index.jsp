<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Chuyển hướng người dùng vào HomeServlet
    response.sendRedirect(request.getContextPath() + "/home");
%>