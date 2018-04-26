package com.example.vladimir.croappwebapi.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vladimir.croappwebapi.R;
import com.example.vladimir.croappwebapi.database.DbHelper;
import com.example.vladimir.croappwebapi.models.User;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    public static final String MyPREFERENCES = "MyPrefs" ;
    private DbHelper dbHelper;
    private int DATABASE_VERSION;
    private SharedPreferences sharedPreferences;
    private TextInputLayout mUsernameWrapper, mPasswordWrapper;
    private EditText mUsername, mPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Intent intent = getIntent();
        DATABASE_VERSION = intent.getIntExtra("DATABASE_VERSION", 1);

        initViews();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(R.id.button_login == view.getId())
                {
                    loginUser();
                }
            }
        });

    }

    private void loginUser() {
        if(validateForm())
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            dbHelper = new DbHelper(this, DATABASE_VERSION);
            String username = mUsername.getText().toString();
            String password = mPassword.getText().toString();

            try {
                User user = dbHelper.getUser(username);

                Log.d(TAG, "name: " + user.getName() + "; username: " + user.getUsername() + "; id: " + user.getId());

                editor.putInt("id", user.getId());
                editor.putString("username", user.getUsername());
                editor.putString("name", user.getName());
                editor.putBoolean("loggedIn", true);
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, ChoiceActivity.class);
                intent.putExtra("DATABASE_VERSION", DATABASE_VERSION);
                startActivity(intent);
            } catch (Exception e)
            {
                Toast.makeText(this, "Обидете се повторно!", Toast.LENGTH_SHORT).show();
            }



        }
    }

    private void initViews() {
        buttonLogin = findViewById(R.id.button_login);
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mUsernameWrapper = findViewById(R.id.usernamewrapper);
        mPasswordWrapper = findViewById(R.id.passwordwrapper);
    }

    private boolean validateForm() {
        boolean result = true;
        if(TextUtils.isEmpty(mUsername.getText().toString()))
        {
            mUsernameWrapper.setError("Задолжително поле");
            result = false;
        }
        else
        {
            mUsernameWrapper.setError(null);
        }

        if(TextUtils.isEmpty(mPassword.getText().toString()))
        {
            mPasswordWrapper.setError("Задолжително поле");
            result = false;
        }
        else
        {
            mPasswordWrapper.setError(null);
        }

        return result;
    }
}
