package com.ycjw.minesecurity.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycjw.minesecurity.ActivityController;
import com.ycjw.minesecurity.ExamActivity;
import com.ycjw.minesecurity.MainActivity;
import com.ycjw.minesecurity.R;
import com.ycjw.minesecurity.model.Response;
import com.ycjw.minesecurity.model.SelectionQuestion;
import com.ycjw.minesecurity.util.Constant;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OnlineExamAdapter extends PagerAdapter {
    public static boolean isSubmit = false;
    private View view;
    private List<SelectionQuestion> questions = new ArrayList<>();
    private String[] anwser = null;
    public OnlineExamAdapter(List<SelectionQuestion> questions) {
        this.questions = questions;
        anwser = new String[questions.size()];
        for(int i = 0;i < questions.size();i++){
            anwser[i] = "";
        }
    }

    public OnlineExamAdapter() {
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater li = ActivityController.getLastActivity().getLayoutInflater();
        view = li.inflate(R.layout.layout_view_online_exam,null,false);
        {
            TextView exam_title = view.findViewById(R.id.exam_title);
            final TextView exam_a = view.findViewById(R.id.exam_a);
            final TextView exam_b = view.findViewById(R.id.exam_b);
            final TextView exam_c = view.findViewById(R.id.exam_c);
            final TextView exam_d = view.findViewById(R.id.exam_d);
            final ColorStateList default_color = exam_a.getTextColors();

            final ColorStateList finalDefault_color = default_color;

            if(anwser[position].contains("A") || anwser[position].contains("a")){
                exam_a.setTextColor(Color.parseColor("#0099ff"));
            }
            if(anwser[position].contains("B") || anwser[position].contains("b")){
                exam_b.setTextColor(Color.parseColor("#0099ff"));
            }
            if(anwser[position].contains("C") || anwser[position].contains("c")){
                exam_c.setTextColor(Color.parseColor("#0099ff"));
            }
            if(anwser[position].contains("D") || anwser[position].contains("d")){
                exam_d.setTextColor(Color.parseColor("#0099ff"));
            }

            if(isSubmit){
                TextView question_correct_answer = view.findViewById(R.id.question_correct_answer);
                TextView question_yours_answer = view.findViewById(R.id.question_yours_answer);

                question_correct_answer.setVisibility(View.VISIBLE);
                question_yours_answer.setVisibility(View.VISIBLE);

                question_correct_answer.setText("正确答案：" + questions.get(position).getRightOptions());
                question_yours_answer.setText("你的答案：" + anwser[position]);
            }

            if(questions.get(position).getIsMultiAnswer() == 0){
                exam_a.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exam_b.setTextColor(default_color);
                        exam_c.setTextColor(default_color);
                        exam_d.setTextColor(default_color);
                        exam_a.setTextColor(Color.parseColor("#0099ff"));
                        if(!(anwser[position].contains("A") || anwser[position].contains("a"))){
                            anwser[position] = "A";
                        }
                    }
                });

                exam_b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exam_a.setTextColor(default_color);
                        exam_c.setTextColor(default_color);
                        exam_d.setTextColor(default_color);
                        exam_b.setTextColor(Color.parseColor("#0099ff"));
                        if(!(anwser[position].contains("B") || anwser[position].contains("b"))){
                            anwser[position] = "B";
                        }
                    }
                });

                exam_c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exam_a.setTextColor(default_color);
                        exam_b.setTextColor(default_color);
                        exam_d.setTextColor(default_color);
                        exam_c.setTextColor(Color.parseColor("#0099ff"));
                        if(!(anwser[position].contains("C") || anwser[position].contains("c"))){
                            anwser[position] = "C";
                        }
                    }
                });

                exam_d.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exam_a.setTextColor(default_color);
                        exam_b.setTextColor(default_color);
                        exam_c.setTextColor(default_color);
                        exam_d.setTextColor(Color.parseColor("#0099ff"));
                        if(!(anwser[position].contains("D") || anwser[position].contains("d"))){
                            anwser[position] = "D";
                        }
                    }
                });
            }else{
                exam_a.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(exam_a.getTextColors().equals(default_color)){
                            exam_a.setTextColor(Color.parseColor("#0099ff"));
                            if(!(anwser[position].contains("A") || anwser[position].contains("a"))){
                                anwser[position] = anwser[position] + "A";
                            }
                        }else {
                            exam_a.setTextColor(default_color);
                            if(anwser[position].contains("A") || anwser[position].contains("a")){
                                anwser[position] = anwser[position].replace("A","");
                                anwser[position] = anwser[position].replace("a","");
                            }
                        }
                    }
                });

                exam_b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(exam_b.getTextColors().equals(default_color)){
                            exam_b.setTextColor(Color.parseColor("#0099ff"));
                            if(!(anwser[position].contains("B") || anwser[position].contains("b"))){
                                anwser[position] = anwser[position] + "B";
                            }
                        }else {
                            exam_b.setTextColor(default_color);
                            if(anwser[position].contains("B") || anwser[position].contains("b")){
                                anwser[position] = anwser[position].replace("B","");
                                anwser[position] = anwser[position].replace("b","");
                            }
                        }

                    }
                });

                exam_c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(exam_c.getTextColors().equals(default_color)){
                            exam_c.setTextColor(Color.parseColor("#0099ff"));
                            if(!(anwser[position].contains("C") || anwser[position].contains("c"))){
                                anwser[position] = anwser[position] + "C";
                            }
                        }else {
                            exam_c.setTextColor(default_color);
                            if(anwser[position].contains("C") || anwser[position].contains("c")){
                                anwser[position] = anwser[position].replace("C","");
                                anwser[position] = anwser[position].replace("c","");
                            }
                        }

                    }
                });

                exam_d.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(exam_d.getTextColors().equals(default_color)){
                            exam_d.setTextColor(Color.parseColor("#0099ff"));
                            if(!(anwser[position].contains("D") || anwser[position].contains("d"))){
                                anwser[position] = anwser[position] + "D";
                            }
                        }else {
                            exam_d.setTextColor(default_color);
                            if(anwser[position].contains("D") || anwser[position].contains("d")){
                                anwser[position] = anwser[position].replace("D","");
                                anwser[position] = anwser[position].replace("d","");
                            }
                        }

                    }
                });
            }

            String question_type = questions.get(position).getIsMultiAnswer()==0?"(单选题)":"(多选题)";
            SpannableStringBuilder style=new SpannableStringBuilder(question_type + questions.get(position).getQuestionDescribe());
            style.setSpan(new ForegroundColorSpan(Color.parseColor("#0099ff")),0,5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            exam_title.setText(style);
            exam_a.setText("A  " + questions.get(position).getOptionA());
            exam_b.setText("B  " + questions.get(position).getOptionB());
            exam_c.setText("C  " + questions.get(position).getOptionC());
            exam_d.setText("D  " + questions.get(position).getOptionD());
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //container.removeView(view);
    }

    private String sortAnswer(String answer){
        String right = "";
        if(answer.contains("A") || answer.contains("a")){
            right += "A";
        }
        if(answer.contains("B") || answer.contains("b")){
            right += "B";
        }
        if(answer.contains("C") || answer.contains("c")){
            right += "C";
        }
        if(answer.contains("D") || answer.contains("d")){
            right += "D";
        }
        return right;
    }

    public int getScore(){
        int score = 0;
        for(int i = 0;i < questions.size();i++){
            if(sortAnswer(questions.get(i).getRightOptions()).equals(sortAnswer(anwser[i]))){
                score += 10;
            }
        }
        return score;
    }

    public void completedQuestion(){
        for(int i = 0;i < questions.size();i++){
            if(sortAnswer(questions.get(i).getRightOptions()).equals(sortAnswer(anwser[i]))) {
                final int index = i;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String url = Constant.HTTP + Constant.CURRENT_IP + "question/completed_one_question";
                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("phone", MainActivity.user.getPhoneNum())
                                    .add("questionId",questions.get(index).getQuestionId())
                                    .build();
                            Request request  = new Request.Builder()
                                    .url(url)
                                    .post(requestBody)
                                    .build();
                            okhttp3.Response response = client.newCall(request).execute();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

}
