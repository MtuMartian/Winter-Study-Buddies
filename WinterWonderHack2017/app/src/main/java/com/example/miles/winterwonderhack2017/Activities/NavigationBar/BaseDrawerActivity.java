package com.example.miles.winterwonderhack2017.Activities.NavigationBar;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.miles.winterwonderhack2017.Activities.CreatePostingPage.CreatePostingActivity;
import com.example.miles.winterwonderhack2017.Activities.HomePage.HomeActivity;
import com.example.miles.winterwonderhack2017.Activities.NavigationBar.DrawerListAdapter;
import com.example.miles.winterwonderhack2017.Activities.NavigationBar.NavigationItem;
import com.example.miles.winterwonderhack2017.Activities.ViewProfilePage.ProfileActivity;
import com.example.miles.winterwonderhack2017.R;

import java.util.ArrayList;

/**
 * Created by miles on 2/25/17.
 */

public class BaseDrawerActivity extends Activity {
    ListView drawerList;
    RelativeLayout drawerPane;
    protected ActionBarDrawerToggle drawerToggle;
    protected DrawerLayout drawerLayout;

    ArrayList<NavigationItem> navigationItems = new ArrayList<>();

    public void onPause()
    {
        super.onPause();
        drawerLayout.closeDrawer(drawerPane);
    }

    protected void setupDrawer()
    {
        navigationItems.add(new NavigationItem("NEARBY", "See posts near you!", R.drawable.ic_location));
        navigationItems.add(new NavigationItem("PROFILE", "View your profile!", R.drawable.ic_user_profile));
        navigationItems.add(new NavigationItem("NEW POSTING", "Create a new post!", R.drawable.ic_add_new));
        navigationItems.add(new NavigationItem("GROUPS", "View your groups!", R.drawable.ic_group));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        drawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        drawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, navigationItems);
        drawerList.setAdapter(adapter);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                selectItemFromDrawer(position);
            }
        });
    }

    protected void selectItemFromDrawer(int position)
    {
        Intent intent;
        switch (position){
            case (0):
                intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case (1):
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                finish();
                break;
            case (2):
                intent = new Intent(this, CreatePostingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(drawerPane);
    }

    public void openDrawer(View v)
    {
        drawerLayout.openDrawer(drawerPane);
    }
}
