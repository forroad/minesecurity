package com.ycjw.minesecurity.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycjw.minesecurity.ActivityController;
import com.ycjw.minesecurity.ExamActivity;
import com.ycjw.minesecurity.ExamRecordActivity;
import com.ycjw.minesecurity.MainActivity;
import com.ycjw.minesecurity.PlanActivity;
import com.ycjw.minesecurity.R;
import com.ycjw.minesecurity.SettingActivity;
import com.ycjw.minesecurity.StatisticActivity;
import com.ycjw.minesecurity.StudyActivity;
import com.ycjw.minesecurity.TrainingActivity;
import com.ycjw.minesecurity.TrainingBackActivity;
import com.ycjw.minesecurity.model.Exam;
import com.ycjw.minesecurity.model.Resource;
import com.ycjw.minesecurity.model.Response;
import com.ycjw.minesecurity.model.TrainPlan;
import com.ycjw.minesecurity.util.Constant;
import com.ycjw.minesecurity.util.GlideUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainAdapter extends PagerAdapter {
    private List<View> views = new ArrayList<>();

    public MainAdapter(List<View> views) {
        this.views = views;
    }

    public MainAdapter() {
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        if(position == 0){
            //培训安排
            ConstraintLayout home_training_schedule = views.get(0).findViewById(R.id.home_training);
            home_training_schedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TrainingActivity.actionStart(ActivityController.getLastActivity());
                }
            });
            //随机练习
            ConstraintLayout home_training = views.get(0).findViewById(R.id.home_random_traning);
            home_training.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ExamActivity.actionStart(ActivityController.getLastActivity(),0,"");
                }
            });
            //在线模考
            ConstraintLayout home_virtual_exam = views.get(0).findViewById(R.id.home_virtual_exam);
            home_virtual_exam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ExamActivity.actionStart(ActivityController.getLastActivity(),1,"");
                }
            });
            //初始化最近学习
            final ArrayList<Resource> resources = new ArrayList<>();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        String url = Constant.HTTP + Constant.CURRENT_IP + "learn_record/find_some_record";
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
                        final com.ycjw.minesecurity.model.Response<ArrayList<Resource>> arrayListResponse = gson.fromJson(responseData, new TypeToken<Response<ArrayList<Resource>>>(){}.getType());
                        ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RecyclerView study_resources = views.get(position).findViewById(R.id.home_recent_study_recycle);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityController.getLastActivity());
                                layoutManager.setOrientation(RecyclerView.VERTICAL);
                                study_resources.setLayoutManager(layoutManager);
                                resources.addAll(arrayListResponse.getData());
                                ResourceAdapter resourceAdapter = new ResourceAdapter(resources,ActivityController.getLastActivity());
                                study_resources.setAdapter(resourceAdapter);
                                //设置RecycleView点击事件
                                resourceAdapter.setOnItemClickListener(new ResourceAdapter.OnRecyclerViewItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, String resourceId) {
                                        StudyActivity.actionStart(ActivityController.getLastActivity(),resourceId);
                                    }
                                });
                            }
                        });
                    }catch (Exception e){
                        ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ActivityController.getLastActivity(),"获取最近学习记录失败，请检查网络",Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();
            //初始化通知信息
            final ArrayList<TrainPlan> trainPlans = new ArrayList<>();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        String url = Constant.HTTP + Constant.CURRENT_IP + "train/findAll_effect_plan";
                        OkHttpClient client = new OkHttpClient();
                        Request request  = new Request.Builder()
                                .url(url)
                                .build();
                        okhttp3.Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        final com.ycjw.minesecurity.model.Response<ArrayList<TrainPlan>> arrayListResponse = gson.fromJson(responseData, new TypeToken<Response<ArrayList<TrainPlan>>>(){}.getType());
                        ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RecyclerView plan_recycle = views.get(position).findViewById(R.id.home_plan_recycle);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityController.getLastActivity());
                                layoutManager.setOrientation(RecyclerView.VERTICAL);
                                plan_recycle.setLayoutManager(layoutManager);
                                trainPlans.addAll(arrayListResponse.getData());
                                PlanAdapter planAdapter = new PlanAdapter(trainPlans);
                                plan_recycle.setAdapter(planAdapter);
                                planAdapter.setOnPlanItemClickListener(new PlanAdapter.OnPlanItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        PlanActivity.actionStart(ActivityController.getLastActivity(),trainPlans.get(position).getPlanName()
                                                ,new SimpleDateFormat("yyyy-MM-dd").format(trainPlans.get(position).getCreateTime())
                                                ,trainPlans.get(position).getTrainContent());
                                    }
                                });
                            }
                        });
                    }catch (Exception e){
                        ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ActivityController.getLastActivity(),"获取最近通知失败，请检查网络",Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        else if(position == 1){
            final ArrayList<Resource> resources = new ArrayList<>();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        String url = Constant.HTTP + Constant.CURRENT_IP + "user/find_all_resource";
                        OkHttpClient client = new OkHttpClient();
                        Request request  = new Request.Builder()
                                .url(url)
                                .build();
                        okhttp3.Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        final com.ycjw.minesecurity.model.Response<ArrayList<Resource>> arrayListResponse = gson.fromJson(responseData, new TypeToken<Response<ArrayList<Resource>>>(){}.getType());
                        ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RecyclerView study_resources = views.get(position).findViewById(R.id.study_resources);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityController.getLastActivity());
                                layoutManager.setOrientation(RecyclerView.VERTICAL);
                                study_resources.setLayoutManager(layoutManager);
                                resources.addAll(arrayListResponse.getData());
                                ResourceAdapter resourceAdapter = new ResourceAdapter(resources,ActivityController.getLastActivity());
                                study_resources.setAdapter(resourceAdapter);
                                //设置RecycleView点击事件
                                resourceAdapter.setOnItemClickListener(new ResourceAdapter.OnRecyclerViewItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, String resourceId) {
                                        StudyActivity.actionStart(ActivityController.getLastActivity(),resourceId);
                                    }
                                });
                            }
                        });
                    }catch (Exception e){
                        ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ActivityController.getLastActivity(),"获取学习资源失败，请检查网络",Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        else if(position == 2){
            final ArrayList<Exam> examArrayList = new ArrayList<>();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        String url = Constant.HTTP + Constant.CURRENT_IP + "exam/find_exam_list";
                        OkHttpClient client = new OkHttpClient();
                        Request request  = new Request.Builder()
                                .url(url)
                                .build();
                        okhttp3.Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        final com.ycjw.minesecurity.model.Response<ArrayList<Exam>> arrayListResponse = gson.fromJson(responseData, new TypeToken<Response<ArrayList<Exam>>>(){}.getType());
                        ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RecyclerView exams = views.get(2).findViewById(R.id.exams);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityController.getLastActivity());
                                layoutManager.setOrientation(RecyclerView.VERTICAL);
                                exams.setLayoutManager(layoutManager);
                                examArrayList.addAll(arrayListResponse.getData());
                                ExamAdapter examAdapter = new ExamAdapter(examArrayList);
                                exams.setAdapter(examAdapter);
                                //设置RecycleView点击事件
                                examAdapter.setOnItemClickListener(new ExamAdapter.OnRecyclerViewItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, String examId) {
                                        for(Exam exam:examArrayList){
                                            if(exam.getExamId().equals(examId)){
                                                if(System.currentTimeMillis() > exam.getExamStartTime().getTime() && System.currentTimeMillis() < exam.getExamEndTime().getTime()){
                                                    ExamActivity.actionStart(ActivityController.getLastActivity(),2,examId);
                                                }else {
                                                    Toast.makeText(ActivityController.getLastActivity(),"请在规定时间内参加考试",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }catch (Exception e){
                        ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ActivityController.getLastActivity(),"获取考试列表，请检查网络",Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        else if(position == 3){
            //我的界面点击事件
            ConstraintLayout mine_setting;mine_setting = views.get(position).findViewById(R.id.mine_setting);
            mine_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SettingActivity.actionStart(ActivityController.getLastActivity());
                }
            });
            //总学习时间
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
                              TextView mine_study_time = views.get(3).findViewById(R.id.mine_study_time);
                              mine_study_time.setText("已学习：" + arrayListResponse.getData() + "分钟");
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
            //显示头像
            ImageView mine_image = views.get(position).findViewById(R.id.mine_image);
            //加载用户头像
            GlideUtil.showHeadImage(mine_image,mine_image.getContext());
            //设置学习统计点击事件
            ConstraintLayout mine_study = views.get(position).findViewById(R.id.mine_study_record);
            mine_study.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StatisticActivity.actionStart(ActivityController.getLastActivity());
                }
            });
            //设置考试记录点击事件
            ConstraintLayout exam_record = views.get(position).findViewById(R.id.mine_exam_record);
            exam_record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ExamRecordActivity.actionStart(ActivityController.getLastActivity());
                }
            });
            //培训反馈
            ConstraintLayout mine_training_feedback = views.get(position).findViewById(R.id.mine_training_feedback);
            mine_training_feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TrainingBackActivity.actionStart(ActivityController.getLastActivity());
                }
            });
        }
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}
