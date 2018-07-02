package com.mai.dibbler.delegate;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.mai.dibbler.R;
import com.mai.dibbler.base.BaseDelegate;
import com.mai.dibbler.bean.Course;
import com.mai.dibbler.bean.Video;
import com.mai.dibbler.db.DBManager;
import com.mai.dibbler.utils.MGlide;
import com.mai.xmai_fast_lib.utils.MLog;

import butterknife.Bind;

/**
 * Created by maijuntian on 2018/6/28.
 */
public class CourseDetailDelegate extends BaseDelegate {
    @Bind(R.id.iv_return)
    ImageView ivReturn;
    @Bind(R.id.iv_course_img)
    ImageView ivCourseImg;
    @Bind(R.id.vv_course_video)
    VideoView vvCourseVideo;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_play)
    TextView tvPlay;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_corse_detail;
    }

    public void showCourse(Course course) {

        MLog.log("查询--->" + course.getVideoUrl());
        Video video = DBManager.getInstance().getVideoDao().load(course.getVideoUrl());

        if (video != null) {
            MLog.log("结果--->" + video.toString());
            vvCourseVideo.setVisibility(View.VISIBLE);
            ivCourseImg.setVisibility(View.INVISIBLE);
            //本地的视频 需要在手机SD卡根目录添加一个 fl1234.mp4 视频
            final String videoUrl = video.getPath();

            vvCourseVideo.setVideoURI(Uri.parse(videoUrl));
            vvCourseVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setVolume(0, 0);
                    mp.start();
                }
            });
            vvCourseVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    vvCourseVideo.setVideoURI(Uri.parse(videoUrl));
                    vvCourseVideo.start();
                }
            });
            vvCourseVideo.start();
        } else {
            vvCourseVideo.setVisibility(View.INVISIBLE);
            ivCourseImg.setVisibility(View.VISIBLE);
            MGlide.load(mContext, course.getVideoImageUrl(), ivCourseImg);
        }

        tvName.setText(course.getVideoName());
        tvContent.setText("简介：\n" + course.getIntrodution());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestory() {
        vvCourseVideo.destroyDrawingCache();
    }
}
