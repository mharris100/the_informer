package com.example.mharris.theinformer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailView extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        JSONObject rep = null;
        try {
            rep = new JSONObject(getIntent().getStringExtra("rep"));

            Log.d("rep", rep.toString());

            final String zip = getIntent().getStringExtra("zip");


            JSONObject bio = rep.getJSONObject("bio");

            String type = rep.getString("type");
            String party = bio.getString("party");
            String name = bio.getString("first_name") + " " + bio.getString("last_name");
//            String district = rep.getString("name");
            String state = getIntent().getStringExtra("state");
            String phone = rep.getJSONObject("contact").getString("phone");
            String twitter = rep.getJSONObject("social").getString("twitter");


            String contact = rep.getJSONObject("contact").getString("contact_form");

            if (contact == "null") {
                contact = rep.getJSONObject("contact").getString("url");
            }
            if (contact == "null") {
                contact = "No Contact Info";
            }

            View b = findViewById(R.id.background);

            if (party.equals("Democrat")) {
                Log.d("rep name", name);
                b.setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (party.equals("Republican")) {
                b.setBackgroundColor(getResources().getColor(R.color.red));
            } else {
                b.setBackgroundColor(getResources().getColor(R.color.gray));
            }

            TextView repName = findViewById(R.id.repName2);
            repName.setText(name);


            TextView repOrSen = findViewById(R.id.repOrSen);

            String repOrSenString;
            if (type.equals("representative")) {
                repOrSenString = "House of Representatives (" + state + ")";
            } else {
                repOrSenString = "Senator (" + state + ")";
            }

            repOrSen.setText(repOrSenString);

            TextView repParty = findViewById(R.id.party);

            String  partyString = "Party:  " + party;

            repParty.setText(partyString);


            TextView repContact = findViewById(R.id.contact2);

            String  contactString = "Contact:  " + contact;

            repContact.setText(contactString);


            TextView repPhone = findViewById(R.id.phone);

            String  phoneString = "Phone:  " + phone;

            repPhone.setText(phoneString);


            TextView reptwitter = findViewById(R.id.twitter);

            String  twitterString = "Twitter:  " + twitter;

            reptwitter.setText(twitterString);


            Button backButton = findViewById(R.id.back);

            //Assign a listener to your button
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailView.this, RepPage.class);
                    intent.putExtra("type", "zip");
                    intent.putExtra("zip", zip);
                    startActivity(intent);
                }
            });





        } catch (JSONException e) {
            Log.d("JSonException", e.toString());
        }








    }
}
