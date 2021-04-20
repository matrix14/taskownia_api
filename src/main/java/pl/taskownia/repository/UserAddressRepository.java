package pl.taskownia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskownia.model.UserAddress;
import pl.taskownia.model.UserPersonalData;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
}
