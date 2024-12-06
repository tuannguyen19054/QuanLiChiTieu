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

/**
 * Adapter để hiển thị danh sách Loại Thu trong ListView.
 */
public class LoaiThuAdapter extends ArrayAdapter<LoaiThu> {

    /**
     * Constructor để khởi tạo adapter.
     *
     * @param context  Context của ứng dụng.
     * @param loaithu  Danh sách các Loại Thu.
     */
    public LoaiThuAdapter(@NonNull Context context, @NonNull List<LoaiThu> loaithu) {
        super(context, 0, loaithu);
    }

    /**
     * Phương thức tạo View cho mỗi item trong ListView.
     *
     * @param position    Vị trí của item trong danh sách.
     * @param convertView View hiện tại (nếu tái sử dụng).
     * @param parent      ViewGroup cha chứa View này.
     * @return View đã được tạo hoặc tái sử dụng.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Kiểm tra và tạo View nếu convertView chưa được khởi tạo.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_loaithu, parent, false);
        }

        // Lấy đối tượng LoaiThu tại vị trí hiện tại.
        LoaiThu loaiThu = getItem(position);

        // Ánh xạ các thành phần giao diện.
        TextView tvLoaiThuId = convertView.findViewById(R.id.tvLoaiThuId); // TextView hiển thị ID Loại Thu.
        TextView tvLoaiThuName = convertView.findViewById(R.id.tvLoaiThuName); // TextView hiển thị tên Loại Thu.
        ImageButton btnEdit = convertView.findViewById(R.id.btn_edit); // Nút chỉnh sửa.
        ImageButton btnDelete = convertView.findViewById(R.id.btn_delete); // Nút xóa.

        if (loaiThu != null) {
            // Hiển thị ID và tên Loại Thu trên giao diện.
            tvLoaiThuId.setText(String.valueOf(loaiThu.getId()));
            tvLoaiThuName.setText(loaiThu.getName());

            // Sự kiện nhấn nút "Chỉnh sửa".
            btnEdit.setOnClickListener(v -> {
                // Mở Activity EditLoaithu và truyền dữ liệu qua Intent.
                Intent intent = new Intent(getContext(), EditLoaithu.class);
                intent.putExtra("user_id", loaiThu.getUserId());
                intent.putExtra("loaithu_id", loaiThu.getId());
                intent.putExtra("loaithu_name", loaiThu.getName());
                // Bắt đầu Activity chỉnh sửa và chờ kết quả.
                ((Activity) getContext()).startActivityForResult(intent, 1);
            });

            // Sự kiện nhấn nút "Xóa".
            btnDelete.setOnClickListener(v -> {
                // Hiển thị hộp thoại xác nhận trước khi xóa.
                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận xóa") // Tiêu đề hộp thoại.
                        .setMessage("Bạn có chắc chắn muốn xóa loại thu này?") // Nội dung hộp thoại.
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            // Thực hiện xóa Loại Thu trong cơ sở dữ liệu.
                            Database db = new Database(getContext());
                            boolean isDeleted = db.deleteLoaithu(loaiThu.getId(), loaiThu.getUserId());
                            if (isDeleted) {
                                // Hiển thị thông báo thành công.
                                Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                // Loại bỏ item khỏi danh sách và cập nhật giao diện.
                                remove(loaiThu);
                                notifyDataSetChanged();
                            } else {
                                // Hiển thị thông báo lỗi nếu xóa thất bại.
                                Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss()) // Hủy thao tác xóa.
                        .show(); // Hiển thị hộp thoại.
            });
        }

        // Trả về View đã được thiết lập.
        return convertView;
    }
}
