package com.example.android_quanlychitieu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddLoaiThu extends AppCompatActivity {
    private Database database;
    private EditText edtName;
    private Button btnSaveLoaithu,btnCancelLoaithu;
    private int userId;  // Thêm biến để lưu user_id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loai_thu);

        database = new Database(this);
        edtName = findViewById(R.id.etLoaiThu);
        btnSaveLoaithu = findViewById(R.id.btnSaveLoaithu);
        btnCancelLoaithu = findViewById(R.id.btnCancelLoathu);

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

            boolean isTaskAdded = database.insertLoaiThu(loaithuname,  userId);
            if (isTaskAdded) {
                Toast.makeText(this, "Thêm loại thu thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddLoaiThu.this, LoaithuFragment.class);
                intent.putExtra("user_id", userId);  // Truyền user_id khi quay lại TaskActivity
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Thêm loại thu thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
        btnCancelLoaithu.setOnClickListener(v -> finish());
    }
}