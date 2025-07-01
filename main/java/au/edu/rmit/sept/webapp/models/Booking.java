package au.edu.rmit.sept.webapp.models;

import java.util.Date;

public record Booking(Long id, String owner, String petName, String date, String time, String location,
                String visitReason, String price) {
}