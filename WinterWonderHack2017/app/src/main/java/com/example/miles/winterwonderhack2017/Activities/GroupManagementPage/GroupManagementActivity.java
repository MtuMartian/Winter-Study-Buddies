package com.example.miles.winterwonderhack2017.Activities.GroupManagementPage;

import android.os.Bundle;
import android.widget.ListView;

import com.example.miles.winterwonderhack2017.Activities.Data.User;
import com.example.miles.winterwonderhack2017.Activities.NavigationBar.BaseDrawerActivity;
import com.example.miles.winterwonderhack2017.R;

import java.util.ArrayList;

public class GroupManagementActivity extends BaseDrawerActivity
{
    ListView userListView;
    ArrayList<User> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_management);

        userListView = (ListView) findViewById(R.id.userListView);

        setupDrawer();

        data = new ArrayList<User>();
        data.add(new User("id", "John", "email", "Math", 3, 3.5, null, null, null));
        data.add(new User("id", "Mike", "email", "Math", 3, 3.5, null, null, null));
        data.add(new User("id", "Jess", "email", "Math", 3, 3.5, null, null, null));
        data.add(new User("id", "Anne", "email", "Math", 3, 3.5, null, null, null));
        data.add(new User("id", "Jake", "email", "Math", 3, 3.5, null, null, null));

        UserListAdapter adapter = new UserListAdapter(this, R.layout.user_item, data);
        userListView.setAdapter(adapter);
    }
}
