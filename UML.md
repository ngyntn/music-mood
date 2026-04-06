
## 📊 Các Mô Hình UML Được Đề Xuất

### 1. **Use Case Diagram** (Sơ đồ Trường Hợp Sử Dụng)

```
┌─────────────────────────────────────────┐
│         Music Management System          │
├─────────────────────────────────────────┤
│                                          │
│  ┌─────────────┐         ┌────────────┐ │
│  │ User        │         │ Admin      │ │
│  └──────┬──────┘         └─────┬──────┘ │
│         │                       │        │
│         │  ┌──────────────────┐ │        │
│         ├──┤ Authentication   ├─┤        │
│         │  │ (Register/Login) │ │        │
│         │  └──────────────────┘ │        │
│         │                       │        │
│         │  ┌──────────────────┐ │        │
│         ├──┤ Browse Songs     ├─┤        │
│         │  └──────────────────┘ │        │
│         │                       │        │
│         │  ┌──────────────────┐ │        │
│         ├──┤ Search Songs     │ │        │
│         │  └──────────────────┘ │        │
│         │                       │        │
│         │  ┌──────────────────┐ │        │
│         ├──┤ Play Music       │ │        │
│         │  └──────────────────┘ │        │
│         │                       │        │
│         │  ┌──────────────────┐ │        │
│         ├──┤ Manage Playlist  ├─┤        │
│         │  └──────────────────┘ │        │
│         │                       │        │
│         │                  ┌────┴──────┐ │
│         │                  │ Manage    │ │
│         │                  │ Library   │ │
│         │                  │ (CRUD)    │ │
│         │                  └───────────┘ │
│         │                       │        │
│         │                  ┌────┴──────┐ │
│         │                  │ Manage    │ │
│         │                  │ Users     │ │
│         │                  └───────────┘ │
│         │                                │
└─────────────────────────────────────────┘
```

### 2. **Class Diagram** (Sơ đồ Lớp - Entity & Business Logic)

