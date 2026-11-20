package com.project.oba.model.request.expense;

import com.project.oba.entity.ExpenseType;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@ToString
public class CreateExpenseRequest {

    @NotNull(message = "Expense type can not be null!")
    private ExpenseType expenseType;

    @NotNull(message = "Amount can not be null!")
    private BigDecimal amount;

    @Size(max = 255, message = "Description cannot exceed 255 characters!")
    private String description;

}
