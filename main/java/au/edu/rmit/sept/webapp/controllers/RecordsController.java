package au.edu.rmit.sept.webapp.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import au.edu.rmit.sept.webapp.models.MedRecord;
import au.edu.rmit.sept.webapp.models.VacRecord;
import au.edu.rmit.sept.webapp.models.Record;
import au.edu.rmit.sept.webapp.models.User;

import au.edu.rmit.sept.webapp.services.RecordService;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;



@Controller
@RequestMapping("/records")
public class RecordsController {

    private RecordService service;

    @Autowired
    public RecordsController(RecordService service) {
        this.service = service;
    }

    @GetMapping
    String records(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Collection<VacRecord> vacRecords = service.getVacRecordsUser(user.email());
            model.addAttribute("vacRecords", vacRecords);
            Collection<MedRecord> medRecords = service.getMedRecordsUser(user.email());
            model.addAttribute("medRecords", medRecords);
            return "records/records";
        } else {
            return "redirect:/";
        }

    }

    @PutMapping
    String create(@ModelAttribute Record record) {
        this.service.createRecord(
                record.owner(),
                record.petName(),
                record.date(),
                record.location(),
                record.description(),
                record.prescription(),
                record.treatment());
        return "redirect:/records";
    }

    @GetMapping("/download/vaccinations")
    public ResponseEntity<byte[]> downloadVaccinationRecords(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Collection<VacRecord> vacRecords = service.getVacRecordsUser(user.email());
            String file = "Vaccination Records\n\n";
            for (VacRecord vac : vacRecords) {
                file += "Vaccination: " + vac.description() +
                        ", Pet Name: " + vac.petName() +
                        ", Location of Vaccination: " + vac.location() +
                        ", Date Administered: " + vac.date() +
                        "\n";
            }
            byte[] fileBytes = file.toString().getBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("attachment", "vaccination-records.txt");
            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        
    }

    @GetMapping("/download/medical-history")
    public ResponseEntity<byte[]> downloadMedicalHistory(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Collection<MedRecord> medRecords = service.getMedRecordsUser(user.email());
            String file = "Medical History\n\n";
            for (MedRecord med : medRecords) {
                file += "Date: " + med.date() +
                        ", Clinic: " + med.location() +
                        ", Pet: " + med.petName() +
                        ", Assessment: " + med.description() +
                        ", Prescribed Medication: " + med.prescription() +
                        ", Treatment: " + med.treatment() + "\n";
            }
            byte[] fileBytes = file.toString().getBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("attachment", "medical-history.txt");
            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}