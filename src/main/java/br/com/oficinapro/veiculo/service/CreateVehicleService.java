package br.com.oficinapro.veiculo.service;

import br.com.oficinapro.common.exception.ResourceConflictException;
import br.com.oficinapro.veiculo.domain.Vehicle;
import br.com.oficinapro.veiculo.dto.request.VehicleRequest;
import br.com.oficinapro.veiculo.dto.response.VehicleResponse;
import br.com.oficinapro.veiculo.mapper.VehicleMapper;
import br.com.oficinapro.veiculo.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateVehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public CreateVehicleService(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    @Transactional
    public VehicleResponse create(VehicleRequest vehicleRequest) {
        validatePlateUniqueness(vehicleRequest.plate());
        validateChassisUniqueness(vehicleRequest.chassis());
        validateRenavamUniqueness(vehicleRequest.renavam());

        Vehicle vehicle = vehicleMapper.toEntity(vehicleRequest);
        normalizeOptionalFields(vehicle);
        applyDefaults(vehicle);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(savedVehicle);
    }

    private void validatePlateUniqueness(String plate) {
        if (vehicleRepository.findByPlate(plate) != null) {
            throw new ResourceConflictException("Plate already exists");
        }
    }

    private void validateChassisUniqueness(String chassis) {
        if (isBlank(chassis)) {
            return;
        }

        if (vehicleRepository.findByChassis(chassis.trim()) != null) {
            throw new ResourceConflictException("Chassis already exists");
        }
    }

    private void validateRenavamUniqueness(String renavam) {
        if (isBlank(renavam)) {
            return;
        }

        if (vehicleRepository.findByRenavam(renavam.trim()) != null) {
            throw new ResourceConflictException("Renavam already exists");
        }
    }

    private void normalizeOptionalFields(Vehicle vehicle) {
        vehicle.setChassis(normalize(vehicle.getChassis()));
        vehicle.setRenavam(normalize(vehicle.getRenavam()));
        vehicle.setBrand(normalize(vehicle.getBrand()));
        vehicle.setModel(normalize(vehicle.getModel()));
        vehicle.setColor(normalize(vehicle.getColor()));
        vehicle.setFuelType(normalize(vehicle.getFuelType()));
        vehicle.setNotes(normalize(vehicle.getNotes()));
    }

    private void applyDefaults(Vehicle vehicle) {
        vehicle.setPlate(vehicle.getPlate().trim().toUpperCase());

        if (vehicle.getCurrentMileage() == null) {
            vehicle.setCurrentMileage(0L);
        }
    }

    private String normalize(String value) {
        if (isBlank(value)) {
            return null;
        }

        return value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
