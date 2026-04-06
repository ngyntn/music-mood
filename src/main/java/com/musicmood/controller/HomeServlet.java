package com.musicmood.controller;


import com.musicmood.ejb.SongServiceLocal;
import com.musicmood.entity.Song;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Xử lý trang chủ và chức năng tìm kiếm bài hát.
 *   GET /home          -> Hiển thị toàn bộ bài hát
 *   GET /home?q=...    -> Hiển thị kết quả tìm kiếm
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @EJB
    private SongServiceLocal songService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String keyword = req.getParameter("q");
        List<Song> songs;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Tìm kiếm
            songs = songService.searchSongs(keyword.trim());
            req.setAttribute("keyword", keyword.trim());
            req.setAttribute("searchMode", true);
        } else {
            // Lấy toàn bộ danh sách
            songs = songService.getAllSongs();
            req.setAttribute("searchMode", false);
        }

        req.setAttribute("songs", songs);
        req.getRequestDispatcher("/views/user/home.jsp").forward(req, resp);
    }
}
