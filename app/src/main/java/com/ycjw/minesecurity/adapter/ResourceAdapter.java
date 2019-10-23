package com.ycjw.minesecurity.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ycjw.minesecurity.MainActivity;
import com.ycjw.minesecurity.R;
import com.ycjw.minesecurity.SettingActivity;
import com.ycjw.minesecurity.model.Resource;
import com.ycjw.minesecurity.util.Constant;
import com.ycjw.minesecurity.util.GlideUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ViewHolder> implements View.OnClickListener{
    private static final String TAG = "ResourceAdapter";
    private ArrayList<Resource> resources;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private Context context;



    public ResourceAdapter(ArrayList<Resource> resources,Context context) {
        this.resources = resources;
        this.context = context;
    }

    public  interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , String resourceId);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(String)v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView study_resource_image;
        private ImageView study_resource_image1;
        private ImageView resource_cover;
        private TextView text_title;
        ViewHolder(View itemView) {
            super(itemView);
            this.study_resource_image = itemView.findViewById(R.id.study_resource_image);
            this.text_title = itemView.findViewById(R.id.home_recent_study_resource_text);
            study_resource_image1 = itemView.findViewById(R.id.study_resource_image1);
            resource_cover = itemView.findViewById(R.id.resource_cover);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_study,viewGroup,false );
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        GlideUtil.showResourceImg(viewHolder.resource_cover,resources.get(i).getResourceId(),viewHolder.itemView.getContext());
        viewHolder.text_title.setText(resources.get(i).getResourceName());
        viewHolder.itemView.setTag(resources.get(i).getResourceId());
    }

    @Override
    public int getItemCount() {
        return resources.size();
    }

}
