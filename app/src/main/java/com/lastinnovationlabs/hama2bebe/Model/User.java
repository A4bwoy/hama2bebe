package com.lastinnovationlabs.hama2bebe.Model;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String search;
    private String userPicture;
    private String profileUpdate;
    private String ApplicationTime;

    public User() {
    }

    public User(String id, String firstName, String lastName,
                String search, String userPicture, String profileUpdate,
                String applicationTime) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.search = search;
        this.userPicture = userPicture;
        this.profileUpdate = profileUpdate;
        ApplicationTime = applicationTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public String getProfileUpdate() {
        return profileUpdate;
    }

    public void setProfileUpdate(String profileUpdate) {
        this.profileUpdate = profileUpdate;
    }

    public String getApplicationTime() {
        return ApplicationTime;
    }

    public void setApplicationTime(String applicationTime) {
        ApplicationTime = applicationTime;
    }
}
