package com.example.vladimir.croappwebapi.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vladimir.croappwebapi.R;
import com.example.vladimir.croappwebapi.database.DbHelper;
import com.example.vladimir.croappwebapi.models.Award;
import com.example.vladimir.croappwebapi.models.Contact;
import com.example.vladimir.croappwebapi.models.Question;
import com.example.vladimir.croappwebapi.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String URL = "http://10.0.2.2/cro/requests/";
    public static final String MyPREFERENCES = "MyPrefs" ;
    private int DATABASE_VERSION, mCount;
    private boolean loggedIn;

    private DbHelper dbHelper;
    private RelativeLayout relativeLayout;
    private NavigationView navView;
    private DrawerLayout mDrawer;
    private ImageView mOpen, mClose;
    private SharedPreferences sharedPreferences;
    private TextInputLayout mUsernameWrapper, mPasswordWrapper;
    private EditText mUsername, mPassword;
    private Button buttonLogin;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean("loggedIn", false);
        String name = sharedPreferences.getString("name", "");
        Log.d(TAG, "name: " + name);
        Log.d(TAG, "loggedIn: " + loggedIn);
        if(loggedIn)
        {
            startActivity(new Intent(MainActivity.this, ChoiceActivity.class));
        }

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        mCount = sharedPreferences.getInt("count", 1);
        DATABASE_VERSION = mCount;
        mDrawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        queue = Volley.newRequestQueue(this);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.syncAll)
                {
                    mCount ++;
                    Log.d(TAG, "COUNT IN ID: " + mCount);
                    requestSyncUsers();
                    requestSyncQuestions();
                    requestSyncAwards();
                    editor.putInt("count", mCount);
                }
                else if(item.getItemId() == R.id.uploadAll)
                {
                    uploadContacts();
                    uploadAwards();
                }

                return false;
            }
        });

        mOpen = findViewById(R.id.open_drawer);

        mOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(Gravity.LEFT);
                relativeLayout = findViewById(R.id.rel_layout);
                mClose = relativeLayout.findViewById(R.id.close_drawer);
                mClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });
            }
        });

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

    @Override
    protected void onPause() {

        super.onPause();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("count", mCount);
        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("count", mCount);
        editor.commit();
    }

    private void loginUser() {
        Log.d(TAG, "sharedCount: " + mCount);
        if(validateForm())
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            dbHelper = new DbHelper(this, mCount);
            String username = mUsername.getText().toString();
            String password = mPassword.getText().toString();
            User user = dbHelper.getUser(username);
            Log.d(TAG, "DATABASE VERSION: " + DATABASE_VERSION);

            if(dbHelper.md5(password,user.getPassword()))
            {
                if(!(user.getId() == 0 || user.getName() == null)){
                    editor.putInt("id", user.getId());
                    editor.putString("username", user.getUsername());
                    editor.putString("name", user.getName());
                    editor.putBoolean("loggedIn", true);
                    editor.commit();

                    Intent intent = new Intent(MainActivity.this, ChoiceActivity.class);
                    intent.putExtra("DATABASE_VERSION", DATABASE_VERSION);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(this, "Обидете се повторно!", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "Погрешно корисничко име / лозинка. Обидете се повторно!", Toast.LENGTH_SHORT).show();
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

    private void uploadAwards() {
        if(hasInternetAccess(this)) {
            if(mCount == 1)
            {
                dbHelper = new DbHelper(getApplicationContext());
            }
            else
            {
                dbHelper = new DbHelper(getApplicationContext(), mCount);
                DATABASE_VERSION = mCount;
            }
            String url = URL + "updateAwards.php";
            long number = dbHelper.getAwardCount();

            for (int i = 1; i <= number; i++) {
                final int finalI = i;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Award award = dbHelper.getAwardById(finalI);
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", String.valueOf(award.getId()));
                        params.put("name", award.getName());
                        params.put("value", String.valueOf(award.getValue()));
                        params.put("category", String.valueOf(award.getPid()));

                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        }
        else {
        Toast.makeText(this, "Потребна е интернет врска за синхронизирање на прашањата.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadContacts() {

        if(hasInternetAccess(this))
        {
            if(mCount == 1)
            {
                dbHelper = new DbHelper(getApplicationContext());
            }
            else
            {
                dbHelper = new DbHelper(getApplicationContext(), mCount);
                DATABASE_VERSION = mCount;
            }
            String url = URL + "updateContacts.php";
            long number = dbHelper.getContactsCount();

            for(int i = 1; i <= number; i++)
            {
                final int finalI = i;
                Contact contact = dbHelper.getContactById(i);
                if(contact.getFlag() == 1)
                {
                    continue;
                }
                else
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "Error: " + error.getMessage());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Contact contact = dbHelper.getContactById(finalI);
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id", String.valueOf(contact.getId()));
                            params.put("name", contact.getName());
                            params.put("email", contact.getEmail());
                            params.put("phone", contact.getNumber());
                            params.put("user_id", String.valueOf(contact.getUserId()));

                            dbHelper.updateContact(contact.getId());

                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        }
        else {
            Toast.makeText(this, "Потребна е интернет врска за синхронизирање на прашањата.", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestSyncAwards() {

        if(hasInternetAccess(this))
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(mCount == 1)
                    {
                        dbHelper = new DbHelper(getApplicationContext());
                    }
                    else
                    {
                        dbHelper = new DbHelper(getApplicationContext(), mCount);
                        DATABASE_VERSION = mCount;
                    }

                    String url = URL + "requestSyncAwards.php";

                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++)
                            {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    int id = jsonObject.getInt("id");
                                    String name = jsonObject.getString("name");
                                    Integer value = jsonObject.optInt("value");
                                    int pid = jsonObject.getInt("pid");
                                    Log.d(TAG, "PID: " + pid);

                                    Award award = new Award(id, name, value, pid);

                                    if(dbHelper.addAward(award))
                                    {
                                        Log.d(TAG, "Sucessful awards!");
                                    } else {
                                        Log.d(TAG, "Unsuccessful awards");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "Error:" + error.getMessage());
                        }
                    });

                    queue.add(jsonArrayRequest);
                }
            }).start();
        }
        else
        {
            Toast.makeText(this, "Потребна е интернет врска за синхронизирање на наградите.", Toast.LENGTH_SHORT).show();
        }

    }

    private void requestSyncQuestions() {

        if(hasInternetAccess(this))
        {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(mCount == 1)
                    {
                        dbHelper = new DbHelper(getApplicationContext());
                    }
                    else
                    {
                        dbHelper = new DbHelper(getApplicationContext(), mCount);
                        DATABASE_VERSION = mCount;
                    }

                    String url = URL + "requestSyncQuestions.php";

                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++)
                            {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    int id = jsonObject.getInt("id");
                                    Integer pid = jsonObject.optInt("pid");
                                    Integer qid = jsonObject.optInt("qid");
                                    Integer aid = jsonObject.optInt("aid");
                                    int correct = jsonObject.getInt("correct");
                                    String body = jsonObject.getString("body");
                                    int status = jsonObject.getInt("status");
                                    String created_at = jsonObject.getString("created_at");
                                    int user_id = jsonObject.optInt("user_id");

                                    Log.d(TAG, "PID: " + pid + "QID: " + qid + "AID: " + aid);

                                    Question question = new Question(id, pid, qid, aid, correct, body, status, created_at, user_id);

                                    if(dbHelper.addQuestion(question))
                                    {
                                        Log.d(TAG, "SUCESSFUL!");
                                    }
                                    else
                                    {
                                        Log.d(TAG, "UNSUCCESSFUL");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "Error:" + error.getMessage());
                        }
                    });

                    queue.add(jsonArrayRequest);
                }
            }).start();
        }
        else
        {
            Toast.makeText(this, "Потребна е интернет врска за синхронизирање на прашањата.", Toast.LENGTH_SHORT).show();
        }


    }

    private void requestSyncUsers() {

        if(hasInternetAccess(this))
        {
            Log.d(TAG, "MCOUNT: " + mCount);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(mCount == 1)
                {
                    dbHelper = new DbHelper(getApplicationContext());
                }
                else
                {
                    dbHelper = new DbHelper(getApplicationContext(), mCount);
                    DATABASE_VERSION = mCount;
                }

                String url = URL + "requestSyncUsers.php";

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++)
                        {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String name = jsonObject.getString("name");
                                String username = jsonObject.getString("username");
                                String password = jsonObject.getString("password");
                                int status = jsonObject.getInt("status");
                                String created_at = jsonObject.getString("created_at");
                                User user = new User(id, name, username, password, status, created_at);
                                if(dbHelper.addUser(user))
                                {
                                    Log.d(TAG, "Successful!");
                                } else {
                                    Log.d(TAG, "Unsucessful!");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error:" + error.getMessage());
                    }
                });

                queue.add(jsonArrayRequest);
            }
        }).start();

        }
        else
        {
            Toast.makeText(this, "Потребна е интернет врска за синхронизирање на корисниците.", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean hasInternetAccess(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

//    public boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null;
//    }

}
