package android.jlu.com.municipalmanage.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.jlu.com.municipalmanage.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TaskItemActivity extends AppCompatActivity {
     private TextView task_id,task_time,task_address,task_type,task_state,task_desc;
     private String  taskId,taskTime,taskAddress,taskType,taskState,taskDesc;
     private Button show_photo,show_video;
     private String photoUri,videoUri;

    private static final String TAG = "TaskItemActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_item);
        task_id = (TextView)findViewById(R.id.task_id);
        task_time = (TextView)findViewById(R.id.task_time);
        task_address = (TextView)findViewById(R.id.task_address);
        task_type = (TextView)findViewById(R.id.task_type);
        task_state = (TextView)findViewById(R.id.task_state);
        task_desc = (TextView)findViewById(R.id.task_desc);
        show_photo = (Button)findViewById(R.id.show_photo);
        show_video = (Button)findViewById(R.id.show_video);

        Intent intent = getIntent();
        taskId = intent.getStringExtra("task_id");
        taskTime = intent.getStringExtra("task_time");
        taskAddress = intent.getStringExtra("task_address");
        taskType = intent.getStringExtra("task_type");
        taskState = intent.getStringExtra("task_state");
        taskDesc = intent.getStringExtra("task_desc");
        photoUri = intent.getStringExtra("task_pic");
        videoUri = intent.getStringExtra("task_video");


        task_id.setText(taskId);
        task_time.setText(taskTime);
        task_address.setText(taskAddress);
        task_type.setText(taskType);
        if(taskState.equals("0")){
            task_state.setText("未维修");
        }else if (taskState.equals("1")){
            task_state.setText("正在维修");
        }

        task_desc.setText(taskDesc);




        show_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  ProgressDialog dialog = new ProgressDialog(TaskItemActivity.this);
                dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
                dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
                dialog.setTitle("提示");
                dialog.setMessage("原图较大，正在加载...");
                dialog.show();
                new Thread(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {
                            Thread.sleep(1500);
                            Intent intent_photo = new Intent(TaskItemActivity.this,InternetPicActivity.class);
                            intent_photo.putExtra("URI",photoUri);
                            startActivity(intent_photo);

                            dialog.dismiss();

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            dialog.dismiss();
                        }
                    }
                }.start();

            }
        });


        show_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  ProgressDialog dialog2 = new ProgressDialog(TaskItemActivity.this);
                dialog2.setCancelable(true);// 设置是否可以通过点击Back键取消
                dialog2.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
                dialog2.setTitle("提示");
                dialog2.setMessage("小视频正在加载...");
                dialog2.show();
                new Thread(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {
                            Thread.sleep(3000);
                            Intent intent_photo = new Intent(TaskItemActivity.this,InternetVideoActivity.class);
                            intent_photo.putExtra("URI",videoUri);
                            startActivity(intent_photo);
                            dialog2.dismiss();

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            dialog2.dismiss();
                        }
                    }
                }.start();
            }
        });



    }
}
