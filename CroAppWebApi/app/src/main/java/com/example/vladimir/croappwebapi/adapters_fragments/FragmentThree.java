package com.example.vladimir.croappwebapi.adapters_fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vladimir.croappwebapi.R;
import com.example.vladimir.croappwebapi.activities.MainActivity;
import com.example.vladimir.croappwebapi.database.DbHelper;
import com.example.vladimir.croappwebapi.models.Award;

import java.util.List;

/**
 * Created by Vladimir on 3/23/2018.
 */

public class FragmentThree extends Fragment {

    private DbHelper dbHelper;
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String name, username;
    private int id;
    private boolean loggedIn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.awards_fragment, container, false);

        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 1);
        loggedIn = sharedPreferences.getBoolean("loggedIn", false);
        name = sharedPreferences.getString("name", "");
        username = sharedPreferences.getString("username", "");

        if(loggedIn)
        {
            int DATABASE_VERSION = sharedPreferences.getInt("count", 1);
            dbHelper = new DbHelper(getActivity(), DATABASE_VERSION);
            List<Award> awards = dbHelper.getAllAwards();
            if(awards.size() > 0)
            {
                TextView tv = v.findViewById(R.id.fragmentText1);
                tv.setText("Преглед на наградите:");
                ListView listView = v.findViewById(R.id.listViewAwards);
                AwardsAdapter adapter = new AwardsAdapter(getActivity(), awards);

                listView.setAdapter(adapter);
            }
            else {
                TextView tv = v.findViewById(R.id.fragmentText1);
                tv.setText("Во моментов нема внесени награди.");
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
