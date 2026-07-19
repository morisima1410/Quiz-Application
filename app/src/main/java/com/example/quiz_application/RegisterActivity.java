package com.example.quiz_application;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword;
    Button btnRegister;
    TextView tvLogin;

    FirebaseAuth firebaseAuth;
    DatabaseReference userRef;

    // 🔥 YOUR ADMIN EMAIL
    private static final String ADMIN_EMAIL = "morisimu@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");

        btnRegister.setOnClickListener(v -> registerUser());

        tvLogin.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class))
        );
    }

    private void registerUser() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            etName.setError("Enter Name");
            return;
        }

        if(TextUtils.isEmpty(email)){
            etEmail.setError("Enter Email");
            return;
        }

        if(TextUtils.isEmpty(password)){
            etPassword.setError("Enter Password");
            return;
        }

        if(password.length()<6){
            etPassword.setError("Minimum 6 characters");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(authResult -> {

                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    if(user==null) return;

                    String uid = user.getUid();

                    // SAVE DISPLAY NAME
                    UserProfileChangeRequest profile =
                            new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                    user.updateProfile(profile);

                    // ADMIN / USER ROLE
                    String role = email.equalsIgnoreCase(ADMIN_EMAIL)
                            ? "admin"
                            : "user";

                    UserModel model = new UserModel(name,email,role);

                    userRef.child(uid).setValue(model)
                            .addOnSuccessListener(a -> {

                                Toast.makeText(this,"Registered Successfully",Toast.LENGTH_SHORT).show();

                                // DIRECT LOGIN
                                if(role.equals("admin")){
                                    startActivity(new Intent(this, AddQuestion.class));
                                }else{
                                    startActivity(new Intent(this, MainActivity.class));
                                }

                                finish();
                            });

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show()
                );
    }
}
