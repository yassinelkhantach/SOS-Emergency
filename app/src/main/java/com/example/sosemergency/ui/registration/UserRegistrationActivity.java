package com.example.sosemergency.ui.registration;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.sosemergency.R;
import com.example.sosemergency.entities.User;
import com.example.sosemergency.utils.DateConverterUtil;
import com.example.sosemergency.utils.UserPersistenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    // Request code for starting ContactRegistrationActivity
    private static final int REQUEST_CONTACT_REGISTRATION = 2;

    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        gestureDetector = new GestureDetector(this, new SwipeGestureListener());


        // Initialize UI elements
        fullName = findViewById(R.id.registrationFullName);
        dateOfBirth = findViewById(R.id.registrationDateOfBirth);
        country = findViewById(R.id.registrationCountry);
        dotIcon = findViewById(R.id.registrationDot);

        // Set up an array of country names for the autocomplete feature
        String[] countries = {"Morocco", "France", "England"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, countries);
        country.setAdapter(adapter);

        //Set DatPicker for dateOfBirth
        dateOfBirth.setOnClickListener(view -> showDatePicker(dateOfBirth));
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = day+"-"+month+"-"+year;
                dateOfBirth.setText(date);
            }
        };

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();

            if (Math.abs(diffX) > Math.abs(diffY)
                    && Math.abs(diffX) > SWIPE_THRESHOLD
                    && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                // Right swipe detected

                // Check if all required fields are filled
                if (isFieldsFilled()) {
                    // All fields are filled, proceed to ContactRegistrationActivity
                    Intent intent = new Intent(UserRegistrationActivity.this, ContactRegistrationActivity.class);

                    // Pass user data to ContactRegistrationActivity
                    intent.putExtra("FULL_NAME", fullName.getText().toString());
                    intent.putExtra("DOB", dateOfBirth.getText().toString());
                    intent.putExtra("COUNTRY", country.getText().toString());

                    startActivityForResult(intent, REQUEST_CONTACT_REGISTRATION);
                    return true;
                } else {
                    // Display a message if fields are not filled
                    Toast.makeText(UserRegistrationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
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

    // Use Date Picker for date of birth
    private void showDatePicker(EditText profileDate) {
        // Get the current date
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        // If profileDate has a valid date, use it as the initial date for the DatePicker
        String dateString = profileDate.getText().toString();
        if (!TextUtils.isEmpty(dateString)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            try {
                Date date = dateFormat.parse(dateString);
                cal.setTime(date);
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Create a DatePickerDialog with a custom theme and set the date listener
        DatePickerDialog dialog = new DatePickerDialog(UserRegistrationActivity.this, R.style.DatePickerTheme, mDateSetListener, year, month, day);

        // Set the background of the dialog to white
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        // Show the date picker dialog
        dialog.show();
    }
}
