package br.com.oficinapro.estoque.mapper;

import br.com.oficinapro.estoque.domain.StockMovement;
import br.com.oficinapro.estoque.dto.request.StockMovementRequest;
import br.com.oficinapro.estoque.dto.response.StockMovementResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "movedBy", ignore = true)
    StockMovement toEntity(StockMovementRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "movedBy", ignore = true)
    void updateEntity(StockMovementRequest dto, @MappingTarget StockMovement stockMovement);

    StockMovementResponse toResponse(StockMovement stockMovement);
}
