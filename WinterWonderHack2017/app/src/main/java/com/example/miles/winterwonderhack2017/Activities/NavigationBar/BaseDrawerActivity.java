package com.example.miles.winterwonderhack2017.Activities.NavigationBar;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.miles.winterwonderhack2017.Activities.NavigationBar.DrawerListAdapter;
import com.example.miles.winterwonderhack2017.Activities.NavigationBar.NavigationItem;
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

    protected void setupDrawer()
    {
        navigationItems.add(new NavigationItem("HOME", "Sub title", R.drawable.ic_user_profile));

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
        drawerLayout.closeDrawer(drawerPane);
    }

    public void openDrawer(View v)
    {
        drawerLayout.openDrawer(drawerPane);
    }
}
