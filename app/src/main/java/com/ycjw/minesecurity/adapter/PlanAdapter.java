package com.ycjw.minesecurity.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycjw.minesecurity.ActivityController;
import com.ycjw.minesecurity.R;
import com.ycjw.minesecurity.model.Exam;
import com.ycjw.minesecurity.model.ExamRecord;
import com.ycjw.minesecurity.model.Response;
import com.ycjw.minesecurity.model.TrainPlan;
import com.ycjw.minesecurity.util.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "PlanAdapter";
    private ArrayList<TrainPlan> trainPlans;
    private OnPlanItemClickListener mOnItemClickListener = null;

    public  interface OnPlanItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnPlanItemClickListener(OnPlanItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }


    public PlanAdapter(ArrayList<TrainPlan> trainPlans) {
        this.trainPlans = trainPlans;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView information_title;
        private TextView information_time;
        public ViewHolder(View itemView) {
            super(itemView);
            this.information_title = itemView.findViewById(R.id.information_title);
            this.information_time = itemView.findViewById(R.id.information_time);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_view_plan,viewGroup,false );
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder,final int i) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        viewHolder.information_title.setText(trainPlans.get(i).getPlanName());
        viewHolder.information_time.setText(dateFormat.format(trainPlans.get(i).getCreateTime()));
        viewHolder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
        return trainPlans.size();
    }

}
