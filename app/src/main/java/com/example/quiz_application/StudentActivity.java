package com.example.quiz_application;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {
    TextView txtQ;
    RadioButton r1,r2,r3,r4;
    RadioGroup rg;
    Button btnNext;
    TextView txtQno;

    ArrayList<Question> list = new ArrayList<>();
    int index = 0, score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        txtQ = findViewById(R.id.txtQuestion);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        r4 = findViewById(R.id.r4);
        rg = findViewById(R.id.radioGroup);
        btnNext = findViewById(R.id.btnNext);
        txtQno = findViewById(R.id.txtQno);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Quiz");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    list.add(ds.getValue(Question.class));
                }
                showQuestion();
            }
            @Override public void onCancelled(DatabaseError error) {}
        });

        btnNext.setOnClickListener(v -> {
            if (rg.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this,"Select answer", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selected = findViewById(rg.getCheckedRadioButtonId());
            if (selected.getText().toString().equals(list.get(index).answer)) {
                score++;
            }

            index++;

            if (index < list.size()) {
                showQuestion();
            } else {
                Intent i = new Intent(this, ResultActivity.class);
                i.putExtra("score", score);
                i.putExtra("total", list.size());
                startActivity(i);
                finish();
            }
        });
    }

    void showQuestion() {
        Question q = list.get(index);

        txtQno.setText("Question " + (index + 1) + " / " + list.size());
        txtQ.setText(q.question);

        r1.setText(q.op1);
        r2.setText(q.op2);
        r3.setText(q.op3);
        r4.setText(q.op4);

        rg.clearCheck();

        if (index == list.size() - 1)
            btnNext.setText("Finish");
        else
            btnNext.setText("Next");
    }

}