```
┌──────────────────────────────────────────────────────────────────┐
│                       ENTITY LAYER (JPA)                         │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌─────────────────────────┐    ┌──────────────────────────┐    │
│  │ <<Entity>> User         │    │ <<Entity>> Song          │    │
│  ├─────────────────────────┤    ├──────────────────────────┤    │
│  │ - userId: Long          │    │ - songId: Long           │    │
│  │ - username: String      │    │ - title: String          │    │
│  │ - password: String      │    │ - artist: String         │    │
│  │ - email: String         │    │ - album: String          │    │
│  │ - role: UserRole        │    │ - audioUrl: String       │    │
│  │ - createdAt: Date       │    │ - imageUrl: String       │    │
│  │ - playlists: List       │────│ - duration: Integer      │    │
│  └─────────────────────────┘    │ - playlists: List        │    │
│                                  └──────────────────────────┘    │
│                                                                   │
│  ┌─────────────────────────┐    ┌──────────────────────────┐    │
│  │ <<Entity>> Playlist     │    │ <<Enum>> UserRole        │    │
│  ├─────────────────────────┤    ├──────────────────────────┤    │
│  │ - playlistId: Long      │    │ ADMIN                    │    │
│  │ - name: String          │    │ USER                     │    │
│  │ - description: String   │    └──────────────────────────┘    │
│  │ - user: User            │                                     │
│  │ - songs: List<Song>     │                                     │
│  │ - createdAt: Date       │                                     │
│  │ - updatedAt: Date       │                                     │
│  └─────────────────────────┘                                     │
│                                                                   │
└───────────────────���──────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│                      BUSINESS LOGIC LAYER (EJB)                  │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌──────────────────────────┐ ┌──────────────────────────────┐  │
│  │ <<Interface>>            │ │ <<Stateless Session Bean>>   │  │
│  │ UserServiceLocal         │ │ UserServiceBean implements   │  │
│  ├──────────────────────────┤ │ UserServiceLocal             │  │
│  │ + register(User): void   │ ├──────────────────────────────┤  │
│  │ + login(String, String): │ │ - userRepository             │  │
│  │   User                   │ │ + register(User): void       │  │
│  │ + getUserById(Long):     │ │ + login(String, String):     │  │
│  │   User                   │ │   User                       │  │
│  │ + getAllUsers(): List    │ │ + getUserById(Long): User    │  │
│  │ + updateUser(User): void │ │ + getAllUsers(): List        │  │
│  └──────────────────────────┘ │ + updateUser(User): void     │  │
│                                │ + validateCredentials()      │  │
│                                └──────────────────────────────┘  │
│                                                                   │
│  ┌──────────────────────────┐ ┌──────────────────────────────┐  │
│  │ <<Interface>>            │ │ <<Stateless Session Bean>>   │  │
│  │ SongServiceLocal         │ │ SongServiceBean implements   │  │
│  ├──────────────────────────┤ │ SongServiceLocal             │  │
│  │ + addSong(Song): void    │ ├──────────────────────────────┤  │
│  │ + updateSong(Song): void │ │ - songRepository             │  │
│  │ + deleteSong(Long): void │ │ + addSong(Song): void        │  │
│  │ + getSongById(Long):     │ │ + updateSong(Song): void     │  │
│  │   Song                   │ │ + deleteSong(Long): void     │  │
│  │ + getAllSongs(): List    │ │ + getSongById(Long): Song    │  │
│  │ + searchSongs(String):   │ │ + getAllSongs(): List        │  │
│  │   List                   │ │ + searchSongs(String):       │  │
│  │ + getSongsByArtist():    │ │   List                       │  │
│  │   List                   │ │ + getSongsByArtist(String):  │  │
│  └──────────────────────────┘ │   List                       │  │
│                                └──────────────────────────────┘  │
│                                                                   │
│  ┌──────────────────────────┐ ┌──────────────────────────────┐  │
│  │ <<Interface>>            │ │ <<Stateless Session Bean>>   │  │
│  │ PlaylistServiceLocal     │ │ PlaylistServiceBean          │  │
│  ├──────────────────────────┤ │ implements                   │  │
│  │ + createPlaylist():      │ │ PlaylistServiceLocal         │  │
│  │   void                   │ ├──────────────────────────────┤  │
│  │ + addSongToPlaylist():   │ │ - playlistRepository         │  │
│  │   void                   │ │ + createPlaylist(): void     │  │
│  │ + removeSongFromPlaylist │ │ + addSongToPlaylist(): void  │  │
│  │   (): void               │ │ + removeSongFromPlaylist():  │  │
│  │ + getPlaylistsByUser():  │ │   void                       │  │
│  │   List                   │ │ + getPlaylistsByUser():      │  │
│  │ + deletePlaylist(Long):  │ │   List                       │  │
│  │   void                   │ │ + deletePlaylist(Long):      │  │
│  │ + updatePlaylist():      │ │   void                       │  │
│  │   void                   │ │ + updatePlaylist(): void     │  │
│  └──────────────────────────┘ └──────────────────────────────┘  │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER (Web)                      │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌──────────────────────┐   ┌──────────────────────────────┐    │
│  │ <<Servlet>>          │   │ <<Servlet>>                  │    │
│  │ AuthController       │   │ SongController               │    │
│  ├──────────────────────┤   ├──────────────────────────────┤    │
│  │ - userService        │   │ - songService                │    │
│  │ + doGet(): void      │   │ + doGet(): void              │    │
│  │ + doPost(): void     │   │ + doPost(): void             │    │
│  └──────────────────────┘   └──────────────────────────────┘    │
│           │                              │                       │
│           └──────────────┬───────────────┘                       │
│                          │                                       │
│  ┌──────────────────────┐│  ┌──────────────────────────────┐    │
│  │ <<Servlet>>          ││  │ <<JSP>>                      │    │
│  │ PlaylistController   ││  │ home.jsp, profile.jsp,       │    │
│  ├──────────────────────┤│  │ library.jsp, admin.jsp...    │    │
│  │ - playlistService    ││  └──────────────────────────────┘    │
│  │ + doGet(): void      ││                                       │
│  │ + doPost(): void     ││                                       │
│  └──────────────────────┘                                        │
│                                                                   │
└──────────────────────────────────────────���───────────────────────┘
```

