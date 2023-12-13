package com.example.sosemergency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.example.sosemergency.ui.registration.UserRegistrationActivity;
import com.example.sosemergency.utils.AllergyPersistenceManager;
import com.example.sosemergency.utils.ContactPersistenceManager;
import com.example.sosemergency.utils.ThreadPoolManager;
import com.example.sosemergency.utils.UserPersistenceManager;
/**
 * The main activity of the SOS Emergency app.
 */
public class MainActivity extends AppCompatActivity {

    private ImageView appLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appLogo = findViewById(R.id.app_logo); // Assuming you have an ImageView for the app logo

        // Initialize the Room database for user information
        UserPersistenceManager.initAppDatabase(getApplicationContext());
        // Initialize the Room database for contact information
        ContactPersistenceManager.initAppDatabase(getApplicationContext());
        // Initialize the Room database for allergy information
        AllergyPersistenceManager.initAppDatabase(getApplicationContext());

        // Display the logo for 1 second
        displayLogoAndRedirect();
    }

    /**
     * Display the logo for 2 or 3 seconds before redirecting.
     */
    private void displayLogoAndRedirect() {
        appLogo.setVisibility(View.VISIBLE);

        // Use a Handler to delay the redirection
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if the user is already registered
                if (UserPersistenceManager.exists()) {
                    // If registered, redirect to BootstrapActivity
                    redirectToBootstrapActivity();
                } else {
                    // If not registered, redirect to the registration page
                    redirectToRegistrationPage();
                }
            }
        }, 1000); // 2000 milliseconds (2 seconds) delay
    }

    /**
     * Redirect to the BootstrapActivity.
     */
    private void redirectToBootstrapActivity() {
        Intent bootstrapIntent = new Intent(MainActivity.this, BootstrapActivity.class);
        startActivity(bootstrapIntent);
    }

    /**
     * Redirect to the registration page.
     */
    private void redirectToRegistrationPage() {
        Intent registrationIntent = new Intent(MainActivity.this, UserRegistrationActivity.class);
        startActivity(registrationIntent);
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