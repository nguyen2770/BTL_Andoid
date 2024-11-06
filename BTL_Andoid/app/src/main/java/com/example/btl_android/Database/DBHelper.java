package com.example.btl_android.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "musicApp.db"; // Tên cơ sở dữ liệu
    private static final int DATABASE_VERSION = 1; // Phiên bản của cơ sở dữ liệu

    // Câu lệnh SQL để tạo bảng User
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE User (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "fullName TEXT NOT NULL, " +
                    "phoneNumber TEXT NOT NULL UNIQUE, " +
                    "password TEXT NOT NULL, " +
                    "email TEXT)";

    // Câu lệnh SQL để tạo bảng FavoriteSongs
    private static final String CREATE_FAVORITE_SONGS_TABLE =
            "CREATE TABLE FavoriteSongs (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "userId INTEGER, " +                  // ID người dùng
                    "songId TEXT NOT NULL, " +            // ID bài hát từ API
                    "FOREIGN KEY (userId) REFERENCES User(id) ON DELETE CASCADE)";


    // Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tạo các bảng khi cơ sở dữ liệu được tạo lần đầu
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE); // Tạo bảng User
        db.execSQL(CREATE_FAVORITE_SONGS_TABLE); // Tạo bảng FavoriteSongs
    }

    // Nâng cấp cơ sở dữ liệu khi phiên bản thay đổi
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ nếu tồn tại
        db.execSQL("DROP TABLE IF EXISTS FavoriteSongs");
        db.execSQL("DROP TABLE IF EXISTS User");
        // Tạo lại bảng mới
        onCreate(db);
    }
}
