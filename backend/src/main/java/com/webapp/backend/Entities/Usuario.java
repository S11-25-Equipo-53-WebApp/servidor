package com.webapp.backend.Entities;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.webapp.backend.Entities.enums.TipoUsuario;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
public class Usuario extends BaseEntity implements UserDetails {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;


    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_registro")
    private Date fechaRegistro;

    // -----------------------------
    // SPRING SECURITY
    // -----------------------------
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority("ROLE_" + tipoUsuario.name()));
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
