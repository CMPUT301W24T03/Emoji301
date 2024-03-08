package com.example.emojibrite;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;

public class Users implements Parcelable {




public class Users {

    private String HomePage;

    private String name;
    private String autoGenImage;

    private String uploadedImage;
    private String email;
    private String number;
    private String role;


    private String uid;


    // unique id? add getter and setter

    // set to false by default in constructors
    private boolean enableNotification;

    private boolean enableGeolocationTracking;
//    private boolean isAdmin;

    /**
     * Users constructor with name, email, link to ImagePath  which can be
     * generated picture or the picture from the gallery,
     * and number
     * @param name of the user
     * @param email of the user
     * @param ImagePath of the picture
     */


    public Users(String uid, String name, String email, String ImagePath, String number) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.HomePage = HomePage;
        this.ImagePath = ImagePath;
        this.number = number;
        this.enableNotification = false;
        this.enableGeolocationTracking = false;



    public Users(String uid){
        this.uid = uid;
        this.name = null;
        this.email = null;
        this.autoGenImage = null;
        this.uploadedImage = null;
        this.number = null;
        this.enableNotification = false;
        this.enableGeolocationTracking = false;

    }


    public String getUploadedImage() {
        return uploadedImage;
    }

    public void setUploadedImage(Bitmap uploadedImage) {
        if (uploadedImage == null){
            this.uploadedImage = null;
        }
        else {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            uploadedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            this.uploadedImage = encodedImage;
        }

    }
    public void setAutoGenImage(Bitmap autoGenImage) {
        if (autoGenImage == null) {
            this.autoGenImage = null;
        }
        else {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            autoGenImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            this.autoGenImage = encodedImage;
        }
    }
    public String getAutoGenImage() {
        return autoGenImage;
    }


    public String getProfileUid() {
        return uid;
    }


    //no setter yet

    /**
     * Gets the name of the user
     * @return name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user
     * @param name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email of the user
     * @return email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user
     * @param email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the HomePage of the user
     * @return HomePage of the user
     */
    public String getHomePage(){
        return HomePage;
    }


    /**
     * Gets the HomePage of the user
     * @param HomePage of the user
     */
    public void setHomePage(String HomePage){
        this.HomePage = HomePage;
    }


    /**
     * Gets the ImagePath of the user
     * @return ImagePath of the user
     */


    /**
     * Sets the ImagePath of the user
     * @param autoGenImage of the user
     */


    /**
     * Gets the number of the user
     * @return the number of the user
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the number of the user
     * @param number of the user
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Gets the enableNotification of the user
     * @return the enableNotification of the user
     */
    public boolean isEnableNotification() {
        return enableNotification;
    }

    /**
     * Sets the enableNotification of the user
     * @param enableNotification of the user
     */
    public void setEnableNotification(boolean enableNotification) {
        this.enableNotification = enableNotification;
    }

    /**
     * Gets the enableGeolocationTracking of the user
     * @return the enableGeolocationTracking of the user
     */
    public boolean isEnableGeolocationTracking() {

        return enableGeolocationTracking;
    }

    /**
     * Sets the enableGeolocationTracking of the user
     * @param enableGeolocationTracking of the user
     */
    public void setEnableGeolocationTracking(boolean enableGeolocationTracking) {
        this.enableGeolocationTracking = enableGeolocationTracking;
    }

    /**
     * Gets the role of the user
     * @param role of the user
     */
    public void setRole(String role) {

        this.role = role;
    }

    /**
     * Sets the role of the user
     * @return role of the user
     */
    public String getRole() {

        return role;
    }


    /*
    parcelable implementation
     */
    protected Users(Parcel in) {
        //deserialization of the object
        name = in.readString();
        autoGenImage = in.readString();
        uploadedImage = in.readString();
        email = in.readString();
        number = in.readString();
        role = in.readString();
        uid = in.readString();
        enableNotification = in.readByte() != 0;
        enableGeolocationTracking = in.readByte() != 0;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(autoGenImage);
        dest.writeString(uploadedImage);
        dest.writeString(email);
        dest.writeString(number);
        dest.writeString(role);
        dest.writeString(uid);
        dest.writeByte((byte) (enableNotification ? 1 : 0));
        dest.writeByte((byte) (enableGeolocationTracking ? 1 : 0));
    }
    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }
        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };
}
