package com.example.sosemergency.ui.registration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosemergency.R;
import com.example.sosemergency.ui.contact.ContactModel;

import java.util.List;

/**
 * ContactAdapter - RecyclerView adapter for displaying and managing contact items.
 *
 * This adapter is responsible for handling the UI representation of contact items in a RecyclerView.
 * It binds ContactModel data to the corresponding views and allows the user to remove contacts.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    // List to store contact data
    private List<ContactModel> contactList;
    // Views for updating icon visibility
    private View passIcon;
    private View addIcon;
    private View dotIcon;

    /**
     * Constructor for ContactAdapter.
     *
     * @param contactList List of ContactModel objects to be displayed.
     * @param passIcon    View representing the "Select" icon.
     * @param addIcon     View representing the "Add" icon.
     * @param dotIcon     View representing the "Dot" icon.
     */
    public ContactAdapter(List<ContactModel> contactList, View passIcon, View addIcon, View dotIcon) {
        this.contactList = contactList;
        this.passIcon = passIcon;
        this.addIcon = addIcon;
        this.dotIcon = dotIcon;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the contact item layout
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        // Bind contact data to the ViewHolder
        ContactModel contact = contactList.get(position);
        holder.bind(contact);

        // Set onClickListener for the delete button
        holder.deleteContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeContact(holder.getAdapterPosition());
            }
        });
    }
    @Override
    public int getItemCount() {
        // Return the number of contacts in the list
        return contactList.size();
    }
    /**
     * ContactViewHolder - ViewHolder for displaying individual contact items.
     */
    static class ContactViewHolder extends RecyclerView.ViewHolder {
        // Views within the contact item layout
        TextView contactNameTextView;
        TextView contactPhoneTextView;
        ImageButton deleteContactButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            contactNameTextView = itemView.findViewById(R.id.textContactName);
            contactPhoneTextView = itemView.findViewById(R.id.textContactPhone);
            deleteContactButton = itemView.findViewById(R.id.deleteContact);
        }
        /**
         * Bind method to populate the ViewHolder with contact data.
         *
         * @param contact ContactModel object containing contact information.
         */
        public void bind(ContactModel contact) {
            contactNameTextView.setText(contact.getName());
            contactPhoneTextView.setText(contact.getPhone());
        }
    }
    /**
     * Remove a contact from the list and update UI.
     *
     * @param position Position of the contact to be removed.
     */
    public void removeContact(int position) {
        // Remove the contact from the list
        contactList.remove(position);
        // Notify the adapter of the removal
        notifyItemRemoved(position);
        // Update visibility of icons based on the number of selected contacts
        updateIconsVisibility();
    }
    /**
     * Update the visibility of icons based on the number of selected contacts.
     */
    private void updateIconsVisibility() {
        if (contactList.size() < 1) {
            // If no contacts or only one contact is chosen, hide the "Select" icon and show the "Add" and "Dot" icons
            passIcon.setVisibility(View.GONE);
            addIcon.setVisibility(View.VISIBLE);
            dotIcon.setVisibility(View.VISIBLE);
        } else if (contactList.size() >= 1 && contactList.size() < 4) {
            // If one or more than one contact is chosen but less than 4, show both the "Add" and "Select" icons, hide the "Dot" icon
            addIcon.setVisibility(View.VISIBLE);
            passIcon.setVisibility(View.VISIBLE);
            dotIcon.setVisibility(View.GONE);
        } else if (contactList.size() == 4) {
            // If 4 contacts are chosen, hide the "Add" icon and show the "Select" icon, hide the "Dot" icon
            addIcon.setVisibility(View.GONE);
            passIcon.setVisibility(View.VISIBLE);
            dotIcon.setVisibility(View.GONE);
        }
    }
}

