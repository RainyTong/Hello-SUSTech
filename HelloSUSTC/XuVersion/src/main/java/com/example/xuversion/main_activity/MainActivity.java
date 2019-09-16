package com.example.xuversion.main_activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.xuversion.MyApplication;
import com.example.xuversion.RealmHelper;
import com.example.xuversion.SharedUtil;
import com.example.xuversion.tensorflow_activity.TensorflowActivity;
import com.example.xuversion.calendar_activity.CalendarActivity;
import com.example.xuversion.class_table_activity.ClassShowActivity;
import com.example.xuversion.login_activity.LoginActivity;
import com.example.xuversion.R;
import com.example.xuversion.map_activity.MapActivity;
import com.example.xuversion.widget.MyImageButton;
import java.util.List;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.main_signin_btn)
    Button signInBtn;
    @BindView(R.id.main_user_layout)
    View userLayout;
    @BindView(R.id.main_signin_layout)
    View signinLayout;
    @BindView(R.id.main_user_name)
    TextView userNametv;
    @BindView(R.id.main_user_logout)
    TextView userLogOut;

    @BindViews({R.id.main_xiaoLi_button,R.id.main_map_button,R.id.main_building_button,R.id.main_class_button})
    List<MyImageButton> myImageButtons;




    private Realm realm;
    private RealmHelper realmHelper;

    private static final String[] TAB_TEXT = new String[]{"school calendar","school map",
                                                          "identify building","class table"};
    private static final int[] ICON = new int[]{R.drawable.calendar,R.drawable.school_map,R.drawable.camera,R.drawable.class_table};
    private static final int LOGIN_FIRST = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm);
        signInBtn.setOnClickListener(this);
        userLogOut.setOnClickListener(this);
        initImageButton();
    }

    private void initImageButton(){
        //set text
        for(int i=0;i<myImageButtons.size();i++){
            MyImageButton myImageButton = myImageButtons.get(i);
            setWeight(myImageButton);
            myImageButton.setText(TAB_TEXT[i]);
            myImageButton.setOnClickListener(this);
            myImageButton.setBackground(getDrawable(ICON[i]));
            //set image
        }
    }

    private void setWeight(MyImageButton btn){
        ViewGroup.LayoutParams l = btn.getLayoutParams();
        l.width = 0;
        l.height = (getScreenWidth()-12)/2;
    }

    /**
     * Get the screen width
     * @return the width
     */
    public int getScreenWidth() {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        return point.x;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_signin_btn:
                Intent it1 = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(it1);
                break;
            case R.id.main_user_logout:
                askLogout("Are you sure to log out??");
                break;
            case R.id.main_xiaoLi_button:
                Intent it2 = new Intent(MainActivity.this,CalendarActivity.class);
                startActivity(it2);
                break;
            case R.id.main_map_button:
                Intent it3 = new Intent(MainActivity.this,MapActivity.class);
                startActivity(it3);
                break;
            case R.id.main_building_button:
                Intent it4 = new Intent(MainActivity.this,TensorflowActivity.class);
                startActivity(it4);
                break;
            case R.id.main_class_button:
                toClassShow();
                break;
            default:
                break;
        }
    }
    private void toClassShow(){
        String name = SharedUtil.getIntance(MainActivity.this).readShared("name",null);
        if(name == null) {
            Toast.makeText(MainActivity.this, "请先登录账号", Toast.LENGTH_LONG).show();
            Intent it = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(it,LOGIN_FIRST);
        }
        else{
            Intent it = new Intent(MainActivity.this,ClassShowActivity.class);
            startActivity(it);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_FIRST && resultCode == RESULT_OK) {
            Intent it = new Intent(MainActivity.this,ClassShowActivity.class);
            startActivity(it);
        }
    }


    private void showToast(String text){
        Toast toast =  Toast.makeText(this,text,Toast.LENGTH_SHORT);
        toast.show();
    }

    private void askLogout(final String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedUtil.getIntance(MainActivity.this).removeShared("name");
                //注销
                realmHelper.deleteAllClass();
                MyApplication.getInstance().setGetUserClass(false);
                showToast("Log out successfully");
                userLayout.setVisibility(View.GONE);
                signinLayout.setVisibility(View.VISIBLE);
            }
        });
        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //进来先看有无用户
        String username = SharedUtil.getIntance(this).readShared("name", "none");
        if(!username.equals("none")){
            userLayout.setVisibility(View.VISIBLE);
            signinLayout.setVisibility(View.GONE);
            userNametv.setText(username);
            //初始化忘记密码下划线
            userLogOut.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            userLogOut.getPaint().setAntiAlias(true);//抗锯齿
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


}
