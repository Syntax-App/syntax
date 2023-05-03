package edu.brown.cs.student.main.server.types;

public class UserTypes {

    public record User(String uuid, String name, String email, String pic, UserStats stats) {}
    public record UserStats(int highlpm, double highacc, int numraces, double avglpm, double avgacc, double exp) {}
    public record NewestStats(String email, int recentlpm, double recentacc){};

}
