package com.ycjw.minesecurity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycjw.minesecurity.util.AndroidUtil;
import com.ycjw.minesecurity.util.Constant;
import com.ycjw.minesecurity.model.User;
import com.ycjw.minesecurity.util.GlideUtil;
import com.ycjw.minesecurity.util.RealPathFromUriUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.ycjw.minesecurity.MainActivity.user;

public class SettingActivity extends BaseActivity {
    private static final int CHOOSE_PHOTO = 2;

    private ImageView setting_return;
    private ImageView setting_head_image;
    private ConstraintLayout setting_head_layout;
    private ConstraintLayout setting_name_layout;
    private ConstraintLayout setting_sex_layout;
    private ConstraintLayout setting_telephone_layout;
    private ConstraintLayout setting_password_layout;
    private Button setting_logout_button;
    private TextView setting_name_content;
    private TextView setting_sex_content;
    private TextView setting_telephone_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //初始化控件
        initWidget();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            setting_name_content.setText(MainActivity.user.getUserName());
            String sex  = "未知";
            if(MainActivity.user.getUserSex() == 1){
                sex = "男";
            }else if(MainActivity.user.getUserSex() == 2){
                sex = "女";
            }
            setting_sex_content.setText(sex);
            setting_telephone_content.setText(MainActivity.user.getPhoneNum());
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(SettingActivity.this,"登录信息失效，请重新登录",Toast.LENGTH_SHORT).show();
        }

    }

    private void initWidget(){
        setting_return = findViewById(R.id.setting_return);
        setting_head_layout = findViewById(R.id.setting_head_layout);
        setting_name_layout = findViewById(R.id.setting_name_layout);
        setting_sex_layout = findViewById(R.id.setting_sex_layout);
        setting_telephone_layout = findViewById(R.id.setting_telephone_layout);
        setting_password_layout = findViewById(R.id.setting_password_layout);
        setting_logout_button = findViewById(R.id.setting_logout_button);
        setting_name_content = findViewById(R.id.setting_name_content);
        setting_sex_content = findViewById(R.id.setting_sex_content);
        setting_telephone_content = findViewById(R.id.setting_telephone_content);
        setting_head_image = findViewById(R.id.setting_head_image);

        //加载用户头像
        GlideUtil.showHeadImage(setting_head_image,SettingActivity.this);

        setting_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setting_head_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(SettingActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    openAlbum();
                }
            }
        });

        setting_name_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyActivity.actionStart(SettingActivity.this,1,MainActivity.user.getUserName());
            }
        });

        setting_sex_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyActivity.actionStart(SettingActivity.this,2,MainActivity.user.getUserSex() + "");
            }
        });

        setting_telephone_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyActivity.actionStart(SettingActivity.this,3,MainActivity.user.getPhoneNum());
            }
        });

        setting_password_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyPasswordActivity.actionStart(SettingActivity.this);
            }
        });

        setting_logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.user = null;
                SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("user", "");
                editor.apply();
                LoginActivity.actionStart(SettingActivity.this);
            }
        });
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,SettingActivity.class);
        context.startActivity(intent);
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(SettingActivity.this, "请授予权限后再使用该功能",Toast.LENGTH_SHORT).show();
                }
                break;
            default:break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.provider.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" +id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.provider.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }else if("content".equalsIgnoreCase(uri.getScheme())){
                imagePath = getImagePath(uri,null);
            }else if("file".equalsIgnoreCase(uri.getScheme())){
                imagePath = uri.getPath();
            }
            displayImage(imagePath);
        }
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection){
        return RealPathFromUriUtils.getRealPathFromUri(SettingActivity.this,uri);
//       String path = null;
//        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
//        if(cursor != null){
//            if(cursor.moveToFirst()){
//                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            }
//            cursor.close();
//        }
//        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath != null){
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            setting_head_image.setImageBitmap(bitmap);
            uploadHeadImg(imagePath);
        }else {
            Toast.makeText(this,"打开图片失败",Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadHeadImg(String imagePath){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//        FormBody body = new FormBody.Builder().add("telephone", MainActivity.user.getPhoneNum())
//                .add("headImg", bitmaptoString(bitmap))
//                .build();
        File file = new File(imagePath);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //可以根据自己的接口需求在这里添加上传的参数
                .addFormDataPart("telephone", MainActivity.user.getPhoneNum())
                .addFormDataPart("headImg",  file.getName(), fileBody)
                .build();
        Request request = new Request.Builder().url(Constant.HTTP + Constant.CURRENT_IP + "user/modify_user_headImg").post(requestBody).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SettingActivity.this,"上传图像失败，请检查网络",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                Gson gson = new Gson();
                com.ycjw.minesecurity.model.Response<User> modify_response = gson.fromJson(responseData, new TypeToken<com.ycjw.minesecurity.model.Response<User>>(){}.getType());
                modify_sucess(modify_response);
            }
        });
    }

    public String bitmaptoString(Bitmap bitmap){
        //将Bitmap转换成字符串
        String string=null;
        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bStream);
        byte[]bytes=bStream.toByteArray();
        string= Base64.encodeToString(bytes,Base64.DEFAULT);
        return string;
    }

    private void modify_sucess(final com.ycjw.minesecurity.model.Response<User> userResponse){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(userResponse.getMessage().endsWith("成功")){
                    //加载用户头像
                    MainActivity.user = userResponse.getData();
                    GlideUtil.set_glide_signature(GlideUtil.ImgType.head_img);
                    GlideUtil.showHeadImage(setting_head_image,SettingActivity.this);
                    Toast.makeText(SettingActivity.this,userResponse.getMessage(),Toast.LENGTH_SHORT).show();
                }else {
                    AndroidUtil.hideOneInputMethod(SettingActivity.this);
                    Toast.makeText(SettingActivity.this,userResponse.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
