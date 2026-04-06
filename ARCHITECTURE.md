# 🏗️ Kiến Trúc Hệ Thống (System Architecture)

Dự án áp dụng mô hình 3 lớp (3-Tier Architecture) kết hợp mô hình MVC trên nền tảng Java EE.

## 1. Presentation Layer (Lớp Giao Diện - View)
* Sử dụng **JSP (JavaServer Pages)** kết hợp **JSTL** để render dữ liệu động.
* Sử dụng **Bootstrap 5** để xây dựng giao diện responsive, thân thiện với người dùng.
* Tương tác với người dùng qua các Form HTTP (GET/POST).

## 2. Web/Controller Layer (Lớp Điều Hướng)
* Gồm các **Servlets** (`@WebServlet`).
* **Nhiệm vụ:** Tiếp nhận HTTP Request từ JSP, gọi EJB để xử lý logic, lưu trữ kết quả vào `HttpServletRequest` hoặc `HttpSession`, sau đó forward/redirect người dùng đến trang JSP phù hợp.
* Các Controller chính: `AuthServlet`, `HomeServlet`, `PlaylistServlet`, `AdminSongServlet`, `AdminUserServlet`.

## 3. Business Logic Layer (Lớp Nghiệp Vụ Lõi - EJB)
* Đây là "trái tim" của hệ thống, sử dụng **Stateless Session Beans** (`@Stateless`).
* Các Controller giao tiếp với EJB thông qua Local Interface (`@Local`) bằng cơ chế Dependency Injection (`@EJB`).
* **Ưu điểm:** Container (WildFly) tự động quản lý vòng đời của Bean, xử lý đa luồng (thread-safe) và tự động quản lý Transaction (JTA).
* Các EJB Components:
    * `UserSessionBean`: Xử lý đăng nhập, đăng ký, mã hóa/kiểm tra người dùng.
    * `SongSessionBean`: Quản lý kho nhạc, thêm/sửa/xóa, tìm kiếm bài hát.
    * `PlaylistSessionBean`: Quản lý logic tạo playlist, thêm bài hát vào playlist.

## 4. Data Access Layer (Lớp Dữ Liệu - JPA)
* Sử dụng **JPA (Java Persistence API)** với implementation là **Hibernate**.
* EJB sử dụng `EntityManager` được inject tự động (`@PersistenceContext`) để thực hiện các câu lệnh truy vấn xuống Database thông qua Datasource của WildFly.