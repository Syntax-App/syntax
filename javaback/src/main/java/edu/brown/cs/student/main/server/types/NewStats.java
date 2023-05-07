package edu.brown.cs.student.main.server.types;

/**
 * This record stores the stats of the most recent completed race of the user
 */
public record NewStats(String email, int recentlpm, double recentacc){}

