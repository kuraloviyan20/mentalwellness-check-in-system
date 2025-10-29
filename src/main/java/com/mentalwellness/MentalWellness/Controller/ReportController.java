package com.mentalwellness.MentalWellness.Controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mentalwellness.MentalWellness.Service.ReportService;
import com.mentalwellness.MentalWellness.Service.UserService;
import com.mentalwellness.MentalWellness.model.User;

@Controller
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private UserService userService;

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @GetMapping("/mood-trends/user/{id}")
    public String moodTrends(@PathVariable Long id, Model model) {
        Map<java.time.LocalDate, Double> data = reportService.getAverageMoodByDateForUser(id);
        List<String> labels = data.keySet().stream().map(d -> d.format(DF)).collect(Collectors.toList());
        List<Double> values = data.values().stream().collect(Collectors.toList());
        User user = userService.getUserById(id).orElse(null);
        model.addAttribute("labels", labels);
        model.addAttribute("values", values);
        model.addAttribute("user", user);
        return "reports/mood-trends";
    }

    @GetMapping("/stress-trends/user/{id}")
    public String stressTrends(@PathVariable Long id, Model model) {
        Map<java.time.LocalDate, Double> data = reportService.getAverageStressByDateForUser(id);
        List<String> labels = data.keySet().stream().map(d -> d.format(DF)).collect(Collectors.toList());
        List<Double> values = data.values().stream().collect(Collectors.toList());
        User user = userService.getUserById(id).orElse(null);
        model.addAttribute("labels", labels);
        model.addAttribute("values", values);
        model.addAttribute("user", user);
        return "reports/stress-trends";
    }
}
