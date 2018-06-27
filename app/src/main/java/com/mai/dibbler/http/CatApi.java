package com.mai.dibbler.http;


import com.mai.dibbler.bean.Course;
import com.mai.dibbler.bean.WaitCourse;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface CatApi {

    @POST("external/videoPlay/allVideoCourseList")
    Observable<CatRespone<List<Course>>> allVideoCourseList(@Body RequestBody json);

    @POST("external/videoPlay/videoWaitForPlayList")
    Observable<CatRespone<WaitCourse>> videoWaitForPlayList(@Body RequestBody json);
}
