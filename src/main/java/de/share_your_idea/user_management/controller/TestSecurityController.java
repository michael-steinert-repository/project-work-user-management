package de.share_your_idea.user_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestSecurityController {

    @GetMapping("/admin/get")
    public String getAdmin() {
        return "Welcome Admin";
    }

    @GetMapping("/user/get")
    public String getUser() {
        return "Welcome User";
    }
}
