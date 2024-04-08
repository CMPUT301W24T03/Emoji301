package com.example.emojibrite;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 *This is a construction class for events
 */
public class Event implements Serializable {

    /**
     * This is the event poster
     */
    private String imageUri;
    /**
     * This is the event title
     */
    private String eventTitle;

    /**
     * This is the date of the event
     */
    private Date date;
    /**
     * This is the time of the event
     */
    private String time;

    /**
     * This is the event description
     */
    private String description;
    /**
     * This is the milestone of the event
     */
    private Integer milestone;
    /**
     * This is the location of the event
     */
    private String location;
    /**
     * This is the QR Code for checking in
     */
    private String checkInQRCode;
    /**
     * This is the QR Code for event details
     */
    private String eventQRCode;

//    private Uri checkInQRCode; // Store as String
//    private Uri eventQRCode;   // Store as String

    /**
     * This is the event capacity
     */
    private Integer capacity;

    /**
     * This is the event ID
     */
    private String id;

    /**
     * This is the organizer ID
     */
    private String organizer;

    /**
     * This is the check in ID
     */
    private String checkInID;

    /**
     * This is a list of attendees
     */
    private ArrayList<String> attendeesList = new ArrayList<>();

    /**
     * This is a list of geolocations
     */
    private ArrayList<String> geolocationList = new ArrayList<>();

    /**
     * This is the current attendance
     */
    private Integer currentAttendance;


    /**
     * This is a construction class for events
     * @param id this is the event ID
     * @param imageUri This is the event poster
     * @param eventTitle This is the event title
     * @param date This is the date of the event
     * @param time This is the time of the event
     * @param description This is the event description
     * @param milestone This is the milestone of the event
     * @param location This is the location of the event
     * @param checkInQRCode This is the QR Code for checking in
     * @param eventQRCode This is the QR Code for event details
     * @param capacity This is the event capacity
     * @param organizer This is the organizer id
     * @param checkInID  this is chekced in id
     * @param currentAttendance currentattendance
     */

    public Event(String id, String imageUri, String eventTitle, Date date, String time, String description, Integer milestone, String location, String checkInQRCode, String eventQRCode, Integer capacity, String organizer, String checkInID, Integer currentAttendance){

        this.imageUri=imageUri;
        this.eventTitle=eventTitle;
        this.date=date;
        this.time=time;
        this.description=description;
        this.milestone=milestone;
        this.location=location;
        this.eventQRCode=eventQRCode;
        this.checkInQRCode=checkInQRCode;
        this.id = id;
        this.capacity = capacity;
        this.organizer = organizer;
        this.checkInID = checkInID;
        this.currentAttendance=currentAttendance;

    }


    /**
     * event constructor
     * @param imageUri imageuri
     * @param eventTitle event title
     * @param date date
     * @param time time
     * @param description description
     * @param milestone milestone
     * @param location location
     * @param checkInQRCode checkin
     * @param eventQRCode eventqr
     * @param capacity capacity
     * @param organizer organizer
     */
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

    /**
     * event constructor
     * @param imageUri imageuri
     * @param eventTitle event title
     * @param date date
     * @param time time
     * @param description description
     * @param milestone milestone
     * @param location location
     * @param capacity capacity
     * @param organizer organizer
     */
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

    /**
     * event constructor
     * @param imageUri imageuri
     * @param eventTitle event title
     * @param date date
     * @param time time
     * @param description description
     * @param location location
     * @param milestone milestone
     * @param checkInQRCode checkin
     * @param eventQRCode eventqr
     * @param capacity capacity
     */
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

    /**
     * event constructor
     * @param imageUri imageuri
     * @param eventTitle event title
     * @param date date
     * @param time time
     * @param description description
     * @param location location
     * @param checkInQRCode checkin
     * @param eventQRCode eventqr
     * @param capacity capacity
     */
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

    /**
     * event constructor
     * @param imageUri imageuri
     * @param eventTitle event title
     * @param date date
     * @param time time
     * @param description description
     * @param milestone milestone
     * @param location location
     * @param capacity capacity
     */
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

