package com.example.xuversion.class_table_activity;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.xuversion.R;
import com.example.xuversion.RealmHelper;
import com.example.xuversion.model.ClassInfo;

import java.util.List;



public class ClassView extends ScrollView{
    private ClassLine[] classLine;
    private LinearLayout layout;
    private int day;
    private RealmHelper realmHelper;
    private List<ClassInfo> classInfos;

    /**
     * The view for one day's class
     * @param context context object for the day
     * @param day the day name
     * @param realmHelper the realmhelper
     */
    public ClassView(final Context context, RealmHelper realmHelper,int day) {
        super(context);
        this.day = day;
        this.realmHelper = realmHelper;
        initial(context);
        addView(layout);
        setVerticalScrollBarEnabled(false);

    }

    /**
     * Set dialog for each line of class in class of the day
     * @param classChoiceDialog ClassChoiceDialog object that want to set
     */
    public void setInfoWindow(ClassChoiceDialog classChoiceDialog){
         for(int i=0;i<6;i++){
             classLine[i].setInfowindow(classChoiceDialog);
             classLine[i].setListener();
         }
    }

    /**
     * Initialize the context for this class view
     * @param context the context object
     */
    public void initial(Context context){
        classLine = new ClassLine[6];
        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(layoutParams);
        for(int i=0;i<6;i++){
            classLine[i] = new ClassLine(context);
            classLine[i].setDajie(i+1);
            classLine[i].setWeekday(day);
            layout.addView(classLine[i]);
        }
        updateInfo();
    }

    /**
     * Class info setter
     * @param classInfos the class info that you want to set
     */
    public void setClassInfos(List<ClassInfo> classInfos) {
        this.classInfos = classInfos;
    }

    /**
     * Update the information
     */
    public void updateInfo(){
        classInfos = realmHelper.queryClassByDay(day);
        ClassInfo result[] = new ClassInfo[6];
        for(ClassInfo classInfo: classInfos){
            result[classInfo.getLecture()-1] = classInfo;
        }

        for(int i=0;i<classLine.length;i++){
            if(result[i] == null){
                classLine[i].setText("无","无","无","无");
                classLine[i].setBackgroundColor(0xBFd0d0d0);
            }else {
                classLine[i].setText(result[i].getName(),result[i].getProfessor(),result[i].getClassroom(),result[i].getTeachingweek());
                classLine[i].setBackground(getResources().getDrawable(R.drawable.classbackground));
            }

        }
    }

}
