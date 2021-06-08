package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPage extends AppCompatActivity {

    EditText regiMail, regiPass;
    Button signUp;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        auth  = FirebaseAuth.getInstance();
        regiMail = findViewById(R.id.EmailRegister);
        regiPass = findViewById(R.id.PasswordRegister);
        signUp = findViewById(R.id.regiBtn);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp.setClickable(false);
                String email = regiMail.getText().toString();
                String pass = regiPass.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    regiMail.setError("Required Field!");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    regiPass.setError("Required Field!");
                    return;
                }
                signUpFire(email, pass);
            }
        });
    }

    public void signUpFire(String userEmail, String userPass) {
        auth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(RegisterPage.this, "Successfully Created", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterPage.this, "Resiter Failed.Please try again", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}