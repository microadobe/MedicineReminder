package com.kadirsoran.medicinereminder;

public class Prescription {

    private String prescriptionName;
    private String dosage;
    private String frequency;
    private String duration;

    public Prescription(String prescriptionName, String dosage, String frequency, String duration) {
        this.prescriptionName = prescriptionName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
    }

    public String getPrescriptionName() {
        return prescriptionName;
    }

    public String getDosage() {
        return dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getDuration() {
        return duration;
    }
}
