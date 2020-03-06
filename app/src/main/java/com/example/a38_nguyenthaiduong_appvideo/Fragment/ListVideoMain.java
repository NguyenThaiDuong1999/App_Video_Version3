package com.example.a38_nguyenthaiduong_appvideo.Fragment;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a38_nguyenthaiduong_appvideo.Adapter.VideoMainAdapter;
import com.example.a38_nguyenthaiduong_appvideo.Define.Define;
import com.example.a38_nguyenthaiduong_appvideo.Define.Define_Method;
import com.example.a38_nguyenthaiduong_appvideo.Interface.IonClickAvatar;
import com.example.a38_nguyenthaiduong_appvideo.Object.History;
import com.example.a38_nguyenthaiduong_appvideo.Object.Video;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.example.a38_nguyenthaiduong_appvideo.SQL.SQLHelperMain;
import com.example.a38_nguyenthaiduong_appvideo.databinding.RvMainBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiresApi(api = Build.VERSION_CODES.M)
public class ListVideoMain extends Fragment {

    RvMainBinding binding;
    String urlApi = Define.STRING_HOTVIDEO_CATEGORY;
    ArrayList<Video> videos;
    VideoMainAdapter videoAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Set<String> seturl = new ArraySet<>();

    SQLHelperMain sqlHelperMain;
    Define_Method define_method = new Define_Method();
    ArrayList<History> arrayListSQL;

    public static ListVideoMain newInstance() {

        Bundle args = new Bundle();
        ListVideoMain fragment = new ListVideoMain();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.rv_main, container, false);
        videos = new ArrayList<>();
        videoAdapter = new VideoMainAdapter(videos);
        binding.rvmain.setAdapter(videoAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        binding.rvmain.setLayoutManager(layoutManager);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();

        new DoGetData(urlApi).execute();

        // Gọi fragment animationDrawer
        getFragmentManager().beginTransaction().replace(R.id.animationcontainer, new AnimationDraw()).commit();

        videoAdapter.setIonClickAvatar(new IonClickAvatar() {
            @Override
            public void onClickAvatar(Video video, int position) {
                editor.putInt(Define.STRING_VI_TRI, position);
                editor.putString(Define.STRING_TEN_PHIM, video.getTenphim());
                editor.putString(Define.STRING_URL, video.getUrl());
                editor.putStringSet("seturl", seturl);
                editor.commit();
                getFragmentManager().beginTransaction().replace(R.id.animationcontainer, new PlayVideoMain()).commit();

                History item = new History(video.getAvatar(),video.getTenphim(),video.getUrl());
                sqlHelperMain = new SQLHelperMain(getContext());
                arrayListSQL = sqlHelperMain.getAllItem();

                if (arrayListSQL.isEmpty()==false && define_method.CHECK(item.getTenphim(),arrayListSQL)){
                    sqlHelperMain.deleteItem(item.getTenphim());
                }
                sqlHelperMain.insertVideo(item);
            }
        });

        return binding.getRoot();
    }

    class DoGetData extends AsyncTask<Void, Void, Void> {
        String result = "";
        String urlApi;

        public DoGetData(String urlApi) {
            this.urlApi = urlApi;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.prBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(urlApi);
                URLConnection connection = url.openConnection();
                InputStream is = connection.getInputStream();
                int byteCharacter;
                while ((byteCharacter = is.read()) != -1) {
                    result += (char) byteCharacter;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String avatar = jsonObject.getString("avatar");
                    String title = jsonObject.getString("title");
                    String url = jsonObject.getString("file_mp4");
                    videos.add(new Video(avatar, title, url));
                    seturl.add(url);
                }
                videoAdapter.notifyDataSetChanged();
                binding.prBar.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
