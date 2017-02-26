package com.example.miles.winterwonderhack2017.Activities;

import android.content.Context;
import android.content.Intent;
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
import com.example.miles.winterwonderhack2017.Activities.HomePage.HomeActivity;
import com.example.miles.winterwonderhack2017.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterUserActivity extends AppCompatActivity
{
    private EditText userNameField;
    private EditText emailField;
    private EditText passwordField;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        userNameField = (EditText) findViewById(R.id.registerUserName);
        emailField = (EditText) findViewById(R.id.registerEmail);
        passwordField = (EditText) findViewById(R.id.registerPassword);
    }

    public void register(View v)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://198.211.114.218:8787/api/create_user";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            public void onResponse(String response)
            {
                SharedPreferences settings = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("user_auth", response);
                editor.putString("user_name", userNameField.getText().toString());
                editor.commit();
                System.out.println("COMMITED");
                System.out.println("RETREIVING!!!: " + settings.getString("user_auth", ""));
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
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
                username = userNameField.getText().toString();
                params.put("username", userNameField.getText().toString());
                params.put("email", emailField.getText().toString());
                params.put("password", passwordField.getText().toString());

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
