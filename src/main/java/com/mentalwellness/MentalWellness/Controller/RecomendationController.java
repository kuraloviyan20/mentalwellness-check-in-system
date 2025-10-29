package com.mentalwellness.MentalWellness.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;

import com.mentalwellness.MentalWellness.Service.RecomendationService;
import com.mentalwellness.MentalWellness.Service.UserService;
import com.mentalwellness.MentalWellness.Service.TrendRecommendationService;
import com.mentalwellness.MentalWellness.model.Recomendation;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/recommendations")
public class RecomendationController {
    @Autowired
    private RecomendationService recommendationService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private TrendRecommendationService trendRecommendationService;
    
    @GetMapping("/user/{id}")
    public String getUserRecommendations(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("recommendations", recommendationService.getAllRecommendations(id));
            model.addAttribute("currentUser", userService.getUserById(id).orElse(null));
            return "recomendation/list";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/recommendations";
        }
    }

    @GetMapping
    public String getAllRecommendations(Model model) {
        model.addAttribute("recommendations", recommendationService.getAllRecommendations());
        return "recomendation/list";
    }
    
    @GetMapping("/new")
    public String createRecommendationForm(Model model) {
        model.addAttribute("rec", new Recomendation());
        model.addAttribute("users", userService.getAllUsers());
        return "recomendation/form";
    }

    @PostMapping
    public String createRecommendation(
            @Valid @ModelAttribute("rec") Recomendation recommendation,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers());
            return "recomendation/form";
        }

        try {
            Recomendation saved = recommendationService.createRecommendation(recommendation);
            redirectAttributes.addFlashAttribute("message", "Recommendation created successfully!");
            return "redirect:/recommendations/user/" + saved.getUser().getUser_id();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/recommendations/new";
        }
    }

    @GetMapping("/edit/{id}")
    public String editRecommendationForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("rec", recommendationService.getRecommendationById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recommendation ID: " + id)));
            model.addAttribute("users", userService.getAllUsers());
            return "recomendation/form";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/recommendations";
        }
    }

    @PostMapping("/update/{id}")
    public String updateRecommendation(
            @PathVariable Long id,
            @Valid @ModelAttribute("rec") Recomendation recommendation,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers());
            return "recomendation/form";
        }

        try {
            Recomendation updated = recommendationService.updateRecommendation(id, recommendation);
            redirectAttributes.addFlashAttribute("message", "Recommendation updated successfully!");
            return "redirect:/recommendations/user/" + updated.getUser().getUser_id();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/recommendations/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteRecommendation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Long userId = recommendationService.getRecommendationById(id)
                .map(rec -> rec.getUser().getUser_id())
                .orElse(null);
                
            recommendationService.deleteRecommendation(id);
            redirectAttributes.addFlashAttribute("message", "Recommendation deleted successfully!");
            
            if (userId != null) {
                return "redirect:/recommendations/user/" + userId;
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/recommendations";
    }

    @PostMapping("/generate/{userId}")
    public String generateFromTrends(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        try {
            Recomendation saved = trendRecommendationService.generateForUser(userId);
            redirectAttributes.addFlashAttribute("message", "Trend-based recommendation created.");
            return "redirect:/recommendations/user/" + saved.getUser().getUser_id();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/recommendations/user/" + userId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to generate recommendation: " + e.getMessage());
            return "redirect:/recommendations/user/" + userId;
        }
    }
}
