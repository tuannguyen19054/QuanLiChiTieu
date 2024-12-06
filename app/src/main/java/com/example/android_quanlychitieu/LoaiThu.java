package com.example.android_quanlychitieu;

public class LoaiThu {
    private int id;
    private String name;
    private int userId;
    // Constructor
    public LoaiThu(int id, String name, int userIdFromDb) {
        this.id = id;
        this.name = name;
        this.userId = userIdFromDb;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    @Override
    public String toString() {
        return name; // Hiển thị tên loại thu
    }
}
