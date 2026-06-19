package com.expensesplitter.controller;

import com.expensesplitter.model.User;
import com.expensesplitter.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String profile(Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("userEmail", auth.getName());
        return "profile";
    }

    @PostMapping("/upload")
    public String uploadPicture(@RequestParam("file") MultipartFile file,
                                Authentication auth) throws IOException {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String filename = "user_" + user.getId() + "_" +
                          file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.]", "_");
        Path path = Paths.get(UPLOAD_DIR + filename);
        Files.write(path, file.getBytes());

        user.setProfilePicture("/uploads/" + filename);
        userRepository.save(user);

        return "redirect:/profile";
    }
}