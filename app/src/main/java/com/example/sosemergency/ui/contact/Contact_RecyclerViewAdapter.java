package com.example.sosemergency.ui.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosemergency.R;

import java.util.ArrayList;


public class Contact_RecyclerViewAdapter extends RecyclerView.Adapter<Contact_RecyclerViewAdapter.ViewHolder>{

    // Context is needed to inflate the layout
    Context context;
    // Reference to the ContactFragment to interact with its functions
    ContactFragment contactFragment;
    // List of ContactModel objects to display in the RecyclerView
    ArrayList<ContactModel> contactModels;

    // Constructor for the RecyclerViewAdapter
    public Contact_RecyclerViewAdapter(Context context, ArrayList<ContactModel> contactModels,ContactFragment contactFragment) {
        this.context = context;
        this.contactModels = contactModels;
        this.contactFragment = contactFragment;
    }


    // Inflates the layout for each item in the RecyclerView
    @NonNull
    @Override
    public Contact_RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.contact_item,parent,false);
        return new Contact_RecyclerViewAdapter.ViewHolder(view);
    }

    // Binds data to the views in each RecyclerView item
    @Override
    public void onBindViewHolder(@NonNull Contact_RecyclerViewAdapter.ViewHolder holder, int position) {
        // Set the name and phone number in TextViews
        holder.textContactName.setText(contactModels.get(position).getName());
        holder.textContactPhone.setText(contactModels.get(position).getPhone());
        // Set onClickListener for the delete button
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    // Call the onDeleteContact function in the ContactFragment
                    contactFragment.onDeleteContact(position);
                }
            }
        });
    }

    // Returns the total number of items in the data set
    @Override
    public int getItemCount() {
        return contactModels.size();
    }

    // ViewHolder class to hold references to the views in each RecyclerView item
    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textContactName,textContactPhone;
        ImageButton btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textContactName = itemView.findViewById(R.id.textContactName);
            textContactPhone = itemView.findViewById(R.id.textContactPhone);
            btnDelete = itemView.findViewById(R.id.deleteContact);
        }
    }
}
