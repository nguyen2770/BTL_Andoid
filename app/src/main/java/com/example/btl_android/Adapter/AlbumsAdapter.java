package com.example.btl_android.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_android.Interface.IClickAlbumListener;
import com.example.btl_android.R;
import com.example.btl_android.modal.Album;
import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {
    List<Album> albums;
    IClickAlbumListener listener;

    public AlbumsAdapter(List<Album> albums, IClickAlbumListener listener) {
        this.albums = albums;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlbumsAdapter.AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_author, parent, false);

        return new AlbumsAdapter.AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumsAdapter.AlbumViewHolder holder, int position) {
        Album album = albums.get(position);
        if (album == null) {
            return;
        }
        String imageUrl = album.getImage();

        Glide.with(holder.imgv.getContext())
                .load(imageUrl)
                .into(holder.imgv);

        holder.txt_nameAlbum.setText(String.valueOf(album.getAlbum()));
        holder.txt_nameTG.setText(String.valueOf(album.getAuthor()));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickAlbum(album);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (albums != null) {
            return albums.size();
        }
        return 0;
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_nameAlbum, txt_nameTG;
        private ImageView imgv;
        private LinearLayout layout;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_nameAlbum = itemView.findViewById(R.id.txt_titleAlbum);
            txt_nameTG = itemView.findViewById(R.id.txt_authorAlbum);
            imgv = itemView.findViewById(R.id.tmv_avataImgAl);
            layout = itemView.findViewById(R.id.layoutAlbum);
        }
    }
}
