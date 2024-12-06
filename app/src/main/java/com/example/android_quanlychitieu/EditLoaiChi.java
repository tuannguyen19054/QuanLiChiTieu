package com.example.android_quanlychitieu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditLoaiChi extends AppCompatActivity {

    private Database database;
    private EditText edtName;
    private Button btnSaveLoaithu, btnCancelLoaithu;
    private int userId;
    private int idloaithu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_loai_chi);

        database = new Database(this);
        edtName = findViewById(R.id.etLoaiChi);
        btnSaveLoaithu = findViewById(R.id.btnSaveLoaiChi);
        btnCancelLoaithu = findViewById(R.id.btnCancelLoaichi);

        userId = getIntent().getIntExtra("user_id", -1);
        idloaithu = getIntent().getIntExtra("loaichi_id", -1);

        if (userId == -1 || idloaithu == -1) {
            Toast.makeText(this, "Không xác định được thông tin người dùng hoặc loại chi!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String loaithuName = getIntent().getStringExtra("loaichi_name");
        if (loaithuName != null) {
            edtName.setText(loaithuName);
        }
        //Sự kiện
        btnSaveLoaithu.setOnClickListener(v -> {
            String loathuName = edtName.getText().toString().trim();
            if (loathuName.isEmpty()) {
                edtName.setError("Tên loại chi không được để trống!");
                edtName.requestFocus();
                return;
            }

            boolean isTaskUpdated = database.updateLoaiChi(idloaithu, loathuName, userId);
            if (isTaskUpdated) {
                Toast.makeText(this, "Cập nhật loại chi thành công!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("user_id", userId);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Cập nhật loại chi thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelLoaithu.setOnClickListener(v -> finish());
    }
}
