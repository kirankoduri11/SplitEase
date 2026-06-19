package com.expensesplitter.controller;

import com.expensesplitter.model.Group;
import com.expensesplitter.model.User;
import com.expensesplitter.repository.UserRepository;
import com.expensesplitter.service.ExpenseService;
import com.expensesplitter.service.GroupService;
import com.expensesplitter.service.SettlementService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private final UserRepository userRepository;
    private final ExpenseService expenseService;
    private final SettlementService settlementService;

    public GroupController(GroupService groupService,
                           UserRepository userRepository,
                           ExpenseService expenseService,
                           SettlementService settlementService) {
        this.groupService = groupService;
        this.userRepository = userRepository;
        this.expenseService = expenseService;
        this.settlementService = settlementService;
    }

    @GetMapping
    public String listGroups(Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("groups", groupService.getGroupsForUser(user));
        model.addAttribute("userEmail", auth.getName());
        return "groups";
    }

    @PostMapping("/create")
    public String createGroup(@RequestParam String name,
                              @RequestParam String description,
                              Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        groupService.createGroup(name, description, user);
        return "redirect:/groups";
    }

    @GetMapping("/{id}")
    public String groupDetail(@PathVariable Long id, Model model, Authentication auth) {
        Optional<Group> groupOpt = groupService.getGroupById(id);
        if (groupOpt.isEmpty()) return "redirect:/groups";
        Group group = groupOpt.get();
        model.addAttribute("group", group);
        model.addAttribute("userEmail", auth.getName());
        model.addAttribute("expenses", expenseService.getExpensesForGroup(group));
        model.addAttribute("balances", expenseService.calculateBalances(group));
        model.addAttribute("settlements", settlementService.getSettlementsForGroup(group));
        return "group-detail";
    }

    @PostMapping("/{id}/add-member")
    public String addMember(@PathVariable Long id,
                            @RequestParam String email,
                            Authentication auth,
                            RedirectAttributes redirectAttributes) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        String result = groupService.addMember(id, email, user);
        if (!result.equals("success")) {
            redirectAttributes.addFlashAttribute("errorMsg", result);
        }
        return "redirect:/groups/" + id;
    }
}