package com.example.xuversion.class_add_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.xuversion.R;
import com.example.xuversion.model.ClassInfo;

import java.util.List;

public class ClassUpdateAdapter extends BaseAdapter {

    private List<ClassInfo> searchRecordsList;
    private LayoutInflater inflater;

    /**
     * constructor
     * @param context context
     * @param searchRecordsList search record list
     */
    public ClassUpdateAdapter(Context context, List<ClassInfo> searchRecordsList) {
        this.searchRecordsList = searchRecordsList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return searchRecordsList.size() == 0 ? 0 : searchRecordsList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchRecordsList.size() == 0 ? null : searchRecordsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null == convertView){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.internet_classview_item,null);
            viewHolder.classname = (TextView) convertView.findViewById(R.id.update_classname);
            viewHolder.teacher = (TextView) convertView.findViewById(R.id.update_teacher);
            viewHolder.classroom = (TextView) convertView.findViewById(R.id.update_classroom);
            viewHolder.weektimes = (TextView) convertView.findViewById(R.id.update_weektimes);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.classname.setText("课程："+searchRecordsList.get(position).getName());
        viewHolder.teacher.setText("授课教师："+searchRecordsList.get(position).getProfessor());
        viewHolder.classroom.setText("教室："+searchRecordsList.get(position).getClassroom());
        viewHolder.weektimes.setText("周次："+searchRecordsList.get(position).getTeachingweek());

        return convertView;
    }


    private static class ViewHolder {
        TextView classname;
        TextView teacher;
        TextView classroom;
        TextView weektimes;
    }
}

