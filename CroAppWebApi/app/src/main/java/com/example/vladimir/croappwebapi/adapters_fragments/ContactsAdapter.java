package com.example.vladimir.croappwebapi.adapters_fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vladimir.croappwebapi.R;
import com.example.vladimir.croappwebapi.models.Contact;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vladimir on 3/28/2018.
 */

public class ContactsAdapter extends ArrayAdapter<Contact> {

    public ContactsAdapter(Context context, List<Contact> contacts)
    {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_contact, parent, false);
        }

        TextView contactName = convertView.findViewById(R.id.contact_name);
        TextView contactEmail = convertView.findViewById(R.id.contact_mail);
        TextView contactPhone = convertView.findViewById(R.id.contact_phone);

        contactName.setText(contact.getName());
        contactEmail.setText(contact.getEmail());
        contactPhone.setText(contact.getNumber());

        return convertView;
    }
}
