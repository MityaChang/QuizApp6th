package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizPage extends AppCompatActivity {

    TextView time, tvcorrectCount, tvwrongCount;
    TextView tvQn, ansA, ansB, ansC, ansD;
    Button nextQN, finishBtn;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReferenceSecond;

    String quizQn;
    String quizCorrectAns;
    String quizansA;
    String quizansB;
    String quizansC;
    String quizansD;

    //Check the total number of qns
    int qnCount;
    int qnNum = 1;

    String userAns;

    int countCorrect = 0;
    int countWrong = 0;

    CountDownTimer countDownTimer;
    private static final long TOTAL_TIME = 30000;
    Boolean timerContinue;
    long timeLeft = TOTAL_TIME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);

        database = FirebaseDatabase.getInstance();
        //Get reference from database by searching child
        databaseReference = database.getReference().child("Questions");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReferenceSecond = database.getReference();

        time = findViewById(R.id.tvTimeCount);
        tvcorrectCount = findViewById(R.id.tvCorrectCount);
        tvwrongCount = findViewById(R.id.tvWrongCount);
        tvQn = findViewById(R.id.tvQuestion);
        ansA = findViewById(R.id.tvAns1);
        ansB = findViewById(R.id.tvAns2);
        ansC = findViewById(R.id.tvAns3);
        ansD = findViewById(R.id.tvAns4);
        nextQN = findViewById(R.id.nextQNBtn);
        finishBtn = findViewById(R.id.finishGameBtn);
        quizQNs();

        nextQN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
                quizQNs();
            }
        });
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendScore();
                Intent intent = new Intent(QuizPage.this,ScorePage.class);
                startActivity(intent);
                finish();
            }
        });
        ansA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
                //Assign the item chosen by the user to a String
                userAns = "a";
                //Check the ans correct or not by retrieving from the database correct ans
                if (quizCorrectAns.equals(userAns)) {
                    ansA.setBackgroundColor(Color.GREEN);
                    countCorrect++;
                    tvcorrectCount.setText("" + countCorrect);
                } else {
                    ansA.setBackgroundColor(Color.RED);
                    countWrong++;
                    tvwrongCount.setText("" + countWrong);
                    findAns();
                }
            }
        });
        ansB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
                userAns = "b";
                //Check the ans correct or not by retrieving from the database correct ans
                if (quizCorrectAns.equals(userAns)) {
                    ansB.setBackgroundColor(Color.GREEN);
                    countCorrect++;
                    tvcorrectCount.setText("" + countCorrect);
                } else {
                    ansB.setBackgroundColor(Color.RED);
                    countWrong++;
                    tvwrongCount.setText("" + countWrong);
                    findAns();
                }
            }
        });
        ansC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
                userAns = "c";
                //Check the ans correct or not by retrieving from the database correct ans
                if (quizCorrectAns.equals(userAns)) {
                    ansC.setBackgroundColor(Color.GREEN);
                    countCorrect++;
                    tvcorrectCount.setText("" + countCorrect);
                } else {
                    ansC.setBackgroundColor(Color.RED);
                    countWrong++;
                    tvwrongCount.setText("" + countWrong);
                    findAns();
                }
            }
        });
        ansD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
                userAns = "d";
                //Check the ans correct or not by retrieving from the database correct ans
                if (quizCorrectAns.equals(userAns)) {
                    ansD.setBackgroundColor(Color.GREEN);
                    countCorrect++;
                    tvcorrectCount.setText("" + countCorrect);
                } else {
                    ansD.setBackgroundColor(Color.RED);
                    countWrong++;
                    tvwrongCount.setText("" + countWrong);
                    findAns();
                }
            }
        });
    }

    //Assign data when pulling from database
    public void quizQNs() {
        //The timer will start whenever the game method is called
        startTimer();
        //Cancel the BG Colour when user pick another option
        ansA.setBackgroundColor(Color.WHITE);
        ansB.setBackgroundColor(Color.WHITE);
        ansC.setBackgroundColor(Color.WHITE);
        ansD.setBackgroundColor(Color.WHITE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Provide the child number of the parent qns count
                qnCount = (int) snapshot.getChildrenCount();
                //Get the qn value first, JSon data will be converted to string
                quizQn = snapshot.child(String.valueOf(qnNum)).child("q").getValue().toString();
                quizansA = snapshot.child(String.valueOf(qnNum)).child("a").getValue().toString();
                quizansB = snapshot.child(String.valueOf(qnNum)).child("b").getValue().toString();
                quizansC = snapshot.child(String.valueOf(qnNum)).child("c").getValue().toString();
                quizansD = snapshot.child(String.valueOf(qnNum)).child("d").getValue().toString();
                quizCorrectAns = snapshot.child(String.valueOf(qnNum)).child("answer").getValue().toString();

                tvQn.setText(quizQn);
                //Print the options for textview
                ansA.setText(quizansA);
                ansB.setText(quizansB);
                ansC.setText(quizansC);
                ansD.setText(quizansD);

                if (qnNum < qnCount) {
                    qnNum++;
                } else {
                    Toast.makeText(QuizPage.this, "End of the questions", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizPage.this, "Sorry,an error has occured", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void findAns() {
        if (quizCorrectAns.equals("a")) {
            ansA.setBackgroundColor(Color.GREEN);
        } else if (quizCorrectAns.equals("b")) {
            ansB.setBackgroundColor(Color.GREEN);
        } else if (quizCorrectAns.equals("c")) {
            ansC.setBackgroundColor(Color.GREEN);
        } else if (quizCorrectAns.equals("d")) {
            ansD.setBackgroundColor(Color.GREEN);
        }
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Time is counting down on each interval 1000
                timeLeft = millisUntilFinished;
                updateCountDown();
            }

            @Override
            public void onFinish() {
                //if the time interval finish or reach 0
                timerContinue = false;
                pauseTimer();
                tvQn.setText("Sorry! Time is up");
            }
        }.start();
        timerContinue = true;
    }

    public void resetTimer() {
        timeLeft = TOTAL_TIME;
        updateCountDown();
    }

    public void updateCountDown() {
        int sec = (int) (timeLeft / 1000) % 60;
        time.setText(""+sec);
    }

    public void pauseTimer(){
        countDownTimer.cancel();
        timerContinue = false;
    }

    public void sendScore(){
        String userUID = user.getUid();
        //Send user score to the user database
        databaseReferenceSecond.child("scores").child(userUID).child("correct").setValue(countCorrect).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(QuizPage.this, "scores sent successfully",Toast.LENGTH_LONG).show();
            }
        });
        databaseReferenceSecond.child("scores").child(userUID).child("wrong").setValue(countWrong).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(QuizPage.this, "scores for wrong sent successfully",Toast.LENGTH_LONG).show();
            }
        });
    }
}