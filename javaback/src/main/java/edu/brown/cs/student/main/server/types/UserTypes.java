package edu.brown.cs.student.main.server.types;

public class UserTypes {

    public record User(String uuid, String name, String email, String pic, UserStats stats) {}
    public record UserStats(int highlpm, float highacc, int numraces, float avglpm, float avgacc, float exp) {}

}
