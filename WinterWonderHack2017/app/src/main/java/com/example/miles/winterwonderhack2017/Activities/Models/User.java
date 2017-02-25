package com.example.miles.winterwonderhack2017.Activities.Models;

/**
 * Created by miles on 2/25/17.
 */

public class User {
    public String id;
    public String name;
    public String email;
    public String major;
    public int year;
    public double credibility;
    public String[] workedWith;
    public String[] groups;
    public String[] classes;

    public User(String id, String name, String email, String major, int year, double credibility, String[] workedWith, String[] groups, String[] classes) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.major = major;
        this.year = year;
        this.credibility = credibility;
        this.workedWith = workedWith;
        this.groups = groups;
        this.classes = classes;
    }
}
