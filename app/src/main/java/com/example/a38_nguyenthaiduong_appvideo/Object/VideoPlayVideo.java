package com.example.a38_nguyenthaiduong_appvideo.Object;

import java.io.Serializable;

public class VideoPlayVideo implements Serializable {
    String avatar;
    String tenphim;

    public VideoPlayVideo(String avatar, String tenphim) {
        this.avatar = avatar;
        this.tenphim = tenphim;
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
}
