package application;

import java.sql.Date;

public class BloodUnit {
    private int bloodID;
    private int donorID;
    private Date donationDate;
    private int volume;
    private String status;

    public BloodUnit(int bloodID, int donorID, Date donationDate, int volume, String status) {
        this.bloodID = bloodID;
        this.donorID = donorID;
        this.donationDate = donationDate;
        this.volume = volume;
        this.status = status;
    }

    public int getBloodID() {
        return bloodID;
    }

    public int getDonorID() {
        return donorID;
    }

    public Date getDonationDate() {
        return donationDate;
    }

    public int getVolume() {
        return volume;
    }

    public String getStatus() {
        return status;
    }
}
