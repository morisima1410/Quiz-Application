package com.example.quiz_application;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView txtResult, txtMessage;
    Button btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        txtResult = findViewById(R.id.txtResult);
        txtMessage = findViewById(R.id.txtMessage);
        btnRetry = findViewById(R.id.btnRetry);

        int score = getIntent().getIntExtra("score",0);
        int total = getIntent().getIntExtra("total",0);

        txtResult.setText("Score: " + score + " / " + total);

        // Optional: message based on score
        if(score == total)
            txtMessage.setText("Excellent! You got all correct.");
        else if(score >= total/2)
            txtMessage.setText("Good! Keep practicing.");
        else
            txtMessage.setText("Better luck next time.");

        // Retry button
        btnRetry.setOnClickListener(v -> {
            startActivity(new Intent(ResultActivity.this, StudentActivity.class));
            finish();
        });
    }
}
