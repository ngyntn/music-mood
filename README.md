Chào bạn! Đây là bản nội dung cho file **README.md** của dự án, được trình bày một cách chuyên nghiệp, cấu trúc rõ ràng để bạn có thể đưa thẳng vào repository của mình.

---

# Online Music Management Module (EJB Model)

Dự án xây dựng hệ thống quản lý và nghe nhạc trực tuyến dựa trên kiến trúc **Java EE (Jakarta EE)**, tập trung vào việc áp dụng **Enterprise JavaBeans (EJB)** để xử lý logic nghiệp vụ và **JPA** để quản trị cơ sở dữ liệu.

## 1. Tổng Quan Dự Án
* **Mục tiêu:** Xây dựng module nghe nhạc cơ bản (Học thuật/Bài tập lớn).
* **Kiến trúc:** Phân lớp (Presentation -> Business -> Data Layer).
* **Tech Stack:**
    * **Backend:** Java EE 8, EJB 3.x (Stateless), JPA (Hibernate).
    * **Frontend:** JSP, Servlets, JSTL, Bootstrap 5.
    * **Database:** MySQL.
    * **Server:** WildFly / Payara Server.



---

## 2. Các Tính Năng Chính

### Đối với Người dùng (User)
* **Xác thực:** Đăng ký, Đăng nhập và Đăng xuất.
* **Khám phá:** Xem danh sách bài hát mới nhất tại trang chủ.
* **Tìm kiếm:** Truy vấn bài hát theo tên hoặc ca sĩ.
* **Trình phát nhạc:** Nghe nhạc trực tuyến qua trình phát HTML5 tích hợp.
* **Playlist:** Tạo danh sách phát cá nhân và quản lý bài hát yêu thích.

### Đối với Quản trị viên (Admin)
* **Quản lý thư viện:** Thêm, sửa, xóa bài hát (CRUD).
* **Quản lý người dùng:** Xem danh sách thành viên hệ thống.

---

## 3. Kiến Trúc Hệ Thống

### Sơ đồ Cơ sở dữ liệu (ERD)
Dự án bao gồm 4 bảng chính:
1.  **Users:** Lưu thông tin tài khoản và phân quyền (`ADMIN`/`USER`).
2.  **Songs:** Lưu thông tin bài hát và đường dẫn file/ảnh.
3.  **Playlists:** Thông tin danh sách phát của từng người dùng.
4.  **Playlist_Songs:** Bảng trung gian thiết lập mối quan hệ Many-to-Many giữa Playlist và Song.



### Thành phần EJB (Business Logic)
Hệ thống sử dụng các **Stateless Session Beans** để tách biệt logic xử lý:
* `UserServiceLocal`: Xử lý đăng nhập, đăng ký.
* `SongServiceLocal`: Các nghiệp vụ CRUD và tìm kiếm bài hát.
* `PlaylistServiceLocal`: Quản lý logic thêm/bớt bài hát vào playlist.

---

## 4. Cấu Trúc Thư Mục (Maven)

```plaintext
online-music-ejb/
├── pom.xml                 # Cấu hình dependencies (EJB, JPA, MySQL, JSTL)
└── src/main/
    ├── java/com/musicapp/
    │   ├── entity/         # JPA Entities (@Entity)
    │   ├── ejb/            # Interfaces & Stateless Session Beans
    │   ├── controller/     # Servlets điều hướng
    │   └── util/           # Helper classes
    ├── resources/
    │   └── META-INF/
    │       └── persistence.xml  # Cấu hình kết nối Database
    └── webapp/
        ├── WEB-INF/        # web.xml và thư viện
        ├── views/          # Giao diện JSP (admin/user)
        └── assets/         # CSS, JS, Images
```

---

## 5. Hướng Dẫn Cài Đặt & Triển Khai

### Bước 1: Chuẩn bị Cơ sở dữ liệu
Tạo database MySQL và chạy script sau để khởi tạo dữ liệu mẫu:
```sql
CREATE DATABASE music_db;
-- Chèn Admin mẫu
INSERT INTO Users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');
-- Chèn bài hát mẫu
INSERT INTO Songs (title, artist, audio_url) VALUES ('Song A', 'Artist A', 'url_to_mp3');
```

### Bước 2: Cấu hình Persistence
Chỉnh sửa file `src/main/resources/META-INF/persistence.xml` để khớp với thông tin MySQL của bạn (username, password).

### Bước 3: Build & Deploy
1.  Sử dụng Maven để đóng gói: `mvn clean package`.
2.  Copy file `.war` trong thư mục `target` vào thư mục `standalone/deployments` của **WildFly**.
3.  Khởi động server và truy cập: `http://localhost:8080/online-music-ejb`.

---

## 6. Quy Trình Phát Triển (Implementation Roadmap)
1.  **Setup:** Cấu hình `pom.xml` và `persistence.xml`.
2.  **Data Layer:** Định nghĩa các Entity JPA.
3.  **EJB Layer:** Viết các Session Beans xử lý logic.
4.  **Web Layer:** Xây dựng Servlets để điều hướng request.
5.  **UI Layer:** Hoàn thiện giao diện JSP với Bootstrap và JSTL.

---
*Dự án này được phát triển nhằm mục đích giáo dục, minh họa sức mạnh của mô hình EJB trong việc quản lý giao dịch và bảo mật trong Java EE.*