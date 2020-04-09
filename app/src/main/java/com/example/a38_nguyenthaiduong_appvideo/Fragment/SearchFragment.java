package com.example.a38_nguyenthaiduong_appvideo.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a38_nguyenthaiduong_appvideo.Adapter.SearchAdapter;
import com.example.a38_nguyenthaiduong_appvideo.Define.Define;
import com.example.a38_nguyenthaiduong_appvideo.Interface.IonClickAvatarSearch;
import com.example.a38_nguyenthaiduong_appvideo.Object.Search;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.example.a38_nguyenthaiduong_appvideo.databinding.ActivitySearchBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class SearchFragment extends Fragment {
    ActivitySearchBinding binding;
    String urlApi = Define.STRING_HOTVIDEO_CATEGORY;
    ArrayList<Search> searches;
    SearchAdapter searchAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String getEdtSearch;

    private IonClickAvatarSearch ionClickAvatarSearch;

    public static SearchFragment newInstance() {

        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_search, container, false);

        refreshList();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();

        //Search Function:
        binding.edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getEdtSearch = binding.edtsearch.getText().toString();
                if (getEdtSearch.equals("")) {
                    refreshList();
                } else {
                    for (int i = 0; i < searches.size(); i++) {
                        if (!(searches.get(i).getTenphim().toLowerCase().contains(getEdtSearch.toLowerCase()))) {
                            searches.remove(searches.get(i));
                        }
                    }
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Click to play
        searchAdapter.setIonClickAvatarSearch(new IonClickAvatarSearch() {
            @Override
            public void onClickAvatarSearch(Search search, int position) {
                ionClickAvatarSearch.onClickAvatarSearch(search, position);
            }
        });

        return binding.getRoot();
    }

    private void refreshList(){
        searches = new ArrayList<>();
        searchAdapter = new SearchAdapter(searches);
        binding.rvsearch.setAdapter(searchAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvsearch.setLayoutManager(layoutManager);
        new DoGetData(urlApi).execute();
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
            binding.prBarSearch.setVisibility(View.VISIBLE);
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
                    searches.add(new Search(avatar, title, url));
                }
                searchAdapter.notifyDataSetChanged();
                binding.prBarSearch.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IonClickAvatarSearch){
            ionClickAvatarSearch = (IonClickAvatarSearch) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement");
        }
    }
}
