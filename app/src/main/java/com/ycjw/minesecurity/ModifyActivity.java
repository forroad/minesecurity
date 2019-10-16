package com.ycjw.minesecurity;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycjw.minesecurity.util.AndroidUtil;
import com.ycjw.minesecurity.util.Constant;
import com.ycjw.minesecurity.model.Response;
import com.ycjw.minesecurity.model.User;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ModifyActivity extends BaseActivity {
    private ImageView user_information_modify_return;
    private TextView user_information_modify_save;
    private TextView user_information_modify_text;
    private EditText user_information_input;
    private ImageView user_information_input_delete;

    private ConstraintLayout user_information_modify_layout;
    private ConstraintLayout user_information_modify_sex_layout;
    private ConstraintLayout user_information_modify_sex_male_layout;
    private ConstraintLayout user_information_modify_sex_female_layout;

    private ImageView user_information_modify_sex_male_image;
    private ImageView user_information_modify_sex_female_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        //初始化控件
        initWidget();
    }

    private void initWidget(){
        Toolbar toolbar = findViewById(R.id.user_information_modify_toolbar);
        setSupportActionBar(toolbar);
        user_information_modify_return = findViewById(R.id.user_information_modify_return);
        user_information_modify_save = findViewById(R.id.user_information_modify_save);
        user_information_input = findViewById(R.id.user_information_input);
        user_information_input_delete = findViewById(R.id.user_information_input_delete);
        user_information_modify_text = findViewById(R.id.user_information_modify_text);
        user_information_modify_layout = findViewById(R.id.user_information_modify_layout);
        user_information_modify_sex_layout = findViewById(R.id.user_information_modify_sex_layout);
        user_information_modify_sex_male_image = findViewById(R.id.user_information_modify_sex_male_image);
        user_information_modify_sex_female_image = findViewById(R.id.user_information_modify_sex_female_image);
        user_information_modify_sex_female_layout = findViewById(R.id.user_information_modify_sex_female_layout);
        user_information_modify_sex_male_layout = findViewById(R.id.user_information_modify_sex_male_layout);


        Intent intent = getIntent();
        final int modify_type = intent.getIntExtra("modify_type",0);
        String show_text = intent.getStringExtra("show_text");
        setWidgetText(modify_type,show_text);

        user_information_input.requestFocus();
        AndroidUtil.openInput(ModifyActivity.this,user_information_input);

        user_information_modify_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidUtil.hideOneInputMethod(ModifyActivity.this);
                onBackPressed();
            }
        });

        user_information_input_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_information_input.setText("");
            }
        });

        user_information_modify_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidUtil.hideOneInputMethod(ModifyActivity.this);
                saveUserInformation(modify_type,user_information_input.getText().toString());
            }
        });

        user_information_modify_sex_male_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_information_modify_sex_male_image.setVisibility(View.VISIBLE);
                user_information_modify_sex_female_image.setVisibility(View.GONE);
            }
        });
        user_information_modify_sex_female_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_information_modify_sex_male_image.setVisibility(View.GONE);
                user_information_modify_sex_female_image.setVisibility(View.VISIBLE);
            }
        });
    }

    public static void actionStart(Context context,int modify_type,String show_text){
        Intent intent = new Intent(context,ModifyActivity.class);
        intent.putExtra("modify_type",modify_type);
        intent.putExtra("show_text",show_text);
        context.startActivity(intent);
    }

    private void setWidgetText(int type_int,String show_text){
        if(type_int == 2){
            user_information_modify_sex_layout.setVisibility(View.VISIBLE);
            user_information_modify_layout.setVisibility(View.GONE);
            if(show_text.equals("1")){
                user_information_modify_sex_male_image.setVisibility(View.VISIBLE);
                user_information_modify_sex_female_image.setVisibility(View.GONE);
            }else if(show_text.equals("2")) {
                user_information_modify_sex_male_image.setVisibility(View.GONE);
                user_information_modify_sex_female_image.setVisibility(View.VISIBLE);
            }else {
                user_information_modify_sex_male_image.setVisibility(View.GONE);
                user_information_modify_sex_female_image.setVisibility(View.GONE);
            }
        }else {
            user_information_modify_sex_layout.setVisibility(View.GONE);
            user_information_modify_layout.setVisibility(View.VISIBLE);
        }

        String type = "信息";
        switch (type_int){
            case 0:break;
            case 1:type = "姓名";break;
            case 2:type = "性别";break;
            case 3:type = "手机号";break;
            default:break;
        }
        user_information_modify_text.setText(type + "修改");
        SpannableString hint = new SpannableString("请输入" + type);
        user_information_input.setHint(hint);
        user_information_input.setText(show_text);
    }

    private void saveUserInformation(int type_int,String text){
        switch (type_int){
            case 0:break;
            case 1: {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String url = Constant.HTTP + Constant.CURRENT_IP + "user/modify_user_name";
                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("telephone",MainActivity.user.getPhoneNum())
                                    .add("name",user_information_input.getText().toString())
                                    .build();
                            Request request  = new Request.Builder()
                                    .url(url)
                                    .post(requestBody)
                                    .build();
                            okhttp3.Response response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            Gson gson = new Gson();
                            com.ycjw.minesecurity.model.Response<User> modify_response = gson.fromJson(responseData, new TypeToken<Response<User>>(){}.getType());
                            modify_sucess(modify_response);
                        }catch (Exception e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ModifyActivity.this,"修改失败，请检查网络",Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            }
            case 2:{
                int sex = 3;
                if(user_information_modify_sex_male_image.getVisibility() == View.VISIBLE){
                    sex = 1;
                }else if(user_information_modify_sex_female_image.getVisibility() == View.VISIBLE){
                    sex = 2;
                }else {
                    Toast.makeText(ModifyActivity.this,"请输入正确的性别",Toast.LENGTH_SHORT).show();
                    return;
                }
                final int finalSex = sex;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String url = Constant.HTTP + Constant.CURRENT_IP + "user/modify_user_sex";
                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("telephone",MainActivity.user.getPhoneNum())
                                    .add("sex", finalSex + "")
                                    .build();
                            Request request  = new Request.Builder()
                                    .url(url)
                                    .post(requestBody)
                                    .build();
                            okhttp3.Response response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            Gson gson = new Gson();
                            com.ycjw.minesecurity.model.Response<User> modify_response = gson.fromJson(responseData, new TypeToken<Response<User>>(){}.getType());
                            modify_sucess(modify_response);
                        }catch (Exception e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ModifyActivity.this,"修改失败，请检查网络",Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            }
            case 3:{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String url = Constant.HTTP + Constant.CURRENT_IP + "user/modify_user_phoneNum";
                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("telephone",MainActivity.user.getPhoneNum())
                                    .add("phoneNum",user_information_input.getText().toString())
                                    .build();
                            Request request  = new Request.Builder()
                                    .url(url)
                                    .post(requestBody)
                                    .build();
                            okhttp3.Response response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            Gson gson = new Gson();
                            com.ycjw.minesecurity.model.Response<User> modify_response = gson.fromJson(responseData, new TypeToken<Response<User>>(){}.getType());
                            modify_sucess(modify_response);
                        }catch (Exception e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ModifyActivity.this,"修改失败，请检查网络",Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            }
            default:break;
        }
    }

    private void modify_sucess(final Response<User> userResponse){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(userResponse.getMessage().endsWith("成功")){
                    AndroidUtil.hideOneInputMethod(ModifyActivity.this);
                    Toast.makeText(ModifyActivity.this,userResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.e("Setting",userResponse.toString());
                    MainActivity.user = userResponse.getData();
                    onBackPressed();
                }else {
                    AndroidUtil.hideOneInputMethod(ModifyActivity.this);
                    Toast.makeText(ModifyActivity.this,userResponse.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
