package com.example.a38_nguyenthaiduong_appvideo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a38_nguyenthaiduong_appvideo.Interface.IonClickAvatarHotVideo;
import com.example.a38_nguyenthaiduong_appvideo.Object.HotVideo;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HotVideoAdapter extends RecyclerView.Adapter<HotVideoAdapter.ViewHoder> {

    List<HotVideo> hotVideos;
    Context context;
    IonClickAvatarHotVideo ionClickAvatarHotVideo;

    public HotVideoAdapter(List<HotVideo> hotVideos) {
        this.hotVideos = hotVideos;
    }

    public void setIonClickAvatarHotVideo(IonClickAvatarHotVideo ionClickAvatarHotVideo) {
        this.ionClickAvatarHotVideo = ionClickAvatarHotVideo;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.itemlisthotvideo, parent, false);
        context = parent.getContext();
        ViewHoder viewhoder = new ViewHoder(view);
        return viewhoder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        final HotVideo hotVideo = hotVideos.get(position);

        String avatar = hotVideo.getAvatar();
        String tenphim = hotVideo.getTenphim();

        holder.tvtenphim.setText(tenphim);
        Picasso.with(context).load(avatar).into(holder.imganhphim);

        holder.imganhphim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickAvatarHotVideo.onClickAvatarHotVideo(hotVideo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotVideos.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView tvtenphim;
        ImageView imganhphim;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tvtenphim = itemView.findViewById(R.id.tvtenphim);
            imganhphim = itemView.findViewById(R.id.imganhphim);
        }
    }
}
