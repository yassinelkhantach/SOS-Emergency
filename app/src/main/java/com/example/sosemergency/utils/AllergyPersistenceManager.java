package com.example.sosemergency.utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.sosemergency.DataLoader.AllergyDatabaseContract;
import com.example.sosemergency.DataLoader.AppDatabase;
import com.example.sosemergency.entities.Allergy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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
        ThreadPoolManager.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.allergyLoader().insertAllergy(allergy);
                Log.i("insertAllergy",allergy.getName()+" allergy inserted successfully !");
            }
        });
    }

    // Retrieves all allergies from the Room database
    public static List<Allergy> getAllergies() {
        // Wrap the original getAllergies method using ThreadPoolManager
        try {
            return ThreadPoolManager.executeDatabaseQuerySync(new Callable<List<Allergy>>() {
                @Override
                public List<Allergy> call() {
                    try {
                        // Retrieve the list of allergies from the Room database
                        return appDatabase.allergyLoader().getAllergies();
                    } catch (Exception e) {
                        Log.e("getAllergies", "Error retrieving allergies: " + e.getMessage());
                        return new ArrayList<>(); // Return an empty list or handle the error accordingly
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Retrieves the user allergies from the Room database
    public static List<Allergy> getUserAllergies(Long userId) {
        // Wrap the original getUserAllergies method using ThreadPoolManager
        try {
            return ThreadPoolManager.executeDatabaseQuerySync(new Callable<List<Allergy>>() {
                @Override
                public List<Allergy> call() {
                    try {
                        // Retrieve the user allergies from the Room database based on the user ID
                        return appDatabase.allergyLoader().getUserAllergies(userId);
                    } catch (Exception e) {
                        Log.e("getUserAllergies", "Error retrieving user allergies: " + e.getMessage());
                        return new ArrayList<>(); // Return an empty list or handle the error accordingly
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Delete all allergies from the Room database
    public static void deleteAllergies() {
        ThreadPoolManager.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.allergyLoader().deleteAllergies();
                Log.i("deleteAllergies","allergies deleted successfully !");
            }
        });
    }

    // Delete selected allergy from the Room database
    public static void deleteAllergy(Allergy allergy) {
        ThreadPoolManager.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.allergyLoader().deleteAllergy(allergy.getName());
                Log.i("deleteAllergy",allergy.getName()+ " deleted successfully !");
            }
        });
    }
}