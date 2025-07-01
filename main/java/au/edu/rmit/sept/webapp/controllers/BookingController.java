package au.edu.rmit.sept.webapp.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import au.edu.rmit.sept.webapp.models.Booking;
import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.services.BookingService;
import au.edu.rmit.sept.webapp.services.ClinicService;
import au.edu.rmit.sept.webapp.repositories.PetRepository;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private BookingService service;
    private ClinicService clinicService;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    public BookingController(BookingService service, ClinicService clinicService) {
        this.service = service;
        this.clinicService = clinicService;
    }

    @GetMapping
    String bookings(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "User not logged in.");
            return "redirect:/";
        }

        else if (user.isAdmin()) {
            model.addAttribute("error", "Admin users cannot access this page.");
            return "redirect:/admin/dashboard";
        }

        Collection<Booking> bookings = service.getBookingsUser(user.email());
        model.addAttribute("bookings", bookings);
        model.addAttribute("user", user.email());
        return "bookings/bookings";

    }

    @GetMapping("/newBooking")
    public String newBooking(@RequestParam(value = "location", required = false) String location, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "User not logged in.");
            return "redirect:/";
        }

        else if (user.isAdmin()) {
            model.addAttribute("error", "Admin users cannot access this page.");
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("booking", new Booking(null, "", "", "", "", "", ":", null));
        model.addAttribute("selectedLocation", location);
        model.addAttribute("selectedDate", LocalDate.now());
        return "bookings/bookingForm";
    }

    @GetMapping("/newBooking/get-available-times")
    public String getAvailableBookings(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String location,
            Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "User not logged in.");
            return "redirect:/";
        }

        else if (user.isAdmin()) {
            model.addAttribute("error", "Admin users cannot access this page.");
            return "redirect:/admin/dashboard";
        }

        List<Pet> pets = petRepository.findPetByOwner(user.firstName());
        model.addAttribute("pets", pets);

        List<LocalTime> availableTimes = this.service.getAvailableTimes(date, location);
        Collection<Clinic> clinicPrices = this.clinicService.getPricesClinic(location);
        model.addAttribute("availableTimes", availableTimes);
        model.addAttribute("clinicPrices", clinicPrices);
        model.addAttribute("selectedDate", date);
        model.addAttribute("selectedLocation", location);

        return "bookings/bookingForm";
    }

    @PostMapping("/newBooking/add-booking")
    public String create(@ModelAttribute Booking booking, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Long workingId = (Long) session.getAttribute("workingId");
        String[] visitReasonPrice = booking.visitReason().split(":");
        if (user != null) {
            if (workingId != null) {
                this.service.updateBooking(
                        user.email(),
                        booking.petName(),
                        booking.date(),
                        booking.time(),
                        booking.location(),
                        visitReasonPrice[0],
                        visitReasonPrice[1],
                        workingId);
                session.setAttribute("workingId", null);
            } else {
                this.service.create(
                        user.email(),
                        booking.petName(),
                        booking.date(),
                        booking.time(),
                        booking.location(),
                        visitReasonPrice[0],
                        visitReasonPrice[1]);
            }

            return "redirect:/bookings";
        } else {
            return "redirect:/";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        // System.out.println("Booking ID to delete: " + id);
        this.service.deleteBookingById(id);
        return "redirect:/bookings";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, HttpSession session, Model model) {
        session.setAttribute("workingId", id);
        model.addAttribute("selectedDate", LocalDate.now());
        return "bookings/bookingForm";
    }

}