package com.example.vladimir.croappwebapi.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vladimir.croappwebapi.R;
import com.example.vladimir.croappwebapi.database.DbHelper;
import com.example.vladimir.croappwebapi.models.Award;

public class AwardActivity extends AppCompatActivity {

    private static final String TAG = "AwardActivity";
    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedPreferences;
    private TextView mAward;
    private FloatingActionButton fab;
    private DbHelper dbHelper;
    private int DATABASE_VERSION, id, pid;
    private String name, username;
    private boolean loggedIn;
    Award award;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        Intent intent = this.getIntent();
        pid = intent.getIntExtra("PID", 1);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        DATABASE_VERSION = sharedPreferences.getInt("count", 2);
        dbHelper = new DbHelper(this, DATABASE_VERSION);
        loggedIn = sharedPreferences.getBoolean("loggedIn", false);
        id = sharedPreferences.getInt("id", 0);
        name = sharedPreferences.getString("name", "");
        username = sharedPreferences.getString("username", "");

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

        if (loggedIn)
        {
            award = dbHelper.getRandomAward(pid);
            dbHelper.decrementValue(award);
            Log.d(TAG, "awardId: " + award.getId());
            Log.d(TAG, "awardName: " + award.getName());
            Log.d(TAG, "awardValue: " + award.getValue());
            displayAward(award);
        }
        else {
            Toast.makeText(this, "Мора да сте најавени за да продолжите.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AwardActivity.this, MainActivity.class));
        }
    }

    private void displayAward(Award award) {
        mAward = findViewById(R.id.award_tv);
        mAward.setText(award.getName());
        fab = findViewById(R.id.startOver);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AwardActivity.this, ChoiceActivity.class);
                intent.putExtra("DATABASE_VERSION", DATABASE_VERSION);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {

        super.onPause();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", id);
        editor.putString("username", username);
        editor.putString("name", name);
        editor.putBoolean("loggedIn", loggedIn);
        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", id);
        editor.putString("username", username);
        editor.putString("name", name);
        editor.putBoolean("loggedIn", loggedIn);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", id);
        editor.putString("username", username);
        editor.putString("name", name);
        editor.putBoolean("loggedIn", loggedIn);
        editor.commit();
    }
}
