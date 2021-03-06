package com.example.a38_nguyenthaiduong_appvideo.Fragment;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.a38_nguyenthaiduong_appvideo.Define.Define;
import com.example.a38_nguyenthaiduong_appvideo.Object.VideoTrending;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.example.a38_nguyenthaiduong_appvideo.databinding.ActivityPlayVideoTrendingBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class PlayVideoTrending extends Fragment {

    ActivityPlayVideoTrendingBinding binding;
    int currentTime;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_play_video_trending, container, false);

        mediaPlayer = new MediaPlayer();
        String layUrl = (String) getArguments().getSerializable(Define.STRING_URL_VIDEO_TRENDING);
        Uri uri = Uri.parse(layUrl);
        binding.vvplayvideo.setVideoURI(uri);
        binding.vvplayvideo.requestFocus();
        binding.vvplayvideo.start();
        updateSeekBar();

        String layTenPhim = (String) getArguments().getSerializable(Define.STRING_TEN_PHIM_VIDEO_TRENDING);
        binding.tvplayvideo.setText(layTenPhim);

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
                currentTime = binding.sbplayvideotrending.getProgress();
                binding.vvplayvideo.seekTo(currentTime);
            }
        });

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

        //MinimizeScreen
        binding.imgzoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imgzoomin.setVisibility(View.GONE);
                binding.imgzoomout.setVisibility(View.VISIBLE);
                binding.tvplayvideo.setVisibility(View.VISIBLE);
                binding.vvplayvideo.setVisibility(View.VISIBLE);

                int getTimeCurrent = binding.vvplayvideo.getCurrentPosition();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) binding.relaVideoView.getLayoutParams();
                params1.width = params.MATCH_PARENT;
                params1.height = 600;
                binding.relaVideoView.setLayoutParams(params1);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                binding.vvplayvideo.seekTo(getTimeCurrent);
            }
        });
        //MaximizeScreen
        binding.imgzoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imgzoomout.setVisibility(View.GONE);
                binding.imgzoomin.setVisibility(View.VISIBLE);
                binding.tvplayvideo.setVisibility(View.GONE);
                binding.vvplayvideo.setVisibility(View.GONE);

                int getTimeCurrent = binding.vvplayvideo.getCurrentPosition();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) binding.relaVideoView.getLayoutParams();
                params1.width = params.MATCH_PARENT;
                params1.height = params1.MATCH_PARENT;
                binding.relaVideoView.setLayoutParams(params);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                binding.vvplayvideo.seekTo(getTimeCurrent);
            }
        });

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
                binding.sbplayvideotrending.setProgress(mCurrentPosition);
                int timeStart = binding.vvplayvideo.getCurrentPosition();
                binding.tvcurrenttime.setText(milliSecondsToTimer(timeStart));
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
