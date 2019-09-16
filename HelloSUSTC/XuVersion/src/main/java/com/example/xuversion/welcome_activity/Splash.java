package com.example.xuversion.welcome_activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.xuversion.MyApplication;
import com.example.xuversion.R;
import com.example.xuversion.RealmHelper;

import java.util.List;

import io.realm.Realm;
import pub.devrel.easypermissions.EasyPermissions;

public class Splash extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private int[] lanuchImageArray = {
            R.drawable.mor, R.drawable.noon, R.drawable.moon, R.drawable.cat};

    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION};
    private Realm realm;
    private RealmHelper realmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch);
        ViewPager vp_launch = (ViewPager) findViewById(R.id.vp_launch);
        LaunchImproveAdapter mAdapter = new LaunchImproveAdapter(getSupportFragmentManager(), lanuchImageArray);
        vp_launch.setAdapter(mAdapter);
        vp_launch.setCurrentItem(0);
        getPermission();
        realm = Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm);
        //删除缓存数据
        realmHelper.deleteAllClass();
        realmHelper.deleteAllBuilding();
        MyApplication.getInstance().setGetUserClass(false);
        MyApplication.getInstance().setGetALLBuilding(false);
    }

    //获取权限
    private void getPermission() {
        if (!EasyPermissions.hasPermissions(this, permissions)) {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的存储、照相、定位使用权限", 1, permissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //框架要求必须这么写
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //成功打开权限
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        showToast("相关权限获取成功");
    }

    //用户未同意权限
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        showToast("请同意相关权限，否则功能无法使用");
    }

    private void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
