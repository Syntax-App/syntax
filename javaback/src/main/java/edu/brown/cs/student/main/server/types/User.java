package edu.brown.cs.student.main.server.types;

import com.google.cloud.firestore.annotation.PropertyName;

public class User {
    String uuid;
    String name;
    String email;
    String pic;
    UserStats stats;

    public User() {
    }

    public User(String uuid, String name, String email, String pic, UserStats stats) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.pic = pic;
        this.stats = stats;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPic() {
        return this.pic;
    }

    @PropertyName("stats")
    public UserStats getStats() {
        return this.stats;
    }

    @PropertyName("stats")
    public void setStats(UserStats stats) {
        this.stats = stats;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
