package com.example.a38_nguyenthaiduong_appvideo.Define;

import com.example.a38_nguyenthaiduong_appvideo.Object.History;

import java.util.ArrayList;

public class Define_Method {
    public  boolean CHECK (String tenphim, ArrayList<History> arrayList){
        for (History history:arrayList){
            if (tenphim.equals(history.getTenphim())==true){
                return true;
            }
        }
        return false;
    }
}
