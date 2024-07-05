package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    // Declare UI elements
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    // Hardcoded credentials for demonstration purposes
    private static final String CORRECT_USERNAME = "user";
    private static final String CORRECT_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);

        // Set click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve username and password input
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Check if the credentials are correct
                if (username.equals(CORRECT_USERNAME) && password.equals(CORRECT_PASSWORD)) {
                    // If correct, navigate to the next activity (e.g., HomeActivity)
                    Intent intent = new Intent(LoginActivity.this, MainTabActivity.class);
                    startActivity(intent);
                    finish(); // Finish the login activity
                } else {
                    // If incorrect, show a toast message
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}