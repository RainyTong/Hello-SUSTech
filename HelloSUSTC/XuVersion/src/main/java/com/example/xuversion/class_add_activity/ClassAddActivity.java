package com.example.xuversion.class_add_activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.xuversion.MyApplication;
import com.example.xuversion.R;
import com.example.xuversion.model.ClassInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class ClassAddActivity extends AppCompatActivity {
    @BindView(R.id.layout_return)
    ImageView returnbtn;
    @BindView(R.id.layout_name)
    TextView titleName;
    @BindView(R.id.class_go)
    EditText searchContentEt;
    @BindView(R.id.class_search_cancel_tv)
    ImageView cancel;
    @BindView(R.id.class_search_records)
    ListView recordsListLv;
    @BindView(R.id.class_add_bar)
    ProgressBar classAddBar;

    /**
     * bind click event
     * @param view the view that need to bind
     */
    @OnClick({R.id.layout_return, R.id.class_search_cancel_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_return:
                this.finish();
                break;
            case R.id.class_search_cancel_tv:
                searchContentEt.setText("");
                cancel.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * bind click event
     * @param parent the parent view
     * @param view current view
     * @param position the positon of the item
     * @param id the id
     */
    @OnItemClick(R.id.class_search_records)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyApplication.getInstance().getClassInfo().setId(
                searchRecordsList.get(position).getId()); //此处是服务器ID
        MyApplication.getInstance().getClassInfo().setClassroom(
                searchRecordsList.get(position).getClassroom());
        MyApplication.getInstance().getClassInfo().setProfessor(
                searchRecordsList.get(position).getProfessor());
        MyApplication.getInstance().getClassInfo().setName(
                searchRecordsList.get(position).getName());
        MyApplication.getInstance().getClassInfo().setTeachingweek(
                searchRecordsList.get(position).getTeachingweek());

        Intent it = new Intent();
        setResult(RESULT_OK, it);
        finish();
    }






    private ClassUpdateAdapter recordsAdapter;  //自定义Adapter
    private List<ClassInfo> searchRecordsList; //真正的list
    private List<ClassInfo> useList;
    private int weekday;
    private int lecture;

    private static final String GETCLASS = "getclass";

    /**
     * onCreate method
     * @param savedInstanceState the last state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_update);
        ButterKnife.bind(this);
        initView();
        getClassFromInternet();
        initAutoUpateEditTextListener();
    }

    private void initView() {
        searchRecordsList = new ArrayList<>();
        useList = new ArrayList<>();
        recordsAdapter = new ClassUpdateAdapter(this, useList);
        recordsListLv.setAdapter(recordsAdapter);
        weekday = MyApplication.getInstance().getClassInfo().getWeekday();
        lecture = MyApplication.getInstance().getClassInfo().getLecture();
        LectureTime lectureTime = ClassUtil.getLectureTime(lecture);
        titleName.setText(String.format("%s (%s)",ClassUtil.getWeekDay(weekday),lectureTime.getDajie()));
    }


    //传来json串
    private void initClassFromInternet(String getFromInternet) {
        try {
            JSONArray listArray = new JSONArray(getFromInternet);
            for (int i = 0; i < listArray.length(); i++) {
                JSONObject list_item = listArray.getJSONObject(i);

                int id = list_item.getInt("id");
                int lecture = list_item.getInt("lecture");
                int weekday = list_item.getInt("weekday");

                String name = list_item.getString("name");
                String classroom = list_item.getString("classroom");
                String teacher = list_item.getString("professor");
                String teachingweek = list_item.getString("teachingweek");
                searchRecordsList.add(new ClassInfo(id, lecture, weekday, name, classroom, teacher, teachingweek));
                useList.add(new ClassInfo(id, lecture, weekday, name, classroom, teacher, teachingweek));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getClassFromInternet() {
        //Logon network request
        String url = MyApplication.getUrl() + "/class/get?weekday=" + weekday + "&lecture=" + lecture;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        initClassFromInternet(s);
                        recordsAdapter.notifyDataSetChanged();
                        classAddBar.setVisibility(View.GONE);
                        recordsListLv.setVisibility(View.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast(volleyError.toString());
                    }
                });
        //Set the network timeout
        request.setRetryPolicy(new DefaultRetryPolicy(8000, 0, 0f));
        request.setTag(GETCLASS);
        MyApplication.getInstance().getHttpQueues().add(request);

    }


    private void initAutoUpateEditTextListener() {

        //根据输入的信息去模糊搜索
        searchContentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tempName = searchContentEt.getText().toString();
                if (!tempName.equals("")) {
                    cancel.setVisibility(View.VISIBLE);
                    useList.clear();
                    useList.addAll(dothesearch(tempName));

                } else {
                    cancel.setVisibility(View.INVISIBLE);
                    useList.clear();
                    useList.addAll(searchRecordsList);

                }
                recordsAdapter.notifyDataSetChanged();
            }
        });
        //清除键
        cancel.setVisibility(View.INVISIBLE);

    }

    //模糊搜索
    private List<ClassInfo> dothesearch(String tempName) {
        List<ClassInfo> resultList = new ArrayList<>();
        for (ClassInfo classInfo : searchRecordsList) {
            if (classInfo.isAResult(tempName)) {
                resultList.add(classInfo);
            }
        }
        return resultList;
    }


    private void showToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * bind Onclick event
     */
    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getInstance().getHttpQueues().cancelAll(GETCLASS);

    }

}
