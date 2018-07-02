package com.mai.dibbler.download;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.mai.xmai_fast_lib.utils.MLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yufs on 2017/8/16.
 */

public class DownloadUtils {
    public static final int DOWNLOAD_FAIL = 0;
    public static final int DOWNLOAD_PROGRESS = 1;
    public static final int DOWNLOAD_SUCCESS = 2;
    private static DownloadUtils downloadUtil;
    private final OkHttpClient okHttpClient;

    public static DownloadUtils getInstance() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtils();
        }
        return downloadUtil;
    }

    private DownloadUtils() {
        okHttpClient = new OkHttpClient();
    }

    /**
     *
     */
    public void download(final String url, final String saveDir, final String fileName, final OnDownloadListener listener) {
        this.listener = listener;

        //储存下载文件的目录
        final String savePath = isExistDir(saveDir);

        final File file = new File(savePath, fileName);

        long downloadedLength = 0;//已下载的文件长度

        if (file.exists()) {
            downloadedLength = file.length();
        }

        final long downloadedLengthFinal = downloadedLength;

        Request request = new Request.Builder()
                .addHeader("RANGE", "bytes=" + downloadedLength + "-").url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                MLog.log("失败了--->" + e.getMessage());
                Message message = Message.obtain();
                message.what = DOWNLOAD_FAIL;
                mHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len;
                RandomAccessFile savedFile = null;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();

                    savedFile = new RandomAccessFile(file, "rw");//随机读写模式为读写，写入file中
                    //跳过已下载的字节
                    savedFile.seek(downloadedLengthFinal);

                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        savedFile.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        //下载中
                        Message message = Message.obtain();
                        message.what = DOWNLOAD_PROGRESS;
                        message.obj = progress;
                        mHandler.sendMessage(message);
                    }
                    //下载完成
                    Message message = Message.obtain();
                    message.what = DOWNLOAD_SUCCESS;
                    message.obj = file.getAbsolutePath();
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = DOWNLOAD_FAIL;
                    mHandler.sendMessage(message);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if (savedFile != null) {
                            savedFile.close();
                        }
                    } catch (IOException e) {

                    }
                }
            }
        });
    }

    private String isExistDir(String saveDir) {
        File downloadFile = new File(saveDir);
        if (!downloadFile.mkdirs()) {
            try {
                downloadFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DOWNLOAD_PROGRESS:
                    listener.onDownloading((Integer) msg.obj);
                    break;
                case DOWNLOAD_FAIL:
                    listener.onDownloadFailed();
                    break;
                case DOWNLOAD_SUCCESS:
                    listener.onDownloadSuccess((String) msg.obj);
                    break;
            }
        }
    };


    OnDownloadListener listener;

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess(String path);

        /**
         * 下载进度
         *
         * @param progress
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}