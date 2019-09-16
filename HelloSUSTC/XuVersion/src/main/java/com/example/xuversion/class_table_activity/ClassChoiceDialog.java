package com.example.xuversion.class_table_activity;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.xuversion.R;
import com.example.xuversion.class_add_activity.ClassUtil;
import com.example.xuversion.class_add_activity.LectureTime;


public class ClassChoiceDialog {
    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private TextView class_cancel;
    private TextView class_clear;
    private TextView class_update;
    private TextView class_week;
    private TextView class_dajie;
    private View view;

    /**
     * Show the choice when long press the class
     * @param context the context that shown on
     */
    public ClassChoiceDialog(Context context) {
        // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        this.context = context;
        initialView();
        dialog = builder.create();
        initListener();

    }

    private void initialbuilder() {
        // 设置我们自己定义的布局文件作为弹出框的Content
        builder = new AlertDialog.Builder(context);
        builder.setView(view);
    }

    private void initialView() {
        view = LayoutInflater.from(context).inflate(R.layout.class_choice_dialog, null);
        initialbuilder();
        class_cancel = (TextView) view.findViewById(R.id.classcancel);
        class_clear = (TextView) view.findViewById(R.id.classclear);
        class_update = (TextView) view.findViewById(R.id.classupdate);
        class_week = (TextView) view.findViewById(R.id.class_week);
        class_dajie = (TextView) view.findViewById(R.id.class_dajie);
    }

    /**
     * class clear getter
     * @return the class_clear
     */
    public TextView getClassClear() {
        return class_clear;
    }

    /**
     * Get the update view
     * @return update view of classes
     */
    public TextView getClassUpdate() {
        return class_update;
    }

    private void initListener() {
        //取消
        class_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    /**
     * Miss method
     */
    public void toMiss(){
        dialog.dismiss();
    }
    /**
     * set the week information into infowindow
     * @param weekday the weekday name
     */
    public void setClassWeek(int weekday) {
        class_week.setText(ClassUtil.getWeekDay(weekday));
    }

    /**
     * set the class information into infowindow
     * @param classseq class name index
     */
    public void setClassDajie(int classseq) {
        LectureTime lectureTime = ClassUtil.getLectureTime(classseq);
        class_dajie.setText(String.format("%s (%s)",lectureTime.getDajie(),lectureTime.getTime()));
    }

    /**
     * Show method
     */
    public void toShow() {
        //这个位置十分重要，只有位于这个位置逻辑才是正确的
        dialog.show();

    }
}
