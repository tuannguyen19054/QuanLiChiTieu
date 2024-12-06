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

public class LoaichiFragment extends Fragment {
    private FloatingActionButton fab;
    private Database db;
    private List<LoaiChi> loaichiList;
    private LoaiChiAdapter loaithuAdapter;
    private int userId;

    public LoaichiFragment() {
    }

    public static LoaichiFragment newInstance(int userId) {
        LoaichiFragment fragment = new LoaichiFragment();
        Bundle args = new Bundle();
        args.putInt("user_id", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt("user_id", -1);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (loaithuAdapter != null) {
            loadLoaichi(userId);
            loaithuAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loaichi, container, false);

        db = new Database(requireContext());

        if (getArguments() != null) {
            userId = getArguments().getInt("user_id", -1);
        }

        Log.d("TAG", "user_id in LoaiChiFragment: " + userId);

        if (userId == -1) {
            Toast.makeText(getContext(), "Lỗi: Không có user_id", Toast.LENGTH_SHORT).show();
            return view;
        }

        ListView listView = view.findViewById(R.id.listViewLoaiThu);
        loaichiList = new ArrayList<>();
        loaithuAdapter = new LoaiChiAdapter(requireContext(), loaichiList);
        listView.setAdapter(loaithuAdapter);

        loadLoaichi(userId);

        fab = view.findViewById(R.id.fab_page);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddLoaiChi.class);
            intent.putExtra("user_id", userId);
            ((Activity) getContext()).startActivityForResult(intent, 1);
        });



        return view;
    }



    public void loadLoaichi(int userId) {
        Log.d("loaichi", "Loading loai chi for userId: " + userId);
        SQLiteDatabase dbsqlt = this.db.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + Database.TABLE_LOAICHI +
                    " WHERE " + Database.COLUMN_USER_ID_FK + " = ?";
            cursor = dbsqlt.rawQuery(query, new String[]{String.valueOf(userId)});

            loaichiList.clear();

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(Database.COLUMN_LOAICHI_ID));
                    String name = cursor.getString(cursor.getColumnIndex(Database.COLUMN_LOAICHI_NAME));
                    int userIdFromDb = cursor.getInt(cursor.getColumnIndex(Database.COLUMN_USER_ID_FK));
                    loaichiList.add(new LoaiChi(id, name, userIdFromDb));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("LoaiChiFragment", "Error loading Loaichi", e);
        } finally {
            if (cursor != null) cursor.close();
        }

        if (loaithuAdapter != null) {
            loaithuAdapter.notifyDataSetChanged();
        }
    }



}
