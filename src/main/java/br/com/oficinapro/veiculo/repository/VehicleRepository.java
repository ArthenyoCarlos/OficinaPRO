package br.com.oficinapro.veiculo.repository;

import br.com.oficinapro.veiculo.domain.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    Vehicle findByPlate(String plate);

    Vehicle findByChassis(String chassis);

    Vehicle findByRenavam(String renavam);

    Optional<Vehicle> findByCode(String code);

    @Query("SELECT v FROM Vehicle v WHERE v.enabled = :enabled")
    Page<Vehicle> findAll(@Param("enabled") boolean enabled, Pageable pageable);
}
