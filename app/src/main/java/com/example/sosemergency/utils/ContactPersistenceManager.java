package com.example.sosemergency.utils;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import com.example.sosemergency.DataLoader.AppDatabase;
import com.example.sosemergency.DataLoader.ContactDatabaseContract;
import com.example.sosemergency.entities.Contact;

import java.util.ArrayList;
import java.util.List;

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
        // Create a ContentValues object to store contact data
        ContentValues values = new ContentValues();
        values.put(ContactDatabaseContract.ContactEntry.COLUMN_NAME, contact.getName());
        values.put(ContactDatabaseContract.ContactEntry.COLUMN_PHONE_NUMBER, contact.getPhoneNumber());
        // Insert the contact into the Room database
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.contactLoader().insertContact(contact);
            }
        });
    }

    // Adds a list of contacts to the Room database
    public static void addContacts(List<Contact> selectedContacts) {
        // Iterate through the selected contacts and add them to the database
        for (Contact contact : selectedContacts) {
            String name = contact.getName();
            String phoneNumber = contact.getPhoneNumber();

            // Create a ContentValues object to store contact data
            ContentValues values = new ContentValues();
            values.put(ContactDatabaseContract.ContactEntry.COLUMN_NAME, name);
            values.put(ContactDatabaseContract.ContactEntry.COLUMN_PHONE_NUMBER, phoneNumber);
            // Insert the contact into the Room database
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.contactLoader().insertContact(new Contact(name, phoneNumber));
                }
            });
        }
    }

    // Retrieves a list of all contacts from the Room database
    public static List<Contact> getContacts() {
        List<Contact> contactList = new ArrayList<>();
        // Retrieve contacts from the Room database
        List<Contact> localContacts = appDatabase.contactLoader().getAllContacts();
        // Add local contacts to the list
        contactList.addAll(localContacts);
        return contactList;
    }

    // Deletes all contacts from the Room database
    public static void deleteContacts() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.contactLoader().clearAllContacts();
            }
        });
    }

    // Deletes a specific contact from the Room database based on the phone number
    public static void deleteContact(String phoneNumber) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.contactLoader().deleteContactById(phoneNumber);
            }
        });
    }
}