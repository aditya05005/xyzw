package application;

public class Request {
    private int requestID;
    private String requestDate;
    private String bloodGroup;
    private int volumeRequested;
    private int hospitalID;

    public Request(int requestID, String requestDate, String bloodGroup, int volumeRequested, int hospitalID) {
        this.requestID = requestID;
        this.requestDate = requestDate;
        this.bloodGroup = bloodGroup;
        this.volumeRequested = volumeRequested;
        this.hospitalID = hospitalID;
    }

    public int getRequestID() { return requestID; }
    public String getRequestDate() { return requestDate; }
    public String getBloodGroup() { return bloodGroup; }
    public int getVolumeRequested() { return volumeRequested; }
    public int getHospitalID() { return hospitalID; }
}
