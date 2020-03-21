package com.skkk.bitmapoptimizationdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    ImageView iv;

    ExecutorService threadPoolExecutor;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View view1 = mInflater.inflate(R.layout.activity_main, null);
        View view2 = mInflater.inflate(R.layout.activity_main, (ViewGroup) findViewById(android.R.id.content), false);
        View view3 = mInflater.inflate(R.layout.activity_main, (ViewGroup) findViewById(android.R.id.content), true);

        iv = findViewById(R.id.iv);

        Glide.with(this).load(R.drawable.person).into(iv);
    }

    /**
     * 通过调整options.inSampleSize来修改大小
     */
    private void optimizationBitmapByInSampleSize() {
        Log.d(TAG, "\n开始压缩图片");
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person);
        Log.d(TAG, String.format("压缩前\tw: %d\th: %d\tsize: %d", bitmap.getWidth(), bitmap.getHeight(), bitmap.getByteCount()));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person, options);
        Log.d(TAG, "options.inJustDecodeBounds设置为True,获取到的bitmap为：" + bitmap);
        options.inSampleSize = 2;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person, options);
        Log.d(TAG, String.format("压缩后\tw: %d\th: %d\tsize: %d", bitmap.getWidth(), bitmap.getHeight(), bitmap.getByteCount()));
    }


    /**
     * 通过调整options.inSampleSize来修改大小
     */
    private void optimizationBitmapByInPreferredConfig() {
        Log.d(TAG, "\n开始压缩图片");
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person);
        Log.d(TAG, String.format("压缩前\tw: %d\th: %d\tsize: %d", bitmap.getWidth(), bitmap.getHeight(), bitmap.getByteCount()));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inMutable = true;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person, options);
        Log.d(TAG, String.format("压缩后\tw: %d\th: %d\tsize: %d", bitmap.getWidth(), bitmap.getHeight(), bitmap.getByteCount()));
    }

    /**
     * 通过调整bitmap.compress来修改图片质量
     */
    private void optimizationBitmapByCompress() {
        Log.d(TAG, "\n开始压缩图片");
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person);
        Log.d(TAG, String.format("压缩前\tw: %d\th: %d\tsize: %d", bitmap.getWidth(), bitmap.getHeight(), bitmap.getByteCount()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
        Log.d(TAG, String.format("压缩后\tw: %d\th: %d\tsize: %d", bitmap.getWidth(), bitmap.getHeight(), bitmap.getByteCount()));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (!threadPoolExecutor.isShutdown()) {
//            threadPoolExecutor.shutdown();
//            threadPoolExecutor = null;
//        }
//        if (!bitmap.isRecycled()) {
//            bitmap.recycle();
//            bitmap = null;
//        }
    }
}
