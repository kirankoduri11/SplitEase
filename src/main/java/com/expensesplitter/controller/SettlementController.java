package com.expensesplitter.controller;

import com.expensesplitter.model.Group;
import com.expensesplitter.model.User;
import com.expensesplitter.repository.UserRepository;
import com.expensesplitter.service.GroupService;
import com.expensesplitter.service.SettlementService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/settlements")
public class SettlementController {

    private final SettlementService settlementService;
    private final GroupService groupService;
    private final UserRepository userRepository;

    public SettlementController(SettlementService settlementService,
                                GroupService groupService,
                                UserRepository userRepository) {
        this.settlementService = settlementService;
        this.groupService = groupService;
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public String addSettlement(@RequestParam String paidToEmail,
                                @RequestParam BigDecimal amount,
                                @RequestParam Long groupId,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        User paidBy = userRepository.findByEmail(auth.getName()).orElseThrow();
        Optional<Group> groupOpt = groupService.getGroupById(groupId);
        if (groupOpt.isEmpty()) return "redirect:/groups";

        String result = settlementService.settle(paidBy, paidToEmail, amount, groupOpt.get());
        if (!result.equals("success")) {
            redirectAttributes.addFlashAttribute("errorMsg", result);
        }
        return "redirect:/groups/" + groupId;
    }
}