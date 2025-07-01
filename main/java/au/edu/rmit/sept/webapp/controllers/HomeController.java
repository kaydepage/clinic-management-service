package au.edu.rmit.sept.webapp.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import au.edu.rmit.sept.webapp.models.User;



@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("message", "Welcome, " + user.firstName() + "!");
        } else {
            return "redirect:/";
        }
        return "welcome/index";
    }

}