package com.project.oba.controller;

import com.project.oba.model.request.user.UpdateUserNameRequest;
import com.project.oba.model.response.user.UserResponse;
import com.project.oba.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public UserResponse getUser() {
        return userService.getUser(userService.getAuthenticatedUserId());
    }

    @PutMapping("/update-username")
    public UserResponse updateUser(@Valid @RequestBody UpdateUserNameRequest updateUserNameRequest) {
        return userService.updateUser(userService.getAuthenticatedUserId(), updateUserNameRequest);
    }

    @DeleteMapping("/{userId}")
    public UserResponse deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }

    @GetMapping("/get-budget")
    public BigDecimal getBudget() {
        return userService.getBudget(userService.getAuthenticatedUserId());
    }

    @PutMapping("/set-budget")
    public void setBudget(@RequestParam BigDecimal budget) {
        userService.setBudget(userService.getAuthenticatedUserId(), budget);
    }

}