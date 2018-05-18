package amber.promo.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HighscoreActivity extends AppCompatActivity implements HighscoresHelper.Callback {
    /** The following class displays the highscores activity. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        // retrieves data from previous activity
        Intent intent = getIntent();
        Highscore highscore = (Highscore) intent.getSerializableExtra("highscore");

        HighscoresHelper highscoresHelper = new HighscoresHelper();

        // updates highscore
        if (highscore != null) {
            highscoresHelper.postNewHighscores(highscore);
        }

        // retrieve highscores
        highscoresHelper.getHighscores(this);
    }

    @Override
    public void gotHighscores(ArrayList<Highscore> highScores) {
        // connects listView to adapter
        HighscoresAdapter highscoresAdapter = new HighscoresAdapter(this, R.layout.entry_highscore, highScores);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(highscoresAdapter);
    }

    @Override
    public void gotError(String message) {
        Toast.makeText(HighscoreActivity.this, "Error occurred",
                Toast.LENGTH_SHORT).show();
    }

    public void startNewRound(View view) {
        Intent intent = new Intent(HighscoreActivity.this, GameActivity.class);
        startActivity(intent);
        finish();
    }

    public void returnLogin(View view) {
        Intent intent = new Intent(HighscoreActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
