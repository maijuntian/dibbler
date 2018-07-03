package com.mai.dibbler.delegate;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mai.dibbler.MyApplication;
import com.mai.dibbler.R;
import com.mai.dibbler.base.BaseDelegate;
import com.mai.dibbler.bean.Course;
import com.mai.dibbler.bean.WaitCourse;
import com.mai.dibbler.utils.MGlide;
import com.mai.xmai_fast_lib.baseadapter.BaseRecyclerViewAdapter;
import com.mai.xmai_fast_lib.baseadapter.BaseViewHolderImpl;
import com.mai.xmai_fast_lib.baseadapter.listener.RBaseAnimator;
import com.mai.xmai_fast_lib.baseadapter.listener.ROnItemClickListener;
import com.mai.xmai_fast_lib.utils.MLog;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by maijuntian on 2018/6/28.
 */
public class WaitListDelegate extends BaseDelegate {
    @Bind(R.id.iv_return)
    ImageView ivReturn;
    @Bind(R.id.rv_course)
    RecyclerView rvCourse;
    @Bind(R.id.tv_tip)
    TextView tvTip;

    BaseRecyclerViewAdapter<Course> adapter;

    RBaseAnimator slideTopAnimator = new RBaseAnimator() {
        @Override
        public Animator getAnimator(View view) {
            ObjectAnimator animtor = ObjectAnimator.ofFloat(view, "translationY", -view.getMeasuredHeight(), 0);
            animtor.setDuration(3000);
            return animtor;
        }
    };

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_wait_playing;
    }

    @Override
    public void initWidget() {
        rvCourse.setItemAnimator(new DefaultItemAnimator());
        rvCourse.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
    }


    public void initWaitCourse(ROnItemClickListener itemClickListener) {

        WaitCourse waitCourse = MyApplication.INSTANCE.mWaitCourse;

        if (waitCourse != null && waitCourse.getVideoWaitList() != null) {
            tvTip.setText("待播视频：" + waitCourse.getVideoWaitPlayTotal() + "个");

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

                adapter.setOnItemClickListener(itemClickListener);
                rvCourse.setAdapter(adapter);
            } else {
                adapter.setAnimator(null);
                adapter.notifyDataSetChanged();
            }
        }

    }


    public void showInsert(ROnItemClickListener itemClickListener) {
        WaitCourse waitCourse = MyApplication.INSTANCE.mWaitCourse;

        tvTip.setText("待播视频：" + waitCourse.getVideoWaitPlayTotal() + "个");
        final Course removeCourse = waitCourse.getVideoWaitList().remove(waitCourse.getVideoOnDemandSub());

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

        adapter.setOnItemClickListener(itemClickListener);
        rvCourse.setAdapter(adapter);

        final int subIndex = waitCourse.getVideoOnDemandSub();

        if (waitCourse.getVideoOnDemandSub() >= 3) {
            MLog.log("先滚动，再插入");
            timer(new Action1<Long>() {
                @Override
                public void call(Long aLong) {
                    WaitCourse waitCourse = MyApplication.INSTANCE.mWaitCourse;

                    if (waitCourse.getVideoWaitList().size() >= (subIndex + 1)) {
                        rvCourse.smoothScrollToPosition(subIndex + 1);
                    } else {
                        rvCourse.smoothScrollToPosition(subIndex);
                    }

                    timer(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            insertAnim(removeCourse);
                        }
                    });
                }
            });
        } else {
            MLog.log("直接插入");
            timer(new Action1<Long>() {
                @Override
                public void call(Long aLong) {
                    insertAnim(removeCourse);
                }
            });
        }
    }

    private void insertAnim(Course removeCourse) {

        adapter.setAnimator(slideTopAnimator);
        WaitCourse waitCourse = MyApplication.INSTANCE.mWaitCourse;
        waitCourse.getVideoWaitList().add(waitCourse.getVideoOnDemandSub(), removeCourse);
        adapter.notifyItemInserted(waitCourse.getVideoOnDemandSub());

    }


    private void timer(Action1<Long> action1) {
        Observable.timer(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(action1, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
