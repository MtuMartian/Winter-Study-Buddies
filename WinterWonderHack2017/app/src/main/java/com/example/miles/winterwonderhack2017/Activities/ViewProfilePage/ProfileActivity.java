package com.example.miles.winterwonderhack2017.Activities.ViewProfilePage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miles.winterwonderhack2017.Activities.Data.Misc.YearParser;
import com.example.miles.winterwonderhack2017.Activities.HomePage.HomeActivity;
import com.example.miles.winterwonderhack2017.Activities.NavigationBar.BaseDrawerActivity;
import com.example.miles.winterwonderhack2017.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends BaseDrawerActivity
{

    TextView nameText;
    TextView emailText;
    TextView majorText;
    TextView yearText;
    TextView classesText;
    TextView groupsText;
    TextView postingsText;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupDrawer();

        SharedPreferences settings = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);


        username = settings.getString("user_name", "");

        nameText = (TextView) findViewById(R.id.nameText);
        emailText = (TextView) findViewById(R.id.emailText);
        majorText = (TextView) findViewById(R.id.majorText);
        yearText = (TextView) findViewById(R.id.yearText);
        classesText = (TextView) findViewById(R.id.classesText);
        groupsText = (TextView) findViewById(R.id.groupsText);
        postingsText = (TextView) findViewById(R.id.postingsText);

        getData();
    }

    private void getData()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://198.211.114.218:8787/api/user_info";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            public void onResponse(String res)
            {
                try {
                    System.out.println(res);
                    System.out.println("REFORMATTING JSON");
                    res = res.replaceAll("\"\\[", "\\[");
                    res = res.replaceAll("\\]\"", "\\]");
                    JSONObject jObj = new JSONObject(res);
                    String user = jObj.getString("username");
                    String major = jObj.getString("major");
                    double credibility = jObj.getDouble("credibility");
                    JSONArray helpPosts = jObj.getJSONArray("helpposts");
                    JSONArray classes = jObj.getJSONArray("classes");
                    JSONArray groups = jObj.getJSONArray("groups");
                    int year = jObj.getInt("year");
                    String email = jObj.getString("email");
                    JSONArray workedWith = jObj.getJSONArray("workedwith");

                    String classString = "";
                    for (int i = 0; i < classes.length() - 1; i++)
                    {
                        classString += classes.getString(i) + ", ";
                    }
                    if (classes.length() >= 1)
                        classString += classes.getString(classes.length() - 1);

                    String groupsString = "";
                    for (int i = 0; i < groups.length() - 1; i++)
                    {
                        groupsString += groups.getString(i) + ", ";
                    }
                    if (groups.length() >= 1)
                        groupsString += groups.getString(groups.length() - 1);

                    nameText.setText(user);
                    if (major.equals(""))
                        major = "Unknown";
                    majorText.setText("Major: " + major);
                    if (classes.length() == 0)
                        classString = "Unknown";
                    classesText.setText("Classes: " + classString);
                    if (groups.length() == 0)
                        groupsString = "Unknown";
                    groupsText.setText("Groups: " + groupsString);
                    yearText.setText("Class standing: " + YearParser.YearToString(year));
                    if (email.equals(""))
                        email = "Unknown";
                    emailText.setText("Email: " + email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("RESULT:\n" + res);
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
                params.put("username", username);

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
