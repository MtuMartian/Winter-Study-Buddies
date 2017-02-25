package com.example.miles.winterwonderhack2017.Activities.EditProfilePage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.miles.winterwonderhack2017.Activities.NavigationBar.BaseDrawerActivity;
import com.example.miles.winterwonderhack2017.R;

public class EditProfileActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setupDrawer();
    }
}
