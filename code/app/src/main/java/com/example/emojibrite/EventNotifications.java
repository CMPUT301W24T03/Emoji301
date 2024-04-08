package com.example.emojibrite;

import android.net.Uri;

public class EventNotifications {

    String eventTitle;
    String notifMessage;

    Uri imageUri;


    /**
     * Constructor for notifications
     * @param eventTitle Event Title
     * @param notifMessage Notification message
     * @param imageUri Event Poster
     */
    public EventNotifications(String eventTitle, String notifMessage, Uri imageUri){
        this.eventTitle=eventTitle;
        this.notifMessage=notifMessage;
        this.imageUri=imageUri; //event poster
    }

    /**
     * Gets the event Title
     * @return event title
     */
    public String getEventTitle(){
        return this.eventTitle;
    }

    /**
     * Gets the message
     * @return notification message
     */
    public String getNotifMessage(){
        return this.notifMessage;
    }

    /**
     * Gets the poster picture
     * @return gets the image
     */
    public Uri getImageUri(){
        return this.imageUri;
    }

    /**
     * Sets the event title
     * @param eventTitle String for the event Title
     */
    public void setEventTitle(String eventTitle){this.eventTitle=eventTitle;}

    /**
     * Sets the notification message
     * @param notifMessage notification message
     */
    public void setNotifMessage(String notifMessage){this.notifMessage=notifMessage;}

    /**
     * Sets the event poster picture
     * @param imageUri the image poster
     */
    public void setImageUri(Uri imageUri){this.imageUri=imageUri;}





}
