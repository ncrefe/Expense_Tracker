package com.project.oba.service;

import com.project.oba.entity.User;
import com.project.oba.exception.BusinessException;
import com.project.oba.exception.ErrorCode;
import com.project.oba.model.request.auth.LoginRequest;
import com.project.oba.model.request.auth.RegisterRequest;
import com.project.oba.model.request.auth.ResetPasswordRequest;
import com.project.oba.model.response.auth.LoginResponse;
import com.project.oba.repository.UserRepository;
import com.project.oba.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;


    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest registerRequest) {

        if(!registerRequest.getPassword().equals(registerRequest.getRewritePassword())){
            throw new BusinessException(ErrorCode.password_mismatch,"Passwords didn't matches");
        }

        Optional<User> existingUser = userRepository.findByUserName(registerRequest.getUserName());
        if (existingUser.isPresent()) {
            throw new BusinessException(ErrorCode.account_already_exists, "Username already exist!");
        }

        User user = new User();
        user.setUserName(registerRequest.getUserName());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        user.setBudget(BigDecimal.valueOf(0));

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByUserName(loginRequest.getUserName());
        if (!user.isPresent()) {
            throw new BusinessException(ErrorCode.account_missing, "Username could not found!");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPasswordHash())) {
            throw new BusinessException(ErrorCode.password_mismatch, "Password does not match!");
        }

        return LoginResponse.builder()
                .id(user.get().getId())
                .token(jwtService.createToken(user.get().getId().toString()))
                .build();
    }

    public boolean resetPassword(ResetPasswordRequest resetPasswordRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "User not found"));

        if (!passwordEncoder.matches(resetPasswordRequest.getCurrentPassword(), user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.password_mismatch, "Current password does not match!");
        }

        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getRewritePassword())) {
            throw new BusinessException(ErrorCode.password_mismatch, "New passwords does not match!");
        }

        user.setPasswordHash(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepository.save(user);

        return true;
    }

}

