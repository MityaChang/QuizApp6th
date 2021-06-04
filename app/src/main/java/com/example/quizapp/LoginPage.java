package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPage extends AppCompatActivity {

    EditText mail, password;
    Button signin, signInGoogle;
    TextView signUp, forgotPass;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        auth = FirebaseAuth.getInstance();
        mail = findViewById(R.id.EmailSignin);
        password = findViewById(R.id.PasswordSignIn);
        signin = findViewById(R.id.signInbtn);
        SignInButton signInGoogle = (SignInButton) findViewById(R.id.googleSignIn);
        signUp = findViewById(R.id.tvsignUp);
        forgotPass = findViewById(R.id.forgotPasstv);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMail = mail.getText().toString();
                String userPass = password.getText().toString();
                //Get text from the user enter
                signInFire(userMail, userPass);
            }
        });

        signInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInGoogle();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);

            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    public void signInFire(String userEmail, String userPass) {
        signin.setClickable(false);
        auth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(LoginPage.this, "Successfully Sing in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginPage.this, "Sign In Failed..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(LoginPage.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void SignInGoogle() {
        //standard google builder method for signing in
        GoogleSignInOptions so = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //Extract the ID Number in a string manner, the user will choose google account to sign in
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this,so);
        signInOpt();
    }

    public void signInOpt(){
        Intent intent = googleSignInClient.getSignInIntent();
        //Request code when retrieving data
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            //Getting the intent Data
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            firesignGoogle(task);
        }
    }

    private void firesignGoogle(Task<GoogleSignInAccount> task){
        try{
            GoogleSignInAccount acc = task.getResult(ApiException.class);
            Toast.makeText(LoginPage.this,"Successfully Sign in",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginPage.this,MainActivity.class);
            startActivity(intent);
            finish();
            //Call another method to link the parameter
            fireGoogleAcc(acc);
        }
        catch (ApiException e){
            e.printStackTrace();
            Toast.makeText(LoginPage.this,"Sign In Failed",Toast.LENGTH_SHORT).show();
        }
    }

    private void fireGoogleAcc(GoogleSignInAccount acc){
        //Identify and access the code on which device we are logging in
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(),null);
        auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    //Receive user info data
                    user.getDisplayName();
                }else{

                }
            }
        });
    }
}