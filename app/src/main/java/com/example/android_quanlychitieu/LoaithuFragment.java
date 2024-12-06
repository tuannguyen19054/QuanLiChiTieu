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

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment dùng để quản lý và hiển thị danh sách các loại thu nhập (LoaiThu).
 */
public class LoaithuFragment extends Fragment {

    private FloatingActionButton fab; // Nút nổi để thêm mới loại thu nhập.
    private Database db; // Đối tượng Database để thao tác với cơ sở dữ liệu.
    private List<LoaiThu> loaithuList; // Danh sách các loại thu nhập.
    private LoaiThuAdapter loaithuAdapter; // Adapter để hiển thị danh sách LoaiThu.
    private int userId; // ID của người dùng, dùng để lấy dữ liệu liên quan.

    /**
     * Constructor mặc định của Fragment.
     */
    public LoaithuFragment() {
    }

    /**
     * Phương thức để tạo một instance mới của Fragment với userId được truyền vào.
     *
     * @param userId ID của người dùng.
     * @return LoaithuFragment instance với userId được gắn.
     */
    public static LoaithuFragment newInstance(int userId) {
        LoaithuFragment fragment = new LoaithuFragment();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt("user_id", -1); // Lấy userId từ arguments.
        }
    }

    /**
     * Phương thức được gọi khi Fragment quay lại foreground.
     * Tải lại danh sách loại thu nhập và cập nhật giao diện.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (loaithuAdapter != null) {
            loadLoaithu(userId); // Tải dữ liệu từ cơ sở dữ liệu.
            loaithuAdapter.notifyDataSetChanged(); // Thông báo adapter cập nhật giao diện.
        }
    }

    /**
     * Phương thức được gọi để tạo giao diện cho Fragment.
     *
     * @param inflater Dùng để "thổi phồng" layout XML thành View.
     * @param container View cha chứa Fragment này.
     * @param savedInstanceState Dữ liệu được lưu lại khi trạng thái thay đổi (nếu có).
     * @return View đã được khởi tạo.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Gắn layout từ XML vào Fragment.
        View view = inflater.inflate(R.layout.fragment_loaithu, container, false);

        // Khởi tạo đối tượng Database.
        db = new Database(requireContext());

        // Lấy userId từ arguments.
        if (getArguments() != null) {
            userId = getArguments().getInt("user_id", -1);
        }

        Log.d("TAG", "user_id in LoaithuFragment: " + userId);

        // Kiểm tra nếu userId không hợp lệ.
        if (userId == -1) {
            Toast.makeText(getContext(), "Lỗi: Không có user_id", Toast.LENGTH_SHORT).show();
            return view;
        }

        // Gắn ListView từ layout vào biến.
        ListView listView = view.findViewById(R.id.listViewLoaiThu);

        // Khởi tạo danh sách loại thu nhập và adapter.
        loaithuList = new ArrayList<>();
        loaithuAdapter = new LoaiThuAdapter(requireContext(), loaithuList);
        listView.setAdapter(loaithuAdapter); // Gắn adapter vào ListView.

        // Tải dữ liệu từ cơ sở dữ liệu và hiển thị.
        loadLoaithu(userId);

        // Xử lý sự kiện khi nhấn nút thêm mới (FloatingActionButton).
        fab = view.findViewById(R.id.fab_page);
        fab.setOnClickListener(v -> {
            // Chuyển sang Activity AddLoaiThu để thêm mới loại thu nhập.
            Intent intent = new Intent(getContext(), AddLoaiThu.class);
            intent.putExtra("user_id", userId); // Truyền userId qua Intent.
            ((Activity) getContext()).startActivityForResult(intent, 1); // Bắt đầu Activity và chờ kết quả.
        });

        return view; // Trả về View đã được khởi tạo.
    }

    /**
     * Phương thức tải danh sách loại thu nhập từ cơ sở dữ liệu.
     *
     * @param userId ID của người dùng (lọc dữ liệu theo user).
     */
    public void loadLoaithu(int userId) {
        Log.d("loaiThu", "Loading loai thu for userId: " + userId);

        // Lấy cơ sở dữ liệu ở chế độ chỉ đọc.
        SQLiteDatabase dbsqlt = this.db.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Câu truy vấn để lấy danh sách LoaiThu của người dùng.
            String query = "SELECT * FROM " + Database.TABLE_LOAITHU +
                    " WHERE " + Database.COLUMN_USER_ID_FK + " = ?";
            cursor = dbsqlt.rawQuery(query, new String[]{String.valueOf(userId)});

            // Xóa dữ liệu cũ trong danh sách.
            loaithuList.clear();

            // Duyệt qua Cursor và thêm dữ liệu vào danh sách.
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(Database.COLUMN_LOAITHU_ID)); // Lấy ID.
                    String name = cursor.getString(cursor.getColumnIndex(Database.COLUMN_LOAITHU_NAME)); // Lấy tên.
                    int userIdFromDb = cursor.getInt(cursor.getColumnIndex(Database.COLUMN_USER_ID_FK)); // Lấy userId.
                    loaithuList.add(new LoaiThu(id, name, userIdFromDb)); // Thêm vào danh sách.
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            // Ghi log nếu có lỗi xảy ra.
            Log.e("LoaithuFragment", "Error loading Loaithu", e);
        } finally {
            // Đóng Cursor nếu không còn sử dụng.
            if (cursor != null) cursor.close();
        }

        // Thông báo adapter cập nhật giao diện nếu không null.
        if (loaithuAdapter != null) {
            loaithuAdapter.notifyDataSetChanged();
        }
    }
}
