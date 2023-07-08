package com.project.oba.service;

import com.project.oba.entity.User;
import com.project.oba.exception.BusinessException;
import com.project.oba.exception.ErrorCode;
import com.project.oba.model.request.user.UpdateUserNameRequest;
import com.project.oba.model.response.user.FriendshipRequestsResponse;
import com.project.oba.model.response.user.UserResponse;
import com.project.oba.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public UserResponse getUser(long userId) {
        System.out.println("userId: " + userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "User not found"));

        return UserResponse.fromEntity(user);
    }

    public UserResponse updateUser(Long userId, UpdateUserNameRequest updateUserNameRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "User not found"));

        user.setUserName(updateUserNameRequest.getName());

        userRepository.save(user);

        return UserResponse.fromEntity(user);
    }

    public UserResponse deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "User not found"));

        userRepository.delete(user);

        return UserResponse.fromEntity(user);
    }

    public Long getAuthenticatedUserId() {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.equals("anonymousUser")) {
            throw new BusinessException(ErrorCode.unauthorized, "There is no enough authority!");
        }
        return Long.parseLong(principal);
    }

    public BigDecimal getBudget(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "User not found"));

        return user.getBudget();
    }

    public void setBudget(Long userId, BigDecimal newBudget){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "User not found"));

        user.setBudget(newBudget);

        userRepository.save(user);
    }

}
