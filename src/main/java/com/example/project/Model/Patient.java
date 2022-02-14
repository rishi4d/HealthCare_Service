package com.example.project.Model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Data
public class Patient {

    private String patient_Id;
    private String patient_name;
    private String patient_email;
    private String patient_mobile;
    private Date registeredDate;


    public Patient(String patient_name, String patient_email, String patient_mobile, Date registeredDate) {
        this.patient_name = patient_name;
        this.patient_email = patient_email;
        this.patient_mobile = patient_mobile;
        this.registeredDate = registeredDate;
    }

    public Patient() {
        super();
    }

}
