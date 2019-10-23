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
import com.ycjw.minesecurity.model.TrainingBack;
import com.ycjw.minesecurity.util.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class TrainBackAdapter extends RecyclerView.Adapter<TrainBackAdapter.ViewHolder>{
    private static final String TAG = "TrainBackAdapter";
    private ArrayList<TrainingBack> trainingBacks;

    public TrainBackAdapter(ArrayList<TrainingBack> trainingBacks) {
        this.trainingBacks = trainingBacks;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView training_back_text;
        private TextView teaining_back_time;
        public ViewHolder(View itemView) {
            super(itemView);
            this.training_back_text = itemView.findViewById(R.id.training_back_text);
            this.teaining_back_time = itemView.findViewById(R.id.teaining_back_time);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_view_mine_training_back,viewGroup,false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder,final int i) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        viewHolder.training_back_text.setText(trainingBacks.get(i).getContent());
        viewHolder.teaining_back_time.setText(dateFormat.format(trainingBacks.get(i).getCreateDate()));
    }

    @Override
    public int getItemCount() {
        return trainingBacks.size();
    }

}
