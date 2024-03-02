package com.example.emojibrite;

import java.util.Date;
import java.util.Random;

public class Event {

    private String imagePath;
    private String eventTitle;
    private Date date;
    private String time;
    private String description;
    private Integer milestone;
    private String location;
    private Boolean checkInQRCode;
    private Boolean eventQRCode;

    private Integer capacity;

    private String id;



    public Event(String imagePath, String eventTitle, Date date, String time, String description, Integer milestone, String location, Boolean checkInQRCode, Boolean eventQRCode, Integer capacity){

        this.imagePath=imagePath;
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

    public Event(String imagePath, String eventTitle, Date date, String time, String description, String location, Boolean checkInQRCode, Boolean eventQRCode, Integer capacity){

        this.imagePath=imagePath;
        this.eventTitle=eventTitle;
        this.date=date;
        this.time=time;
        this.description=description;
        this.location=location;
        this.eventQRCode=eventQRCode;
        this.checkInQRCode=checkInQRCode;
        this.capacity = capacity;

    } //without milestones. Feel free to suggest more stuff

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



    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public void setCheckInQRCode(Boolean checkInQRCode)
    {
        this.checkInQRCode=checkInQRCode;
    }

    public void setEventQRCode(Boolean eventQRCode)
    {
        this.eventQRCode=eventQRCode;
    }

    public String getImagePath()
    {
        return imagePath;
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

    public Boolean getCheckInQRCode() {
        return checkInQRCode;
    }

    public Boolean getEventQRCode() {
        return eventQRCode;
    }

    public Integer getCapacity(){return capacity;}
}