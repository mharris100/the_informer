package com.example.mharris.theinformer;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class RepPage extends Activity
{


    private FusedLocationProviderClient mFusedLocationClient;
    private EditText zip;

    public JSONArray repsGlobal;
    public String zipGlobal;
    public String stateGlobal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_page);


        //is this
        String type =  getIntent().getStringExtra("type");


        if (type.equals("zip")) {
            EditText zip = findViewById(R.id.zip_code_button);


            Integer zip_code = Integer.parseInt(getIntent().getStringExtra("zip"));

            zipCode(zip_code);

        } else if (type.equals("current location")) {

            currentLocation();
        } else {
            randomLoc();
        }


        Button current_location_button = findViewById(R.id.current_location_button2);

        //Assign a listener to your button
        current_location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start your second activity
//                zip = true;
                Intent intent = new Intent(RepPage.this, RepPage.class);
                intent.putExtra("type", "current location");
                startActivity(intent);
            }
        });


        zip = findViewById(R.id.new_zip_code);

        //helped from https://stackoverflow.com/questions/1489852/android-handle-enter-in-an-edittext
        final TextView.OnEditorActionListener enterListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {

                    Intent intent = new Intent(RepPage.this, RepPage.class);
                    intent.putExtra("type", "zip");
                    intent.putExtra("zip", zip.getText().toString());
                    startActivity(intent);

                }

                return true;
            }

        };

        zip.setOnEditorActionListener(enterListener);



        Button random_button = findViewById(R.id.random);

        //Assign a listener to your button
        random_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start your second activity
