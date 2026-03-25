package br.com.oficinapro.veiculo.mapper;

import br.com.oficinapro.veiculo.domain.Vehicle;
import br.com.oficinapro.veiculo.dto.request.VehicleRequest;
import br.com.oficinapro.veiculo.dto.response.VehicleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "client", ignore = true)
    Vehicle toEntity(VehicleRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "client", ignore = true)
    void updateEntity(VehicleRequest dto, @MappingTarget Vehicle vehicle);

    VehicleResponse toResponse(Vehicle vehicle);
}
