package au.edu.rmit.sept.webapp.models;


public record Order(String prescriberLocation,
    String ownerFirstName,
    String streetAddress,
    String city,
    String state,
    String postcode,
    String prescriptionName,
    String quantity,
    String dosageInfo,
    Double totalPrice
) {
    
}