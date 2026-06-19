package com.expensesplitter.service;

import com.expensesplitter.model.Expense;
import com.expensesplitter.model.GroupMember;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class EmailService {

    private final Optional<JavaMailSender> mailSender;

    public EmailService(Optional<JavaMailSender> mailSender) {
        this.mailSender = mailSender;
    }

    public void sendExpenseNotification(Expense expense) {
        if (mailSender.isEmpty()) {
            System.out.println("Mail not configured, skipping email.");
            return;
        }
        for (GroupMember member : expense.getGroup().getMembers()) {
            String email = member.getUser().getEmail();
            if (email.equals(expense.getPaidBy().getEmail())) continue;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("New expense in " + expense.getGroup().getName());
            message.setText(
                "Hi " + member.getUser().getName() + ",\n\n" +
                expense.getPaidBy().getName() + " added a new expense:\n" +
                "Description: " + expense.getDescription() + "\n" +
                "Amount: ₹" + expense.getAmount() + "\n\n" +
                "- SplitEase Team"
            );
            mailSender.get().send(message);
        }
    }
}