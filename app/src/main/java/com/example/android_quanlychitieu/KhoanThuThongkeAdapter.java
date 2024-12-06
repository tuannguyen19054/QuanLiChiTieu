package com.example.android_quanlychitieu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class KhoanThuThongkeAdapter extends ArrayAdapter<KhoanThu> {

    private Context context;
    private List<KhoanThu> khoanThuList;

    public KhoanThuThongkeAdapter(Context context, List<KhoanThu> khoanThuList) {
        super(context, R.layout.item_thongke_thu, khoanThuList);
        this.context = context;
        this.khoanThuList = khoanThuList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_thongke_thu, parent, false);
        }

        KhoanThu khoanThu = khoanThuList.get(position);

        TextView tvNgayThu = convertView.findViewById(R.id.tvNgayThu);
        TextView tvTenKhoanThu = convertView.findViewById(R.id.tvTenKhoanThu);
        TextView tvSoTien = convertView.findViewById(R.id.tvSoTien);

        tvNgayThu.setText("Ngày: " + khoanThu.getNgayThu());
        tvTenKhoanThu.setText("Tên khoản thu: " + khoanThu.getTenKhoanThu());
        tvSoTien.setText("Tiền: +" + khoanThu.getSoTien() + "$");

        return convertView;
    }
}
