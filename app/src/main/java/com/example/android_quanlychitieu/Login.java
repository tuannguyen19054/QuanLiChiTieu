package com.example.android_quanlychitieu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private TextView resendTextView;
    private Button editLogin;
    private EditText editName,editPass;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database=new Database(this);
        editName=findViewById(R.id.login_editname);
        editPass=findViewById(R.id.login_editpass);
         resendTextView = findViewById(R.id.text_resend);
        resendTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Singup.class);
            startActivity(intent);
        });

        editLogin=findViewById(R.id.btn_login);
        editLogin.setOnClickListener(v -> {
            String username = editName.getText().toString().trim();
            String password = editPass.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Vui lòng nhập tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidName(username)) {
                Toast.makeText(Login.this, "Invalid name. Use 6 characters .", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidPassword(password)) {
                Toast.makeText(Login.this, "Invalid password. Use 6 characters .", Toast.LENGTH_SHORT).show();
                return;
            }
            loginUser(username, password);

        });

    }
    private boolean isValidPassword(String password) {
        return password.length() >= 6 ;

    }
    private boolean isValidName(String name) {
        return name.length() >= 6 ;

    }
    public void loginUser(String username, String password) {
        int userId =database.login(username, password);
        if (userId != -1) {
            Intent intent = new Intent(Login.this, tablayout_khoanchi.class);
            intent.putExtra("user_id", userId);
            intent.putExtra("username", username);
            startActivity(intent);
            Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(Login.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

}