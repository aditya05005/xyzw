package application;

public class Hospital {
    private int hospitalId;
    private String name;
    private String contact;
    private String location;
    private String billingCode;

    public Hospital(int hospitalId, String name, String contact, String location, String billingCode) {
        this.hospitalId = hospitalId;
        this.name = name;
        this.contact = contact;
        this.location = location;
        this.billingCode = billingCode;
    }

    public int getHospitalId() { return hospitalId; }
    public void setHospitalId(int hospitalId) { this.hospitalId = hospitalId; }

    public String getName() { return name;}
    public void setName(String name) { this.name = name;}
    
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getBillingCode() { return billingCode; }
    public void setBillingCode(String billingCode) { this.billingCode = billingCode; }
}
