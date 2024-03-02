package com.example.emojibrite;

public class Profile {
    private String name;
    private String ImagePath;
    private String email;
    private String number;
//    private String role;

    // set to false by default in constructors
    private boolean enableNotification;
    private boolean enableGeolocationTracking;
//    private boolean isAdmin;

    /**
     * Profile constructor with name, email, link to ImagePath  which can be
     * generated picture or the picture from the gallery,
     * and number
     * @param name of the user
     * @param email of the user
     * @param ImagePath of the picture
     */
    public Profile(String name, String email, String ImagePath, String number) {
        this.name = name;
        this.email = email;
        this.ImagePath = ImagePath;
        this.number = number;
        this.enableNotification = false;
        this.enableGeolocationTracking = false;

    }

    /**
     * Profile constructor with name and the link to ImagePath which cam be
     * generated picture or the picture from the gallery
     * @param name of the user
     * @param ImagePath of the picture
     */
    public Profile(String name, String ImagePath) {
        this.name = name;
        this.ImagePath = ImagePath;
        this.enableNotification = false;
        this.enableGeolocationTracking = false;
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
        return ImagePath;
    }

    /**
     * Sets the ImagePath of the user
     * @param imagePath of the user
     */
    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
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
}
