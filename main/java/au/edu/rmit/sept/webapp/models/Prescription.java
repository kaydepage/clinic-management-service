package au.edu.rmit.sept.webapp.models;

public record Prescription(String owner, String petName, String name, String dateOfIssue, String prescriberLocation,
                String description,
                String quantity, String dosageInfo, double totalPrice) {

}
