/**
 * Created by Yuri Khechoyan on 1/1/2017.
 */

package com.sweng.customerqueue.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Variables
    @NotNull
    @Size(min=1)
    private String firstName;

    @NotNull
    @Size(min=1)
    private String lastName;

    @NotNull
    @Pattern(regexp = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$")
    private String mobileNumber;

    @NotNull
    private String reasonForVisit;

    private Timestamp checkInTime;

    private Timestamp checkOutTime;

    private int place;

    public Customer(){
    }

    public Customer(String firstName, String lastName, String mobileNumber){
        //constructor for the customer
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
    }

//SETTERS AND GETTERS

    public void setId(Long id) { this.id = id; }
    public void setFirstName(String name){
        firstName = name;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setMobileNumber(String mobileNumber){
        this.mobileNumber = mobileNumber;
    }
    public void setReasonForVisit(String reasonForVisit) { this.reasonForVisit = reasonForVisit;}
    public void setCheckInTime(Timestamp checkInTime)
        { this.checkInTime = checkInTime; }
    public void setCheckOutTime(Timestamp checkOutTime)
        { this.checkOutTime = checkOutTime; }
    public void setPlace(int place) { this.place = place; }


    public Long getId() { return id; }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getMobileNumber(){
        return mobileNumber;
    }
    public String getReasonForVisit() { return reasonForVisit; }
    public Timestamp getCheckInTime() { return checkInTime; }
    public Timestamp getCheckOutTime() { return checkOutTime; }
    public int getPlace() { return place; }
}
