package br.com.oficinapro.ordemservico.mapper;

import br.com.oficinapro.ordemservico.domain.ServiceOrder;
import br.com.oficinapro.ordemservico.dto.request.ServiceOrderRequest;
import br.com.oficinapro.ordemservico.dto.response.ServiceOrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ServiceOrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "responsibleTechnician", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "serviceItems", ignore = true)
    @Mapping(target = "productItems", ignore = true)
    @Mapping(target = "statusHistory", ignore = true)
    @Mapping(target = "receipts", ignore = true)
    ServiceOrder toEntity(ServiceOrderRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "responsibleTechnician", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "serviceItems", ignore = true)
    @Mapping(target = "productItems", ignore = true)
    @Mapping(target = "statusHistory", ignore = true)
    @Mapping(target = "receipts", ignore = true)
    void updateEntity(ServiceOrderRequest dto, @MappingTarget ServiceOrder serviceOrder);

    ServiceOrderResponse toResponse(ServiceOrder serviceOrder);
}
