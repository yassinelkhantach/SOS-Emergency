package com.example.sosemergency.ui.registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.sosemergency.R;
import com.example.sosemergency.entities.User;
import com.example.sosemergency.utils.DateConverterUtil;
import com.example.sosemergency.utils.UserPersistenceManager;

/**
 * UserRegistrationActivity - Activity for user registration.
 *
 * This activity allows the user to enter their full name, date of birth, and their country.
 * The user can proceed to the next activity by clicking the dot icon after
 * filling in all required fields.
 */
public class UserRegistrationActivity extends AppCompatActivity {
    // UI elements
    EditText fullName;
    EditText dateOfBirth;
    AutoCompleteTextView country;
    ImageView dotIcon;

    // Request code for starting ContactRegistrationActivity
    private static final int REQUEST_CONTACT_REGISTRATION = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        // Initialize UI elements
        fullName = findViewById(R.id.registrationFullName);
        dateOfBirth = findViewById(R.id.registrationDateOfBirth);
        country = findViewById(R.id.registrationCountry);
        dotIcon = findViewById(R.id.registrationDot);

        // Set up an array of country names for the autocomplete feature
        String[] countries = {"Morocco", "France", "England"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, countries);
        country.setAdapter(adapter);

        // Set OnClickListener for the dot icon
        dotIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if all required fields are filled
                if (isFieldsFilled()) {
                    //Save User Registration
                    User user = new User(fullName.getText().toString(), DateConverterUtil.parseDate(dateOfBirth.getText().toString()),country.getText().toString());
                    UserPersistenceManager.registerUser(user);

                    // All fields are filled, proceed to ContactRegistrationActivity
                    Intent intent = new Intent(UserRegistrationActivity.this, ContactRegistrationActivity.class);

                    // Pass user data to ContactRegistrationActivity
                    intent.putExtra("FULL_NAME", fullName.getText().toString());
                    intent.putExtra("DOB", dateOfBirth.getText().toString());
                    intent.putExtra("COUNTRY", country.getText().toString());

                    startActivityForResult(intent, REQUEST_CONTACT_REGISTRATION);

                } else {
                    // Display a message for performing some action if fields are not filled
                    Toast.makeText(UserRegistrationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Helper method to check if all required fields are filled.
     *
     * @return True if all fields are filled, false otherwise.
     */
    private boolean isFieldsFilled() {
        // Extract text from UI elements
        String name = fullName.getText().toString();
        String dob = dateOfBirth.getText().toString();
        String selectedCountry = country.getText().toString();

        // Check if any of the fields is empty
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(dob) && !TextUtils.isEmpty(selectedCountry);
    }

    // Override onActivityResult to handle the result from ContactRegistrationActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CONTACT_REGISTRATION && resultCode == RESULT_OK) {
            // Retrieve the data from ContactRegistrationActivity
            String fullNameValue = data.getStringExtra("FULL_NAME");
            String dobValue = data.getStringExtra("DOB");
            String countryValue = data.getStringExtra("COUNTRY");

            // Use the retrieved data as needed (e.g., populate the fields)
            fullName.setText(fullNameValue);
            dateOfBirth.setText(dobValue);
            country.setText(countryValue);
        }
    }
}
