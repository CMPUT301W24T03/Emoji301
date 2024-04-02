package com.example.emojibrite;

import android.net.Uri;

public class Image {
    private String  userId, eventId;

    private Uri imageURL;

    public Image(){

    }
    public Uri getImageURL() {

        return imageURL;
    }

    public void setImageURL(Uri imageURL) {

        this.imageURL = imageURL;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }
}