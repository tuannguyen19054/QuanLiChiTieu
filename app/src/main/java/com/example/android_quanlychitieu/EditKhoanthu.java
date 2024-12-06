package com.example.android_quanlychitieu;

import android.app.DatePickerDialog;
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

public class EditKhoanthu extends AppCompatActivity {
    private Database database;
    private EditText edtName, edtNgayThu, edtTien, edtGhiChu;
    private Spinner spLoaiThu;
    private Button btnSave, btnCancel;
    private int userId, khoanThuId;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    private List<LoaiThu> loaiThuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_khoanthu);

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

        // Lấy dữ liệu truyền từ Intent
        userId = getIntent().getIntExtra("user_id", -1);
        khoanThuId = getIntent().getIntExtra("khoanthu_id", -1);

        if (userId == -1 || khoanThuId == -1) {
            Toast.makeText(this, "Dữ liệu không hợp lệ!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupDatePicker();
        loadLoaiThuSpinner();
        loadKhoanThuData();


        btnSave.setOnClickListener(v -> saveKhoanThu());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            edtNgayThu.setText(dateFormatter.format(calendar.getTime()));
        };

        edtNgayThu.setOnClickListener(v -> new DatePickerDialog(EditKhoanthu.this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());
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
            Log.e("EditKhoanThu", "Error loading loaithu: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi khi tải danh sách loại thu!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadKhoanThuData() {
        KhoanThu khoanThu = database.getKhoanThu(khoanThuId, userId);
        if (khoanThu == null) {
            Toast.makeText(this, "Không tìm thấy dữ liệu khoản thu!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        edtName.setText(khoanThu.getTenKhoanThu());
        edtNgayThu.setText(khoanThu.getNgayThu());
        edtTien.setText (String.valueOf(khoanThu.getSoTien()));
        edtGhiChu.setText(khoanThu.getGhiChu());

        // Chọn loại thu tương ứng trong Spinner
        for (int i = 0; i < loaiThuList.size(); i++) {
            if (loaiThuList.get(i).getId() == khoanThu.getLoaiThuId()) {
                spLoaiThu.setSelection(i);
                break;
            }
        }
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

        try {
            if (database.updateKhoanThu(khoanThuId, name, ngaythu, tien, ghichu, loaiThuId, userId)) {
                Toast.makeText(this, "Cập nhật khoản thu thành công!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Cập nhật khoản thu thất bại!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("EditKhoanThu", "Error updating khoan thu: " + e.getMessage());
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
