package amber.promo.trivia;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HighscoresAdapter extends ArrayAdapter<Highscore> {
    /** Adapter for highscores. */

    private List<Highscore> data;
    private Context context;

    // initiliazes constructor
    public HighscoresAdapter(@NonNull Context context, int resource, ArrayList<Highscore> objects) {
        super(context, resource, objects);
        data = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.entry_highscore, parent, false);
        }

        // set name
        TextView username = convertView.findViewById(R.id.username);
        username.setText(data.get(position).getName());

        // set score
        TextView highscore = convertView.findViewById(R.id.highscore);
        Float highscoreFloat = data.get(position).getHighScore();
        Integer highscoreInt = Math.round(highscoreFloat);
        highscore.setText(Integer.toString(highscoreInt));

        return convertView;
    }
}
