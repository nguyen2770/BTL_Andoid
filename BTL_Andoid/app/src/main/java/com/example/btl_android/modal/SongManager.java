package com.example.btl_android.modal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SongManager {
    private Map<String, Song> songMap = new HashMap<>();
    Map<String, Album> albumMap = new HashMap<>(); // Sử dụng HashMap để nhóm các bài hát theo album

    public void buildSongMap(List<Song> songList) {
        for (Song song : songList) {
            songMap.put(song.getId(), song);
        }
    }

    public void buildAlbumMap(List<Album> albumList) {
        for (Album album : albumList) {
            albumMap.put(album.getAlbum(), album);
        }
    }

    public Song getSongById(String id) {
        return songMap.get(id); // Trả về null nếu không tìm thấy bài hát với id đó
    }

    public int getSongBySongList(List<Song> tmp, Song song) {
        // Duyệt qua danh sách để tìm vị trí của bài hát
        for (int i = 0; i < tmp.size(); i++) {
            if (tmp.get(i).getId().equals(song.getId())) {
                return i; // Trả về vị trí của bài hát
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy bài hát
    }

    // Lấy danh sách bài hát theo tác giả
    public List<Song> getSongsByArtist(String artist) {
        List<Song> result = new ArrayList<>();
        for (Song song : songMap.values()) {
            if (song.getArtist().equalsIgnoreCase(artist)) {
                result.add(song);
            }
        }
        return result;
    }

    // Lấy danh sách bài hát theo album
    public List<Song> getSongsByAlbum(String album) {
        List<Song> result = new ArrayList<>();
        for (Song song : songMap.values()) {
            if (song.getAlbum().equalsIgnoreCase(album)) {
                result.add(song);
            }
        }
        return result;
    }

    // lấy danh sách bài hát theo ablum và tác giả
    public List<Song> getSongsByAlbumAndArtist(String album, String artist) {
        List<Song> result = new ArrayList<>();
        for (Song song : songMap.values()) {
            if (song.getAlbum().equalsIgnoreCase(album) || song.getArtist().equalsIgnoreCase(artist)) {
                result.add(song);
            }
        }
        // lấy thêm danh sách tại ít bài hát quá
        while (result.size() < 10) {
            // Chuyển đổi các giá trị của HashMap thành một danh sách
            List<Song> songList = new ArrayList<>(songMap.values());
            // Trộn danh sách
            Collections.shuffle(songList);
            // lấy ra thêm bài hát khác với bài hát đã chọn
            for (Song song : songList) {
                if (result.size() >= 10) {
                    break;
                }
                if (!result.contains(song)) {
                    result.add(song);
                }
            }
        }
        return result;
    }

    public List<Song> getRandomSong() {
        List<Song> result = new ArrayList<>();
        while (result.size() < 10) {
            // Chuyển đổi các giá trị của HashMap thành một danh sách
            List<Song> songList = new ArrayList<>(songMap.values());
            // Trộn danh sách
            Collections.shuffle(songList);
            // lấy ra thêm bài hát khác với bài hát đã chọn
            for (Song song : songList) {
                if (result.size() >= 10) {
                    break;
                }
                if (!result.contains(song)) {
                    result.add(song);
                }
            }
        }
        return result;

    }

    public List<Album> setAlbum(List<Song> songList) {
        List<Album> result = new ArrayList<>();



        for (Song song : songList) {
            String albumName = song.getAlbum();
            String imageUrl = song.getImage(); // Lấy hình ảnh từ bài hát
            String author = song.getArtist();

            // Kiểm tra xem album đã tồn tại trong albumMap chưa
            if (!albumMap.containsKey(albumName)) {
                // Tạo một album mới và thêm vào albumMap
                Album album = new Album(imageUrl, author, albumName, new ArrayList<>());
                albumMap.put(albumName, album);
            }

            // Thêm bài hát vào album tương ứng
            Album existingAlbum = albumMap.get(albumName);
            existingAlbum.addSong(song);

            if (existingAlbum.getAlbum().equals(albumName) && !existingAlbum.getAuthor().equals(author)) {
                existingAlbum.setAuthor("Nhiều tác giả");
            }

        }


        // Lấy tất cả các album từ albumMap và thêm vào result
        result.addAll(albumMap.values());

        return result; // Trả về danh sách album

    }

    // Giả sử lớp Album có phương thức getSongs() trả về danh sách các bài hát trong album
    public List<Album> sortAlbumsBySongCount(Map<String, Album> albumMap) {
        return albumMap.values().stream()
                // sắp xếp album theo số lượng bài hát, giảm dần
                .sorted((a1, a2) -> Integer.compare(a2.getSongs().size(), a1.getSongs().size()))
                // thu thập kết quả dưới dạng danh sách
                .collect(Collectors.toList());
    }

    public List<Album> getAlbumsBySongCount( int songCount) {
        List<Album> albums = sortAlbumsBySongCount(albumMap);
        List<Album> tmp = new ArrayList<>();
        int k = 0;
        while(k < songCount){
            tmp.add(albums.get(k));
            k++;
        }
        return tmp;

    }




}