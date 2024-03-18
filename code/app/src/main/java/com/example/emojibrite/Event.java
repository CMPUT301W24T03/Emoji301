package com.example.emojibrite;

import android.net.Uri;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

/**
 *This is a construction class for events
 */
public class Event implements Serializable {

    private String imageUri;
    private String eventTitle;
    private Date date;
    private String time;
    private String description;
    private Integer milestone;
    private String location;
    private String checkInQRCode;
    private String eventQRCode;

//    private Uri checkInQRCode; // Store as String
//    private Uri eventQRCode;   // Store as String

    private Integer capacity;

    private String id;

    private String organizer;




    public Event(String id, String imageUri, String eventTitle, Date date, String time, String description, Integer milestone, String location, String checkInQRCode, String eventQRCode, Integer capacity, String organizer){

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
        this.organizer = organizer;

    }



    public Event(String  imageUri, String eventTitle, Date date, String time, String description, Integer milestone, String location, String checkInQRCode, String eventQRCode, Integer capacity, String organizer){

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
        this.organizer = organizer;

    }

    public Event(String imageUri, String eventTitle, Date date, String time, String description, Integer milestone, String location, Integer capacity, String organizer){

        this.imageUri=imageUri;
        this.eventTitle=eventTitle;
        this.date=date;
        this.time=time;
        this.description=description;
        this.milestone=milestone;
        this.location=location;
        this.id = generateRandomId();
        this.capacity = capacity;
        this.organizer = organizer;

    }

    public Event(String  imageUri, String eventTitle, Date date, String time, String description, Integer milestone, String location, String checkInQRCode, String eventQRCode, Integer capacity){

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

    public Event(String imageUri, String eventTitle, Date date, String time, String description, String location, String checkInQRCode, String eventQRCode, Integer capacity){

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

    public Event(String imageUri, String eventTitle, Date date, String time, String description, Integer milestone, String location, Integer capacity){

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

    public Event(){}

    private String generateRandomId(){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i<12;i++){
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
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



    public void setImageUri(String imageUri) {
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

    public void setCheckInQRCode(String checkInQRCode)
    {
        this.checkInQRCode = checkInQRCode;
    }

    public void setEventQRCode(String eventQRCode)
    {
        this.eventQRCode = eventQRCode;
    }

    public void setOrganizer(String organizer){this.organizer=organizer;}

    public Uri getImageUri()
    {
        return imageUri != null ? Uri.parse(imageUri) : null; //reconverting the string to an Image
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

        return checkInQRCode != null ? Uri.parse(checkInQRCode): null;
    }

    public Uri getEventQRCode() {
        return eventQRCode != null ? Uri.parse(eventQRCode): null;
    }

    public Integer getCapacity(){return capacity;}

    public String getOrganizer(){return organizer;}

}