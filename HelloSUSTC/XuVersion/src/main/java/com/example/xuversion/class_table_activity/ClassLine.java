package com.example.xuversion.class_table_activity;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xuversion.MyApplication;
import com.example.xuversion.class_add_activity.ClassUtil;
import com.example.xuversion.class_add_activity.LectureTime;
import com.example.xuversion.model.ClassInfo;


public class ClassLine extends LinearLayout {
    private LinearLayout leftLayout;
    private LinearLayout rightLayout;
    private TextView dajie;
    private TextView shijian;
    private TextView classname;
    private TextView classroom;
    private TextView classlong;
    private TextView teacher;
    private ClassChoiceDialog infowindow;
    private int classseq;
    private int weekday;

    private void setleftLayout(Context context){
        leftLayout = new LinearLayout(context);
        leftLayout.setPadding(12,20,12,20);
        leftLayout.setGravity(Gravity.CENTER_VERTICAL);
        leftLayout.setBackgroundColor(0x7FB29D72);
        dajie = new TextView(context);
        shijian = new TextView(context);
        leftLayout.setOrientation(VERTICAL);
        leftLayout.addView(dajie);
        leftLayout.addView(shijian);
        dajie.setText("一、二节");
        leftset(dajie);
        shijian.setText("14:30-15:30");
        leftset(shijian);

    }

    /**
     * set information window with a dialog
     * @param classChoiceDialog the dialog that we want to set
     */
    public void setInfowindow(ClassChoiceDialog classChoiceDialog){
        this.infowindow = classChoiceDialog;
    }

    /**
     * Set the listener for each line of class
     */
    public void setListener(){
        setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                ClassInfo c = new ClassInfo(classseq,weekday);
                MyApplication.getInstance().setClassInfo(c);
                //改变dialog
                infowindow.setClassDajie(c.getLecture());
                infowindow.setClassWeek(c.getWeekday());
                infowindow.toShow();
                return true;
            }
        });

    }

    /**
     * set which day is for this class
     * @param day day name
     */
    public void setWeekday(int day){
        weekday = day;
    }

    /**
     * set which class time slot is for this class
     * @param i representing which time slot
     */
    public void setDajie(int i){
        classseq = i;
        LectureTime lectureTime = ClassUtil.getLectureTime(i);
        dajie.setText(lectureTime.getDajie());
        shijian.setText(lectureTime.getTime());
    }

    /**
     * Set other information for the class
     * @param name subject name
     * @param teachername teacher name
     * @param learnTime classroom
     * @param weektimes which week
     */
    public void setText(String name,String teachername,String learnTime,String weektimes){
        classname.setText("课程："+name);
        teacher.setText("授课教师："+teachername);
        classroom.setText("教室："+learnTime);
        classlong.setText("周数: "+weektimes);
    }

    private void leftset(TextView r){
        r.setTextSize(15);
        r.setGravity(Gravity.CENTER);
        r.setTextColor(Color.WHITE);
        r.setPadding(10,5,10,5);
    }

    private void rightset(TextView r){
        r.setTextSize(12);
        r.setTextColor(0xFFB39E72);
        r.setPadding(20,5,15,5);
    }

    private void setRightLayout(Context context){
        rightLayout = new LinearLayout(context);
        classname = new TextView(context);
        classroom = new TextView(context);
        teacher = new TextView(context);
        classlong = new TextView(context);

        rightLayout.setPadding(40,8,10,8);
        rightLayout.setGravity(Gravity.LEFT);
        rightLayout.setOrientation(VERTICAL);
        rightLayout.addView(classname);
        rightLayout.addView(teacher);
        rightLayout.addView(classroom);
        rightLayout.addView(classlong);
        classname.setText("课程：计算机网络");
        teacher.setText("授课教师：张进");
        classroom.setText("教室：荔园");
        classlong.setText("周数: 1-16");
        rightset(classname);
        rightset(teacher);
        rightset(classroom);
        rightset(classlong);
    }

    /**
     * Constructor, this contains the content for each class
     * @param context the Context object
     */
    public ClassLine(final Context context) {
        super(context);
        setleftLayout(context);
        setRightLayout(context);
        addView(leftLayout);
        addView(rightLayout);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        leftLayout.setLayoutParams(layoutParams);
        LayoutParams classlineParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        classlineParams.setMargins(0,20,0,20);
        setLayoutParams(classlineParams);
    }
}
