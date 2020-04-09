package com.example.a38_nguyenthaiduong_appvideo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a38_nguyenthaiduong_appvideo.Interface.IonClickAvatarSearch;
import com.example.a38_nguyenthaiduong_appvideo.Object.Search;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Viewhoder>{
    ArrayList<Search> searches;
    ArrayList<Search> searchArrayList;
    Context context;
    IonClickAvatarSearch ionClickAvatarSearch;

    public SearchAdapter(ArrayList<Search> searches) {
        this.searches = searches;
    }

    public void setIonClickAvatarSearch(IonClickAvatarSearch ionClickAvatarSearch) {
        this.ionClickAvatarSearch = ionClickAvatarSearch;
    }

    @NonNull
    @Override
    public Viewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.itemlistsearch, parent, false);
        context = parent.getContext();
        SearchAdapter.Viewhoder viewhoder = new SearchAdapter.Viewhoder(view);
        return viewhoder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewhoder holder, final int position) {
        final Search search = searches.get(position);
        String avatar = search.getAvatar();
        String tenphim = search.getTenphim();

        holder.tvtenphim.setText(tenphim);
        Picasso.with(context).load(avatar).into(holder.imganhphim);

        holder.imganhphim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickAvatarSearch.onClickAvatarSearch(search, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searches.size();
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
