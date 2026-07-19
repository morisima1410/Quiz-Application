package com.example.quiz_application;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText q,o1,o2,o3,o4,ans;
    Button btnAdd;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        q = findViewById(R.id.etQuestion);
        o1 = findViewById(R.id.etOp1);
        o2 = findViewById(R.id.etOp2);
        o3 = findViewById(R.id.etOp3);
        o4 = findViewById(R.id.etOp4);
        ans = findViewById(R.id.etAnswer);
        btnAdd = findViewById(R.id.btnAdd);
        ref = FirebaseDatabase.getInstance().getReference("Quiz");

        btnAdd.setOnClickListener(v -> {
            String id = ref.push().getKey();

            ref.child(id).child("question").setValue(q.getText().toString());
            ref.child(id).child("op1").setValue(o1.getText().toString());
            ref.child(id).child("op2").setValue(o2.getText().toString());
            ref.child(id).child("op3").setValue(o3.getText().toString());
            ref.child(id).child("op4").setValue(o4.getText().toString());
            ref.child(id).child("answer").setValue(ans.getText().toString());

            Toast.makeText(this, "Question Added", Toast.LENGTH_SHORT).show();
            q.setText(""); o1.setText(""); o2.setText("");
            o3.setText(""); o4.setText(""); ans.setText("");
        });
    }
}