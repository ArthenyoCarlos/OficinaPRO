package br.com.oficinapro.auth.service.auth;

import br.com.oficinapro.auth.dto.security.AccountCredentialDTO;
import br.com.oficinapro.auth.dto.security.TokenDTO;
import br.com.oficinapro.auth.reposirory.UserRepository;
import br.com.oficinapro.common.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    public ResponseEntity<TokenDTO> signIn(AccountCredentialDTO credentials){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.username(),
                        credentials.password()
                )
        );
        var user = repository.findByUsername(credentials.username());
        if(user == null){
            throw new UsernameNotFoundException("Username " + credentials.username() + " not found!" );
        }

        var token = tokenProvider.createAccessToken(credentials.username(), user.getRoles());

        return ResponseEntity.ok(token);
    }
}
