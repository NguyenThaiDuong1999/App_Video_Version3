package com.example.a38_nguyenthaiduong_appvideo.Mainactivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.a38_nguyenthaiduong_appvideo.Fragment.ListHistory;
import com.example.a38_nguyenthaiduong_appvideo.Fragment.ListVideoMain;
import com.example.a38_nguyenthaiduong_appvideo.Fragment.PlayCategoryVideo;
import com.example.a38_nguyenthaiduong_appvideo.Fragment.PlayHotVideo;
import com.example.a38_nguyenthaiduong_appvideo.Fragment.PlayVideoTrending;
import com.example.a38_nguyenthaiduong_appvideo.Fragment.Trending;
import com.example.a38_nguyenthaiduong_appvideo.Interface.IonClickAvatarCategoryVideo;
import com.example.a38_nguyenthaiduong_appvideo.Interface.IonClickAvatarHotVideo;
import com.example.a38_nguyenthaiduong_appvideo.Interface.IonClickAvatarVideoTrending;
import com.example.a38_nguyenthaiduong_appvideo.Object.Category;
import com.example.a38_nguyenthaiduong_appvideo.Object.HotVideo;
import com.example.a38_nguyenthaiduong_appvideo.Object.VideoTrending;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.example.a38_nguyenthaiduong_appvideo.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements IonClickAvatarVideoTrending, IonClickAvatarHotVideo, IonClickAvatarCategoryVideo {

    private static final String TAG = "MainActivity";

    ActivityMainBinding binding;
    ActionBar actionBar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //ẩn thanh tiêu đề
        actionBar = getSupportActionBar();
        actionBar.hide();

        //gọi Fragment chứa list video và ảnh quảng cáo ở home screen
        getFragment(new ListVideoMain());

        //
        binding.bottomNavigation.setOnNavigationItemSelectedListener(navListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    getFragment(new ListVideoMain());
                    break;
                case R.id.nav_trending:
                    getFragment(new Trending());
                    break;
                case R.id.nav_history:
                    getFragment(new ListHistory());
                    break;
            }
            return true;
        }
    };

    public void getFragment(Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getFragment: " + e.getMessage());
        }
    }

    @Override
    public void onClickAvatarVideoTrending(VideoTrending videoTrending) {
        getFragment(PlayVideoTrending.newInstance(videoTrending));
    }

    @Override
    public void onClickAvatarHotVideo(HotVideo hotVideo) {
        getFragment(PlayHotVideo.newInstance(hotVideo));
    }

    @Override
    public void onClickAvatarCategoryVideo(Category category) {
        getFragment(PlayCategoryVideo.newInstance(category));
    }
}
