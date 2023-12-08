package com.example.sosemergency.ui.registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.sosemergency.R;

public class UserRegistrationActivity extends AppCompatActivity {
    EditText fullName;
    EditText dateOfBirth;
    AutoCompleteTextView country;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        fullName = findViewById(R.id.registrationFullName);
        dateOfBirth = findViewById(R.id.registrationDateOfBirth);
        country = findViewById(R.id.registrationCountry);
        ImageView dotIcon = findViewById(R.id.registrationDot);

        // Set up an array of country names
        String[] countries = {"Morocco", "France", "England"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, countries);
        country.setAdapter(adapter);

        // Set OnClickListener for the dot icon
        dotIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if all required fields are filled
                if (isFieldsFilled()) {
                    // All fields are filled, proceed to Activity 2
                    startActivity(new Intent(UserRegistrationActivity.this, ContactRegistrationActivity.class));
                } else {
                    // Display a message or perform some action if fields are not filled
                    Toast.makeText(UserRegistrationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Helper method to check if all required fields are filled
    private boolean isFieldsFilled() {
        String name = fullName.getText().toString();
        String dob = dateOfBirth.getText().toString();
        String selectedCountry = country.getText().toString();
        // Check if any of the fields is empty
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(dob) && !TextUtils.isEmpty(selectedCountry);
    }
}
