package com.example.android_quanlychitieu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class Singup extends AppCompatActivity {
    Button editsigup;
    EditText editName,editPass,editPassconfi;
    TextView resendTextView;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        editName=findViewById(R.id.signup_editname);
        editPass=findViewById(R.id.signup_editpass);
        editPassconfi=findViewById(R.id.signup_editpassconfi);
        database=new Database(this);
         resendTextView = findViewById(R.id.text_resend1);
        resendTextView.setOnClickListener(v -> {
            finish();
        });

        editsigup=findViewById(R.id.btn_signup);

        editsigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editName.getText().toString().trim();
                String password = editPass.getText().toString().trim();
                String passcofi = editPassconfi.getText().toString().trim();


                if (!isValidName(username)) {
                    Toast.makeText(Singup.this, "Invalid name. Use 6 characters .", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidPassword(password)) {
                    Toast.makeText(Singup.this, "Invalid password. Use 6 characters .", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidPassword(passcofi)) {
                    Toast.makeText(Singup.this, "Invalid password. Use 6 characters .", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(passcofi)) {
                    Toast.makeText(Singup.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (database.checkName(username)) {
                    Toast.makeText(Singup.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (username.isEmpty() || password.isEmpty()|| passcofi.isEmpty()) {
                    Toast.makeText(Singup.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {

                    boolean isRegistered = database.registerUser(username, password);
                    if (isRegistered) {
                        Toast.makeText(Singup.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Singup.this,Login.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Singup.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }
    private boolean isValidPassword(String password) {
        return password.length() >= 6 ;

    }
    private boolean isValidName(String name) {
        return name.length() >= 6 ;

    }
}