package br.com.oficinapro.financeiro.mapper;

import br.com.oficinapro.financeiro.domain.Receipt;
import br.com.oficinapro.financeiro.dto.request.ReceiptRequest;
import br.com.oficinapro.financeiro.dto.response.ReceiptResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "serviceOrder", ignore = true)
    @Mapping(target = "receivedBy", ignore = true)
    Receipt toEntity(ReceiptRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "serviceOrder", ignore = true)
    @Mapping(target = "receivedBy", ignore = true)
    void updateEntity(ReceiptRequest dto, @MappingTarget Receipt receipt);

    ReceiptResponse toResponse(Receipt receipt);
}
