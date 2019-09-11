package com.owulanii.androidlogin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prefs.UserInfo;
import prefs.UserSession;

public class SignUp extends AppCompatActivity {
    private String TAG = SignUp.class.getSimpleName();
    private EditText username, email, password;
    private Button signup;
    private ProgressDialog progressDialog;
    private UserSession session;
    private UserInfo userInfo;
    public static final String mypreference = "RESPONDER";
    SharedPreferences sharedpreferences;
    Spinner institution;
    ArrayList<String> institutionArray= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username        = (EditText)findViewById(R.id.username);
        email           = (EditText)findViewById(R.id.email);
        password        = (EditText)findViewById(R.id.password);
        signup          = (Button)findViewById(R.id.signup);
        progressDialog  = new ProgressDialog(this);
        session         = new UserSession(this);
        userInfo        = new UserInfo(this);
        institution= (Spinner) findViewById(R.id.institution);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Choose institution");
        categories.add("hospital");
        categories.add("Bank");
        categories.add("police");
        categories.add("Administrative");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        institution.setAdapter(dataAdapter);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName = username.getText().toString().trim();
                String mail  = email.getText().toString().trim();
                String pass  = password.getText().toString().trim();
                String inst  = institution.getSelectedItem().toString();

                if (inst.equals("Choose institution")){
                    return;
                }

                signup(uName, mail, pass, inst);
            }
        });
    }

    private void signup(final String username, final String email, final String password, final String inst){
        // Tag used to cancel the request
        String tag_string_req = "req_signup";
        progressDialog.setMessage("Signing up...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Utils.REGISTER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONObject user = jObj.getJSONObject("user");
                        int id = user.getInt("id");
                        String uName = user.getString("username");
                        String email = user.getString("email");

                        // Inserting row in users table
                        userInfo.setEmail(email);
                        userInfo.setUsername(uName);
                        session.setLoggedin(true);

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt("id", id);
                        editor.apply();

                        startActivity(new Intent(SignUp.this, MainActivity.class));
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        toast(errorMsg);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    toast("Json error: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                toast("Unknown Error occurred");
                progressDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("institution", inst);

                return params;
            }

        };

        // Adding request to request queue
        AndroidLoginController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void toast(String x){
        Toast.makeText(this, x, Toast.LENGTH_SHORT).show();
    }
}
