package com.example.android_quanlychitieu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class KhoanchiThongkeAdapter extends ArrayAdapter<KhoanChi> {

    private Context context;
    private List<KhoanChi> khoanThuList;

    public KhoanchiThongkeAdapter(Context context, List<KhoanChi> khoanThuList) {
        super(context, R.layout.item_thonke_chi, khoanThuList);
        this.context = context;
        this.khoanThuList = khoanThuList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_thonke_chi, parent, false);
        }

        KhoanChi khoanThu = khoanThuList.get(position);

        TextView tvNgayThu = convertView.findViewById(R.id.tvNgayThu);
        TextView tvTenKhoanThu = convertView.findViewById(R.id.tvTenKhoanThu);
        TextView tvSoTien = convertView.findViewById(R.id.tvSoTien);

        tvNgayThu.setText("Ngày: " + khoanThu.getNgayChi());
        tvTenKhoanThu.setText("Tên khoản chi: " + khoanThu.getTenKhoanChi());
        tvSoTien.setText("Tiền: +" + khoanThu.getSoTien() + "$");

        return convertView;
    }
}
