package br.com.oficinapro.veiculo.service;

import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.veiculo.domain.Vehicle;
import br.com.oficinapro.veiculo.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteVehicleService {

    private final VehicleRepository vehicleRepository;

    public DeleteVehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(String code) {
        Vehicle vehicle = vehicleRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        if (!vehicle.isEnabled()) {
            throw new BusinessException("Vehicle already disabled");
        }

        vehicle.setEnabled(false);
        vehicleRepository.save(vehicle);
    }
}
