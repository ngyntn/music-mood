package com.musicmood.ejb;


import com.musicmood.entity.Song;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Stateless Session Bean xử lý toàn bộ logic liên quan đến Song (CRUD).
 */
@Stateless
public class SongSessionBean implements SongServiceLocal {

    @PersistenceContext(unitName = "MusicPU")
    private EntityManager em;

    // ===================================================
    // READ - Lấy tất cả bài hát
    // ===================================================

    @Override
    public List<Song> getAllSongs() {
        return em.createQuery("SELECT s FROM Song s ORDER BY s.id DESC", Song.class)
                .getResultList();
    }

    // ===================================================
    // SEARCH - Tìm kiếm theo tên hoặc ca sĩ
    // ===================================================

    @Override
    public List<Song> searchSongs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllSongs();
        }

        // LOWER() để tìm không phân biệt hoa/thường
        // LIKE '%keyword%' để tìm chuỗi con
        String likePattern = "%" + keyword.trim().toLowerCase() + "%";

        TypedQuery<Song> query = em.createQuery(
                "SELECT s FROM Song s WHERE LOWER(s.title) LIKE :kw OR LOWER(s.artist) LIKE :kw " +
                        "ORDER BY s.title ASC",
                Song.class
        );
        query.setParameter("kw", likePattern);
        return query.getResultList();
    }

    // ===================================================
    // FIND BY ID
    // ===================================================

    @Override
    public Song findById(Long id) {
        return em.find(Song.class, id);
    }

    // ===================================================
    // CREATE - Thêm bài hát mới
    // ===================================================

    @Override
    public void addSong(Song song) {
        em.persist(song);
    }

    // ===================================================
    // UPDATE - Cập nhật thông tin bài hát
    // ===================================================

    @Override
    public void updateSong(Song song) {
        // em.merge() xử lý cả trường hợp entity đang detached
        em.merge(song);
    }

    // ===================================================
    // DELETE - Xóa bài hát
    // ===================================================

    @Override
    public void deleteSong(Long id) {
        Song song = em.find(Song.class, id);
        if (song != null) {
            // Trước khi xóa Song, cần xóa nó khỏi tất cả Playlist liên quan
            // để tránh lỗi FK constraint trên bảng playlist_songs
            if (song.getPlaylists() != null) {
                for (var playlist : song.getPlaylists()) {
                    playlist.getSongs().remove(song);
                }
            }
            em.remove(song);
        }
    }
}
