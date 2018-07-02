package com.mai.dibbler.delegate;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.mai.dibbler.MyApplication;
import com.mai.dibbler.R;
import com.mai.dibbler.base.BaseDelegate;
import com.mai.dibbler.bean.Course;
import com.mai.dibbler.bean.WaitCourse;
import com.mai.dibbler.utils.MGlide;
import com.mai.xmai_fast_lib.baseadapter.BaseRecyclerViewAdapter;
import com.mai.xmai_fast_lib.baseadapter.BaseViewHolderImpl;

import butterknife.Bind;

/**
 * Created by maijuntian on 2018/6/28.
 */
public class WaitListDelegate extends BaseDelegate {
    @Bind(R.id.iv_return)
    ImageView ivReturn;
    @Bind(R.id.rv_course)
    RecyclerView rvCourse;

    BaseRecyclerViewAdapter<Course> adapter;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_wait_playing;
    }

    @Override
    public void initWidget() {
        rvCourse.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
    }


    public void initWaitCourse() {

        WaitCourse waitCourse = MyApplication.INSTANCE.mWaitCourse;

        if (waitCourse != null && waitCourse.getVideoWaitList() != null) {

            if (adapter == null) {
                adapter = new BaseRecyclerViewAdapter<Course>(waitCourse.getVideoWaitList()) {
                    @Override
                    protected int bindLayoutId(int position) {
                        return R.layout.rv_item_wait_list_course;
                    }

                    @Override
                    protected void initView(Course data, BaseViewHolderImpl viewHolder) {
                        viewHolder.setText(R.id.tv_course_name, data.getVideoName())
                                .setText(R.id.tv_course_time, data.getVideoDuration())
                                .setText(R.id.tv_start_time, data.getVideoStartTime());

                        MGlide.load(mContext, data.getVideoImageUrl(), (ImageView) viewHolder.findViewById(R.id.iv_course_img));
                    }
                };
                rvCourse.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }

        }
    }
}
