package au.edu.rmit.sept.webapp.controllers;

import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.models.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import au.edu.rmit.sept.webapp.services.UserService;
import au.edu.rmit.sept.webapp.services.PetService;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @GetMapping("/profile")
    public String prescription(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("name", " "  + user.firstName() + " " + user.lastName());
            model.addAttribute("email", " " + user.email());

            List<Pet> pets = petService.getPetsByOwner(user.firstName());
            model.addAttribute("pets", pets);

            return "profile/profile";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/registerPet")
    
    public String showRegisterPetForm(HttpSession session, Model model) {
    User user = (User) session.getAttribute("user");
    if (user != null) {
        return "profile/registerPet"; // This should match the name of the HTML file.
    } else {
        return "redirect:/";
    }
    }

    @PostMapping("/registerPet")
public String registerPet(
        @RequestParam("name") String name,
        @RequestParam("breed") String breed,
        @RequestParam("age") int age,
        HttpSession session, Model model) {

    User user = (User) session.getAttribute("user");

    try {
        if (user != null) {
            
            petService.createPet(name, breed, age, user.firstName(), user.lastName());
            
            return "redirect:/profile";
        } else {
            return "redirect:/";
        }
    } catch (Exception e) {
        
        model.addAttribute("error", "Pet registration failed: " + e.getMessage());
        return "profile/registerPet";
    }
}

}


