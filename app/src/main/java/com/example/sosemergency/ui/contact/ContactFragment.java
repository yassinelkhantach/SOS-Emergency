package com.example.sosemergency.ui.contact;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosemergency.R;
import com.example.sosemergency.databinding.FragmentContactBinding;

import java.util.ArrayList;

public class ContactFragment extends Fragment {

    private FragmentContactBinding binding;
    private ArrayList<ContactModel> contactsModels = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Change ActionBar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_contact);

        ContactViewModel homeViewModel =
                new ViewModelProvider(this).get(ContactViewModel.class);

        binding = FragmentContactBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // adding the contacts dynamically to the page using recycler view
        setContactsModels();
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.contactRecyclerView);
        Contact_RecyclerViewAdapter adapter = new Contact_RecyclerViewAdapter(this.getContext(),contactsModels,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        //add new contact button
        CardView addNewContactCard = binding.getRoot().findViewById(R.id.addNewContactCard);
        addNewContactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setContactsModels(){
        String[] contactsName = getResources().getStringArray(R.array.contact_name);
        String[] contactsPhone = getResources().getStringArray(R.array.contact_phone);

        for(int i=0;i< contactsName.length;i++){
            contactsModels.add(new ContactModel(contactsName[i],contactsPhone[i]));
        }
    }


    //delete contact
    public void onDeleteContact(int position){
        Log.d("Delete contact test", "onDeleteContact: position="+position);
    }
}