package com.malaika.quizapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvQuestion, tvQuestionCount, tvScore;
    private MaterialButton btnOpt1, btnOpt2, btnOpt3, btnOpt4, btnNext;

    private int score = 0;
    private int currentQuestionIndex = 0;
    private String selectedAnswer = "";

    // Question Data Model Structure
    private static class QuestionModel {
        String question;
        List<String> options;
        String correctAnswer;

        QuestionModel(String question, String[] opts, String correctAnswer) {
            this.question = question;
            this.options = new ArrayList<>();
            Collections.addAll(this.options, opts);
            this.correctAnswer = correctAnswer;
        }
    }

    private List<QuestionModel> questionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvQuestionCount = findViewById(R.id.tvQuestionCount);
        tvScore = findViewById(R.id.tvScore);

        btnOpt1 = findViewById(R.id.btnOpt1);
        btnOpt2 = findViewById(R.id.btnOpt2);
        btnOpt3 = findViewById(R.id.btnOpt3);
        btnOpt4 = findViewById(R.id.btnOpt4);
        btnNext = findViewById(R.id.btnNext);

        btnOpt1.setOnClickListener(this);
        btnOpt2.setOnClickListener(this);
        btnOpt3.setOnClickListener(this);
        btnOpt4.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        initQuestions();
        loadNewQuestion();
    }

    private void initQuestions() {
        questionList.add(new QuestionModel(
                "Which language is officially recommended by Google for Android app development?",
                new String[]{"Python", "Kotlin", "Swift", "PHP"},
                "Kotlin"
        ));
        questionList.add(new QuestionModel(
                "What does SDK stand for in Mobile Development?",
                new String[]{"Software Development Kit", "System Design Key", "Software Data Kernel", "Source Development Kit"},
                "Software Development Kit"
        ));
        questionList.add(new QuestionModel(
                "Which local database system is natively built into Android?",
                new String[]{"MySQL", "MongoDB", "SQLite", "PostgreSQL"},
                "SQLite"
        ));
        questionList.add(new QuestionModel(
                "Which component in Android is used to layout UI elements linearly?",
                new String[]{"RelativeLayout", "LinearLayout", "TableLayout", "FrameLayout"},
                "LinearLayout"
        ));
        questionList.add(new QuestionModel(
                "What is the function of 'SharedPreferences' in Android?",
                new String[]{"Store Key-Value Data", "Render 3D Models", "Handle Network Calls", "Play Audio Files"},
                "Store Key-Value Data"
        ));
        questionList.add(new QuestionModel(
                "Which file contains essential information about your Android application?",
                new String[]{"build.gradle", "MainActivity.java", "AndroidManifest.xml", "styles.xml"},
                "AndroidManifest.xml"
        ));
        questionList.add(new QuestionModel(
                "What is the superclass of all UI widgets in Android?",
                new String[]{"View", "ViewGroup", "Object", "Component"},
                "View"
        ));
        questionList.add(new QuestionModel(
                "Which lifecycle method is called first when an Activity is created?",
                new String[]{"onStart()", "onResume()", "onCreate()", "onLaunch()"},
                "onCreate()"
        ));
        questionList.add(new QuestionModel(
                "What tool is used to build and automate Android project packaging?",
                new String[]{"Maven", "Gradle", "Ant", "CMake"},
                "Gradle"
        ));
        questionList.add(new QuestionModel(
                "Which method is used to launch a new Activity in Android?",
                new String[]{"startActivity()", "openActivity()", "launchIntent()", "runActivity()"},
                "startActivity()"
        ));

        // Shuffle questions randomly
        Collections.shuffle(questionList);
    }

    @Override
    public void onClick(View v) {
        MaterialButton clickedButton = (MaterialButton) v;

        if (clickedButton.getId() == R.id.btnNext) {
            if (selectedAnswer.isEmpty()) {
                Toast.makeText(this, "Please select an answer first!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedAnswer.equals(questionList.get(currentQuestionIndex).correctAnswer)) {
                score++;
                tvScore.setText("Score: " + score);
            }

            currentQuestionIndex++;
            selectedAnswer = "";
            resetOptionStyles();
            loadNewQuestion();

        } else {
            resetOptionStyles();
            selectedAnswer = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.parseColor("#064E3B"));
            clickedButton.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#10B981")));
        }
    }

    private void loadNewQuestion() {
        if (currentQuestionIndex == questionList.size()) {
            finishQuiz();
            return;
        }

        QuestionModel currentQ = questionList.get(currentQuestionIndex);

        // Shuffle options for the current question
        List<String> shuffledOptions = new ArrayList<>(currentQ.options);
        Collections.shuffle(shuffledOptions);

        tvQuestionCount.setText("Question " + (currentQuestionIndex + 1) + "/" + questionList.size());
        tvQuestion.setText(currentQ.question);
        btnOpt1.setText(shuffledOptions.get(0));
        btnOpt2.setText(shuffledOptions.get(1));
        btnOpt3.setText(shuffledOptions.get(2));
        btnOpt4.setText(shuffledOptions.get(3));
    }

    private void resetOptionStyles() {
        int defaultBg = Color.parseColor("#1E293B");
        int defaultStroke = Color.parseColor("#334155");

        MaterialButton[] buttons = {btnOpt1, btnOpt2, btnOpt3, btnOpt4};
        for (MaterialButton btn : buttons) {
            btn.setBackgroundColor(defaultBg);
            btn.setStrokeColor(ColorStateList.valueOf(defaultStroke));
        }
    }

    private void finishQuiz() {
        tvQuestion.setText("Quiz Completed!\n\nFinal Score: " + score + " / " + questionList.size());
        btnOpt1.setVisibility(View.GONE);
        btnOpt2.setVisibility(View.GONE);
        btnOpt3.setVisibility(View.GONE);
        btnOpt4.setVisibility(View.GONE);
        btnNext.setText("Restart Quiz");
        btnNext.setOnClickListener(v -> recreate());
    }
}