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
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UpdateUserService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final AuthService authService;
    private final UserMapper userMapper;

    public UpdateUserService(
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

    @Transactional
    public UserResponseDTO update(String code, UserRequestDTO userRequestDTO) {
        User user = userRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        updateUsername(user, userRequestDTO.username());
        updateEmail(user, userRequestDTO.email());

        if (userRequestDTO.fullName() != null) {
            user.setFullName(userRequestDTO.fullName());
        }

        if (userRequestDTO.password() != null && !userRequestDTO.password().isBlank()) {
            user.setPassword(authService.generateHashedPassword(userRequestDTO.password()));
        }

        if (userRequestDTO.roles() != null) {
            user.setPermissions(resolvePermissions(userRequestDTO.roles()));
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    private void updateUsername(User user, String username) {
        if (username == null || username.equals(user.getUsername())) {
            return;
        }

        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new ResourceConflictException("Username already exists");
        }

        user.setUsername(username);
    }

    private void updateEmail(User user, String email) {
        if (email == null || email.equals(user.getEmail())) {
            return;
        }

        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new ResourceConflictException("Email already exists");
        }

        user.setEmail(email);
    }

    private List<Permission> resolvePermissions(List<String> roles) {
        List<Permission> permissions = permissionRepository.findByNameIn(roles);

        if (permissions.size() != roles.size()) {
            throw new BusinessException("One or more roles are invalid");
        }

        return permissions;
    }

}
