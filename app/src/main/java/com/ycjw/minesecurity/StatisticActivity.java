package com.ycjw.minesecurity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycjw.minesecurity.adapter.LineView;
import com.ycjw.minesecurity.adapter.PlanAdapter;
import com.ycjw.minesecurity.model.Response;
import com.ycjw.minesecurity.model.TrainPlan;
import com.ycjw.minesecurity.util.Constant;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class StatisticActivity extends BaseActivity {
    private ImageView statistics_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        initWidget();
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,StatisticActivity.class);
        //intent.putExtra("param",value);
        context.startActivity(intent);
    }


    private void initWidget(){
        statistics_return= findViewById(R.id.statistics_return);


        /**
         * 设置总的学习时间
         */
        {
            //总学习时间
            final TextView statistics_all_study_time = findViewById(R.id.statistics_all_study_time);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        String url = Constant.HTTP + Constant.CURRENT_IP + "learn_record/get_timelong_sum?phone=" + MainActivity.user.getPhoneNum();
                        OkHttpClient client = new OkHttpClient();
                        Request request  = new Request.Builder()
                                .url(url)
                                .build();
                        okhttp3.Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        final com.ycjw.minesecurity.model.Response<Integer> arrayListResponse = gson.fromJson(responseData, new TypeToken<Response<Integer>>(){}.getType());
                        ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String register = new SimpleDateFormat("yyyy-MM-dd").format(MainActivity.user.getRegisterDate());
                                String allStudyTime = "200";

                                statistics_all_study_time.setText("自" + register + "日注册起，至今已学习" + arrayListResponse.getData() + "分钟");
                            }
                        });
                    }catch (Exception e){
                        ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ActivityController.getLastActivity(),"获取学习时间失败，请检查网络",Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        /**
         * 初始化学习时间表
         */
        {
            //初始化通知信息
            final ArrayList<Integer> timeLongs = new ArrayList<>();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        String url = Constant.HTTP + Constant.CURRENT_IP + "learn_record/count_learn_record?phoneNum=" + MainActivity.user.getPhoneNum();
                        OkHttpClient client = new OkHttpClient();
                        Request request  = new Request.Builder()
                                .url(url)
                                .build();
                        okhttp3.Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        final com.ycjw.minesecurity.model.Response<ArrayList<Integer>> arrayListResponse = gson.fromJson(responseData, new TypeToken<Response<ArrayList<Integer>>>(){}.getType());
                        ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeLongs.addAll(arrayListResponse.getData());
                                int max = 0;
                                for(Integer integer:timeLongs){
                                    if(integer > max) max = integer;
                                }
                                List<String> yList = new ArrayList<>();
                                if(max == 0){
                                    yList.add(String.valueOf(100));
                                    yList.add(String.valueOf(80));
                                    yList.add(String.valueOf(60));
                                    yList.add(String.valueOf(40));
                                    yList.add(String.valueOf(20));
                                    yList.add(String.valueOf(0));
                                }else {
                                    yList.add(max + "");
                                    yList.add((int)((max/5) * 4) + "");
                                    yList.add((int)((max/5) * 3) + "");
                                    yList.add((int)((max/5) * 2) + "");
                                    yList.add(max/5 + "");
                                    yList.add("0");
                                }
                                List<Float> datas = new ArrayList<>();
                                if(max == 0){
                                    datas.add((float)(0));
                                    datas.add((float)(0));
                                    datas.add((float)(0));
                                    datas.add((float)(0));
                                    datas.add((float)(0));
                                    datas.add((float)(0));
                                }else {
                                    datas.add(Float.valueOf(timeLongs.get(0))/Float.valueOf(max));
                                    datas.add(Float.valueOf(timeLongs.get(1))/Float.valueOf(max));
                                    datas.add(Float.valueOf(timeLongs.get(2))/Float.valueOf(max));
                                    datas.add(Float.valueOf(timeLongs.get(3))/Float.valueOf(max));
                                    datas.add(Float.valueOf(timeLongs.get(4))/Float.valueOf(max));
                                    datas.add(Float.valueOf(timeLongs.get(5))/Float.valueOf(max));
                                }
                                List<String> xList=getCurrentWeekDay();
                                LineView lineView= findViewById(R.id.study_statistics_lineview);
                                lineView.setViewData(yList,xList,datas);
                                lineView.setVisibility(View.VISIBLE);
                            }
                        });
                    }catch (Exception e){
                        ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ActivityController.getLastActivity(),"获取培训安排失败，请检查网络",Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        /**
         * 初始化考试成绩表
         */
        {
            //初始化通知信息
            final ArrayList<Float> scores = new ArrayList<>();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        String url = Constant.HTTP + Constant.CURRENT_IP + "exam/findExamScore?telephone=" + MainActivity.user.getPhoneNum();
                        OkHttpClient client = new OkHttpClient();
                        Request request  = new Request.Builder()
                                .url(url)
                                .build();
                        okhttp3.Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        final com.ycjw.minesecurity.model.Response<ArrayList<Float>> arrayListResponse = gson.fromJson(responseData, new TypeToken<Response<ArrayList<Float>>>(){}.getType());
                        ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                scores.addAll(arrayListResponse.getData());
                                List<String> yList = new ArrayList<>();
                                yList.add(String.valueOf(100));
                                yList.add(String.valueOf(80));
                                yList.add(String.valueOf(60));
                                yList.add(String.valueOf(40));
                                yList.add(String.valueOf(20));
                                yList.add(String.valueOf(0));
                                List<Float> datas = new ArrayList<>();
                                datas.add((float)(scores.get(0)/100));
                                datas.add((float)(scores.get(1)/100));
                                datas.add((float)(scores.get(2)/100));
                                datas.add((float)(scores.get(3)/100));
                                datas.add((float)(scores.get(4)/100));
                                datas.add((float)(scores.get(5)/100));
                                List<String> xList=new ArrayList<>();
                                xList.add("1");
                                xList.add("2");
                                xList.add("3");
                                xList.add("4");
                                xList.add("5");
                                xList.add("6");
                                LineView study_statistics_lineview_exam= findViewById(R.id.study_statistics_lineview_exam);
                                study_statistics_lineview_exam.setViewData(yList,xList,datas);
                                study_statistics_lineview_exam.setVisibility(View.VISIBLE);
                            }
                        });
                    }catch (Exception e){
                        ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ActivityController.getLastActivity(),"获取考试成绩失败，请检查网络",Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();
        }



        statistics_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * 获取最近一周的时间 MM-dd
     */
    private List<String> getCurrentWeekDay() {
        List<String> data = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        for (int i = 0; i < 6; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            data.add(sdf.format(calendar.getTime()));
        }
        return data;
    }
}
