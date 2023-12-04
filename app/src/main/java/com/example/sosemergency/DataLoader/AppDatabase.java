package com.example.sosemergency.DataLoader;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.sosemergency.entities.Contact;
import com.example.sosemergency.entities.User;
/*
 * This class represents the Room Database for the SOS Emergency application.
 * It is annotated with @Database to indicate that it is a Room database, and it
 * includes references to the entities Contact and User.
 *
 * @Database annotation parameters:
 *   entities - Array of entity classes that are part of this database.
 *              In this case, it includes Contact.class and User.class.
 *   version - The version number of the database. It is set to 1 in this example.
 *   exportSchema - Indicates whether to export the schema to a folder. Set to false.
 *
 * This class extends RoomDatabase and provides abstract methods for accessing
 * the DAOs (Data Access Objects) related to Contact and User entities.
 * The DAOs are ContactLoader and UserLoader, respectively.
 *
 * Example Usage:
 *   AppDatabase appDatabase = Room.databaseBuilder(context, AppDatabase.class, "database-name").build();
 *   ContactLoader contactLoader = appDatabase.contactLoader();
 *   UserLoader userLoader = appDatabase.userLoader();
 */
@Database(entities = {Contact.class, User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    /*
     * Abstract method to get the ContactLoader DAO.
     *
     * @return ContactLoader - Data Access Object for interacting with the Contact entity.
     */
    public abstract ContactLoader contactLoader();

    /*
     * Abstract method to get the UserLoader DAO.
     *
     * @return UserLoader - Data Access Object for interacting with the User entity.
     */
    public abstract UserLoader userLoader();
}
