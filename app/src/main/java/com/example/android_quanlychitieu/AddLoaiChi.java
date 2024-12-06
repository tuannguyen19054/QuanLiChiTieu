package com.example.android_quanlychitieu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddLoaiChi extends AppCompatActivity {
    private Database database;
    private EditText edtName;
    private Button btnSaveLoaithu,btnCancelLoaithu;
    private int userId;  // Thêm biến để lưu user_id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loai_chi);

        database = new Database(this);
        edtName = findViewById(R.id.etLoaiChi);
        btnSaveLoaithu = findViewById(R.id.btnSaveLoaiChi);
        btnCancelLoaithu = findViewById(R.id.btnCancelLoaichi);

        userId = getIntent().getIntExtra("user_id", -1);
        if (userId == -1) {
            Toast.makeText(this, "Không xác định được người dùng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnSaveLoaithu.setOnClickListener(v -> {
            String loaithuname = edtName.getText().toString().trim();

            if (loaithuname.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isTaskAdded = database.insertLoaiChi(loaithuname,  userId);
            if (isTaskAdded) {
                Toast.makeText(this, "Thêm loại chi thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddLoaiChi.this, LoaichiFragment.class);
                intent.putExtra("user_id", userId);  // Truyền user_id khi quay lại TaskActivity
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Thêm loại chi thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
        btnCancelLoaithu.setOnClickListener(v -> finish());
    }
}