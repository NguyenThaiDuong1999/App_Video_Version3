package com.example.a38_nguyenthaiduong_appvideo.Fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.a38_nguyenthaiduong_appvideo.R;
import com.example.a38_nguyenthaiduong_appvideo.databinding.AnimationdrawBinding;

public class AnimationDraw extends Fragment {
    AnimationdrawBinding binding;

    public static AnimationDraw newInstance() {

        Bundle args = new Bundle();

        AnimationDraw fragment = new AnimationDraw();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.animationdraw, container, false);
        //Animation Drawer
        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ad1), 3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ad2), 3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ad3), 3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ad4), 3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ad5), 3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ad6), 3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ad7), 3000);
        animationDrawable.setOneShot(false);
        binding.imganimationdrawer.setImageDrawable(animationDrawable);
        animationDrawable.start();
        //Moving TextView
        binding.tvquangcao.setSelected(true);

        return binding.getRoot();
    }
}
