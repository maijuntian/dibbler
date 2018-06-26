package com.mai.dibbler.bean;

/**
 * Created by mai on 2018/6/25.
 */
public class Course {


    /**
     * videoId : 1e5aecea5c63461699cd98b20e088761
     * videoImageUrl : http://imgdata1.healthmall.cn/group1/M00/D9/3C/CgAAUljHV1SARQQGAAIwPvLnjk0459.jpg
     * videoUrl : http://vod.healthmall.cn/b638d0c7c0574661ab9b10a08399c8b2/c3274a64114b458d867e30045b0da987-699a80a29a15c59c9d0868389f86cc3f.mp4
     * videoName : TRX悬挂健身系统
     * videoDuration : 03:25
     * introdution :    TRX悬挂健身系统可以帮助训练者完成几乎全身肌肉的训练，提高力量、柔韧性和核心稳定性。
     */

    private String introdution;
    private String videoDuration;
    private String videoEndTime;
    private String videoId;
    private String videoImageUrl;
    private String videoName;
    private String videoStartTime;
    private String videoUrl;

    public String getIntrodution() {
        return introdution;
    }

    public void setIntrodution(String introdution) {
        this.introdution = introdution;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getVideoEndTime() {
        return videoEndTime;
    }

    public void setVideoEndTime(String videoEndTime) {
        this.videoEndTime = videoEndTime;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoImageUrl() {
        return videoImageUrl;
    }

    public void setVideoImageUrl(String videoImageUrl) {
        this.videoImageUrl = videoImageUrl;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoStartTime() {
        return videoStartTime;
    }

    public void setVideoStartTime(String videoStartTime) {
        this.videoStartTime = videoStartTime;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
