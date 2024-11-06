package com.example.btl_android.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.btl_android.modal.Song;

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

    // Hàm kiểm tra thông tin đăng nhập
//    public String checkUserLogin(String phoneNumber, String password) {
//        Cursor cursor = db.query("User",
//                new String[]{"fullName"},
//                "phoneNumber=? AND password=?",
//                new String[]{phoneNumber, password},
//                null, null, null);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            String fullName = cursor.getString(cursor.getColumnIndex("fullName"));
//            cursor.close();
//            return fullName; // Trả về tên người dùng nếu đăng nhập thành công
//        }
//
//        return null; // Trả về null nếu đăng nhập thất bại
//    }
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

    public Song getSongDetailsFromApi(String songId) {
        Song song = null;
        // Thực hiện yêu cầu GET tới API của bạn để lấy thông tin bài hát
        // Giả sử bạn có một phương thức thực hiện yêu cầu và trả về một đối tượng Song

        // Ví dụ sử dụng Retrofit hoặc một thư viện HTTP khác để gọi API
        // Dưới đây là mã giả để mô phỏng việc gọi API
        String url = "https://thantrieu.com/resources/braniumapis/songs.json"; // URL của API
        // Gọi API và phân tích cú pháp JSON để lấy thông tin bài hát
        // Lưu ý: Bạn sẽ cần sử dụng thư viện như Retrofit, Volley hoặc OkHttp để thực hiện việc này

        // Giả sử bạn đã gọi API và nhận được thông tin bài hát
        // Tạo một đối tượng Song từ dữ liệu nhận được
        // Ví dụ: song = new Song(songId, title, album, artist, source, image, duration, favorite, counter, replay);

        return song; // Trả về đối tượng Song
    }













}
