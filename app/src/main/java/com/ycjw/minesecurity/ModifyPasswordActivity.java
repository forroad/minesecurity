package com.ycjw.minesecurity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ModifyPasswordActivity extends BaseActivity {
    private ImageView modify_password_return;
    private EditText old_password;
    private EditText new_password;
    private Button modify_password_button;
    private EditText new_password_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);

        //初始化控件
        initWidget();
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,ModifyPasswordActivity.class);
        //intent.putExtra("param",value);
        context.startActivity(intent);
    }

    private void initWidget(){
        modify_password_return = findViewById(R.id.user_password_modify_return);
        old_password = findViewById(R.id.old_password);
        new_password = findViewById(R.id.new_password);
        modify_password_button = findViewById(R.id.modify_password_button);
        new_password_ok = findViewById(R.id.new_password_ok);

        //返回
        modify_password_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                AndroidUtil.hideOneInputMethod(ModifyPasswordActivity.this);
            }
        });

        //输入框自动聚焦
        old_password.requestFocus();

        //确定修改
        modify_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String old_password_text = old_password.getText().toString();
                final String new_password_text = new_password.getText().toString();
                final String new_password_ok_text = new_password_ok.getText().toString();

                if (!new_password_text.equals(new_password_ok_text)) {
                    AndroidUtil.hideOneInputMethod(ModifyPasswordActivity.this);
                    Toast.makeText(ModifyPasswordActivity.this,"输入的两次密码不相等",Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String url = Constant.HTTP + Constant.CURRENT_IP + "user/modify_user_password";
                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("telephone",MainActivity.user.getPhoneNum())
                                    .add("old_password",old_password_text)
                                    .add("new_password",new_password_text)
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
                                    Toast.makeText(ModifyPasswordActivity.this,"修改失败，请检查网络",Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void modify_sucess(final Response<User> userResponse){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(userResponse.getMessage().endsWith("成功")){
                    AndroidUtil.hideOneInputMethod(ModifyPasswordActivity.this);
                    Toast.makeText(ModifyPasswordActivity.this,userResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    MainActivity.user = null;
                    SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user", "");
                    editor.apply();
                    LoginActivity.actionStart(ModifyPasswordActivity.this);
                }else {
                    AndroidUtil.hideOneInputMethod(ModifyPasswordActivity.this);
                    Toast.makeText(ModifyPasswordActivity.this,userResponse.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
