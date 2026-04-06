package com.musicmood.ejb;


import com.musicmood.entity.Song;
import javax.ejb.Local;
import java.util.List;

/**
 * Local Interface cho SongSessionBean.
 */
@Local
public interface SongServiceLocal {

    /** Lấy toàn bộ danh sách bài hát (cho trang chủ và Admin). */
    List<Song> getAllSongs();

    /**
     * Tìm kiếm bài hát theo từ khóa.
     * Tìm trong cả title lẫn artist (không phân biệt hoa thường).
     */
    List<Song> searchSongs(String keyword);

    /** Lấy thông tin 1 bài hát theo ID. */
    Song findById(Long id);

    /** Thêm bài hát mới (Admin). */
    void addSong(Song song);

    /** Cập nhật thông tin bài hát (Admin). */
    void updateSong(Song song);

    /** Xóa bài hát khỏi hệ thống (Admin). */
    void deleteSong(Long id);
}
