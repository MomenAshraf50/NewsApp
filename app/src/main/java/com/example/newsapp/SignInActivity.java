package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    TextInputEditText editTextEmail,editTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editTextEmail = findViewById(R.id.sign_in_email_et);
        editTextPassword = findViewById(R.id.sign_in_password_et);



    }

    public void signUp(View view) {
        startActivity(new Intent(SignInActivity.this,SignUp.class));
    }

    public void signIn(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if (email.isEmpty()||password.isEmpty()){
            Toast.makeText(this, "Please fill all data"
                    , Toast.LENGTH_SHORT).show();
            return;
        }

        signInWithEmailAndPassword(email,password);

    }

    private void signInWithEmailAndPassword(String email, String password) {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignInActivity.this, "Sign In success"
                                    , Toast.LENGTH_SHORT).show();
                            editTextEmail.setText("");
                            editTextPassword.setText("");
                            startActivity(new Intent(SignInActivity.this,MainActivity.class));
                        }else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(SignInActivity.this, errorMessage
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}