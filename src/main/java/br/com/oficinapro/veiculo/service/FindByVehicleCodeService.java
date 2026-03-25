package br.com.oficinapro.veiculo.service;

import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.veiculo.domain.Vehicle;
import br.com.oficinapro.veiculo.dto.response.VehicleResponse;
import br.com.oficinapro.veiculo.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindByVehicleCodeService {

    private final VehicleRepository vehicleRepository;

    public FindByVehicleCodeService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional(readOnly = true)
    public VehicleResponse findByCode(String code) {
        Vehicle vehicle = vehicleRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with code: " + code));

        return new VehicleResponse(vehicle);
    }
}
