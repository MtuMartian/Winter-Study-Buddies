package com.example.miles.winterwonderhack2017.Activities.DiscussionPage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miles.winterwonderhack2017.Activities.Data.Message;
import com.example.miles.winterwonderhack2017.Activities.Data.Posting;
import com.example.miles.winterwonderhack2017.Activities.HomePage.PostingListAdapter;
import com.example.miles.winterwonderhack2017.Activities.NavigationBar.BaseDrawerActivity;
import com.example.miles.winterwonderhack2017.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DiscussionActivity extends BaseDrawerActivity
{
    ListView messageListView;
    EditText messageEditText;
    MessageListAdapter adapter;
    ArrayList<Message> messages;
    String postID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        postID = getIntent().getStringExtra("id");

        setupDrawer();

        getData();

        messageEditText = (EditText) findViewById(R.id.message);


        messageListView = (ListView) findViewById(R.id.messageList);
        messages = new ArrayList();
        adapter = new MessageListAdapter(this, R.id.messageList, messages);
        messageListView.setAdapter(adapter);
    }

    private void getData()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://198.211.114.218:8787/api/view_discussion";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    messages = new ArrayList<Message>();
                    JSONArray jArray = new JSONArray(response);
                    for (int i = 0; i < jArray.length(); i++)
                    {
                        JSONObject jObj = (JSONObject) jArray.get(i);
                        String username = jObj.getString("username");
                        String comment = jObj.getString("comment");
                        String time = jObj.getString("time");
                        Message message = new Message(time, username, comment);
                        messages.add(message);
                    }
                    adapter = new MessageListAdapter(getApplicationContext(), R.id.messageList, messages);
                    messageListView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("GETTING RESPONSE FROM DISCUSSION!!!");
                System.out.println(response);
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {

                System.out.println("RESPONSE FAILED");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                String key = prefs.getString("user_auth", "");
                params.put("key", key);
                params.put("hpost", postID);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void sendMessage(View v)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://198.211.114.218:8787/api/new_comment";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            public void onResponse(String response) {
                System.out.println("GETTING RESPONSE FROM POSTING MESSAGE!!!");
                System.out.println(response);
                getData();
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {

                System.out.println("RESPONSE FAILED");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                String key = prefs.getString("user_auth", "");
                String comm = messageEditText.getText().toString();
                params.put("key", key);
                params.put("hpost", postID);
                params.put("comm", comm);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
