package pl.taskownia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskownia.model.User;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    User findByEmail(String email);
    User findByUsername(String username);
    @Transactional
    void deleteByUsername(String username);
}
