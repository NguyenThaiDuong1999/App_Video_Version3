package com.example.a38_nguyenthaiduong_appvideo.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a38_nguyenthaiduong_appvideo.Adapter.VideoTrendingAdapter;
import com.example.a38_nguyenthaiduong_appvideo.Define.Define;
import com.example.a38_nguyenthaiduong_appvideo.Define.Define_Method;
import com.example.a38_nguyenthaiduong_appvideo.Interface.IonClickAvatarVideoTrending;
import com.example.a38_nguyenthaiduong_appvideo.Object.History;
import com.example.a38_nguyenthaiduong_appvideo.Object.VideoTrending;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.example.a38_nguyenthaiduong_appvideo.SQL.SQLHelperMain;
import com.example.a38_nguyenthaiduong_appvideo.databinding.ListVideoTrendingBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ListVideoTrending extends Fragment {

    ListVideoTrendingBinding binding;
    List<VideoTrending> videoTrendings;
    VideoTrendingAdapter videoTrendingAdapter;
    String urlApihotvideo1 = Define.STRING_HOTVIDEO;
    private IonClickAvatarVideoTrending ionClickAvatarVideoTrending;

    SQLHelperMain sqlHelperMain;
    Define_Method define_method = new Define_Method();
    ArrayList<History> arrayListSQL;

    public static ListVideoTrending newInstance() {

        Bundle args = new Bundle();

        ListVideoTrending fragment = new ListVideoTrending();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.list_video_trending, container, false);

        videoTrendings = new ArrayList<>();
        new DoGetData(urlApihotvideo1).execute();

        videoTrendingAdapter = new VideoTrendingAdapter(videoTrendings);
        binding.rvlistvideotrending.setAdapter(videoTrendingAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvlistvideotrending.setLayoutManager(layoutManager);

        videoTrendingAdapter.setIonClickAvatarVideoTrending(new IonClickAvatarVideoTrending() {
            @Override
            public void onClickAvatarVideoTrending(VideoTrending videoTrending) {
                ionClickAvatarVideoTrending.onClickAvatarVideoTrending(videoTrending);
                History item = new History(videoTrending.getAvatar(),videoTrending.getTenphim(),videoTrending.getUrl());
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
            binding.prBarListVideoTrending.setVisibility(View.VISIBLE);
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
                    videoTrendings.add(new VideoTrending(avatar, title, url));
                }
                videoTrendingAdapter.notifyDataSetChanged();
                binding.prBarListVideoTrending.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IonClickAvatarVideoTrending){
            ionClickAvatarVideoTrending = (IonClickAvatarVideoTrending) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement");
        }
    }
}
