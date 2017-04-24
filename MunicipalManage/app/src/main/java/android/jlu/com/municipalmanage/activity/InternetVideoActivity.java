package android.jlu.com.municipalmanage.activity;

import android.content.Intent;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.baseclass.UriSet;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class InternetVideoActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "InternetVideoActivity";
    private SurfaceView surface1;
    private Button start, stop, pre;
    private MediaPlayer mediaPlayer1;
    private String video_uri;
    private int postion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_video);
        surface1 = (SurfaceView) findViewById(R.id.surface1);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        pre = (Button) findViewById(R.id.pre);
        Intent intent = getIntent();
        video_uri = intent.getStringExtra("URI");
        Log.d(TAG, "onCreate: "+video_uri);
        video_uri = video_uri.substring(103);
        video_uri = video_uri.replaceAll("\\\\", "/");
        video_uri = video_uri.replaceAll("\\//", "/");
        video_uri = UriSet.SERVER_URI+video_uri;
        Log.d(TAG, "onCreate: "+video_uri);

        mediaPlayer1 = new MediaPlayer();
        //设置播放时打开屏幕
        surface1.getHolder().setKeepScreenOn(true);
        surface1.getHolder().addCallback(new SurfaceViewLis());
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        pre.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                try {
                    play();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.pre:
                if (mediaPlayer1.isPlaying()) {
                    mediaPlayer1.pause();
                } else {
                    mediaPlayer1.start();
                }
                break;
            case R.id.stop:
                if (mediaPlayer1.isPlaying())
                    mediaPlayer1.stop();

                break;
            default:
                break;
        }

    }




    public void play() throws IllegalArgumentException, SecurityException,
            IllegalStateException, IOException {

            mediaPlayer1.reset();
            mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer1.setDataSource(video_uri);
            // 把视频输出到SurfaceView上
            mediaPlayer1.setDisplay(surface1.getHolder());
            mediaPlayer1.prepare();
            mediaPlayer1.start();


    }

    private class SurfaceViewLis implements SurfaceHolder.Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (postion == 0) {
                try {
                    play();
                    mediaPlayer1.seekTo(postion);
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    finish();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    finish();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    finish();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    finish();
                }

            }

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

    }

    @Override
    protected void onPause() {
        if (mediaPlayer1.isPlaying()) {
            // 保存当前播放的位置
            postion = mediaPlayer1.getCurrentPosition();
            mediaPlayer1.stop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer1.isPlaying())
            mediaPlayer1.stop();
        mediaPlayer1.release();
        super.onDestroy();
    }

}

