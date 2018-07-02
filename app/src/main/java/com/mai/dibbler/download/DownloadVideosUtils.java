package com.mai.dibbler.download;

import com.mai.dibbler.MyApplication;
import com.mai.dibbler.bean.Course;
import com.mai.dibbler.bean.Video;
import com.mai.dibbler.db.DBManager;
import com.mai.dibbler.utils.Constant;
import com.mai.xmai_fast_lib.utils.MLog;
import com.mai.xmai_fast_lib.utils.NetUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by maijuntian on 2018/6/27.
 */
public class DownloadVideosUtils {

    List<String> finishList = new ArrayList<>();

    Set<String> videoUrls = new HashSet<>();

    boolean isDowning = false;

    static DownloadVideosUtils instance;

    DownloadVideosUtils() {
        List<Video> videos = DBManager.getInstance().getVideoDao().loadAll();
        if (videos != null) {
            for (Video video : videos) {
                finishList.add(video.getUrl());
            }
        }
    }

    public static DownloadVideosUtils getInstance() {
        if (instance == null)
            instance = new DownloadVideosUtils();
        return instance;
    }

    public void addVideoUrls(List<Course> courses) {

        Observable.just(courses).map(new Func1<List<Course>, Object>() {
            @Override
            public Object call(List<Course> courses) {
                for (Course course : courses) {
                    if (!finishList.contains(course.getVideoUrl()))
                        videoUrls.add(course.getVideoUrl());
                }
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (!isDowning)
                            startDownload();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });


    }

    public void startDownload() {
        if (!NetUtils.isNetworkAvailable(MyApplication.INSTANCE.getApplicationContext())) {
            MLog.log("网络不可用--->取消下载");
            isDowning = false;
            return;
        }
        Iterator<String> iterator = videoUrls.iterator();
        if (!iterator.hasNext()) {
            isDowning = false;
            MLog.log("全部下载完成");
            return;
        }
        isDowning = true;
        final String videoUrl = iterator.next();
        final String fileName = videoUrl.substring(videoUrl.lastIndexOf("/"));
        MLog.log("下载视频--->" + videoUrl);
        DownloadUtils.getInstance().download(videoUrl, Constant.SDCARD_DOWNLOAD_PATH, fileName, new DownloadUtils.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String path) {
                MLog.log("下载成功--->移除，继续下载下一个");
                DBManager.getInstance().getVideoDao().insertOrReplace(new Video(videoUrl, Constant.SDCARD_DOWNLOAD_PATH + "/" + fileName));
                videoUrls.remove(videoUrl);
                finishList.add(videoUrl);
                startDownload();
            }

            @Override
            public void onDownloading(int progress) {
                MLog.log("下载进度--->" + progress);
            }

            @Override
            public void onDownloadFailed() {
                MLog.log("下载失败，重新下载");
                startDownload();
            }
        });
    }
}