package com.konekokonekone.nekodion.api.usecase;

import com.konekokonekone.nekodion.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupUseCase {

    private final UserService userService;

    public void syncUser(Jwt jwt) {
        String auth0Id = jwt.getClaimAsString("sub");
        String email = jwt.getClaimAsString("http://claim/email");
        userService.saveUserIfNotExists(auth0Id, email);
    }
}
