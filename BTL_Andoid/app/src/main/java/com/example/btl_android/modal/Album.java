package com.example.btl_android.modal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {
    private String image;
    private String author;
    private String album;
    private List<Song> songs;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public Album(String image, String author, String album) {
        this.image = image;
        this.author = author;
        this.album = album;
    }

    public Album(String image, String author, String album, List<Song> songs) {
        this.image = image;
        this.author = author;
        this.album = album;
        this.songs = songs;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public int countSongs(List<Song> songList){
        return songList.size();
    }
}
