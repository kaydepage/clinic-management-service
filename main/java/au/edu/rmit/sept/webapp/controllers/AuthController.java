package au.edu.rmit.sept.webapp.controllers;
import au.edu.rmit.sept.webapp.services.UserService;  
import au.edu.rmit.sept.webapp.models.User; 

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/")
public class AuthController {

     @Autowired
    private UserService userService;

    @GetMapping
    public String AuthPage() {
        return "user-auth/signup-login";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam("firstName") String firstName,
                         @RequestParam("lastName") String lastName,
                         @RequestParam("email") String email,
                         @RequestParam("password") String password,
                         Model model,
                         HttpSession session) {
        try {
            userService.createUser(firstName, lastName, email, password, false, null);
            User user = userService.validateLogin(email, password);
            session.setAttribute("user", user);
            return "redirect:/home";   // Redirect to home page after successful signup (Automatic login)
        } catch (Exception e) {
            model.addAttribute("error", "Signup failed: " + e.getMessage());
            return "user-auth/signup-login";  // Return to signup page with error message
        }
    }

@PostMapping("/login")
public String login(@RequestParam("email") String email, 
                    @RequestParam("password") String password, 
                    Model model,
                    HttpSession session) {
    try {
        User user = userService.validateLogin(email, password);
        if (user != null) {
            session.setAttribute("user", user); 
            if (userService.isUserAdmin(email)) { // Check if user is admin
                String clinic = user.location(); // Assuming you have a method to get location
                session.setAttribute("clinic", clinic); // Save clinic in session
                return "redirect:/admin/dashboard"; 
            }
            return "redirect:/home";  // Redirect to the home page on successful login
        } else {
            model.addAttribute("error", "Invalid login credentials");
            return "user-auth/signup-login";  // Redirect back if login fails
        }
    } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage());
        return "user-auth/signup-login";  // Redirect back with error message
    }
}

}
