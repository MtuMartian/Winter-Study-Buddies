package com.example.miles.winterwonderhack2017.Activities.NavigationBar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miles.winterwonderhack2017.R;

import java.util.ArrayList;

/**
 * Created by miles on 2/25/17.
 */

public class DrawerListAdapter extends BaseAdapter
{
    Context context;
    ArrayList<NavigationItem> navigationItems;

    public DrawerListAdapter(Context context, ArrayList<NavigationItem> navigationItems)
    {
        this.context = context;
        this.navigationItems = navigationItems;
    }

    @Override
    public int getCount()
    {
        return navigationItems.size();
    }

    @Override
    public Object getItem(int position)
    {
        return navigationItems.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_item, null);
        }
        else
        {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);

        titleView.setText(navigationItems.get(position).title);
        subtitleView.setText(navigationItems.get(position).subtitle);
        imageView.setImageResource(navigationItems.get(position).icon);

        return view;
    }
}