    /**
     * event consutrco
     */
    public Event(){}

    /**
     *
     * @param imageUri imageuri
     */
    public Event(String imageUri) {
        this.imageUri = imageUri;
    }

    /**
     *
     * @return randomId
     */
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

    /**
     *
     * @return id
     */
    public String getId(){
        return id;
    }

    /**
     *
     * @param id id
     */
    public void setId(String id){
        this.id=id;
    }

    /**
     *
     * @param capacity capacity
     */
    public void setCapacity(Integer capacity){this.capacity=capacity;}

    /**
     *
     * @param checkInID check in
     */
    public void setCheckInID(String checkInID){this.checkInID=checkInID;}


    /**
     *
     * @param imageUri imageuri
     */
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    /**
     *
     * @param eventTitle event tite
     */
    public void setEventTitle(String eventTitle)
    {
        this.eventTitle=eventTitle;
    }

    /**
     *
     * @param date date
     */
    public void setDate(Date date)
    {
        this.date=date;
    }

    /**
     *
     * @param time time
     */
    public void setTime(String time)
    {
        this.time=time;
    }

    /**
     *
     * @param description description
     */
    public void setDescription(String description)
    {
        this.description=description;
    }

    /**
     *
     * @param milestone milestone
     */
    public void setMilestone(Integer milestone)
    {
        this.milestone=milestone;
    }

    /**
     *
     * @param location location
     */
    public void setLocation(String location)
    {
        this.location=location;
    }

    /**
     *
     * @param checkInQRCode checkin
     */
    public void setCheckInQRCode(String checkInQRCode)
    {
        this.checkInQRCode = checkInQRCode;
    }

    /**
     *
     * @param eventQRCode eventqr
     */
    public void setEventQRCode(String eventQRCode)
    {
        this.eventQRCode = eventQRCode;
    }

    /**
     *
     * @param organizer organizer
     */
    public void setOrganizer(String organizer){this.organizer=organizer;}

    /**
     *
     * @return imageuri
     */
    public Uri getImageUri()
    {
        return imageUri != null ? Uri.parse(imageUri) : null; //reconverting the string to an Image
    }

    /**
     *
     * @return event title
     */
    public String getEventTitle()
    {
        return eventTitle;
    }

    /**
     *
     * @return date
     */
    public Date getDate()
    {
        return date;
    }

    /**
     *
     * @return time
     */
    public String getTime()
    {
        return time;
    }

    /**
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     *
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @return milestone
     */
    public Integer getMilestone() {
        return milestone;
    }

    /**
     *
     * @return checkin
     */
    public Uri getCheckInQRCode() {

        return checkInQRCode != null ? Uri.parse(checkInQRCode): null;
    }

    /**
     *
     * @return eventqr
     */
    public Uri getEventQRCode() {
        return eventQRCode != null ? Uri.parse(eventQRCode): null;
    }

    /**
     *
     * @return capacity
     */
    public Integer getCapacity(){return capacity;}

    /**
     *
     * @return organizer
     */
    public String getOrganizer(){return organizer;}

    /**
     *
     * @return getcheckin
     */
    public String getCheckInID(){return checkInID;}

    /**
     *
     * @return attendee
     */
    public ArrayList<String> getAttendeesList() {
        return attendeesList;
    }

    /**
     *
     * @param attendesList list
     */
    public void setAttendeesList(ArrayList<String> attendesList) {
        this.attendeesList = attendesList;
    }

    /**
     *
     * @return geoloication
     */
    public ArrayList<String> getGeolocationList() {
        return geolocationList;
    }

    /**
     *
     * @param geolocationList list
     */
    public void setGeolocationList(ArrayList<String> geolocationList) {
        this.geolocationList = geolocationList;
    }

    /**
     *
     * @return current attendance
     */
    public Integer getcurrentAttendance(){return this.currentAttendance;}

    /**
     *
     * @param currentAttendance attendance
     */
    public void setCurrentAttendance(Integer currentAttendance){this.currentAttendance=currentAttendance;}

}