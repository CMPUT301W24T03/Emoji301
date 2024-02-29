package com.example.emojibrite;

import java.util.Date;

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



    public Event(String imagePath, String eventTitle, Date date, String time, String description, Integer milestone, String location, Boolean checkInQRCode, Boolean eventQRCode){

        this.imagePath=imagePath;
        this.eventTitle=eventTitle;
        this.date=date;
        this.time=time;
        this.description=description;
        this.milestone=milestone;
        this.location=location;
        this.eventQRCode=eventQRCode;
        this.checkInQRCode=checkInQRCode;

    }

    public Event(String imagePath, String eventTitle, Date date, String time, String description, String location, Boolean checkInQRCode, Boolean eventQRCode){

        this.imagePath=imagePath;
        this.eventTitle=eventTitle;
        this.date=date;
        this.time=time;
        this.description=description;
        this.location=location;
        this.eventQRCode=eventQRCode;
        this.checkInQRCode=checkInQRCode;

    } //without milestones. Feel free to suggest more stuff



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
}