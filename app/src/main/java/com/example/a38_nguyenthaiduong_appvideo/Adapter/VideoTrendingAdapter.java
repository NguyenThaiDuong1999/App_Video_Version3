package com.example.a38_nguyenthaiduong_appvideo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a38_nguyenthaiduong_appvideo.Interface.IonClickAvatarVideoTrending;
import com.example.a38_nguyenthaiduong_appvideo.Object.VideoTrending;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoTrendingAdapter extends RecyclerView.Adapter<VideoTrendingAdapter.ViewHoder> {
    List<VideoTrending> videoTrendings;
    Context context;
    IonClickAvatarVideoTrending ionClickAvatarVideoTrending;

    public VideoTrendingAdapter(List<VideoTrending> videoTrendings) {
        this.videoTrendings = videoTrendings;
    }

    public void setIonClickAvatarVideoTrending(IonClickAvatarVideoTrending ionClickAvatarVideoTrending) {
        this.ionClickAvatarVideoTrending = ionClickAvatarVideoTrending;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.itemlistvideotrending, parent, false);
        context = parent.getContext();
        ViewHoder viewHoder = new ViewHoder(view);
        return viewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, final int position) {
        final VideoTrending videoTrending = videoTrendings.get(position);
        String avatar = videoTrending.getAvatar();
        String tenphim = videoTrending.getTenphim();

        holder.tvtenphim.setText(tenphim);
        Picasso.with(context).load(avatar).into(holder.imganhphim);

        holder.imganhphim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickAvatarVideoTrending.onClickAvatarVideoTrending(videoTrending, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoTrendings.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        ImageView imganhphim;
        TextView tvtenphim;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imganhphim = itemView.findViewById(R.id.imganhphim);
            tvtenphim = itemView.findViewById(R.id.tvtenphim);
        }
    }
}
