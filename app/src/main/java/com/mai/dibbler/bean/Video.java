package com.mai.dibbler.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by maijuntian on 2018/6/27.
 */
@Entity
public class Video {

    @Id
    private String url;

    @Property
    private String path;

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Generated(hash = 481494426)
    public Video(String url, String path) {
        this.url = url;
        this.path = path;
    }

    @Generated(hash = 237528154)
    public Video() {
    }

    @Override
    public String toString() {
        return "Video{" +
                "url='" + url + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
