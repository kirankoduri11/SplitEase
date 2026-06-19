package com.expensesplitter.service;

import com.expensesplitter.model.Expense;
import com.expensesplitter.model.Group;
import com.expensesplitter.model.GroupMember;
import com.expensesplitter.model.User;
import com.expensesplitter.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final EmailService emailService;

    public ExpenseService(ExpenseRepository expenseRepository,
                          EmailService emailService) {
        this.expenseRepository = expenseRepository;
        this.emailService = emailService;
    }

    public Expense addExpense(String description, BigDecimal amount,
                              User paidBy, Group group) {
        Expense expense = new Expense();
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setPaidBy(paidBy);
        expense.setGroup(group);
        expense = expenseRepository.save(expense);

        try {
            emailService.sendExpenseNotification(expense);
        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
        }

        return expense;
    }

    public List<Expense> getExpensesForGroup(Group group) {
        return expenseRepository.findByGroupOrderByCreatedAtDesc(group);
    }

    public Map<String, BigDecimal> calculateBalances(Group group) {
        List<Expense> expenses = expenseRepository.findByGroupOrderByCreatedAtDesc(group);
        List<GroupMember> members = group.getMembers();
        int memberCount = members.size();

        Map<String, BigDecimal> balances = new HashMap<>();
        for (GroupMember m : members) {
            balances.put(m.getUser().getName(), BigDecimal.ZERO);
        }

        for (Expense expense : expenses) {
            BigDecimal share = expense.getAmount()
                    .divide(BigDecimal.valueOf(memberCount), 2, RoundingMode.HALF_UP);

            String paidByName = expense.getPaidBy().getName();
            balances.put(paidByName,
                    balances.getOrDefault(paidByName, BigDecimal.ZERO)
                            .add(expense.getAmount()).subtract(share));

            for (GroupMember m : members) {
                String name = m.getUser().getName();
                if (!name.equals(paidByName)) {
                    balances.put(name,
                            balances.getOrDefault(name, BigDecimal.ZERO).subtract(share));
                }
            }
        }

        return balances;
    }
}