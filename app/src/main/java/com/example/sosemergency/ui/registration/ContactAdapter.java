package com.example.sosemergency.ui.registration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosemergency.R;
import com.example.sosemergency.ui.contact.ContactModel;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<ContactModel> contactList;

    public ContactAdapter(List<ContactModel> contactList) {
        this.contactList = contactList;
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
    }
}

