package com.mai.dibbler.utils;

import android.os.Environment;

/**
 * Created by maijuntian on 2018/6/28.
 */
public class Constant {

    public static final String BASE_PATH = Environment
            .getExternalStorageDirectory().getPath() + "/dibble/";

    public static final String SDCARD_DOWNLOAD_PATH = BASE_PATH + "download/";
}
