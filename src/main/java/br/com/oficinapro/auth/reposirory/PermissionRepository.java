package br.com.oficinapro.auth.reposirory;

import br.com.oficinapro.auth.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByName(String name);

    List<Permission> findByNameIn(Collection<String> names);
}
