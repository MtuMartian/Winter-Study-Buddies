package com.example.miles.winterwonderhack2017.Activities.Models;

import java.util.Date;

/**
 * Created by miles on 2/25/17.
 */

public class Message
{
    public Date time;
    public String poster;
    public String text;

    public Message(Date time, String poster, String text) {
        this.time = time;
        this.poster = poster;
        this.text = text;
    }
}
