package com.example.miles.winterwonderhack2017.Activities.ViewProfilePage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.miles.winterwonderhack2017.Activities.NavigationBar.BaseDrawerActivity;
import com.example.miles.winterwonderhack2017.R;

public class ProfileActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupDrawer();
    }
}
