package com.example.miles.winterwonderhack2017.Activities.HomePage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miles.winterwonderhack2017.Activities.CreatePostingPage.CreatePostingActivity;
import com.example.miles.winterwonderhack2017.Activities.Data.Posting;
import com.example.miles.winterwonderhack2017.Activities.NavigationBar.BaseDrawerActivity;
import com.example.miles.winterwonderhack2017.Activities.PostingPage.PostingActivity;
import com.example.miles.winterwonderhack2017.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseDrawerActivity
{
    ListView postingListView;
    ListAdapter postingListAdapter;
    ArrayList<Posting> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupDrawer();

        postingListView = (ListView) findViewById(R.id.postingListView);

       // ArrayList<Posting> data = new ArrayList<Posting>();

       /* data.add(new Posting("id", "This is a title", "CS 3425", "This is a description about the thing I want", "user", "id"));
        data.add(new Posting("id", "This class is hard", "CS 3331", "This is a description about the thing I want", "user", "id"));
        data.add(new Posting("id", "What is a variable?", "CS 1121", "This is a description about the thing I want", "user", "id")); */

        getData();

        //postingListAdapter = new PostingListAdapter(this, R.layout.posting_item, data);

       // postingListView.setAdapter(postingListAdapter);
        postingListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parentView, View childView, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), PostingActivity.class);
                Posting dataElement = data.get(position);
                intent.putExtra("id", dataElement.id);
                intent.putExtra("title", dataElement.title);
                intent.putExtra("subject", dataElement.subject);
                intent.putExtra("description", dataElement.description);
                intent.putExtra("posterId", dataElement.posterId);
                intent.putExtra("user", dataElement.user);
                startActivity(intent);
            }
        });
    }

    public void launchNeWPostingPage(View v)
    {
        Intent intent = new Intent(this, CreatePostingActivity.class);
        startActivity(intent);
    }

    public void getData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://198.211.114.218:8787/api/get_nearby";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            public void onResponse(String response) {

                System.out.println(response);
                response = response.replaceAll("'\\{", "\\{").replaceAll("\\}'", "\\}");
                System.out.println("Formatted: " + response);
                try {
                    JSONArray jArray = new JSONArray(response);
                    data = new ArrayList<Posting>();
                    for (int i = 0; i < jArray.length(); i++)
                    {
                        JSONObject jObj = (JSONObject) jArray.get(i);
                        String userName = jObj.getString("username");
                        String description = jObj.getString("desp");
                        String className = jObj.getString("class");
                        String title = jObj.getString("title");
                        String id = jObj.getString("pid");

                        data.add(new Posting(id, title, className, description, id, userName));
                    }

                    postingListAdapter = new PostingListAdapter(getApplicationContext(), R.layout.posting_item, data);

                    postingListView.setAdapter(postingListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                params.put("xcoord", "69");
                params.put("ycoord", "69");

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
