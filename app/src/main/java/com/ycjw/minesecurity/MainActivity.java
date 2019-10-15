package com.ycjw.minesecurity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ycjw.minesecurity.adapter.MainAdapter;
import com.ycjw.minesecurity.model.User;

import java.security.cert.PolicyNode;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    public static User user = null;
    private LinearLayout tab_homepage;
    private LinearLayout tab_study;
    private LinearLayout tab_exam;
    private LinearLayout tab_mine;
    private ImageView home_page;
    private ImageView home_page1;
    private ImageView security_study;
    private ImageView security_study1;
    private ImageView exam;
    private ImageView exam1;
    private ImageView mine;
    private ImageView mine1;
    private ViewPager main_viewpage;
    private ArrayList<View> views = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        initWidgets();

        //设置底部导航栏点击事件
        setOnClick();

    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        //intent.putExtra("param",value);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }

    /**
     * 初始化主界面控件
     */
    private void initWidgets(){
        tab_homepage = findViewById(R.id.tab_homepage);
        tab_study = findViewById(R.id.tab_study);
        tab_exam = findViewById(R.id.tab_exam);
        tab_mine = findViewById(R.id.tab_mine);
        home_page = findViewById(R.id.home_page);
        home_page1 = findViewById(R.id.home_page1);
        security_study = findViewById(R.id.security_study);
        security_study1 = findViewById(R.id.security_study1);
        exam = findViewById(R.id.exam);
        exam1 = findViewById(R.id.exam1);
        mine = findViewById(R.id.mine);
        mine1 = findViewById(R.id.mine1);
        main_viewpage = findViewById(R.id.main_viewpage);

        LayoutInflater li = getLayoutInflater();
        views.add(li.inflate(R.layout.layout_view_home,null,false));
        views.add(li.inflate(R.layout.layout_view_study,null,false));
        views.add(li.inflate(R.layout.layout_view_exam,null,false));
        views.add(li.inflate(R.layout.layout_view_mine,null,false));
        MainAdapter mAdapter = new MainAdapter(views);
        main_viewpage.setAdapter(mAdapter);
        main_viewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /**
                 * 当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法会一直得到
                 * arg0:当前页面，即点击滑动的页面
                 * arg1:当前页面偏移的百分比
                 * arg2:当前页面偏移的像素位置
                 */
            }

            @Override
            public void onPageSelected(int position) {
                //arg0 当前选中的页面，在页面跳转完毕的时候调用的。
                switch (position){
                    case 0:
                        setInitImageView();
                        home_page1.setAlpha(1.0f);
                        home_page.setAlpha(0.0f);
                        break;
                    case 1:
                        setInitImageView();
                        security_study1.setAlpha(1.0f);
                        security_study.setAlpha(0.0f);
                        break;
                    case 2:
                        setInitImageView();
                        exam1.setAlpha(1.0f);
                        exam.setAlpha(0.0f);
                        break;
                    case 3:
                        setInitImageView();
                        mine1.setAlpha(1.0f);
                        mine.setAlpha(0.0f);
                        break;
                     default:break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /**
                 * arg0
                 *	1:表示正在滑动
                 *	2:表示滑动完毕
                 *	0:表示什么都没做，就是停在那
                 */
            }
        });

        Toolbar toolbar = findViewById(R.id.home_title);
        setSupportActionBar(toolbar);
    }

    /**
     * 设置点击事件
     */
    private void setOnClick(){
        tab_homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setInitImageView();
               home_page1.setAlpha(1.0f);
               home_page.setAlpha(0.0f);
               main_viewpage.setCurrentItem(0);
               Toolbar toolbar = findViewById(R.id.home_title);
               setSupportActionBar(toolbar);
            }
        });
        tab_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInitImageView();
                security_study1.setAlpha(1.0f);
                security_study.setAlpha(0.0f);
                main_viewpage.setCurrentItem(1);
                Toolbar toolbar = findViewById(R.id.study_title);
                setSupportActionBar(toolbar);
            }
        });
        tab_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInitImageView();
                exam1.setAlpha(1.0f);
                exam.setAlpha(0.0f);
                main_viewpage.setCurrentItem(2);
                Toolbar toolbar = findViewById(R.id.exam_title_toolbar);
                setSupportActionBar(toolbar);
            }
        });
        tab_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInitImageView();
                mine1.setAlpha(1.0f);
                mine.setAlpha(0.0f);
                main_viewpage.setCurrentItem(3);
                Toolbar toolbar = findViewById(R.id.mine_title_toolbar);
                setSupportActionBar(toolbar);
            }
        });
    }

    //将底部导航栏状态设置为初始状态
    private void setInitImageView(){
        home_page1.setAlpha(0.0f);
        home_page.setAlpha(1.0f);
        security_study1.setAlpha(0.0f);
        security_study.setAlpha(1.0f);
        exam1.setAlpha(0.0f);
        exam.setAlpha(1.0f);
        mine1.setAlpha(0.0f);
        mine.setAlpha(1.0f);
    }
}
