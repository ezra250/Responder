package com.owulanii.androidlogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import prefs.UserInfo;
import prefs.UserSession;

public class MainActivity extends AppCompatActivity {

    private Button logout;
    private TextView tvUsername, tvEmail;
    private UserInfo userInfo;
    private UserSession userSession;
    public static final String mypreference = "RESPONDER";
    SharedPreferences sharedpreferences;
    JSONObject data;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInfo        = new UserInfo(this);
        userSession     = new UserSession(this);
        logout          = (Button)findViewById(R.id.logout);
        tvUsername      = (TextView)findViewById(R.id.key_username);
        tvEmail         = (TextView)findViewById(R.id.key_email);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        Log.d("ID###", sharedpreferences.getInt("id", 0)+"");
        Log.d("didier", sharedpreferences.getString("token", ""));

        if (sharedpreferences.contains("token") && !sharedpreferences.getString("token", "").isEmpty()){
            sendRegistrationToServer(sharedpreferences.getString("token", ""));
        }

        if(!userSession.isUserLoggedin()){
            startActivity(new Intent(this, Login.class));
            finish();
        }

        String username = userInfo.getKeyUsername();
        String email    = userInfo.getKeyEmail();

        tvUsername.setText(username);
        tvEmail.setText(email);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSession.setLoggedin(false);
                userInfo.clearUserInfo();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }
        });


    }

    private void sendRegistrationToServer(String token) {
        try {
            data = new JSONObject();
            data.put("token", token);
            data.put("id", sharedpreferences.getInt("id", 0));

            String URL = Utils.SAVETOKEN_URL;

            SaveToken saveToken = new SaveToken();
            saveToken.execute(URL);
            Log.d("didierData", String.valueOf(data));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class SaveToken extends AsyncTask<String, Void, String> {
        private Exception exception = null;

        @Override
        protected void onPreExecute() {
            // put ui changes here
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String JsonDATA = data.toString();
            String JsonResponse = null;

            try {

                Log.e(TAG, "Started Connecting to " + params[0]);
                url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");

                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
                writer.close();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = buffer.toString();


                Log.i(TAG, JsonResponse);
                return JsonResponse;

            } catch (IOException e) {
                exception = e;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if(!(exception == null)){
                Toast.makeText(getApplicationContext(),"Something went wrong , Try again! ", Toast.LENGTH_LONG).show();

                Log.e(TAG, "Exception at login " + exception.getMessage());
            }else{
                //success
            }

        }

    }

}
