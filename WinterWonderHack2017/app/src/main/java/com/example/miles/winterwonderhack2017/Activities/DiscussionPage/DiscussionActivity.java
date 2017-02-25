package com.example.miles.winterwonderhack2017.Activities.DiscussionPage;

import android.os.Bundle;
import android.widget.ListView;

import com.example.miles.winterwonderhack2017.Activities.Models.Message;
import com.example.miles.winterwonderhack2017.Activities.NavigationBar.BaseDrawerActivity;
import com.example.miles.winterwonderhack2017.R;

import java.util.ArrayList;
import java.util.Date;

public class DiscussionActivity extends BaseDrawerActivity
{
    ListView messageListView;
    MessageListAdapter adapter;
    ArrayList<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        setupDrawer();

        messageListView = (ListView) findViewById(R.id.messageList);
        messages = new ArrayList();
        messages.add(new Message(new Date(), "Baron", "Hello people!"));
        messages.add(new Message(new Date(), "Miles", "Hi!"));
        messages.add(new Message(new Date(), "Baron", "What is up?"));
        adapter = new MessageListAdapter(this, R.id.messageList, messages);
        messageListView.setAdapter(adapter);
    }
}
