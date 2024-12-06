package com.example.android_quanlychitieu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuanLiChiTieu.db";
    private static final int DATABASE_VERSION = 1;

    // Table: users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Table: loaithu
    public static final String TABLE_LOAITHU = "loaithu";
    public static final String COLUMN_LOAITHU_ID = "loaithu_id";
    public static final String COLUMN_LOAITHU_NAME = "loaithu_name";
    public static final String COLUMN_USER_ID_FK = "user_id_fk";

    //
    public static final String TABLE_LOAICHI = "loaichi";
    public static final String COLUMN_LOAICHI_ID = "loaichi_id";
    public static final String COLUMN_LOAICHI_NAME = "loaichi_name";


    // KhoanThu
    public static final String TABLE_KHOANTHU = "khoanthu";

    // Các cột trong bảng khoanthu
    public static final String COLUMN_KHOANTHU_ID = "khoanthu_id";
    public static final String COLUMN_KHOANTHU_NAME = "khoanthu_name"; // Tên khoản thu
    public static final String COLUMN_NGAYTHU = "ngaythu"; // Ngày thu
    public static final String COLUMN_TIEN = "tien"; // Số tiền
    public static final String COLUMN_GHICHU = "ghichu"; // Ghi chú
    public static final String COLUMN_LOAITHU_ID_FK = "loaithu_id_fk"; // Khóa ngoại đến loaithu
    public static final String COLUMN_LOAICHI_ID_FK = "loaichi_id_fk"; // Khóa ngoại đến loaithu

    //Khoanchi
    public static final String TABLE_KHOANCHI = "khoanchi";

    // Các cột trong bảng khoanthu
    public static final String COLUMN_KHOANCHI_ID = "khoanchi_id";
    public static final String COLUMN_KHOANCHI_NAME = "khoanchi_name"; // Tên khoản thu
    public static final String COLUMN_NGAYCHI = "ngaychi"; // Ngày thu
    public static final String COLUMN_TIENCHI = "tienchi"; // Số tiền
    public static final String COLUMN_GHICHUCHI = "ghichu"; // Ghi chú





    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table users
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL)";
        db.execSQL(CREATE_USERS_TABLE);

        // Create table loaithu
        String CREATE_LOAITHU_TABLE = "CREATE TABLE " + TABLE_LOAITHU + " (" +
                COLUMN_LOAITHU_ID + " INTEGER NOT NULL, " +
                COLUMN_LOAITHU_NAME + " TEXT NOT NULL, " +
                COLUMN_USER_ID_FK + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), " +
                "PRIMARY KEY(" + COLUMN_LOAITHU_ID + ", " + COLUMN_USER_ID_FK + "))";
        db.execSQL(CREATE_LOAITHU_TABLE);

        // table khoanthu



        String CREATE_TABLE_KHOANTHU = "CREATE TABLE " + TABLE_KHOANTHU + " ("
                + COLUMN_KHOANTHU_ID + " INTEGER NOT NULL, "
                + COLUMN_KHOANTHU_NAME + " TEXT NOT NULL, "
                + COLUMN_NGAYTHU + " TEXT, "
                + COLUMN_TIEN + " TEXT, "
                + COLUMN_GHICHU + " TEXT, "
                + COLUMN_LOAITHU_ID_FK + " INTEGER NOT NULL, "
                + COLUMN_USER_ID_FK + " INTEGER NOT NULL, "
                + "PRIMARY KEY (" + COLUMN_KHOANTHU_ID + ", " + COLUMN_USER_ID_FK + "), "
                + "FOREIGN KEY (" + COLUMN_LOAITHU_ID_FK + ") REFERENCES " + TABLE_LOAITHU + " (" + COLUMN_LOAITHU_ID + "), "
                + "FOREIGN KEY (" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + " (" + COLUMN_USER_ID + ")"
                + ");";
        db.execSQL(CREATE_TABLE_KHOANTHU);


        //table loại chi
        String CREATE_LOAICHI_TABLE = "CREATE TABLE " + TABLE_LOAICHI + " (" +
                COLUMN_LOAICHI_ID + " INTEGER NOT NULL, " +
                COLUMN_LOAICHI_NAME + " TEXT NOT NULL, " +
                COLUMN_USER_ID_FK + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), " +
                "PRIMARY KEY(" + COLUMN_LOAICHI_ID + ", " + COLUMN_USER_ID_FK + "))";
        db.execSQL(CREATE_LOAICHI_TABLE);

        //TABLE KHOANCHI

        String CREATE_TABLE_KHOANCHI = "CREATE TABLE " + TABLE_KHOANCHI + " ("
                + COLUMN_KHOANCHI_ID + " INTEGER NOT NULL, "
                + COLUMN_KHOANCHI_NAME + " TEXT NOT NULL, "
                + COLUMN_NGAYCHI + " TEXT, "
                + COLUMN_TIENCHI + " TEXT, "
                + COLUMN_GHICHUCHI + " TEXT, "
                + COLUMN_LOAICHI_ID_FK + " INTEGER NOT NULL, "
                + COLUMN_USER_ID_FK + " INTEGER NOT NULL, "
                + "PRIMARY KEY (" + COLUMN_KHOANCHI_ID + ", " + COLUMN_USER_ID_FK + "), "
                + "FOREIGN KEY (" + COLUMN_LOAICHI_ID_FK + ") REFERENCES " + TABLE_LOAICHI + " (" + COLUMN_LOAICHI_ID + "), "
                + "FOREIGN KEY (" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + " (" + COLUMN_USER_ID + ")"
                + ");";
        db.execSQL(CREATE_TABLE_KHOANCHI);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOAITHU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOAICHI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KHOANTHU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KHOANCHI);
        onCreate(db);
    }

    // Register user and auto-create loaithu entries
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        if (result != -1) {
            int userId = getLastInsertedId();
            return true;
        }
        return false;
    }


    //thêm khoanthu
    public boolean insertKhoanThu(String name,String ngaythu,String tien,String ghichu, int loaithuId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int newId = getNextKhoanThuId(userId);
        ContentValues values = new ContentValues();
        values.put(COLUMN_KHOANTHU_ID, newId);
        values.put(COLUMN_KHOANTHU_NAME, name);
        values.put(COLUMN_NGAYTHU, ngaythu);
        values.put(COLUMN_TIEN, tien);
        values.put(COLUMN_GHICHU, ghichu);
        values.put(COLUMN_LOAITHU_ID_FK, loaithuId);
        values.put(COLUMN_USER_ID_FK, userId);
        long result = db.insert(TABLE_KHOANTHU, null, values);
        return result != -1;
    }
    public boolean insertKhoanChi(String name,String ngaythu,String tien,String ghichu, int loaithuId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int newId = getNextKhoanThuId(userId);
        ContentValues values = new ContentValues();
        values.put(COLUMN_KHOANCHI_ID, newId);
        values.put(COLUMN_KHOANCHI_NAME, name);
        values.put(COLUMN_NGAYCHI, ngaythu);
        values.put(COLUMN_TIENCHI, tien);
        values.put(COLUMN_GHICHUCHI, ghichu);
        values.put(COLUMN_LOAICHI_ID_FK, loaithuId);
        values.put(COLUMN_USER_ID_FK, userId);
        long result = db.insert(TABLE_KHOANCHI, null, values);
        return result != -1;
    }

    //updatekhoanthu
    public boolean updateKhoanThu(int khoanThuId, String name, String ngayThu, String tien, String ghiChu, int loaiThuId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Gán giá trị mới
        values.put(COLUMN_KHOANTHU_NAME, name);
        values.put(COLUMN_NGAYTHU, ngayThu);
        values.put(COLUMN_TIEN, tien);
        values.put(COLUMN_GHICHU, ghiChu);
        values.put(COLUMN_LOAITHU_ID_FK, loaiThuId);
        values.put(COLUMN_USER_ID_FK, userId);

        // Điều kiện cập nhật: đúng khoản thu ID và người dùng
        String whereClause = COLUMN_KHOANTHU_ID + " = ? AND " + COLUMN_USER_ID_FK + " = ?";
        String[] whereArgs = {String.valueOf(khoanThuId), String.valueOf(userId)};

        try {
            int rows = db.update(TABLE_KHOANTHU, values, whereClause, whereArgs);
            return rows > 0; // Trả về true nếu có dòng bị ảnh hưởng
        } catch (Exception e) {
            Log.e("Database", "Error updating khoan thu: " + e.getMessage());
            return false; // Trả về false nếu có lỗi
        }
    }
    public boolean updateKhoanChi(int khoanThuId, String name, String ngayThu, String tien, String ghiChu, int loaiThuId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Gán giá trị mới
        values.put(COLUMN_KHOANCHI_NAME, name);
        values.put(COLUMN_NGAYCHI, ngayThu);
        values.put(COLUMN_TIENCHI, tien);
        values.put(COLUMN_GHICHUCHI, ghiChu);
        values.put(COLUMN_LOAICHI_ID_FK, loaiThuId);
        values.put(COLUMN_USER_ID_FK, userId);

        // Điều kiện cập nhật: đúng khoản thu ID và người dùng
        String whereClause = COLUMN_KHOANCHI_ID + " = ? AND " + COLUMN_USER_ID_FK + " = ?";
        String[] whereArgs = {String.valueOf(khoanThuId), String.valueOf(userId)};

        try {
            int rows = db.update(TABLE_KHOANCHI, values, whereClause, whereArgs);
            return rows > 0; // Trả về true nếu có dòng bị ảnh hưởng
        } catch (Exception e) {
            Log.e("Database", "Error updating khoan thu: " + e.getMessage());
            return false; // Trả về false nếu có lỗi
        }
    }

    public KhoanThu getKhoanThu(int khoanThuId, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        KhoanThu khoanThu = null;
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + TABLE_KHOANTHU +
                    " WHERE " + COLUMN_KHOANTHU_ID + " = ? AND " + COLUMN_USER_ID_FK + " = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(khoanThuId), String.valueOf(userId)});

            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KHOANTHU_NAME));
                String ngayThu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NGAYTHU));
                int tien = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIEN));
                String ghiChu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GHICHU));
                int loaiThuId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAITHU_ID_FK));

                khoanThu = new KhoanThu(khoanThuId, name, ngayThu, tien, ghiChu, loaiThuId, userId);
            }
        } catch (Exception e) {
            Log.e("Database", "Error fetching khoan thu: " + e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return khoanThu;
    }

    public KhoanChi getKhoanChi(int khoanThuId, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        KhoanChi khoanThu = null;
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + TABLE_KHOANCHI +
                    " WHERE " + COLUMN_KHOANCHI_ID + " = ? AND " + COLUMN_USER_ID_FK + " = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(khoanThuId), String.valueOf(userId)});

            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KHOANCHI_NAME));
                String ngayThu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NGAYCHI));
                int tien = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIENCHI));
                String ghiChu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GHICHUCHI));
                int loaiThuId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAICHI_ID_FK));

                khoanThu = new KhoanChi(khoanThuId, name, ngayThu, tien, ghiChu, loaiThuId, userId);
            }
        } catch (Exception e) {
            Log.e("Database", "Error fetching khoan thu: " + e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return khoanThu;
    }


    // Insert loaithu
    public boolean insertLoaiThu(String name, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int newId = getNextLoaiThuId(userId);
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOAITHU_ID, newId);
        values.put(COLUMN_LOAITHU_NAME, name);
        values.put(COLUMN_USER_ID_FK, userId);
        long result = db.insert(TABLE_LOAITHU, null, values);
        return result != -1;
    }

    public boolean insertLoaiChi(String name, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int newId = getNextLoaiChiId(userId);
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOAICHI_ID, newId);
        values.put(COLUMN_LOAICHI_NAME, name);
        values.put(COLUMN_USER_ID_FK, userId);
        long result = db.insert(TABLE_LOAICHI, null, values);
        return result != -1;
    }

    public int getNextKhoanThuId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int nextId = 1; // ID mặc định bắt đầu từ 1
        Cursor cursor = null;
        try {
            String query = "SELECT MAX(" + COLUMN_KHOANTHU_ID + ") AS max_id FROM " + TABLE_KHOANTHU +
                    " WHERE " + COLUMN_USER_ID_FK + "=?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
            if (cursor != null && cursor.moveToFirst()) {
                int maxId = cursor.getInt(cursor.getColumnIndexOrThrow("max_id"));
                if (maxId > 0) {
                    nextId = maxId + 1;
                }
            }
        } catch (Exception e) {
            Log.e("Database", "Error calculating next ID", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return nextId;
    }

    public int getNextKhoanChiId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int nextId = 1; // ID mặc định bắt đầu từ 1
        Cursor cursor = null;
        try {
            String query = "SELECT MAX(" + COLUMN_KHOANCHI_ID + ") AS max_id FROM " + TABLE_KHOANCHI +
                    " WHERE " + COLUMN_USER_ID_FK + "=?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
            if (cursor != null && cursor.moveToFirst()) {
                int maxId = cursor.getInt(cursor.getColumnIndexOrThrow("max_id"));
                if (maxId > 0) {
                    nextId = maxId + 1;
                }
            }
        } catch (Exception e) {
            Log.e("Database", "Error calculating next ID", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return nextId;
    }

    public int getNextLoaiChiId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int nextId = 1; // ID mặc định bắt đầu từ 1
        Cursor cursor = null;
        try {
            String query = "SELECT MAX(" + COLUMN_LOAICHI_ID + ") AS max_id FROM " + TABLE_LOAICHI +
                    " WHERE " + COLUMN_USER_ID_FK + "=?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
            if (cursor != null && cursor.moveToFirst()) {
                int maxId = cursor.getInt(cursor.getColumnIndexOrThrow("max_id"));
                if (maxId > 0) {
                    nextId = maxId + 1;
                }
            }
        } catch (Exception e) {
            Log.e("Database", "Error calculating next ID", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return nextId;
    }
    public int getNextLoaiThuId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int nextId = 1; // ID mặc định bắt đầu từ 1
        Cursor cursor = null;
        try {
            String query = "SELECT MAX(" + COLUMN_LOAITHU_ID + ") AS max_id FROM " + TABLE_LOAITHU +
                    " WHERE " + COLUMN_USER_ID_FK + "=?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
            if (cursor != null && cursor.moveToFirst()) {
                int maxId = cursor.getInt(cursor.getColumnIndexOrThrow("max_id"));
                if (maxId > 0) {
                    nextId = maxId + 1;
                }
            }
        } catch (Exception e) {
            Log.e("Database", "Error calculating next ID", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return nextId;
    }

    // Update loaithu
    public boolean updateLoaiThu(int loaithuId, String name, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOAITHU_NAME, name);

        int result = db.update(TABLE_LOAITHU, values,
                COLUMN_LOAITHU_ID + "=? AND " + COLUMN_USER_ID_FK + "=?",
                new String[]{String.valueOf(loaithuId), String.valueOf(userId)});
        return result > 0;
    }

    public boolean updateLoaiChi(int loaithuId, String name, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOAICHI_NAME, name);

        int result = db.update(TABLE_LOAICHI, values,
                COLUMN_LOAICHI_ID + "=? AND " + COLUMN_USER_ID_FK + "=?",
                new String[]{String.valueOf(loaithuId), String.valueOf(userId)});
        return result > 0;
    }

    // User login
    public int login(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USERS +
                        " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password});

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
            cursor.close();
            return userId;
        }
        if (cursor != null) cursor.close();
        return -1;
    }

    private int getLastInsertedId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        return -1;
    }

    public boolean deleteLoaithu(int loaithuId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_LOAITHU,
                COLUMN_LOAITHU_ID + "=? AND " + COLUMN_USER_ID_FK + "=?",
                new String[]{String.valueOf(loaithuId), String.valueOf(userId)});
        return result > 0;
    }

    public boolean deleteLoaiChi(int loaithuId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_LOAICHI,
                COLUMN_LOAICHI_ID + "=? AND " + COLUMN_USER_ID_FK + "=?",
                new String[]{String.valueOf(loaithuId), String.valueOf(userId)});
        return result > 0;
    }

    public boolean deleteKhoanthu(int loaithuId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_KHOANTHU,
                COLUMN_KHOANTHU_ID + "=? AND " + COLUMN_USER_ID_FK + "=?",
                new String[]{String.valueOf(loaithuId), String.valueOf(userId)});
        return result > 0;
    }
    public boolean deleteKhoanchi(int loaithuId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_KHOANCHI,
                COLUMN_KHOANCHI_ID + "=? AND " + COLUMN_USER_ID_FK + "=?",
                new String[]{String.valueOf(loaithuId), String.valueOf(userId)});
        return result > 0;
    }



    // Thêm phương thức mới vào Database class

    public Boolean checkName(String name) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where username = ?", new String[]{name});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public List<LoaiThu> getAllLoaiThuWithIds(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<LoaiThu> loaiThuList = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Sửa lại câu query để thêm COLUMN_USER_ID_FK vào SELECT
            String query = "SELECT " + COLUMN_LOAITHU_ID + ", " + COLUMN_LOAITHU_NAME + ", " + COLUMN_USER_ID_FK +
                    " FROM " + TABLE_LOAITHU +
                    " WHERE " + COLUMN_USER_ID_FK + " = ?";

            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});


            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAITHU_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOAITHU_NAME));
                    int userIdFromDb = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID_FK));

                    // Debug: In ra từng bản ghi
                    Log.d("Database", "Found loaithu - ID: " + id + ", Name: " + name + ", UserId: " + userIdFromDb);

                    loaiThuList.add(new LoaiThu(id, name, userIdFromDb));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Database", "Error getting loaithu: " + e.getMessage(), e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return loaiThuList;
    }
    public List<LoaiChi> getAllLoaiChiWithIds(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<LoaiChi> loaiThuList = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Sửa lại câu query để thêm COLUMN_USER_ID_FK vào SELECT
            String query = "SELECT " + COLUMN_LOAICHI_ID + ", " + COLUMN_LOAICHI_NAME + ", " + COLUMN_USER_ID_FK +
                    " FROM " + TABLE_LOAICHI +
                    " WHERE " + COLUMN_USER_ID_FK + " = ?";

            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});


            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAICHI_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOAICHI_NAME));
                    int userIdFromDb = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID_FK));

                    // Debug: In ra từng bản ghi
                    Log.d("Database", "Found loaithu - ID: " + id + ", Name: " + name + ", UserId: " + userIdFromDb);

                    loaiThuList.add(new LoaiChi(id, name, userIdFromDb));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Database", "Error getting loaithu: " + e.getMessage(), e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return loaiThuList;
    }

    // Thêm phương thức này vào class Database
    public String getLoaiThuName(int loaiThuId, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String name = null;
        Cursor cursor = null;

        try {
            String query = "SELECT " + COLUMN_LOAITHU_NAME +
                    " FROM " + TABLE_LOAITHU +
                    " WHERE " + COLUMN_LOAITHU_ID + " = ? " +
                    " AND " + COLUMN_USER_ID_FK + " = ?";

            cursor = db.rawQuery(query,
                    new String[]{String.valueOf(loaiThuId), String.valueOf(userId)});

            if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOAITHU_NAME));
            }
        } catch (Exception e) {
            Log.e("Database", "Error getting loaithu name: " + e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return name;
    }

    public String getLoaiChiName(int loaiThuId, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String name = null;
        Cursor cursor = null;

        try {
            String query = "SELECT " + COLUMN_LOAICHI_NAME +
                    " FROM " + TABLE_LOAICHI +
                    " WHERE " + COLUMN_LOAICHI_ID + " = ? " +
                    " AND " + COLUMN_USER_ID_FK + " = ?";

            cursor = db.rawQuery(query,
                    new String[]{String.valueOf(loaiThuId), String.valueOf(userId)});

            if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOAICHI_NAME));
            }
        } catch (Exception e) {
            Log.e("Database", "Error getting loaithu name: " + e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return name;
    }
    public List<KhoanThu> getKhoanThuTheoNgay(String startDate, String endDate, int userId) {
        List<KhoanThu> khoanThuList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_KHOANTHU +
                " WHERE " + COLUMN_NGAYTHU + " BETWEEN ? AND ? " +
                " AND " + COLUMN_USER_ID_FK + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate, String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                KhoanThu khoanThu = new KhoanThu(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KHOANTHU_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KHOANTHU_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NGAYTHU)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TIEN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GHICHU)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAITHU_ID_FK)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID_FK))
                );
                khoanThuList.add(khoanThu);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return khoanThuList;
    }

    public int countKhoanThuTheoNgay(String startDate, String endDate, int userId) {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_KHOANTHU +
                " WHERE " + COLUMN_NGAYTHU + " BETWEEN ? AND ? " +
                " AND " + COLUMN_USER_ID_FK + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate, String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public List<KhoanChi> getKhoanChiTheoNgay(String startDate, String endDate, int userId) {
        List<KhoanChi> khoanThuList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_KHOANCHI +
                " WHERE " + COLUMN_NGAYCHI + " BETWEEN ? AND ? " +
                " AND " + COLUMN_USER_ID_FK + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate, String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                KhoanChi khoanThu = new KhoanChi(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KHOANCHI_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KHOANCHI_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NGAYCHI)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TIENCHI)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GHICHUCHI)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAICHI_ID_FK)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID_FK))
                );
                khoanThuList.add(khoanThu);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return khoanThuList;
    }

    public int countKhoanChiTheoNgay(String startDate, String endDate, int userId) {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_KHOANCHI +
                " WHERE " + COLUMN_NGAYCHI + " BETWEEN ? AND ? " +
                " AND " + COLUMN_USER_ID_FK + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate, String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }



}
