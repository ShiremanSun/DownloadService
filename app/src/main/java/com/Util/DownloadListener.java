package com.Util;

/**
 * Created by DroodSunny on 2017/10/11.
 */
//回调接口
public interface DownloadListener {
    void onProgress(int progress);
    void onSuccess();
    void onFailed();
    void onPaused();
    void onCancle();
}
