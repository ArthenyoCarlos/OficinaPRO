package br.com.oficinapro.veiculo.service;

import br.com.oficinapro.veiculo.domain.Vehicle;
import br.com.oficinapro.veiculo.dto.response.VehicleResponse;
import br.com.oficinapro.veiculo.repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindAllVehicleService {

    private final VehicleRepository vehicleRepository;

    public FindAllVehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional(readOnly = true)
    public Page<VehicleResponse> findAll(boolean enabled, Pageable pageable) {
        Page<Vehicle> vehicles = vehicleRepository.findAll(enabled, pageable);
        return vehicles.map(VehicleResponse::new);
    }
}
