package com.example.btl_android.api;

import android.view.GestureDetector;

import com.example.btl_android.modal.ListSongs;
import com.example.btl_android.modal.Song;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://thantrieu.com/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
            .create(ApiService.class);

    @GET("resources/braniumapis/songs.json")
    Call<ListSongs> getSongs();
}