package com.example.emojibrite;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Users implements Parcelable {
    private String name;
    private Bitmap autoGenImage;

    private Bitmap uploadedImage;
    private String email;
    private String number;
    private String role;


    private String uid;


    // unique id? add getter and setter

    // set to false by default in constructors
    private boolean enableNotification;

    private boolean enableGeolocationTracking;
//    private boolean isAdmin;


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


    public Bitmap getUploadedImage() {
        return uploadedImage;
    }

    public void setUploadedImage(Bitmap uploadedImage) {
        this.uploadedImage = uploadedImage;

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
     * Gets the ImagePath of the user
     * @return ImagePath of the user
     */
    public Bitmap getAutoGenImage() {
        return autoGenImage;
    }

    /**
     * Sets the ImagePath of the user
     * @param autoGenImage of the user
     */
    public void setAutoGenImage(Bitmap autoGenImage) {
        this.autoGenImage = autoGenImage;
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
        autoGenImage = in.readParcelable(Bitmap.class.getClassLoader());
        uploadedImage = in.readParcelable(Bitmap.class.getClassLoader());
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
        dest.writeParcelable(autoGenImage, flags);
        dest.writeParcelable(uploadedImage, flags);
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
