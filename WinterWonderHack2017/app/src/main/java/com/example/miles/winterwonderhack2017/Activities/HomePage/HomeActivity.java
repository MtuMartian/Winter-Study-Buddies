package com.example.miles.winterwonderhack2017.Activities.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.miles.winterwonderhack2017.Activities.CreatePostingPage.CreatePostingActivity;
import com.example.miles.winterwonderhack2017.Activities.Models.Posting;
import com.example.miles.winterwonderhack2017.Activities.NavigationBar.BaseDrawerActivity;
import com.example.miles.winterwonderhack2017.Activities.PostingPage.PostingActivity;
import com.example.miles.winterwonderhack2017.R;

import java.util.ArrayList;

public class HomeActivity extends BaseDrawerActivity
{
    ListView postingListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupDrawer();

        postingListView = (ListView) findViewById(R.id.postingListView);

        ArrayList<Posting> data = new ArrayList<Posting>();

        data.add(new Posting("id", "This is a title", "CS 3425", "This is a description about the thing I want", "user", "id"));
        data.add(new Posting("id", "This class is hard", "CS 3331", "This is a description about the thing I want", "user", "id"));
        data.add(new Posting("id", "What is a variable?", "CS 1121", "This is a description about the thing I want", "user", "id"));

        ListAdapter postingListAdapter = new PostingListAdapter(this, R.layout.posting_item, data);

        postingListView.setAdapter(postingListAdapter);
        postingListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parentView, View childView, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), PostingActivity.class);
                startActivity(intent);
            }
        });
    }

    public void launchNeWPostingPage(View v)
    {
        Intent intent = new Intent(this, CreatePostingActivity.class);
        startActivity(intent);
    }
}
