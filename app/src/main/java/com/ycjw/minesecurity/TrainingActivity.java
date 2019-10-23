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
import com.ycjw.minesecurity.adapter.PlanAdapter;
import com.ycjw.minesecurity.model.Response;
import com.ycjw.minesecurity.model.TrainPlan;
import com.ycjw.minesecurity.util.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class TrainingActivity extends BaseActivity {
    private ImageView training_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        initWidget();
    }

    /**
     * 初始化培训安排界面控件
     */
    private void initWidget(){
        training_return = findViewById(R.id.training_return);

        //初始化通知信息
        final ArrayList<TrainPlan> trainPlans = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String url = Constant.HTTP + Constant.CURRENT_IP + "train/findTraining";
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
                            RecyclerView training_recycle = findViewById(R.id.training_recycle);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityController.getLastActivity());
                            layoutManager.setOrientation(RecyclerView.VERTICAL);
                            training_recycle.setLayoutManager(layoutManager);
                            trainPlans.addAll(arrayListResponse.getData());
                            PlanAdapter planAdapter = new PlanAdapter(trainPlans);
                            training_recycle.setAdapter(planAdapter);
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
                            Toast.makeText(ActivityController.getLastActivity(),"获取培训安排失败，请检查网络",Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }).start();

        training_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * 打开培训安排界面
     */
    public static void actionStart(Context context){
        Intent intent = new Intent(context,TrainingActivity.class);
        context.startActivity(intent);
    }
}
