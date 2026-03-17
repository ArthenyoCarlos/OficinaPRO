package br.com.oficinapro.auth.service.user;

import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.auth.dto.user.response.UserResponseDTO;
import br.com.oficinapro.auth.reposirory.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindAllUserService {

    UserRepository userRepository;

    public FindAllUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(boolean enabled, Pageable pageable){
        Page<User> users = userRepository.findAll(enabled, pageable);
        return users.map(x -> new UserResponseDTO(x));
    }
}
