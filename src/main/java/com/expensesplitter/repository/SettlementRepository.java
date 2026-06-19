package com.expensesplitter.repository;

import com.expensesplitter.model.Group;
import com.expensesplitter.model.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    List<Settlement> findByGroupOrderByCreatedAtDesc(Group group);
}