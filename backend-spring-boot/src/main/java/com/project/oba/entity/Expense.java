package com.project.oba.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
public class Expense extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "expense_id_seq")
    @SequenceGenerator(name = "expense_id_seq", sequenceName = "expense_id_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "expense_type")
    private ExpenseType expenseType;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private BigDecimal amount;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

}
