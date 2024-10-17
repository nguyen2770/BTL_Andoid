package com.example.btl_android.modal;

import java.util.List;

public class ListSongs {
    private List<Song> songs;

    public ListSongs(List<Song> songs) {
        this.songs = songs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
