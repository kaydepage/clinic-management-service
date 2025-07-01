package au.edu.rmit.sept.webapp.controllers;

import au.edu.rmit.sept.webapp.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResourcesController {

    @GetMapping("/eduresources")
    public String showEducationalResources(Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "User not logged in.");
            return "redirect:/";
        }

        else if (user.isAdmin()) {
            model.addAttribute("error", "Admin users cannot access this page.");
            return "redirect:/admin/dashboard";
        }

        // Add any data you need to pass to the view here (if needed)
        return "eduresources/eduresources";  // This should match the HTML file name (educational-resources.html)
    }
}
