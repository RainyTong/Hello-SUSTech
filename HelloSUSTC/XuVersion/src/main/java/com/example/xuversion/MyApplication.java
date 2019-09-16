package com.example.xuversion;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.xuversion.model.ClassInfo;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application{
    private RequestQueue queues ;
    private static String url = "http://10.21.84.93:8080"; //120.76.60.146 10.21.84.93
    private static MyApplication mApp;
    private boolean getALLBuilding;
    private boolean getUserClass;
    /** object of class information*/
    public ClassInfo classInfo;
    /**
     * Constructor
     */
    @Override
    public void onCreate() {
        super.onCreate();
        if(mApp == null){
            mApp = this;
        }
        getALLBuilding = false;
        getUserClass = false;
        queues = Volley.newRequestQueue(getApplicationContext());
        Realm.init(this);
        RealmConfiguration config = new  RealmConfiguration.Builder()
                .name("sustc.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    /**
     * use the singleton pattern to get a MyApplication instance
     * @return MyApplication instance
     */
    public static MyApplication getInstance(){
        return mApp;
    }

    /**
     * get volley RequsetQueue
     * @return RequsetQueue
     */
    public RequestQueue getHttpQueues() {
        return queues;
    }

    /**
     * get a static url
     * @return url
     */
    public static String getUrl(){
        return url;
    }

    /**
     * Is Get all buildings
     * @return true or false
     */
    public boolean isGetALLBuilding() {
        return getALLBuilding;
    }

    /**
     * set all buildings
     * @param getALLBuilding true or false
     */
    public void setGetALLBuilding(boolean getALLBuilding) {
        this.getALLBuilding = getALLBuilding;
    }

    /**
     * getUserClass getter
     * @return getUserClass
     */
    public boolean isGetUserClass() {
        return getUserClass;
    }

    /**
     * getUserClass setter
     * @param getUserClass getUserClass
     */
    public void setGetUserClass(boolean getUserClass) {
        this.getUserClass = getUserClass;
    }
}



