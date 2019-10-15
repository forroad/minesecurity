package com.ycjw.minesecurity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycjw.minesecurity.model.AndroidUtil;
import com.ycjw.minesecurity.model.Constant;
import com.ycjw.minesecurity.model.Response;
import com.ycjw.minesecurity.model.User;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.ycjw.minesecurity.MainActivity.user;

public class LoginActivity extends BaseActivity {
    private static int symbol = 1;
    private EditText login_telephone;
    private EditText login_password;
    private EditText login_password_ok;
    private Button login_or_register;
    private TextView login_tip;
    private TextView toolbar_text;
    private ImageView login_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化控件信息
        initLoginWidget();
    }

    private void initLoginWidget(){
        login_telephone = findViewById(R.id.login_telephone);
        login_password = findViewById(R.id.login_password);
        login_password_ok = findViewById(R.id.login_password_ok);
        login_or_register = findViewById(R.id.login_or_register);
        login_tip = findViewById(R.id.login_tip);
        toolbar_text = findViewById(R.id.login_title_text);
        login_return = findViewById(R.id.login_return);

        //初始化界面
        changeWidget();

        //设置登录注册界面转换点击事件
        login_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                symbol=symbol==1?2:1;
                changeWidget();
            }
        });

        login_or_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidUtil.hideOneInputMethod(LoginActivity.this);
                login_or_register();
            }
        });

        login_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(symbol == 1){
                    ActivityController.finishAll();
                }else {
                    symbol = 1;
                    changeWidget();
                }
            }
        });

    }

    /**
     * 修改登录界面和注册界面样式显示
     */
    private void changeWidget(){
        if(symbol == 1){
            toolbar_text.setText("登录");
            login_password_ok.setVisibility(View.GONE);
            login_or_register.setText("登录");
            login_tip.setText("立即注册");
        }else {
            toolbar_text.setText("注册");
            login_password_ok.setVisibility(View.VISIBLE);
            login_or_register.setText("注册");
            login_tip.setText("已有账号，立即登录");
        }
    }

    //登录或注册
    private void login_or_register(){
        if(symbol == 1){
            //登录
            final String telephone = login_telephone.getText().toString();
            final String password = login_password.getText().toString();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        String url =Constant.HTTP + Constant.CURRENT_IP + "user/login";
                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("telephone",telephone)
                                .add("password",password)
                                .build();
                        Request request  = new Request.Builder()
                                .url(url)
                                .post(requestBody)
                                .build();
                        okhttp3.Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        com.ycjw.minesecurity.model.Response<User> login_response = gson.fromJson(responseData, new TypeToken<com.ycjw.minesecurity.model.Response<User>>(){}.getType());
                        login_success(login_response);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        else {
            //注册
            final String telephone = login_telephone.getText().toString();
            final String password = login_password.getText().toString();
            final String password_ok = login_password_ok.getText().toString();
            //判断两次密码是否相等
            if(!password.equals(password_ok)){
                Toast.makeText(LoginActivity.this,"输入的两次密码不相等",Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        String url =Constant.HTTP + Constant.CURRENT_IP + "user/register";
                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("telephone",telephone)
                                .add("password",password)
                                .build();
                        Request request  = new Request.Builder()
                                .url(url)
                                .post(requestBody)
                                .build();
                        okhttp3.Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        com.ycjw.minesecurity.model.Response<User> register_response = gson.fromJson(responseData, new TypeToken<com.ycjw.minesecurity.model.Response<User>>(){}.getType());
                        register_sucess(register_response);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    //登录逻辑处理
    private void login_success(final Response<User> userResponse){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               if(userResponse.getMessage().equals("登录成功")){
                   user = userResponse.getData();
                   MainActivity.actionStart(LoginActivity.this);
               }else {
                   Toast.makeText(LoginActivity.this,userResponse.getMessage(),Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    //注册逻辑处理
    private void register_sucess(final Response<User> userResponse){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(userResponse.getMessage().equals("注册成功")){
                    symbol = 1;
                    changeWidget();
                    Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this,userResponse.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