### 3. **Sequence Diagram** (Sơ đồ Tuần Tự - Quy Trình Đăng Nhập)

```
User          Browser         AuthController    UserServiceBean    Database
 │               │                   │                  │              │
 │─ Login ────────►                  │                  │              │
 │                │─ HTTP POST ─────►│                  │              │
 │                │                  │─ login() ───────►│              │
 │                │                  │                  │─ Query ─────►│
 │                │                  │                  │◄─ User Data ─│
 │                │                  │◄─ User Object ──│              │
 │                │◄─ Redirect ──────│                  │              │
 │                │                  │                  │              │
 │◄─ Session ─────│                  │                  │              │
```

### 4. **Component Diagram** (Sơ đồ Thành Phần)

```
┌──────────────────────────────────────────────────────────────────┐
│                     WEB APPLICATION                              │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌────────────────────────┐          ┌──────────────────────┐   │
│  │  Presentation Tier     │          │  Business Tier       │   │
│  │  (JSP, Servlets)       │◄────────►│  (EJB, Services)     │   │
│  └────────────────────────┘          └──────────────────────┘   │
│           │                                       │              │
│           │                                       │              │
│           └───────────────────┬───────────────────┘              │
│                               │                                  │
│                    ┌──────────▼────────────┐                    │
│                    │  Data Access Layer    │                    │
│                    │  (JPA, Repositories)  │                    │
│                    └──────────┬────────────┘                    │
│                               │                                  │
│                    ┌──────────▼────────────┐                    │
│                    │  DATABASE LAYER       │                    │
│                    │  (MySQL)              │                    │
│                    └───────────────────────┘                    │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘
```

### 5. **Entity-Relationship Diagram (ERD)** (Sơ đồ Quan Hệ Thực Thể)

```
┌─────────────┐                    ┌──────────────┐
│   USERS     │                    │    SONGS     │
├─────────────┤                    ├──────────────┤
│ userId (PK) │                    │ songId (PK)  │
│ username    │                    │ title        │
│ password    │                    │ artist       │
│ email       │                    │ album        │
│ role        │                    │ audioUrl     │
│ createdAt   │                    │ imageUrl     │
│ updatedAt   │                    │ duration     │
└─────────────┘                    └──────────────┘
      │                                   │
      │                  1:N              │
      │        has many                   │
      │                                   │
      │            ┌──────────────────┐   │
      │            │  PLAYLISTS       │   │
      │            ├──────────────────┤   │
      └───────────►│ playlistId (PK)  │   │
                   │ userId (FK)      │───┘
                   │ name             │
                   │ description      │
                   │ createdAt        │
                   │ updatedAt        │
                   └────────┬─────────┘
                            │
                            │ N:N
                            │
                   ┌────────▼──────────────┐
                   │ PLAYLIST_SONGS        │
                   ├───────────────────────┤
                   │ playlistSongId (PK)   │
                   │ playlistId (FK)       │
                   │ songId (FK)           │
                   │ addedAt               │
                   └───────────────────────┘
```

## 📝 Tóm Tắt Kiến Trúc

| Layer | Thành Phần | Công Nghệ |
|-------|-----------|----------|
| **Presentation** | JSP, Servlets | Jakarta/Java EE Servlets |
| **Business Logic** | EJB Services (Session Beans) | EJB 3.x (Stateless) |
| **Data Access** | JPA Entities, Repositories | JPA/Hibernate |
| **Database** | 4 bảng (Users, Songs, Playlists, Playlist_Songs) | MySQL |

