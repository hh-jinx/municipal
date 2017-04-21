package android.jlu.com.municipalmanage.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.base.BasePopWindow;
import android.jlu.com.municipalmanage.utils.MyApplication;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import mabeijianxi.camera.MediaRecorderActivity;
import mabeijianxi.camera.model.AutoVBRMode;
import mabeijianxi.camera.model.MediaRecorderConfig;
import mabeijianxi.camera.util.FileUtils;


/**
 * Created by beyond on 17/4/20.
 */

public class ReportActivity extends Activity {
    //照片路径
    private String PHOTO_PIC_PATH;
    private String PHOTO_COMPRESS_PATH;
    //视频
    private String videoUri;
    //视频第一帧
    private String videoScreenshot;

    private TextView tv_select_type;//类型选择
    private List<String> currentTypeLists;

    private View parentView;
    private PopupWindow popupWindow;

    private TextView tv_pop_title;
    private ImageView iv_pop_back;
    private TextView tv_question_type;
    private ListView popListOne;
    private ListView popListTwo;
    private ImageView iv_take_photo;
    private ImageView iv_take_video;
    private ImageView iv_photo_show;

    private ImageView iv_video_screenshot;

    private AlertDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        tv_select_type = (TextView) findViewById(R.id.tv_select_type);
        iv_take_photo = (ImageView) findViewById(R.id.iv_take_photo);
        iv_take_video = (ImageView) findViewById(R.id.iv_take_video);
        iv_photo_show = (ImageView) findViewById(R.id.iv_photo_show);

        iv_video_screenshot = (ImageView) findViewById(R.id.iv_video_screenshot);

        tv_select_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
                popListOne.setAdapter(new PopAdapter(MyApplication.typeLists));
            }
        });

        iv_photo_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PHOTO_COMPRESS_PATH != null) {
                    Intent intent = new Intent(ReportActivity.this, PhotoShowActivity.class);
                    intent.putExtra("PHOTO_COMPRESS_PATH", PHOTO_COMPRESS_PATH);
                    startActivity(intent);
                } else {
                    //还没拍照
                    Toast.makeText(ReportActivity.this, "没有拍照哦！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iv_take_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 录制
                MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
                        .doH264Compress(new AutoVBRMode()
//                        .setVelocity(BaseMediaBitrateConfig.Velocity.ULTRAFAST)
                        )
                        .setMediaBitrateConfig(new AutoVBRMode()
//                        .setVelocity(BaseMediaBitrateConfig.Velocity.ULTRAFAST)
                        )
                        .smallVideoWidth(480)
                        .smallVideoHeight(360)
                        .recordTimeMax(5 * 1000)
                        .maxFrameRate(20)
                        .captureThumbnailsTime(1)
                        .recordTimeMin((int) (1.5 * 1000))
                        .build();


//                MediaRecorderActivity.goSmallVideoRecorder(
//                        ReportActivity.this, ReportActivity.class.getName(), config);

                startActivityForResult(
                        new Intent(ReportActivity.this, MediaRecorderActivity.class)
                                .putExtra("over_activity_name", ReportActivity.class.getName())
                                .putExtra("media_recorder_config_key", config), 5);


            }
        });


        iv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                PHOTO_PIC_PATH =
                        MyApplication.PHOTO_PATH + String.valueOf(System.currentTimeMillis()) + ".png";
                Uri uri = Uri.fromFile(new File(PHOTO_PIC_PATH));
                //为拍摄的图片指定一个存储的路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 6);
            }
        });

        iv_video_screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(ReportActivity.this, VideoPlayerActivity.class)
                                .putExtra("path", videoUri));
            }
        });

//        initData();
        initPopView();

        tv_question_type = (TextView) findViewById(R.id.tv_question_type);

    }


    @Override
    public void onBackPressed() {
        hesitate();
    }

    private void hesitate() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.hint)
                    .setMessage(R.string.record_camera_exit_dialog_message)
                    .setNegativeButton(
                            R.string.record_camera_cancel_dialog_no,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Toast.makeText(ReportActivity.this,
                                            "暂不放弃", Toast.LENGTH_SHORT).show();
                                }

                            })
                    .setPositiveButton(R.string.record_camera_cancel_dialog_yes,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(ReportActivity.this,
                                            "确定放弃", Toast.LENGTH_SHORT).show();
                                    FileUtils.deleteDir(getIntent().getStringExtra(MediaRecorderActivity.OUTPUT_DIRECTORY));
                                    FileUtils.deleteFile(PHOTO_PIC_PATH);
                                    FileUtils.deleteFile(PHOTO_COMPRESS_PATH);
                                    finish();
                                }
                            })
                    .setCancelable(false)
                    .show();
        } else {
            dialog.show();
        }
    }

    private void initData() {
        Intent intent = getIntent();
        videoUri = intent.getStringExtra(MediaRecorderActivity.VIDEO_URI);
        videoScreenshot = intent.getStringExtra(MediaRecorderActivity.VIDEO_SCREENSHOT);
        Bitmap bitmap = BitmapFactory.decodeFile(videoScreenshot);
        iv_video_screenshot.setImageBitmap(bitmap);
    }


    // 获取压缩后的图片地址
    // 进行尺寸压缩之后再进行质量压缩
    public String getCompressedPath(Context context, String path) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;                                       // 获取图片的宽高，图片并没有加载进内存
            BitmapFactory.decodeFile(path, options);

            int originWidth = options.outWidth;
            int originHeight = options.outHeight;
            // 目标尺寸的宽高根据具体情况来定
            float targetHeight = 1024f;
            float targetWidth = 1024f;
            int rate = 1;                                                             // rate=1表示不缩放
            if (originWidth >= originHeight && originWidth > targetWidth) {           // 如果宽度大的话根据宽度固定大小缩放
                rate = (int) (options.outWidth / targetWidth);
            } else if (originWidth < originHeight && originHeight > targetHeight) {   // 如果高度高的话根据宽度固定大小缩放
                rate = (int) (options.outHeight / targetHeight);
            }
            if (rate <= 0) rate = 1;

            options.inSampleSize = rate;                                              // 设置缩放比例
            options.inJustDecodeBounds = false;                                       // 这里一定要将其设置回false，因为之前我们将其设置成了true

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);

            String name = String.valueOf(System.currentTimeMillis());
