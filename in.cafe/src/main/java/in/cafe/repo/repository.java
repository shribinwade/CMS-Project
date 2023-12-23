package in.cafe.repo;

import in.cafe.entities.User;

import in.cafe.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface repository extends JpaRepository<User,Integer> {
    @Query("select u from User u where u.email=:email")
    User findByEmailId(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.role = 'user'")
    List<User> getAllUser();

    @Query("update User u set u.status = :status where u.id=:id")
    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

    @Query("select u.email from User u where u.role = 'admin'")
    List<String> getAllAdmin();

}
