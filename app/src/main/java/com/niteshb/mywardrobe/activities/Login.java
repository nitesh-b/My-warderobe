package com.niteshb.mywardrobe.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.niteshb.mywardrobe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {
private Button signupButton, signInButton;
private SignInButton googleSignIn;
private GoogleSignInClient googleSignInClient;
private  int RC_SIGN_IN = 100;
private EditText mEmail, mPassword;
private FirebaseAuth mAuth;
private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signupButton = findViewById(R.id.signup_button);
        googleSignIn = findViewById(R.id.google_sign_in_button);
        googleSignIn.setOnClickListener(this);
        signupButton.setOnClickListener(this);
        mEmail = findViewById(R.id.login_username);
        mPassword= findViewById(R.id.login_password);
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progress_circular);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
googleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    protected void onStart() {
        super.onStart();
       FirebaseUser currentUser = mAuth.getCurrentUser();
          updateUI(currentUser);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup_button:
                signUpButtonClicked(v);
                break;
            case R.id.signin_button:
                performSignIn(v);
                break;
            case R.id.google_sign_in_button:
                googleSignInClicked(v);

        }
    }

    public void performSignIn(View view) {
progressBar.setVisibility(View.VISIBLE);
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user );
                        } else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Login.this, "Sorry! Problem logging in ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void signUpButtonClicked(View view) {
        startActivity(new Intent(this, SignUp.class));
    }

    public void googleSignInClicked(View view){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try{
        GoogleSignInAccount acc = task.getResult(ApiException.class);
            Toast.makeText(this, "Successfully signed in with Google", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }catch (ApiException e){
            Toast.makeText(this, "Sign In with Google Failed", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acc) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                     FirebaseUser user = mAuth.getCurrentUser();
                     updateUI(user);
                }else{
                    Toast.makeText(Login.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user!=null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Plese Log In to continue...", Toast.LENGTH_SHORT).show();
        }

    }

}
