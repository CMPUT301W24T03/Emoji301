package com.example.emojibrite;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

/**
 * Users class to store the user's information
 */
public class Users implements Parcelable{
    // attributes
    private String homePage;
    private String name;
    private String autoGenImageUri;
    private String uploadedImageUri;
    private String email;
    private String number;
    private String role;
    private String profileUid;

    // set to false by default in constructors
    private boolean enableNotification;
    private boolean enableGeolocationTracking;

    private boolean enableAdmin;
    private String imagePath;

    // notification token
    private String fcmToken;

    public Users() {}


    /**
     * Users constructor with uid
     *
     * @param profileUid unique id of the user
     */
    public Users(String profileUid) {
        this.profileUid = profileUid;
        this.name = null;
        this.email = null;
        this.autoGenImageUri = null;
        this.uploadedImageUri = null;
        this.number = null;
        this.enableNotification = false;
        this.enableGeolocationTracking = false;
        this.homePage = null;
        this.fcmToken = null;

    }

    public void setEnableAdmin(boolean enableAdmin) {
        this.enableAdmin = enableAdmin;
    }
    public boolean getEnableAdmin() {
        return enableAdmin;
    }
    public void setProfileUid(String profileUid) {
        this.profileUid = profileUid;
    }

    /**
     * gets the uploaded image
     * @return uploadedImage : the image uploaded by the user
     */
    public String getUploadedImageUri() {
        return uploadedImageUri;
    }

    /**
     * sets the uploaded image
     * @param uploadedImageUri : the image uploaded by the user
     */
    public void setUploadedImageUri(String uploadedImageUri) {
        this.uploadedImageUri = uploadedImageUri;

    }

    public String getHomePage() {
        return homePage;
    }
    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }
    /**
     * gets the auto generated image
     * @param autoGenImageUri : the image generated by the app
     */
    public void setAutoGenImageUri(String autoGenImageUri) {
        this.autoGenImageUri = autoGenImageUri;

    }

    /**
     * gets the auto generated image
     * @return autoGenImage : the image generated by the app
     */
    public String getAutoGenImageUri() {
        return autoGenImageUri;
    }

    /**
     * gets the user's unique id
     * @return uid : the unique id of the user
     */
    public String getProfileUid() {
        return profileUid;
    }

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
     * Gets the ImagePath of the user
     * @return ImagePath of the user
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Sets the ImagePath of the user
     * @param autoGenImage of the user
     */
    public void setImagePath(String autoGenImage) {
        this.imagePath = autoGenImage;
    }

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
    public boolean getEnableNotification() {
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
    public boolean getEnableGeolocation(){

        return enableGeolocationTracking;
    }

    /**
     * Sets the enableGeolocationTracking of the user
     * @param enableGeolocationTracking of the user
     */
    public void setEnableGeolocation(boolean enableGeolocationTracking) {
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


    /**
     * Gets the FCM token
     * @return a string of the FCM token
     */
    public String getFcmToken() {
        return fcmToken;
    }

    /**
     * Sets the FCM token
     * @param fcmToken a string of the FCM token
     */
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    /**
     * Parcelable constructor for the Users class
     * @param in
     */
    protected Users(Parcel in) {
        //deserialization of the object
        name = in.readString();
        autoGenImageUri = in.readString();
        uploadedImageUri = in.readString();
        email = in.readString();
        number = in.readString();
        role = in.readString();
        profileUid = in.readString();
        homePage = in.readString();
        enableNotification = in.readByte() != 0;
        enableGeolocationTracking = in.readByte() != 0;
        enableAdmin = in.readByte() != 0;
        fcmToken = in.readString();

    }
    /**
     * Parcelable creator for the Users class
     */
    @Override
    public int describeContents() {
        return 0;
    }
    /**
     * Parcelable creator for the Users class
     */
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(autoGenImageUri);
        dest.writeString(uploadedImageUri);
        dest.writeString(email);
        dest.writeString(number);
        dest.writeString(role);
        dest.writeString(profileUid);
        dest.writeString(homePage);
        dest.writeByte((byte) (enableNotification ? 1 : 0));
        dest.writeByte((byte) (enableGeolocationTracking ? 1 : 0));
        dest.writeByte((byte) (enableAdmin ? 1 : 0));
        dest.writeString(fcmToken);
    }

    /**
     * Parcelable creator for the Users class
     */
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