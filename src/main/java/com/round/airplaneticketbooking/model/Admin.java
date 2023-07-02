package com.round.airplaneticketbooking.model;

import com.round.airplaneticketbooking.constants.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "admin")
public class Admin implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", strategy = "native")
    private Long id;


    @Column(name = "username")
    private String userName;

    @Column(unique=true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public Admin() {
        role = Role.ADMIN;
    }

    public Admin(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        role = Role.ADMIN;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
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
