package com.mai.dibbler.delegate;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mai.dibbler.MyApplication;
import com.mai.dibbler.R;
import com.mai.dibbler.base.BaseDelegate;
import com.mai.dibbler.bean.Course;
import com.mai.dibbler.bean.WaitCourse;
import com.mai.dibbler.utils.MGlide;
import com.mai.xmai_fast_lib.baseadapter.BaseRecyclerViewAdapter;
import com.mai.xmai_fast_lib.baseadapter.BaseViewHolderImpl;
import com.mai.xmai_fast_lib.baseadapter.listener.ROnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainDelegate extends BaseDelegate {
    @Bind(R.id.tv_wait_list)
    TextView tvWaitList;
    @Bind(R.id.tv_playing)
    TextView tvPlaying;
    @Bind(R.id.tv_next_play)
    TextView tvNextPlay;
    @Bind(R.id.rb_course_name)
    RadioButton rbCourseName;
    @Bind(R.id.rb_course_time)
    RadioButton rbCourseTime;
    @Bind(R.id.rb_course_hot)
    RadioButton rbCourseHot;
    @Bind(R.id.ll_sort)
    LinearLayout llSort;
    @Bind(R.id.rv_course)
    RecyclerView rvCourse;
    @Bind(R.id.iv_qrcode)
    ImageView ivQrcode;
    @Bind(R.id.et_input)
    public EditText etInput;
    @Bind(R.id.rg_order)
    public RadioGroup rgOrder;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initWidget() {
        super.initWidget();

        rvCourse.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rbCourseName.setChecked(true);
    }

    public void initWaitCourse() {
        WaitCourse waitCourse = MyApplication.INSTANCE.mWaitCourse;
        if (waitCourse == null || TextUtils.isEmpty(waitCourse.getVideoPlayHint())) {
            tvPlaying.setText("正在播放：");
        } else {
            tvPlaying.setText("正在播放：" + waitCourse.getVideoPlayHint());
        }

        if (waitCourse == null || TextUtils.isEmpty(waitCourse.getNextVideoPlayHint())) {
            tvNextPlay.setText("下一个：");
        } else {
            tvNextPlay.setText("下一个：" + waitCourse.getNextVideoPlayHint());
        }
    }

    public void initCourses(List<Course> courses, ROnItemClickListener onItemClickListener) {

        BaseRecyclerViewAdapter adapter = new BaseRecyclerViewAdapter<Course>(courses) {
            @Override
            protected int bindLayoutId(int position) {
                return R.layout.rv_item_main_course;
            }

            @Override
            protected void initView(Course data, BaseViewHolderImpl viewHolder) {
                viewHolder.setText(R.id.tv_course_tip, data.getVideoName())
                        .setText(R.id.tv_course_time, data.getVideoDuration());

                MGlide.load(mContext, data.getVideoImageUrl(), (ImageView) viewHolder.findViewById(R.id.iv_course_img));
            }

        };
        adapter.setOnItemClickListener(onItemClickListener);
        rvCourse.setAdapter(adapter);
    }
}
