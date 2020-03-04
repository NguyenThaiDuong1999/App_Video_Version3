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
import com.example.a38_nguyenthaiduong_appvideo.Object.VideoTrending;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.example.a38_nguyenthaiduong_appvideo.databinding.ActivityPlayVideoBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class PlayVideoTrending extends Fragment {

    ActivityPlayVideoBinding binding;
    int currenttime;
    MediaPlayer mediaPlayer;

    public static PlayVideoTrending newInstance(VideoTrending videoTrending) {

        Bundle args = new Bundle();
        PlayVideoTrending fragment = new PlayVideoTrending();
        args.putSerializable(Define.STRING_URL_VIDEO_TRENDING, videoTrending.getUrl());
        args.putSerializable(Define.STRING_TEN_PHIM_VIDEO_TRENDING, videoTrending.getTenphim());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_play_video, container, false);

        mediaPlayer = new MediaPlayer();
        String layurl = (String) getArguments().getSerializable(Define.STRING_URL_VIDEO_TRENDING);
        Uri uri = Uri.parse(layurl);
        binding.vvplayvideo.setVideoURI(uri);
        binding.vvplayvideo.requestFocus();
        binding.vvplayvideo.start();
        updateSeekBar();

        String laytenphim = (String) getArguments().getSerializable(Define.STRING_TEN_PHIM_VIDEO_TRENDING);
        binding.tvplayvideo.setText(laytenphim);

        // Goi Fragment chua list video trending
        getFragmentManager().beginTransaction().replace(R.id.playvideotrendingcontainer, new ListVideoTrending()).commit();

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
                binding.sbplayvideotrending.setVisibility(View.GONE);
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
                binding.sbplayvideotrending.setVisibility(View.VISIBLE);
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
                        binding.sbplayvideotrending.setVisibility(View.GONE);
                    }
                }, 6000);
                return false;
            }
        });

        binding.sbplayvideotrending.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currenttime = binding.sbplayvideotrending.getProgress();
                binding.vvplayvideo.seekTo(currenttime);
            }
        });

        //Get duration time
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(layurl);
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
                binding.sbplayvideotrending.setProgress(mCurrentPosition);
                int timestart = binding.vvplayvideo.getCurrentPosition();
                binding.tvcurrenttime.setText(milisecondsToTimer(timestart));
                updateSeekBar();
            }
        }
    };

    private void updateSeekBar() {
        Handler handler = new Handler();
        handler.postDelayed(runnable, 1000);
        binding.sbplayvideotrending.setMax(binding.vvplayvideo.getDuration());
    }

}
