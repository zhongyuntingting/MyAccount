package com.example.zs.myaccount;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.zs.application.MyAplication;
import com.example.zs.bean.PayoutContentInfo;
import com.example.zs.pager.AccountPager;
import com.example.zs.pager.BasePager;
import com.example.zs.pager.OwnerPager;
import com.example.zs.pager.ReportFormPager;
import com.example.zs.pager.WishPager;
import com.example.zs.utils.DensityUtil;
import com.example.zs.utils.ScaleBitmapUtils;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private String tag = "MainActivity";
    public static ViewPager vp_mainactivity;
    public RadioGroup rg_mainactivity_bottom;
    public RadioButton rb_mainactivity_detail;
    public RadioButton rb_mainactivity_wish;
    public RadioButton rb_mainactivity_plus;
    public RadioButton rb_mainactivity_list;
    public RadioButton rb_mainactivity_mine;
    private static final int PHOTO_REQUEST_CAREMA = 100;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 101;// 从相册中选择
    private static final int PHOTO_REQUEST_CAREMA_FROM_ACCOUNT = 201;
    private static final int PHOTO_REQUEST_GALLERY_FROM_ACCOUNT = 202;
    private Uri photoUri;
    //新建ArrayList用于存储ViewPager里的不同page，从BasePager里面拿View
    List<BasePager> pageList = new ArrayList<BasePager>();
    private MainActivity_ContentAdapter mainActivity_contentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //隐藏标题栏
        getSupportActionBar().hide();

        /*//透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/

        //初始化主页面的控件,并抽成成员变量，方便调用
        //ViewPager
        vp_mainactivity = (ViewPager) findViewById(R.id.vp_mainactivity);
        //RadioGroup
        rg_mainactivity_bottom = (RadioGroup) findViewById(R.id.rg_mainactivity_bottom);
        //RadioButton
        rb_mainactivity_detail = (RadioButton) findViewById(R.id.rb_mainactivity_detail);
        rb_mainactivity_wish = (RadioButton) findViewById(R.id.rb_mainactivity_wish);
        // rb_mainactivity_plus = (RadioButton) findViewById(R.id.rb_mainactivity_plus);
        rb_mainactivity_list = (RadioButton) findViewById(R.id.rb_mainactivity_list);
        rb_mainactivity_mine = (RadioButton) findViewById(R.id.rb_mainactivity_mine);
        //将“明细”（第一个）按钮默认设为选定状态
        rg_mainactivity_bottom.check(R.id.rb_mainactivity_detail);

        //将每个page页面加入pageList
        pageList.add(new AccountPager(this));
        pageList.add(new WishPager(this));
        //pageList.add(new 57页面);
        pageList.add(new ReportFormPager(this));
        pageList.add(new OwnerPager(this));

        //新建Adapter用于每个RadioButton点击显示不同页面
        mainActivity_contentAdapter = new MainActivity_ContentAdapter();

        vp_mainactivity.setAdapter(mainActivity_contentAdapter);

        //处理RadioGroup的点击事件，使之与对应的的ViewPager页面对应
        //（暂时跳转两个页面做测试）
        rg_mainactivity_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId) {

                    case R.id.rb_mainactivity_detail:
                        vp_mainactivity.setCurrentItem(0, false);
                        pageList.get(0).initData();
                        MyAplication application0 = (MyAplication) getApplication();
                            Log.i("lalalala", "设置了");
                            application0.setAccountPager(pageList.get(0));

                        break;

                    case R.id.rb_mainactivity_wish:
                        Log.i(tag, "00");
                        vp_mainactivity.setCurrentItem(1, false);
                        pageList.get(1).initData();
                        MyAplication application1 = (MyAplication) getApplication();
                        if (application1.getWishPager() == null) {
                            application1.setWishPager(pageList.get(1));
                        }
                        break;
                    case R.id.rb_mainactivity_list:
                        vp_mainactivity.setCurrentItem(2, false);
                        pageList.get(2).initData();
                        MyAplication application2 = (MyAplication) getApplication();
                        if (application2.getReportFormPager() == null) {
                            application2.setReportFormPager(pageList.get(2));
                        }
                        break;

                    case R.id.rb_mainactivity_mine:
                        vp_mainactivity.setCurrentItem(3, false);
                        pageList.get(3).initData();
                        MyAplication application03 = (MyAplication) getApplication();
                        application03.setOwnerPager(pageList.get(3));
                        break;
                }
            }
        });

        vp_mainactivity.setCurrentItem(0);
        pageList.get(0).initData();
        MyAplication application0 = (MyAplication) getApplication();

            Log.i("lalalala", "设置了");
            application0.setAccountPager(pageList.get(0));

        Log.i(tag, "wennm");
    }

    //新建Adapter用于每个RadioButton点击显示不同页面
    public class MainActivity_ContentAdapter extends PagerAdapter {
        private int mChildCount = 0;

        //解决notifyDataSetChanged 无法刷新page数据的bug
        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }


        @Override
        public int getCount() {

            return pageList.size();
            //return 0;
        }

        public BasePager getRefreshTarget() {
            if (pageList != null && pageList.get(3) != null) {
                return pageList.get(3);
            }
            return null;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
            // return false;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i("mainActivity", "viewpager-instantiateItem=" + position);
            BasePager basePager = pageList.get(position);
            /*测试代码
            Log.i("hahaha","&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
           if(basePager!=null)
                Log.i("hahaha","##################");
            if(basePager.mrootView!=null)
                Log.i("hahaha","000000000000000");*/
            container.addView(basePager.mrootView);
            return basePager.mrootView;
            // return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            //super.destroyItem(container, position, object);
        }
    }


    public void test() {


        Log.i("haha", "云中歌");

    }

    public void test2() {
        Log.i("zhangxudong", "zhangxudong");

        Log.i("lll", "llll");

        Log.i("aola", "aolaaolaaola");

    }

    public void test3() {


        Log.i("11", "大油桃");

    }

    long firstTime;

    //方式一
    //防止用户误触返回键退出应用,重写back键
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {//比较优先级低
            Toast.makeText(MainActivity.this, "再次点击退出钱哆哆记账",
                    Toast.LENGTH_SHORT).show();
            firstTime = secondTime;//记录上次的时间
        } else {
            super.onBackPressed();
        }
    }

    /*//按键松开触发
    //方式二
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            long secondTime = System.currentTimeMillis();
            if (secondTime-firstTime>1000){
                Toast.makeText(MainActivity.this, "再次点击退出钱哆哆记账",
                        Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            }else {
                finish();
            }
        }
        return super.onKeyUp(keyCode, event);
    }
*/
    public void add(View v) {
        //test
        /*Intent intent = new Intent(this,AddRecordActivity.class);
        intent.putExtra("isIncome",false);
        intent.putExtra("id",1);
        intent.putExtra("year",2016);
        intent.putExtra("month",1);
        intent.putExtra("day",1);
        intent.putExtra("money","350");
        intent.putExtra("remarks","备注测试");
        intent.putExtra("photoUriString","");
        intent.putExtra("resourceID",2130837665);
        intent.putExtra("categoryName","红包");
        startActivityForResult(intent,110);*/
        Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
        //test intent.putExtra("photoUriString","content://media/external/images/media/33");
        startActivityForResult(intent, 50);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i(tag, resultCode + "--" + requestCode);
        Log.i(tag, "000");
        Bitmap resultBitmap;
        //确认健返回
        if (resultCode == 555 && intent != null) {
            //刷新page的数据
            int id = intent.getIntExtra("id", 0);
            int resourceID = intent.getIntExtra("resourceID", 0);
            String categoryName = intent.getStringExtra("categoryName");
            int year = intent.getIntExtra("year", 0);
            int mouth = intent.getIntExtra("mouth", 0);
            int day = intent.getIntExtra("day", 0);
            String money = intent.getStringExtra("money");
            String marks = intent.getStringExtra("marks");
            String photoUriString = intent.getStringExtra("photoUriString");
            PayoutContentInfo payouContentInfo = new PayoutContentInfo(id, resourceID, categoryName, year, mouth, day, money, marks, photoUriString);
            Log.i(tag, payouContentInfo.toString());
            Log.i("kkkjjjhj", "fggggf");
            ReportFormPager reportFormPager = (ReportFormPager) pageList.get(2);
            reportFormPager.refreshPauout(payouContentInfo);
            Log.i("kkkjjjhj", reportFormPager.toString());
            //super无法执行到
            // return;
        } else if (resultCode == 444) {

        } else if (requestCode == PHOTO_REQUEST_GALLERY) {
            //去图库获取到的数据
            if (resultCode == RESULT_OK) {
                if (intent != null) {
                    if (intent.hasExtra("data")) {

                        Bitmap bitmap = intent.getParcelableExtra("data");
                    }
                    //获取图片的全路径uri
                    photoUri = intent.getData();
                    Log.i("wwwwwwww", "调用图库  uri=" + photoUri);

                } else {
                    return;
                }
            }
        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            //照相
            if (resultCode == RESULT_OK) {
            }
        } else if (requestCode == 111) {
            Log.i("wwwwwwww", "添加愿望onActivityResult---requestCode==111");
            if (resultCode == 112) {
                Log.i("wwwwwwww", "添加愿望onActivityResult---resultCode==112");
                Intent wishintent = getIntent();
                boolean hasaddwish = wishintent.getBooleanExtra("hasaddwish", false);
                Log.i("wwwwww", "hasaddwish===" + hasaddwish);

                if (hasaddwish) {
                    pageList.get(1).initView();
                    Log.i("wwwwww", "mainActivity_contentAdapter==notifyDataSetChanged");

                }
            }
        } else if (requestCode == PHOTO_REQUEST_CAREMA_FROM_ACCOUNT||requestCode==PHOTO_REQUEST_GALLERY_FROM_ACCOUNT) {

            Uri result = intent.getData();
            if (result == null) {

                if (intent.hasExtra("data")) {

                    Bitmap bitmap = intent.getParcelableExtra("data");
                    result = ScaleBitmapUtils.scaleBitmap(bitmap, DensityUtil.dip2px(this, 200), DensityUtil.dip2px(this, 200));
                    bitmap.recycle();

                    Log.i("lalala","1212121");
                }
            }

            Intent leapAddRecordActivityIntent = new Intent(this, AddRecordActivity.class);
            leapAddRecordActivityIntent.putExtra("photoUriString", result.toString());
            startActivityForResult(leapAddRecordActivityIntent, 50);
            super.onActivityResult(requestCode, resultCode, intent);
        }

    }
}
