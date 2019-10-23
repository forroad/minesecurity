package com.ycjw.minesecurity.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ycjw.minesecurity.R;
import com.ycjw.minesecurity.model.Exam;
import com.ycjw.minesecurity.model.Resource;
import com.ycjw.minesecurity.util.GlideUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> implements View.OnClickListener{
    private static final String TAG = "ResourceAdapter";
    private ArrayList<Exam> exams;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    ExamAdapter(ArrayList<Exam> exams) {
        this.exams = exams;
    }

    public  interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String examId);
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
        private TextView exam_title;
        private TextView exam_apply_information;
        private TextView exam_start_information;
        public ViewHolder(View itemView) {
            super(itemView);
            this.exam_title = itemView.findViewById(R.id.exam_title);
            this.exam_apply_information = itemView.findViewById(R.id.exam_apply_information);
            this.exam_start_information = itemView.findViewById(R.id.exam_start_information);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_exam,viewGroup,false );
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.exam_title.setText(exams.get(i).getExamTitle());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm");
        //viewHolder.exam_apply_information.setText(dateFormat.format(exams.get(i).getExamDeadline()));
        viewHolder.exam_apply_information.setText(dateFormat.format(exams.get(i).getExamStartTime()));
        //viewHolder.exam_start_information.setText(dateFormat.format(exams.get(i).getExamStartTime()) + "-" + dateFormat2.format(exams.get(i).getExamEndTime()));
        viewHolder.exam_start_information.setText(dateFormat.format(exams.get(i).getExamEndTime()));
        viewHolder.itemView.setTag(exams.get(i).getExamId());
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

}
