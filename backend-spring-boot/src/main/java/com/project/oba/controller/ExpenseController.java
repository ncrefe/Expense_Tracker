package com.project.oba.controller;

import com.project.oba.model.request.expense.CreateExpenseRequest;
import com.project.oba.model.response.expense.ExpenseResponse;
import com.project.oba.service.ExpenseService;
import com.project.oba.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;

    @GetMapping
    public List<ExpenseResponse> getAll() {
        return expenseService.getAll(userService.getAuthenticatedUserId());
    }

    @GetMapping("/{expenseId}")
    public ExpenseResponse get(@PathVariable Long expenseId) {
        return expenseService.getById(expenseId,userService.getAuthenticatedUserId());
    }

    @PostMapping
    public ExpenseResponse create(@Valid @RequestBody CreateExpenseRequest createExpenseRequest) {
        return expenseService.create(createExpenseRequest,userService.getAuthenticatedUserId());
    }

    @DeleteMapping("/{expenseId}")
    public void delete(@PathVariable Long expenseId) {
        expenseService.deleteById(expenseId,userService.getAuthenticatedUserId());
    }

    @GetMapping("/categories-total")
    public HashMap getCategoriesTotal() {
        return expenseService.getCategoriesTotal(userService.getAuthenticatedUserId());
    }

}
