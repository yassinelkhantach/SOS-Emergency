package com.example.sosemergency.DataLoader;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sosemergency.entities.Contact;

import java.util.List;

/*
 * This interface serves as the Data Access Object (DAO) for the Contact entity
 * in the Room database. It includes methods annotated with Room annotations
 * for performing CRUD (Create, Read, Update, Delete) operations on the Contact table.
 *
 * @Dao annotation:
 *   Indicates that this interface is a Data Access Object for Room.
 *
 * Methods:
 *   - insertContact: Inserts a new Contact into the database.
 *                    Returns the row ID of the inserted contact.
 *
 *   - getAllContacts: Retrieves all contacts from the database.
 *                     Returns a List of Contact objects.
 *
 *   - clearAllContacts: Deletes all contacts from the database.
 *
 *   - deleteContactById: Deletes a contact from the database based on its phone number.
 *
 * Example Usage:
 *   ContactLoader contactLoader = appDatabase.contactLoader();
 *   long contactId = contactLoader.insertContact(new Contact("John Doe", "123-456-7890"));
 *   List<Contact> allContacts = contactLoader.getAllContacts();
 *   contactLoader.clearAllContacts();
 *   contactLoader.deleteContactById("123-456-7890");
 */
@Dao
public interface ContactLoader {

    /*
     * Inserts a new contact into the database.
     *
     * @param contact - The Contact object to be inserted.
     * @return long - The row ID of the inserted contact.
     */
    @Insert
    long insertContact(Contact contact);

    /*
     * Retrieves all contacts from the database.
     *
     * @return List<Contact> - A list containing all Contact objects in the database.
     */
    @Query("SELECT * FROM contacts")
    List<Contact> getAllContacts();

    /*
     * Deletes all contacts from the database.
     */
    @Query("DELETE FROM contacts")
    void clearAllContacts();

    /*
     * Deletes a contact from the database based on its phone number.
     *
     * @param phoneNumber - The phone number of the contact to be deleted.
     */
    @Query("DELETE FROM contacts WHERE phoneNumber = :phoneNumber")
    void deleteContactById(String phoneNumber);
}

