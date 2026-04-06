package com.musicmood.ejb;


import com.musicmood.entity.Playlist;
import com.musicmood.entity.Song;
import com.musicmood.entity.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Stateless Session Bean xử lý toàn bộ logic liên quan đến Playlist.
 *
 * Lưu ý transaction: Vì @Stateless, mỗi method tự động chạy trong
 * một JTA transaction (TransactionAttributeType.REQUIRED mặc định).
 * Container sẽ commit sau khi method kết thúc thành công, hoặc
 * rollback nếu có RuntimeException.
 */
@Stateless
public class PlaylistSessionBean implements PlaylistServiceLocal {

    @PersistenceContext(unitName = "MusicPU")
    private EntityManager em;

    // ===================================================
    // CREATE PLAYLIST
    // ===================================================

    @Override
    public Playlist createPlaylist(String name, Long userId) {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("Không tìm thấy User với ID: " + userId);
        }

        Playlist playlist = new Playlist(name, user);
        em.persist(playlist);

        // Sau persist, playlist.getId() đã có giá trị từ DB
        return playlist;
    }

    // ===================================================
    // ADD SONG TO PLAYLIST
    // ===================================================

    @Override
    public void addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = em.find(Playlist.class, playlistId);
        Song song = em.find(Song.class, songId);

        if (playlist == null) {
            throw new IllegalArgumentException("Không tìm thấy Playlist với ID: " + playlistId);
        }
        if (song == null) {
            throw new IllegalArgumentException("Không tìm thấy Song với ID: " + songId);
        }

        // Helper method trong Playlist đã kiểm tra trùng lặp
        playlist.addSong(song);

        // em.merge() đảm bảo thay đổi được ghi vào bảng playlist_songs
        em.merge(playlist);
    }

    // ===================================================
    // GET SONGS IN PLAYLIST
    // ===================================================

    @Override
    public List<Song> getSongsInPlaylist(Long playlistId) {
        Playlist playlist = em.find(Playlist.class, playlistId);
        if (playlist == null) {
            return new ArrayList<>();
        }

        // Gọi tường minh để trigger LAZY loading trong cùng transaction
        List<Song> songs = playlist.getSongs();

        // Khởi tạo collection để tránh LazyInitializationException
        // khi Servlet truy cập ngoài transaction scope
        songs.size(); // trigger load
        return songs;
    }

    // ===================================================
    // GET PLAYLISTS BY USER
    // ===================================================

    @Override
    public List<Playlist> getPlaylistsByUser(Long userId) {
        TypedQuery<Playlist> query = em.createQuery(
                "SELECT p FROM Playlist p WHERE p.user.id = :userId ORDER BY p.id DESC",
                Playlist.class
        );
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    // ===================================================
    // FIND BY ID
    // ===================================================

    @Override
    public Playlist findById(Long playlistId) {
        return em.find(Playlist.class, playlistId);
    }

    // ===================================================
    // DELETE PLAYLIST
    // ===================================================

    @Override
    public void deletePlaylist(Long playlistId) {
        Playlist playlist = em.find(Playlist.class, playlistId);
        if (playlist != null) {
            // Xóa các liên kết trong bảng playlist_songs trước (tự động do JPA quản lý JoinTable)
            playlist.getSongs().clear();
            em.merge(playlist);     // Flush xóa rows trong playlist_songs
            em.remove(playlist);    // Xóa row trong bảng playlists
        }
    }
}
