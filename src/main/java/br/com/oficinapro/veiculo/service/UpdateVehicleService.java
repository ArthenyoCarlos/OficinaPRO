package br.com.oficinapro.veiculo.service;

import br.com.oficinapro.cliente.domain.Client;
import br.com.oficinapro.cliente.repository.ClientRepository;
import br.com.oficinapro.common.exception.ResourceConflictException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.veiculo.domain.Vehicle;
import br.com.oficinapro.veiculo.dto.request.VehicleRequest;
import br.com.oficinapro.veiculo.dto.response.VehicleResponse;
import br.com.oficinapro.veiculo.mapper.VehicleMapper;
import br.com.oficinapro.veiculo.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateVehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final ClientRepository clientRepository;

    public UpdateVehicleService(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper, ClientRepository clientRepository) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public VehicleResponse update(String code, VehicleRequest vehicleRequest) {
        Vehicle vehicle = vehicleRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with code: " + code));

        updatePlate(vehicle, vehicleRequest.plate());
        updateChassis(vehicle, vehicleRequest.chassis());
        updateRenavam(vehicle, vehicleRequest.renavam());

        vehicleMapper.updateEntity(vehicleRequest, vehicle);
        assignClient(vehicle, vehicleRequest.clientCode());
        normalizeOptionalFields(vehicle);
        applyDefaults(vehicle);

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(updatedVehicle);
    }

    private void updatePlate(Vehicle vehicle, String plate) {
        if (plate == null) {
            return;
        }

        String normalizedPlate = plate.trim().toUpperCase();
        if (normalizedPlate.equals(vehicle.getPlate())) {
            return;
        }

        Vehicle existingVehicle = vehicleRepository.findByPlate(normalizedPlate);
        if (existingVehicle != null && !existingVehicle.getId().equals(vehicle.getId())) {
            throw new ResourceConflictException("Plate already exists");
        }

        vehicle.setPlate(normalizedPlate);
    }

    private void updateChassis(Vehicle vehicle, String chassis) {
        String normalizedChassis = normalize(chassis);
        if (normalizedChassis == null || normalizedChassis.equals(vehicle.getChassis())) {
            return;
        }

        Vehicle existingVehicle = vehicleRepository.findByChassis(normalizedChassis);
        if (existingVehicle != null && !existingVehicle.getId().equals(vehicle.getId())) {
            throw new ResourceConflictException("Chassis already exists");
        }

        vehicle.setChassis(normalizedChassis);
    }

    private void updateRenavam(Vehicle vehicle, String renavam) {
        String normalizedRenavam = normalize(renavam);
        if (normalizedRenavam == null || normalizedRenavam.equals(vehicle.getRenavam())) {
            return;
        }

        Vehicle existingVehicle = vehicleRepository.findByRenavam(normalizedRenavam);
        if (existingVehicle != null && !existingVehicle.getId().equals(vehicle.getId())) {
            throw new ResourceConflictException("Renavam already exists");
        }

        vehicle.setRenavam(normalizedRenavam);
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

    private void assignClient(Vehicle vehicle, String clientCode) {
        if (clientCode == null) {
            return;
        }

        if (clientCode.trim().isEmpty()) {
            if (vehicle.getClient() != null) {
                vehicle.getClient().removeVehicle(vehicle);
            }
            return;
        }

        Client client = clientRepository.findByCode(clientCode.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with code: " + clientCode));

        if (vehicle.getClient() != null && vehicle.getClient().getId().equals(client.getId())) {
            return;
        }

        if (vehicle.getClient() != null) {
            vehicle.getClient().removeVehicle(vehicle);
        }

        client.addVehicle(vehicle);
    }

    private String normalize(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return value.trim();
    }
}
