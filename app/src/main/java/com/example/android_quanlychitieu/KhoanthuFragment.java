package com.example.android_quanlychitieu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment dùng để quản lý và hiển thị danh sách các khoản thu (KhoanThu).
 */
public class KhoanthuFragment extends Fragment {
    private FloatingActionButton fab; // Nút thêm khoản thu (FloatingActionButton).
    private Database db; // Đối tượng cơ sở dữ liệu (Database).
    private List<KhoanThu> khoanthuList; // Danh sách các khoản thu.
    private KhoanThuAdapter khoanthuAdapter; // Adapter để hiển thị danh sách khoản thu.
    private int userId; // ID người dùng, được truyền qua arguments.

    /**
     * Constructor mặc định của Fragment.
     */
    public KhoanthuFragment() {
        // Constructor rỗng cần thiết.
    }

    /**
     * Phương thức tạo một instance mới của KhoanthuFragment với userId.
     *
     * @param userId ID người dùng.
     * @return Instance của KhoanthuFragment.
     */
    public static KhoanthuFragment newInstance(int userId) {
        KhoanthuFragment fragment = new KhoanthuFragment();
        Bundle args = new Bundle();
        args.putInt("user_id", userId); // Truyền userId qua arguments.
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Phương thức được gọi khi Fragment được tạo.
     * Dùng để lấy dữ liệu từ arguments nếu có.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt("user_id", -1); // Lấy userId từ arguments.
        }
    }

    /**
     * Phương thức được gọi khi Fragment quay lại foreground.
     * Tải lại danh sách các khoản thu và cập nhật giao diện.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (khoanthuAdapter != null) {
            loadLoaithu(userId); // Tải dữ liệu từ cơ sở dữ liệu.
        }
    }

    /**
     * Tạo giao diện cho Fragment.
     *
     * @param inflater Dùng để "inflate" layout từ XML thành View.
     * @param container View cha chứa Fragment này.
     * @param savedInstanceState Dữ liệu được lưu lại khi trạng thái thay đổi (nếu có).
     * @return View đã được tạo.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Gắn layout từ XML vào Fragment.
        View view = inflater.inflate(R.layout.fragment_khoanthu, container, false);

        // Khởi tạo đối tượng Database.
        db = new Database(requireContext());

        // Lấy userId từ arguments.
        if (getArguments() != null) {
            userId = getArguments().getInt("user_id", -1);
        }

        Log.d("TAG", "user_id in khoanthuFragment: " + userId);

        // Kiểm tra nếu userId không hợp lệ.
        if (userId == -1) {
            Toast.makeText(getContext(), "Lỗi: Không có user_id", Toast.LENGTH_SHORT).show();
            return view;
        }

        // Gắn ListView từ layout vào biến.
        ListView listView = view.findViewById(R.id.listViewKhoanThu);

        // Khởi tạo danh sách khoản thu và adapter.
        khoanthuList = new ArrayList<>();
        khoanthuAdapter = new KhoanThuAdapter(requireContext(), khoanthuList, db, userId);

        // Gắn adapter vào ListView.
        listView.setAdapter(khoanthuAdapter);

        // Tải dữ liệu từ cơ sở dữ liệu.
        loadLoaithu(userId);

        // Xử lý sự kiện khi nhấn nút thêm mới (FloatingActionButton).
        fab = view.findViewById(R.id.fab_khoanthu);
        fab.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(getContext(), "Lỗi: Không xác định được người dùng", Toast.LENGTH_SHORT).show();
                return;
            }
            // Chuyển sang Activity AddKhoanThu để thêm mới khoản thu.
            Intent intent = new Intent(getContext(), AddKhoanThu.class);
            intent.putExtra("user_id", userId); // Truyền userId qua Intent.
            ((Activity) getContext()).startActivityForResult(intent, 1); // Bắt đầu Activity và chờ kết quả.
        });

        return view; // Trả về View đã được khởi tạo.
    }

    /**
     * Phương thức tải danh sách các khoản thu từ cơ sở dữ liệu.
     *
     * @param userId ID người dùng để lọc dữ liệu.
     */
    private void loadLoaithu(int userId) {
        Log.d("loaiThu", "Loading khoan thu for userId: " + userId);

        // Lấy cơ sở dữ liệu ở chế độ chỉ đọc.
        SQLiteDatabase dbsqlt = this.db.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Câu truy vấn để lấy danh sách KhoanThu của người dùng.
            String query = "SELECT * FROM " + Database.TABLE_KHOANTHU +
                    " WHERE " + Database.COLUMN_USER_ID_FK + " = ?";
            cursor = dbsqlt.rawQuery(query, new String[]{String.valueOf(userId)});

            // Xóa dữ liệu cũ trong danh sách.
            khoanthuList.clear();

            // Duyệt qua Cursor và thêm dữ liệu vào danh sách.
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_KHOANTHU_ID)); // Lấy ID.
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_KHOANTHU_NAME)); // Lấy tên khoản thu.
                    String ngaythu = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_NGAYTHU)); // Ngày thu.
                    int tien = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_TIEN)); // Số tiền.
                    String ghichu = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_GHICHU)); // Ghi chú.
                    int loathu = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_LOAITHU_ID_FK)); // Loại thu.
                    int userIdFromDb = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_USER_ID_FK)); // ID người dùng.

                    // Thêm dữ liệu vào danh sách.
                    khoanthuList.add(new KhoanThu(id, name, ngaythu, tien, ghichu, loathu, userIdFromDb));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            // Ghi log nếu có lỗi xảy ra.
            Log.e("KhoanthuFragment", "Error loading Loaithu", e);
        } finally {
            // Đóng Cursor nếu không còn sử dụng.
            if (cursor != null) cursor.close();
        }

        // Thông báo adapter cập nhật giao diện nếu không null.
        if (khoanthuAdapter != null) {
            khoanthuAdapter.notifyDataSetChanged();
        }
    }
}
