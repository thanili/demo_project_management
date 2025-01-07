package org.example.project_management.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hub")
public class LoginWebController {
    @GetMapping("/login")
    public String showLoginPage() {
        return "/login";
    }
}
