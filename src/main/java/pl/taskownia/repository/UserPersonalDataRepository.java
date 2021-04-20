package pl.taskownia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskownia.model.User;
import pl.taskownia.model.UserPersonalData;

import javax.transaction.Transactional;

@Repository
public interface UserPersonalDataRepository extends JpaRepository<UserPersonalData, Long> {
}
