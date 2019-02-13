package com.example.suchamillennial.androidweatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // Declare references to Widgets

    EditText cityName;
    
    // Get the API Key from the Key file
    static final String APP_KEY = Keys.APP_KEY;

    // The findWeather method is called by the View type Button upon and onClick event
    public void findWeather(View view){
        String cityString = cityName.getText().toString();
        Log.i("cityName", cityString);
        DownloadTask task = new DownloadTask();
        // task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + cityString);
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+cityString+"&appid="+APP_KEY);
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create references to the UI Widgets for use in code
        cityName = (EditText)findViewById(R.id.cityName);

        // Comment out the floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    // The Async task method that will be used to make the call to the API
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            // Try catch block incase the URL or connection is invalid

            try {
                // Make a URL object from the first string parameter
                url = new URL(urls[0]);

                // Create a connection by opening one to the URL that was just created
                urlConnection = (HttpURLConnection) url.openConnection();

                // Create an InputStream from the URL connection
                InputStream in = urlConnection.getInputStream();

                // Create an InputStreamReader object to read the created InputStream
                InputStreamReader reader = new InputStreamReader(in);

                // Start reading the data from the InputStream while it still exists
                int data = reader.read();
                while(data != -1){
                    // Cast the incoming data as a char and add it to the result string
                    char current = (char) data;
                    result += current;

                    // Get the next bit of data from the InputStream
                    data = reader.read();
                }

                // Return the result string from the API call
                Log.i("result",result);
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        // This is a method that will run in the background after the AsyncTask completes. It does NOT effect the UI. This will parse the JSON response
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result!=null){
                try {
                    // Create a JSON Object from the API call result String. Needs try catch in case of invalid JSON
                    JSONObject jsonObject = new JSONObject(result);

                    // Get the info inside the weather section of the JSONObject
                    String weatherInfo = jsonObject.getString("weather");

                    Log.i("Weather Content", weatherInfo);

                    JSONArray arr = new JSONArray(weatherInfo);

                    // Loop through array to get all elements of the weather info and turn into JSON Objects
                    for(int i =0; i <arr.length();i++){

                        JSONObject jsonPart = arr.getJSONObject(i);

                        // Log the information from each section of the weather
                        Log.i("main", jsonPart.getString("main"));
                        Log.i("description", jsonPart.getString("description"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Log.i("Null","NULL");
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
