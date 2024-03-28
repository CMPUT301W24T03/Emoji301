package com.example.emojibrite;

public class ImageClass {
    private String imageURL, caption;

    public ImageClass(){

    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public ImageClass(String imageURL, String caption) {
        this.imageURL = imageURL;
        this.caption = caption;
    }
}