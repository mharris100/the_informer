package com.example.mharris.theinformer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private Button current_location_button;
    private EditText zip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        current_location_button = findViewById(R.id.current_location_button);

        zip = findViewById(R.id.zip_code_button);



        //Assign a listener to your button
        current_location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start your second activity
//                zip = true;
                Intent intent = new Intent(MainActivity.this, RepPage.class);
                intent.putExtra("type", "current location");
                startActivity(intent);
            }
        });




        //helped from https://stackoverflow.com/questions/1489852/android-handle-enter-in-an-edittext
        final TextView.OnEditorActionListener enterListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {

                    Intent intent = new Intent(MainActivity.this, RepPage.class);
                    intent.putExtra("type", "zip");
                    intent.putExtra("zip", zip.getText().toString());
                    startActivity(intent);
                }
                return true;
            }

        };

        zip.setOnEditorActionListener(enterListener);


    }
}