//                zip = true;
                Intent intent = new Intent(RepPage.this, RepPage.class);
                try {
                    JSONObject r1 = repsGlobal.getJSONObject(0);
                    intent.putExtra("type", "random");
                } catch (JSONException | IndexOutOfBoundsException e ) {
                    Log.d("JSONException", e.toString());
                }

                startActivity(intent);
            }
        });

        Button rep1 = findViewById(R.id.rep1);

        //Assign a listener to your button
        rep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RepPage.this,  DetailView.class);
                try {
                    JSONObject r1 = repsGlobal.getJSONObject(0);
                    intent.putExtra("rep", r1.toString());
                    intent.putExtra("zip", zipGlobal);
                    intent.putExtra("state", stateGlobal);
                } catch (JSONException | IndexOutOfBoundsException e ) {
                    Log.d("JSONException", e.toString());
                }
                startActivity(intent);
            }
        });

        Button rep2 = findViewById(R.id.rep2);

        //Assign a listener to your button
        rep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(RepPage.this,  DetailView.class);
                try {
                    JSONObject r2 = repsGlobal.getJSONObject(1);
                    intent.putExtra("rep", r2.toString());
                    intent.putExtra("zip", zipGlobal);
                    intent.putExtra("state", stateGlobal);
                } catch (JSONException | IndexOutOfBoundsException e ) {
                    Log.d("JSONException", e.toString());
                }
                startActivity(intent);
            }
        });

        Button rep3 = findViewById(R.id.rep3);

        //Assign a listener to your button
        rep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RepPage.this, DetailView.class);
                try {
                    JSONObject r3 = repsGlobal.getJSONObject(2);
                    intent.putExtra("rep", r3.toString());
                    intent.putExtra("zip", zipGlobal);
                    intent.putExtra("state", stateGlobal);

                } catch (JSONException | IndexOutOfBoundsException e ) {
                    Log.d("JSONException", e.toString());
                }
                startActivity(intent);
            }
        });


    }

    private void randomLoc(){
        int n = 1000000;
        int highLat = (int)(44.101053 * n);
        int lowLat = (int)(32.630631 * n);
        int highLon = (int)(-80.099145 * n);
        int lowLon =  (int)(-122.867530 * n);


        try {
            Random r = new Random();

            Double randLat = (double) (r.nextInt(highLat - lowLat) + lowLat) / n;
            Double randLon = (double) (r.nextInt(highLon - lowLon) + lowLon) / n;

            String params = "?q=" + randLat.toString() + "," + randLon.toString() + "&api_key=45965bcbba520b5551556998b596242bc4e8911&fields=cd";

            Log.d("url", "https://api.geocod.io/v1.3/reverse" + params);

            String api = "https://api.geocod.io/v1.3/reverse" + params;
            URL url = new URL(api);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();


            JSONObject j = new JSONObject(content.toString());

            JSONObject loc = j.getJSONArray("results").getJSONObject(0).getJSONObject("address_components");
            String zip = loc.getString("zip");
            String city = loc.getString("city");
            String state = loc.getString("state");
            stateGlobal = state;



            TextView locBox = findViewById(R.id.zip_or_current_location);

            String locString = zip + " " + city + ", " + state;

            locBox.setText(locString);


            JSONParse(j);

        } catch (IOException | JSONException e) {
            Log.d(e.toString(), e.toString());
        }


    }

    public void zipCode(Integer zip_code){

        TextView current_location = findViewById(R.id.zip_or_current_location);

        String zipString = "Zip Code " + zip_code.toString();

        current_location.setText(zipString);

//        "https://api.geocod.io/v1.3/geocode?postal_code=93105&api_key=45965bcbba520b5551556998b596242bc4e8911&fields=cd"


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);



        try {
            String urlString = "https://api.geocod.io/v1.3/geocode?postal_code=" + zip_code.toString() + "&api_key=45965bcbba520b5551556998b596242bc4e8911&fields=cd";

            Log.d("url",urlString);


            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();


            JSONObject j = new JSONObject(content.toString());

            JSONObject loc = j.getJSONArray("results").getJSONObject(0).getJSONObject("address_components");
            String zip = loc.getString("zip");
            String city = loc.getString("city");
            String state = loc.getString("state");

            stateGlobal = state;



            TextView locBox = findViewById(R.id.zip_or_current_location);

            String locString = zip + " " + city + ", " + state;

            locBox.setText(locString);


            JSONParse(j);







        } catch (IOException | JSONException e) {
            Log.d(e.toString(), e.toString());
        }


    }

    private void JSONParse(JSONObject j) {

        try {
            Log.d("before reps", "before reps");
            JSONArray reps = j.getJSONArray("results").getJSONObject(0).getJSONObject("fields").getJSONArray("congressional_districts").getJSONObject(0).getJSONArray("current_legislators");

            zipGlobal =  j.getJSONArray("results").getJSONObject(0).getJSONObject("address_components").getString("zip");


            Log.d("after reps", "after reps");

            Log.d("reps", reps.toString());

            repsGlobal = reps;

            Button b;
            for (int i = 0; i < 3; i++) {

                JSONObject r = reps.getJSONObject(i);

                JSONObject bio = r.getJSONObject("bio");



                Log.d("bio", bio.toString());

                String type = r.getString("type");

                String name = bio.getString("first_name") + " " + bio.getString("last_name");

                String contact = r.getJSONObject("contact").getString("contact_form");
                Log.d("contact", contact);


                String party = bio.getString("party");

                if (contact == null) {
                    contact = r.getJSONObject("contact").getString("url");
                }
                int id = R.id.rep1;

                switch (i + 1) {
                    case 1:
                        id = R.id.rep1;
                        break;
                    case 2:
                        id = R.id.rep2;
                        break;
                    case 3:
                        id = R.id.rep3;
                        break;

                }
                b = findViewById(id);

                if (party.equals("Democrat")) {
                    Log.d("rep name", name);
                    b.setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (party.equals("Republican")) {
                    b.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    b.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                String buttonString;
                if (type.equals("representative")) {
                    buttonString = "House of Reps \n " + name;
                    b.setText(buttonString);
                } else {
                    buttonString = "Senator " + name;
                    b.setText(buttonString);
                }

            }
        } catch (JSONException e) {
                Log.d("JSONException", e.toString());
            }

    }



    public void currentLocation(){

        try {
            final Activity a = this;

            TextView current_location = findViewById(R.id.zip_or_current_location);

            current_location.setText(getResources().getString(R.string.current_location));


            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(a);


            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {



                @Override
                public void onSuccess(Location location) {

//                            Log.d("before getReps", location.toString());
                    if (location != null) {
                        Log.d("location",location.toString());
                        getRepsFromLocation(a, location);
                    } else {
                        Log.d("location is null","location is null");
                    }
                    Log.d("after getReps", "after getReps@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

                }


            });

        } catch (SecurityException e ){
            Log.d("Security Exception", e.toString());

        }

    }

    public void getRepsFromLocation(Activity a, Location location){

        Location l = new Location("");


        // Got last known location. In some rare situations this can be null.
        if (location != null) {
            // Logic to handle location object
            Double lat = location.getLatitude();
            Log.d("latitude",lat.toString());
            Double lon = location.getLongitude();
            Log.d("longitude",lon.toString());

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);



            try {
                String params = "?q=" + lat.toString() + "," + lon.toString() + "&api_key=45965bcbba520b5551556998b596242bc4e8911&fields=cd";

                Log.d("url","https://api.geocod.io/v1.3/reverse" + params);

                String api = "https://api.geocod.io/v1.3/reverse" + params;
                URL url = new URL(api);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();



                JSONObject j = new JSONObject(content.toString());

                JSONObject loc = j.getJSONArray("results").getJSONObject(0).getJSONObject("address_components");
                String zip = loc.getString("zip");
                String city = loc.getString("city");
                String state = loc.getString("state");

                stateGlobal = state;

                TextView locBox = findViewById(R.id.zip_or_current_location);

                String locString = zip + " " + city + ", " + state;

                locBox.setText(locString);

                JSONParse(j);




            } catch (IOException | JSONException e) {
                Log.d(e.toString(), e.toString());
            }

        }

    }


}
