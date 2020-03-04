package com.example.a38_nguyenthaiduong_appvideo.Object;

import java.io.Serializable;

public class Video implements Serializable {

    String avatar;
    String tenphim;
    String url;

    public Video(String avatar, String tenphim, String url) {
        this.avatar = avatar;
        this.tenphim = tenphim;
        this.url = url;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTenphim() {
        return tenphim;
    }

    public void setTenphim(String tenphim) {
        this.tenphim = tenphim;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
