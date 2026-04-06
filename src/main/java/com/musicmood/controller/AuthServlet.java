package com.musicmood.controller;


import com.musicmood.ejb.UserServiceLocal;
import com.musicmood.entity.User;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Xử lý các request liên quan đến xác thực:
 *   GET  /login    -> Hiển thị form đăng nhập
 *   POST /login    -> Xử lý đăng nhập
 *   GET  /logout   -> Đăng xuất, hủy session
 *   GET  /register -> Hiển thị form đăng ký
 *   POST /register -> Xử lý đăng ký tài khoản
 */
@WebServlet(urlPatterns = {"/login", "/logout", "/register"})
public class AuthServlet extends HttpServlet {

    /**
     * @EJB: Container tự inject UserSessionBean vào đây.
     * Không cần new, không cần lookup JNDI thủ công.
     */
    @EJB
    private UserServiceLocal userService;

    // =========================================================
    // GET: Hiển thị các trang form
    // =========================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        switch (path) {
            case "/login":
                // Nếu đã đăng nhập rồi thì chuyển về trang chủ
                if (isLoggedIn(req)) {
                    resp.sendRedirect(req.getContextPath() + "/home");
                    return;
                }
                req.getRequestDispatcher("/views/user/login.jsp").forward(req, resp);
                break;

            case "/register":
                if (isLoggedIn(req)) {
                    resp.sendRedirect(req.getContextPath() + "/home");
                    return;
                }
                req.getRequestDispatcher("/views/user/register.jsp").forward(req, resp);
                break;

            case "/logout":
                HttpSession session = req.getSession(false);
                if (session != null) {
                    session.invalidate(); // Hủy toàn bộ session
                }
                resp.sendRedirect(req.getContextPath() + "/login");
                break;

            default:
                resp.sendRedirect(req.getContextPath() + "/home");
        }
    }

    // =========================================================
    // POST: Xử lý submit form
    // =========================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();

        if ("/login".equals(path)) {
            handleLogin(req, resp);
        } else if ("/register".equals(path)) {
            handleRegister(req, resp);
        }
    }

    // =========================================================
    // PRIVATE: Xử lý đăng nhập
    // =========================================================

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Validate input cơ bản
        if (isBlank(username) || isBlank(password)) {
            req.setAttribute("errorMsg", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            req.getRequestDispatcher("/views/user/login.jsp").forward(req, resp);
            return;
        }

        // Gọi EJB để xác thực
        User user = userService.login(username.trim(), password.trim());

        if (user == null) {
            req.setAttribute("errorMsg", "Tên đăng nhập hoặc mật khẩu không đúng.");
            req.getRequestDispatcher("/views/user/login.jsp").forward(req, resp);
            return;
        }

        // Đăng nhập thành công -> Lưu user vào session
        HttpSession session = req.getSession(true);
        session.setAttribute("loggedUser", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userRole", user.getRole());
        session.setMaxInactiveInterval(30 * 60); // Session hết hạn sau 30 phút

        // Phân luồng dựa theo role
        if ("ADMIN".equals(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/admin/songs");
        } else {
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }

    // =========================================================
    // PRIVATE: Xử lý đăng ký
    // =========================================================

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        // Validate
        if (isBlank(username) || isBlank(password) || isBlank(confirmPassword)) {
            req.setAttribute("errorMsg", "Vui lòng điền đầy đủ thông tin.");
            req.getRequestDispatcher("/views/user/register.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirmPassword)) {
            req.setAttribute("errorMsg", "Mật khẩu xác nhận không khớp.");
            req.getRequestDispatcher("/views/user/register.jsp").forward(req, resp);
            return;
        }

        if (username.trim().length() < 3) {
            req.setAttribute("errorMsg", "Tên đăng nhập phải có ít nhất 3 ký tự.");
            req.getRequestDispatcher("/views/user/register.jsp").forward(req, resp);
            return;
        }

        // Gọi EJB để đăng ký
        User newUser = new User(username.trim(), password.trim(), "USER");
        boolean success = userService.register(newUser);

        if (!success) {
            req.setAttribute("errorMsg", "Tên đăng nhập '" + username + "' đã được sử dụng.");
            req.getRequestDispatcher("/views/user/register.jsp").forward(req, resp);
            return;
        }

        // Đăng ký thành công -> Chuyển về trang login với thông báo
        resp.sendRedirect(req.getContextPath() + "/login?registered=true");
    }

    // =========================================================
    // HELPER
    // =========================================================

    private boolean isLoggedIn(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute("loggedUser") != null;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
