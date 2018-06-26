package com.mai.dibbler.bean;

import java.util.List;

/**
 * Created by mai on 2018/6/25.
 */
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
    private List<VideoWaitList> videoWaitList;

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

    public List<VideoWaitList> getVideoWaitList() {
        return videoWaitList;
    }

    public void setVideoWaitList(List<VideoWaitList> videoWaitList) {
        this.videoWaitList = videoWaitList;
    }

    public static class VideoWaitList {
        /**
         * introdution : string
         * videoDuration : string
         * videoEndTime : string
         * videoId : string
         * videoImageUrl : string
         * videoName : string
         * videoStartTime : string
         * videoUrl : string
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
}
