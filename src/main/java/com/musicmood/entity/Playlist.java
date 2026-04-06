package com.musicmood.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity ánh xạ tới bảng "playlists" và bảng trung gian "playlist_songs".
 *
 * Quan hệ:
 *   - Playlist -- Many-to-One --> User      (nhiều playlist thuộc về 1 user)
 *   - Playlist -- Many-to-Many --> Song     (1 playlist có nhiều bài, 1 bài trong nhiều playlist)
 */
@Entity
@Table(name = "playlists")
public class Playlist implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== FIELDS =====

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    /**
     * Quan hệ Many-to-One: Nhiều Playlist thuộc về 1 User.
     * @JoinColumn(name = "user_id") chỉ định cột FK trong bảng "playlists".
     * fetch = LAZY: Không load User object ngay khi query Playlist.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Quan hệ Many-to-Many giữa Playlist và Song.
     *
     * @JoinTable định nghĩa bảng trung gian "playlist_songs":
     *   - joinColumns: FK trỏ về bảng "playlists" (playlist_id)
     *   - inverseJoinColumns: FK trỏ về bảng "songs" (song_id)
     *
     * cascade = PERSIST, MERGE: Khi lưu Playlist thì lưu luôn các Song mới (nếu có).
     * KHÔNG dùng cascade = REMOVE để tránh xóa Song khi xóa Playlist.
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "playlist_songs",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songs = new ArrayList<>();

    // ===== CONSTRUCTORS =====

    public Playlist() {}

    public Playlist(String name, User user) {
        this.name = name;
        this.user = user;
    }

    // ===== HELPER METHODS =====

    /**
     * Thêm một bài hát vào playlist.
     * Kiểm tra trùng lặp trước khi thêm.
     */
    public void addSong(Song song) {
        if (!this.songs.contains(song)) {
            this.songs.add(song);
        }
    }

    /**
     * Xóa một bài hát khỏi playlist.
     */
    public void removeSong(Song song) {
        this.songs.remove(song);
    }

    // ===== GETTERS & SETTERS =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Song> getSongs() { return songs; }
    public void setSongs(List<Song> songs) { this.songs = songs; }

    @Override
    public String toString() {
        return "Playlist{id=" + id + ", name='" + name + "'}";
    }
}
