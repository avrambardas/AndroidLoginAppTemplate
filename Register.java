package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    EditText mFullName, mEmail, mPassword, mConfirmPassword;
    Button mRegisterBtn;
    TextView gotoLogin;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.password2);
        mRegisterBtn = findViewById(R.id.RegisterButton);
        gotoLogin = findViewById(R.id.LogintextView);

        fAuth = FirebaseAuth.getInstance();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // extract data from the form
                String Name = mFullName.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String confPassword = mConfirmPassword.getText().toString();

                // validate data
                if (Name.isEmpty()){
                    mFullName.setError("Name is required");
                    mFullName.requestFocus();
                    return;
                }

                if (email.isEmpty()){
                    mEmail.setError("email is required");
                    mEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmail.setError("Please provide valid email");
                    mEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()){
                    mPassword.setError("password is required");
                    mPassword.requestFocus();
                    return;
                }

                if (confPassword.isEmpty()){
                    mConfirmPassword.setError("confirmation is required");
                    mConfirmPassword.requestFocus();
                    return;
                }

                if (!password.equals(confPassword)){
                    mConfirmPassword.setError("Passwords do not match");
                    mConfirmPassword.requestFocus();
                }

                // validate data message
                Toast.makeText(Register.this, "Data Validated", Toast.LENGTH_LONG).show();

                // sign user in the database
                fAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // if the user account is created successfully, send him to main
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // in case it doesn't have an account, go to login activity
        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }

    // if the user has already logged in before, it goes directly to main
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}