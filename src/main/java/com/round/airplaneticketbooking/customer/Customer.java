package com.round.airplaneticketbooking.customer;

import com.round.airplaneticketbooking.booking.Booking;
import com.round.airplaneticketbooking.enumsAndTemplates.Role;
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
@Table(name = "customer")
public class Customer implements UserDetails {
    @Id
    @GeneratedValue
    private Long customerId;
    private String userName;
    private String password;
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    public Customer() {
        role = Role.CUSTOMER;
    }

    public Customer(String userName, String password, String email, String phone) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        role = Role.CUSTOMER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(Role.CUSTOMER.name()));
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
