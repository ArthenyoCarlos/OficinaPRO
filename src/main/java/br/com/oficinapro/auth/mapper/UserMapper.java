package br.com.oficinapro.auth.mapper;

import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.auth.dto.user.request.UserRequestDTO;
import br.com.oficinapro.auth.dto.user.response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "credentialsNonExpired", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    User toEntity(UserRequestDTO dto);

    @Mapping(target = "roles", expression = "java(user.getRoles())")
    UserResponseDTO toResponse(User user);
}
