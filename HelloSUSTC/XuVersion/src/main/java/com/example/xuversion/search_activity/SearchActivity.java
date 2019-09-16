package com.example.xuversion.search_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.example.xuversion.RealmHelper;
import com.example.xuversion.model.Building;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import io.realm.Realm;

public class SearchActivity extends AppCompatActivity{

    @BindView(R.id.search_back)
    ImageView backBtn;
    @BindView(R.id.search_edit)
    EditText searchContentEt;   //搜索框
    @BindView(R.id.search_content_cancel_tv)
    ImageView cancel;
    @BindView(R.id.search_btn)
    TextView searchBtn;
    @BindViews({R.id.search_study,R.id.search_work,R.id.search_sport,R.id.search_live})
    List<TextView> tabList;
    @BindView(R.id.search_records)
    ListView searchRecords;
    @BindView(R.id.clear_all_records_tv)
    TextView clearAllRecordsTv;
    @BindView(R.id.search_process_bar)
    ProgressBar progressBar;

    /**
     * Constructor
     * @param parent parent AdapterView
     * @param view The view
     * @param position The position
     * @param id id
     */
    @OnItemClick(R.id.search_records)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        searchAction(searchRecordsList.get(position).getName());
    }

    /**
     * OnClick Method
     * @param v The view that need to be bound with OnClick event
     */
    //四个tab
    @OnClick({R.id.search_back,R.id.search_content_cancel_tv,R.id.search_btn,R.id.clear_all_records_tv,
            R.id.search_study, R.id.search_work,R.id.search_sport,R.id.search_live})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_back:
                this.finish();
                break;
            case R.id.search_content_cancel_tv:
                searchContentEt.setText("");
                cancel.setVisibility(View.INVISIBLE);
                break;
            case R.id.search_btn:
                //搜索
                searchAction(searchContentEt.getText().toString());
                break;
            case R.id.clear_all_records_tv:
                //清除历史记录
                doDeleteRecord();
                break;
                // tab list
            case R.id.search_study:
                doTabClick("study");
                break;
            case R.id.search_work:
                doTabClick("work");
                break;
            case R.id.search_sport:
                doTabClick("sport");
                break;
            case R.id.search_live:
                doTabClick("life");
                break;
            default:
                break;
        }
    }



    private static final String GETBUILDING = "getAll";
    private SearchRecordsAdapter recordsAdapter;  //自定义Adapter
    private List<Building> searchRecordsList; //真正的list
    private Realm realm;
    private RealmHelper realmHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm);
        initListView();
        initEditTextListener();
    }

    private void doDeleteRecord(){
        //删除数据库所有的历史记录
        realmHelper.deleteAllHistory();
        searchRecordsList.clear();
        recordsAdapter.notifyDataSetChanged();
    }

    private void openListView(){
        MyApplication.getInstance().setGetALLBuilding(true);
        progressBar.setVisibility(View.GONE);
        searchRecords.setVisibility(View.VISIBLE);
    }

    private void initListView(){
        //bindAdapter
        searchRecordsList = new ArrayList<>();
        recordsAdapter = new SearchRecordsAdapter(this, searchRecordsList);
        searchRecords.setAdapter(recordsAdapter);
        if(MyApplication.getInstance().isGetALLBuilding()){
            openListView();
            initData();
        }
        else {
            getBuilding();
        }
    }

    private void getBuilding() {
        String url = MyApplication.getUrl()+"/building/getAll";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {//s为请求返回的字符串数据
                        try{
                            JSONArray jsonArray = new JSONArray(s);
                            realmHelper.addBuildingByJSONArray(jsonArray);
                            initData();
                            openListView();
                        }catch (Exception e){
                            showToast(e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast(volleyError.toString());
                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(8000,0,0f));
        request.setTag(GETBUILDING);
        MyApplication.getInstance().getHttpQueues().add(request);
    }

    private void doTabClick(String type){
        searchRecordsList.clear();
        searchRecordsList.addAll(realmHelper.queryByType(type));
        recordsAdapter.notifyDataSetChanged();
        clearAllRecordsTv.setVisibility(View.GONE);
    }


    private void initData() {
       searchRecordsList.addAll(realmHelper.queryAllHistory());
       recordsAdapter.notifyDataSetChanged();
    }



    private void searchAction(String record) {
        hideKeyboard(getCurrentFocus().getWindowToken());   //收起键盘
        if (record.length() == 0) {
            showToast("不能为空");
            return;
        }

        //将搜索记录保存至数据库中
        if (realmHelper.iscontainBuilding(record)) {
            realmHelper.addHistory(record);
            showToast(record);

            Intent it = new Intent();
            it.putExtra("clickbuilding", record);
            setResult(RESULT_OK, it);
            finish();
        } else {
            showToast("没有这个地点");
        }

    }



    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private void initEditTextListener() {
        //输入框搜索响应
        searchContentEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchAction(searchContentEt.getText().toString());
                return false;
            }
        });

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
                    searchRecordsList.clear();
                    searchRecordsList.addAll(realmHelper.querySimilarBuilding(tempName));
                    clearAllRecordsTv.setVisibility(View.GONE);
                } else {
                    cancel.setVisibility(View.INVISIBLE);
                    searchRecordsList.clear();
                    searchRecordsList.addAll(realmHelper.queryAllHistory());
                    clearAllRecordsTv.setVisibility(View.VISIBLE);
                }
                recordsAdapter.notifyDataSetChanged();
            }
        });


    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getInstance().getHttpQueues().cancelAll(GETBUILDING);
    }
}
