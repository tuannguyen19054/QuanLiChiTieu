package com.example.android_quanlychitieu;

public class KhoanThu {
    private int id;
    private String tenKhoanThu;
    private String ngayThu;
    private double soTien;
    private String ghiChu;
    private int loaiThuId;
    private int userId;

    // Constructor
    public KhoanThu(int id, String tenKhoanThu, String ngayThu, double soTien, String ghiChu, int loaiThuId, int userId) {
        this.id = id;
        this.tenKhoanThu = tenKhoanThu;
        this.ngayThu = ngayThu;
        this.soTien = soTien;
        this.ghiChu = ghiChu;
        this.loaiThuId = loaiThuId;
        this.userId = userId;
    }

    public KhoanThu(String tenKhoanThu, String ngayThu, double soTien, String ghiChu, int loaiThuId, int userId) {
        this.tenKhoanThu = tenKhoanThu;
        this.ngayThu = ngayThu;
        this.soTien = soTien;
        this.ghiChu = ghiChu;
        this.loaiThuId = loaiThuId;
        this.userId = userId;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenKhoanThu() {
        return tenKhoanThu;
    }

    public void setTenKhoanThu(String tenKhoanThu) {
        this.tenKhoanThu = tenKhoanThu;
    }

    public String getNgayThu() {
        return ngayThu;
    }

    public void setNgayThu(String ngayThu) {
        this.ngayThu = ngayThu;
    }

    public double getSoTien() {
        return soTien;
    }

    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public int getLoaiThuId() {
        return loaiThuId;
    }

    public void setLoaiThuId(int loaiThuId) {
        this.loaiThuId = loaiThuId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "KhoanThu{" +
                "id=" + id +
                ", tenKhoanThu='" + tenKhoanThu + '\'' +
                ", ngayThu='" + ngayThu + '\'' +
                ", soTien=" + soTien +
                ", ghiChu='" + ghiChu + '\'' +
                ", loaiThuId=" + loaiThuId +
                ", userId=" + userId +
                '}';
    }
}
