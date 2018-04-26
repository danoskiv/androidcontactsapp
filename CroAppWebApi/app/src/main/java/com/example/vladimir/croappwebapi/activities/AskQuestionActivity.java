package com.example.vladimir.croappwebapi.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vladimir.croappwebapi.R;
import com.example.vladimir.croappwebapi.SimpleTextView;
import com.example.vladimir.croappwebapi.database.DbHelper;
import com.example.vladimir.croappwebapi.models.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AskQuestionActivity extends AppCompatActivity {

    private static final String TAG = "AskQuestioActivity";
    public static final String MyPREFERENCES = "MyPrefs" ;
    private DbHelper dbHelper;
    private RadioButton mAnswer1, mAnswer2, mAnswer3;
    private RadioGroup mGroup;
    private Button mSubmit;
    private TextView mQuestion;
    private SharedPreferences sharedPreferences;
    private boolean loggedIn;
    private int DATABASE_VERSION,pid,id;
    private String name, username;
    Question question, correctAnswer;
    List<Question> answers;
    Question[] questionAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        answers = new ArrayList<>();

        pid = this.getIntent().getIntExtra("PID", 1);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        DATABASE_VERSION = sharedPreferences.getInt("count", 2);
        dbHelper = new DbHelper(this, DATABASE_VERSION);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){
            // 6.5inch device or bigger
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            SimpleTextView.setGlobalSize(15);
        }else{
            // smaller device
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        loggedIn = sharedPreferences.getBoolean("loggedIn", false);
        name = sharedPreferences.getString("name", "");
        username = sharedPreferences.getString("username", "");
        id = sharedPreferences.getInt("id", 1);
        if(loggedIn)
        {
            //user is logged in!
            initViewsAndListeners();

            question = dbHelper.getRandomQuestion(pid);
            mQuestion.setText(question.getBody());
            answers = dbHelper.getAllAnswers(question.getQid(), question.getPid());
            questionAnswers = new Question[answers.size()];

            for (int i = 0; i < answers.size(); i++) {
                questionAnswers[i] = new Question();
                questionAnswers[i].setQid(answers.get(i).getQid());
                questionAnswers[i].setAid(answers.get(i).getAid());
                questionAnswers[i].setBody(answers.get(i).getBody());
                questionAnswers[i].setCorrect(answers.get(i).getCorrect());
            }

            for (int i = 0; i < answers.size(); i++) {

                if(questionAnswers[i].getCorrect() == 1)
                {
                    correctAnswer = questionAnswers[i];
                    if(i == (answers.size() - 1))
                    {
                        TextView randomView = getRandomView();

                        if (randomView == mAnswer1) {
                            mAnswer1.setText(correctAnswer.getBody());
                            mAnswer2.setText(questionAnswers[answers.size() - i - 1].getBody());
                            mAnswer3.setText(questionAnswers[answers.size() - i].getBody());
                        } else if (randomView == mAnswer2) {
                            mAnswer2.setText(correctAnswer.getBody());
                            mAnswer1.setText(questionAnswers[answers.size() - i - 1].getBody());
                            mAnswer3.setText(questionAnswers[answers.size() - i].getBody());
                        } else if (randomView == mAnswer3) {
                            mAnswer3.setText(correctAnswer.getBody());
                            mAnswer2.setText(questionAnswers[answers.size() - i - 1].getBody());
                            mAnswer1.setText(questionAnswers[answers.size() - i].getBody());
                        }
                    }
                    else if(i == 0)
                    {
                        TextView randomView = getRandomView();

                        if (randomView == mAnswer1) {
                            mAnswer1.setText(correctAnswer.getBody());
                            mAnswer2.setText(questionAnswers[answers.size() - 2].getBody());
                            mAnswer3.setText(questionAnswers[answers.size() - 1].getBody());
                        } else if (randomView == mAnswer2) {
                            mAnswer2.setText(correctAnswer.getBody());
                            mAnswer1.setText(questionAnswers[answers.size() - 2].getBody());
                            mAnswer3.setText(questionAnswers[answers.size() - 1].getBody());
                        } else if (randomView == mAnswer3) {
                            mAnswer3.setText(correctAnswer.getBody());
                            mAnswer2.setText(questionAnswers[answers.size() - 1].getBody());
                            mAnswer1.setText(questionAnswers[answers.size() - 2].getBody());
                        }
                    }
                    else if(i == 1)
                    {
                        TextView randomView = getRandomView();

                        if (randomView == mAnswer1) {
                            mAnswer1.setText(correctAnswer.getBody());
                            mAnswer2.setText(questionAnswers[0].getBody());
                            mAnswer3.setText(questionAnswers[answers.size() - 1].getBody());
                        } else if (randomView == mAnswer2) {
                            mAnswer2.setText(correctAnswer.getBody());
                            mAnswer1.setText(questionAnswers[0].getBody());
                            mAnswer3.setText(questionAnswers[answers.size() - 1].getBody());
                        } else if (randomView == mAnswer3) {
                            mAnswer3.setText(correctAnswer.getBody());
                            mAnswer2.setText(questionAnswers[0].getBody());
                            mAnswer1.setText(questionAnswers[answers.size() - 1].getBody());
                        }
                    }
                }
            }
        }
        else
        {
            //user is not logged in!
        }

    }

    private void initViewsAndListeners() {

        mQuestion = findViewById(R.id.question_tv);
        mSubmit = findViewById(R.id.submit_answer);
        mAnswer1 =  findViewById(R.id.answer_1);
        mAnswer2 = findViewById(R.id.answer_2);
        mAnswer3 = findViewById(R.id.answer_3);
        mGroup = findViewById(R.id.radio_group);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.submit_answer)
                {
                    submitAnswer();
                }
            }
        });

    }

    private void submitAnswer() {
        boolean c = false;
        if (validateForm()) {
            int selectedId = mGroup.getCheckedRadioButtonId();

            String radio1 = mAnswer1.getText().toString();
            String radio2 = mAnswer2.getText().toString();
            String radio3 = mAnswer3.getText().toString();

            if (mAnswer1.getId() == selectedId) {
                if (radio1.equals(correctAnswer.getBody())) {
                    c = true;
                }
            } else if (mAnswer2.getId() == selectedId) {
                if (radio2.equals(correctAnswer.getBody())) {
                    c = true;
                }
            } else if (mAnswer3.getId() == selectedId) {
                if (radio3.equals(correctAnswer.getBody())) {
                    c = true;
                }
            }

            if(c) {
                Intent intent = new Intent(this, ContactActivity.class);
                intent.putExtra("DATABASE_VERSION", DATABASE_VERSION);
                intent.putExtra("PID", pid);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "Обидете се повторно", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AskQuestionActivity.class);
                intent.putExtra("PID", pid);
                startActivity(intent);
            }
        }
    }

    private TextView getRandomView() {

        int random = new Random().nextInt(3);
        String[] array = new String[] {"tv1", "tv2", "tv3"};

        String tv = array[random];
        if(tv == "tv1")
        {
            return mAnswer1;
        }
        else if (tv == "tv2")
        {
            return mAnswer2;
        }
        else
        {
            return mAnswer3;
        }
    }

    private boolean validateForm()
    {
        boolean result = true;

        if(mGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(this,"Одберете еден одговор", Toast.LENGTH_LONG).show();

            result = false;
        }

        return result;
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
