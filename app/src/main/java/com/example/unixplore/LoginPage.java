package com.example.unixplore;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginPage extends AppCompatActivity {

    private EditText editTextLoginEmail, editTextLoginPwd;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;

    private static final String TAG = "LoginPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        editTextLoginEmail = findViewById(R.id.editText_login_email);
        editTextLoginPwd = findViewById(R.id.editText_login_pwd);
        progressBar = findViewById(R.id.progessBar);

        authProfile = FirebaseAuth.getInstance();

        Button buttonForgotPassword = findViewById(R.id.button_forgot_password);
        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginPage.this, "You can reset your password now!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginPage.this, ForgotPasswordActivity.class));
            }
        });

        // Show Hide Password using Eye Icon
        ImageView imageViewShowHidePwd = findViewById(R.id.imageView_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
        imageViewShowHidePwd.setOnClickListener(v -> {
            if (editTextLoginPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                // If password is visible, then hide it
                editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                // Change Icon
                imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
            } else {
                editTextLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);
            }
        });

        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(view -> {
            String textEmail = editTextLoginEmail.getText().toString().trim();
            String textPwd = editTextLoginPwd.getText().toString().trim();
            if (TextUtils.isEmpty(textEmail)) {
                Toast.makeText(LoginPage.this, "Please enter your Email Address", Toast.LENGTH_SHORT).show();
                editTextLoginEmail.setError("Email Address is required");
                editTextLoginEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                Toast.makeText(LoginPage.this, "Please enter a valid Email Address", Toast.LENGTH_SHORT).show();
                editTextLoginEmail.setError("Valid Email Address is required");
                editTextLoginEmail.requestFocus();
            } else if (TextUtils.isEmpty(textPwd)) {
                Toast.makeText(LoginPage.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                editTextLoginPwd.setError("Password is required");
                editTextLoginPwd.requestFocus();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                loginUser(textEmail, textPwd);
            }
        });
    }

    private void loginUser(String email, String pwd) {
        authProfile.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();
                    if (firebaseUser != null && firebaseUser.isEmailVerified()) {
                        Toast.makeText(LoginPage.this, "You are logged in now", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginPage.this, ProfileActivity.class));
                        finish();
                    } else if (firebaseUser != null) {
                        firebaseUser.sendEmailVerification();
                        // Don't show alert box here
                        //showAlertDialog();
                        authProfile.signOut();
                    }
                } else {
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (Exception e) {
                        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                        Toast.makeText(LoginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
