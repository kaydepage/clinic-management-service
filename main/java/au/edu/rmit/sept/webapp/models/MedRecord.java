package au.edu.rmit.sept.webapp.models;

public record MedRecord(String id, String owner, String petName, String date, String location, String description,
                String prescription, String treatment) {
}