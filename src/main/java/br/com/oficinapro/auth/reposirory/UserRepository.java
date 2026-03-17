package br.com.oficinapro.auth.reposirory;

import br.com.oficinapro.auth.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
    User findByEmail(String email);
    Optional<User> findByCode(String code);

    @Query("SELECT u FROM User u WHERE u.enabled = :enabled")
    Page<User> findAll(@Param("enabled") boolean enabled, Pageable pageable);
}
