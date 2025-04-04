package com.example.expensetracker.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.expensetracker.database.ExpenseDatabase;
import com.example.expensetracker.entities.Login;
import com.example.expensetracker.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class RegisterActivity  extends AppCompatActivity {

    private EditText newFirstName, newLastName, newUserName, newPassword;
    private Button btnNewRegister;
    private FloatingActionButton close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        newFirstName = findViewById(R.id.firstName);
        newLastName = findViewById(R.id.lastName);
        newUserName = findViewById(R.id.userName);
        newPassword = findViewById(R.id.password);
        btnNewRegister = findViewById(R.id.btnNewRegister);
        close = findViewById(R.id.floatingActionButton);

        btnNewRegister.setOnClickListener(v -> completeRegistration());

        close.setOnClickListener(v -> finish());

    }

    private void completeRegistration() {
        String firstName = newFirstName.getText().toString();
        String lastName = newLastName.getText().toString();
        String userName = newUserName.getText().toString();
        String password = newPassword.getText().toString();

        if (!validate(firstName,lastName,userName,password)) {
            return;
        }

        new Thread(() -> {
            ExpenseDatabase database = ExpenseDatabase.getInstance(getApplicationContext());

            if (database.loginDAO().getUserID(userName) > 0) {
                runOnUiThread(() -> Snackbar.make(findViewById(android.R.id.content), "Username already taken. Please choose another.", Snackbar.LENGTH_LONG).show());
                return;
            }

            Login newLogin = new Login(userName, password, firstName, lastName);
            database.loginDAO().insert(newLogin);

            runOnUiThread(() -> {
                Snackbar.make(findViewById(android.R.id.content), "Registration Successful!", Snackbar.LENGTH_LONG).show();

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }, 1500);


            });

        }).start();
    }

    private boolean validate(String firstName, String lastName, String userName, String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || userName.isEmpty() || password.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Please complete all requested forms", Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (!firstName.matches("^[A-Za-z]+$")) {
            Snackbar.make(findViewById(android.R.id.content),"First name can only contain letters", Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (!lastName.matches("^[A-Za-z]+$")) {
            Snackbar.make(findViewById(android.R.id.content),"Last name can only contain letters", Snackbar.LENGTH_LONG).show();
            return false;
        }

        if(!userName.matches("^[a-zA-z0-9]+$")) {
            Snackbar.make(findViewById(android.R.id.content), "Username must only contain letters and numbers", Snackbar.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
