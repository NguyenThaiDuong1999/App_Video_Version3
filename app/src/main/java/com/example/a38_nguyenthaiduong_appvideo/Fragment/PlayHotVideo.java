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
import com.example.a38_nguyenthaiduong_appvideo.Object.HotVideo;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.example.a38_nguyenthaiduong_appvideo.databinding.ActivityPlayHotVideoBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class PlayHotVideo extends Fragment {

    ActivityPlayHotVideoBinding binding;
    int currenttime;
    MediaPlayer mediaPlayer;

    public static PlayHotVideo newInstance(HotVideo hotVideo) {

        Bundle args = new Bundle();
        PlayHotVideo fragment = new PlayHotVideo();
        args.putSerializable(Define.STRING_URL_HOT_VIDEO, hotVideo.getUrl());
        args.putSerializable(Define.STRING_TEN_PHIM_HOT_VIDEO, hotVideo.getTenphim());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_play_hot_video, container, false);

        String layurlhotvideo = (String) getArguments().getSerializable(Define.STRING_URL_HOT_VIDEO);
        Uri uri = Uri.parse(layurlhotvideo);
        binding.vvplayvideo.setVideoURI(uri);
        binding.vvplayvideo.requestFocus();
        binding.vvplayvideo.start();
        updateSeekBar();

        String laytenphimhotvideo = (String) getArguments().getSerializable(Define.STRING_TEN_PHIM_HOT_VIDEO);
        binding.tvplayvideo.setText(laytenphimhotvideo);

        // Goi Fragment chua list hot video
        getFragmentManager().beginTransaction().replace(R.id.playhotvideocontainer, new ListHotVideo()).commit();

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
                binding.sbplayhotvideo.setVisibility(View.GONE);
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
                binding.sbplayhotvideo.setVisibility(View.VISIBLE);
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
                        binding.sbplayhotvideo.setVisibility(View.GONE);
                    }
                }, 6000);
                return false;
            }
        });

        binding.sbplayhotvideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currenttime = binding.sbplayhotvideo.getProgress();
                binding.vvplayvideo.seekTo(currenttime);
            }
        });

        //Get duration time
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(layurlhotvideo);
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
                binding.sbplayhotvideo.setProgress(mCurrentPosition);
                int timestart = binding.vvplayvideo.getCurrentPosition();
                binding.tvcurrenttime.setText(milisecondsToTimer(timestart));
                updateSeekBar();
            }
        }
    };

    private void updateSeekBar() {
        Handler handler = new Handler();
        handler.postDelayed(runnable, 1000);
        binding.sbplayhotvideo.setMax(binding.vvplayvideo.getDuration());
    }
}
