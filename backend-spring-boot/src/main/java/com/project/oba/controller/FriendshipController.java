package com.project.oba.controller;

import com.project.oba.model.response.expense.ExpenseResponse;
import com.project.oba.model.response.user.FriendshipRequestsResponse;
import com.project.oba.model.response.user.UserResponse;
import com.project.oba.service.ExpenseService;
import com.project.oba.service.FriendshipService;
import com.project.oba.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/friendship")
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final UserService userService;
    private final ExpenseService expenseService;

    @PostMapping("/send-request/{username}")
    public boolean sendRequest(@PathVariable String username) {
        return friendshipService.sendRequest(username, userService.getAuthenticatedUserId());
    }

    @GetMapping("/requests")
    public List<FriendshipRequestsResponse> getAllRequests() {
        return friendshipService.getAllRequests(userService.getAuthenticatedUserId());
    }

    @PostMapping("/accept-request/{username}")
    public boolean acceptRequest(@PathVariable String username) {
        return friendshipService.acceptRequest(username, userService.getAuthenticatedUserId());
    }

    @DeleteMapping("/reject-request/{username}")
    public boolean rejectRequest(@PathVariable String username) {
        return friendshipService.rejectRequest(username, userService.getAuthenticatedUserId());
    }

    @DeleteMapping("/remove/{username}")
    public boolean remove(@PathVariable String username) {
        return friendshipService.remove(username, userService.getAuthenticatedUserId());
    }

    @GetMapping("/all")
    public List<UserResponse> getAll() {
        return friendshipService.getAll(userService.getAuthenticatedUserId());
    }

    @GetMapping("/expenses")
    public List<ExpenseResponse> getExpenses() {
        return friendshipService.getExpenses(userService.getAuthenticatedUserId());
    }

    @GetMapping("/categories-total/{username}")
    public HashMap getCategoriesTotal(@PathVariable String username) {
        return expenseService.getCategoriesTotal(username);
    }


    @GetMapping("all-expenses/{username}")
    public List<ExpenseResponse> getAllExpenses(@PathVariable String username) {
        return expenseService.getAll(username);
    }


}
