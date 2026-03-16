package br.com.oficinapro.auth.controller;

import br.com.oficinapro.auth.dto.security.AccountCredentialDTO;
import br.com.oficinapro.auth.service.auth.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody AccountCredentialDTO credential){

        if(credentialsIsInvalid(credential)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid credentials");
        }

        var token = authService.signIn(credential);

        if(token == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid credentials");
        }

        return ResponseEntity.ok().body(token);
    }

    @PutMapping("/refresh/{username}")
    public ResponseEntity<?> refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken){

        if(parametersAreInvalid(username, refreshToken)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid credentials");
        }

        var token = authService.refreshToken(username, refreshToken);

        if(token == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid credentials");
        }

        return ResponseEntity.ok().body(token);
    }

    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
    }

    private static boolean credentialsIsInvalid(AccountCredentialDTO credential) {
        return credential == null ||
                StringUtils.isBlank(credential.password()) ||
                StringUtils.isBlank(credential.username());
    }
}
