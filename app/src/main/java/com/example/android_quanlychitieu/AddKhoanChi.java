package com.example.android_quanlychitieu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;

public class AddKhoanChi extends AppCompatActivity {
    private Database database;
    private EditText edtName, edtNgayThu, edtTien, edtGhiChu;
    private Spinner spLoaiThu;
    private Button btnSave, btnCancel;
    private int userId;
    private List<LoaiChi> loaiThuList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_khoan_chi);

        database = new Database(this);


        edtName = findViewById(R.id.etTenKhoanChi);
        edtNgayThu = findViewById(R.id.etNgayChi);
        edtTien = findViewById(R.id.etTien_chi);
        edtGhiChu = findViewById(R.id.etGhiChu_chi);
        spLoaiThu = findViewById(R.id.spLoaiThu);
        btnSave = findViewById(R.id.btnSaveLoathu);
        btnCancel = findViewById(R.id.btnCancelLoathu);
        edtNgayThu.setOnClickListener(v -> showDatePickerDialog(edtNgayThu));

        userId = getIntent().getIntExtra("user_id", -1);
        if (userId == -1) {
            Toast.makeText(this, "User khoanchi ID không hợp lệ!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        loadLoaiThuSpinner();
        btnSave.setOnClickListener(v -> saveKhoanThu());
        btnCancel.setOnClickListener(v -> finish());

    }





    private void loadLoaiThuSpinner() {
        try {

            loaiThuList = database.getAllLoaiChiWithIds(userId);
            if (loaiThuList == null || loaiThuList.isEmpty()) {
                Toast.makeText(this, "Chưa có loại chi nào, hãy thêm loại chi trước!", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }



            ArrayAdapter<LoaiChi> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    loaiThuList
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spLoaiThu.setAdapter(adapter);

        } catch (Exception e) {
            Log.e("AddKhoanchi", "Error loading loaichi: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi khi tải danh sách loại chi!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showDatePickerDialog(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    editText.setText(formattedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
    private void saveKhoanThu() {
        if (loaiThuList == null || loaiThuList.isEmpty()) {
            Toast.makeText(this, "Chưa có loại chi nào, vui lòng thêm loại chi trước!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        LoaiChi selectedLoaiThu = (LoaiChi) spLoaiThu.getSelectedItem();
        if (selectedLoaiThu == null) {
            Toast.makeText(this, "Vui lòng chọn loại chi!", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = edtName.getText().toString().trim();
        String ngaythu = edtNgayThu.getText().toString().trim();
        String tien = edtTien.getText().toString().trim();
        String ghichu = edtGhiChu.getText().toString().trim();
        int loaiThuId = selectedLoaiThu.getId();

        // Validate các trường input
        if (TextUtils.isEmpty(name)) {
            edtName.setError("Vui lòng nhập tên khoản chi");
            return;
        }

        if (TextUtils.isEmpty(tien)) {
            edtTien.setError("Vui lòng nhập số tiền");
            return;
        }

        // Kiểm tra loaiThuId hợp lệ
        if (loaiThuId <= 0) {
            Toast.makeText(this, "Loại chi không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (database.insertKhoanChi(name, ngaythu, tien, ghichu, loaiThuId, userId)) {
                Toast.makeText(this, "Thêm khoản chi thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddKhoanChi.this, KhoanchiFragment.class);
                intent.putExtra("user_id", userId);  // Truyền user_id khi quay lại TaskActivity
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Thêm khoản chi thất bại!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("AddKhoancchi", "Error saving khoan chi: " + e.getMessage());
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }
}