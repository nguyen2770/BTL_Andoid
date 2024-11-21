package com.example.btl_android.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_android.Interface.IClickSongListener;
import com.example.btl_android.R;
import com.example.btl_android.modal.Song;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> implements Filterable {

    IClickSongListener songListener;
    List<Song> songList;
    List<Song> newSong;

    public SongsAdapter(List<Song> songList, IClickSongListener songListener) {
        this.songListener = songListener;
        this.songList = songList;
        this.newSong = songList;
    }



    @NonNull
    @Override
    public SongsAdapter.SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,parent,false);

        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsAdapter.SongViewHolder holder, int position) {
        Song song = songList.get(position);
        if(song == null){
            return;
        }
        String imageUrl = song.getImage();

        Glide.with(holder.imgv.getContext())
                .load(imageUrl)
                .into(holder.imgv);

        holder.txt_nameSongs.setText(String.valueOf(song.getTitle()));
        holder.txt_nameTG.setText(String.valueOf(song.getArtist()));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songListener.onClickSong(song);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(songList != null){
            return songList.size();
        }
        return 0;
    }



    public class SongViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_nameSongs, txt_nameTG;
        private ImageView imgv;
        private LinearLayout layout;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_nameSongs = itemView.findViewById(R.id.txt_titleMusic);
            txt_nameTG = itemView.findViewById(R.id.txt_authorMusic);
            imgv = itemView.findViewById(R.id.tmv_avataImgMusic);
            layout = itemView.findViewById(R.id.layoutSong);
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = removeAccent(charSequence.toString());
                if(strSearch.isEmpty()){
                    songList = newSong;
                }else{
                    List<Song> list = new ArrayList<>();

                    for(Song song : newSong){
                        if(removeAccent(song.getTitle()).toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(song);
                        }
                    }
                    songList = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = songList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                songList = (List<Song>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private static String removeAccent(String s){
        String normalizedString = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalizedString).replaceAll("").replaceAll("đ", "d").replaceAll("Đ", "D");
    }



}
