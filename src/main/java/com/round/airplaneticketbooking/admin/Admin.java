package com.round.airplaneticketbooking.admin;

import com.round.airplaneticketbooking.enumsAndTemplates.Role;
import com.round.airplaneticketbooking.flight.Flight;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
    @GeneratedValue
    private Long adminId;
    private String userName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "addedByAdmin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Flight> flights;


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
        return List.of(new SimpleGrantedAuthority(Role.ADMIN.name()));
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
