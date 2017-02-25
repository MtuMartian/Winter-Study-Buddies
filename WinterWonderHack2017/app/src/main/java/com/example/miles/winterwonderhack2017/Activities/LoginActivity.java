package com.example.miles.winterwonderhack2017.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miles.winterwonderhack2017.Activities.HomePage.HomeActivity;
import com.example.miles.winterwonderhack2017.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class LoginActivity extends Activity
{
    private EditText usernameField;
    private EditText passwordField;
    private TextView errorMessageField;

    private Map<String, String> tempUserValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences settings = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        if (settings.contains("user_auth"))
        {
            String userAuth = settings.getString("user_auth", null);
            System.out.println("USER AUTH: " + userAuth);

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://198.211.114.218:8787/api/validate_key";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
            {
                public void onResponse(String res)
                {
                    if (Integer.parseInt(res) == 0)
                    {
                        // Create a loading indicator and deactivate it here
                        return;
                    }
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
                        SharedPreferences settings = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                        String auth = settings.getString("user_auth", "");
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("key", auth);

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

        usernameField = (EditText) findViewById(R.id.userNameField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        errorMessageField = (TextView) findViewById(R.id.errorMessage);

        tempUserValidation = new HashMap<>();
        tempUserValidation.put("miles", "Password");
        tempUserValidation.put("baron", "LongerPassword");

    }

    public void loginButtonPressed(View v)
    {
        String user = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://198.211.114.218:8787/api/login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            public void onResponse(String response)
            {
                SharedPreferences settings = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("user_auth", response);
                editor.commit();
                Intent homePageLauncher = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(homePageLauncher);
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {

                System.out.println(error.getLocalizedMessage());
                System.out.println(error.getMessage());
            }
        })
        {
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", usernameField.getText().toString());
                params.put("password", passwordField.getText().toString());

                return params;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(stringRequest);

        //params username password, returns key
        //new ValidateLoginTask().execute(user, password);
    }

    public void registerButtonPressed(View v)
    {
        Intent intent = new Intent(this, RegisterUserActivity.class);
        startActivity(intent);
    }

    private class ValidateLoginTask extends AsyncTask<String, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(String... params) {
            /*try {
                Thread.sleep(3000);
                if (!tempUserValidation.containsKey(params[0].toLowerCase()))
                    return false;
                if (tempUserValidation.get(params[0].toLowerCase()).equals(params[1]))
                    return true;
                return false;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }*/
            return true;
        }

        protected void onPostExecute(Boolean result)
        {
            if (result)
            {
                Intent homePageLauncher = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(homePageLauncher);
            }
            else
            {
                passwordField.setText("");
                errorMessageField.setVisibility(View.VISIBLE);
            }
        }
    }
}
