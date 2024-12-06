package com.example.android_quanlychitieu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditLoaithu extends AppCompatActivity {

    private Database database;
    private EditText edtName;
    private Button btnSaveLoaithu, btnCancelLoaithu;
    private int userId;
    private int idloaithu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_loaithu);

        database = new Database(this);
        edtName = findViewById(R.id.etLoaiThu);
        btnSaveLoaithu = findViewById(R.id.btnSaveLoathu);
        btnCancelLoaithu = findViewById(R.id.btnCancelLoathu);

        userId = getIntent().getIntExtra("user_id", -1);
        idloaithu = getIntent().getIntExtra("loaithu_id", -1);

        if (userId == -1 || idloaithu == -1) {
            Toast.makeText(this, "Không xác định được thông tin người dùng hoặc loại thu!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String loaithuName = getIntent().getStringExtra("loaithu_name");
        if (loaithuName != null) {
            edtName.setText(loaithuName);
        }
        //Sự kiện
        btnSaveLoaithu.setOnClickListener(v -> {
            String loathuName = edtName.getText().toString().trim();
            if (loathuName.isEmpty()) {
                edtName.setError("Tên loại thu không được để trống!");
                edtName.requestFocus();
                return;
            }

            boolean isTaskUpdated = database.updateLoaiThu(idloaithu, loathuName, userId);
            if (isTaskUpdated) {
                Toast.makeText(this, "Cập nhật loại thu thành công!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("user_id", userId);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Cập nhật loại thu thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelLoaithu.setOnClickListener(v -> finish());
    }
}
