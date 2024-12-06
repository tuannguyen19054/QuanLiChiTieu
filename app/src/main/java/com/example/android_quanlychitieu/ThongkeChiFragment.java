package com.example.android_quanlychitieu;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.List;

public class ThongkeChiFragment extends Fragment {

    private int userId;

    public ThongkeChiFragment() {
        // Required empty public constructor
    }

    public static ThongkeChiFragment newInstance(int userId) {
        ThongkeChiFragment fragment = new ThongkeChiFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thongke_chi, container, false);

        EditText edtStartDate = view.findViewById(R.id.edtStartDate);
        EditText edtEndDate = view.findViewById(R.id.edtEndDate);
        Button btnSearch = view.findViewById(R.id.btnSearch);
        ListView listViewKhoanThu = view.findViewById(R.id.listViewKhoanThu);
        TextView textViewTongTien = view.findViewById(R.id.textViewTongTien);
        TextView textViewCount = view.findViewById(R.id.textViewcount);

        edtStartDate.setOnClickListener(v -> showDatePickerDialog(edtStartDate));





        edtEndDate.setOnClickListener(v -> showDatePickerDialog(edtEndDate));

        // Khởi tạo database
        Database db = new Database(getContext());
        int userId = getArguments().getInt("user_id", -1);

        btnSearch.setOnClickListener(v -> {
            String startDate = edtStartDate.getText().toString();
            String endDate = edtEndDate.getText().toString();

            List<KhoanChi> khoanThuList = db.getKhoanChiTheoNgay(startDate, endDate, userId);
            int count = db.countKhoanChiTheoNgay(startDate, endDate, userId);

            KhoanchiThongkeAdapter adapter = new KhoanchiThongkeAdapter(getContext(), khoanThuList);
            listViewKhoanThu.setAdapter(adapter);

            double tongTien = 0;
            for (KhoanChi kt : khoanThuList) {
                tongTien += kt.getSoTien();
            }
            textViewTongTien.setText(String.format("Tổng tiền: %.2f$", tongTien));
            textViewCount.setText(String.format("Số lượng: %d", count));

        });

        return view;
    }

    private void showDatePickerDialog(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    editText.setText(formattedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}
