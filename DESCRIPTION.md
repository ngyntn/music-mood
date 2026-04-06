# 🎵 MusicMood - Hệ Thống Quản Lý Nghe Nhạc Trực Tuyến

Đây là dự án Bài tập lớn với đề tài: **Xây dựng module quản lý nghe nhạc online với mô hình thành phần EJB**. Hệ thống được thiết kế theo kiến trúc Java EE tiêu chuẩn, phân tách rõ ràng giữa giao diện, logic điều hướng và logic nghiệp vụ lõi.

## 🚀 Công Nghệ Sử Dụng (Tech Stack)
* **Backend:** Java EE 8 (Jakarta EE 8 namespace `javax.*`).
* **Lõi Nghiệp Vụ:** Enterprise JavaBeans (EJB 3.x - Stateless Session Beans).
* **Truy Cập Dữ Liệu:** JPA (Hibernate Implementation).
* **Controller:** Java Servlets.
* **Frontend:** JSP, JSTL, HTML5, Bootstrap 5.
* **Cơ Sở Dữ Liệu:** MySQL 8.0+.
* **Máy Chủ Ứng Dụng (App Server):** WildFly 26.1.3.Final.
* **Công Cụ Build:** Maven.

## ✨ Chức Năng Chính
Hệ thống xoay quanh 2 nhóm người dùng chính:

**1. Người Dùng (User):**
* Khám phá và nghe các bài hát có trên hệ thống.
* Tìm kiếm bài hát theo Tên hoặc Ca sĩ.
* Đăng ký, Đăng nhập tài khoản cá nhân.
* Quản lý Playlist: Tạo mới playlist, thêm bài hát yêu thích vào playlist, xem và nghe nhạc từ playlist cá nhân, xóa playlist.

**2. Quản Trị Viên (Admin):**
* Xem danh sách toàn bộ người dùng trong hệ thống.
* Quản lý Bài hát (CRUD): Thêm bài hát mới, cập nhật thông tin (tên, ca sĩ, ảnh bìa, link nhạc audio), xóa bài hát.

