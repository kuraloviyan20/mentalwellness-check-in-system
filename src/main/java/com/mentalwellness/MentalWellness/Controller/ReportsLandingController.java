package com.mentalwellness.MentalWellness.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mentalwellness.MentalWellness.Service.UserService;

@Controller
public class ReportsLandingController {

    @Autowired
    private UserService userService;

    @GetMapping("/reports")
    public String reportsHome(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "reports/index";
    }
}
