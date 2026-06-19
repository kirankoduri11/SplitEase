package com.expensesplitter.repository;

import com.expensesplitter.model.Expense;
import com.expensesplitter.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByGroupOrderByCreatedAtDesc(Group group);
}