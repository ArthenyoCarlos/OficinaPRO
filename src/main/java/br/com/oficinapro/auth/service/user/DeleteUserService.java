package br.com.oficinapro.auth.service.user;

import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.auth.reposirory.UserRepository;
import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteUserService {

    private final UserRepository userRepository;

    public DeleteUserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(String code){
        User user = userRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(!user.isEnabled()){
            throw new BusinessException("User already disabled");
        }

        user.setEnabled(false);
        userRepository.save(user);
    }
}
