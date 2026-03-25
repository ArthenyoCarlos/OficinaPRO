package br.com.oficinapro.veiculo.service;

import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.veiculo.domain.Vehicle;
import br.com.oficinapro.veiculo.repository.VehicleRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivateVehicleService {

    private final VehicleRepository vehicleRepository;

    public ActivateVehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void activate(String code) {
        Vehicle vehicle = vehicleRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with code: " + code));

        if (vehicle.isEnabled()) {
            throw new BusinessException("Vehicle already enabled");
        }

        vehicle.setEnabled(true);
        vehicleRepository.save(vehicle);
    }
}
