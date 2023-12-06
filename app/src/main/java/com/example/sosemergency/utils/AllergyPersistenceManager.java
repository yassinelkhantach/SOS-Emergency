package com.example.sosemergency.utils;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import com.example.sosemergency.DataLoader.AllergyDatabaseContract;
import com.example.sosemergency.DataLoader.AppDatabase;
import com.example.sosemergency.entities.Allergy;

import java.util.List;

public class AllergyPersistenceManager {

    // The application context used for database initialization
    private static Context appContext;

    // An instance of the Room database.
    private static AppDatabase appDatabase;

    // Initialize the database in a static method
    public static AppDatabase initAppDatabase(Context context) {
        if (appDatabase == null) {
            appContext = context;
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "allergy-database").build();
        }
        return appDatabase;
    }

    // insert an allergy by adding its information to the Room database
    public static void insertAllergy(Allergy allergy) {
        // Create a ContentValues object to store contact data
        ContentValues values = new ContentValues();
        values.put(AllergyDatabaseContract.AllergyEntry.COLUMN_NAME, allergy.getName());
        values.put(AllergyDatabaseContract.AllergyEntry.COLUMN_DESCRIPTION, allergy.getDescription());
        values.put(AllergyDatabaseContract.AllergyEntry.COLUMN_USER_ID, allergy.getUserId());
        // Insert the contact into the Room database
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.allergyLoader().insertAllergy(allergy);
                Log.i("insertAllergy",allergy.getName()+" allergy inserted successfully !");
            }
        });
    }

    // Retrieves all allergies from the Room database
    public static List<Allergy> getAllergies() {
        return appDatabase.allergyLoader().getAllergies();
    }


    // Retrieves the user allergies from the Room database
    public static List<Allergy> getUserAllergies(Long userId) {
        return appDatabase.allergyLoader().getUserAllergies(userId);
    }

    // Delete all allergies from the Room database
    public static void deleteAllergies() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.allergyLoader().deleteAllergies();
                Log.i("deleteAllergies","allergies deleted successfully !");
            }
        });
    }
}