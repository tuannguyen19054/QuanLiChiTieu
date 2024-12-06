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

public class KhoanThuAdapter extends ArrayAdapter<KhoanThu> {
    private Database database; // Đối tượng để truy xuất cơ sở dữ liệu
    private int userId; // ID của người dùng, dùng để lọc dữ liệu theo người dùng

    /**
     * Constructor: Khởi tạo adapter cho danh sách KhoanThu
     *
     * @param context   Ngữ cảnh của Activity/Fragment
     * @param khoanthu  Danh sách các khoản thu cần hiển thị
     * @param db        Đối tượng Database để thao tác dữ liệu
     * @param userId    ID của người dùng
     */
    public KhoanThuAdapter(@NonNull Context context, @NonNull List<KhoanThu> khoanthu, Database db, int userId) {
        super(context, 0, khoanthu);
        this.database = db;
        this.userId = userId;
    }

    /**
     * Phương thức getView: Hiển thị từng item trong ListView
     *
     * @param position      Vị trí của item trong danh sách
     * @param convertView   View cũ được tái sử dụng, nếu null thì tạo mới
     * @param parent        ViewGroup cha của item
     * @return              View hiển thị dữ liệu cho item
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Nếu convertView null, tạo mới từ file XML
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_khoanthu, parent, false);
        }

        // Lấy đối tượng KhoanThu hiện tại
        KhoanThu khoanThu = getItem(position);

        // Liên kết các View trong item layout
        TextView ngayThu = convertView.findViewById(R.id.tvNgayKhoanThu); // Hiển thị ngày thu
        TextView ghichu = convertView.findViewById(R.id.tvGhiChuKhoanThu); // Hiển thị ghi chú
        TextView tienKhoanthu = convertView.findViewById(R.id.tvSoTienKhoanThu); // Hiển thị số tiền
        TextView khoanThuName = convertView.findViewById(R.id.tvTenKhoanThu); // Hiển thị tên loại thu nhập
        ImageButton btnEdit = convertView.findViewById(R.id.btnEditKhoanThu); // Nút chỉnh sửa
        ImageButton btnDelete = convertView.findViewById(R.id.btnDeleteKhoanThu); // Nút xóa

        // Gắn dữ liệu cho các TextView
        if (khoanThu != null) {
            ngayThu.setText("Ngày: " + khoanThu.getNgayThu()); // Hiển thị ngày thu
            ghichu.setText("Ghi chú: " + khoanThu.getGhiChu()); // Hiển thị ghi chú
            tienKhoanthu.setText("Tiền: " + khoanThu.getSoTien()); // Hiển thị số tiền

            // Lấy tên loại thu nhập từ cơ sở dữ liệu
            String loaiThuName = database.getLoaiThuName(khoanThu.getLoaiThuId(), userId);
            khoanThuName.setText(loaiThuName != null ? "Loại thu: " + loaiThuName : "Không rõ");

            // Sự kiện khi nhấn nút chỉnh sửa
            btnEdit.setOnClickListener(v -> {
                // Tạo Intent để mở Activity chỉnh sửa khoản thu
                Intent intent = new Intent(getContext(), EditKhoanthu.class);
                intent.putExtra("user_id", khoanThu.getUserId());
                intent.putExtra("loaithu_id_fk", khoanThu.getLoaiThuId());
                intent.putExtra("khoanthu_id", khoanThu.getId());
                intent.putExtra("ngaythu", khoanThu.getNgayThu());
                intent.putExtra("tien", khoanThu.getSoTien());
                intent.putExtra("ghichu", khoanThu.getGhiChu());
                intent.putExtra("khoanthu_name", khoanThu.getTenKhoanThu());
                ((Activity) getContext()).startActivityForResult(intent, 1); // Mở Activity và chờ kết quả
            });

            // Sự kiện khi nhấn nút xóa
            btnDelete.setOnClickListener(v -> {
                // Hiển thị hộp thoại xác nhận xóa
                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa khoản thu này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            // Xóa khoản thu trong cơ sở dữ liệu
                            boolean isDeleted = database.deleteKhoanthu(khoanThu.getId(), khoanThu.getUserId());
                            if (isDeleted) {
                                // Nếu xóa thành công, cập nhật giao diện
                                Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                remove(khoanThu); // Xóa khỏi danh sách
                                notifyDataSetChanged(); // Thông báo danh sách đã thay đổi
                            } else {
                                // Nếu xóa thất bại, hiển thị thông báo lỗi
                                Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss()) // Đóng hộp thoại nếu hủy
                        .show();
            });
        }

        // Trả về View đã được gắn dữ liệu
        return convertView;
    }
}
