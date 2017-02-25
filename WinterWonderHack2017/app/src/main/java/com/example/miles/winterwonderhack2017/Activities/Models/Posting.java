package com.example.miles.winterwonderhack2017.Activities.Models;

/**
 * Created by miles on 2/25/17.
 */

public class Posting
{
    public Posting(String id, String title, String subject, String description, String posterId, String discussionId)
    {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.description = description;
        this.posterId = posterId;
        this.discussionId = discussionId;
    }

    public String id;
    public String title;
    public String subject;
    public String description;
    public String posterId;
    public String discussionId;
}
