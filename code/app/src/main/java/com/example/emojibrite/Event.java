package com.example.emojibrite;

import android.net.Uri;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

/**
 *This is a construction class for events
 */
public class Event implements Serializable {

    private Uri imageUri;
    private String eventTitle;
    private Date date;
    private String time;
    private String description;
    private Integer milestone;
    private String location;
    private Uri checkInQRCode;
    private Uri eventQRCode;

    private Integer capacity;

    private String id;

    private String organizer;





    public Event(Uri imageUri, String eventTitle, Date date, String time, String description, Integer milestone, String location, Uri checkInQRCode, Uri eventQRCode, Integer capacity){

        this.imageUri=imageUri;
        this.eventTitle=eventTitle;
        this.date=date;
        this.time=time;
        this.description=description;
        this.milestone=milestone;
        this.location=location;
        this.eventQRCode=eventQRCode;
        this.checkInQRCode=checkInQRCode;
        this.id = generateRandomId();
        this.capacity = capacity;

    }

    public Event(Uri imageUri, String eventTitle, Date date, String time, String description, String location, Uri checkInQRCode, Uri eventQRCode, Integer capacity){

        this.imageUri=imageUri;
        this.eventTitle=eventTitle;
        this.date=date;
        this.time=time;
        this.description=description;
        this.location=location;
        this.eventQRCode=eventQRCode;
        this.checkInQRCode=checkInQRCode;
        this.capacity = capacity;

    } //without milestones. Feel free to suggest more stuff

    public Event(Uri imageUri, String eventTitle, Date date, String time, String description, Integer milestone, String location, Integer capacity){

        this.imageUri=imageUri;
        this.eventTitle=eventTitle;
        this.date=date;
        this.time=time;
        this.description=description;
        this.milestone=milestone;
        this.location=location;

        this.id = generateRandomId();
        this.capacity = capacity;

    }

    private String generateRandomId(){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i<6;i++){
            int inddex = random.nextInt(chars.length());
            sb.append(chars.charAt(inddex));
        }
        return sb.toString();
    }

    public String getId(){
        return id;
    }

    public void setId(){
        this.id=id;
    }

    public void setCapacity(){this.capacity=capacity;}



    public void setImagePath(Uri imageUri) {
        this.imageUri = imageUri;
    }
    public void setEventTitle(String eventTitle)
    {
        this.eventTitle=eventTitle;
    }

    public void setDate(Date date)
    {
        this.date=date;
    }
    public void setTime(String time)
    {
        this.time=time;
    }
    public void setDescription(String description)
    {
        this.description=description;
    }

    public void setMilestone(Integer milestone)
    {
        this.milestone=milestone;
    }


    public void setLocation(String location)
    {
        this.location=location;
    }

    public void setCheckInQRCode(Uri checkInQRCode)
    {
        this.checkInQRCode=checkInQRCode;
    }

    public void setEventQRCode(Uri eventQRCode)
    {
        this.eventQRCode=eventQRCode;
    }

    public Uri getImagePath()
    {
        return imageUri;
    }

    public String getEventTitle()
    {
        return eventTitle;
    }

    public Date getDate()
    {
        return date;
    }

    public String getTime()
    {
        return time;
    }
    public String getDescription()
    {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public Integer getMilestone() {
        return milestone;
    }

    public Uri getCheckInQRCode() {
        return checkInQRCode;
    }

    public Uri getEventQRCode() {
        return eventQRCode;
    }

    public Integer getCapacity(){return capacity;}


}