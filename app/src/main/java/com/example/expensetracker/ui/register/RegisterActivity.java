package com.example.expensetracker.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.expensetracker.database.ExpenseDatabase;
import com.example.expensetracker.entities.Login;
import com.example.expensetracker.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
                runOnUiThread(() -> Toast.makeText(this, "Username already taken. Please choose another.", Toast.LENGTH_SHORT).show());
                return;
            }

            Login newLogin = new Login(userName, password, firstName, lastName);
            database.loginDAO().insert(newLogin);

            runOnUiThread(() -> {
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            });

        }).start();
    }

    private boolean validate(String firstName, String lastName, String userName, String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || userName.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please complete all requested forms", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!firstName.matches("^[A-Za-z]+$")) {
            Toast.makeText(this,"First name can only contain letters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!lastName.matches("^[A-Za-z]+$")) {
            Toast.makeText(this,"Last name can only contain letters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!userName.matches("^[a-zA-z0-9]+$")) {
            Toast.makeText(this, "Username must only contain letters and numbers", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
