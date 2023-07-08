package com.project.oba.controller;


import com.project.oba.model.request.auth.LoginRequest;
import com.project.oba.model.request.auth.RegisterRequest;
import com.project.oba.model.request.auth.ResetPasswordRequest;
import com.project.oba.model.response.auth.LoginResponse;
import com.project.oba.service.AuthenticationService;
import com.project.oba.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest registerRequest) {
        authenticationService.register(registerRequest);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PutMapping("/reset-password")
    public boolean resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return authenticationService.resetPassword(resetPasswordRequest, userService.getAuthenticatedUserId());
    }

}
