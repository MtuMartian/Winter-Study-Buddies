package com.example.miles.winterwonderhack2017.Activities.CreatePostingPage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_posting);

        setupDrawer();

        titleField = (EditText) findViewById(R.id.titleField);
        classField = (EditText) findViewById(R.id.classField);
        descriptionField = (EditText) findViewById(R.id.descriptionField);
    }

    public void post(View v)
    {
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
                params.put("xcoord", "69");
                params.put("ycoord", "69");

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
