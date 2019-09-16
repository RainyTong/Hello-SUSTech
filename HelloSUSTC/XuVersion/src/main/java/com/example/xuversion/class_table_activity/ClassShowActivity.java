package com.example.xuversion.class_table_activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.xuversion.MyApplication;
import com.example.xuversion.R;
import com.example.xuversion.RealmHelper;
import com.example.xuversion.SharedUtil;
import com.example.xuversion.class_add_activity.ClassAddActivity;
import com.example.xuversion.model.ClassInfo;
import com.example.xuversion.model.Status;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;


public class ClassShowActivity extends AppCompatActivity {

    @BindView(R.id.layout_return)
    ImageView layoutReturn;
    @BindView(R.id.layout_name)
    TextView layoutName;
    @BindView(R.id.class_vp_content)
    ViewPager viewPager;
    @BindView(R.id.show_progress)
    ProgressBar showProgress;

    /**
     * bing onclick event
     */
    @OnClick(R.id.layout_return)
    public void onViewClicked() {
        ClassShowActivity.this.finish();
    }

    private ClassViewAdapter pageAdapter;

    private ArrayList<ClassView> mViewList;
    private ClassChoiceDialog classChoiceDialog;

    private Realm realm;
    private RealmHelper realmHelper;
    private static final int REQUST_CLASS_ADD = 2;
    private static final int POST_MODE = 0;
    private static final int DELETE_MODE = 1;


    private static final String POST_CLASS = "postclass";
    private static final String DELETE_CLASS = "deleteclass";
    private static final String ALL_CLASS = "allclass";
    /**
     * onCreat function, initialiaze the activity
     *
     * @param savedInstanceState last state instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_show);
        ButterKnife.bind(this);
        classChoiceDialog = new ClassChoiceDialog(this);
        realm = Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm);
        layoutName.setText("2019Summer");
        initialView();
        adddelete();
        addupdate();

    }
    private void initialView(){
        if(MyApplication.getInstance().isGetUserClass()){
            initalPagerAdapter();
            initalViewPager();
            showClass();
        }else {
            getUserClassFromInternet();
        }
    }

    //获取课表的操作
    private void getUserClassFromInternet(){
        String name = SharedUtil.getIntance(ClassShowActivity.this).readShared("name", "none");
        String url = MyApplication.getUrl() + "/user/class_table?username="+name;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try{
                            JSONArray jsonArray = new JSONArray(s);
                            realmHelper.addClassByJSONArray(jsonArray);
                            initalPagerAdapter();
                            initalViewPager();
                            showClass();
                            MyApplication.getInstance().setGetALLBuilding(true);
                        }catch (Exception e){
                            showToast(e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast("获取失败：" + volleyError.toString());
                    }
                });
        //Set the network timeout
        request.setRetryPolicy(new DefaultRetryPolicy(8000, 0, 0f));
        request.setTag(ALL_CLASS);
        MyApplication.getInstance().getHttpQueues().add(request);
    }


    private void showClass(){
        showProgress.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
    }

    private void hideClass(){
        showProgress.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
    }


    private void initalPagerAdapter() {
        mViewList = new ArrayList<ClassView>();
        //初始化mViewList
        for (int i = 0; i < 7; i++) {
            ClassView classView = new ClassView(this, realmHelper, i + 1);
            classView.setInfoWindow(classChoiceDialog);
            classView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            mViewList.add(classView);
        }
        pageAdapter = new ClassViewAdapter(this, mViewList);
    }


    private void initalViewPager() {
        viewPager = (ViewPager) findViewById(R.id.class_vp_content);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0);
        //美化标题栏
        PagerTabStrip pts_tab = (PagerTabStrip) findViewById(R.id.class_tab);
        pts_tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        pts_tab.setTextColor(Color.BLUE);
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void adddelete() {

        classChoiceDialog.getClassClear().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (realmHelper.isContainClass(MyApplication.getInstance().getClassInfo())) {
                    hideClass();
                    ClassInfo classInfo = realmHelper.getClass(MyApplication.getInstance().getClassInfo());
                    MyApplication.getInstance().getClassInfo().setId(classInfo.getId());
                    deleteClass();
                } else {
                    showToast("no class to delete");
                }

            }
        });

    }

    private void addupdate() {

        classChoiceDialog.getClassUpdate().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (realmHelper.isContainClass(MyApplication.getInstance().getClassInfo())) {
                    showToast("delete class first");
                } else {
                    Intent it = new Intent(ClassShowActivity.this, ClassAddActivity.class);
                    startActivityForResult(it, REQUST_CLASS_ADD);
                }
                classChoiceDialog.toMiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUST_CLASS_ADD && resultCode == RESULT_OK) {
            //网络同步操作
            //数据库更新操作
            //更新ui
            hideClass();
            postClass();
        }
    }

    private void updateLocal() {
        ClassView classView = mViewList.get(MyApplication.getInstance().getClassInfo().getWeekday() - 1);
        classView.updateInfo();
        showClass();
    }

    private Status getStatusFromJson(String s) {
        try {
            JSONObject j = new JSONObject(s);
            return new Status(j.getBoolean("result"), j.getString("description"));
        } catch (Exception e) {
            return new Status(false, e.getMessage());
        }
    }

    private void deleteClass() {
        String url = MyApplication.getUrl() + "/user/class_table";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Status status = getStatusFromJson(s);
                        showToast(status.getDescription());
                        if (status.isResult()) { //成功
                            realmHelper.deleteClassByDay(MyApplication.getInstance().getClassInfo());
                            classChoiceDialog.toMiss();
                            updateLocal();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast("删除失败：" + volleyError.toString());
                        classChoiceDialog.toMiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String name = SharedUtil.getIntance(ClassShowActivity.this).readShared("name", "none");
                Map<String, String> map = new HashMap<>();
                map.put("username", name);
                map.put("class_id", MyApplication.getInstance().getClassInfo().getId() + "");
                map.put("mode", DELETE_MODE + "");
                return map;
            }
        };
        //Set the network timeout
        request.setRetryPolicy(new DefaultRetryPolicy(8000, 0, 0f));
        request.setTag(DELETE_CLASS);
        MyApplication.getInstance().getHttpQueues().add(request);
    }


    private void postClass() {
        String url = MyApplication.getUrl() + "/user/class_table";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Status status = getStatusFromJson(s);
                        showToast(status.getDescription());
                        if (status.isResult()) { //成功
                            realmHelper.addClass(MyApplication.getInstance().getClassInfo());
                            updateLocal();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast("更新失败：" + volleyError.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String name = SharedUtil.getIntance(ClassShowActivity.this).readShared("name", "none");
                Map<String, String> map = new HashMap<>();
                map.put("username", name);
                map.put("class_id", MyApplication.getInstance().getClassInfo().getId() + "");
                map.put("mode", POST_MODE + "");
                return map;
            }
        };
        //Set the network timeout
        request.setRetryPolicy(new DefaultRetryPolicy(8000, 0, 0f));
        request.setTag(POST_CLASS);
        MyApplication.getInstance().getHttpQueues().add(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getInstance().getHttpQueues().cancelAll(POST_CLASS);
        MyApplication.getInstance().getHttpQueues().cancelAll(DELETE_CLASS);
        MyApplication.getInstance().getHttpQueues().cancelAll(ALL_CLASS);
    }


}
