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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddKhoanThu extends AppCompatActivity {
    private Database database;
    private EditText edtName, edtNgayThu, edtTien, edtGhiChu;
    private Spinner spLoaiThu;
    private Button btnSave, btnCancel;
    private int userId;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    private List<LoaiThu> loaiThuList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_khoan_thu);

        database = new Database(this);
        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        edtName = findViewById(R.id.etTenKhoanChi);
        edtNgayThu = findViewById(R.id.etNgayChi);
        edtTien = findViewById(R.id.etTien_chi);
        edtGhiChu = findViewById(R.id.etGhiChu_chi);
        spLoaiThu = findViewById(R.id.spLoaiThu);
        btnSave = findViewById(R.id.btnSaveLoathu);
        btnCancel = findViewById(R.id.btnCancelLoathu);
//        edtNgayThu.setText(dateFormatter.format(calendar.getTime()));

        edtNgayThu.setOnClickListener(v -> showDatePickerDialog(edtNgayThu));
        userId = getIntent().getIntExtra("user_id", -1);
        if (userId == -1) {
            Toast.makeText(this, "User khoanthu ID không hợp lệ!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadLoaiThuSpinner();
        btnSave.setOnClickListener(v -> saveKhoanThu());
        btnCancel.setOnClickListener(v -> finish());

    }





    private void loadLoaiThuSpinner() {
        try {

            loaiThuList = database.getAllLoaiThuWithIds(userId);
            if (loaiThuList == null || loaiThuList.isEmpty()) {
                Toast.makeText(this, "Chưa có loại thu nào, hãy thêm loại thu trước!", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }



            ArrayAdapter<LoaiThu> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    loaiThuList
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spLoaiThu.setAdapter(adapter);

        } catch (Exception e) {
            Log.e("AddKhoanThu", "Error loading loaithu: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi khi tải danh sách loại thu!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Chưa có loại thu nào, vui lòng thêm loại thu trước!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        LoaiThu selectedLoaiThu = (LoaiThu) spLoaiThu.getSelectedItem();
        if (selectedLoaiThu == null) {
            Toast.makeText(this, "Vui lòng chọn loại thu!", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = edtName.getText().toString().trim();
        String ngaythu = edtNgayThu.getText().toString().trim();
        String tien = edtTien.getText().toString().trim();
        String ghichu = edtGhiChu.getText().toString().trim();
        int loaiThuId = selectedLoaiThu.getId();

        // Validate các trường input
        if (TextUtils.isEmpty(name)) {
            edtName.setError("Vui lòng nhập tên khoản thu");
            return;
        }

        if (TextUtils.isEmpty(tien)) {
            edtTien.setError("Vui lòng nhập số tiền");
            return;
        }

        // Kiểm tra loaiThuId hợp lệ
        if (loaiThuId <= 0) {
            Toast.makeText(this, "Loại thu không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (database.insertKhoanThu(name, ngaythu, tien, ghichu, loaiThuId, userId)) {
                Toast.makeText(this, "Thêm khoản thu thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddKhoanThu.this, KhoanthuFragment.class);
                intent.putExtra("user_id", userId);  // Truyền user_id khi quay lại TaskActivity
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Thêm khoản thu thất bại!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("AddKhoanThu", "Error saving khoan thu: " + e.getMessage());
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