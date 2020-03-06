package com.example.a38_nguyenthaiduong_appvideo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a38_nguyenthaiduong_appvideo.Interface.IonClickAvatarHistory;
import com.example.a38_nguyenthaiduong_appvideo.Object.History;
import com.example.a38_nguyenthaiduong_appvideo.Object.Video;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHoder>{

    ArrayList<History> histories;
    Context context;
    IonClickAvatarHistory ionClickAvatarHistory;

    public HistoryAdapter(ArrayList<History> histories) {
        this.histories = histories;
    }

    public void setOnClickAvatarHistory(IonClickAvatarHistory ionClickAvatarHistory) {
        this.ionClickAvatarHistory = ionClickAvatarHistory;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_history, parent, false);
        context = parent.getContext();
        HistoryAdapter.ViewHoder viewhoder = new HistoryAdapter.ViewHoder(view);
        return viewhoder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        final History history = histories.get(position);

        String avatar = history.getAvatar();
        String tenphim = history.getTenphim();

        holder.tvtenphim.setText(tenphim);
        Picasso.with(context).load(avatar).into(holder.imganhphim);

        holder.imganhphim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickAvatarHistory.onClickAvatarHistory(history);
            }
        });
    }

    @Override
    public int getItemCount() {
        return histories.size();
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
