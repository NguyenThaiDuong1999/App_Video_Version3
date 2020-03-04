package com.example.a38_nguyenthaiduong_appvideo.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.a38_nguyenthaiduong_appvideo.Define.Define;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.example.a38_nguyenthaiduong_appvideo.databinding.ActivityPlayVideo1Binding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiresApi(api = Build.VERSION_CODES.M)
public class PlayVideoMain extends Fragment {

    ActivityPlayVideo1Binding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int i1;
    MediaPlayer mediaPlayer;
    int currenttime;
    Set<String> layseturl = new ArraySet<>();
    List<String> laymangurl;

    public static PlayVideoMain newInstance() {

        Bundle args = new Bundle();

        PlayVideoMain fragment = new PlayVideoMain();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_play_video_1, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();
        mediaPlayer = new MediaPlayer();

        i1 = sharedPreferences.getInt(Define.STRING_VI_TRI, 1);
        final String layurl = sharedPreferences.getString(Define.STRING_URL, null);
        Uri uri = Uri.parse(layurl);
        binding.vvplayvideo.setVideoURI(uri);
        binding.vvplayvideo.requestFocus();
        binding.vvplayvideo.start();
        updateSeekBar();

        layseturl = sharedPreferences.getStringSet("mangurl", null);
        laymangurl = new ArrayList<>(layseturl);
        Collections.reverse(laymangurl);

        String laytenphim = sharedPreferences.getString(Define.STRING_TEN_PHIM, null);
        binding.tvplayvideo.setText(laytenphim);

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
                Uri uri = Uri.parse(laymangurl.get(i1+1));
                binding.vvplayvideo.setVideoURI(uri);
                binding.vvplayvideo.requestFocus();
                binding.vvplayvideo.start();
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
                binding.sbplayvideomain.setVisibility(View.GONE);
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
                binding.sbplayvideomain.setVisibility(View.VISIBLE);
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
                        binding.sbplayvideomain.setVisibility(View.GONE);
                    }
                }, 6000);
                return false;
            }
        });

        binding.sbplayvideomain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currenttime = binding.sbplayvideomain.getProgress();
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
    private String milisecondsToTimer(int miliseconds){
        return new SimpleDateFormat("mm:ss").format(miliseconds);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(binding.vvplayvideo != null){
                int mCurrentPosition = binding.vvplayvideo.getCurrentPosition();
                binding.sbplayvideomain.setProgress(mCurrentPosition);
                int timestart = binding.vvplayvideo.getCurrentPosition();
                binding.tvcurrenttime.setText(milisecondsToTimer(timestart));
                updateSeekBar();
            }
        }
    };

    private void updateSeekBar(){
        Handler handler = new Handler();
        handler.postDelayed(runnable, 1000);
        binding.sbplayvideomain.setMax(binding.vvplayvideo.getDuration());
    }

}
