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

import com.example.a38_nguyenthaiduong_appvideo.Adapter.CategoryAdapter;
import com.example.a38_nguyenthaiduong_appvideo.Define.Define;
import com.example.a38_nguyenthaiduong_appvideo.Define.Define_Method;
import com.example.a38_nguyenthaiduong_appvideo.Interface.IonClickAvatarCategoryVideo;
import com.example.a38_nguyenthaiduong_appvideo.Object.Category;
import com.example.a38_nguyenthaiduong_appvideo.Object.History;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.example.a38_nguyenthaiduong_appvideo.SQL.SQLHelperMain;
import com.example.a38_nguyenthaiduong_appvideo.databinding.RvCategoryBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ListCategoryVideo extends Fragment {
    RvCategoryBinding binding;
    List<Category> categories;
    CategoryAdapter categoryAdapter;
    String urlApiCategory = Define.STRING_CATEGORY;
    private IonClickAvatarCategoryVideo ionClickAvatarCategoryVideo;

    SQLHelperMain sqlHelperMain;
    Define_Method define_method = new Define_Method();
    ArrayList<History> arrayListSQL;

    public static ListCategoryVideo newInstance() {

        Bundle args = new Bundle();

        ListCategoryVideo fragment = new ListCategoryVideo();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.rv_category, container, false);

        categories = new ArrayList<>();
        new DoGetData(urlApiCategory).execute();

        categoryAdapter = new CategoryAdapter(categories);
        binding.rvcategory.setAdapter(categoryAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvcategory.setLayoutManager(layoutManager);

        categoryAdapter.setOnClickAvatarCategoryVideo(new IonClickAvatarCategoryVideo() {
            @Override
            public void onClickAvatarCategoryVideo(Category category) {
                ionClickAvatarCategoryVideo.onClickAvatarCategoryVideo(category);

                History item = new History(category.getAvatar(),category.getTenphim(),category.getUrl());
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

    class DoGetData extends AsyncTask<Void, Void, Void>{
        String result = "";
        String urlApi;

        public DoGetData(String urlApi) {
            this.urlApi = urlApi;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.prBarCategory.setVisibility(View.VISIBLE);
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
                    categories.add(new Category(avatar, title, url));
                }
                categoryAdapter.notifyDataSetChanged();
                binding.prBarCategory.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IonClickAvatarCategoryVideo){
            ionClickAvatarCategoryVideo = (IonClickAvatarCategoryVideo) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement");
        }
    }
}
