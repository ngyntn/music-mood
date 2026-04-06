package com.musicmood.controller;


import com.musicmood.ejb.PlaylistServiceLocal;
import com.musicmood.ejb.SongServiceLocal;
import com.musicmood.entity.Playlist;
import com.musicmood.entity.Song;
import com.musicmood.entity.User;

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
 * Xử lý toàn bộ chức năng Playlist của User:
 *   GET  /playlist          -> Danh sách playlist của user hiện tại
 *   GET  /playlist?id=...   -> Xem chi tiết (bài hát trong) 1 playlist
 *   POST /playlist          -> Tạo playlist mới
 *   POST /playlist?action=addSong -> Thêm bài hát vào playlist
 *   POST /playlist?action=delete  -> Xóa playlist
 */
@WebServlet("/playlist")
public class PlaylistServlet extends HttpServlet {

    @EJB
    private PlaylistServiceLocal playlistService;

    @EJB
    private SongServiceLocal songService;

    // =========================================================
    // GET
    // =========================================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Kiểm tra đăng nhập
        User loggedUser = getLoggedUser(req);
        if (loggedUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String playlistIdParam = req.getParameter("id");

        if (playlistIdParam != null) {
            // Xem chi tiết playlist cụ thể
            showPlaylistDetail(req, resp, loggedUser, Long.parseLong(playlistIdParam));
        } else {
            // Liệt kê tất cả playlist của user
            showPlaylistList(req, resp, loggedUser);
        }
    }

    private void showPlaylistList(HttpServletRequest req, HttpServletResponse resp, User loggedUser)
            throws ServletException, IOException {

        List<Playlist> playlists = playlistService.getPlaylistsByUser(loggedUser.getId());
        req.setAttribute("playlists", playlists);
        req.getRequestDispatcher("/views/user/playlist.jsp").forward(req, resp);
    }

    private void showPlaylistDetail(HttpServletRequest req, HttpServletResponse resp,
                                    User loggedUser, Long playlistId)
            throws ServletException, IOException {

        Playlist playlist = playlistService.findById(playlistId);

        // Bảo mật: Chỉ chủ playlist mới được xem
        if (playlist == null || !playlist.getUser().getId().equals(loggedUser.getId())) {
            resp.sendRedirect(req.getContextPath() + "/playlist");
            return;
        }

        List<Song> songsInPlaylist = playlistService.getSongsInPlaylist(playlistId);
        List<Song> allSongs = songService.getAllSongs(); // Để hiển thị dropdown thêm bài

        req.setAttribute("playlist", playlist);
        req.setAttribute("songsInPlaylist", songsInPlaylist);
        req.setAttribute("allSongs", allSongs);
        req.getRequestDispatcher("/views/user/playlist-detail.jsp").forward(req, resp);
    }

    // =========================================================
    // POST
    // =========================================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        User loggedUser = getLoggedUser(req);
        if (loggedUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        if ("addSong".equals(action)) {
            handleAddSong(req, resp, loggedUser);
        } else if ("delete".equals(action)) {
            handleDeletePlaylist(req, resp, loggedUser);
        } else {
            // Mặc định: Tạo playlist mới
            handleCreatePlaylist(req, resp, loggedUser);
        }
    }

    // =========================================================
    // PRIVATE: Tạo playlist mới
    // =========================================================

    private void handleCreatePlaylist(HttpServletRequest req, HttpServletResponse resp, User loggedUser)
            throws IOException, ServletException {

        String name = req.getParameter("playlistName");

        if (name == null || name.trim().isEmpty()) {
            req.setAttribute("errorMsg", "Tên playlist không được để trống.");
            showPlaylistList(req, resp, loggedUser);
            return;
        }

        playlistService.createPlaylist(name.trim(), loggedUser.getId());
        resp.sendRedirect(req.getContextPath() + "/playlist?created=true");
    }

    // =========================================================
    // PRIVATE: Thêm bài hát vào playlist
    // =========================================================

    private void handleAddSong(HttpServletRequest req, HttpServletResponse resp, User loggedUser)
            throws IOException {

        String playlistIdParam = req.getParameter("playlistId");
        String songIdParam = req.getParameter("songId");

        if (playlistIdParam == null || songIdParam == null) {
            resp.sendRedirect(req.getContextPath() + "/playlist");
            return;
        }

        Long playlistId = Long.parseLong(playlistIdParam);
        Long songId = Long.parseLong(songIdParam);

        // Kiểm tra quyền sở hữu
        Playlist playlist = playlistService.findById(playlistId);
        if (playlist != null && playlist.getUser().getId().equals(loggedUser.getId())) {
            playlistService.addSongToPlaylist(playlistId, songId);
        }

        resp.sendRedirect(req.getContextPath() + "/playlist?id=" + playlistId + "&songAdded=true");
    }

    // =========================================================
    // PRIVATE: Xóa playlist
    // =========================================================

    private void handleDeletePlaylist(HttpServletRequest req, HttpServletResponse resp, User loggedUser)
            throws IOException {

        String playlistIdParam = req.getParameter("playlistId");
        if (playlistIdParam == null) {
            resp.sendRedirect(req.getContextPath() + "/playlist");
            return;
        }

        Long playlistId = Long.parseLong(playlistIdParam);
        Playlist playlist = playlistService.findById(playlistId);

        // Chỉ chủ sở hữu mới được xóa
        if (playlist != null && playlist.getUser().getId().equals(loggedUser.getId())) {
            playlistService.deletePlaylist(playlistId);
        }

        resp.sendRedirect(req.getContextPath() + "/playlist");
    }

    // =========================================================
    // HELPER
    // =========================================================

    private User getLoggedUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        return (User) session.getAttribute("loggedUser");
    }
}
