package com.example.sosemergency.DataLoader;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sosemergency.entities.User;

/*
 * This interface serves as the Data Access Object (DAO) for the User entity
 * in the Room database. It includes methods annotated with Room annotations
 * for performing CRUD (Create, Read, Update, Delete) operations on the User table.
 *
 * @Dao annotation:
 *   Indicates that this interface is a Data Access Object for Room.
 *
 * Methods:
 *   - insertUser: Inserts a new User into the database.
 *                 Returns the row ID of the inserted user.
 *
 *   - getUser: Retrieves a single user from the database.
 *              Returns a User object.
 *
 *   - deleteUser: Deletes all users from the database.
 *
 * Note: Unlike getAllContacts in the ContactLoader interface, getUser here
 *       returns a single User object instead of a List. This assumes that
 *       there is only one user in the "users" table.
 *
 */
@Dao
public interface UserLoader {

    /*
     * Inserts a new user into the database.
     *
     * @param user - The User object to be inserted.
     * @return long - The row ID of the inserted user.
     */
    @Insert
    long insertUser(User user);

    /*
     * Retrieves a single user from the database.
     *
     * @return User - The User object retrieved from the database.
     */
    @Query("SELECT * FROM users")
    User getUser();

    /*
     * Deletes all users from the database.
     */
    @Query("DELETE FROM users")
    void deleteUser();
}


