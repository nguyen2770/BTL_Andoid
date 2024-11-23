package com.example.btl_android.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private Context context;
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    //Viet phuong thuc khoi tao
    public DBManager(Context context) {
        this.context = context;
    }
    //Viet phuong thuc mo ket noi
    public DBManager open(){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }
    //Viet phuong thuc dong ket noi
    public void close(){
        dbHelper.close();
    }

    public void addUser(String fullName, String phoneNumber, String password, String email) {
        ContentValues values = new ContentValues();
        values.put("fullName", fullName);
        values.put("phoneNumber", phoneNumber);
        values.put("password", password);
        values.put("email", email);

        db.insert("User", null, values);
    }

    public String[] checkUserLogin(String phoneNumber, String password) {
        // Thực hiện truy vấn để kiểm tra thông tin đăng nhập
        Cursor cursor = db.query("User", new String[]{"id", "fullName"}, "phoneNumber=? AND password=?",
                new String[]{phoneNumber, password}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String id = cursor.getString(cursor.getColumnIndex("id")); // Lấy id
            String fullName = cursor.getString(cursor.getColumnIndex("fullName")); // Lấy fullName
            cursor.close();
            return new String[]{id, fullName}; // Trả về một mảng chứa id và fullName
        }
        return null; // Nếu không tìm thấy, trả về null
    }

    public boolean isFavoriteSong(int userId, String songId) {
        if (db == null) {
            throw new IllegalStateException("Database is not opened. Call open() before using the database.");
        }
        String query = "SELECT * FROM FavoriteSongs WHERE userId = ? AND songId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), songId});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


    public void addFavoriteSong(int userId, String songId) {
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("songId", songId);

        db.insert("FavoriteSongs", null, values);
    }
    public void removeFavoriteSong(int userId, String songId) {
        db.delete("FavoriteSongs", "userId = ? AND songId = ?", new String[]{String.valueOf(userId), songId});
    }

    public List<String> getFavoriteSongIds(int userId) {
        List<String> songIds = new ArrayList<>();
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("FavoriteSongs", new String[]{"songId"}, "userId = ?", new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String songId = cursor.getString(cursor.getColumnIndex("songId"));
                    songIds.add(songId); // Lưu songId vào danh sách
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return songIds;
    }

}
