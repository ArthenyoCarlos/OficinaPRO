package br.com.oficinapro.auth.service.user;

import br.com.oficinapro.auth.reposirory.UserRepository;
import org.hibernate.envers.Audited;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService implements UserDetailsService {

    @Audited
    UserRepository userRepository;

    public AuthUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if(user != null){
            return user;
        }else {
            throw new UsernameNotFoundException("Username" + username + "not found!");
        }
    }
}
