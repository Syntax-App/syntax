package edu.brown.cs.student.main.server.types;

import com.google.cloud.firestore.annotation.PropertyName;

/**
 * This class represents a User with their data
 */
public class User {
    String uuid;
    String name;
    String email;
    String pic;
    UserStats stats;

    /**
     * User constructor
     */
    public User() {
    }

    /**
     * User constructor with specified uuid, name, email, picture, and statistics
     */
    public User(String uuid, String name, String email, String pic, UserStats stats) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.pic = pic;
        this.stats = stats;
    }

    /**
     * Accessor method to get a user's uuid
     * @return - string uuid of user
     */
    public String getUuid() {
        return this.uuid;
    }

    /**
     * Accessor method to get a user's name
     * @return - string name of user
     */
    public String getName() {
        return this.name;
    }

    /**
     * Accessor method to get a user's email
     * @return - string email of user
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Accessor method to get a user's profile picture
     * @return - string url of user's profile picture
     */
    public String getPic() {
        return this.pic;
    }

    /**
     * Accessor method to get a user's stats
     * @return - UserStats representing user statistics
     */
    @PropertyName("stats")
    public UserStats getStats() {
        return this.stats;
    }

    /**
     * Mutator method to change a user's stats
     * @param stats - UserStats
     */
    @PropertyName("stats")
    public void setStats(UserStats stats) {
        this.stats = stats;
    }

    /**
     * Mutator method to change a user's uuid
     * @param uuid - string uuid to set with
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
