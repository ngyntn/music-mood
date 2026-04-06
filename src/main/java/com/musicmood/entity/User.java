package com.musicmood.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity ánh xạ tới bảng "users" trong Database.
 * Serializable bắt buộc khi Entity được dùng trong EJB context.
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== FIELDS =====

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /**
     * Role của người dùng: "ADMIN" hoặc "USER"
     * Dùng EnumType.STRING để lưu text vào DB thay vì số thứ tự.
     */
    @Column(name = "role", nullable = false, length = 10)
    private String role;

    /**
     * Một User có thể có nhiều Playlist (One-to-Many).
     * mappedBy = "user" trỏ tới field "user" bên trong class Playlist.
     * cascade = ALL: Khi xóa User thì xóa luôn các Playlist của họ.
     * fetch = LAZY: Chỉ load Playlists khi được gọi tường minh (tránh N+1 query).
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Playlist> playlists = new ArrayList<>();

    // ===== CONSTRUCTORS =====

    public User() {}

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // ===== GETTERS & SETTERS =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<Playlist> getPlaylists() { return playlists; }
    public void setPlaylists(List<Playlist> playlists) { this.playlists = playlists; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role='" + role + "'}";
    }
}
