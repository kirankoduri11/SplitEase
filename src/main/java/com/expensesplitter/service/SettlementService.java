package com.expensesplitter.service;

import com.expensesplitter.model.Group;
import com.expensesplitter.model.Settlement;
import com.expensesplitter.model.User;
import com.expensesplitter.repository.SettlementRepository;
import com.expensesplitter.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final UserRepository userRepository;

    public SettlementService(SettlementRepository settlementRepository,
                             UserRepository userRepository) {
        this.settlementRepository = settlementRepository;
        this.userRepository = userRepository;
    }

    public String settle(User paidBy, String paidToEmail,
                         BigDecimal amount, Group group) {
        Optional<User> paidToOpt = userRepository.findByEmail(paidToEmail);
        if (paidToOpt.isEmpty()) return "No user found with that email";

        Settlement settlement = new Settlement();
        settlement.setPaidBy(paidBy);
        settlement.setPaidTo(paidToOpt.get());
        settlement.setAmount(amount);
        settlement.setGroup(group);
        settlementRepository.save(settlement);
        return "success";
    }

    public List<Settlement> getSettlementsForGroup(Group group) {
        return settlementRepository.findByGroupOrderByCreatedAtDesc(group);
    }
}