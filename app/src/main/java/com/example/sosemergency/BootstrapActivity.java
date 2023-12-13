package com.example.sosemergency;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.customview.widget.Openable;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_report);

        // Check if the current destination is the Home fragment
        if (navController.getCurrentDestination().getId() == R.id.navigation_home) {
            // Do nothing or handle as needed when in the Home fragment
            // For example, you may want to show a message or perform a specific action
            // Override this block with your desired behavior.
            return true;
        } else {
            // Navigate up or handle the Up button press normally
            return NavigationUI.navigateUp(navController, (Openable) null);
        }
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