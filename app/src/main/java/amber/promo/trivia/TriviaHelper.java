package amber.promo.trivia;

import android.content.Context;
import android.telecom.Call;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.security.auth.callback.Callback;

    public class TriviaHelper implements Response.Listener<JSONArray>, Response.ErrorListener {
    /** Retrieves random questions from JSON array. */

    // initializes interface
    public interface Callback {
        void gotRandomQuestion(RandomQuestion randomQuestion);
        void gotRandomQuestionError(String message);
    }

    // initializes properties
    private Context context;
    private RandomQuestion randomQuestion = new RandomQuestion();
    private Callback inputActivity;
    private int counter = 0;
    private int MAX = 4;
    private List<String> answers = new ArrayList<>();

    // initializes constructor
    public TriviaHelper(Context context) {
        this.context = context;
    }

    public void getNextQuestion(Callback activity) {

        int min = 0;
        int max = 3;
        Random r = new Random();
        counter = r.nextInt(max - min + 1) + min;

        inputActivity = activity;

        // instantiates the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://jservice.io/api/random";

        counter = 0;

        for (int i = 0; i < MAX; i++) {
            // stores API data from a JSON object
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                    url, null, this, this);

            // add the request to the RequestQueue
            queue.add(jsonArrayRequest);
        }
    }

    @Override
    public void onResponse(JSONArray response) {

        // fills RandomQuestion with data from JSON array
        try {

            // get first JSON object in array
            JSONObject jsonObject = response.getJSONObject(0);

            // store JSONObjects with key's "answer" and "question"
            String answer = jsonObject.getString("answer");
            answers.add(answer);

            // increment counter
            counter++;

            if (counter == MAX) {

                // add question
                String question = jsonObject.getString("question");
                randomQuestion.setQuestion(question);

                // add correct answer
                String correctAnswer = jsonObject.getString("answer");
                randomQuestion.setCorrectAnswer(correctAnswer);

                // add answers
                String[] answersArray = answers.toArray(new String[answers.size()]);
                randomQuestion.setAnswers(answersArray);

                // call method gotRandomQuestion
                inputActivity.gotRandomQuestion(randomQuestion);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    // stores error message when request failed
    @Override
    public void onErrorResponse(VolleyError error) {
        inputActivity.gotRandomQuestionError(error.getMessage());
    }
}
