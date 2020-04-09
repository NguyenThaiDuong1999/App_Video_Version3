package com.example.a38_nguyenthaiduong_appvideo.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.a38_nguyenthaiduong_appvideo.Define.Define;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.example.a38_nguyenthaiduong_appvideo.databinding.ActivityPlayVideoMainBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiresApi(api = Build.VERSION_CODES.M)
public class PlayVideoMain extends Fragment {

    ActivityPlayVideoMainBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int i1;
    MediaPlayer mediaPlayer;
    int currentTime;
    Set<String> layseturl = new ArraySet<>();
    List<String> laymangurl;

    int x1, y1;
    AudioManager audioManager;
    Handler handler = new Handler();
    boolean reChangeVol = true;
    boolean reChangePosition = true;
    int maxVol, stepVol, currentVol;

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
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_play_video_main, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();
        mediaPlayer = new MediaPlayer();

        i1 = sharedPreferences.getInt(Define.STRING_VI_TRI, 1);
        final String layUrl = sharedPreferences.getString(Define.STRING_URL, null);
        Uri uri = Uri.parse(layUrl);
        binding.vvplayvideo.setVideoURI(uri);
        binding.vvplayvideo.requestFocus();
        binding.vvplayvideo.start();
        updateSeekBar();

        layseturl = sharedPreferences.getStringSet("mangurl", null);
        laymangurl = new ArrayList<>(layseturl);
        Collections.reverse(laymangurl);

        String layTenPhim = sharedPreferences.getString(Define.STRING_TEN_PHIM, null);
        binding.tvplayvideo.setText(layTenPhim);

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
                binding.imgzoomin.setVisibility(View.GONE);
                binding.sbplayvideomain.setVisibility(View.GONE);
            }
        }, 6000);

        binding.sbplayvideomain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentTime = binding.sbplayvideomain.getProgress();
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

    //SetVolume
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        stepVol = 100 / maxVol;
        currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        currentVol = currentVol * stepVol;
        binding.tvvolumeup.setText(String.valueOf(currentVol));

    //ControlVolume
        binding.vvplayvideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = (int) motionEvent.getX();
                        y1 = (int) motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        binding.lltimerewind.setVisibility(View.VISIBLE);
                        binding.tvgetdurationtime.setText(milliSecondsToTimer(binding.vvplayvideo.getDuration()));
                        if (Math.abs(motionEvent.getX() - x1) > 50) {
                            reChangeVol = false;
                        }
                        if (Math.abs(motionEvent.getY() - y1) > 50) {
                            reChangePosition = false;
                        }
                        if (Math.abs(motionEvent.getX() - x1) > 50 && reChangePosition) {
                            int timeCurrent = (binding.vvplayvideo.getCurrentPosition() + (int) motionEvent.getX() - x1);
                            binding.vvplayvideo.seekTo(timeCurrent);
                            binding.tvgetcurrenttime.setText(milliSecondsToTimer(timeCurrent));

                        }

                        if (Math.abs(motionEvent.getY() - y1) > 50 && reChangeVol) {
                            binding.llvolumerewind.setVisibility(View.VISIBLE);
                            if (motionEvent.getY() - y1 < 0 && currentVol < 100) {
                                currentVol++;
                                binding.tvvolumeup.setText(String.valueOf(currentVol));
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol / stepVol, 0);
                                // audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                            } else if (motionEvent.getY() - y1 > 0 && currentVol > 0) {
                                currentVol--;
                                binding.tvvolumeup.setText(String.valueOf(currentVol));
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol / stepVol, 0);
                                // audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        binding.lltimerewind.setVisibility(View.INVISIBLE);
                        binding.llvolumerewind.setVisibility(View.INVISIBLE);
                        reChangeVol = true;
                        reChangePosition = true;
                        break;
                }
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
                        binding.imgzoomin.setVisibility(View.GONE);
                        binding.sbplayvideomain.setVisibility(View.GONE);
                    }
                }, 6000);
                return true;
            }
        });

        return binding.getRoot();
    }

    @SuppressLint("SimpleDateFormat")
    private String milliSecondsToTimer(int milliSeconds){
        return new SimpleDateFormat("mm:ss").format(milliSeconds);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(binding.vvplayvideo != null){
                int mCurrentPosition = binding.vvplayvideo.getCurrentPosition();
                binding.sbplayvideomain.setProgress(mCurrentPosition);
                int timeStart = binding.vvplayvideo.getCurrentPosition();
                binding.tvcurrenttime.setText(milliSecondsToTimer(timeStart));
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
