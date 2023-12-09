package com.example.sosemergency.ui.registration;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosemergency.BootstrapActivity;
import com.example.sosemergency.MainActivity;
import com.example.sosemergency.R;
import com.example.sosemergency.ui.contact.ContactModel;

import java.util.ArrayList;
import java.util.List;

/**
 * ContactRegistrationActivity - Activity for registering contacts.
 *
 * This activity allows the user to pick contacts from the device's contact list and
 * displays the selected contacts in a RecyclerView. The user can add contacts until
 * a maximum of four contacts is reached. The selected contacts can be removed, and the
 * user can navigate to other activities using icons provided on the screen.
 */
public class ContactRegistrationActivity extends AppCompatActivity {

    // Constant to identify the result of picking a contact
    private static final int RESULT_PICK_CONTACT = 1;

    // UI elements
    ImageView addIcon;
    ImageView passIcon;
    TextView textChoose;
    RecyclerView contactRecyclerView;
    ContactAdapter contactAdapter;
    ImageView dotIcon;

    // List to store selected contacts
    List<ContactModel> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_registration);

        // Initialize UI elements
        addIcon = findViewById(R.id.addIcon);
        textChoose = findViewById(R.id.textChoose);
        contactRecyclerView = findViewById(R.id.contactRecyclerView);
        dotIcon = findViewById(R.id.registrationDot);
        passIcon = findViewById(R.id.passIcon);

        // Initialize RecyclerView and its adapter
        contactAdapter = new ContactAdapter(contactList, passIcon, addIcon, dotIcon);
        contactRecyclerView.setAdapter(contactAdapter);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set click listeners for icons
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the user has already picked four contacts
                if (contactList.size() < 4) {
                    // Start the contact picking intent
                    Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(in, RESULT_PICK_CONTACT);
                } else {
                    // Display a toast message indicating the maximum contacts reached
                    Toast.makeText(ContactRegistrationActivity.this, "You have already picked four contacts", Toast.LENGTH_SHORT).show();
                }
            }
        });
        passIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start BootstrapActivity when passIcon is clicked
                startActivity(new Intent(ContactRegistrationActivity.this, BootstrapActivity.class));
            }
        });
        dotIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start UserRegistrationActivity when dotIcon is clicked
                startActivity(new Intent(ContactRegistrationActivity.this, UserRegistrationActivity.class));
            }
        });
    }

    // Handle the result of picking a contact
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    // Process the picked contact
                    contactPicked(data);
                    break;
            }
        } else {
            // Display a toast message for a failed contact pick
            Toast.makeText(this, "Failed To pick contact", Toast.LENGTH_SHORT).show();
        }
    }

    // Process the picked contact and add it to the list
    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try {
            // Extract contact information from the cursor
            String contactName = null;
            String contactPhone = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                contactName = cursor.getString(nameIndex);
                contactPhone = cursor.getString(phoneIndex);

                // Check if the contact is not already picked
                if (!isContactAlreadyPicked(contactName, contactPhone)) {
                    // Create a new ContactModel
                    ContactModel contact = new ContactModel(contactName, contactPhone);

                    // Add the picked contact to the list
                    contactList.add(contact);

                    // Notify the adapter of the data set change
                    contactAdapter.notifyItemInserted(contactList.size() - 1);
                } else {
                    // Display a toast message indicating that the contact is already picked
                    Toast.makeText(this, "This contact is already picked", Toast.LENGTH_SHORT).show();
                }
            }
            // Update visibility of icons based on the number of selected contacts
            updateIconVisibility();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                // Close the cursor if it's not null
                cursor.close();
            }
        }
    }

    // Update visibility of icons based on the number of selected contacts
    private void updateIconVisibility() {
        if (contactList.size() >= 1 && contactList.size() < 4) {
            // If one or more than one contact is chosen but less than 4, show both the "Add" icon and the "Select" icon
            passIcon.setVisibility(View.VISIBLE);
            dotIcon.setVisibility(View.GONE);
        } else if (contactList.size() == 4) {
            // If 4 contacts are chosen, hide the "Add" icon and show only the "Select" icon
            addIcon.setVisibility(View.GONE);
            passIcon.setVisibility(View.VISIBLE);
            dotIcon.setVisibility(View.GONE);
        }
    }

    // Helper method to check if the contact is already picked
    private boolean isContactAlreadyPicked(String name, String phone) {
        for (ContactModel contact : contactList) {
            if (contact.getName().equals(name) && contact.getPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }

    // Method to remove a contact when the delete button is clicked
    private void removeContact(int position) {
        contactAdapter.removeContact(position);
    }
}