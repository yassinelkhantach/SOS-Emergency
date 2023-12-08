package com.example.sosemergency.DataLoader;

import android.provider.BaseColumns;

/*
 * This class defines the contract for the Contact database, including the table structure
 * and column names. It follows the recommended Android SQLite database contract design.
 *
 * The class is marked as final and the constructor is private to prevent accidental instantiation.
 * It includes an inner class, ContactEntry, that implements the BaseColumns interface to
 * include standard column names like _ID.
 *
 * Example Usage:
 *   To reference the table name: ContactDatabaseContract.ContactEntry.TABLE_NAME
 *   To reference the name column: ContactDatabaseContract.ContactEntry.COLUMN_NAME
 *   To reference the phone number column: ContactDatabaseContract.ContactEntry.COLUMN_PHONE_NUMBER
 */
public final class ContactDatabaseContract {
    private ContactDatabaseContract() {}

    /* Inner class that defines the table contents */
    public static class ContactEntry implements BaseColumns {

        /*
         * The name of the table in the Contact database.
         */
        public static final String TABLE_NAME = "contacts";

        /*
         * The column name for the contact's name.
         */
        public static final String COLUMN_NAME = "name";

        /*
         * The column name for the contact's phone number.
         */
        public static final String COLUMN_PHONE_NUMBER = "phone_number";
    }
}

