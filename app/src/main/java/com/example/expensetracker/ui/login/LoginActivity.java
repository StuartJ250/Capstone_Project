package com.example.expensetracker.ui.login;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.MainActivity;
import com.example.expensetracker.R;
import com.example.expensetracker.database.ExpenseDatabase;
import com.example.expensetracker.entities.Login;
import com.example.expensetracker.ui.register.RegisterActivity;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    private EditText xUsername, xPassword;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        xUsername = findViewById(R.id.userName);
        xPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.register);

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        if(preferences.getBoolean("isLoggedIn", false)) {
            navigateToMainActivity();
        }

        btnLogin.setOnClickListener(v -> handleLogin());

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String username = xUsername.getText().toString();
        String password = xPassword.getText().toString();

        new Thread(() ->{
            Login login = ExpenseDatabase.getInstance(getApplicationContext()).loginDAO().loginUser(username, password);
            if (login != null){
                SharedPreferences.Editor editor = getSharedPreferences("user_prefs", MODE_PRIVATE).edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putInt("userID", login.getUserID());
                editor.putString("userName", username);
                editor.putString("firstName", login.getFirstName());
                editor.putString("lastName", login.getLastName());
                boolean isSaved = editor.commit();

                if(isSaved) {
                    System.out.println("DEBUG");
                } else {
                    System.out.println("ERROR");
                }

                SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                String storedUsername = preferences.getString("userName", "Guest");
                // System.out.println("DEBUG: Username saved: " + storedUsername);
                runOnUiThread(() -> {
                    Snackbar.make(findViewById(android.R.id.content), "Login Successful!", Snackbar.LENGTH_LONG).show();

                    new Handler(Looper.getMainLooper()).postDelayed(() ->{
                        navigateToMainActivity();
                    }, 2000);

                });
            }
            else {
                runOnUiThread(() ->
                        Snackbar.make(findViewById(android.R.id.content), "Invalid Username or Password", Snackbar.LENGTH_LONG).show());
            }
        }).start();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
