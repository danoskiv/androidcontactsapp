package com.example.vladimir.croappwebapi.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vladimir.croappwebapi.R;
import com.example.vladimir.croappwebapi.database.DbHelper;
import com.example.vladimir.croappwebapi.models.Contact;
import com.example.vladimir.croappwebapi.models.User;

public class ContactActivity extends AppCompatActivity {

    public static final String TAG = "ContactActivity";
    public static final String MyPREFERENCES = "MyPrefs";
    private DbHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private EditText mName, mEmail, mPhone;
    private TextInputLayout mNameWrapper, mEmailWrapper, mPhoneWrapper;
    private CheckBox yes, no;
    private Button mSubmit, close;
    private TextView polisa, polisaPopup;
    private int DATABASE_VERSION, id, pid;
    private String PID,name, username;
    private PopupWindow popupWindow;
    private boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Intent intent = this.getIntent();
        pid = intent.getIntExtra("PID", 1);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        DATABASE_VERSION = sharedPreferences.getInt("count", 2);
        loggedIn = sharedPreferences.getBoolean("loggedIn", false);
        id = sharedPreferences.getInt("id", 0);
        name = sharedPreferences.getString("name", "");
        username = sharedPreferences.getString("username", "");
        dbHelper = new DbHelper(this, DATABASE_VERSION);

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

        if (loggedIn){
            initViewsAndListeners();
        }
        else {
            Toast.makeText(this, "Мора да сте најавени за да продолжите.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ContactActivity.this, MainActivity.class));
        }

    }

    private void initViewsAndListeners() {
        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.mail);
        mPhone = findViewById(R.id.phone);
        mNameWrapper = findViewById(R.id.namewrapper);
        mEmailWrapper = findViewById(R.id.mailwrapper);
        mPhoneWrapper = findViewById(R.id.phonewrapper);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        mSubmit = findViewById(R.id.button_continue);
        polisaPopup = findViewById(R.id.policy);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.button_continue)
                {
                    submitContact();
                }
            }
        });

        polisaPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp();
            }
        });
    }

    private void showPopUp() {
            try {
                // We need to get the instance of the LayoutInflater
                LayoutInflater inflater = (LayoutInflater) ContactActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.popup,
                        (ViewGroup) findViewById(R.id.popup_1));
                layout.setAnimation(AnimationUtils.loadAnimation(this,R.anim.myanim));
                popupWindow = new PopupWindow(layout,1000,600,true);
                popupWindow.setAnimationStyle(R.style.popup_window_animation);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                close = layout.findViewById(R.id.close_popup);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    private void submitContact() {
        if(validateForm())
        {
            if(isAccepted())
            {
                String name = mName.getText().toString();
                String email = mEmail.getText().toString();
                String phone = mPhone.getText().toString();

                int contactId = dbHelper.getLatestContactId();
                Contact contact = new Contact(contactId, name, email, phone, id);
                if(dbHelper.addContact(contact))
                {
                    Intent intent = new Intent(this, AwardActivity.class);
                    intent.putExtra("PID", pid);
                    intent.putExtra("DATABASE_VERSION", DATABASE_VERSION);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, "Неуспешно внесување. Обидете се повторно", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ContactActivity.class);
                    startActivity(intent);
                }
            }
            else
            {
                Toast.makeText(this, "Не можете да продолжите доколку не ја прифаќате согласноста за " +
                        "користење на податоците.", Toast.LENGTH_LONG).show();
            }

        }
    }

    private boolean validateForm()
    {
        boolean result = true;
        if(TextUtils.isEmpty(mName.getText().toString()))
        {
            mNameWrapper.setError("Задолжително поле");
            result = false;
        }
        else
        {
            mNameWrapper.setError(null);
            result = true;
        }

        if(TextUtils.isEmpty(mPhone.getText().toString()))
        {
            mPhoneWrapper.setError("Задолжително поле");
            result = false;
        }
        else
        {
            mPhoneWrapper.setError(null);
            result = true;
        }

        return result;
    }

    private boolean isAccepted()
    {
        boolean accepted = false;

        yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    no.setChecked(false);
                }
            }
        });
        no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    yes.setChecked(false);
                }
            }
        });

        if(yes.isChecked())
        {
            accepted = true;
        }
        else if(no.isChecked())
        {
            accepted = false;
        }

        return accepted;
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

