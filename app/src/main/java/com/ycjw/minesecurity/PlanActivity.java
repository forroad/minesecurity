package com.ycjw.minesecurity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PlanActivity extends BaseActivity {
    private TextView plan_title;
    private TextView plan_create_time;
    private TextView plan_content;
    private ImageView plan_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        initWidget();
    }

    /**
     * 初始化通知界面控件
     */
    private void initWidget(){
        plan_title = findViewById(R.id.plan_title);
        plan_create_time = findViewById(R.id.plan_create_time);
        plan_content = findViewById(R.id.plan_content);
        plan_return = findViewById(R.id.plan_return);

        Intent intent = getIntent();
        String title = intent.getStringExtra("planName");
        String create_time = intent.getStringExtra("plan_create_time");
        String content = intent.getStringExtra("plan_content");

        plan_title.setText(title);
        plan_create_time.setText(create_time);
        plan_content.setText(content);

        plan_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * 打开通知界面
     * @param context
     * @param planName 通知标题
     * @param plan_create_time 通知发布时间
     * @param plan_content 通知内容
     */
    public static void actionStart(Context context, String planName, String plan_create_time, String plan_content){
        Intent intent = new Intent(context,PlanActivity.class);
        intent.putExtra("planName",planName);
        intent.putExtra("plan_create_time",plan_create_time);
        intent.putExtra("plan_content",plan_content);
        context.startActivity(intent);
    }
}
