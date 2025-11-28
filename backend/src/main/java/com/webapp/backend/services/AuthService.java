package com.webapp.backend.services;

import com.webapp.backend.Entities.User;
import com.webapp.backend.dto.auth.*;
import com.webapp.backend.infra.security.TokenService;
import com.webapp.backend.infra.security.userDetails.UserDetailsImpl;
import com.webapp.backend.mappers.AuthMapper;
import com.webapp.backend.repository.EmpresaRepository;
import com.webapp.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;

    public AuthLoginResponse login(AuthLoginRequest request) {

        var authToken = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        );

        var authentication = authenticationManager.authenticate(authToken);

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userDetails.user();

        String token = tokenService.generarToken(user);

        return authMapper.toUserLoginResponse(token);
    }


    public AuthRegisterResponse register(AuthRegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("El email ya está registrado");
        }

        var empresa = empresaRepository.findById(request.companyId())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        // Mapeo DTO → Entity
        User nuevo = authMapper.toEntity(request, empresa, passwordEncoder);

        userRepository.save(nuevo);

        String token = tokenService.generarToken(nuevo);

        // Mapeo Entity → DTO
        return authMapper.toUserRegisterResponse(nuevo, token);
    }
}
