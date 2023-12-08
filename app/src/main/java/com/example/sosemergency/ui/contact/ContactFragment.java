package com.example.sosemergency.ui.contact;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

public class ContactFragment extends Fragment {

    // Constants for request codes
    private static final int REQUEST_READ_CONTACTS_PERMISSION = 1;
    private static final int REQUEST_CONTACT = 2;

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
        // Add new contact button click listener
        CardView addNewContactCard = binding.getRoot().findViewById(R.id.addNewContactCard);
        // Check if the max 4 contacts reached and disable the button
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
        // Check if the max 4 contacts reached
        if (contactsNumber() == 4) {
            displayToastMessage("You can only add 4 trusted contacts");
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
        Log.d("Delete contact test", "onDeleteContact: position="+position);
        contactsModels.remove(position);
        updateAdapter();
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
        // Add the new ContactModel to the list
        contactsModels.add(newContact);
        // Notify the adapter about the data change
        updateAdapter();
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
        String[] contactsName = getResources().getStringArray(R.array.contact_name);
        String[] contactsPhone = getResources().getStringArray(R.array.contact_phone);

        for (int i = 0; i < contactsName.length; i++) {
            contactsModels.add(new ContactModel(contactsName[i], contactsPhone[i]));
        }
    }

    // Check if the max 4 contacts reached and disable the button
    public void toggleAddNewContactCardStatus() {
        CardView addNewContactCard = binding.getRoot().findViewById(R.id.addNewContactCard);
        addNewContactCard.setEnabled(contactsNumber() != 4);
    }

}