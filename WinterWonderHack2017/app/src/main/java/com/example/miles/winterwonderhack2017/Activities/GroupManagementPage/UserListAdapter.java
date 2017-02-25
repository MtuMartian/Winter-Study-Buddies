package com.example.miles.winterwonderhack2017.Activities.GroupManagementPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.miles.winterwonderhack2017.Activities.Models.Posting;
import com.example.miles.winterwonderhack2017.Activities.Models.User;
import com.example.miles.winterwonderhack2017.R;

import java.util.List;

/**
 * Created by miles on 2/25/17.
 */

public class UserListAdapter extends ArrayAdapter<User> {

    public UserListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public UserListAdapter(Context context, int resource, List<User> data) {
        super(context, resource, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.user_item, null);
        }

        User user = getItem(position);

        if (user != null)
        {
            TextView userNameTV = (TextView) view.findViewById(R.id.userName);
            userNameTV.setText(user.name);
        }

        return view;
    }
}
