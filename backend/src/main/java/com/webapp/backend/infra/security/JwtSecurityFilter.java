package com.webapp.backend.infra.security;

import com.webapp.backend.infra.security.userDetails.UserDetailsImpl;
import com.webapp.backend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // SI NO HAY TOKEN → SEGUIR SIN AUTH
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // Remove "Bearer "

        try {
            String email = tokenService.getSubject(token); // Extract email from token

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                var userOpt = userRepository.findByEmail(email);

                if (userOpt.isPresent()) {

                    var userDetails = new UserDetailsImpl(userOpt.get());

                    var authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }


        } catch (Exception e) {
            // TOKEN INVÁLIDO / EXPIRADO → NO LANZAR ERROR
            // simplemente deja la request como pública
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
