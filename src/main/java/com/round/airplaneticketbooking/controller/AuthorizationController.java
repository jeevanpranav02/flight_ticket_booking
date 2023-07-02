package com.round.airplaneticketbooking.controller;

import com.round.airplaneticketbooking.constants.request.LoginRequest;
import com.round.airplaneticketbooking.constants.request.RegisterRequest;
import com.round.airplaneticketbooking.constants.response.AuthenticationToken;
import com.round.airplaneticketbooking.service.AdminService;
import com.round.airplaneticketbooking.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/v1")
public class AuthorizationController {
    private final AdminService adminService;
    private final CustomerService customerService;

    public AuthorizationController(
            AdminService adminService,
            CustomerService customerService
    ) {
        this.adminService = adminService;
        this.customerService = customerService;
    }

    @PostMapping("customer/signup")
    public ResponseEntity<?> customerSignup(@RequestBody RegisterRequest registerRequest) {
        AuthenticationToken createdCustomer = customerService.signup(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @PostMapping("customer/login")
    public ResponseEntity<AuthenticationToken> customerLogin(@RequestBody LoginRequest loginRequest) {
        AuthenticationToken authToken = customerService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(authToken);
    }

    @PostMapping("admin/signup")
    public ResponseEntity<?> adminSignup(@RequestBody RegisterRequest registerRequest) {
        AuthenticationToken createdAdmin = adminService.signup(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
    }

    @PostMapping("admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody LoginRequest loginRequest) {
        AuthenticationToken authToken = adminService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(authToken);
    }
}
