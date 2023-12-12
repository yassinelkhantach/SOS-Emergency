package com.example.sosemergency.ui.contact;

import com.example.sosemergency.entities.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter {
    public static ContactModel entityToModel(Contact entity) {
        return new ContactModel(entity.name,entity.phoneNumber);
    }

    public static Contact modelToEntity(ContactModel model) {
        return new Contact(model.name,model.phone);
    }
}

