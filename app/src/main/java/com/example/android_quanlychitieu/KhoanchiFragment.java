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
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class KhoanchiFragment extends Fragment {
    private FloatingActionButton fab;
    private Database db;
    private List<KhoanChi> khoanchiList;
    private KhoanchiAdapter khoanthuAdapter;
    private int userId;

    public KhoanchiFragment() {
        // Required empty public constructor
    }

    public static KhoanchiFragment newInstance(int userId) {
        KhoanchiFragment fragment = new KhoanchiFragment();
        Bundle args = new Bundle();
        args.putInt("user_id", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt("user_id", -1);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (khoanthuAdapter != null) {
            loadLoaichi(userId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khoanchi, container, false);

        // Khởi tạo Database
        db = new Database(requireContext());
        if (getArguments() != null) {
            userId = getArguments().getInt("user_id", -1);
        }
        Log.d("TAG", "user_id in khoanchiFragment: " + userId);
        // Kiểm tra user_id
        if (userId == -1) {
            Toast.makeText(getContext(), "Lỗi: Không có user_id", Toast.LENGTH_SHORT).show();
            return view;
        }
        ListView listView = view.findViewById(R.id.listViewKhoanThu);
        khoanchiList = new ArrayList<>();
        khoanthuAdapter = new KhoanchiAdapter(requireContext(), khoanchiList, db, userId);

        listView.setAdapter(khoanthuAdapter);

        loadLoaichi(userId);

        fab = view.findViewById(R.id.fab_khoanchi);
        fab.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(getContext(), "Lỗi: Không xác định được người dùng", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(getContext(), AddKhoanChi.class);
            intent.putExtra("user_id", userId);
            ((Activity) getContext()).startActivityForResult(intent, 1);
        });

        return view;
    }



    private void loadLoaichi(int userId) {
        Log.d("loaichi", "Loading khoan chi for userId: " + userId);
        SQLiteDatabase dbsqlt = this.db.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + Database.TABLE_KHOANCHI +
                    " WHERE " + Database.COLUMN_USER_ID_FK + " = ?";
            cursor = dbsqlt.rawQuery(query, new String[]{String.valueOf(userId)});

            khoanchiList.clear();

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_KHOANCHI_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_KHOANCHI_NAME));
                    String ngaythu = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_NGAYCHI));
                    int tien = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_TIENCHI));
                    String ghichu = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_GHICHUCHI));
                    int loathu = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_LOAICHI_ID_FK));
                    int userIdFromDb = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_USER_ID_FK));
                    khoanchiList.add(new KhoanChi(id,name,ngaythu,tien,ghichu,loathu,userIdFromDb));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("KhoanchiFragment", "Error loading Loaichi", e);
        } finally {
            if (cursor != null) cursor.close();
        }

        if (khoanthuAdapter != null) {
            khoanthuAdapter.notifyDataSetChanged();
        }
    }
}
