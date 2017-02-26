package com.example.miles.winterwonderhack2017.Activities.DiscussionPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.miles.winterwonderhack2017.Activities.Data.Message;
import com.example.miles.winterwonderhack2017.R;

import java.util.List;

/**
 * Created by miles on 2/25/17.
 */

public class MessageListAdapter extends ArrayAdapter<Message>
{
    public MessageListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public MessageListAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.message_item, null);
        }

        Message message = getItem(position);

        if (message != null)
        {
            TextView messageTV = (TextView) view.findViewById(R.id.message);
            TextView userTV = (TextView) view.findViewById(R.id.author);
            TextView dateTV = (TextView) view.findViewById(R.id.date);
            messageTV.setText(message.text);
            userTV.setText(message.poster);
            dateTV.setText(message.time.toString());
        }

        return view;
    }
}
