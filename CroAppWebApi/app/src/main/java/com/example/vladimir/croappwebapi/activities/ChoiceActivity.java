package com.example.vladimir.croappwebapi.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.vladimir.croappwebapi.R;
import com.example.vladimir.croappwebapi.adapters_fragments.ViewPagerAdapter;

public class ChoiceActivity extends AppCompatActivity {

    private static final String TAG = "ChoiceActivity";
    public static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences sharedPreferences;
    private int DATABASE_VERSION, id;
    private String name, username;
    private boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        DATABASE_VERSION = sharedPreferences.getInt("count", 2);
        loggedIn = sharedPreferences.getBoolean("loggedIn", false);
        name = sharedPreferences.getString("name", "");
        username = sharedPreferences.getString("username", "");
        id = sharedPreferences.getInt("id", 1);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){
            // 6.5inch device or bigger
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            // smaller device
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if (loggedIn) {
            ViewPager viewPager = findViewById(R.id.container);

            ViewPagerAdapter adapter = new ViewPagerAdapter(this, getSupportFragmentManager());

            viewPager.setAdapter(adapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
            tabLayout.setupWithViewPager(viewPager);

            //Logout FAB
            findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("loggedIn");
                    editor.remove("name");
                    editor.remove("id");
                    editor.remove("username");
                    editor.commit();
                    startActivity(new Intent(ChoiceActivity.this, MainActivity.class));
                    finish();
                }
            });
        } else {
            Toast.makeText(this, "Мора да сте најавени за да продолжите.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ChoiceActivity.this, MainActivity.class));
        }
    }
}