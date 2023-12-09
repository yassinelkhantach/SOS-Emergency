package com.example.sosemergency.utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.example.sosemergency.DataLoader.AppDatabase;
import com.example.sosemergency.DataLoader.ContactDatabaseContract;
import com.example.sosemergency.entities.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/*
 * This class manages the persistence of Contact entities in the Room database.
 * It provides methods for initializing the database, adding, retrieving, and deleting contacts.
 *
 * Methods:
 *   - initAppDatabase: Initializes the Room database and returns an instance of AppDatabase.
 *   - addContact: Adds a single contact to the Room database.
 *   - addContacts: Adds a list of contacts to the Room database.
 *   - getContacts: Retrieves a list of all contacts from the Room database.
 *   - deleteContacts: Deletes all contacts from the Room database.
 *   - deleteContact: Deletes a specific contact from the Room database based on the phone number.
 */
public class ContactPersistenceManager {
    private static Context appContext;
    private static AppDatabase appDatabase;

    // Initialize the database in a static method
    public static AppDatabase initAppDatabase(Context context) {
        if (appDatabase == null) {
            appContext = context;
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "contact-database").build();
        }
        return appDatabase;
    }

    // Adds a single contact to the Room database
    public static void addContact(Contact contact) {
        try {
            // Create a ContentValues object to store contact data
            ContentValues values = new ContentValues();
            values.put(ContactDatabaseContract.ContactEntry.COLUMN_NAME, contact.getName());
            values.put(ContactDatabaseContract.ContactEntry.COLUMN_PHONE_NUMBER, contact.getPhoneNumber());
            ThreadPoolManager.execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.contactLoader().insertContact(contact);
                }
            });
        } catch (Exception e) {
            Log.e("addContact", "Error adding contact: " + e.getMessage());
            showToast("Error adding contact");
        }
    }

    // Adds a list of contacts to the Room database
    public static void addContacts(List<Contact> selectedContacts) {
        if (selectedContacts.isEmpty()) {
            return;
        }
        // Convert the list of contacts to an array for batch insertion
        Contact[] contactsArray = selectedContacts.toArray(new Contact[0]);
        // AsyncTask for background processing
        ThreadPoolManager.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Batch insert the contacts into the Room database
                    appDatabase.contactLoader().insertContacts(contactsArray);
                    Log.i("addContacts", "Contacts added successfully!");
                } catch (Exception e) {
                    Log.e("addContacts", "Error adding contacts: " + e.getMessage());
                    showToast("Error adding contacts");
                }
            }
        });
    }

    // Retrieves a list of all contacts from the Room database
    public static List<Contact> getContacts() {
        try {
            // Using Callable directly
            Callable<List<Contact>> callable = new Callable<List<Contact>>() {
                @Override
                public List<Contact> call() {
                    try {
                        // the list obtained from the Room database
                        return appDatabase.contactLoader().getAllContacts();
                    } catch (Exception e) {
                        Log.e("getContacts", "Error retrieving contacts: " + e.getMessage());
                        return new ArrayList<>(); // Return an empty list or handle the error accordingly
                    }
                }
            };

            // Execute the task in the background
            return ThreadPoolManager.executeDatabaseQuerySync(callable);
        } catch (Exception e) {
            Log.e("getContacts", "Error retrieving contacts: " + e.getMessage());
            showToast("There was an error retrieving contacts!");
            return new ArrayList<>(); // Return an empty list or handle the error accordingly
        }
    }

    // Deletes all contacts from the Room database
    public static void deleteContacts() {
        try {
            ThreadPoolManager.execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.contactLoader().clearAllContacts();
                }
            });
        } catch (Exception e) {
            Log.e("deleteContacts", "Error deleting contacts: " + e.getMessage());
            showToast("Error: Cannot delete all contacts");
        }
    }

    // Deletes a specific contact from the Room database based on the phone number
    public static void deleteContact(String phoneNumber) {
        try {
            ThreadPoolManager.execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.contactLoader().deleteContact(phoneNumber);
                }
            });
        } catch (Exception e) {
            Log.e("deleteContact", "Error deleting contact: " + e.getMessage());
            showToast("Error: Cannot delete this contact");
        }
    }

    private static void showToast(String message) {
        // Assuming 'this' refers to the current Activity or you have a reference to a Context
        Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show();
    }
}