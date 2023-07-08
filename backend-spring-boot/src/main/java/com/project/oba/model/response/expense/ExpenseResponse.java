package com.project.oba.model.response.expense;

import com.project.oba.entity.Expense;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Data
@Builder
@Setter
public class ExpenseResponse {

    private String expenseType;
    private String description;
    private BigDecimal amount;
    private ZonedDateTime created;
    private Long id;
    private String username;

    public static ExpenseResponse fromEntity(Expense expense) {
        if (expense == null) {
            return null;
        }
        return ExpenseResponse.builder().
                expenseType(expense.getExpenseType().toString()).
                description(expense.getDescription()).
                amount(expense.getAmount()).
                id(expense.getId()).
                created(expense.getCreated()).
                build();
    }

}
