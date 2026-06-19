package com.expensesplitter.controller;

import com.expensesplitter.model.Group;
import com.expensesplitter.model.User;
import com.expensesplitter.repository.UserRepository;
import com.expensesplitter.service.ExpenseService;
import com.expensesplitter.service.GroupService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final GroupService groupService;
    private final UserRepository userRepository;

    public ExpenseController(ExpenseService expenseService,
                             GroupService groupService,
                             UserRepository userRepository) {
        this.expenseService = expenseService;
        this.groupService = groupService;
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public String addExpense(@RequestParam String description,
                             @RequestParam BigDecimal amount,
                             @RequestParam Long groupId,
                             Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        Optional<Group> groupOpt = groupService.getGroupById(groupId);
        if (groupOpt.isEmpty()) return "redirect:/groups";
        expenseService.addExpense(description, amount, user, groupOpt.get());
        return "redirect:/groups/" + groupId;
    }
}