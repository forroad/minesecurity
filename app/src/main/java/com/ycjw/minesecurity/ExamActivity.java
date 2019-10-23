package com.ycjw.minesecurity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycjw.minesecurity.adapter.ExamAdapter;
import com.ycjw.minesecurity.adapter.MainAdapter;
import com.ycjw.minesecurity.adapter.MyDialog;
import com.ycjw.minesecurity.adapter.OnlineExamAdapter;
import com.ycjw.minesecurity.model.Exam;
import com.ycjw.minesecurity.model.ExamRecord;
import com.ycjw.minesecurity.model.Response;
import com.ycjw.minesecurity.model.SelectionQuestion;
import com.ycjw.minesecurity.model.User;
import com.ycjw.minesecurity.util.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ExamActivity extends BaseActivity {
    private Toolbar  online_exam_toolbar;
    private ImageView exam_return;
    private ViewPager exam_viewpager;
    private Button previous_question;
    private Button next_question;
    private TextView online_exam_text;
    private TextView exam_time;
    private OnlineExamAdapter onlineExamAdapter;
    private int examType = 0;
    private String examId;

    private int hours = 0;
    private int minutes = 1;
    private int seconds = 0;
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    if(seconds == 0){
                        seconds = 59;
                        if(minutes == 0){
                            minutes = 59;
                            if(hours == 0){
                                exam_end();
                            }else {
                                hours--;
                            }
                        }else {
                            minutes--;
                        }
                    }else {
                        seconds --;
                    }
                    String timeText = hours + ":";
                    if(minutes < 10){
                        timeText = timeText + "0" + minutes + ":";
                    }else {
                        timeText = timeText + minutes + ":";
                    }
                    if(seconds < 10){
                        timeText = timeText + "0" + seconds;
                    }else {
                        timeText = timeText + seconds;
                    }
                    exam_time.setText(timeText);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        //初始化控件
        initWidgeet();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OnlineExamAdapter.isSubmit = false;
    }

    private void initWidgeet(){
        exam_return = findViewById(R.id.online_exam_return);
        exam_viewpager = findViewById(R.id.exam_viewpager);
        previous_question = findViewById(R.id.previous_question);
        next_question = findViewById(R.id.next_question);
        online_exam_toolbar = findViewById(R.id.online_exam_toolbar);
        online_exam_text = findViewById(R.id.online_exam_text);
        exam_time = findViewById(R.id.exam_time);

        Intent intent = getIntent();
        int type = intent.getIntExtra("examType",0);
        this.examType = type;
        this.examId = intent.getStringExtra("examId");
        String title = "考试";
        switch (type){
            case 0:
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            final List<SelectionQuestion> questions = new ArrayList<>();
                            String url = Constant.HTTP + Constant.CURRENT_IP + "question/find_all_nocompleted?phone=" + MainActivity.user.getPhoneNum();
                            OkHttpClient client = new OkHttpClient();
                            Request request  = new Request.Builder()
                                    .url(url)
                                    .build();
                            okhttp3.Response response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            Gson gson = new Gson();
                            final com.ycjw.minesecurity.model.Response<List<SelectionQuestion>> modify_response = gson.fromJson(responseData, new TypeToken<Response<List<SelectionQuestion>>>(){}.getType());
                            if(modify_response.getMessage().endsWith("成功")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        questions.addAll(modify_response.getData());
                                        onlineExamAdapter = new OnlineExamAdapter(questions);
                                        exam_viewpager.setAdapter(onlineExamAdapter);

                                        previous_question.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                int currentPosition = exam_viewpager.getCurrentItem();
                                                int position = currentPosition - 1 >= 0?currentPosition - 1:0;
                                                exam_viewpager.setCurrentItem(position);
                                            }
                                        });

                                        next_question.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                int currentPosition = exam_viewpager.getCurrentItem();
                                                int position = currentPosition + 1 >= questions.size()?currentPosition:currentPosition + 1;
                                                exam_viewpager.setCurrentItem(position);
                                            }
                                        });
                                    }
                                });
                            }
                            else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ExamActivity.this,modify_response.getMessage(),Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        }catch (Exception e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ExamActivity.this,"获取试题失败，请检查网络",Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                }).start();
                title = "随机练习";
                exam_time.setVisibility(View.GONE);
                break;
            }
            case 1:
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            final List<SelectionQuestion> questions = new ArrayList<>();
                            String url = Constant.HTTP + Constant.CURRENT_IP + "exam/virtual_exam";
                            OkHttpClient client = new OkHttpClient();
                            Request request  = new Request.Builder()
                                    .url(url)
                                    .build();
                            okhttp3.Response response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            Gson gson = new Gson();
                            final com.ycjw.minesecurity.model.Response<List<SelectionQuestion>> modify_response = gson.fromJson(responseData, new TypeToken<Response<List<SelectionQuestion>>>(){}.getType());
                            if(modify_response.getMessage().endsWith("成功")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        questions.addAll(modify_response.getData());
                                        onlineExamAdapter = new OnlineExamAdapter(questions);
                                        exam_viewpager.setAdapter(onlineExamAdapter);

                                        previous_question.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                int currentPosition = exam_viewpager.getCurrentItem();
                                                int position = currentPosition - 1 >= 0?currentPosition - 1:0;
                                                exam_viewpager.setCurrentItem(position);
                                            }
                                        });

                                        next_question.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                int currentPosition = exam_viewpager.getCurrentItem();
                                                int position = currentPosition + 1 >= questions.size()?currentPosition:currentPosition + 1;
                                                exam_viewpager.setCurrentItem(position);
                                            }
                                        });
                                    }
                                });
                            }
                            else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ExamActivity.this,modify_response.getMessage(),Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        }catch (Exception e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ExamActivity.this,"获取试题失败，请检查网络",Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                }).start();
                title = "模拟考试";
                break;
            }
            case 2:
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            final List<SelectionQuestion> questions = new ArrayList<>();
                            String url = Constant.HTTP + Constant.CURRENT_IP + "exam/start_exam";
                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("phone",MainActivity.user.getPhoneNum())
                                    .add("examId",examId)
                                    .build();
                            Request request  = new Request.Builder()
                                    .url(url)
                                    .post(requestBody)
                                    .build();
                            okhttp3.Response response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            Gson gson = new Gson();
                            final com.ycjw.minesecurity.model.Response<List<SelectionQuestion>> modify_response = gson.fromJson(responseData, new TypeToken<Response<List<SelectionQuestion>>>(){}.getType());
                            if(modify_response.getMessage().endsWith("成功")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        questions.addAll(modify_response.getData());
                                        onlineExamAdapter = new OnlineExamAdapter(questions);
                                        exam_viewpager.setAdapter(onlineExamAdapter);

                                        previous_question.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                int currentPosition = exam_viewpager.getCurrentItem();
                                                int position = currentPosition - 1 >= 0?currentPosition - 1:0;
                                                exam_viewpager.setCurrentItem(position);
                                            }
                                        });

                                        next_question.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                int currentPosition = exam_viewpager.getCurrentItem();
                                                int position = currentPosition + 1 >= questions.size()?currentPosition:currentPosition + 1;
                                                exam_viewpager.setCurrentItem(position);
                                            }
                                        });
                                    }
                                });
                            }
                            else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ExamActivity.this,modify_response.getMessage(),Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        }catch (Exception e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ExamActivity.this,"获取试题失败，请检查网络",Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                }).start();
                title = "在线考试";break;
            }
        }

        online_exam_text.setText(title);

        if(type != 0){
            timer.schedule(task, 0, 1000);       // timeTask
        }

        setSupportActionBar(online_exam_toolbar);

        exam_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!OnlineExamAdapter.isSubmit){
                    final MyDialog myDialog = new MyDialog(ExamActivity.this,R.style.MyDialog);
                    myDialog.setTitle("提示");
                    myDialog.setMessage("正在考试中，是否退出");
                    myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                        @Override
                        public void onYesOnclick() {
                            finish();
                            myDialog.dismiss();
                        }
                    });
                    myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            myDialog.dismiss();
                        }
                    });
                    myDialog.show();
                }else {
                    finish();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exam_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.submit_exam:
                final MyDialog myDialog = new MyDialog(ExamActivity.this,R.style.MyDialog);
                myDialog.setTitle("是否交卷？");
                myDialog.setMessage("交卷后无法修改");
                myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesOnclick() {
                        timer.cancel();
                        exam_time.setVisibility(View.GONE);
                        switch (examType){
                            case 0:
                            {
                                OnlineExamAdapter.isSubmit = true;
                                exam_viewpager.setCurrentItem(0);
                                onlineExamAdapter.notifyDataSetChanged();
                                exam_viewpager.setAdapter(onlineExamAdapter);
                                Menu menu = online_exam_toolbar.getMenu();
                                menu.setGroupVisible(0,false);
                                onlineExamAdapter.completedQuestion();
                                break;
                            }
                            case 1:
                                OnlineExamAdapter.isSubmit = true;
                                exam_viewpager.setCurrentItem(0);
                                onlineExamAdapter.notifyDataSetChanged();
                                exam_viewpager.setAdapter(onlineExamAdapter);
                                timer.cancel();
                                exam_time.setVisibility(View.GONE);
                                Menu menu = online_exam_toolbar.getMenu();
                                menu.setGroupVisible(0,false);
                                Toast.makeText(getApplicationContext(),"考试结束",Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                uploadExamScore();
                                finish();
                                break;
                        }
                        myDialog.dismiss();
                    }
                });
                myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        myDialog.dismiss();
                    }
                });
                myDialog.show();
                break;
        }
        return true;
    }

    /**
     * 打开考试界面
     * @param context
     * @param examType 考试类型 0-随机练习，1-模拟考试，2-在线考试
     * @param examId 题库id
     */
    public static void actionStart(Context context,int examType,String examId){
        Intent intent = new Intent(context,ExamActivity.class);
        intent.putExtra("examType",examType);
        intent.putExtra("examId",examId);
        context.startActivity(intent);
    }

    private void exam_end(){
        timer.cancel();
        exam_time.setVisibility(View.GONE);
        switch (examType){
            case 0:
            {
                OnlineExamAdapter.isSubmit = true;
                exam_viewpager.setCurrentItem(0);
                onlineExamAdapter.notifyDataSetChanged();
                exam_viewpager.setAdapter(onlineExamAdapter);
                Menu menu = online_exam_toolbar.getMenu();
                menu.setGroupVisible(0,false);
                onlineExamAdapter.completedQuestion();
                break;
            }
            case 1:
                OnlineExamAdapter.isSubmit = true;
                exam_viewpager.setCurrentItem(0);
                onlineExamAdapter.notifyDataSetChanged();
                exam_viewpager.setAdapter(onlineExamAdapter);
                Menu menu = online_exam_toolbar.getMenu();
                menu.setGroupVisible(0,false);
                Toast.makeText(getApplicationContext(),"考试结束",Toast.LENGTH_LONG).show();
                break;
            case 2:
                final MyDialog myDialog = new MyDialog(ExamActivity.this,R.style.MyDialog);
                myDialog.setTitle("提示");
                myDialog.setMessage("考试结束，请确认交卷");
                myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesOnclick() {
                        uploadExamScore();
                        finish();
                        myDialog.dismiss();
                    }
                });
                myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                    }
                });
                myDialog.show();
                break;
        }
    }

    private void uploadExamScore(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String url = Constant.HTTP + Constant.CURRENT_IP + "exam/finished_exam";
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("phone",MainActivity.user.getPhoneNum())
                            .add("examId",examId)
                            .add("grade",onlineExamAdapter.getScore() + "")
                            .build();
                    Request request  = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();
                    okhttp3.Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    com.ycjw.minesecurity.model.Response<ExamRecord> modify_response = gson.fromJson(responseData, new TypeToken<Response<ExamRecord>>(){}.getType());
                    if(modify_response.getMessage().endsWith("成功")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ExamActivity.this,"交卷成功，成绩已上传",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ExamActivity.this,"交卷失败，请检查网络",Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
