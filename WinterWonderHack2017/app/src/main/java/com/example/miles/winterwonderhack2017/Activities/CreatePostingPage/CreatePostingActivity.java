package com.example.miles.winterwonderhack2017.Activities.CreatePostingPage;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miles.winterwonderhack2017.Activities.NavigationBar.BaseDrawerActivity;
import com.example.miles.winterwonderhack2017.R;

import java.util.HashMap;
import java.util.Map;

public class CreatePostingActivity extends BaseDrawerActivity {

    EditText titleField;
    EditText classField;
    EditText descriptionField;
    Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_posting);

        LocationManager locMan = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                loc = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

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

        titleField = (EditText) findViewById(R.id.titleField);
        classField = (EditText) findViewById(R.id.classField);
        descriptionField = (EditText) findViewById(R.id.descriptionField);
    }

    public void post(View v)
    {
        if (loc == null)
            return;
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://198.211.114.218:8787/api/post_help";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            public void onResponse(String response)
            {
                System.out.println(response);
                finish();
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {

                System.out.println("RESPONSE FAILED");
                Toast.makeText(getApplicationContext(), "Post Failed", Toast.LENGTH_SHORT).show();
                finish();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                String key = prefs.getString("user_auth", "");
                params.put("key", key);
                params.put("title", titleField.getText().toString());
                params.put("desp", descriptionField.getText().toString());
                params.put("class", classField.getText().toString());
                params.put("xcoord", Double.toString(loc.getLongitude()));
                params.put("ycoord", Double.toString(loc.getLatitude()));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
