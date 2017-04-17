package android.jlu.com.municipalmanage.utils;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * <pre>
 *     author : qiuyudong
 *     e-mail : qiuyudongjlu@qq.com
 *     time   : 2017/04/15
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
