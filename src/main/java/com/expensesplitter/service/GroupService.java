package com.expensesplitter.service;

import com.expensesplitter.model.Group;
import com.expensesplitter.model.GroupMember;
import com.expensesplitter.model.User;
import com.expensesplitter.repository.GroupMemberRepository;
import com.expensesplitter.repository.GroupRepository;
import com.expensesplitter.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository,
                        GroupMemberRepository groupMemberRepository,
                        UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userRepository = userRepository;
    }

    public Group createGroup(String name, String description, User creator) {
        Group group = new Group();
        group.setName(name);
        group.setDescription(description);
        group.setCreatedBy(creator);
        group = groupRepository.save(group);

        GroupMember member = new GroupMember();
        member.setGroup(group);
        member.setUser(creator);
        groupMemberRepository.save(member);

        return group;
    }

    public List<Group> getGroupsForUser(User user) {
        return groupRepository.findGroupsByMember(user);
    }

    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public String addMember(Long groupId, String email, User currentUser) {
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        if (groupOpt.isEmpty()) return "Group not found";

        Group group = groupOpt.get();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return "No user found with that email";

        User userToAdd = userOpt.get();
        if (groupMemberRepository.existsByGroupAndUser(group, userToAdd)) {
            return "User is already a member";
        }

        GroupMember member = new GroupMember();
        member.setGroup(group);
        member.setUser(userToAdd);
        groupMemberRepository.save(member);
        return "success";
    }
}