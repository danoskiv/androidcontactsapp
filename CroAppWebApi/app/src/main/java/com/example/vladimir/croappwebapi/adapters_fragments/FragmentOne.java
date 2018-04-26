package com.example.vladimir.croappwebapi.adapters_fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vladimir.croappwebapi.R;
import com.example.vladimir.croappwebapi.activities.AskQuestionActivity;
import com.example.vladimir.croappwebapi.activities.ChoiceActivity;
import com.example.vladimir.croappwebapi.activities.MainActivity;
import com.example.vladimir.croappwebapi.database.DbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vladimir on 3/23/2018.
 */

public class FragmentOne extends Fragment {

    private static final String TAG = "FragmentOne";
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private Spinner spinner;
    private DbHelper dbHelper;
    private String item, name, username;
    private boolean loggedIn;
    private int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ask_question, container, false);


        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 1);
        loggedIn = sharedPreferences.getBoolean("loggedIn", false);
        name = sharedPreferences.getString("name", "");
        username = sharedPreferences.getString("username", "");
        int DATABASE_VERSION = sharedPreferences.getInt("count", 1);
        if(loggedIn)
        {
            dbHelper = new DbHelper(getActivity(), DATABASE_VERSION);
            List<String> categories;
            categories = dbHelper.getAllCategories();
            spinner = v.findViewById(R.id.spinner_cat);
            final TextView tv = v.findViewById(R.id.askQuestion);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view.getId() == R.id.askQuestion)
                    {
                        int pid = dbHelper.getPidByName(item);
                        Log.d(TAG, "PID ID: " + pid);
                        if (!(dbHelper.checkAward(pid)))
                        {
                            Toast.makeText(getActivity(), "Не постои доволен број на награди за оваа категорија. Одберете друга категорија.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent intent = new Intent(getActivity(), AskQuestionActivity.class);
                            intent.putExtra("PID", pid);
                            startActivity(intent);
                        }
                    }
                }
            });

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    item = adapterView.getItemAtPosition(i).toString();
                    tv.setText("Постави прашање од категорија: " + item);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        else
        {
            Toast.makeText(getContext(), "Мора да сте најавени за да продолжите.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
        }

        return v;
    }
}
