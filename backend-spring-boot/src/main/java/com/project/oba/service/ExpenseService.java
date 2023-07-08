package com.project.oba.service;

import com.project.oba.entity.Expense;
import com.project.oba.entity.ExpenseType;
import com.project.oba.entity.User;
import com.project.oba.exception.BusinessException;
import com.project.oba.exception.ErrorCode;
import com.project.oba.model.request.expense.CreateExpenseRequest;
import com.project.oba.model.response.expense.ExpenseResponse;
import com.project.oba.repository.ExpenseRepository;
import com.project.oba.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ExpenseService {

    private ExpenseRepository expenseRepository;
    private UserRepository userRepository;
    private UserService userService;

    public ExpenseResponse create(CreateExpenseRequest createExpenseRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.account_missing, "User not found"));
        Expense expense = new Expense();
        expense.setAmount(createExpenseRequest.getAmount());
        expense.setExpenseType(createExpenseRequest.getExpenseType());
        expense.setUser(user);
        expense.setDescription(createExpenseRequest.getDescription());
        expenseRepository.save(expense);

        BigDecimal newBudget = user.getBudget().subtract(expense.getAmount());
        userService.setBudget(userId, newBudget);

        return ExpenseResponse.fromEntity(expense);
    }

    public void deleteById(Long expenseId, Long userId) {
        if (expenseRepository.existsById(expenseId) &&
                expenseRepository.findById(expenseId).get().getUser().getId().equals(userId)) {
            expenseRepository.deleteById(expenseId);
        } else {
            throw new BusinessException(ErrorCode.resource_missing, "The expense which is you try to delete not found");
        }
    }

    public ExpenseResponse getById(Long expenseId, Long authenticatedUserId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "The expense which is you try to get not found"));
        if (expense.getUser().getId().equals(authenticatedUserId)) {
            return ExpenseResponse.fromEntity(expense);
        } else {
            throw new BusinessException(ErrorCode.resource_missing, "The expense which is you try to get not found");
        }
    }

    public List<ExpenseResponse> getAll(Long userId) {
        return expenseRepository.findAllByUserId(userId).stream().map(ExpenseResponse::fromEntity).collect(Collectors.toList());
    }

    public HashMap getCategoriesTotal(Long authenticatedUserId) {
        List<Expense> expenses = expenseRepository.findAllByUserId(authenticatedUserId);
        HashMap<String, BigDecimal> categoriesTotal = new HashMap<>();
        for (ExpenseType expenseType : ExpenseType.values()) {
            categoriesTotal.put(expenseType.name(), BigDecimal.ZERO);
            for (Expense expense : expenses) {
                if (expense.getExpenseType().equals(expenseType)) {
                    categoriesTotal.put(expenseType.name(), categoriesTotal.get(expenseType.name()).add(expense.getAmount()));
                }
            }
        }

        return categoriesTotal;
    }

    // Friend
    public HashMap getCategoriesTotal(String username) {
        Optional<User> friend = Optional.ofNullable(userRepository.findByUserName(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "The user which is you try to get not found")));
        if (friend.isPresent()) {
            List<Expense> expenses = expenseRepository.findAllByUserId(friend.get().getId());
            HashMap<String, BigDecimal> categoriesTotal = new HashMap<>();
            for (ExpenseType expenseType : ExpenseType.values()) {
                categoriesTotal.put(expenseType.name(), BigDecimal.ZERO);
                for (Expense expense : expenses) {
                    if (expense.getExpenseType().equals(expenseType)) {
                        categoriesTotal.put(expenseType.name(), categoriesTotal.get(expenseType.name()).add(expense.getAmount()));
                    }
                }
            }
            return categoriesTotal;
        } else {
            return null;
        }
    }

    public List<ExpenseResponse> getAll(String username) {
        Optional<User> friend = Optional.ofNullable(userRepository.findByUserName(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "The user which is you try to get not found")));
        if (friend.isPresent()) {
            List<Expense> expenses = expenseRepository.findAllByUserId(friend.get().getId());
            return expenseRepository.findAllByUserId(friend.get().getId()).stream().map(ExpenseResponse::fromEntity).collect(Collectors.toList());
        } else {
            return null;
        }
    }

}
