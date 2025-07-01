package au.edu.rmit.sept.webapp.controllers;

import au.edu.rmit.sept.webapp.models.Prescription;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.services.PrescriptionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequestMapping("/prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

  
@GetMapping
public String prescription(HttpSession session, Model model) {
    User user = (User) session.getAttribute("user");

    if (user != null) {
        try {
            List<Prescription> prescriptions = prescriptionService.getPrescriptionInfo(user.email());
            
            if (prescriptions.isEmpty()) {
                model.addAttribute("message", "No prescriptions available.");
            } else {
                model.addAttribute("prescriptions", prescriptions);
            }
        } catch (Exception e) {
            model.addAttribute("message", "An error occurred while fetching prescriptions.");
        }
        return "prescription/prescription";
    } else {
        return "redirect:/";
    }
}
    @PostMapping
    public String handleRefill(Model model) {
        boolean success = true;

        if (success) {
            return "redirect:/prescription?success=true";
        } else {
            return "redirect:/prescription?success=false";
        }
    }

}