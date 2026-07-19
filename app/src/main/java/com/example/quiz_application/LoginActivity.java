package com.example.quiz_application;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    EditText loginEmail, loginPassword;
    Button loginButton;
    TextView registerText;

    FirebaseAuth firebaseAuth;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        loginEmail = findViewById(R.id.LoginEmail);
        loginPassword = findViewById(R.id.Loginpassword);
        loginButton = findViewById(R.id.loginButton);
        registerText = findViewById(R.id.registerText);

        firebaseAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");

        loginButton.setOnClickListener(v -> loginUser());

        registerText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser() {

        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            loginEmail.setError("Enter email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            loginPassword.setError("Enter password");
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        if (user == null) {
                            Toast.makeText(this, "User is null", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String userId = user.getUid();

                        userRef.child(userId).child("role").get()
                                .addOnCompleteListener(roleTask -> {

                                    if (roleTask.isSuccessful()) {

                                        String role = roleTask.getResult()
                                                .getValue(String.class);

                                        if (role == null) role = "user";

                                        Toast.makeText(this,
                                                "Login Successful",
                                                Toast.LENGTH_SHORT).show();

                                        if ("admin".equals(role)) {
                                            startActivity(new Intent(this, MainActivity.class));
                                        } else {
                                            startActivity(new Intent(this, StudentActivity.class));
                                        }

                                        finish();

                                    } else {
                                        Toast.makeText(this,
                                                "Failed to read role",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        Toast.makeText(this,
                                "Login Failed: " +
                                        task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
