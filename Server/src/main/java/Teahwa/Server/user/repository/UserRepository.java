package Teahwa.Server.user.repository;

import Teahwa.Server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
}
