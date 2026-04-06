package com.musicmood.ejb;


import com.musicmood.entity.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Stateless Session Bean xử lý logic nghiệp vụ liên quan đến User.
 *
 * @Stateless: Container tự quản lý vòng đời, transaction, và thread-safety.
 * @PersistenceContext: Container inject EntityManager (JPA) tự động.
 */
@Stateless
public class UserSessionBean implements UserServiceLocal {

    /**
     * EntityManager được inject bởi EJB container thông qua JTA transaction.
     * unitName phải khớp với tên trong persistence.xml.
     */
    @PersistenceContext(unitName = "MusicPU")
    private EntityManager em;

    // ===================================================
    // LOGIN
    // ===================================================

    @Override
    public User login(String username, String password) {
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username AND u.password = :password",
                    User.class
            );
            query.setParameter("username", username);
            query.setParameter("password", password);
            return query.getSingleResult();
        } catch (NoResultException e) {
            // Không tìm thấy user với thông tin đăng nhập này
            return null;
        }
    }

    // ===================================================
    // REGISTER
    // ===================================================

    @Override
    public boolean register(User user) {
        // Kiểm tra username đã tồn tại chưa
        if (isUsernameTaken(user.getUsername())) {
            return false;
        }

        // Mặc định role là "USER" khi đăng ký
        user.setRole("USER");

        em.persist(user);
        return true;
    }

    // ===================================================
    // FIND BY ID
    // ===================================================

    @Override
    public User findById(Long id) {
        return em.find(User.class, id);
    }

    // ===================================================
    // PRIVATE HELPER
    // ===================================================

    /**
     * Kiểm tra xem username đã được đăng ký chưa.
     */
    private boolean isUsernameTaken(String username) {
        Long count = em.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class
        ).setParameter("username", username).getSingleResult();
        return count > 0;
    }

    @Override
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u ORDER BY u.id DESC", User.class).getResultList();
    }
}
