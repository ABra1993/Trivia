package amber.promo.trivia;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements TriviaHelper.Callback {
    /** This activity display's the questions. */

    // initializes properties
    private int[] options = new int[]{R.id.optionA, R.id.optionB, R.id.optionC, R.id.optionD};
    private String[] optionsString = new String[]{"optionA", "optionB", "optionC", "optionD"};
    private static int MIN = 0;
    private static int MAX = 4;
    private int optionCorrectAnswer;
    private String correctAnswerString;
    private int counter;

    private int questionCounter = 1;
    private int questionInterval = 5;
    private float countCorrect;
    private float countIncorrect;

    private RandomQuestion storeQuestion;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // get current user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            username = encode(user.getEmail());
        }

        if (savedInstanceState != null) {

            // set question
            TextView question = findViewById(R.id.question);
            question.setText(savedInstanceState.getString("question"));

            // set answers
            for (int i = 0; i < MAX; i ++) {
                TextView answersView = findViewById(options[i]);
                answersView.setText(savedInstanceState.getString(optionsString[i]));
            }

            // set counter in view
            TextView questionCount = findViewById(R.id.questionCount);
            questionCount.setText(getString(R.string.questionCount,
                    Integer.toString(savedInstanceState.getInt("questionNum")),
                    Integer.toString(questionInterval)));

            // set correct answer
            correctAnswerString = savedInstanceState.getString("correctAnswer");

            // set counter
            questionCounter = savedInstanceState.getInt("questionNum");

            // set number of correct and incorrect question
            countCorrect = savedInstanceState.getFloat("correct");
            countIncorrect = savedInstanceState.getFloat("incorrect");

        } else {
            // initiates counter
            questionCounter = 1;

            // sets question and correct multiple choice answer
            TriviaHelper triviaHelper = new TriviaHelper(this);
            triviaHelper.getNextQuestion(this);

            // set counter in view
            TextView questionCount = findViewById(R.id.questionCount);
            questionCount.setText(getString(R.string.questionCount,
                    Integer.toString(questionCounter), Integer.toString(questionInterval)));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // store question
        TextView question = findViewById(R.id.question);
        outState.putString("question", question.getText().toString());

        // store answers
        String[] answers = new String[4];
        for (int i = 0; i < MAX; i++) {
            TextView answersView = findViewById(options[i]);
            answers[i] = answersView.getText().toString();
            outState.putString(optionsString[i], answers[i]);
        }

        // store question number
        outState.putInt("questionNum", questionCounter);

        // store correct question
        outState.putString("correctAnswer", storeQuestion.getCorrectAnswer());

        // save number of correct and incorrect questions
        outState.putFloat("correct", countCorrect);
        outState.putFloat("incorrect", countIncorrect);
    }

    @Override
    public void gotRandomQuestion(RandomQuestion randomQuestion) {

        // stores question
        storeQuestion = randomQuestion;

        // sets question
        TextView question = findViewById(R.id.question);
        question.setText(randomQuestion.getQuestion());

        // chooses option (A, B, C or D) for correct answer
        Random r = new Random();
        optionCorrectAnswer = r.nextInt(MAX - 1 - MIN + 1) + MIN;
        correctAnswerString = randomQuestion.getCorrectAnswer();

        // set correct answer
        TextView correctAnswer = findViewById(options[optionCorrectAnswer]);
        correctAnswer.setText(randomQuestion.getCorrectAnswer());

        // sets rest of possible answers
        counter = 0;
        String[] answers = randomQuestion.getAnswers();
        for (int i = 0; i < MAX; i++) {
            if (i != optionCorrectAnswer) {
                TextView answer = findViewById(options[i]);
                answer.setText(answers[counter]);
                counter++;
            }
        }

        // sets counter
        TextView questionCount = findViewById(R.id.questionCount);
        questionCount.setText(getString(R.string.questionCount,
                Integer.toString(questionCounter), Integer.toString(questionInterval)));
    }

    @Override
    public void gotRandomQuestionError(String message) {
        // creates toast
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), message, duration);
        toast.show();
        System.out.println(message);
    }

    public void AnswerClicked(View view) {

        questionCounter++;

        // checks given answer
        String answerGiven = "";
        Button option;

        switch (view.getId()) {
            case R.id.optionA:
                option = findViewById(R.id.optionA);
                answerGiven = option.getText().toString();
                break;
            case R.id.optionB:
                option = findViewById(R.id.optionB);
                answerGiven = option.getText().toString();
                break;
            case R.id.optionC:
                option = findViewById(R.id.optionC);
                answerGiven = option.getText().toString();
                break;
            case R.id.optionD:
                option = findViewById(R.id.optionD);
                answerGiven = option.getText().toString();
                break;
        }

        // checks whether given answer is correct
        if (answerGiven.equals(correctAnswerString)) {
            countCorrect++;
        } else
            countIncorrect++;

        // resets count when question interval is reached
        if (questionCounter == questionInterval + 1) {
            Toast.makeText(GameActivity.this, getString(R.string.score,
                    Integer.toString(Math.round(countCorrect)),
                    Integer.toString(Math.round(questionInterval))),
                    Toast.LENGTH_SHORT).show();

            Highscore highscore = new Highscore(username, countCorrect, countIncorrect);

            Intent intent = new Intent(GameActivity.this, HighscoreActivity.class);
            intent.putExtra("highscore", highscore);
            startActivity(intent);
            finish();
        }

        // next question
        TriviaHelper triviaHelper = new TriviaHelper(this);
        triviaHelper.getNextQuestion(this);
    }

    // encodes string by replacing invalid firebase chars
    public String encode(String string) {
        string = string.replace("@", "-");
        string = string.replace(".", "-");
        string = string.replace("$", "-");
        string = string.replace("#", "-");
        string = string.replace("[", "-");
        string = string.replace("]", "-");
        return string;
    }
}
