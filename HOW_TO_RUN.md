# ⚙️ Hướng Dẫn Cài Đặt Và Chạy Dự Án (How to Run)

## Yêu cầu hệ thống (Prerequisites)
1. **JDK:** Phiên bản 8, 11 hoặc 17.
2. **Database:** MySQL Server 8.x.
3. **App Server:** WildFly 26.1.3.Final (Lưu ý: Không dùng bản 27+ vì không tương thích chuẩn Java EE 8).
4. **Build Tool:** Maven.

---

## Bước 1: Cấu hình Cơ sở dữ liệu
1. Mở MySQL Server.
2. Tạo database trống bằng câu lệnh:
   ```sql
   CREATE DATABASE music_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

---

## Bước 2: Cấu hình WildFly Server
1. **Giải nén** WildFly 26.
2. **Thêm MySQL Driver:**
    * Tải file `mysql-connector-j-8.x.x.jar`.
    * Vào thư mục: `wildfly-26.1.3.Final\modules\system\layers\base\com\mysql\main` (tạo thư mục nếu chưa có).
    * Copy file `.jar` vào đây và tạo file `module.xml` cấu hình driver.
3. **Cấu hình Datasource:**
    * Mở file: `wildfly-26.1.3.Final\standalone\configuration\standalone.xml`.
    * Thêm Datasource `java:/MusicDS` trỏ tới `jdbc:mysql://localhost:3306/music_db` với username và password MySQL của máy bạn.

---

## Bước 3: Build và Deploy Ứng Dụng
1. Mở **Terminal/CMD** tại thư mục gốc của dự án (nơi chứa file `pom.xml`).
2. Chạy lệnh Maven để đóng gói dự án:
   ```bash
   mvn clean package
   ```
3. Lấy file `.war` vừa được tạo ra trong thư mục `target/`.
4. Đổi tên file thành `ROOT.war` và copy/paste vào thư mục: `wildfly-26.1.3.Final\standalone\deployments\`.

---

## Bước 4: Khởi Động Máy Chủ
1. Mở **Terminal**, trỏ tới thư mục `wildfly-26.1.3.Final\bin`.
2. Chạy file khởi động:
    * **Trên Windows:** `standalone.bat`
    * **Trên Linux/Mac:** `./standalone.sh`
3. Chờ server khởi động và hiện thông báo: `Deployed "ROOT.war"`.
4. Mở trình duyệt và truy cập: `http://localhost:8080/`