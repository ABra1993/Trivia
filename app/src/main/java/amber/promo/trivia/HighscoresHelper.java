package amber.promo.trivia;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HighscoresHelper {
    /** The following class retrieves highscores of different users from databse. */

    // initialize properties
    private Highscore highscore;
    private Callback inputActivity;
    private DatabaseReference db;
    private ArrayList<Highscore> highScores = new ArrayList<>();

    // initializes interface
    public interface Callback {
        void gotHighscores(ArrayList<Highscore> highScores);
        void gotError(String message);
    }

    // initializes constructor
    public HighscoresHelper() {
        db = FirebaseDatabase.getInstance().getReference();
    }

    public void postNewHighscores(final Highscore highscore) {

        final String name = highscore.getName();

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // read from database if user already exists
                if (dataSnapshot.hasChild(name)) {
                    Highscore highscorePrev = dataSnapshot.child(name).getValue(Highscore.class);
                    float highScorePrev = highscorePrev.getHighScore();
                    if (highscore.getHighScore() > highScorePrev) {
                        db.child(name).setValue(highscore);
                    }
                // stores highscore in database when user does not exist
                } else {
                    db.child(name).setValue(highscore);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Highscore updated unsuccessful", "Failed to read value.", error.toException());
            }
        });
    }

    public void getHighscores(Callback activity) {

        inputActivity = activity;

        // read from the database
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // clears highscore list
                if (!highScores.isEmpty()) {
                    highScores = new ArrayList<>();
                }
                // fills highscore list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Highscore highScore = snapshot.getValue(Highscore.class);
                    highScores.add(highScore);
                    Log.d("Read", "Value is: " + highScore.getName());
                }

                inputActivity.gotHighscores(highScores);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // log error and pass message to got error method
                Log.w("Read", "Failed to read value.", error.toException());
                inputActivity.gotError(error.getMessage());
            }
        });
    }
}
