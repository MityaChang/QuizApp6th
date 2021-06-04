package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScorePage extends AppCompatActivity {

    TextView scoreCorrect, scoreWrong , scoreComment;
    Button btnPlayAgain, btnExit;

    String userCorrect;
    String userWrong;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_page);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("scores");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        scoreCorrect = findViewById(R.id.tvCorrectCountScore);
        scoreWrong = findViewById(R.id.tvWrongCountScore);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnExit = findViewById(R.id.btnExit);
//        scoreComment = findViewById(R.id.tvComment);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //pulling result from database
                String userID = user.getUid();
                userCorrect = snapshot.child(userID).child("correct").getValue().toString();
                userWrong = snapshot.child(userID).child("wrong").getValue().toString();
                scoreCorrect.setText(userCorrect);
                scoreWrong.setText(userWrong);
//                int numCorrect = Integer.parseInt(String.valueOf(scoreCorrect));
//                int numWrong = Integer.parseInt(String.valueOf(scoreWrong));
//                if(numCorrect<2){
//                    scoreComment.setText("Please know your school better");
//                }else if(numCorrect<5){
//                    scoreComment.setText("Hello");
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScorePage.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

}