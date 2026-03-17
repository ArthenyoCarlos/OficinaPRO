package br.com.oficinapro.auth.service.user;

import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.auth.reposirory.UserRepository;
import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ActivateUserService {

    private final UserRepository userRepository;

    public ActivateUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void activateUser(String code) {
        User user = userRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with code: " + code));

        if(user.isEnabled()) {
            throw new BusinessException("User already enabled");
        }

        user.setEnabled(true);
        userRepository.save(user);
    }
}
