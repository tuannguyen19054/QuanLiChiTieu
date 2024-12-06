package com.example.android_quanlychitieu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class LoaiChiAdapter extends ArrayAdapter<LoaiChi> {

    public LoaiChiAdapter(@NonNull Context context, @NonNull List<LoaiChi> loaichi) {
        super(context, 0, loaichi);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_loaichi, parent, false);
        }

        LoaiChi loaiChi = getItem(position);

        TextView tvLoaiThuId = convertView.findViewById(R.id.tvLoaiChiId);
        TextView tvLoaiThuName = convertView.findViewById(R.id.tvLoaiChiName);
        ImageButton btnEdit = convertView.findViewById(R.id.btn_edit);
        ImageButton btnDelete = convertView.findViewById(R.id.btn_delete);

        if (loaiChi != null) {
            tvLoaiThuId.setText(String.valueOf(loaiChi.getId()));
            tvLoaiThuName.setText(loaiChi.getName());

            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), EditLoaiChi.class);
                intent.putExtra("user_id", loaiChi.getUserId());
                intent.putExtra("loaichi_id", loaiChi.getId());
                intent.putExtra("loaichi_name", loaiChi.getName());

                ((Activity) getContext()).startActivityForResult(intent, 1);
            });
            btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa loại chi này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {

                            Database db = new Database(getContext());
                            boolean isDeleted = db.deleteLoaiChi(loaiChi.getId(), loaiChi.getUserId());
                            if (isDeleted) {
                                Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                remove(loaiChi);
                                notifyDataSetChanged(); // Cập nhật giao diện
                            } else {
                                Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                            }

                        })
                        .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                        .show();

            });



        }
        return convertView;
    }


}
