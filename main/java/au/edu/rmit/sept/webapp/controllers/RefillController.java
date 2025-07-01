package au.edu.rmit.sept.webapp.controllers;

import au.edu.rmit.sept.webapp.models.Order;
import au.edu.rmit.sept.webapp.models.Prescription;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.services.OrderService;
import au.edu.rmit.sept.webapp.services.PrescriptionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/refill")
public class RefillController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PrescriptionService prescriptionService;

@GetMapping
public String refill(@RequestParam String prescriptionName, HttpSession session, Model model) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
        return "redirect:/login";  // Redirect to login if the user is not logged in
    }

    // Fetch prescription by name and owner (user's first name)
    Prescription prescription = prescriptionService.getRefillPrescriptionByNameAndOwner(prescriptionName, user.email());

    if (prescription == null) {
        model.addAttribute("error", "Prescription not found for the current user");
        return "error"; // Show an error page if no prescription is found
    }

    // Add prescription details to the model
    model.addAttribute("selectedPrescription", prescription);
    //Add user detail attributes
    model.addAttribute("name", " "  + user.firstName() + " " + user.lastName());
    model.addAttribute("email", " " + user.email());
    return "prescription/refill";  // Show the refill form
}

    @PostMapping("/refillOrder")
    public String submitRefillOrder(@ModelAttribute Order order, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user != null) {
            // Fetch the prescription details based on the prescription name selected by the user
            Prescription prescription = prescriptionService.getRefillPrescriptionByNameAndOwner(order.prescriptionName(), user.email());

            // Check if the prescription was found
            if (prescription == null) {
                // If not found, return an error page or message
                model.addAttribute("error", "Prescription not found");

                return "prescription/refill";  // Show the refill form again with the error message
            }

            // Save the order and use the prescription details for refill
            orderService.saveOrder(order);
            prescriptionService.insertPrescription(
                user.email(), prescription.petName(), prescription.name(),
                LocalDate.now().toString(), prescription.prescriberLocation(),
                "New prescription details", prescription.quantity(),
                prescription.dosageInfo(), prescription.totalPrice()
            );

            return "redirect:/prescription";  
        } else {
            return "redirect:/login";
        }
    }
}
