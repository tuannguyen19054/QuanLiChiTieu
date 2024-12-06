package com.example.android_quanlychitieu;

public class KhoanChi {
    private int id;
    private String tenKhoanChi;
    private String ngayChi;
    private double soTien;
    private String ghiChu;
    private int loaiChiId;
    private int userId;

    // Constructor
    public KhoanChi(int id, String tenKhoanChi, String ngayChi, double soTien, String ghiChu, int loaiChiId, int userId) {
        this.id = id;
        this.tenKhoanChi = tenKhoanChi;
        this.ngayChi = ngayChi;
        this.soTien = soTien;
        this.ghiChu = ghiChu;
        this.loaiChiId = loaiChiId;
        this.userId = userId;
    }

    public KhoanChi(String tenKhoanChi, String ngayChi, double soTien, String ghiChu, int loaiChiId, int userId) {
        this.tenKhoanChi = tenKhoanChi;
        this.ngayChi = ngayChi;
        this.soTien = soTien;
        this.ghiChu = ghiChu;
        this.loaiChiId = loaiChiId;
        this.userId = userId;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenKhoanChi() {
        return tenKhoanChi;
    }

    public void setTenKhoanChi(String tenKhoanChi) {
        this.tenKhoanChi = tenKhoanChi;
    }

    public String getNgayChi() {
        return ngayChi;
    }

    public void setNgayChi(String ngayChi) {
        this.ngayChi = ngayChi;
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

    public int getLoaiChiId() {
        return loaiChiId;
    }

    public void setLoaiChiId(int loaiChiId) {
        this.loaiChiId = loaiChiId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "KhoanChi{" +
                "id=" + id +
                ", tenKhoanChi='" + tenKhoanChi + '\'' +
                ", ngayChi='" + ngayChi + '\'' +
                ", soTien=" + soTien +
                ", ghiChu='" + ghiChu + '\'' +
                ", loaiChiId=" + loaiChiId +
                ", userId=" + userId +
                '}';
    }
}
