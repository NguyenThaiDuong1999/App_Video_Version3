package com.example.a38_nguyenthaiduong_appvideo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a38_nguyenthaiduong_appvideo.Interface.IonClickAvatarCategoryVideo;
import com.example.a38_nguyenthaiduong_appvideo.Object.Category;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHoder> {

    List<Category> categories;
    Context context;
    IonClickAvatarCategoryVideo ionClickAvatarCategoryVideo;

    public CategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }

    public void setOnClickAvatarCategoryVideo(IonClickAvatarCategoryVideo ionClickAvatarCategoryVideo) {
        this.ionClickAvatarCategoryVideo = ionClickAvatarCategoryVideo;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.itemlistcategory, parent, false);
        context = parent.getContext();
        ViewHoder viewHoder = new ViewHoder(view);
        return viewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        final Category category = categories.get(position);

        String avatar = category.getAvatar();
        String tenphim = category.getTenphim();

        holder.tvtenphim.setText(tenphim);
        Picasso.with(context).load(avatar).into(holder.imganhphim);

        holder.imganhphim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickAvatarCategoryVideo.onClickAvatarCategoryVideo(category);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
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
