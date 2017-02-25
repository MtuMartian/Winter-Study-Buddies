package com.example.miles.winterwonderhack2017.Activities.CreatePostingPage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.miles.winterwonderhack2017.Activities.NavigationBar.BaseDrawerActivity;
import com.example.miles.winterwonderhack2017.R;

public class CreatePostingActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_posting);

        setupDrawer();
    }
}
