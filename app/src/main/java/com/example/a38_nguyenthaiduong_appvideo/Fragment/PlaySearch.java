package com.example.a38_nguyenthaiduong_appvideo.Fragment;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.a38_nguyenthaiduong_appvideo.Define.Define;
import com.example.a38_nguyenthaiduong_appvideo.Object.Search;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.example.a38_nguyenthaiduong_appvideo.databinding.ActivityPlaySearchBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class PlaySearch extends Fragment {
    ActivityPlaySearchBinding binding;

    int currentTime;
    MediaPlayer mediaPlayer;

    public static PlaySearch newInstance(Search search) {

        Bundle args = new Bundle();
        args.putSerializable(Define.STRING_URL_SEARCH, search.getUrl());
        args.putSerializable(Define.STRING_TEN_PHIM_SEARCH, search.getTenphim());
        PlaySearch fragment = new PlaySearch();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_play_search, container, false);

        mediaPlayer = new MediaPlayer();
        String layUrl = (String) getArguments().getSerializable(Define.STRING_URL_SEARCH);
        Uri uri = Uri.parse(layUrl);
        binding.vvplayvideo.setVideoURI(uri);
        binding.vvplayvideo.requestFocus();
        binding.vvplayvideo.start();
        updateSeekBar();

        String layTenPhim = (String) getArguments().getSerializable(Define.STRING_TEN_PHIM_SEARCH);
        binding.tvplayvideo.setText(layTenPhim);

        //
        getFragmentManager().beginTransaction().replace(R.id.playSearchContainer, new ListHotVideo()).commit();

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
                binding.sbplaysearch.setVisibility(View.GONE);
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
                binding.sbplaysearch.setVisibility(View.VISIBLE);
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
                        binding.sbplaysearch.setVisibility(View.GONE);
                    }
                }, 6000);
                return false;
            }
        });

        binding.sbplaysearch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentTime = binding.sbplaysearch.getProgress();
                binding.vvplayvideo.seekTo(currentTime);
            }
        });

        mediaPlayer = new MediaPlayer();
        //Get duration time
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(layUrl);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final long durationTime = mediaPlayer.getDuration();
        int hours = (int) (durationTime / (1000 * 60 * 60));
        int minutes = (int) (durationTime % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((durationTime % (1000 * 60 * 60)) % (1000 * 60) / 1000);
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
    private String milliSecondsToTimer(int milliSeconds) {
        return new SimpleDateFormat("mm:ss").format(milliSeconds);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (binding.vvplayvideo != null) {
                int mCurrentPosition = binding.vvplayvideo.getCurrentPosition();
                binding.sbplaysearch.setProgress(mCurrentPosition);
                int timeStart = binding.vvplayvideo.getCurrentPosition();
                binding.tvcurrenttime.setText(milliSecondsToTimer(timeStart));
                updateSeekBar();
            }
        }
    };

    private void updateSeekBar() {
        Handler handler = new Handler();
        handler.postDelayed(runnable, 1000);
        binding.sbplaysearch.setMax(binding.vvplayvideo.getDuration());
    }
}
