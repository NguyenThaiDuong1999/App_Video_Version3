package com.example.a38_nguyenthaiduong_appvideo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a38_nguyenthaiduong_appvideo.Interface.IonClickAvatar;
import com.example.a38_nguyenthaiduong_appvideo.Object.Video;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoMainAdapter extends RecyclerView.Adapter<VideoMainAdapter.Viewhoder> {

    List<Video> videos;
    Context context;
    IonClickAvatar ionClickAvatar;

    public VideoMainAdapter(List<Video> videos) {
        this.videos = videos;
    }

    public void setIonClickAvatar(IonClickAvatar ionClickAvatar) {
        this.ionClickAvatar = ionClickAvatar;
    }

    @NonNull
    @Override
    public Viewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_video, parent, false);
        context = parent.getContext();
        Viewhoder viewhoder = new Viewhoder(view);
        return viewhoder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewhoder holder, final int position) {
        final Video video = videos.get(position);
        String avatar = video.getAvatar();
        String tenphim = video.getTenphim();

        holder.tvtenphim.setText(tenphim);
        Picasso.with(context).load(avatar).into(holder.imganhphim);

        holder.imganhphim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickAvatar.onClickAvatar(video, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class Viewhoder extends RecyclerView.ViewHolder {
        ImageView imganhphim;
        TextView tvtenphim;

        public Viewhoder(@NonNull View itemView) {
            super(itemView);

            imganhphim = itemView.findViewById(R.id.imganhphim);
            tvtenphim = itemView.findViewById(R.id.tvtenphim);
        }
    }
}
