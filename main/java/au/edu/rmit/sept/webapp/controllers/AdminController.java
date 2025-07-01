package au.edu.rmit.sept.webapp.controllers;

import java.util.Collection;
import java.util.Objects;

import au.edu.rmit.sept.webapp.services.PrescriptionService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import au.edu.rmit.sept.webapp.models.Booking;
import au.edu.rmit.sept.webapp.models.MedRecord; // Updated import
import au.edu.rmit.sept.webapp.models.Record;
import au.edu.rmit.sept.webapp.models.Order;
import au.edu.rmit.sept.webapp.models.User; 
import au.edu.rmit.sept.webapp.models.VacRecord; // Updated import
import au.edu.rmit.sept.webapp.services.BookingService;
import au.edu.rmit.sept.webapp.services.RecordService;
import au.edu.rmit.sept.webapp.services.OrderService;

@Controller
public class AdminController {

    private BookingService bService;
    private RecordService rService;
    private OrderService oService;
    private PrescriptionService pService;

    @Autowired
    public AdminController(BookingService bService, RecordService rService, OrderService oService, PrescriptionService pService) {
        this.bService = bService;
        this.rService = rService;
        this.oService = oService;
        this.pService = pService;
    }

    // Existing methods for dashboard and other views
    @GetMapping("/admin/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "User not logged in.");
            return "redirect:/";
        }

        else if (!user.isAdmin()) {
            model.addAttribute("error", "User does not have admin privileges");
            return "redirect:/home";
        }
        return "admin/dashboard"; // Path to the Thymeleaf template for the dashboard
    }

    @GetMapping("/admin/manage-records")
    public String manageRecords(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "User not logged in.");
            return "redirect:/";
        }

        else if (!user.isAdmin()) {
            model.addAttribute("error", "User does not have admin privileges");
            return "redirect:/home";
        }
        return "admin/manageRecords";
    }

    @GetMapping("/admin/view-bookings")
    public String viewBookings(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "User not logged in.");
            return "redirect:/";
        }

        else if (!user.isAdmin()) {
            model.addAttribute("error", "User does not have admin privileges");
            return "redirect:/home";
        }

        String clinic = user.location();
        Collection<Booking> bookings = bService.getBookingsClinic(clinic);
        model.addAttribute("bookings", bookings);
        return "admin/viewBookings";
    }

    @GetMapping("/admin/view-orders")
    public String viewOrders(Model model, HttpSession session) {
    User user = (User) session.getAttribute("user");

    if (user == null) {
        model.addAttribute("error", "User not logged in.");
        return "redirect:/";
    }

    else if (!user.isAdmin()) {
        model.addAttribute("error", "User does not have admin privileges");
        return "redirect:/home";
    }
    String clinic = user.location(); 
    if (clinic == null) {
        model.addAttribute("error", "Clinic not found for the logged-in user.");
        return "admin/manageRecords"; // Return to manage records or handle accordingly
    }
    Collection<Order> orders = oService.getOrdersClinic(clinic); 
    model.addAttribute("orders", orders);
    return "admin/viewOrders";

    }

    // Updated method for adding a medical record
    @PostMapping("/admin/add-medical-record")
    public String addMedicalRecord(@ModelAttribute MedRecord medRecord, Model model, HttpSession session) {
        // Logic to save the medical record to the database

        // Set success message in the model
        model.addAttribute("medicalRecordSuccess", true);
        return "admin/manageRecords"; // Redirect to the same page after successful submission
    }

    // Updated method for adding a vaccination record
    @PostMapping("/admin/add-vaccination-record")
    public String addVaccinationRecord(@ModelAttribute VacRecord vacRecord, Model model, HttpSession session) {
        // Logic to save the vaccination record to the database

        // Set success message in the model
        model.addAttribute("vaccinationRecordSuccess", true);
        return "admin/manageRecords"; // Redirect to the same page after successful submission
    }

    @PostMapping("admin/add-record")
    public String addRecord(@ModelAttribute Record record, Model model, HttpSession session) {
        this.rService.createRecord(
                record.owner(),
                record.petName(),
                record.date(),
                record.location(),
                record.description(),
                record.prescription(),
                record.treatment());
        if (!Objects.equals(record.description(), "vaccination")) {
            this.pService.insertPrescription(
                    record.owner(),
                    record.petName(),
                    record.prescription(),
                    record.date(),
                    record.location(),
                    "New prescription details.",
                    "20",
                    "350mg",
                    25.99);
        }
        return "admin/manageRecords";
    }

}
