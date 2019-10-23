package com.ycjw.minesecurity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycjw.minesecurity.adapter.ExamAdapter;
import com.ycjw.minesecurity.adapter.ExamRecordAdapter;
import com.ycjw.minesecurity.adapter.OnlineExamAdapter;
import com.ycjw.minesecurity.model.ExamRecord;
import com.ycjw.minesecurity.model.Response;
import com.ycjw.minesecurity.model.SelectionQuestion;
import com.ycjw.minesecurity.util.Constant;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ExamRecordActivity extends BaseActivity {
    private ImageView exam_record_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_record);

        initWidget();
    }

    private void initWidget(){
        exam_record_return = findViewById(R.id.exam_record_return);

        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        final ArrayList<ExamRecord> examRecords = new ArrayList<>();
                        String url = Constant.HTTP + Constant.CURRENT_IP + "exam_record/find_someexamrecord_byphone";
                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("phone",MainActivity.user.getPhoneNum())
                                .build();
                        Request request  = new Request.Builder()
                                .url(url)
                                .post(requestBody)
                                .build();
                        okhttp3.Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        final com.ycjw.minesecurity.model.Response<List<ExamRecord>> modify_response = gson.fromJson(responseData, new TypeToken<Response<List<ExamRecord>>>(){}.getType());
                        if(modify_response.getMessage().endsWith("成功")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView exam_record = findViewById(R.id.exam_record_recycle);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityController.getLastActivity());
                                    layoutManager.setOrientation(RecyclerView.VERTICAL);
                                    exam_record.setLayoutManager(layoutManager);

                                    examRecords.addAll(modify_response.getData());
                                    if(examRecords.size() <= 0){
                                        Toast.makeText(ExamRecordActivity.this,"暂无考试记录",Toast.LENGTH_SHORT).show();
                                    }
                                    ExamRecordAdapter examRecordAdapter = new ExamRecordAdapter(examRecords);
                                    exam_record.setAdapter(examRecordAdapter);
                                }
                            });
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ExamRecordActivity.this,modify_response.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }catch (Exception e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ExamRecordActivity.this,"获取试题失败，请检查网络",Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        exam_record_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,ExamRecordActivity.class);
        //intent.putExtra("param",value);
        context.startActivity(intent);
    }
}
