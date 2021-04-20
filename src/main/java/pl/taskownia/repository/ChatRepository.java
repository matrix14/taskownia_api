package pl.taskownia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskownia.model.Chat;
import pl.taskownia.model.User;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}
