package com.webapp.backend.infra.security;

import com.webapp.backend.Entities.Usuario;
import com.webapp.backend.Entities.enums.TipoUsuario;
import com.webapp.backend.dto.AuthLoginResponse;
import com.webapp.backend.dto.AuthRegisterRequest;
import com.webapp.backend.dto.AuthRegisterResponse;
import com.webapp.backend.repository.EmpresaRepository;
import com.webapp.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthLoginResponse login(String email, String password) {
        var authToken = new UsernamePasswordAuthenticationToken(email, password);
        var authentication = authenticationManager.authenticate(authToken);
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = tokenService.generarToken(usuario);
        return new AuthLoginResponse(token);
    }

    public AuthRegisterResponse register(AuthRegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RuntimeException("El email ya está registrado");
        }

        var empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        Usuario nuevo = new Usuario();
        nuevo.setEmail(request.email());
        nuevo.setContrasena(passwordEncoder.encode(request.password()));
        nuevo.setNombre(request.nombre());
        nuevo.setEmpresa(empresa);
        nuevo.setTipoUsuario(TipoUsuario.valueOf(request.tipoUsuario().toUpperCase()));
        nuevo.setFechaRegistro(new Date());

        usuarioRepository.save(nuevo);

        String token = tokenService.generarToken(nuevo);

        return new AuthRegisterResponse(
                nuevo.getId(),
                nuevo.getEmail(),
                nuevo.getNombre(),
                nuevo.getTipoUsuario().name(),
                token
        );
    }
}
