package com.production.achour_ar.phareenquete.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.io.Resources;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.type.Color;
import com.production.achour_ar.phareenquete.R;

import io.opencensus.resource.Resource;

public class LoginAct extends AppCompatActivity implements View.OnClickListener {

    private EditText emailTV, passwordTV;
    private TextView stateTV;
    private Button connectButton;
    private FirebaseAuth mAuth;
    private final String TAG = "LoginAct";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_act);

        initView();
        setListener();
        checkIfSignedIn();

    }

    private void initView() {
        mAuth = FirebaseAuth.getInstance();
        stateTV = findViewById(R.id.stateTV);
        progressBar = findViewById(R.id.progressBar);
        emailTV = findViewById(R.id.emailTV);
        passwordTV = findViewById(R.id.passwordTV);
        connectButton = findViewById(R.id.connectButton);
    }

    private void setListener() {
        connectButton.setOnClickListener(this);
    }

    private void login(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        stateTV.setTextColor(getResources().getColor(R.color.colorAccent));
        stateTV.setText("Connexion en cours...");
        stateTV.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail: Success !");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            updateUI(null);
                        }

                    }
                });
    }

    private void updateUI(Object user) {
        progressBar.setVisibility(View.GONE);
        if (user == null){
            stateTV.setTextColor(getResources().getColor(R.color.colorError));
            stateTV.setText("Echec de la connexion");
            stateTV.setVisibility(View.VISIBLE);
            Snackbar.make(findViewById(android.R.id.content),
                    "Impossible de se connecter. Veuillez réessayer.", Snackbar.LENGTH_LONG).show();
        }
        else {
            Log.d(TAG, "updateUI: SIGNED IN - "+((FirebaseUser)user).getUid());
            stateTV.setTextColor(getResources().getColor(R.color.colorSuccess));
            stateTV.setText("Connecté avec succès");
            stateTV.setVisibility(View.VISIBLE);
            startActivity(new Intent(LoginAct.this, HomeAct.class));
            finish();
        }
    }

    private boolean dataValid(String email, String password) {
        boolean correct = true;
        if (email.equals("")){
            correct = false;
            emailTV.setError("Email obligatoire");
            emailTV.requestFocus();
        }
        if (password.equals("")){
            correct = false;
            passwordTV.setError("Mot de passe obligatoire");
            passwordTV.requestFocus();
        }
        return correct;
    }

    private void checkIfSignedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!= null){
            updateUI(currentUser);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.connectButton:
                String email = emailTV.getText().toString();
                String password = passwordTV.getText().toString();
                if (dataValid(email, password)){
                    login(email, password);
                }
                break;
        }
    }
}
