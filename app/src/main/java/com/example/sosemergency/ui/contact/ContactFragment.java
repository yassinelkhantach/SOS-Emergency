package com.example.sosemergency.ui.contact;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosemergency.R;
import com.example.sosemergency.databinding.FragmentContactBinding;
import com.example.sosemergency.entities.Contact;
import com.example.sosemergency.utils.ContactPersistenceManager;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {
    // Constants for request codes
    private static final int REQUEST_READ_CONTACTS_PERMISSION = 1;
    private static final int REQUEST_CONTACT = 2;
    //max N contact value
    private int MAX_CONTACT_ALLOWED;
    // Intent to pick contacts
    final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
    // View binding
    private FragmentContactBinding binding;
    // List to store contact models
    private ArrayList<ContactModel> contactsModels = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Change ActionBar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_contact);
        //max N contact value
        MAX_CONTACT_ALLOWED = getResources().getInteger(R.integer.max_contacts_allowed);

        // Initialize ViewModel
        ContactViewModel homeViewModel =
                new ViewModelProvider(this).get(ContactViewModel.class);
        // Inflate the layout for this fragment
        binding = FragmentContactBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set up RecyclerView for contacts
        setContactsModels();
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.contactRecyclerView);
        Contact_RecyclerViewAdapter adapter = new Contact_RecyclerViewAdapter(this.getContext(), contactsModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        // Add new contact button click listener and set the text with max contact allowed value
        CardView addNewContactCard = binding.getRoot().findViewById(R.id.addNewContactCard);
        TextView addNewContactCardText = addNewContactCard.findViewById(R.id.maxContactText);
        addNewContactCardText.setText(getString(R.string.max_contact_contact, MAX_CONTACT_ALLOWED));
        // Check if the max N contacts reached and disable the button
        toggleAddNewContactCardStatus();
        addNewContactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddNewContact();
            }
        });
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Method to handle adding a new contact
    public void onAddNewContact() {
        // Check if the max N contacts reached
        if (contactsNumber() == MAX_CONTACT_ALLOWED) {
            displayToastMessage("You can only add "+MAX_CONTACT_ALLOWED+" trusted contacts");
            return;
        }
        // Check for contacts permission
        if (hasContactsPermission()) {
            startActivityForResult(pickContact, REQUEST_CONTACT);
        } else {
            Toast.makeText(requireContext(), "Contacts permission denied", Toast.LENGTH_SHORT).show();
            requestContactsPermission();
        }
    }
    //delete contact
    public void onDeleteContact(int position){
        ContactModel contactToDelete = contactsModels.get(position);
        Log.d("delete contact","delete contact with phone number "+contactToDelete.getPhone());
        ContactPersistenceManager.deleteContact(contactToDelete.getPhone());
        contactsModels.remove(position);
        updateAdapter();
        displayToastMessage(contactToDelete.getName()+" deleted from your trusted contacts");
    }
    public boolean hasContactsPermission()
    {
        return ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED;
    }
    public void requestContactsPermission()
    {
        Log.d("Requesting contacts permission","Requesting contacts permission");
        if (!hasContactsPermission())
        {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_CONTACTS_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
           //permission granted
            Log.d("Permission test", "permission granted");
        }
        else{
            //permission denied
            Log.d("Permission test", "permission denied");
            displayToastMessage("Contacts permission denied");
        }
    }
    // Method to handle the result of contact selection
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check if the result is okay
        if (resultCode != Activity.RESULT_OK) return;

        // Check the request code
        if (requestCode == REQUEST_CONTACT && data != null) {
            // Handle the contact URI and add the new contact
            ContactModel newContact = null;
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri contactUri = clipData.getItemAt(i).getUri();
                    newContact = handleContactUri(contactUri);
                }
            } else {
                Uri contactUri = data.getData();
                newContact = handleContactUri(contactUri);
            }
            // Check if the new contact is not null, then add it
            if (newContact != null) {
                addNewContact(newContact);
            } else {
                displayToastMessage("Error while adding new contact");
            }
        }
    }
    // Method to handle contact URI and retrieve contact information
    public ContactModel handleContactUri(Uri contactUri) {
        ContentResolver cr = this.getContext().getContentResolver();
        Cursor cur = cr.query(contactUri, null, null, null, null);
        ContactModel newContact = null;
        try {
            cur.moveToFirst();
            int contactIdColumnIndex = cur.getColumnIndex(ContactsContract.Contacts._ID);
            if (contactIdColumnIndex != -1) {
                String id = cur.getString(contactIdColumnIndex);
                Cursor pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id},
                        null
                );
                try {
                    pCur.moveToFirst();
                    int contactPhoneColumnIndex = pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int contactNameColumnIndex = pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    // Check if columns exist
                    if (contactPhoneColumnIndex != -1 && contactNameColumnIndex != -1) {
                        String phone = pCur.getString(contactPhoneColumnIndex);
                        String name = pCur.getString(contactNameColumnIndex);
                        // Create a new ContactModel instance with the retrieved information
                        newContact = new ContactModel(name, phone);
                    }
                } finally {
                    pCur.close();
                }
            }
        } finally {
            cur.close();
        }
        return newContact;
    }
    // Method to add a new contact to the list
    public void addNewContact(ContactModel newContact) {
        // Check for duplicates by phone number
        for (ContactModel contactModel : contactsModels) {
            if (contactModel.getPhone().equals(newContact.getPhone())) {
                displayToastMessage(newContact.getName() + " already in your trusted contacts");
                return;
            }
        }
        // Add the new ContactModel to the db
        ContactPersistenceManager.addContact(ContactAdapter.modelToEntity(newContact));
        // Add the new ContactModel to the list
        contactsModels.add(newContact);
        // Notify the adapter about the data change
        updateAdapter();
        //display message
        displayToastMessage(newContact.getName() + " added to your trusted contacts");
    }
    // Method to display toast messages
    public void displayToastMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
    // Method to get the number of contacts
    public int contactsNumber() {
        return this.contactsModels.size();
    }
    // Method to update the adapter
    public void updateAdapter() {
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.contactRecyclerView);
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        // Toggle the status of the add new contact card
        toggleAddNewContactCardStatus();
    }
    // Set up initial contact models from resources
    public void setContactsModels() {
        List<Contact> contacts = ContactPersistenceManager.getContacts();
        if (!contacts.isEmpty()) {
            for (Contact contact:contacts){
                contactsModels.add(ContactAdapter.entityToModel(contact));
            }
        }

    }
    // Check if the max N contacts reached and disable the button
    public void toggleAddNewContactCardStatus() {
        CardView addNewContactCard = binding.getRoot().findViewById(R.id.addNewContactCard);
        addNewContactCard.setEnabled(contactsNumber() != MAX_CONTACT_ALLOWED);
        addNewContactCard.setAlpha(contactsNumber() != MAX_CONTACT_ALLOWED?1F:0.7F);
    }
}