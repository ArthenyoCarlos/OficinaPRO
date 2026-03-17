package br.com.oficinapro.auth.service.user;

import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.auth.dto.user.response.UserResponseDTO;
import br.com.oficinapro.auth.reposirory.UserRepository;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindByUserCodeService {

    private final UserRepository userRepository;

    public FindByUserCodeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByCode(String code){
        User user = userRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with code: " + code));

        return new UserResponseDTO(user);
    }
}
