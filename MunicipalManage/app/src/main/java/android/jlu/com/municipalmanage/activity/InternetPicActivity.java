package android.jlu.com.municipalmanage.activity;

import android.content.Intent;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.baseclass.UriSet;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class InternetPicActivity extends AppCompatActivity {
    private String pic_uri;
    private ImageView photo;
    private static final String TAG = "InternetPicActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_pic);
        photo = (ImageView)findViewById(R.id.photo_show);
        Intent intent = getIntent();
        pic_uri = intent.getStringExtra("URI");
        pic_uri = pic_uri.replaceAll("\\\\", "/");
        pic_uri = pic_uri.replaceAll("\\//", "/");
        pic_uri =UriSet.SERVER_URI+ pic_uri.substring(1);
        Log.d(TAG, "onCreate: "+pic_uri);

      Picasso.with(getBaseContext())
              .load(pic_uri)
              //设置加载失败的图片显示
              .error(R.drawable.report_name)
              .into(photo);





    }
}
