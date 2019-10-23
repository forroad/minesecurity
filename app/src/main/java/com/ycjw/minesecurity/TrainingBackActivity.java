package com.ycjw.minesecurity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycjw.minesecurity.adapter.ResourceAdapter;
import com.ycjw.minesecurity.adapter.TrainBackAdapter;
import com.ycjw.minesecurity.model.Resource;
import com.ycjw.minesecurity.model.Response;
import com.ycjw.minesecurity.model.TrainingBack;
import com.ycjw.minesecurity.util.AndroidUtil;
import com.ycjw.minesecurity.util.Constant;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class TrainingBackActivity extends BaseActivity {
    private EditText training_back_input;
    private Button training_back_submit;
    private ImageView training_back_return;
    private RecyclerView recyclerView;
    private TrainBackAdapter trainBackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_back);

        initWidget();
    }

    private void initWidget(){
        training_back_input = findViewById(R.id.training_back_input);
        training_back_submit = findViewById(R.id.training_back_submit);
        training_back_return = findViewById(R.id.training_back_return);

        training_back_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        /**
         * 提交按钮点击事件
         */
        {
            training_back_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AndroidUtil.hideOneInputMethod(TrainingBackActivity.this);
                    //初始化最近学习
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                String url = Constant.HTTP + Constant.CURRENT_IP + "training_back/add";
                                OkHttpClient client = new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("telephone",MainActivity.user.getPhoneNum())
                                        .add("content",training_back_input.getText().toString())
                                        .build();
                                Request request  = new Request.Builder()
                                        .url(url)
                                        .post(requestBody)
                                        .build();
                                okhttp3.Response response = client.newCall(request).execute();
                                String responseData = response.body().string();
                                Gson gson = new Gson();
                                final com.ycjw.minesecurity.model.Response<TrainingBack> arrayListResponse = gson.fromJson(responseData, new TypeToken<Response<TrainingBack>>(){}.getType());
                                if(arrayListResponse.getMessage().endsWith("成功")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ActivityController.getLastActivity(),"提交反馈成功",Toast.LENGTH_SHORT).show();
                                            load_training_back();
                                        }
                                    });
                                }else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ActivityController.getLastActivity(),arrayListResponse.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }catch (Exception e){
                                ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ActivityController.getLastActivity(),"提交反馈失败，请检查网络",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
        }

        load_training_back();
    }

    /**
     * 打开培训反馈界面
     */
    public static void actionStart(Context context){
        Intent intent = new Intent(context,TrainingBackActivity.class);
        context.startActivity(intent);
    }

    private void load_training_back(){
        /**
         * 自动加载我的反馈
         */
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        String url = Constant.HTTP + Constant.CURRENT_IP + "training_back/find?telephone=" + MainActivity.user.getPhoneNum();
                        OkHttpClient client = new OkHttpClient();
                        Request request  = new Request.Builder()
                                .url(url)
                                .build();
                        okhttp3.Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        final com.ycjw.minesecurity.model.Response<ArrayList<TrainingBack>> arrayListResponse = gson.fromJson(responseData, new TypeToken<Response<ArrayList<TrainingBack>>>(){}.getType());
                        if(arrayListResponse.getMessage().endsWith("成功")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("TrainingBack",arrayListResponse.toString());
                                    TextView explain_trainback = findViewById(R.id.explain_trainback);
                                    if(arrayListResponse.getData().size() > 0){
                                        explain_trainback.setVisibility(View.VISIBLE);
                                    }
                                    recyclerView = findViewById(R.id.training_back_mine);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityController.getLastActivity());

                                    Log.e("TrainingBack",String.valueOf(recyclerView == null));
                                    layoutManager.setOrientation(RecyclerView.VERTICAL);
                                    recyclerView.setLayoutManager(layoutManager);
                                    trainBackAdapter = new TrainBackAdapter(arrayListResponse.getData());
                                    recyclerView.setAdapter(trainBackAdapter);
                                }
                            });
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ActivityController.getLastActivity(),arrayListResponse.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }catch (Exception e){
                        ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ActivityController.getLastActivity(),"获取反馈失败，请检查网络",Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }
}
