package com.example.sosemergency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
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

    private ImageView circle; // Circle ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circle = findViewById(R.id.app_logo); // Replace with the ID of your circle ImageView

        // Initialize the Room database for user information
        UserPersistenceManager.initAppDatabase(getApplicationContext());
        // Initialize the Room database for contact information
        ContactPersistenceManager.initAppDatabase(getApplicationContext());
        // Initialize the Room database for allergy information
        AllergyPersistenceManager.initAppDatabase(getApplicationContext());

        // Display the logo and animation for 1 second before redirecting
        displayLogoAndAnimation();
    }

    private void displayLogoAndAnimation() {
        circle.setVisibility(View.VISIBLE);

        // Create a RotateAnimation for the circle
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000); // 1 second duration
        rotateAnimation.setRepeatCount(Animation.INFINITE); // Infinite rotation

        // Start the animation
        circle.startAnimation(rotateAnimation);

        // Use a Handler to delay the redirection
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Stop the animation
                circle.clearAnimation();
                // Check if the user is already registered
                if (UserPersistenceManager.exists()) {
                    // If registered, redirect to BootstrapActivity
                    redirectToBootstrapActivity();
                } else {
                    // If not registered, redirect to the registration page
                    redirectToRegistrationPage();
                }
            }
        }, 1000); // 1000 milliseconds (1 second) delay
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