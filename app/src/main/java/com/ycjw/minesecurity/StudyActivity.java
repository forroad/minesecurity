package com.ycjw.minesecurity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycjw.minesecurity.model.Response;
import com.ycjw.minesecurity.model.User;
import com.ycjw.minesecurity.util.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ycjw.minesecurity.adapter.StudyWebView;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class StudyActivity extends BaseActivity{
    private StudyWebView online_study_web;
    private ImageView online_study_return;
    private Date startTime;
    private Date endTime;
    private Date lastClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        //初始化控件
        initWidget();
        //初始化web
        initWebView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTime = new Date();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            endTime = new Date();
            if(lastClickTime == null){
                lastClickTime = startTime;
            }
            if((endTime.getTime() - lastClickTime.getTime())/1000 > 20*60){
                endTime.setTime(lastClickTime.getTime() + 20*60*1000);
            }
            final long studyTime = (endTime.getTime() - startTime.getTime())/(1000*60);
            Intent intent = getIntent();
            final String resourceId = intent.getStringExtra("resourceId");
            if(studyTime > 1){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String url = Constant.HTTP + Constant.CURRENT_IP + "learn_record/create_learn_record";
                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("phoneNum",MainActivity.user.getPhoneNum())
                                    .add("timeLong", String.valueOf(studyTime))
                                    .add("resourceId", resourceId)
                                    .build();
                            Request request  = new Request.Builder()
                                    .url(url)
                                    .post(requestBody)
                                    .build();
                            okhttp3.Response response = client.newCall(request).execute();
                        }catch (Exception e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(StudyActivity.this,"上传学习记录失败，请检查网络",Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void actionStart(Context context, String resourceId){
        Intent intent = new Intent(context,StudyActivity.class);
        intent.putExtra("resourceId",resourceId);
        context.startActivity(intent);
    }

    private void initWidget(){
        online_study_web = findViewById(R.id.online_study_web);
        online_study_return = findViewById(R.id.online_study_return);

        online_study_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        online_study_web.setOnTouchScreenListener(new StudyWebView.OnTouchScreenListener() {
            @Override
            public void onTouchScreen() {
                lastClickTime = new Date();
            }
        });
    }

    /**
     * 初始化WebView
     */
    private void initWebView() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("resourceId");
        online_study_web.getSettings().setJavaScriptEnabled(true);
        // 设置此属性,可任意比例缩放
        online_study_web.getSettings().setUseWideViewPort(true);
        online_study_web.getSettings().setBlockNetworkImage(false);
        online_study_web.loadUrl("http://ow365.cn/?i=19814&furl=" + Constant.HTTP + Constant.CURRENT_IP + "/user/downloadResource?resourceId=" + id);
        online_study_web.setWebViewClient(new MyWebviewClient());
        online_study_web.setWebChromeClient(new MyChromeClient());
    }

    /**
     * 重写MyWebviewClient方法
     *
     * shouldOverrideUrlLoading（） 拦截网页跳转，是之继续在WebView中进行跳转
     * onPageStarted（） 开始加载的时候（显示进度条）
     * onPageFinished（） 夹在结束的时候（隐藏进度条）
     */
    private class MyWebviewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }

    }
    /**
     * 重写MyChromeClient方法
     *
     * onProgressChanged（） 设置动态进度条
     * onReceivedTitle（） 设置WebView的头部标题
     * onReceivedIcon（）  设置WebView的头部图标
     */
    private class MyChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }
    }

    /**
     * 实现WebView的回退栈
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode== KeyEvent.KEYCODE_BACK && online_study_web.canGoBack()){
            online_study_web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
