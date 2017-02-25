package com.example.miles.winterwonderhack2017.Activities.HomePage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.miles.winterwonderhack2017.Activities.Models.Posting;
import com.example.miles.winterwonderhack2017.R;

import java.util.List;

/**
 * Created by miles on 2/25/17.
 */

public class PostingListAdapter extends ArrayAdapter<Posting>
{
    public PostingListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public PostingListAdapter(Context context, int resource, List<Posting> postings) {
        super(context, resource, postings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.posting_item, null);
        }

        Posting posting = getItem(position);

        if (posting != null)
        {
            TextView titleTV = (TextView) view.findViewById(R.id.title);
            TextView classTV = (TextView) view.findViewById(R.id.subject);
            TextView authorTV = (TextView) view.findViewById(R.id.author);

            titleTV.setText(posting.title);
            classTV.setText(posting.subject);
            authorTV.setText(posting.posterId);
        }

        return view;
    }
}
