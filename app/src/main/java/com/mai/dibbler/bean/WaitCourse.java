package com.mai.dibbler.bean;

import java.util.List;

public class WaitCourse {

    /**
     * nextVideoPlayHint : string
     * videoPlayHint : string
     * videoWaitList : [{"introdution":"string","videoDuration":"string","videoEndTime":"string","videoId":"string","videoImageUrl":"string","videoName":"string","videoStartTime":"string","videoUrl":"string"}]
     * videoWaitPlayTotal : string
     */

    private String nextVideoPlayHint;
    private String videoPlayHint;
    private String videoWaitPlayTotal;
    private List<Course> videoWaitList;

    public String getNextVideoPlayHint() {
        return nextVideoPlayHint;
    }

    public void setNextVideoPlayHint(String nextVideoPlayHint) {
        this.nextVideoPlayHint = nextVideoPlayHint;
    }

    public String getVideoPlayHint() {
        return videoPlayHint;
    }

    public void setVideoPlayHint(String videoPlayHint) {
        this.videoPlayHint = videoPlayHint;
    }

    public String getVideoWaitPlayTotal() {
        return videoWaitPlayTotal;
    }

    public void setVideoWaitPlayTotal(String videoWaitPlayTotal) {
        this.videoWaitPlayTotal = videoWaitPlayTotal;
    }

    public List<Course> getVideoWaitList() {
        return videoWaitList;
    }

    public void setVideoWaitList(List<Course> videoWaitList) {
        this.videoWaitList = videoWaitList;
    }

}
