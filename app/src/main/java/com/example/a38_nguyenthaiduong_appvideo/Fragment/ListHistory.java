package com.example.a38_nguyenthaiduong_appvideo.Fragment;

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

import com.example.a38_nguyenthaiduong_appvideo.Adapter.HistoryAdapter;
import com.example.a38_nguyenthaiduong_appvideo.Define.Define_Method;
import com.example.a38_nguyenthaiduong_appvideo.Interface.IonClickAvatarHistory;
import com.example.a38_nguyenthaiduong_appvideo.Object.History;
import com.example.a38_nguyenthaiduong_appvideo.Object.Video;
import com.example.a38_nguyenthaiduong_appvideo.R;
import com.example.a38_nguyenthaiduong_appvideo.SQL.SQLHelperMain;
import com.example.a38_nguyenthaiduong_appvideo.databinding.HistoryBinding;

import java.util.ArrayList;

public class ListHistory extends Fragment {
    HistoryBinding binding;
    ArrayList<History> historyList;
    ArrayList<History> histories;
    HistoryAdapter historyAdapter;
    SQLHelperMain sqlHelperMain;
    Define_Method define_method = new Define_Method();

    public static ListHistory newInstance() {

        Bundle args = new Bundle();

        ListHistory fragment = new ListHistory();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.history, container, false);

        histories = new ArrayList<>();
        historyList = new ArrayList<>();
        sqlHelperMain = new SQLHelperMain(getContext());
        historyList = sqlHelperMain.getAllItem();

        binding.btndeleteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlHelperMain.delAll();
                historyList = sqlHelperMain.getAllItem();
                historyAdapter = new HistoryAdapter(historyList);
                binding.rvhistory.setAdapter(historyAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                binding.rvhistory.setLayoutManager(layoutManager);
            }
        });

        for (int i=historyList.size()-1; i>=0; i--){
            histories.add(historyList.get(i));
        }

        historyAdapter = new HistoryAdapter(histories);
        historyAdapter.setOnClickAvatarHistory(new IonClickAvatarHistory() {
            @Override
            public void onClickAvatarHistory(History history) {
                if (histories.isEmpty()==false && define_method.CHECK(history.getTenphim(),histories) ){
                    sqlHelperMain.deleteItem(history.getTenphim());
                }
                sqlHelperMain.insertVideo(history);
            }
        });
        binding.rvhistory.setAdapter(historyAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvhistory.setLayoutManager(layoutManager);

        return binding.getRoot();
    }
}
