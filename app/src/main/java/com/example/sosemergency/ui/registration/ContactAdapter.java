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

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<ContactModel> contactList;
    private View passIcon;
    private View addIcon;

    public ContactAdapter(List<ContactModel> contactList, View passIcon, View addIcon) {
        this.contactList = contactList;
        this.passIcon = passIcon;
        this.addIcon = addIcon;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
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
        return contactList.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contactNameTextView;
        TextView contactPhoneTextView;
        ImageButton deleteContactButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactNameTextView = itemView.findViewById(R.id.textContactName);
            contactPhoneTextView = itemView.findViewById(R.id.textContactPhone);
            deleteContactButton = itemView.findViewById(R.id.deleteContact);
        }

        public void bind(ContactModel contact) {
            contactNameTextView.setText(contact.getName());
            contactPhoneTextView.setText(contact.getPhone());
        }
    }

    public void removeContact(int position) {
        contactList.remove(position);
        notifyItemRemoved(position);
        updateIconsVisibility();
    }
    private void updateIconsVisibility() {
        if (contactList.size() <= 1) {
            // If no contacts or only one contact is chosen, hide the "Select" icon and show the "Add" icon
            passIcon.setVisibility(View.GONE);
            addIcon.setVisibility(View.VISIBLE);
        } else if (contactList.size() >= 1 && contactList.size() < 4) {
            // If one or more than one contact is chosen but less than 4, show both the "Add" icon and the "Select" icon
            addIcon.setVisibility(View.VISIBLE);
            passIcon.setVisibility(View.VISIBLE);
        } else if (contactList.size() == 4) {
            // If 4 contacts are chosen, hide the "Add" icon and show the "Select" icon
            addIcon.setVisibility(View.GONE);
            passIcon.setVisibility(View.VISIBLE);
        }
    }
}