//            File uploadImageDir = new File(getUploadImageDir(context));
//            File uploadImageDir = Environment
//                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//
//            File uploadImageFile = new File(uploadImageDir + "/" + name + ".jpg");    // 压缩后的图片位置

//            File uploadImageDir = new File(
//                    Environment.getExternalStorageDirectory() + "/photo11/"
//            );
            File uploadImageFile = new File(MyApplication.PHOTO_PATH + name + ".jpg");

//            if (!uploadImageDir.exists()) {
//                uploadImageDir.mkdirs();
//            } else {
//                if (uploadImageFile.exists()) {
//                    uploadImageFile.delete();
//                }
//            }

            try {
                FileOutputStream out = new FileOutputStream(uploadImageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);                  // 进行质量压缩
                out.flush();
                out.close();
                bitmap.recycle();                                                      // 回收bitmap，节省内存
                Toast.makeText(context, uploadImageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                return uploadImageFile.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == 10) {
//            Toast.makeText(ReportActivity.this, "VIDEO", Toast.LENGTH_SHORT).show();

            Intent intent = data;
            videoUri = intent.getStringExtra(MediaRecorderActivity.VIDEO_URI);
            videoScreenshot = intent.getStringExtra(MediaRecorderActivity.VIDEO_SCREENSHOT);
            Bitmap bitmap = BitmapFactory.decodeFile(videoScreenshot);

//            iv_video_screenshot.setImageBitmap(bitmap);
            iv_video_screenshot.setBackground(new BitmapDrawable(bitmap));

        } else if (requestCode == 6) {

            Bitmap bitmap1 = BitmapFactory.decodeFile(PHOTO_PIC_PATH);
            if (bitmap1 != null) {
                //压缩后的path
                String path = getCompressedPath(ReportActivity.this, PHOTO_PIC_PATH);
                PHOTO_COMPRESS_PATH = path;
                Bitmap bitmap = BitmapFactory.decodeFile(path);
//                iv_photo_show.setImageBitmap(bitmap);
                iv_photo_show.setBackground(new BitmapDrawable(bitmap));
            } else {
//                Toast.makeText(ReportActivity.this, "PHOTO :" + bitmap1, Toast.LENGTH_SHORT).show();
            }


        }
    }


    private void initPopView() {

        popupWindow = new BasePopWindow();
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_report, null);

        int width = getResources().getDisplayMetrics().widthPixels * 3 / 4;
        int height = getResources().getDisplayMetrics().heightPixels * 3 / 5;
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);

        View view = LayoutInflater.from(this).inflate(R.layout.popwindow_type, null);
        popupWindow.setContentView(view);

        tv_pop_title = (TextView) view.findViewById(R.id.tv_pop_title);
        iv_pop_back = (ImageView) view.findViewById(R.id.iv_pop_back);
        popListOne = (ListView) view.findViewById(R.id.lv_one);
        popListTwo = (ListView) view.findViewById(R.id.lv_two);

        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);

        iv_pop_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popListTwo.setVisibility(View.GONE);
                popListOne.setVisibility(View.VISIBLE);
                iv_pop_back.setVisibility(View.GONE);
            }
        });

        popListOne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_pop_title.setText("项目选择");
                iv_pop_back.setVisibility(View.VISIBLE);
                popListOne.setVisibility(View.GONE);
                popListTwo.setVisibility(View.VISIBLE);
                if (position == 0) {
                    currentTypeLists = MyApplication.roadLists1;
                } else if (position == 1) {
                    currentTypeLists = MyApplication.walkLists2;
                } else if (position == 2) {
                    currentTypeLists = MyApplication.stoneLists3;
                } else if (position == 3) {
                    currentTypeLists = MyApplication.waterLists4;
                } else if (position == 4) {
                    currentTypeLists = MyApplication.lightLists5;
                } else if (position == 5) {
                    currentTypeLists = MyApplication.otherLists6;
                }
                popListTwo.setAdapter(new PopAdapter(currentTypeLists));
            }
        });

        popListTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_question_type.setText(currentTypeLists.get(position));
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tv_pop_title.setText("分类选择");
                popListTwo.setVisibility(View.GONE);
                popListOne.setVisibility(View.VISIBLE);
                iv_pop_back.setVisibility(View.GONE);
            }
        });

    }


    class PopAdapter extends BaseAdapter {

        private List<String> lists;

        public PopAdapter(List<String> lists) {
            this.lists = lists;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(ReportActivity.this)
                        .inflate(R.layout.pop_list_item, null);
                holder.nameView = (TextView) convertView.findViewById(R.id.tv_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (getItem(position) != null) {
                holder.nameView.setText(getItem(position).toString());
            }
            return convertView;

        }
    }

    private static class ViewHolder {
        TextView nameView;
    }
}
