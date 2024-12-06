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

public class KhoanchiAdapter extends ArrayAdapter<KhoanChi> {
    private Database database;
    private int userId;

    public KhoanchiAdapter(@NonNull Context context, @NonNull List<KhoanChi> khoanthu, Database db, int userId) {
        super(context, 0, khoanthu);
        this.database = db;
        this.userId = userId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_khoanchi, parent, false);
        }

        KhoanChi khoanThu = getItem(position);

        TextView ngayThu = convertView.findViewById(R.id.tvNgayKhoanThu);
        TextView ghichu = convertView.findViewById(R.id.tvGhiChuKhoanThu);
        TextView tienKhoanthu = convertView.findViewById(R.id.tvSoTienKhoanThu);
        TextView khoanThuName = convertView.findViewById(R.id.tvTenKhoanThu);
        ImageButton btnEdit = convertView.findViewById(R.id.btnEditKhoanThu);
        ImageButton btnDelete = convertView.findViewById(R.id.btnDeleteKhoanThu);

        if (khoanThu != null) {
            ngayThu.setText("Ngày: "+khoanThu.getNgayChi());
            ghichu.setText("Ghi chú: "+khoanThu.getGhiChu());
            tienKhoanthu.setText(String.valueOf("Tiền: "+khoanThu.getSoTien()));

            String loaiThuName = database.getLoaiChiName(khoanThu.getLoaiChiId(), userId);
            khoanThuName.setText(loaiThuName != null ?"Loại Chi: " +loaiThuName : "Không rõ");

            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), EditKhoanChi.class);
                intent.putExtra("user_id", khoanThu.getUserId());
                intent.putExtra("loaichi_id_fk", loaiThuName);
                intent.putExtra("khoanchi_id", khoanThu.getId());
                intent.putExtra("ngaychi", khoanThu.getNgayChi());
                intent.putExtra("tienchi", khoanThu.getSoTien());
                intent.putExtra("ghichu", khoanThu.getGhiChu());
                intent.putExtra("khoanchi_name", khoanThu.getTenKhoanChi());
                ((Activity) getContext()).startActivityForResult(intent, 1);
            });
            btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa khoan  chi này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {

                            Database db = new Database(getContext());
                            boolean isDeleted = db.deleteKhoanchi(khoanThu.getId(), khoanThu.getUserId());
                            if (isDeleted) {
                                Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                remove(khoanThu);
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
