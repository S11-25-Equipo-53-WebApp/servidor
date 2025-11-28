package com.webapp.backend.mappers;

import com.webapp.backend.Entities.User;
import com.webapp.backend.Entities.Company;
import com.webapp.backend.Entities.enums.UserType;
import com.webapp.backend.dto.auth.AuthRegisterRequest;
import com.webapp.backend.dto.auth.AuthRegisterResponse;
import com.webapp.backend.dto.auth.AuthLoginResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuthMapper {

    public User toEntity(AuthRegisterRequest request, Company empresa, PasswordEncoder encoder) {

        User u = new User();
        u.setEmail(request.email());
        u.setPassword(encoder.encode(request.password()));
        u.setFullName(request.fullName());
        u.setCompany(empresa);
        u.setUserType(UserType.valueOf(request.typeUser().toUpperCase()));
        u.setCreatedAt(new Date());

        return u;
    }

    public AuthRegisterResponse toUserRegisterResponse(User user, String token) {
        return new AuthRegisterResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getUserType().name(),
                token
        );
    }

    public AuthLoginResponse toUserLoginResponse(String token) {
        return new AuthLoginResponse(token);
    }
}
