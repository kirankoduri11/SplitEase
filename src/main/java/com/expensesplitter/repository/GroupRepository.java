package com.expensesplitter.repository;

import com.expensesplitter.model.Group;
import com.expensesplitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("SELECT g FROM Group g JOIN g.members m WHERE m.user = :user")
    List<Group> findGroupsByMember(@Param("user") User user);
}