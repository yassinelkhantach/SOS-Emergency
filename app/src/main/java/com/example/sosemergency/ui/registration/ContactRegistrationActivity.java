package com.example.sosemergency.ui.registration;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosemergency.R;
import com.example.sosemergency.ui.contact.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class ContactRegistrationActivity extends AppCompatActivity {
    private static final int RESULT_PICK_CONTACT = 1;

    ImageView addIcon;
    TextView textChoose;
    RecyclerView contactRecyclerView;
    ContactAdapter contactAdapter;

    List<ContactModel> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_registration);

        addIcon = findViewById(R.id.addIcon);
        textChoose = findViewById(R.id.textChoose);
        contactRecyclerView = findViewById(R.id.contactRecyclerView);

        // Initialize your RecyclerView adapter (assuming you have a custom adapter)
        contactAdapter = new ContactAdapter(contactList);
        contactRecyclerView.setAdapter(contactAdapter);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(in, RESULT_PICK_CONTACT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Toast.makeText(this, "Failed To pick contact", Toast.LENGTH_SHORT).show();
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try {
            String contactName = null;
            String contactPhone = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                contactName = cursor.getString(nameIndex);
                contactPhone = cursor.getString(phoneIndex);

                // Create a new ContactModel
                ContactModel contact = new ContactModel(contactName, contactPhone);

                // Add the picked contact to the list
                contactList.add(contact);

                // Notify the adapter of the data set change
                contactAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }
}
