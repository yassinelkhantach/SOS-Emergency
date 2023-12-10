package com.example.sosemergency;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.sosemergency.entities.Contact;
import com.example.sosemergency.utils.ContactPersistenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.sosemergency.databinding.ActivityBootstrapBinding;

public class BootstrapActivity extends AppCompatActivity {

    private ActivityBootstrapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = ActivityBootstrapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_report, R.id.navigation_profile,R.id.navigation_contact,R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_report);
        NavigationUI.setupWithNavController(binding.navView, navController);

        /*
         * This test demonstrates the usage of ContactPersistenceManager methods in the application.
         * It initializes the Room database and adds a contact to the database for testing purposes.
         */

        // Initialize the Room database for testing
        ContactPersistenceManager.initAppDatabase(getApplicationContext());

        // Add a contact to the Room database for testing
        ContactPersistenceManager.addContact(
                new Contact(
                        getResources().getStringArray(R.array.contact_name)[0],
                        getResources().getStringArray(R.array.contact_phone)[0]
                )
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Navigate back to the home page and then to main activity.
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_report);

        if (item.getItemId() == R.id.navigation_home) {
            navController.navigate(R.id.navigation_home);
            return true;
        }

        if (item.getItemId() == R.id.navigation_settings) {
            if (navController.getCurrentDestination().getId() != R.id.navigation_settings) {
                navController.navigate(R.id.navigation_settings);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void hideBottomNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        if (bottomNavigationView != null) {
            bottomNavigationView.animate().translationYBy(bottomNavigationView.getHeight()).alpha(0f)
                    .setDuration(300).withEndAction(() -> {
                        bottomNavigationView.setVisibility(View.GONE);
                        bottomNavigationView.setTranslationY(0);
                    }).start();
        }
    }

    public void showBottomNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        if (bottomNavigationView != null) {
            bottomNavigationView.setAlpha(0f);
            bottomNavigationView.setTranslationY(bottomNavigationView.getHeight());
            bottomNavigationView.setVisibility(View.VISIBLE);
            bottomNavigationView.animate().translationYBy(-bottomNavigationView.getHeight()).alpha(1f)
                    .setDuration(300).start();
        }
    }
}