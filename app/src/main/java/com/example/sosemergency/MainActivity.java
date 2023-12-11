package com.example.sosemergency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sosemergency.ui.registration.UserRegistrationActivity;
import com.example.sosemergency.utils.AllergyPersistenceManager;
import com.example.sosemergency.utils.ContactPersistenceManager;
import com.example.sosemergency.utils.ThreadPoolManager;
import com.example.sosemergency.utils.UserPersistenceManager;
/**
 * The main activity of the SOS Emergency app.
 */
public class MainActivity extends AppCompatActivity {

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        // Initialize the Room database for user information
        UserPersistenceManager.initAppDatabase(getApplicationContext());
        // Initialize the Room database for contact information
        ContactPersistenceManager.initAppDatabase(getApplicationContext());
        // Initialize the Room database for allergy information
        AllergyPersistenceManager.initAppDatabase(getApplicationContext());
        // Set click listener for the button to navigate to the BootstrapActivity
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!UserPersistenceManager.exists()){
                    // Create an intent to navigate to the BootstrapActivity
                    Intent registerIntent = new Intent(MainActivity.this, UserRegistrationActivity.class);
                    // Start the BootstrapActivity
                    startActivity(registerIntent);
                }else {
                    // Create an intent to navigate to the BootstrapActivity
                    Intent homeIntent = new Intent(MainActivity.this, BootstrapActivity.class);
                    // Start the BootstrapActivity
                    startActivity(homeIntent);
                }
            }
        });
    }

    /**
     * Ensures proper cleanup and shutdown of resources.
     * Called when the activity is being destroyed.
     */
    @Override
    protected void onDestroy() {
        // Ensure proper shutdown of the thread pool
        ThreadPoolManager.shutdown();
        super.onDestroy();
    }
}