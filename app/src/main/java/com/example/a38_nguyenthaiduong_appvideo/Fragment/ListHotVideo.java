package com.example.a38_nguyenthaiduong_appvideo.Fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a38_nguyenthaiduong_appvideo.Adapter.HotVideoAdapter;
import com.example.a38_nguyenthaiduong_appvideo.Define.Define;
import com.example.a38_nguyenthaiduong_appvideo.Interface.IonClickAvatarHotVideo;
import com.example.a38_nguyenthaiduong_appvideo.Object.HotVideo;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.example.a38_nguyenthaiduong_appvideo.databinding.RvHotVideoBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ListHotVideo extends Fragment {

    RvHotVideoBinding binding;
    List<HotVideo> hotVideos;
    HotVideoAdapter hotVideoAdapter;
    String urlApihotvideo = Define.STRING_HOTVIDEO;
    private IonClickAvatarHotVideo ionClickAvatarHotVideo;

    public static ListHotVideo newInstance() {

        Bundle args = new Bundle();

        ListHotVideo fragment = new ListHotVideo();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.rv_hot_video, container, false);

        hotVideos = new ArrayList<>();
        new DoGetData(urlApihotvideo).execute();

        hotVideoAdapter = new HotVideoAdapter(hotVideos);
        binding.rvhotvideo.setAdapter(hotVideoAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvhotvideo.setLayoutManager(layoutManager);

        hotVideoAdapter.setIonClickAvatarHotVideo(new IonClickAvatarHotVideo() {
            @Override
            public void onClickAvatarHotVideo(HotVideo hotVideo) {
                ionClickAvatarHotVideo.onClickAvatarHotVideo(hotVideo);
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
            binding.prBarHotVideo.setVisibility(View.VISIBLE);
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
                    hotVideos.add(new HotVideo(avatar, title, url));
                }
                hotVideoAdapter.notifyDataSetChanged();
                binding.prBarHotVideo.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IonClickAvatarHotVideo){
            ionClickAvatarHotVideo = (IonClickAvatarHotVideo) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement");
        }
    }
}
