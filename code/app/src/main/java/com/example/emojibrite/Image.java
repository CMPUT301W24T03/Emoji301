package com.example.emojibrite;

import android.net.Uri;

public class Image {
    private String  userId, eventId;

    private Uri imageURL;


    /**
     * Constructor for the Image class
     */
    public Image(){
    }

    /**
     * method to get image url
     * @return imageURL
     */
    public Uri getImageURL() {

        return imageURL;
    }
    /**
     * Method to get the image URL
     * @param imageURL
     */
    public void setImageURL(Uri imageURL) {

        this.imageURL = imageURL;
    }
    /**
     * Method to set the user ID
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * Method to get the user ID
     * @return userId
     */
    public String getUserId() {
        return userId;
    }
    /**
     * Method to set the event ID
     * @param eventId
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    /**
     * Method to get the event ID
     * @return eventId
     */
    public String getEventId() {
        return eventId;
    }
}