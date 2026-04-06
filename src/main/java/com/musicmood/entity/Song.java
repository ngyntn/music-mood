package com.musicmood.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity ánh xạ tới bảng "songs" trong Database.
 */
@Entity
@Table(name = "songs")
public class Song implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== FIELDS =====

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "artist", nullable = false, length = 150)
    private String artist;

    /**
     * Đường dẫn tới file MP3.
     * Có thể là URL tuyệt đối (https://...) hoặc đường dẫn tương đối (/assets/music/...)
     */
    @Column(name = "audio_url", nullable = false, length = 500)
    private String audioUrl;

    /**
     * Đường dẫn tới ảnh bìa bài hát.
     */
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /**
     * Quan hệ Many-to-Many ngược lại từ phía Song.
     * mappedBy = "songs" trỏ tới field "songs" bên trong class Playlist.
     * KHÔNG dùng cascade ở đây để tránh vô tình xóa Playlist khi xóa Song.
     */
    @ManyToMany(mappedBy = "songs", fetch = FetchType.LAZY)
    private List<Playlist> playlists = new ArrayList<>();

    // ===== CONSTRUCTORS =====

    public Song() {}

    public Song(String title, String artist, String audioUrl, String imageUrl) {
        this.title = title;
        this.artist = artist;
        this.audioUrl = audioUrl;
        this.imageUrl = imageUrl;
    }

    // ===== GETTERS & SETTERS =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<Playlist> getPlaylists() { return playlists; }
    public void setPlaylists(List<Playlist> playlists) { this.playlists = playlists; }

    @Override
    public String toString() {
        return "Song{id=" + id + ", title='" + title + "', artist='" + artist + "'}";
    }
}
