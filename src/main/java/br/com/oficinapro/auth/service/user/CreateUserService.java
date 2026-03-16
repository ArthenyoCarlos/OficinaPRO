package br.com.oficinapro.auth.service.user;

import br.com.oficinapro.auth.domain.Permission;
import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.auth.dto.user.request.UserRequestDTO;
import br.com.oficinapro.auth.dto.user.response.UserResponseDTO;
import br.com.oficinapro.auth.mapper.UserMapper;
import br.com.oficinapro.auth.reposirory.PermissionRepository;
import br.com.oficinapro.auth.reposirory.UserRepository;
import br.com.oficinapro.auth.service.auth.AuthService;
import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceConflictException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateUserService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final AuthService authService;
    private final UserMapper userMapper;

    public CreateUserService(
            UserRepository userRepository,
            PermissionRepository permissionRepository,
            AuthService authService,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.authService = authService;
        this.userMapper = userMapper;
    }

    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        validateUsernameUniqueness(userRequestDTO.username());
        validateEmailUniqueness(userRequestDTO.email());

        User user = mapToUser(userRequestDTO);
        user.setPassword(authService.generateHashedPassword(userRequestDTO.password()));
        user.setPermissions(resolvePermissions(userRequestDTO.roles()));
        applySecurityDefaults(user);

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    private void validateUsernameUniqueness(String username) {
        if (userRepository.findByUsername(username) != null) {
            throw new ResourceConflictException( "Username already exists");
        }
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.findByEmail(email) != null) {
            throw new ResourceConflictException("Email already exists");
        }
    }

    private User mapToUser(UserRequestDTO userRequestDTO) {
        return userMapper.toEntity(userRequestDTO);
    }

    private List<Permission> resolvePermissions(List<String> roles) {
        List<Permission> permissions = permissionRepository.findByNameIn(roles);

        if (permissions.size() != roles.size()) {
            throw new BusinessException("One or more roles are invalid");
        }

        return permissions;
    }

    private void applySecurityDefaults(User user) {
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
    }

    private UserResponseDTO mapToResponse(User user) {
        return userMapper.toResponse(user);
    }
}
