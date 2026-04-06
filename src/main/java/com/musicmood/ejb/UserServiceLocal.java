package com.musicmood.ejb;

import com.musicmood.entity.User;

import javax.ejb.Local;
import java.util.List;

/**
 * Local Interface cho UserSessionBean.
 * Servlets sẽ inject bean này qua @EJB annotation.
 */
@Local
public interface UserServiceLocal {

    /**
     * Đăng nhập: Tìm user theo username và password.
     * @return User object nếu đúng, null nếu sai thông tin.
     */
    User login(String username, String password);

    /**
     * Đăng ký tài khoản mới.
     * @return true nếu đăng ký thành công, false nếu username đã tồn tại.
     */
    boolean register(User user);

    /**
     * Tìm user theo ID (dùng cho session management).
     */
    User findById(Long id);

    List<User> getAllUsers();
}
