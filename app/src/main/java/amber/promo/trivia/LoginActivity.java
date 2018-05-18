package amber.promo.trivia;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements  View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView email;
    private TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // views
        email = findViewById(R.id.email);
        email.setText("");

        password = findViewById(R.id.password);
        password.setText("");


        // button createAccount
        findViewById(R.id.createAccount).setOnClickListener(this);
        findViewById(R.id.signInAccount).setOnClickListener(this);
        findViewById(R.id.noAccount).setOnClickListener(this);

        // initialize fire base authentication instance
        mAuth = FirebaseAuth.getInstance();
    }

    public void createAccount(final String email, String password) {
        /** Creates account and continues to next activity. */

        if (!validateForm()) {
            return;
        }

        // creates account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // sends feedback
                            Log.d("successful login", "Creates user: " + email);
                            Toast.makeText(LoginActivity.this, "Created account: " + email,
                                    Toast.LENGTH_SHORT).show();
                            // FirebaseUser user = mAuth.getCurrentUser();
                            // continue to GameActivity
                            Intent intent = new Intent(LoginActivity.this, GameActivity.class);
                            startActivity(intent);
                        } else {
                            // sends feedback
                            Log.w("unsuccessful login", "createdUserWithEmail: failure", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signInAccount(final String email, String password) {
        /** Signs user in and continues to next activity. */

        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("successful sign in", "Signed in: " + email);
                            Toast.makeText(LoginActivity.this, "Signed in: " + email,
                                    Toast.LENGTH_SHORT).show();
                            //FirebaseUser user = mAuth.getCurrentUser();

                            // continue to GameActivity
                            Intent intent = new Intent(LoginActivity.this, GameActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("unsuccessful sign in", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void noAccount() {
        /** Initiates next activity when user clicks on "continue without account" */

        Intent intent = new Intent(LoginActivity.this, GameActivity.class);
        startActivity(intent);
    }

    public boolean validateForm() {
        /** Checks whether email and password field are empty. */
        boolean valid = true;

        String emailValid = email.getText().toString();
        if (TextUtils.isEmpty(emailValid)) {
            valid = false;
        }

        String passwordValid = password.getText().toString();
        if (TextUtils.isEmpty(passwordValid)) {
            valid = false;
        }

        if (!valid) {
            Toast.makeText(LoginActivity.this, "Incorrect usage: empty fields",
                    Toast.LENGTH_LONG).show();
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        /** Initiates authentication action when user clicks on buttons. */
        int view = v.getId();
        final String emailUser = email.getText().toString();

        if (view == R.id.createAccount) {
            createAccount(emailUser, password.getText().toString());
        } else if (view == R.id.signInAccount) {
            signInAccount(emailUser, password.getText().toString());
        } else {
            noAccount();
        }
    }

    public void showHighscore(View view) {
        Intent intent = new Intent(LoginActivity.this, HighscoreActivity.class);
        startActivity(intent);
        finish();
    }
}
