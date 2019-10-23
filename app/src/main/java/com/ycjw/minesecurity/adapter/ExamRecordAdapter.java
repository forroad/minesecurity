package com.ycjw.minesecurity.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycjw.minesecurity.ActivityController;
import com.ycjw.minesecurity.ExamActivity;
import com.ycjw.minesecurity.R;
import com.ycjw.minesecurity.model.Exam;
import com.ycjw.minesecurity.model.ExamRecord;
import com.ycjw.minesecurity.model.Response;
import com.ycjw.minesecurity.util.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ExamRecordAdapter extends RecyclerView.Adapter<ExamRecordAdapter.ViewHolder>{
    private static final String TAG = "ExamRecordAdapter";
    private ArrayList<ExamRecord> examRecords;

    public ExamRecordAdapter(ArrayList<ExamRecord> examRecords) {
        this.examRecords = examRecords;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView exam_record_title;
        private TextView exam_time_text;
        private TextView exam_score_text;
        public ViewHolder(View itemView) {
            super(itemView);
            this.exam_record_title = itemView.findViewById(R.id.exam_title_record);
            this.exam_time_text = itemView.findViewById(R.id.exam_time_text);
            this.exam_score_text = itemView.findViewById(R.id.exam_score_text);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_view_exam_record,viewGroup,false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder,final int i) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String url = Constant.HTTP + Constant.CURRENT_IP + "exam/findExamById?examId=" + examRecords.get(i).getExamId();
                    OkHttpClient client = new OkHttpClient();
                    Request request  = new Request.Builder()
                            .url(url)
                            .build();
                    okhttp3.Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    final com.ycjw.minesecurity.model.Response<Exam> arrayListResponse = gson.fromJson(responseData, new TypeToken<Response<Exam>>(){}.getType());
                    if(arrayListResponse.getMessage().endsWith("成功")){
                        ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Exam exam = arrayListResponse.getData();
                                viewHolder.exam_record_title.setText(exam.getExamTitle());
                            }
                        });
                    }
                }catch (Exception e){
                    ActivityController.getLastActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ActivityController.getLastActivity(),"获取考试列表，请检查网络",Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }).start();
        Log.e(TAG,examRecords.toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        viewHolder.exam_time_text.setText("考试时间：" + dateFormat.format(examRecords.get(i).getEndTime()));
        viewHolder.exam_score_text.setText("考试得分：" + examRecords.get(i).getGrade());
    }

    @Override
    public int getItemCount() {
        return examRecords.size();
    }

}
