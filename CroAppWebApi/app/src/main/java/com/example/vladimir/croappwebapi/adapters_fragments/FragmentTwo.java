package com.example.vladimir.croappwebapi.adapters_fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vladimir.croappwebapi.R;
import com.example.vladimir.croappwebapi.activities.MainActivity;
import com.example.vladimir.croappwebapi.database.DbHelper;
import com.example.vladimir.croappwebapi.models.Contact;

import java.util.List;

/**
 * Created by Vladimir on 3/23/2018.
 */

public class FragmentTwo extends Fragment {

    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private DbHelper dbHelper;
    private ListView listView;
    private String name, username;
    private int id;
    private boolean loggedIn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);

        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 1);
        loggedIn = sharedPreferences.getBoolean("loggedIn", false);
        name = sharedPreferences.getString("name", "");
        username = sharedPreferences.getString("username", "");

        if(loggedIn)
        {
            int DATABASE_VERSION = sharedPreferences.getInt("count", 1);
            dbHelper = new DbHelper(getActivity(), DATABASE_VERSION);
            listView = v.findViewById(R.id.list_view);
            List<Contact> contacts = dbHelper.getAllContacts();

            if(contacts.size() > 0)
            {
                TextView tv = v.findViewById(R.id.fragmentText1);
                tv.setText("Преглед на контакти:");
                ContactsAdapter adapter = new ContactsAdapter(getActivity(), contacts);
                listView.setAdapter(adapter);
            }
            else {
                TextView tv = v.findViewById(R.id.fragmentText1);
                tv.setText("Во моментов нема внесени контакти.");
            }
        }
        else
        {
            Toast.makeText(getContext(), "Мора да сте најавени за да продолжите.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
        }

        return v;
    }
}
