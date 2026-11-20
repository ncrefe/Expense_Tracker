package com.project.oba.service;

import com.project.oba.entity.User;
import com.project.oba.exception.BusinessException;
import com.project.oba.exception.ErrorCode;
import com.project.oba.model.response.expense.ExpenseResponse;
import com.project.oba.model.response.user.FriendshipRequestsResponse;
import com.project.oba.model.response.user.UserResponse;
import com.project.oba.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class FriendshipService {

    private final UserRepository userRepository;
    private final ExpenseService expenseService;

    public boolean sendRequest(String username, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "User not found"));
        User friend = userRepository.findByUserName(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "User not found"));

        friend.getFriendRequests().add(user);

        userRepository.save(friend);
        return true;
    }

    public List<FriendshipRequestsResponse> getAllRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "User not found"));

        return user.getFriendRequests().stream()
                .map(FriendshipRequestsResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public boolean acceptRequest(String username, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "User not found"));
        User friend = userRepository.findByUserName(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "User not found"));

        user.getFriends().add(friend);
        friend.getFriends().add(user);
        user.getFriendRequests().remove(friend);

        userRepository.save(user);
        userRepository.save(friend);
        return true;
    }

    public boolean rejectRequest(String username, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "User not found"));
        User friend = userRepository.findByUserName(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "User not found"));

        user.getFriendRequests().remove(friend);

        userRepository.save(user);
        return true;
    }

    public boolean remove(String username, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "User not found"));
        User friend = userRepository.findByUserName(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "User not found"));

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);

        userRepository.save(user);
        userRepository.save(friend);
        return true;
    }

    public List<UserResponse> getAll(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "User not found"));

        return user.getFriends().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ExpenseResponse> getExpenses(Long userId) {
        // get all friends
        List<UserResponse> friends = getAll(userId);
        // get all expenses for user friends
        List<ExpenseResponse> expenses = new ArrayList<>();
        for (UserResponse friend : friends) {
            List<ExpenseResponse> unnamedExpenses = expenseService.getAll(friend.getId());
            for (ExpenseResponse response : unnamedExpenses) {
                response.setUsername(friend.getName());
            }
            expenses.addAll(unnamedExpenses);
        }
        // sort expenses by Expense response' ZonedDateTime created
        expenses.sort((o1, o2) -> o2.getCreated().compareTo(o1.getCreated()));
        return expenses;
    }

}
