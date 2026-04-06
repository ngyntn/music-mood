package com.musicmood.ejb;


import com.musicmood.entity.Playlist;
import com.musicmood.entity.Song;
import javax.ejb.Local;
import java.util.List;

/**
 * Local Interface cho PlaylistSessionBean.
 */
@Local
public interface PlaylistServiceLocal {

    /**
     * Tạo một Playlist mới cho user.
     * @param name   Tên playlist.
     * @param userId ID của user sở hữu playlist.
     * @return Playlist object đã được persist (có ID từ DB).
     */
    Playlist createPlaylist(String name, Long userId);

    /**
     * Thêm một bài hát vào playlist.
     * Không thêm nếu bài hát đã có trong playlist.
     */
    void addSongToPlaylist(Long playlistId, Long songId);

    /**
     * Lấy danh sách tất cả bài hát trong một playlist.
     */
    List<Song> getSongsInPlaylist(Long playlistId);

    /**
     * Lấy tất cả playlist của một user.
     */
    List<Playlist> getPlaylistsByUser(Long userId);

    /**
     * Lấy thông tin một playlist theo ID.
     */
    Playlist findById(Long playlistId);

    /**
     * Xóa playlist theo ID (chỉ xóa playlist, không xóa bài hát).
     */
    void deletePlaylist(Long playlistId);
}
