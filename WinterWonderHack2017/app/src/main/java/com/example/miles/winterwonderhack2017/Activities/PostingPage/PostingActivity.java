package com.example.miles.winterwonderhack2017.Activities.PostingPage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.miles.winterwonderhack2017.Activities.DiscussionPage.DiscussionActivity;
import com.example.miles.winterwonderhack2017.Activities.GroupManagementPage.GroupManagementActivity;
import com.example.miles.winterwonderhack2017.Activities.NavigationBar.BaseDrawerActivity;
import com.example.miles.winterwonderhack2017.R;

import org.w3c.dom.Text;

public class PostingActivity extends BaseDrawerActivity
{
    TextView titleText;
    TextView descriptionText;
    TextView userText;
    TextView classText;
    String postID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        Intent intent = getIntent();
        postID = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String subject = intent.getStringExtra("subject");
        String description = intent.getStringExtra("description");
        intent.getStringExtra("posterId");
        String user = intent.getStringExtra("user");

        titleText = (TextView) findViewById(R.id.title);
        descriptionText = (TextView) findViewById(R.id.description);
        userText = (TextView) findViewById(R.id.userText);
        classText = (TextView) findViewById(R.id.classText);

        titleText.setText(title);
        classText.setText("Class: " + subject);
        userText.setText("Posted by: " + user);
        descriptionText.setText(description);

        setupDrawer();
    }

    public void buttonPressed(View v)
    {
        Intent intent = new Intent(this, DiscussionActivity.class);
        intent.putExtra("id", postID);
        startActivity(intent);
    }
}
