package com.musicmood.controller;


import com.musicmood.ejb.UserServiceLocal;
import com.musicmood.entity.User;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Xử lý trang quản lý người dùng cho Admin.
 *   GET /admin/users -> Hiển thị danh sách toàn bộ người dùng
 *
 * Lưu ý: Chức năng này chỉ cần "đọc" danh sách, không có CRUD phức tạp.
 * Để đơn giản, chúng ta sẽ query trực tiếp trong Servlet bằng cách
 * mở rộng UserServiceLocal hoặc dùng thêm một EJB nếu cần.
 */
@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {

    @EJB
    private UserServiceLocal userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!isAdmin(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Lấy danh sách user qua EJB
        List<User> users = userService.getAllUsers();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/views/admin/manage-users.jsp").forward(req, resp);
    }

    private boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;
        return "ADMIN".equals(session.getAttribute("userRole"));
    }
}
