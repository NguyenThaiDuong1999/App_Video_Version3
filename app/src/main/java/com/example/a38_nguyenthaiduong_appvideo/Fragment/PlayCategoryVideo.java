package com.example.a38_nguyenthaiduong_appvideo.Fragment;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.a38_nguyenthaiduong_appvideo.Define.Define;
import com.example.a38_nguyenthaiduong_appvideo.Object.Category;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.example.a38_nguyenthaiduong_appvideo.databinding.ActivityPlayCategoryVideoBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class PlayCategoryVideo extends Fragment {
    ActivityPlayCategoryVideoBinding binding;
    int currenttime;
    MediaPlayer mediaPlayer;

    public static PlayCategoryVideo newInstance(Category category) {

        Bundle args = new Bundle();
        args.putSerializable(Define.STRING_URL_CATEGORY_VIDEO, category.getUrl());
        args.putSerializable(Define.STRING_TEN_PHIM_CATEGORY_VIDEO, category.getTenphim());
        PlayCategoryVideo fragment = new PlayCategoryVideo();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_play_category_video, container, false);

        String layurlcategoryvideo = (String) getArguments().getSerializable(Define.STRING_URL_CATEGORY_VIDEO);
        Uri uri = Uri.parse(layurlcategoryvideo);
        binding.vvplayvideo.setVideoURI(uri);
        binding.vvplayvideo.requestFocus();
        binding.vvplayvideo.start();
        updateSeekBar();

        String laytenphimcategoryvideo = (String) getArguments().getSerializable(Define.STRING_TEN_PHIM_CATEGORY_VIDEO);
        binding.tvplayvideo.setText(laytenphimcategoryvideo);

        // Goi Fragment chua list category video
        getFragmentManager().beginTransaction().replace(R.id.playcategoryvideocontainer, new ListCategoryVideo()).commit();

        binding.icplayvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.vvplayvideo.start();
                binding.icplayvideo.setVisibility(View.GONE);
                binding.icpausevideo.setVisibility(View.VISIBLE);
            }
        });
        binding.icpausevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.vvplayvideo.pause();
                binding.icpausevideo.setVisibility(View.GONE);
                binding.icplayvideo.setVisibility(View.VISIBLE);
            }
        });


        binding.icnextvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.icbackvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.icnextvideo.setVisibility(View.GONE);
                binding.icpausevideo.setVisibility(View.GONE);
                binding.icplayvideo.setVisibility(View.GONE);
                binding.icbackvideo.setVisibility(View.GONE);
                binding.tvcurrenttime.setVisibility(View.GONE);
                binding.tvdurationtime.setVisibility(View.GONE);
                binding.imgzoomout.setVisibility(View.GONE);
                binding.sbplaycategoryvideo.setVisibility(View.GONE);
            }
        }, 6000);

        binding.vvplayvideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.icnextvideo.setVisibility(View.VISIBLE);
                binding.icpausevideo.setVisibility(View.VISIBLE);
                binding.icbackvideo.setVisibility(View.VISIBLE);
                binding.tvcurrenttime.setVisibility(View.VISIBLE);
                binding.tvdurationtime.setVisibility(View.VISIBLE);
                binding.imgzoomout.setVisibility(View.VISIBLE);
                binding.sbplaycategoryvideo.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.icnextvideo.setVisibility(View.GONE);
                        binding.icpausevideo.setVisibility(View.GONE);
                        binding.icplayvideo.setVisibility(View.GONE);
                        binding.icbackvideo.setVisibility(View.GONE);
                        binding.tvcurrenttime.setVisibility(View.GONE);
                        binding.tvdurationtime.setVisibility(View.GONE);
                        binding.imgzoomout.setVisibility(View.GONE);
                        binding.sbplaycategoryvideo.setVisibility(View.GONE);
                    }
                }, 6000);
                return false;
            }
        });

        binding.sbplaycategoryvideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currenttime = binding.sbplaycategoryvideo.getProgress();
                binding.vvplayvideo.seekTo(currenttime);
            }
        });

        //Get duration time
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(layurlcategoryvideo);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final long durationtime = mediaPlayer.getDuration();
        int hours = (int) (durationtime / (1000 * 60 * 60));
        int minutes = (int) (durationtime % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((durationtime % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (hours > 0) {
            binding.tvdurationtime.setText(hours + ":" + minutes + ":" + seconds);
        } else {
            if(seconds > 10)
                binding.tvdurationtime.setText(minutes + ":" + seconds);
            else
                binding.tvdurationtime.setText(minutes + ":0" + seconds);
        }

        return binding.getRoot();
    }

    @SuppressLint("SimpleDateFormat")
    private String milisecondsToTimer(int miliseconds) {
        return new SimpleDateFormat("mm:ss").format(miliseconds);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (binding.vvplayvideo != null) {
                int mCurrentPosition = binding.vvplayvideo.getCurrentPosition();
                binding.sbplaycategoryvideo.setProgress(mCurrentPosition);
                int timestart = binding.vvplayvideo.getCurrentPosition();
                binding.tvcurrenttime.setText(milisecondsToTimer(timestart));
                updateSeekBar();
            }
        }
    };

    private void updateSeekBar() {
        Handler handler = new Handler();
        handler.postDelayed(runnable, 1000);
        binding.sbplaycategoryvideo.setMax(binding.vvplayvideo.getDuration());
    }
}
