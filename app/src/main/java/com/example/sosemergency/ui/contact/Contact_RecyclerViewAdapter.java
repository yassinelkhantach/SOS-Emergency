package com.example.sosemergency.ui.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosemergency.R;

import java.util.ArrayList;


public class Contact_RecyclerViewAdapter extends RecyclerView.Adapter<Contact_RecyclerViewAdapter.ViewHolder>{
    Context context;
    ArrayList<ContactModel> contactModels;

    public Contact_RecyclerViewAdapter(Context context, ArrayList<ContactModel> contactModels) {
        this.context = context;
        this.contactModels = contactModels;
    }

    @NonNull
    @Override
    public Contact_RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.contact_item,parent,false);
        return new Contact_RecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Contact_RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.textContactName.setText(contactModels.get(position).getName());
        holder.textContactPhone.setText(contactModels.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return contactModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textContactName,textContactPhone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textContactName = itemView.findViewById(R.id.textContactName);
            textContactPhone = itemView.findViewById(R.id.textContactPhone);
        }
    }
}
