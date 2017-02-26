package com.example.miles.winterwonderhack2017.Activities.HomePage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

public class HomeActivity extends BaseDrawerActivity {
    ListView postingListView;
    ListAdapter postingListAdapter;
    ArrayList<Posting> data;
    double lat;
    double lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        LocationManager locMan = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                getData(location);
                findViewById(R.id.spinner).setVisibility(View.INVISIBLE);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, (float) 0.1, locationListener );

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        setupDrawer();

        postingListView = (ListView) findViewById(R.id.postingListView);

        // ArrayList<Posting> data = new ArrayList<Posting>();

       /* data.add(new Posting("id", "This is a title", "CS 3425", "This is a description about the thing I want", "user", "id"));
        data.add(new Posting("id", "This class is hard", "CS 3331", "This is a description about the thing I want", "user", "id"));
        data.add(new Posting("id", "What is a variable?", "CS 1121", "This is a description about the thing I want", "user", "id")); */

       // getData();

        //postingListAdapter = new PostingListAdapter(this, R.layout.posting_item, data);

        // postingListView.setAdapter(postingListAdapter);
        postingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parentView, View childView, int position, long id) {
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

    public void launchNeWPostingPage(View v) {
        Intent intent = new Intent(this, CreatePostingActivity.class);
        startActivity(intent);
    }

    public void getData(final Location location) {

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
                    for (int i = 0; i < jArray.length(); i++) {
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

                params.put("xcoord", Double.toString(location.getLongitude()));
                params.put("ycoord", Double.toString(location.getLatitude()));

                System.out.println("x, y: " + location.getLongitude() + ", " + location.getLatitude());

                //Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_SHORT).show();

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
