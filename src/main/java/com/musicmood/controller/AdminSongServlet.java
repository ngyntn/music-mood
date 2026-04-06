package com.musicmood.controller;


import com.musicmood.ejb.SongServiceLocal;
import com.musicmood.entity.Song;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Xử lý toàn bộ CRUD bài hát cho Admin.
 *   GET  /admin/songs             -> Danh sách bài hát
 *   GET  /admin/songs?action=add  -> Form thêm mới
 *   GET  /admin/songs?action=edit&id=... -> Form sửa
 *   POST /admin/songs?action=add  -> Xử lý thêm bài hát
 *   POST /admin/songs?action=edit -> Xử lý cập nhật bài hát
 *   POST /admin/songs?action=delete&id=... -> Xóa bài hát
 */
@WebServlet("/admin/songs")
public class AdminSongServlet extends HttpServlet {

    @EJB
    private SongServiceLocal songService;

    // =========================================================
    // GET
    // =========================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Kiểm tra quyền Admin
        if (!isAdmin(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        if ("add".equals(action)) {
            // Hiển thị form thêm mới
            req.getRequestDispatcher("/views/admin/song-form.jsp").forward(req, resp);

        } else if ("edit".equals(action)) {
            // Hiển thị form chỉnh sửa với dữ liệu hiện tại
            String idParam = req.getParameter("id");
            if (idParam == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/songs");
                return;
            }
            Song song = songService.findById(Long.parseLong(idParam));
            if (song == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/songs");
                return;
            }
            req.setAttribute("song", song);
            req.setAttribute("editMode", true);
            req.getRequestDispatcher("/views/admin/song-form.jsp").forward(req, resp);

        } else {
            // Trang danh sách bài hát
            List<Song> songs = songService.getAllSongs();
            req.setAttribute("songs", songs);
            req.getRequestDispatcher("/views/admin/manage-songs.jsp").forward(req, resp);
        }
    }

    // =========================================================
    // POST
    // =========================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        if (!isAdmin(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        if ("add".equals(action)) {
            handleAddSong(req, resp);
        } else if ("edit".equals(action)) {
            handleEditSong(req, resp);
        } else if ("delete".equals(action)) {
            handleDeleteSong(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/songs");
        }
    }

    // =========================================================
    // PRIVATE: Thêm bài hát
    // =========================================================

    private void handleAddSong(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Song song = buildSongFromRequest(req);

        if (song == null) {
            req.setAttribute("errorMsg", "Vui lòng điền đầy đủ Tên bài hát, Ca sĩ và URL nhạc.");
            req.getRequestDispatcher("/views/admin/song-form.jsp").forward(req, resp);
            return;
        }

        songService.addSong(song);
        resp.sendRedirect(req.getContextPath() + "/admin/songs?added=true");
    }

    // =========================================================
    // PRIVATE: Sửa bài hát
    // =========================================================

    private void handleEditSong(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/songs");
            return;
        }

        Song existing = songService.findById(Long.parseLong(idParam));
        if (existing == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/songs");
            return;
        }

        // Cập nhật các field từ form
        String title = req.getParameter("title");
        String artist = req.getParameter("artist");
        String audioUrl = req.getParameter("audioUrl");
        String imageUrl = req.getParameter("imageUrl");

        if (isBlank(title) || isBlank(artist) || isBlank(audioUrl)) {
            req.setAttribute("errorMsg", "Vui lòng điền đầy đủ Tên bài hát, Ca sĩ và URL nhạc.");
            req.setAttribute("song", existing);
            req.setAttribute("editMode", true);
            req.getRequestDispatcher("/views/admin/song-form.jsp").forward(req, resp);
            return;
        }

        existing.setTitle(title.trim());
        existing.setArtist(artist.trim());
        existing.setAudioUrl(audioUrl.trim());
        existing.setImageUrl(imageUrl != null ? imageUrl.trim() : "");

        songService.updateSong(existing);
        resp.sendRedirect(req.getContextPath() + "/admin/songs?updated=true");
    }

    // =========================================================
    // PRIVATE: Xóa bài hát
    // =========================================================

    private void handleDeleteSong(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idParam = req.getParameter("id");
        if (idParam != null) {
            songService.deleteSong(Long.parseLong(idParam));
        }
        resp.sendRedirect(req.getContextPath() + "/admin/songs?deleted=true");
    }

    // =========================================================
    // HELPER
    // =========================================================

    /**
     * Đọc và validate dữ liệu từ form, trả về Song object hoặc null nếu invalid.
     */
    private Song buildSongFromRequest(HttpServletRequest req) {
        String title = req.getParameter("title");
        String artist = req.getParameter("artist");
        String audioUrl = req.getParameter("audioUrl");
        String imageUrl = req.getParameter("imageUrl");

        if (isBlank(title) || isBlank(artist) || isBlank(audioUrl)) {
            return null;
        }

        return new Song(
                title.trim(),
                artist.trim(),
                audioUrl.trim(),
                imageUrl != null ? imageUrl.trim() : ""
        );
    }

    private boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;
        return "ADMIN".equals(session.getAttribute("userRole"));
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
