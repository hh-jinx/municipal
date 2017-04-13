package android.jlu.com.municipalmanage.activity;

import android.app.Activity;
import android.content.Intent;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.utils.PreferenceUtils;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

/**
 *
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isGuideShowed = PreferenceUtils.getBoolean(this, "is_user_guide_showed", false);
        if (!isGuideShowed) {
            startActivity(new Intent(this, GuideActivity.class));
            finish();
            return;
        }

        //这里现在有个登陆的问题，登陆后我们怎么处理登陆的状态，什么时候需要再次登陆
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //由于登陆的问题，后面确定是进入主界面还是登陆界面
                enterMainActivity();
            }
        }, 2000);

    }

    private void enterMainActivity() {

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
