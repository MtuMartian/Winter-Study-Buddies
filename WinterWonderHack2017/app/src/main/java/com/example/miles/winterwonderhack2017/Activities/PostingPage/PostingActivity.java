package com.example.miles.winterwonderhack2017.Activities.PostingPage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.miles.winterwonderhack2017.Activities.DiscussionPage.DiscussionActivity;
import com.example.miles.winterwonderhack2017.Activities.GroupManagementPage.GroupManagementActivity;
import com.example.miles.winterwonderhack2017.Activities.NavigationBar.BaseDrawerActivity;
import com.example.miles.winterwonderhack2017.R;

public class PostingActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        setupDrawer();
    }

    public void buttonPressed(View v)
    {
        Intent intent = new Intent(this, DiscussionActivity.class);
        startActivity(intent);
    }
}
